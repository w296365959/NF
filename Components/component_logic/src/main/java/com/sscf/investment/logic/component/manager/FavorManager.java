package com.sscf.investment.logic.component.manager;

import BEC.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IFavorManager;
import com.sscf.investment.logic.component.entity.FavorOperationItem;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.db.FavorItem;
import com.sscf.investment.sdk.utils.NetUtil;
import com.dengtacj.thoth.MapProtoLite;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * davidwei
 * 收藏管理
 *
 */
public final class FavorManager extends BroadcastReceiver implements DataSourceProxy.IRequestCallback, Comparator<FavorItem>, Handler.Callback, IFavorManager {
    private static final String TAG = FavorManager.class.getSimpleName();

    /**
     * 资讯收藏版本号
     */
    public static final String KEY_FAVOR_NEWS_VERSION = "key_favor_news_version";

    // work线程的msg
    private static final int MSG_SEND_BROADCAST = 1;
    private static final int MSG_ADD_ITEM = 2;
    private static final int MSG_DELETE_ITEM = 3;
    private static final int MSG_LOAD_DATA_FROM_DB = 4;
    private static final int MSG_SYNC = 5;
    private static final int MSG_MODIFY_SYNC_STATE = 6;
    private static final int MSG_DELETE_ALL_ITEMS = 7;

    // ui线程的msg
    private static final int MSG_DESTROY_THREAD = 11;

    private Handler mWorkHandler;
    private boolean mRegistered;

    /**
     * 是否需要同步
     */
    private boolean mNeedSync = false;
    /**
     * 是否正在同步
     */
    private AtomicBoolean mSyncing = new AtomicBoolean(false);

    // 专门给显示使用的
    /**
     * 不要直接赋值，赋值请调用setFavorItems
     */
    private HashMap<String, FavorItem> mFavorItemsMap;
    private ArrayList<FavorItem> mFavorItems;

    // 需要后台同步的数据
    /**
     * 不要直接赋值，赋值请调用setFavorOperationItemsMap
     */
    private HashMap<String, FavorOperationItem> mFavorOperationItemsMap;

    public ArrayList<FavorItem> getFavorItems() {
        DtLog.d(TAG, "getFavorItems mFavorItems : " + mFavorItems);
        if (mFavorItems == null) {
            return mFavorItems;
        }
        final ArrayList<FavorItem> favorItems = new ArrayList<FavorItem>(mFavorItems);
        Collections.sort(favorItems, this);
        return favorItems;
    }

    private void setFavorItems(final ArrayList<FavorItem> favorItems) {
        if (favorItems == null) {
            mFavorItemsMap = null;
            mFavorItems = null;
        } else {
            final HashMap<String, FavorItem> favorItemsMap = new HashMap<String, FavorItem>(favorItems.size());
            for (FavorItem favorItem : favorItems) {
                favorItemsMap.put(favorItem.getFavorId(), favorItem);
            }
            mFavorItemsMap = favorItemsMap;
            mFavorItems = favorItems;
        }
    }

    private void setFavorOperationItemsMap(final ArrayList<FavorOperationItem> favorOperationItems) {
        if (favorOperationItems == null) {
            mFavorOperationItemsMap = null;
        } else {
            final HashMap<String, FavorOperationItem> favorOperationItemsMap = new HashMap<String, FavorOperationItem>(favorOperationItems.size());
            for (FavorOperationItem favorOperationItem : favorOperationItems) {
                favorOperationItemsMap.put(favorOperationItem.getFavorId(), favorOperationItem);
            }
            mFavorOperationItemsMap = favorOperationItemsMap;
        }
    }

    private void loadFromDB() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo == null) {
            return;
        }

        final ArrayList<FavorItem> favorItems = (ArrayList<FavorItem>) DBHelper.getInstance().findAllByWhere(FavorItem.class, "account_id = " + accountInfo.id, "favorTime desc");;
        setFavorItems(favorItems);

        final ArrayList<FavorOperationItem> favorOperationItems = (ArrayList) DBHelper.getInstance().findAll(FavorOperationItem.class);
        setFavorOperationItemsMap(favorOperationItems);

        DtLog.d(TAG, "loadFromDB favorItems.size() = " + (favorItems != null ? favorItems.size() : "null"));
    }

    public void init() {
        registerAccountReceiver();
        getWorkHandler().sendEmptyMessage(MSG_LOAD_DATA_FROM_DB);
    }

    public void destroy() {
        unregisterAccountReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        DtLog.d(TAG, "onReceive action : " + action);
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) { // 网络变化
            if (NetUtil.isNetWorkConnected(context)) {
                // 被动同步，需要同步的时候才去同步，根据mNeedSync的值判断是否需要同步
                sync();
            }
        } else if (CommonConst.ACTION_LOGIN_SUCCESS.equals(action)) {
            getWorkHandler().sendEmptyMessage(MSG_LOAD_DATA_FROM_DB);
        } else if(CommonConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
            // TODO 退出之后最后同步一次数据到后台，但是退出后没有id，ticket数据
            setFavorItems(null);
            setFavorOperationItemsMap(null);
            getWorkHandler().obtainMessage(MSG_DELETE_ALL_ITEMS, FavorItem.class).sendToTarget();
            getWorkHandler().obtainMessage(MSG_DELETE_ALL_ITEMS, FavorOperationItem.class).sendToTarget();
            SettingPref.putInt(KEY_FAVOR_NEWS_VERSION, 0);
        }
    }

    private void registerAccountReceiver() {
        if (!mRegistered) {
            mRegistered = true;
            final IntentFilter intentFilter = new IntentFilter(CommonConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(CommonConst.ACTION_LOGOUT_SUCCESS);
            final Context context = SDKManager.getInstance().getContext();
            LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter);
            context.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterAccountReceiver() {
        if (mRegistered) {
            final Context context = SDKManager.getInstance().getContext();
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            context.unregisterReceiver(this);
            mRegistered = false;
        }
    }

    public Handler getWorkHandler() {
        if (mWorkHandler == null) {
            final HandlerThread thread = new HandlerThread("favor_thread");
            thread.start();
            mWorkHandler = new Handler(thread.getLooper(), this);
        }
        return mWorkHandler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            // work线程的handler处理
            case MSG_SEND_BROADCAST:
                mWorkHandler.removeMessages(MSG_SEND_BROADCAST);
                LocalBroadcastManager.getInstance(SDKManager.getInstance().getContext())
                        .sendBroadcast(new Intent(CommonConst.ACTION_FAVOR_CHANGED));

                mWorkHandler.removeMessages(MSG_SYNC);
                mWorkHandler.sendEmptyMessageDelayed(MSG_SYNC, 10000L); // 10s后同步
                break;
            case MSG_ADD_ITEM:
                DBHelper.getInstance().add(msg.obj);
                break;
            case MSG_DELETE_ITEM:
                DBHelper.getInstance().delete(msg.obj);
                break;
            case MSG_DELETE_ALL_ITEMS:
                DBHelper.getInstance().deleteAll((Class) msg.obj);
                break;
            case MSG_LOAD_DATA_FROM_DB:
                loadFromDB(); // db里获得数据以后再和后台同步数据
                mWorkHandler.sendEmptyMessage(MSG_SYNC);
                break;
            case MSG_SYNC:
                mNeedSync = true;
                sync();
                mWorkHandler.removeMessages(MSG_SYNC);
                break;
            case MSG_MODIFY_SYNC_STATE:
                mSyncing.set(msg.arg1 > 0);
                break;

            // ui线程的handler处理
            case MSG_DESTROY_THREAD:
                if (mWorkHandler != null) {
                    mWorkHandler.getLooper().quit();
                    mWorkHandler = null;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public boolean isFavor(final FavorItem favorItem) {
        return mFavorItemsMap != null && favorItem != null && favorItem.equals(mFavorItemsMap.get(favorItem.getFavorId()));
    }

    public int getFavorCount() {
        return mFavorItems == null ? 0 : mFavorItems.size();
    }

    public void addFavor(final FavorItem favorItem) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo == null) {
            return;
        }

        if (addFavorItem(favorItem)) {
            addFavorOperationItem(favorItem, FavorOperationItem.ADD);
            sendFavorChangedBroadcast();
        }
    }

    /**
     * 添加favorItem到集合里
     * @param favorItem
     * @return 添加是否成功
     */
    private boolean addFavorItem(final FavorItem favorItem) {
        final String favorId = favorItem.getFavorId();

        if (mFavorItemsMap == null) {
            setFavorItems(new ArrayList<FavorItem>());
        } else {
            if (favorItem.equals(mFavorItemsMap.get(favorId))) { // 如果存在就不用添加
                return false;
            }
        }
        favorItem.setFavorTime(System.currentTimeMillis());
        mFavorItems.add(favorItem);
        mFavorItemsMap.put(favorId, favorItem);
        getWorkHandler().obtainMessage(MSG_ADD_ITEM, favorItem).sendToTarget();
        return true;
    }

    /**
     * 添加favorItem到操作的集合里，方便与后台同步
     * @param favorItem
     */
    private void addFavorOperationItem(final FavorItem favorItem, final int action) {
        if (mFavorOperationItemsMap == null) {
            setFavorOperationItemsMap(new ArrayList<FavorOperationItem>(0));
        }

        final String favorId = favorItem.getFavorId();

        FavorOperationItem favorOperationItem = mFavorOperationItemsMap.get(favorId);
        if (favorOperationItem == null) { // 集合里不存在就添加
            favorOperationItem = new FavorOperationItem(favorItem, action);
            mFavorOperationItemsMap.put(favorId, favorOperationItem);

            getWorkHandler().obtainMessage(MSG_ADD_ITEM, favorOperationItem).sendToTarget();
        } else {
            if (action != favorOperationItem.getAction()) {
                // 如果action与favorOperationItem.getAction()不相同
                // 例如，action是add，favorOperationItem.getAction()是delete，就需要从集合里去掉
                favorOperationItem = mFavorOperationItemsMap.remove(favorId);
                getWorkHandler().obtainMessage(MSG_DELETE_ITEM, favorOperationItem).sendToTarget();
            } // 如果action与favorOperationItem.getAction()相同，就什么都不做
        }
    }

    public void deleteFavor(final FavorItem favorItem) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo == null) {
            return;
        }

        if (deleteFavorItem(favorItem)) {
            addFavorOperationItem(favorItem, FavorOperationItem.DELETE);
            sendFavorChangedBroadcast();
        }
    }

    private void sendFavorChangedBroadcast() {
        final Handler workHandler = getWorkHandler();
        workHandler.removeMessages(MSG_SEND_BROADCAST);
        workHandler.sendEmptyMessageDelayed(MSG_SEND_BROADCAST, 1000L);
    }

    /**
     * 在集合里删除favorItem
     * @param favorItem
     * @return 删除是否成功
     */
    private boolean deleteFavorItem(final FavorItem favorItem) {
        final String favorId = favorItem.getFavorId();

        if (mFavorItemsMap != null && favorItem.equals(mFavorItemsMap.remove(favorId))) {
            mFavorItems.remove(favorItem);
            getWorkHandler().obtainMessage(MSG_DELETE_ITEM, favorItem).sendToTarget();
            return true;
        }

        return false;
    }

    // 同步分2步，1，把本地添加或删除的数据同步到后台，2，把后台的数据同步到本地，需要更新收藏时间
    private void sync() {
        if (!mNeedSync) {
            return;
        }

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo == null) {
            return;
        }

        if (!NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            return;
        }

        if (!mSyncing.get()) {
            mSyncing.set(true);
            final Handler handler = getWorkHandler();
            handler.sendMessageDelayed(handler.obtainMessage(MSG_MODIFY_SYNC_STATE, 0, 0), 30000L); // 防止同步超时

            if (mFavorOperationItemsMap != null && mFavorOperationItemsMap.size() > 0) {
                // 1，把本地添加或删除的数据同步到后台
                updateFavorsRequest(accountInfo.id, accountInfo.ticket, mFavorOperationItemsMap, this);
            } else {
                // 如果没有数据需要同步到后台，就直接拉取
                // 2，把后台的数据同步到本地，需要更新收藏时间
                final int version = SettingPref.getInt(KEY_FAVOR_NEWS_VERSION, 0);
                queryFavorsRequest(accountInfo.id, accountInfo.ticket, version, this);
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success || data.getEntity() == null ) {
            return;
        }

        switch (data.getEntityType()) {
            case EntityObject.ET_FAVOR_QUERY:
                queryCallback(success, (MapProtoLite) data.getEntity());
                break;
            case EntityObject.ET_FAVOR_UPDATE:
                updateCallback(success, (SaveFavorNewsRsp) data.getEntity());
                break;
            default:
                break;
        }
    }

    private void queryCallback(boolean success, MapProtoLite packet) {
        DtLog.d(TAG, "queryCallback success : " + success);
        if (success) {
            final int code = packet.read("", -1);
            DtLog.d(TAG, "queryCallback code : " + code);
            switch (code) {
                case E_FAVOR_RET.E_FR_SUCC:
                    final QueryFavorNewsRsp rsp = packet.read(NetworkConst.RSP, new QueryFavorNewsRsp());
                    handleQuery(rsp.stFavorList);
                    mNeedSync = false;
                    break;
                case E_FAVOR_RET.E_FR_NO_NEED_UPDATE:
                    // 已经是最新的了，无需更新
                    break;
                // ticket验证不过
                case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                    processTicketError();
                    break;
                default:
                    break;
            }
        } else {
            DtLog.d(TAG, "queryCallback failed");
        }
        getWorkHandler().removeMessages(MSG_MODIFY_SYNC_STATE);
        mSyncing.set(false);
    }

    private void processTicketError() {
        LocalBroadcastManager.getInstance(SDKManager.getInstance().getContext()).
                sendBroadcast(new Intent(CommonConst.ACTION_TICKET_ERROR));
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        accountManager.removeAccountInfo();
    }

    /**
     * 更新服务器到本地的返回
     */
    private void handleQuery(final FavorList favorList) {
        DtLog.d(TAG, "handleQuery favorList.iVersion : " + favorList.iVersion);

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo == null) {
            return;
        }

        SettingPref.putInt(KEY_FAVOR_NEWS_VERSION, favorList.iVersion);

        final ArrayList<FavorNews> favorNewses = favorList.vFavorNews;

        final long accountId = accountInfo.id;
        FavorItem favorItem = null;

        getWorkHandler().obtainMessage(MSG_DELETE_ALL_ITEMS, FavorItem.class).sendToTarget();

        final int size = favorNewses.size();
        final ArrayList<FavorItem> favorItems = new ArrayList<FavorItem>(size);
        if (size > 0) {
            for (FavorNews favorNews : favorNewses) {
                if (TextUtils.isEmpty(favorNews.sNewsID)) {
                    continue;
                }
                favorItem = favorNewsToFavorItem(favorNews, accountId);

                favorItems.add(favorItem);
                getWorkHandler().obtainMessage(MSG_ADD_ITEM, favorItem).sendToTarget();
            }
            Collections.sort(favorItems, this);
        }
        setFavorItems(favorItems);
    }

    /**
     * 保存本地收藏到服务器的返回处理
     * @param success
     */
    private void updateCallback(boolean success, SaveFavorNewsRsp rsp) {
        DtLog.d(TAG, "updateCallback success : " + success);
        if (success) {
            final int code = rsp.eRet;
            switch (code) {
                case E_FAVOR_RET.E_FR_SUCC:
                    // 更新到后台成功以后
                    // 1.清理本地的数据
                    setFavorOperationItemsMap(null);
                    getWorkHandler().obtainMessage(MSG_DELETE_ALL_ITEMS, FavorOperationItem.class).sendToTarget();
                    break;
                case E_FAVOR_RET.E_FR_ERROR:
                    break;
                // ticket验证不过
                case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                    processTicketError();
                    break;
                default:
                    break;
            }
        } else {
            DtLog.d(TAG, "updateCallback failed");
        }

        // 不管成功或失败
        // 2，把后台的数据同步到本地，需要更新收藏时间
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        final AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo == null) {
            return;
        }

        final int version = SettingPref.getInt(KEY_FAVOR_NEWS_VERSION, 0);
        queryFavorsRequest(accountInfo.id, accountInfo.ticket, version, this);
    }

    /**
     * 从后台获得数据的时候使用
     * @param favorNews
     * @param accountId
     * @return
     */
    private FavorItem favorNewsToFavorItem(final FavorNews favorNews, final long accountId) {
        final NewsDesc newsDesc = favorNews.stNewsDesc;
        final FavorItem favorItem = new FavorItem(accountId, newsDesc.sNewsID, newsDesc.eNewsType,
                newsDesc.sTitle, newsDesc.sDtInfoUrl,
                CommonConst.MILLIS_FOR_SECOND * newsDesc.iTime);
        favorItem.setThirdUrl(newsDesc.sThirdUrl);
        favorItem.setFavorTime(CommonConst.MILLIS_FOR_SECOND * favorNews.iTimeStamp);
        return favorItem;
    }

    private static void updateFavorsRequest(long accountId, byte[] ticket,
                                            HashMap<String, FavorOperationItem> operationItems, final DataSourceProxy.IRequestCallback observer) {
        final SaveFavorNewsReq req = new SaveFavorNewsReq();
        req.iAccountId = accountId;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);;
        req.vFavorAction = favorOperationItemsToFavorActions(operationItems);

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_FAVOR_UPDATE, req, observer);
    }

    /**
     * 数据更新到后台的时候使用
     */
    private static ArrayList<FavorAction> favorOperationItemsToFavorActions(HashMap<String, FavorOperationItem> operationItems) {
        final ArrayList<FavorAction> favorActions = new ArrayList<FavorAction>(operationItems.size());
        FavorAction favorAction = null;
        for (FavorOperationItem favorOperationItem : operationItems.values()) {
            final NewsDesc newsDesc = new NewsDesc();
            newsDesc.sNewsID = favorOperationItem.getFavorId();
            newsDesc.eNewsType = favorOperationItem.getFavorType();
            newsDesc.sDtSecCode = favorOperationItem.getDtSecCode();
            newsDesc.sTitle = favorOperationItem.getTitle();
            newsDesc.iTime = (int) (favorOperationItem.getPublishTime() / CommonConst.MILLIS_FOR_SECOND);
            newsDesc.sDtInfoUrl = favorOperationItem.getInfoUrl();
            newsDesc.sThirdUrl = favorOperationItem.getThirdUrl();

            final FavorNews favorNews = new FavorNews();
            favorNews.sNewsID = favorOperationItem.getFavorId();
            favorNews.eNewsType = favorOperationItem.getFavorType();
            favorNews.stNewsDesc = newsDesc;

            favorAction = new FavorAction();
            favorAction.eFavorAction = favorOperationItem.getAction();
            favorAction.stFavorNews = favorNews;

            favorActions.add(favorAction);
        }
        return favorActions;
    }

    private static void queryFavorsRequest(long accountId, byte[] ticket, int version, final DataSourceProxy.IRequestCallback observer) {
        final QueryFavorNewsReq req = new QueryFavorNewsReq();
        req.iAccountId =  accountId;
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(ticket);
        req.setStAccountTicket(accountTicket);
        req.iVersion = version;

        req.stUserInfo = SDKManager.getInstance().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_FAVOR_QUERY, req, observer);
    }

    @Override
    public int compare(FavorItem lhs, FavorItem rhs) {
        final long gap = rhs.getFavorTime() - lhs.getFavorTime();
        if (gap > 0) {
            return 1;
        } else if (gap < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
