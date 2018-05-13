package com.sscf.investment.web.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.db.SearchHistoryItem;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IBonusPointManager;
import com.dengtacj.component.managers.IFeedRequestManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.utils.Base64Utils;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.web.sdk.photoviewer.ImagePagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import BEC.E_DT_MEMBER_TYPE;

/**
 * Created by xuebinliu on 9/23/15.
 *
 * Js调用本地代码代理
 */
public class NativeProxy {
    public static final String TAG = "NativeProxy";

    private Handler mHandler;

    private static final String KEY_STOCKS = "stocks";
    private static final String KEY_SEC_NAME = "sCHNShortName";
    private static final String KEY_DT_SEC_CODE = "sDtSecCode";
    private static final String KEY_RESULT = "result";

    public NativeProxy(Handler handler) {
        DtLog.d(TAG, "constructor handler=" + handler);
        mHandler = handler;
    }

    /**
     * 关闭终端页面
     * @param
     */
    @JavascriptInterface
    public void finish() {
        DtLog.d(TAG, "finish");

        Message msg = Message.obtain();
        msg.what = WebConst.MSG_FINISH_PAGE;

        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 设置web page标题
     * @param title
     */
    @JavascriptInterface
    public void setTitle(String title) {
        DtLog.d(TAG, "setTitle title=" + title);
        if (title == null) {
            return;
        }

        Message msg = Message.obtain();
        msg.what = WebConst.MSG_UPDATE_TITLE;
        msg.obj = title;

        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 设置页面是否允许长按操作
     * setOnLongClickListener可能会导致WebView卡死
     * @param enabled
     */
    @JavascriptInterface
    public void setLongClickEnabled(boolean enabled) {
    }

    /**
     * 根据URL下载文件
     * @param url
     */
    @JavascriptInterface
    public void downloadFile(String url) {
        DtLog.d(TAG, "downloadFile url=" + url);
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Message msg = Message.obtain();
        msg.what = WebConst.MSG_DOWNLOAD_FILE;
        msg.obj = url;

        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 根据URL下载文件
     * @param url
     */
    @JavascriptInterface
    public void openPdfFileBySystem(String url) {
        DtLog.d(TAG, "openPdfFileBySystem url=" + url);
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Message msg = Message.obtain();
        msg.what = WebConst.MSG_OPEN_PDF_BY_SYSTEM;
        msg.obj = url;

        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 对应URL的PDF文件是否已经存在
     * @param url PDF文件对应的URL
     * @return 是否存在于cache目录
     */
    @JavascriptInterface
    public boolean isPdfFileExists(String url) {
        DtLog.d(TAG, "isPdfFileExists url=" + url);
        File file = FileUtil.getPdfFileByUrl(url);
        boolean exists = file.exists();
        DtLog.d(TAG, "isPdfFileExists exists = " + exists);
        return exists;
    }

    /**
     * 获取用户的账户信息，目前是用户反馈模块调用
     */
    @JavascriptInterface
    public String getAccountInfo() {
        DtLog.d(TAG, "getAccountInfo");

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());

        AccountInfoExt accountInfoExt = null;
        String guidString = "";
        String dua = "";
        if (accountManager != null) {
            accountInfoExt = accountManager.getAccountInfoExt();
            guidString = accountManager.getGuidString();
            dua = accountManager.getDUA();
        }

        String ticketStr = "";
        int cellphoneState = 0;
        long id = 0;
        String nickname = "";
        String iconUrl = "";
        int memberType = E_DT_MEMBER_TYPE.E_DT_NO_MEMBER;
        long memberEndTime = 0L;
        String idNum = "";
        String realNum = "";
        if (accountInfoExt != null) {
            final AccountInfoEntity accountInfo = accountInfoExt.accountInfo;
            if (accountInfo != null) {
                byte[] ticket = accountInfo.ticket;
                ticketStr = Base64Utils.encodeOnHttp(ticket);
                cellphoneState = TextUtils.isEmpty(accountInfo.cellphone) ? 0 : 1;
                id = accountInfo.id;
                nickname = accountInfo.nickname;
                iconUrl = accountInfo.iconUrl;
            }
            memberType = accountInfoExt.memberType;
            memberEndTime = accountInfoExt.memberEndTime;
            idNum = accountInfoExt.idNum;
            realNum = accountInfoExt.realName;
        }

        final String imei = DeviceUtil.getImei(SDKManager.getInstance().getContext());

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(WebConst.ACCOUNT_DT_ID, id);
            jsonObject.put(WebConst.ACCOUNT_DT_TICKET, ticketStr);
            jsonObject.put(WebConst.ACCOUNT_DT_DUA, dua);
            jsonObject.put(WebConst.ACCOUNT_DT_GUID, guidString);
            jsonObject.put(WebConst.ACCOUNT_DT_IMEI, imei);
            jsonObject.put(WebConst.ACCOUNT_DT_CELLPHONE_STATE, cellphoneState);
            jsonObject.put(WebConst.ACCOUNT_DT_NICKNAME, nickname);
            jsonObject.put(WebConst.ACCOUNT_DT_ICON, iconUrl);
            jsonObject.put(WebConst.ACCOUNT_DT_MEMBER_TYPE, memberType);
            jsonObject.put(WebConst.ACCOUNT_DT_MEMBER_END_TIME, memberEndTime);
            jsonObject.put(WebConst.ACCOUNT_DT_ID_NUM, idNum);
            jsonObject.put(WebConst.ACCOUNT_DT_REAL_NAME, realNum);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    /**
     * 打开登录界面
     */
    @JavascriptInterface
    public boolean openLoginPanel(String json) {
        DtLog.d(TAG, "openLoginPanel");
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());

        if (accountManager != null && accountManager.isLogined()) {
            return false;
        }

        CommonBeaconJump.showLogin(SDKManager.getInstance().getContext());
        return true;
    }

    /**
     * 打开分享界面
     * 更换新接口showSharePanel
     */
    @JavascriptInterface
    @Deprecated
    public void openSharePanel(String json) {
    }

    /**
     * 打开分享界面
     */
    @JavascriptInterface
    public void showSharePanel(final String json) {
        DtLog.d(TAG, "openSharePanel");
        if (mHandler != null) {
            final HashMap<String, ShareParams> paramsMaps = new HashMap<String, ShareParams>(5);
            int screenShot = jsonToShareParams(json, paramsMaps);
            if(!paramsMaps.isEmpty()) {
                mHandler.obtainMessage(WebConst.MSG_SHARE, screenShot, 0, paramsMaps).sendToTarget();
            }
        }
    }

    private static int jsonToShareParams(final String json, HashMap paramsMaps) {
        try {
            final JSONArray jsonArray = new JSONArray(json);
            final int length = jsonArray.length();
            JSONObject jsonObject = null;

            int screenShot = 0;

            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                if(i == 0) {
                    try {
                        screenShot = jsonObject.getInt("screenShot");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                String shareType = jsonObject.getString("shareType");
                String title = jsonObject.getString("title");
                title.replace("灯塔", "晓智");
                String msg = jsonObject.getString("desc");
                msg.replace("灯塔", "晓智");
                String url = jsonObject.getString("link");
                String imgUrl = jsonObject.getString("imgUrl");
//                if (imgUrl.equals("http://res.idtcdn.com/beacon/images/icon.jpg")){
//                    imgUrl = "https://sec.gushi.com/weixin/shareIcon.jpg";
//                }


                paramsMaps.put(shareType, ShareParams.createShareParams(title, msg, url, imgUrl));
            }
            return screenShot;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获得网络信息
     * @return
     */
    @JavascriptInterface
    public String netInfo() {
        long start = System.currentTimeMillis();
        final JSONObject jsonObject = new JSONObject();
        final Context context = SDKManager.getInstance().getContext();
        try {
            jsonObject.put("apn", NetUtil.getApn(context));
            jsonObject.put("networkType", NetUtil.getNetworkType(context));
            jsonObject.put("networkConnected", NetUtil.isNetWorkConnected(context));
            DtLog.d(TAG, "netInfo time is " + (System.currentTimeMillis() - start));
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DtLog.d(TAG, "netInfo null time is " + (System.currentTimeMillis() - start));
        return null;
    }

    /**
     *
     * @return
     */
    @JavascriptInterface
    public void setWebViewTitleBar(final String json) {
        if (mHandler != null) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonArray == null) {
                return;
            }

            mHandler.obtainMessage(WebConst.MSG_SET_WEBVIEW_TITLE_BAR, jsonArray).sendToTarget();
        }
    }

    /**
     * 开始或停止刷新动画
     * @param json，开始动画，其他为停止动画
     * @return
     */
    @JavascriptInterface
    public void setRefreshButtonAnim(final String json) {
        if (mHandler != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObject == null) {
                return;
            }

            mHandler.obtainMessage(WebConst.MSG_SET_REFRESH_BUTTON_ANIM, jsonObject.optInt("status", 0), 0).sendToTarget();
        }
    }

    /**
     * 获得所有搜索历史的记录
     */
    @JavascriptInterface
    public String getAllSearchHistoryItem(final String json) {
        final JSONObject data = new JSONObject();
        final List<SearchHistoryItem> items = SearchHistoryItem.findAllItemFromDb();
        final int size = items.size();
        final JSONArray stocksArray = new JSONArray();
        SearchHistoryItem item = null;
        JSONObject jsonObject = null;
        for (int i = 0; i < size; i++) {
            item = items.get(i);
            if (item != null) {
                jsonObject = new JSONObject();
                try {
                    jsonObject.put(KEY_SEC_NAME, item.getName());
                    jsonObject.put(KEY_DT_SEC_CODE, item.getUnicode());
                    stocksArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            data.put(KEY_STOCKS, stocksArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data.toString();
    }

    /**
     * 清除搜索历史的记录
     */
    @JavascriptInterface
    public void clearSearchHistoryItem(final String json) {
        SDKManager.getInstance().getDefaultExecutor().execute(() -> {
            SearchHistoryItem.clearAllItemFromDb();
        });
    }

    /**
     * 添加搜索历史的记录
     */
    @JavascriptInterface
    public void addSearchHistoryItem(final String json) {
        SDKManager.getInstance().getDefaultExecutor().execute(() -> {
            JSONObject data = null;
            try {
                data = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (data == null) {
                return;
            }

            final JSONArray stocksArray = data.optJSONArray(KEY_STOCKS);
            final int size = stocksArray != null ? stocksArray.length() : 0;
            JSONObject jsonObject = null;
            for (int i = 0; i < size; i++) {
                jsonObject = stocksArray.optJSONObject(i);
                if (data != null) {
                    SearchHistoryItem.addItemToDb(jsonObject.optString(KEY_SEC_NAME), jsonObject.optString(KEY_DT_SEC_CODE));
                }
            }
        });
    }

    /**
     * 获得自选股数据
     */
    @JavascriptInterface
    public String getAllPortfolio(final String json) {
        final JSONObject data = new JSONObject();

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());

        if (portfolioDataManager != null) {
            // 自选股票
            final JSONArray stocksArray = new JSONArray();
            final List<StockDbEntity> stockList = portfolioDataManager.getAllStockList(false, true);
            final int stockSize = stockList != null ? stockList.size() : 0;
            StockDbEntity stock = null;
            JSONObject item = null;

            for (int i = 0; i < stockSize; i++) {
                stock = stockList.get(i);
                if (stock != null) {
                    item = new JSONObject();
                    try {
                        item.put(KEY_SEC_NAME, stock.getSzName());
                        item.put(KEY_DT_SEC_CODE, stock.getDtSecCode());
                        stocksArray.put(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                data.put(KEY_STOCKS, stocksArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data.toString();
    }

    /**
     * 是否已经添加到自选股
     * return 0，不是。1，是。
     */
    @JavascriptInterface
    public String isPortfolio(final String json) {
        JSONObject data = null;
        try {
            data = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int result = 0;
        if (data != null) {
            final String dtSecCode = data.optString(KEY_DT_SEC_CODE);
            if (!TextUtils.isEmpty(dtSecCode)) {
                final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                        .getManager(IPortfolioDataManager.class.getName());
                if (portfolioDataManager != null) {
                    result = portfolioDataManager.isPortfolio(dtSecCode) ? 1 : 0;
                }
            }
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_RESULT, result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 添加自选股
     * return 0，添加失败。1，添加成功。2，自选股超过200
     */
    @JavascriptInterface
    public String addPortfolio(final String json) {
        JSONObject data = null;
        try {
            data = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int result = 0;
        if (data != null) {
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null) {
                final List<StockDbEntity> list = portfolioDataManager.getAllStockList(false, false);
                final int portfolioSize = list == null ? 0 : list.size();
                if (portfolioSize >= CommonConst.MAX_PORTFOLIO_COUNT) {
                    result = 2;
                } else {
                    final JSONArray stocksArray = data.optJSONArray(KEY_STOCKS);
                    SDKManager.getInstance().getDefaultExecutor().execute(() -> {
                        final int size = stocksArray != null ? stocksArray.length() : 0;
                        JSONObject jsonObject = null;
                        for (int i = 0; i < size; i++) {
                            jsonObject = stocksArray.optJSONObject(i);
                            if (jsonObject != null) {
                                portfolioDataManager.addStock(jsonObject.optString(KEY_DT_SEC_CODE), jsonObject.optString(KEY_SEC_NAME));
                            }
                        }
                    });
                    result = 1;
                }
            }
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_RESULT, result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * H5请求本地上报事件
     * @param json
     */
    @JavascriptInterface
    public void reportAction(String json) {
        // 计数统计
        try {
            JSONObject data = new JSONObject(json);
            if (data == null) {
                return;
            }

            int type = data.getInt("type");
            if (type == 0) {
                // 事件统计
                StatisticsUtil.reportAction(data.getString("key"));
                return;
            }

            Map<String, String> map = new HashMap<>();
            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String k = keys.next();
                // 过滤掉count和type属性
                if (!k.equalsIgnoreCase("count") && !k.equalsIgnoreCase("type")) {
                    map.put(k, data.getString(k));
                }
            }

            // 计数统计
            StatisticsUtil.reportAccount(data.getString("key"), map,  data.getInt("count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * h5向native写日志
     * @param json
     */
    @JavascriptInterface
    public void logToNative(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }

        final String level = jsonObject.optString("strLevel");
        final String msg = jsonObject.optString("msg");
        if (TextUtils.isEmpty(level)) {
            DtLog.d("h5", msg);
        } else if (level.equalsIgnoreCase("d")) {
            DtLog.d("h5", msg);
        }else if (level.equalsIgnoreCase("w")) {
            DtLog.w("h5", msg);
        }else if (level.equalsIgnoreCase("e")) {
            DtLog.e("h5", msg);
        } else {
            DtLog.d("h5", msg);
        }
    }

    /**
     * 智能投顾界面，设置输入框
     */
    @JavascriptInterface
    public void setInputValue(final String json) {
        if (mHandler != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObject == null) {
                return;
            }

            final String text = jsonObject.optString("text");
            mHandler.obtainMessage(WebConst.MSG_SET_INPUT_VALUE, text).sendToTarget();
        }
    }

    /**
     * 智能投顾界面，设置输入框
     */
    @JavascriptInterface
    public void sendInfoToNative(final String json) {
        if (mHandler != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObject == null) {
                return;
            }

            mHandler.obtainMessage(WebConst.MSG_SEND_INFO_TO_NATIVE, jsonObject).sendToTarget();
        }
    }

    @JavascriptInterface
    public void popupBox(final String json) {
        if (mHandler != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObject == null) {
                return;
            }

            mHandler.obtainMessage(WebConst.MSG_SHOW_DIALOG, jsonObject).sendToTarget();
        }
    }

    /**
     * 展示网页中的图片列表
     */
    @JavascriptInterface
    public void showImages(final String json) {
        if (json == null) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(json);

            final int curIndex = jsonObject.getInt("curIndex");

            final ArrayList<String> urls = new ArrayList<>();
            JSONArray imageInfos = jsonObject.getJSONArray("imageInfos");
            for (int i = 0; i < imageInfos.length(); i++) {
                JSONObject imageInfo = (JSONObject) imageInfos.get(i);
                String url = (String) imageInfo.get("url");
                urls.add(url);
            }

            final int size = urls.size();
            if (size > 0 && curIndex < size && curIndex >= 0) {
                //show image activity
                ImagePagerActivity.show(SDKManager.getInstance().getContext(), curIndex, urls);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制到剪切板
     */
    @JavascriptInterface
    public void sendCopyInfo(final String json) {
        if (json == null) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }

        DeviceUtil.putToSystemClipboard(SDKManager.getInstance().getContext(), jsonObject.optString("text"));
    }

    /**
     * 获取服务器时间
     * @return
     */
    @JavascriptInterface
    public int getServerTime() {
        TradingStateManager tradingStateManager = TradingStateManager.getInstance();
        return tradingStateManager.getServerTime();
    }

    /**
     * 打开删除评论页面
     */
    @JavascriptInterface
    public void showDeleteCommentPanel(final String json) {
        DtLog.d(TAG, "showDeleteCommentPanel");

        if (json == null) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }

        if (mHandler != null) {
            mHandler.obtainMessage(WebConst.MSG_SHOW_DELETE_COMMENT_DIALOG, jsonObject).sendToTarget();
        }
    }

    /**
     * 评论点赞
     */
    @JavascriptInterface
    public void doLikeComment(final String json) {
        DtLog.d(TAG, "doLikeComment");
        final JSONObject jsonObject = parseJson(json);
        if (jsonObject == null) {
            return;
        }

        final IFeedRequestManager feedRequestManager = (IFeedRequestManager) ComponentManager.getInstance()
                .getManager(IFeedRequestManager.class.getName());
        if (feedRequestManager != null) {
            feedRequestManager.doLikeComment(jsonObject);
        }
    }

    /**
     * 查询评论点赞
     */
    @JavascriptInterface
    public String queryLikeComment(final String json) {
        DtLog.d(TAG, "queryLikeComment");
        final JSONObject jsonObject = parseJson(json);
        if (jsonObject == null) {
            return null;
        }

        JSONObject jsonObj = new JSONObject();
        boolean isAdd = false;
        final IFeedRequestManager feedRequestManager = (IFeedRequestManager) ComponentManager.getInstance()
                .getManager(IFeedRequestManager.class.getName());
        if (feedRequestManager != null) {
            isAdd = feedRequestManager.queryIsLike(jsonObject.optString("feedId"));
        }

        try {
            jsonObj.put("isAdd", isAdd ? 1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DtLog.d(TAG, "queryLikeComment: jsonObj " + jsonObj);
        return jsonObj.toString();
    }

    /**
     * 显示输入对话框
     */
    @JavascriptInterface
    public void popupInputBox(final String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }

        if (mHandler != null) {
            mHandler.obtainMessage(WebConst.MSG_SHOW_INPUT_DIALOG, jsonObject).sendToTarget();
        }
    }

    /**
     * 做积分任务
     */
    @JavascriptInterface
    public void reportFinishedTask(final String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }

        final int taskType = jsonObject.optInt("taskType", -1);
        if (taskType >= 0) {
            final IBonusPointManager bonusPointManager = (IBonusPointManager) ComponentManager.getInstance()
                    .getManager(IBonusPointManager.class.getName());
            if (bonusPointManager != null) {
                bonusPointManager.reportFinishedTask(taskType);
            }

        }
    }

    /**
     * 传递优惠券
     */
    @JavascriptInterface
    public void sendCoupon(final String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }
        if (mHandler != null) {
            mHandler.obtainMessage(WebConst.MSG_SET_COUPON, jsonObject).sendToTarget();
        }
    }

    /**
     * 传递优惠券
     */
    @JavascriptInterface
    public void payResult(final String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }
        if (mHandler != null) {
            mHandler.obtainMessage(WebConst.MSG_H5_PAY_RESULT, jsonObject).sendToTarget();
        }
    }

    /**
     * 传递优惠券
     */
    @JavascriptInterface
    public void sendSignResult(final String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }
        if (mHandler != null) {
            mHandler.obtainMessage(WebConst.MSG_SET_SIGN_RESULT, jsonObject).sendToTarget();
        }
    }

    /**
     * 设置web是否监听back事件
     */
    @JavascriptInterface
    public void setBackButtonListenerEnable(final String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }
        if (mHandler != null) {
            mHandler.obtainMessage(WebConst.MSG_SET_BACK_BUTTON_LISTENER_ENABLE, jsonObject.optInt("enable", 0), 0).sendToTarget();
        }
    }

    @JavascriptInterface
    public void setKeyValue(String jsonStr) {
        final JSONObject jsonObject = parseJson(jsonStr);
        if (jsonObject != null) {
            final String key = jsonObject.optString("key");
            if (!TextUtils.isEmpty(key)) {
                WebSettingPref.putString(key, jsonObject.optString("value"));
            }
        }
    }

    @JavascriptInterface
    public String getKeyValue(String jsonStr) {
        final JSONObject jsonObject = parseJson(jsonStr);
        if (jsonObject != null) {
            final String key = jsonObject.optString("key");
            if (!TextUtils.isEmpty(key)) {
                return WebSettingPref.getString(key, "");
            }
        }
        return "";
    }

    @JavascriptInterface
    public String getLocation4StockMap(String jsonStr) {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(WebConst.MSG_GET_GPS_LOCATION);
        }
        return "";
    }

    private static JSONObject parseJson(final String json) {
        JSONObject jsonObject = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}
