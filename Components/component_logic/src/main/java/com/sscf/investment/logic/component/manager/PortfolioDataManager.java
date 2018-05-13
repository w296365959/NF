package com.sscf.investment.logic.component.manager;

import BEC.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnLocalDataUpdateCallback;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IDataCacheManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.logic.R;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.main.manager.CallbackManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.sdk.net.NetworkConst;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.BaseStockUtil;
import com.sscf.investment.sdk.utils.CharUtils;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.sdk.utils.DtLog;
import com.dengtacj.thoth.MapProtoLite;
import java.util.*;

/**
 * Created by xuebinliu on 2015/8/6.
 *
 * 自选数据管理，自选个股、自选主题
 *
 * 主要功能：
 * 对外提供自选数据的的查询、添加、删除、更改排序、修改提醒等
 * 内存、数据库、网络同步及管理
 *
 * 后台同步时机：
 * 1. 每次启动
 * 2. 用户账号变化
 * 3. 自选数据变化
 *
 * 同步流程
 * 1. query，更新合并服务器的数据
 * 2. 1返回成功，记录版本号
 * 3. 检查本地是否有变更
 * 4. 有变更则上传本地数据upload，成功返回保存返回版本号，清除本地变更
 * 5. 无变更结束
 *
 * 注意事项：用户删除一个自选，上报删除状态，后台返回成功之后，本地确认标记为删除状态，同步时需要同步所有状态的自选数据给后台。
 * 隐患：后台自选数据会越来越多，因为每次同步包含了已删除的自选。
 */
public final class PortfolioDataManager extends BroadcastReceiver implements IPortfolioDataManager {
    private static final String TAG = PortfolioDataManager.class.getSimpleName();

    // 当前分组ID
    public static final String CURRENT_GROUP_ID = "CURRENT_GROUP_ID";

    // 自选同步本地数据是否有变
    public static final String SETTING_PORTFOLIO_CHANGE = "setting_portfolio_change";

    // 自选同步版本号
    public static final String SETTING_PORTFOLIO_VERSION = "setting_portfolio_version";

    // SYNC_DELAY时长内无数据变动时才进行同步，降低同步频率
    private static final long SYNC_DELAY = 3000;

    // 所有自选股内存缓存（包括删除的）
    private Map<String, StockDbEntity> mStockMap = new HashMap<>();
    private final Object STOCK_LIST_LOCK = new Object();

    // 当前分组ID
    private int mCurrentGroupId = SYSTEM_GROUP_ID;

    // 用户分组缓存
    private Map<Integer, GroupEntity> mGroupMap = new HashMap<>();
    private final Object STOCK_GROUP_LOCK = new Object();

    // 后台线程，处理自选数据操作
    private Handler mBackgroundHandler;

    // 统计用户手工添加和删除股票个数
    public int mAddStockCount = 0;
    public int mDelStockCount = 0;

    private OnLocalDataUpdateCallback mOnDataUpdateCallback;

    public PortfolioDataManager() {
        DtLog.d(TAG, "PortfolioDataManager constructor");
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mBackgroundHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * 响应账号变化(如登入、登出)处理，重新加载自选数据
     */
    public void onAccountChange() {
        DtLog.d(TAG, "onAccountChange");
        // 切换账号，自选同步版本号归0
        SettingPref.putInt(SETTING_PORTFOLIO_VERSION, 0);
        mBackgroundHandler.post(() -> reloadData(true));
    }

    /**
     * 更新股票行情
     * @param secCodeList 指定需要更新的股票代码
     */
    public synchronized void updateQuoteFromServer(ArrayList<String> secCodeList) {
        if (secCodeList == null) {
            secCodeList = new ArrayList<>();
            List<StockDbEntity> stocks = getAllStockList(false, false);
            for (StockDbEntity entity : stocks) {
                secCodeList.add(entity.getDtSecCode());
            }
        }

        DtLog.d(TAG, "updateQuoteFromServer secCodeList size=" + secCodeList.size());

        if (secCodeList.size() > 0) {
            QuoteRequestManager.getSimpleQuoteRequest(secCodeList, mNetworkCallback, null);
        }
    }

    /**
     * 从数据库加载自选数据
     * 新用户登录时，需要把没有归属的自选添加到这个账号
     * 检查是否有需要同步的数据
     * @param isUserChange 是否新用户登录
     */
    @Override
    public void reloadData(boolean isUserChange) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            unregisterAccountReceiver();
            return;
        }

        registerAccountReceiver();

        final long accountId = accountManager.getAccountId();
        final String accountIdStr = String.valueOf(accountId);
        DtLog.d(TAG, "reloadData accountId=" + accountId);

        // 加载自选股票数据
        synchronized (STOCK_LIST_LOCK) {
            mStockMap.clear();
            List<StockDbEntity> stockList = loadStockFromDB(accountId);
            if (stockList != null) {
                DtLog.d(TAG, "reloadData from db uid=" + accountId + ", load stock count=" + stockList.size());
                for (int i = 0; i < stockList.size(); i++) {
                    StockDbEntity stockDbEntity = stockList.get(i);
                    if (stockDbEntity != null) {
                        StockDbEntity cacheEntity = mStockMap.get(stockDbEntity.getDtSecCode());
                        if(cacheEntity != null) {
                            if(cacheEntity.getIUpdateTime() > stockDbEntity.getIUpdateTime()) {
                                DBHelper.getInstance().delete(stockDbEntity);
                            } else {
                                DBHelper.getInstance().delete(cacheEntity);
                            }
                        } else {
                            mStockMap.put(stockDbEntity.getDtSecCode(), stockDbEntity);
                        }
                    }
                }
            }
        }

        // 加载分组数据
        synchronized (STOCK_GROUP_LOCK) {
            mGroupMap.clear();
            List<GroupEntity> groupList = loadGroupFromDB(accountId);
            if (groupList != null) {
                DtLog.d(TAG, "reloadData from db uid=" + accountId + ", load group count=" + groupList.size());
                for (int i = 0; i < groupList.size(); i++) {
                    GroupEntity group = groupList.get(i);
                    if (group != null) {
                        mGroupMap.put(group.getCreateTime(), group);
                    }
                    DtLog.d(TAG, "load group=" + group.toString());
                }
            }
        }

        if (isUserChange) {
            // 切换回默认分组
            mCurrentGroupId = SYSTEM_GROUP_ID;
            SettingPref.putInt(CURRENT_GROUP_ID, SYSTEM_GROUP_ID);

            // 新用户登录，同步没有归属的自选全部同步到此账号下面
            if (accountManager.isLogined()) {
                boolean isChange = false;

                // 合并未登录用户的股票(默认分组)
                List<StockDbEntity> stockList = loadStockFromDB(0);
                if (stockList != null) {
                    DtLog.d(TAG, "reloadData from db uid=" + 0 + ", load stock count=" + stockList.size());
                    synchronized (STOCK_LIST_LOCK) {
                        for (StockDbEntity entity : stockList) {
                            entity.setUser_id(accountIdStr);
                            DBHelper.getInstance().update(entity);
                            mStockMap.put(entity.getDtSecCode(), entity);
                            isChange = true;
                        }
                    }
                }

                // 合并未登录用户的分组
                List<GroupEntity> groupList = loadGroupFromDB(0);
                if (groupList == null) {
                    DtLog.d(TAG, "reloadData groupList is null");
                }
                final int size = groupList == null ? 0 : groupList.size();
                DtLog.d(TAG, "reloadData from db uid=" + 0 + ", load group count=" + size);
                if (size > 0) {
                    synchronized (STOCK_GROUP_LOCK) {
                        for (GroupEntity entity : groupList) {
                            if (mGroupMap.containsKey(entity.getName())) {
                                // 如果有同名分组，合并分组中的股票，并删除一个同名分组
                                GroupEntity existGroup = mGroupMap.get(entity.getName());
                                existGroup.addStock(entity.getAllStock(true, false));
                                existGroup.setDel(false);
                                // 删除DB
                                DBHelper.getInstance().delete(entity);
                            } else {
                                entity.setUser_id(accountIdStr);
                                mGroupMap.put(entity.getCreateTime(), entity);
                                // 更新DB
                                updateGroup2DB(entity);
                            }
                            isChange = true;
                        }
                    }
                }

                if (isChange) {
                    // 同步了没有归属的自选,标记需要同步
                    SettingPref.putBoolean(SETTING_PORTFOLIO_CHANGE, true);
                }
            } else {
                //注销的时候也标记变化，触发以GUID为KEY的自选股同步
                SettingPref.putBoolean(SETTING_PORTFOLIO_CHANGE, true);
            }
        } else {
            // 等待分组初始化完成后，再初始化mCurrentGroupId，确保使用数据时能够匹配
            mCurrentGroupId = SettingPref.getInt(CURRENT_GROUP_ID, SYSTEM_GROUP_ID);
        }

        //重新加载股票列表之后立即更新一次当前分组的行情数据
//        updateCurrentGroupStockQuote();   // 全量拉取可能数据量较大，暂时屏蔽，等UI显示后，更新可见区域股票

        // 本地数据重新加载完，通知UI更新
        CallbackManager.getInstance().notify(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, null);

        // 检查是否需要同步
        syncNowNoDelay();
    }

    /**
     * 上传本地股票到服务器
     */
    public void upload() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        final long accountId = accountManager.getAccountId();
        DtLog.d(TAG, "upload, uid=" + accountId);

        // 准备请求列表
        ProSecInfoList proSecInfoList = new ProSecInfoList();
        proSecInfoList.setIVersion(SettingPref.getInt(SETTING_PORTFOLIO_VERSION, 0));

        // 自选股票
        final List<StockDbEntity> stockDbEntityList = getAllStockList(true, false);
        final int stockSize = stockDbEntityList == null ? 0 : stockDbEntityList.size();
        ArrayList<ProSecInfo> proSecList = new ArrayList<>();
        for (int i = 0; i < stockSize; i++) {
            proSecList.add(stockDbEntityList.get(i).convertDbEntity2SecInfo());
        }
        proSecInfoList.setVProSecInfo(proSecList);

        // 自选分组
        ArrayList<GroupInfo> groupInfoList = new ArrayList<>();
        List<GroupEntity> groupEntityList = getAllGroup(true, false);
        for (int i = 0; i < groupEntityList.size(); i++) {
            GroupEntity entity = groupEntityList.get(i);
            List<GroupEntity.GroupStock> stockList = entity.getAllStock(true, false);

            // 填充分组信息
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setSGroupName(entity.getName());
            groupInfo.setICreateTime(entity.getCreateTime());
            groupInfo.setIsDel(entity.isDel());
            groupInfo.setIUpdateTime(entity.getUpdateSortTime());

            // 填充分组的股票信息
            ArrayList<BEC.GroupSecInfo> vGroupSecInfo = new ArrayList<>();
            for (GroupEntity.GroupStock stock : stockList) {
                GroupSecInfo secInfo = new GroupSecInfo();
                secInfo.setSDtSecCode(stock.dtcode);
                secInfo.setIUpdateTime(stock.updateTime);
                secInfo.setIsDel(stock.isDel);
                vGroupSecInfo.add(secInfo);
            }
            groupInfo.setVGroupSecInfo(vGroupSecInfo);

            // 完成一个分组数据封装
            groupInfoList.add(groupInfo);
        }
        proSecInfoList.setVGroupInfo(groupInfoList);

        // 准备请求包
        SavePortfolioReq saveRep = new SavePortfolioReq();
        saveRep.setStUserInfo(accountManager.getUserInfo());
        saveRep.setStProSecInfoList(proSecInfoList);
        saveRep.setIAccountId(accountId);

        AccountTicket ticket = new AccountTicket();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        if (accountInfo != null) {
            ticket.setVtTicket(accountInfo.ticket);
        }
        saveRep.setStAccountTicket(ticket);
        DtLog.d(TAG, "upload, req=" + saveRep.toString());

        DataEngine.getInstance().request(EntityObject.ET_SAVE_PORTFOLIO, saveRep, mNetworkCallback);
    }

    /**
     * 自选同步，延迟3s
     */
    public void syncNow() {
        SettingPref.putBoolean(SETTING_PORTFOLIO_CHANGE, true);
        DtLog.d(TAG, "syncNow");
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeCallbacks(queryRunnable);
            mBackgroundHandler.postDelayed(queryRunnable, SYNC_DELAY);
        }
    }

    /**
     * 立即同步，立即执行
     */
    public void syncNowNoDelay() {
        DtLog.d(TAG, "syncNowNoDelay");
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeCallbacks(queryRunnable);
            mBackgroundHandler.postDelayed(queryRunnable, 0);
        }
    }

    /**
     * 从服务器更新自选
     */
    public void queryFromServer() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        final long accountId = accountManager.getAccountId();
        if (accountId == 0L) {
            // 未登录用户也需要同步，以便下发灯塔直播消息
            if (SettingPref.getBoolean(SETTING_PORTFOLIO_CHANGE, false)) {
                // 本地数据有变时，上传同步
                upload();
            }
            return;
        }

        QueryPortfolioReq req = new QueryPortfolioReq();
        req.setStUserInfo(accountManager.getUserInfo());
        int version = SettingPref.getInt(SETTING_PORTFOLIO_VERSION, 0);
        req.setIVersion(version);
        DtLog.d(TAG, "queryFromServer, local version=" + version);
        req.setIAccountId(accountId);

        AccountTicket ticket = new AccountTicket();
        ticket.setVtTicket(accountManager.getAccountInfo().ticket);
        req.setStAccountTicket(ticket);
        DtLog.d(TAG, "queryFromServer, req=" + req.toString());

        DataEngine.getInstance().request(EntityObject.ET_QUERY_PORTFOLIO, req, mNetworkCallback);
    }

    /**
     * 从数据库加载自选数据 update_time DESC排序
     * @param accountId
     */
    private List<StockDbEntity> loadStockFromDB(long accountId) {
        String where = String.format("user_id='%s'", accountId);
        String order = "update_time DESC";

        // 读取自选股票列表
        return DBHelper.getInstance().findAllByWhere(StockDbEntity.class, where, order);
    }

    /**
     * 从数据库加载分组数据 update_time DESC排序
     */
    private List<GroupEntity> loadGroupFromDB(long accountId) {
        String where = String.format("user_id='%s'", accountId);
        String order = "update_time DESC";

        // 读取自选股票列表
        return DBHelper.getInstance().findAllByWhere(GroupEntity.class, where, order);
    }

    /**
     * 获取所有自选股票
     * @param includeDelete 是否包括标记为删除的股票
     * @param isSort 是否排序
     * @return
     */
    public List<StockDbEntity> getAllStockList(boolean includeDelete, boolean isSort) {
        synchronized (STOCK_LIST_LOCK) {
            List<StockDbEntity> entities = new ArrayList<>();
            Iterator<String> keyIterator = mStockMap.keySet().iterator();
            final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                    .getManager(IDataCacheManager.class.getName());
            final boolean hasCache = dataCacheManager != null;
            while (keyIterator.hasNext()) {
                final String dtSecCode = keyIterator.next();
                StockDbEntity entity = mStockMap.get(dtSecCode);
                if (includeDelete || !entity.isDel()) {
                    if (hasCache) {
                        entity.updateData(dataCacheManager.getSecSimpleQuote(dtSecCode));
                    }
                    entities.add(entity);
                }
            }

            if (isSort) {
                Collections.sort(entities);
            }

            return entities;
        }
    }

    @Override
    public boolean isEmptyFromCurrentGroup() {
        if (mCurrentGroupId == SYSTEM_GROUP_ID) {
            // 默认分组取出所有的股票
            final List<StockDbEntity> stockList = getAllStockList(false, false);
            return stockList == null || stockList.isEmpty();
        } else {
            // 在其他用户创建的分组
            GroupEntity groupEntity = mGroupMap.get(mCurrentGroupId);
            if (groupEntity != null) {
                final List<GroupEntity.GroupStock> groupStocks = groupEntity.getAllStock(false, false);
                return groupStocks == null || groupStocks.isEmpty();
            }
            return true;
        }
    }

    /**
     * 获取当前分组的所有股票
     */
    @Override
    public List<StockDbEntity> getAllStockFromCurrentGroup(boolean includeDelete, boolean isSort) {
        DtLog.d(TAG, "getAllStockFromCurrentGroup currentGroupId=" + mCurrentGroupId);
        List<StockDbEntity> entities = new ArrayList<>();
        if (mCurrentGroupId == SYSTEM_GROUP_ID) {
            // 默认分组取出所有的股票
            return getAllStockList(includeDelete, isSort);
        } else {
            // 在其他用户创建的分组
            GroupEntity groupEntity = mGroupMap.get(mCurrentGroupId);
            if (groupEntity != null) {
                List<GroupEntity.GroupStock> groupStocks = groupEntity.getAllStock(false, false);
                final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                        .getManager(IDataCacheManager.class.getName());
                final boolean hasCache = dataCacheManager != null;
                for (GroupEntity.GroupStock stock : groupStocks) {
                    final String dtSecCode = stock.dtcode;
                    synchronized (STOCK_LIST_LOCK) {
                        try {
                            StockDbEntity entity = mStockMap.get(dtSecCode);
                            if (hasCache) {
                                entity.updateData(dataCacheManager.getSecSimpleQuote(dtSecCode));
                            }
                            StockDbEntity tmp = (StockDbEntity)entity.clone();
                            tmp.setIUpdateTime(stock.updateTime);
                            entities.add(tmp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if (isSort) {
            Collections.sort(entities);
        }

        return entities;
    }

    /**
     * 获取所有沪深股票个数
     */
    @Override
    public int getChineseStockSize() {
        final List<StockDbEntity> stockList = getAllStockList(false, false);
        int size = 0;
        for (StockDbEntity entity : stockList) {
            if (BaseStockUtil.isChineseMarket(entity.getDtSecCode())) {
                size++;
            }
        }
        return size;
    }

    /**
     * 获取所有HK股票个数
     */
    @Override
    public int getHKStockSize() {
        final List<StockDbEntity> stockList = getAllStockList(false, false);
        int size = 0;
        for (StockDbEntity entity : stockList) {
            if (BaseStockUtil.isHongKongMarket(entity.getDtSecCode())) {
                size++;
            }
        }
        return size;
    }

    /**
     * 获取所有US股票个数
     */
    @Override
    public int getUSAStockSize() {
        final List<StockDbEntity> stockList = getAllStockList(false, false);
        int size = 0;
        for (StockDbEntity entity : stockList) {
            if (BaseStockUtil.isUsaMarket(entity.getDtSecCode())) {
                size++;
            }
        }
        return size;
    }

    /**
     * 获取所有持仓股票个数
     * @return
     */
    @Override
    public int getPositionStockSize() {
        final List<StockDbEntity> stockList = getAllStockList(false, false);
        int size = 0;
        for (StockDbEntity entity : stockList) {
            if (entity.isPosition) {
                size++;
            }
        }
        return size;
    }

    /**
     * 通过股票代码获取股票实体
     */
    @Override
    public StockDbEntity getStockDbEntity(String dtSecCode) {
        synchronized (STOCK_LIST_LOCK) {
            if (mStockMap.containsKey(dtSecCode)) {
                return mStockMap.get(dtSecCode);
            }
        }

        return null;
    }

    /**
     * 检查股票或主题是否已经加入自选
     */
    @Override
    public boolean isPortfolio(String secCode) {
        synchronized (STOCK_LIST_LOCK) {
            if (mStockMap.containsKey(secCode) && !mStockMap.get(secCode).isDel) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取个股备注
     * @param secCode
     * @return
     */
    public String getComment(String secCode) {
        if(isPortfolio(secCode)) {
            return mStockMap.get(secCode).comment;
        }
        return "";
    }

    /**
     * 设置个股备注
     */
    @Override
    public void setComment(String secCode, String comment, boolean syncNow) {
        DtLog.d(TAG, "setComment, dtSecCode=" + secCode + ", comment=" + comment);
        synchronized (STOCK_LIST_LOCK) {
            if (isPortfolio(secCode)) {
                StockDbEntity entity = mStockMap.get(secCode);
                entity.comment = comment;
                if (!TextUtils.isEmpty(comment)) {
                    boolean prevHasComment = !TextUtils.isEmpty(entity.comment);
                    int currentTimeSecond = TimeUtils.systemCurrentTimeSeconds();
                    if (prevHasComment) {
                        entity.iCommentUpdateTime = currentTimeSecond;
                    } else {
                        entity.iCommentCreateTime = currentTimeSecond;
                        entity.iCommentUpdateTime = currentTimeSecond;
                    }
                } else {
                    entity.iCommentCreateTime = -1;
                    entity.iCommentUpdateTime = -1;
                }

                DBHelper.getInstance().update(entity);
            }
        }

        if (syncNow) {
            syncNow();
        }
    }

    /**
     * 从db查找一个股票实体
     * @param codeUid
     * @return
     */
    private static final String QUERY_SEC = "user_id='%s' and dt_sec_code='%s'";

    private StockDbEntity findStockFromDB(String codeUid) {
        if (TextUtils.isEmpty(codeUid)) {
            return null;
        }

        // 取得账号id,加载自选数据
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return null;
        }

        final long accountId = accountManager.getAccountId();
        String where = String.format(QUERY_SEC, accountId, codeUid);
        DtLog.d(TAG, "findStockFromDB where=" + where);

        List<StockDbEntity> stockList = DBHelper.getInstance().findAllByWhere(StockDbEntity.class, where);
        if (stockList != null && stockList.size() > 0) {
            if (stockList.size() > 1) {
                DtLog.w(TAG, "findStockFromDB stockList=" + stockList.toString());
            }
            return stockList.get(0);
        }
        return null;
    }

    /**
     * 从db查找一个分组实体
     * @param groupId
     * @return
     */
    private static final String QUERY_GROUP = "user_id='%s' and createTime='%d'";
    private GroupEntity findGroupFromDB(int groupId) {
        DtLog.d(TAG, "findGroupFromDB, groupId=" + groupId);

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return null;
        }

        final long accountId = accountManager.getAccountId();
        String where = String.format(QUERY_GROUP, accountId, groupId);

        List<GroupEntity> groups = DBHelper.getInstance().findAllByWhere(GroupEntity.class, where);
        if (groups != null && groups.size() > 0) {
            if (groups.size() > 1) {
                DtLog.w(TAG, "findGroupFromDB groups=" + groups.toString());
            }
            return groups.get(0);
        }
        return null;
    }

    /**
     * 更新StockDbEntity列表的值，可用于更新自选股的位置，处理编辑界面的拖动位置
     */
    @Override
    public void updateStockList(final List<StockDbEntity> stockList) {
        if (mCurrentGroupId == SYSTEM_GROUP_ID) {
            mBackgroundHandler.post(() -> {
                // 默认分组
                for (StockDbEntity entity : stockList) {
                    DBHelper.getInstance().update(entity);
                }
                // 网络同步
                syncNow();
            });
        } else {
            // 自定义分组
            final GroupEntity groupEntity = mGroupMap.get(mCurrentGroupId);
            if (groupEntity != null) {
                for (StockDbEntity stock : stockList) {
                    groupEntity.updateStock(stock.getDtSecCode(), stock.getIUpdateTime(), false);
                }
                mBackgroundHandler.post(() -> {
                    DBHelper.getInstance().update(groupEntity);
                    // 网络同步
                    syncNow();
                });
            }
        }
    }

    /**
     * 更新GroupEntity列表的值，可用于更新分组的位置，处理编辑界面的拖动位置
     */
    @Override
    public void updateGroupList(final List<GroupEntity> groupEntities) {
        mBackgroundHandler.post(() -> {
            for (GroupEntity group : groupEntities) {
                // 更新db
                updateGroup2DB(group);
            }

            // 网络同步
            syncNow();
        });
    }

    /**
     * 标记一个股票的持仓状态
     * @param dtCode
     * @param isPosition 是否持仓，true持仓
     */
    @Override
    public void updatePositionStatus(String dtCode, boolean isPosition) {
        mBackgroundHandler.post(() -> {
            synchronized (STOCK_LIST_LOCK) {
                final StockDbEntity entity = mStockMap.get(dtCode);
                if (entity == null) {
                    return;
                }
                entity.setIsPosition(isPosition);
                DBHelper.getInstance().update(entity);
            }

            // 网络同步
            syncNow();
        });
    }

    /**
     * 添加股票到分组，详情界面用
     * @param  groupId 分组id
     * @param secCode 唯一码
     * @param name  名字
     */
    @Override
    public void addStock2Group(int groupId, String secCode, String name) {
        DtLog.d(TAG, "addStock2Group, groupId=" + groupId + ", uniCode=" + secCode + ", name=" + name);
        if (TextUtils.isEmpty(secCode)) {
            return;
        }

        if (SYSTEM_GROUP_ID != groupId && mGroupMap.containsKey(groupId)) {
            // 加入用户创建的组
            GroupEntity entity = mGroupMap.get(groupId);
            entity.addStock(secCode);

            // 更新用户分组到db
            updateGroup2DB(entity);
        }

        // 加入自选池
        addStock(secCode, name);
    }

    /**
     * 添加股票，详情界面用
     * @param uniCode 唯一码
     * @param name  名字
     */
    public void addStock(String uniCode, String name) {
        SecInfo secInfo = new SecInfo();
        secInfo.setSDtSecCode(uniCode);
        secInfo.setSCHNShortName(name);

        addStock(secInfo, true);
    }

    private int mLastSaveTime = 0;

    /**
     * 添加股票
     * @param secInfo
     * @param syncNow
     */
    private StockDbEntity addStock(SecInfo secInfo, final boolean syncNow) {
        if (TextUtils.isEmpty(secInfo.getSDtSecCode()) || isPortfolio(secInfo.getSDtSecCode())) {
            return null;
        }

        mAddStockCount++;

        StockDbEntity newEntity;
        int currentTimeSecond = TimeUtils.systemCurrentTimeSeconds();
        // 为了解决在一秒钟内添加多支股票，导致时间戳混乱的问题。
        // 如果当前时间与最后一次更新时间相同或更小，则以最后一次
        // 更新时间+1来作为时间戳，否则就使用当前时间。
        if(currentTimeSecond <= mLastSaveTime) {
            mLastSaveTime = mLastSaveTime + 1;
        } else {
            mLastSaveTime = currentTimeSecond;
        }

        if (mStockMap.containsKey(secInfo.getSDtSecCode())) {
            // 之前删除过的股票，修改删除状态 更新操作时间
            newEntity = mStockMap.get(secInfo.getSDtSecCode());
            // time
            newEntity.setICreateTime(mLastSaveTime);
            newEntity.setIUpdateTime(mLastSaveTime);
            newEntity.setIsDel(false);

            DBHelper.getInstance().update(newEntity);

            // 后台同步
            if (syncNow) {
                syncNow();
            }
        } else {
            // 之前没有添加过，新创建
            newEntity = new StockDbEntity();
            // code & name
            newEntity.setDtSecCode(secInfo.getSDtSecCode());        // 唯一码
            newEntity.setSzName(secInfo.getSCHNShortName());        // 中文名
            // time
            newEntity.setICreateTime(mLastSaveTime);
            newEntity.setIUpdateTime(mLastSaveTime);
            addStock(newEntity, syncNow);
        }

        return newEntity;
    }

    /**
     * 添加股票，增加用户信息，加入内存缓存和数据库
     * @param newEntity
     * @param syncNow
     */
    private void addStock(StockDbEntity newEntity, boolean syncNow) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        // user
        newEntity.setUser_id(String.valueOf(accountManager.getAccountId()));

        // 存入db
        DBHelper.getInstance().add(newEntity);

        // 取得生成_id的实体，这样才可以直接update更新
        StockDbEntity dbEntity = findStockFromDB(newEntity.getDtSecCode());

        if (dbEntity == null) { //存入DB异常失败，此次操作取消
            return;
        }

        // 放入内存
        synchronized (STOCK_LIST_LOCK) {
            mStockMap.put(dbEntity.getDtSecCode(), dbEntity);
        }

        DtLog.d(TAG, "addStock to cache and db ok, code=" + newEntity.getDtSecCode());

        // 后台同步
        if (syncNow) {
            // 新增了自选股票，立即更新一次行情
            ArrayList<String> secCodeList = new ArrayList<>();
            secCodeList.add(newEntity.getDtSecCode());
            updateQuoteFromServer(secCodeList);

            syncNow();
        }
    }

    /**
     * 开始同步，先更新服务器的数据到本地
     */
    private Runnable queryRunnable = () -> {
        DtLog.d(TAG, "queryRunnable run");
        queryFromServer();
    };

    /**
     * 标记为删除，但不真正删除，而是等待服务器确认
     * @param uniCode 代码
     */
    @Override
    public void modifyDeletePortfolio(final String uniCode, final boolean sync) {
        if (mStockMap.containsKey(uniCode)) {
            deleteStockFromAllGroup(new ArrayList<String>() {{
                add(uniCode);
            }}, sync);
        }
    }

    /**
     * 从所有分组中删除股票
     * 标记股票为删除状态，但不真正删除，而是等待服务器确认
     */
    @Override
    public void deleteStockFromAllGroup(List<String> uniCodeList, final boolean sync) {
        boolean change = false;

        // 获取所有用户分组，从所有用户分组中删除
        final List<GroupEntity> groupEntities = getAllGroup(false, false);
        for (GroupEntity group : groupEntities) {
            if (deleteStockFromGroup(uniCodeList, group)) {
                change = true;
            }
        }

        // 从默认分组中删除
        synchronized (STOCK_LIST_LOCK) {
            for (String uniCode : uniCodeList) {
                if (mStockMap.containsKey(uniCode)) {
                    DtLog.d(TAG, "deleteStockFromAllGroup, uniCode=" + uniCode);

                    StockDbEntity stockDbEntity = mStockMap.get(uniCode);
                    // 置为删除状态
                    stockDbEntity.setIsDel(true);
                    DBHelper.getInstance().update(stockDbEntity);
                    change = true;
                    mDelStockCount++;
                }
            }
        }

        if (change) {
            CallbackManager.getInstance().notify(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, null);
            if (sync) {   // 网络同步
                syncNow();
            }
        }
    }

    /**
     * 从分组删除股票
     */
    @Override
    public void deletePortfolioStockFromCurrentGroup(List<String> uniCodeList, final boolean sync) {
        final GroupEntity groupEntity = getCurrentGroup();

        if (groupEntity != null) {
            // 从用户分组删除
            final boolean delete = deleteStockFromGroup(uniCodeList, groupEntity);
            if (sync && delete) {
                // 网络同步
                syncNow();
            }
        } else {
            // 直接从默认分组删除
            deleteStockFromAllGroup(uniCodeList, sync);
        }
    }

    /**
     * 从用户分组删除指定的股票
     * @param uniCodeList
     * @param groupEntity
     * @return
     */
    private boolean deleteStockFromGroup(List<String> uniCodeList, final GroupEntity groupEntity) {
        boolean delete = false;
        if (groupEntity != null) {
            for (String dtCode : uniCodeList) {
                DtLog.d(TAG, "deleteStockFromGroup dtCode=" + dtCode + ", group=" + groupEntity.getName());
                if (groupEntity.delStock(dtCode)) {
                    delete = true;
                }
            }

            if(delete) {
                updateGroup2DB(groupEntity);
                CallbackManager.getInstance().notify(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, null);
            }
        }
        return delete;
    }

    /**
     * 更新个股提醒信息
     */
    public void updateReminderInfo(final StockDbEntity entity) {
        mBackgroundHandler.post(() -> {
            DBHelper.getInstance().update(entity);
            syncNow();
        });
    }

    // 自选同步服务器返回数据
    private DataSourceProxy.IRequestCallback mNetworkCallback = new DataSourceProxy.IRequestCallback() {
        @Override
        public void callback(boolean success, EntityObject data) {
            if (!success || data == null || data.getEntity() == null) {
                // 失败了，通知UI刷新
                DtLog.w(TAG, "mNetworkCallback callback failed, success=" + success);
                return;
            }

            switch (data.getEntityType()) {
                case EntityObject.ET_GET_SIMPLE_QUOTE:
                    handleCombSimple((QuoteSimpleRsp)data.getEntity());
                    break;
                case EntityObject.ET_SAVE_PORTFOLIO:
                    handleSavePortfolio((MapProtoLite) data.getEntity());
                    break;
                case EntityObject.ET_QUERY_PORTFOLIO:
                    handleQueryPortfolio((MapProtoLite)data.getEntity());
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 保存本地数据到服务器的结果处理
     * @param mapProtoLite
     */
    private void handleSavePortfolio(MapProtoLite mapProtoLite) {
        int retCode = mapProtoLite.read("", -1);
        DtLog.d(TAG, "handleSavePortfolio retCode=" + retCode);
        switch (retCode) {
            case E_PORTFOLIO_RET.E_PR_SUCC:
                // 成功同步到后台了
                SavePortfolioRsp rsp = mapProtoLite.read(NetworkConst.RSP, new SavePortfolioRsp());
                SettingPref.putInt(SETTING_PORTFOLIO_VERSION, rsp.getIVersion());
                SettingPref.putBoolean(SETTING_PORTFOLIO_CHANGE, false);
                DtLog.d(TAG, "handleSavePortfolio _E_PR_SUCC version=" + rsp.getIVersion());
                break;
            case E_PORTFOLIO_RET.E_PR_ERROR:
                // 服务器错误
                DtLog.w(TAG, "handleSavePortfolio E_PR_ERROR");
                break;
            case E_PORTFOLIO_RET.E_PR_CONFLICT:
                // 有冲突，发起query，合并服务器和本地，目前走不到这里，因为先update再upload
                DtLog.w(TAG, "handleSavePortfolio _E_PR_CONFLICT");
                break;
            // ticket验证不过
            case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
            case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
            case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                processTicketError();
                break;
        }
    }

    private void processTicketError() {
        LocalBroadcastManager.getInstance(SDKManager.getInstance().getContext()).sendBroadcast(new Intent(CommonConst.ACTION_TICKET_ERROR));
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        accountManager.removeAccountInfo();
    }

    /**
     * 处理同步请求结果
     * @param packate
     */
    private void handleQueryPortfolio(MapProtoLite packate) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }

        int retCode = packate.read("", -1);
        DtLog.d(TAG, "handleQueryPortfolio retCode=" + retCode);
        switch (retCode) {
        case E_PORTFOLIO_RET.E_PR_SUCC:
            // 请求成功
            QueryPortfolioRsp rsp = packate.read(NetworkConst.RSP, new QueryPortfolioRsp());
            if (rsp.iAccountId != accountManager.getAccountId()) {
                // 同步期间 用户变了 不合并
                DtLog.w(TAG, "handleQueryPortfolio _E_PR_SUCC account changed, rsp account=" + rsp.iAccountId +
                        ", local account=" + accountManager.getAccountId());
                return;
            }

            // 合并股票
            ArrayList<String> addedCodeListFromServer = new ArrayList<>();
            ArrayList<BEC.ProSecInfo> rspStockList = rsp.getStProSecInfoList().getVProSecInfo();
            for (ProSecInfo rspStock : rspStockList) {
                String dtSecCode = rspStock.getSDtSecCode();
                String name = rspStock.getSName();

                if (SettingPref.getBoolean(SETTING_PORTFOLIO_CHANGE, false) && isPortfolio(rspStock.sDtSecCode)) {
                    // 本地无变化，以服务器为准
                } else {
                    if (rspStock.isDel && isPortfolio(rspStock.sDtSecCode)) {
                        DtLog.d(TAG, "handleQueryPortfolio delete, rspStock=" + dtSecCode + ", name = " + name);
                        modifyDeletePortfolio(dtSecCode, false);
                    } else {
                        if (mStockMap.containsKey(dtSecCode)) {
                            DtLog.d(TAG, "handleQueryPortfolio local exist, rspStock=" + dtSecCode + ", name = " + name);
                            synchronized (STOCK_LIST_LOCK) {
                                // 自选存在则更新本地
                                StockDbEntity localEntity = mStockMap.get(dtSecCode);
                                StockDbEntity newEntity = StockDbEntity.convertSecInfo2DbEntity(rspStock);
                                newEntity.set_id(localEntity.get_id());
                                mStockMap.put(newEntity.getDtSecCode(), newEntity);
                                DBHelper.getInstance().update(newEntity);
                            }
                        } else {
                            // 自选不存在，加入
                            DtLog.d(TAG, "handleQueryPortfolio add, rspStock=" + dtSecCode + ", name = " + name);
                            addStock(StockDbEntity.convertSecInfo2DbEntity(rspStock), false);
                            addedCodeListFromServer.add(dtSecCode);
                        }
                    }
                }
            }

            // 合并分组
            ArrayList<GroupInfo> rspGroupList = rsp.getStProSecInfoList().getVGroupInfo();
            for (GroupInfo rspGroup : rspGroupList) {
                GroupEntity localGroupEntity;
                int groupId = rspGroup.getICreateTime();
                if (rspGroup.isDel) {
                    DtLog.d(TAG, "handleQueryPortfolio delete group groupId=" + groupId);
                    localGroupEntity = mGroupMap.get(groupId);
                    if (localGroupEntity != null) {
                        delGroup(localGroupEntity.getCreateTime(), false, false);
                    }

                    // 删除的是当前分组，则切换回默认分组
                    if (groupId == mCurrentGroupId) {
                        mCurrentGroupId = SYSTEM_GROUP_ID;
                        SettingPref.putInt(CURRENT_GROUP_ID, SYSTEM_GROUP_ID);
                    }
                } else {
                    if (mGroupMap.containsKey(groupId)) {
                        // 分组存在,本地数据有变时，则以本地变更为准,否则以服务器为准
                        DtLog.d(TAG, "handleQueryPortfolio local exist, update groupId=" + groupId);
                        localGroupEntity = mGroupMap.get(groupId);
                        if (!SettingPref.getBoolean(SETTING_PORTFOLIO_CHANGE, false)) {
                            // 本地无变化，以服务器为准
                            localGroupEntity.setUpdateSortTime(rspGroup.iUpdateTime);
                            localGroupEntity.setName(rspGroup.getSGroupName());
                            localGroupEntity.setDel(rspGroup.getIsDel());
                            DtLog.d(TAG, "local no changed, user server entity, new localGroupEntity=" + localGroupEntity.toString());
                        } else {
                            DtLog.d(TAG, "local has changed, user local entity, localGroupEntity=" + localGroupEntity.toString());
                        }
                    } else {
                        // 分组不存在，加入
                        DtLog.d(TAG, "handleQueryPortfolio add, groupId=" + groupId);
                        localGroupEntity = addGroup(rspGroup.getICreateTime(), rspGroup.getIUpdateTime(), rspGroup.getSGroupName(), false);
                    }

                    // 分组中股票合并
                    ArrayList<BEC.GroupSecInfo> secInfoArrayList = rspGroup.getVGroupSecInfo();
                    for (GroupSecInfo secInfo : secInfoArrayList) {
                        String dtSecCode = secInfo.getSDtSecCode();
                        if (localGroupEntity.isHaveStock(true, dtSecCode)) {
                            // 本地分组存在此股票
                            if (!SettingPref.getBoolean(SETTING_PORTFOLIO_CHANGE, false)) {
                                // 本地无变化，以服务器为准
                                localGroupEntity.updateStock(dtSecCode, secInfo.getIUpdateTime(), secInfo.isDel);
                                DtLog.d(TAG, "local no changed, user server entity, new localGroupEntity=" + localGroupEntity.toString());
                            } else {
                                // 本地有变化，忽略服务器
                                DtLog.d(TAG, "local has changed, user local entity, localGroupEntity=" + localGroupEntity.toString());
                            }
                        } else {
                            // 本地分组无此股票
                            if (isPortfolio(dtSecCode)) {
                                // 如果自选股中存在才加入分组
                                localGroupEntity.addStock(dtSecCode);
                                localGroupEntity.updateStock(dtSecCode, secInfo.getIUpdateTime(), secInfo.isDel);
                            }
                        }
                    }
                    updateGroup2DB(localGroupEntity);
                }
            }

            // 记录更新到的版本号
            DtLog.d(TAG, "handleQueryPortfolio SecBaseInfoReq, new version=" + rsp.getStProSecInfoList().getIVersion());
            SettingPref.putInt(SETTING_PORTFOLIO_VERSION, rsp.getStProSecInfoList().getIVersion());

            if (SettingPref.getBoolean(SETTING_PORTFOLIO_CHANGE, false)) {
                // 本地数据有变时，上传同步
                upload();
            }

            // 通知UI刷新
            CallbackManager.getInstance().notify(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, null);

            //从服务器返回的新增股票列表马上拉取一次行情
            updateQuoteFromServer(addedCodeListFromServer);
            break;
        case E_PORTFOLIO_RET.E_PR_ERROR:
            // 服务器错误
            break;
        case E_PORTFOLIO_RET.E_PR_NO_UPDATE:
            // 本地和服务器版本号一致，服务器没有新数据
            if (SettingPref.getBoolean(SETTING_PORTFOLIO_CHANGE, false)) {
                // 本地数据有变时，同步
                upload();
            }
            break;

        // ticket验证不过
        case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
        case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
        case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
            processTicketError();
            break;
        }
    }

    /**
     * 处理服务器返回的组合简版行情数据
     * @param quoteSimpleRsp
     */
    private void handleCombSimple(final QuoteSimpleRsp quoteSimpleRsp) {
        DtLog.d(TAG, "handleCombSimple");
        final ArrayList<BEC.SecSimpleQuote> secSimpleQuoteList = quoteSimpleRsp.getVSecSimpleQuote();

        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                .getManager(IDataCacheManager.class.getName());
        if (dataCacheManager != null) {
            dataCacheManager.setSecSimpleQuotes(secSimpleQuoteList);
        }

        for (SecSimpleQuote quote : secSimpleQuoteList) {
            synchronized (STOCK_LIST_LOCK) {
                StockDbEntity entity = mStockMap.get(quote.getSDtSecCode());
                if (entity != null) {
                    entity.updateData(quote);
                    if (!TextUtils.isEmpty(quote.getSSecName()) && !quote.getSSecName().equalsIgnoreCase(entity.getSzName())) {
                        // 检测到行情不为空，也不和本地一样，则保存，并同步到后台
                        DtLog.d(TAG, "handleQueryPortfolio subject name changed, local=" + entity.getSzName() + ", quote=" + quote.getSSecName());
                        entity.setSzName(quote.getSSecName());
                        DBHelper.getInstance().update(entity);
                        syncNow();
                    }
                }
            }
        }
    }

    /**
     * 添加一个新分组
     * @param createTime 如果是服务器同步过来的分组，createTime为服务器分组的时间，否则传0
     */
    @Override
    public GroupEntity addGroup(int createTime, int updateTime, String name, boolean syncNow) {
        DtLog.d(TAG, "addGroup start, name=" + name + ", syncNow=" + syncNow + ", createTime=" + createTime);

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return null;
        }

        GroupEntity entity = new GroupEntity();
        entity.setName(name);

        entity.setUser_id(String.valueOf(accountManager.getAccountId()));

        int currentTimeSecond = TimeUtils.systemCurrentTimeSeconds();

        if (createTime == 0) {
            entity.setCreateTime(currentTimeSecond);
        } else {
            entity.setCreateTime(createTime);
        }

        if (updateTime == 0) {
            entity.setUpdateSortTime(currentTimeSecond);
        } else {
            entity.setUpdateSortTime(updateTime);
        }

        entity.setDel(false);

        DBHelper.getInstance().add(entity);

        entity = findGroupFromDB(entity.getCreateTime());

        synchronized (STOCK_GROUP_LOCK) {
            mGroupMap.put(entity.getCreateTime(), entity);
        }

        if (syncNow) {
            syncNow();
        }
        return entity;
    }

    public int checkGroupNameValid(final String name) {
        //只能是中英文字符或者数字
        boolean isValidChar = true;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (CharUtils.isChinese(c)) {
                if (CharUtils.isChinesePunctuation(c)) {
                    isValidChar = false;
                    break;
                }
            } else if (!CharUtils.isAlphabetOrDigit(c)) {
                isValidChar = false;
                break;
            }
        }
        if (!isValidChar) {
            return GROUP_NAME_INVALID_CHAR_NOT_ALLOWED;
        }

        //最多12个字符，中文算两个字符
        int charCount = CharUtils.getCharCount(name);
        if (charCount > 12) {
            return GROUP_NAME_INVALID_TOO_LONG;
        }
        if (TextUtils.isEmpty(name.trim())) {
            return GROUP_NAME_INVALID_TOO_LONG;
        }

//        List<GroupEntity> allGroup = getAllGroup(false, false);
//        for (GroupEntity groupEntity : allGroup) {
//            String groupEntityName = groupEntity.getName();
//            if (TextUtils.equals(name, groupEntityName)) {
//                return GROUP_NAME_INVALID_ALREADY_EXIST;
//            }
//        }

        return GROUP_NAME_VALID;
    }

    @Override
    public void addDataUpdateListener(OnLocalDataUpdateCallback dataUpdateCallback) {
        this.mOnDataUpdateCallback = dataUpdateCallback;
    }

    /**
     * 删除一个分组
     * @param createTime 要删除分组的id
     * @param isDelStock 是否删除分组内的股票
     * @param syncNow 是否同步
     */
    @Override
    public void delGroup(int createTime, boolean isDelStock, boolean syncNow) {
        DtLog.d(TAG, "delGroup createTime=" + createTime);

        GroupEntity groupEntity = mGroupMap.get(createTime);
        if (isDelStock) {
            List<GroupEntity.GroupStock> allStock = groupEntity.getAllStock(true, false);
            DtLog.d(TAG, "delGroup createTime=" + createTime + ", have stock count=" + allStock.size());
            for (GroupEntity.GroupStock stock : allStock) {
                List<GroupEntity> allGroup = getAllGroup(false, false);
                for (GroupEntity group : allGroup) {
                    if (group.isHaveStock(true, stock.dtcode)) {
                        // 从分组中删除股票，并更新db和cache
                        group.delStock(stock.dtcode);
                        DtLog.d(TAG, "delGroup " + "del stock " + stock.dtcode + " from group " + group.getName());
                        updateGroup2DB(group);
                        synchronized (STOCK_GROUP_LOCK) {
                            mGroupMap.put(group.getCreateTime(), group);
                        }
                    }
                }

                // 把该股票在默认分组标记为删除
                modifyDeletePortfolio(stock.dtcode, false);
            }
        }

        groupEntity.setDel(true);   // 把分组标记为删除
        groupEntity.setStocks("");  // 清除此分组内的所有股票

        if (syncNow && createTime == mCurrentGroupId) {
            //用户触发的删除且删的是当前分组就切换到默认分组
            changeCurrentGroup(SYSTEM_GROUP_ID);
        }

        // 更新数据库
        updateGroup2DB(groupEntity);

        if (syncNow) {
            syncNow();
        }
    }

    /**
     * 修复分组名称(如果组名和别的组名相同会覆盖别的组)
     * @param groupId   分组id create time
     * @param destName  要修改的名称
     */
    @Override
    public void modifyGroupName(int groupId, String destName) {
        DtLog.d(TAG, "modifyGroupName groupId=" + groupId + ", destName=" + destName);
        if (groupId == SYSTEM_GROUP_ID || TextUtils.isEmpty(destName)) {
            // 默认分组则不做修改
            return;
        }

        synchronized (STOCK_GROUP_LOCK) {
            GroupEntity orgEntity = mGroupMap.get(groupId);
            if(orgEntity != null) {
                orgEntity.setName(destName);
                updateGroup2DB(orgEntity);
                syncNow();
            }
        }
    }

    /**
     * 异步更新一个分组
     * @param entity
     */
    private void updateGroup2DB(final GroupEntity entity) {
        if (entity == null) {
            return;
        }

        mBackgroundHandler.post(() ->{
                DBHelper.getInstance().update(entity);
                if (null != mOnDataUpdateCallback){
                    mOnDataUpdateCallback.onDataUpdate();
                }
        });
    }

    /**
     * 获取所有分组
     * @param isIncludeDel 是否包括删除的
     * @param isSort    是否排序
     * @return
     */
    @Override
    public List<GroupEntity> getAllGroup(boolean isIncludeDel, boolean isSort) {
        List<GroupEntity> entities = new ArrayList<>();
        if (mGroupMap == null) {
            DtLog.w(TAG, "getAllGroup mGroupMap == null");
            return entities;
        }

        synchronized (STOCK_GROUP_LOCK) {
            Iterator<Integer> keyIterator = mGroupMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                GroupEntity entity = mGroupMap.get(keyIterator.next());
                if (isIncludeDel || !entity.isDel()) {
                    entities.add(entity);
                    DtLog.d(TAG, "getAllGroup entity=" + entity.toString());
                }
            }
        }

        if (isSort) {
            Collections.sort(entities);
        }

        return entities;
    }

    /**
     * 切换分组
     * @param createTime 分组id
     */
    public void changeCurrentGroup(int createTime) {
        mCurrentGroupId = createTime;
        SettingPref.putInt(CURRENT_GROUP_ID, mCurrentGroupId);

        // 分组改变了，通知UI刷新界面
        CallbackManager.getInstance().notify(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, null);
    }

    @Override
    public String getCurrentGroupName() {
        if (mGroupMap == null || mGroupMap.size() == 0 || mCurrentGroupId == SYSTEM_GROUP_ID) {
            return SDKManager.getInstance().getContext().getResources().getString(R.string.portfolio_group_default);
        }
        return mGroupMap.get(mCurrentGroupId).getName();
    }

    /**
     * 获取当前分组
     * @return 如果是默认分组返回null
     */
    public GroupEntity getCurrentGroup() {
        if (mCurrentGroupId == 0 || mGroupMap == null) {
            return null;
        } else {
            return mGroupMap.get(mCurrentGroupId);
        }
    }

    /**
     * 获取用户分组
     */
    @Override
    public GroupEntity getUserGroup(int groupId) {
        if (groupId == 0 || mGroupMap == null) {
            return null;
        } else {
            return mGroupMap.get(groupId);
        }
    }

    /**
     * 获取用户分组名
     */
    @Override
    public String getUserGroupName(int groupId) {
        if (groupId == 0 || mGroupMap == null || mGroupMap.size() == 0) {
            return SDKManager.getInstance().getContext().getResources().getString(R.string.portfolio_group_default);
        } else {
            return mGroupMap.get(groupId).getName();
        }
    }

    @Override
    public int getAddStockCount() {
        return mAddStockCount;
    }

    @Override
    public int getDelStockCount() {
        return mDelStockCount;
    }

    private boolean mRegistered;

    private void registerAccountReceiver() {
        if (!mRegistered) {
            final IntentFilter intentFilter = new IntentFilter(CommonConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(CommonConst.ACTION_LOGOUT_SUCCESS);
            final Context context = SDKManager.getInstance().getContext();
            LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter);
            mRegistered = true;
        }
    }

    private void unregisterAccountReceiver() {
        if (mRegistered) {
            final Context context = SDKManager.getInstance().getContext();
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            mRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (CommonConst.ACTION_LOGIN_SUCCESS.equals(action) || CommonConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
            onAccountChange();
        }
    }
}