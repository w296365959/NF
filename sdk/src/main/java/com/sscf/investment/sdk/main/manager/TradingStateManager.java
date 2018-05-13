package com.sscf.investment.sdk.main.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.dengtacj.component.managers.ITradingStateManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import BEC.E_MARKET_TYPE;
import BEC.E_TRADING_DEAL_TYPE;
import BEC.E_TRADING_TIME_TYPE;
import BEC.TradingTimeDesc;
import BEC.TradingTimeReq;
import BEC.TradingTimeRsp;
import BEC.UserInfo;

/**
 * Created by liqf on 2015/9/25.
 */
public final class TradingStateManager implements ITradingStateManager, DataSourceProxy.IRequestCallback {

    private static final String TAG = TradingStateManager.class.getSimpleName();

    /**
     * 交易状态--未初始化
     */
    public static final int TRADING_STATE_NOT_INITED = -1;

    /**
     * 交易状态--休市
     */
    public static final int TRADING_STATE_RESTING = 0;

    /**
     * 交易状态--交易中
     */
    public static final int TRADING_STATE_TRADING = 1;

    /**
     * 交易状态--集合竞价
     */
    public static final int TRADING_STATE_CALLAUCTION = 2;

    public static final String KEY_TRADING_STATE = "key_trading_state";
    public static final String KEY_MARKET_TYPE = "key_market_type";

    /**
     * 全局的交易时间列表得到更新
     */
    public static final String ACTION_TRADING_STATE_UPDATED = "action_trading_state_updated";

    /**
     * 单个类型市场的交易状态发生了变化
     */
    public static final String ACTION_MARKET_TRADING_STATE_CHANGED = "action_market_trading_state_changed";

    private TradingTimeRsp mTradingTimeRsp;
    private int mGetTradingStateTime;

    /**
     * 是否正在交易。如果为true需要不断刷新实时行情
     */
    private boolean mIsTrading;

    /**
     * 是否是集合竞价期间。目前只有A股和港股有集合竞价
     */
    private boolean mIsInCallauction;

    /**
     * 当天总的交易时长，以分钟为单位
     */
    private int mTradingMinutes;

    private String mOpenTimeStr;
    private String mMiddleTimeStr;
    private String mCloseTimeStr;
    private int mCloseTime;

    private int mOpenTime;

    private int[] mMarketTypeList = new int[] {E_MARKET_TYPE.E_MT_SH, E_MARKET_TYPE.E_MT_SZ, E_MARKET_TYPE.E_MT_HK, E_MARKET_TYPE.E_MT_IC,
            E_MARKET_TYPE.E_MT_NASDAQ, E_MARKET_TYPE.E_MT_NYSE, E_MARKET_TYPE.E_MT_AMEX, E_MARKET_TYPE.E_MT_HIS, E_MARKET_TYPE.E_MT_USI, E_MARKET_TYPE.E_MT_DT};

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
                int marketType = intent.getIntExtra(TradingStateManager.KEY_MARKET_TYPE, -1);
                triggerNextAlarmByMarketType(marketType);
            }
        }
    };

    private static final int RETRY_COMPLETE = -1;
    private static final int MAX_RETRY_TIMES = 3;
    private int mRetryTimes = RETRY_COMPLETE;

    private static TradingStateManager instance;

    public TradingStateManager() {
        IntentFilter filter = new IntentFilter(ACTION_MARKET_TRADING_STATE_CHANGED);
        LocalBroadcastManager.getInstance(SDKManager.getInstance().getContext()).registerReceiver(mBroadcastReceiver, filter);
    }

    public static TradingStateManager getInstance() {
        if (instance == null) {
            instance = new TradingStateManager();
        }
        return instance;
    }

    public void loadTradingTimeData() {
        DtLog.d(TAG, "loadTradingTimeData");
        final TradingTimeReq req = new TradingTimeReq();
        final UserInfo userInfo = SDKManager.getInstance().getUserInfo();
        if (userInfo != null) {
            req.vGuid = userInfo.vGUID;
        }
        req.iLocalTime = (int) (SystemClock.elapsedRealtime() / 1000);
        DataEngine.getInstance().request(EntityObject.ET_QUERY_TRADING_TIME, req, this);
    }

    public int getTradingState(final int marketType) {
        return getTradingState(marketType, true);
    }

    public int getTradingState(final int marketType, boolean ignoreCallauction) {
        if (mTradingTimeRsp == null) {
            return TRADING_STATE_NOT_INITED;
        }

        if (SystemClock.elapsedRealtime() / 1000 - mGetTradingStateTime > DateUtils.HOUR_IN_MILLIS / 1000) {
            loadTradingTimeData(); //距离上次同步时间大于一小时就重新同步一次
        }

        int curState;

        updateTradingState(marketType);

        if (mIsTrading) {
            curState = TRADING_STATE_TRADING;
        } else if(!ignoreCallauction && mIsInCallauction){
            curState = TRADING_STATE_CALLAUCTION;
        } else {
            curState = TRADING_STATE_RESTING;
        }

        return curState;
    }

    public boolean isInCallauction(final int marketType) {
        if (mTradingTimeRsp == null) {
            return false;
        }

        //目前只有A股和港股有集合竞价
        updateTradingState(marketType);

        return mIsInCallauction;
    }

    public boolean isTrading(final int marketType) {
        if (mTradingTimeRsp == null) {
            return false;
        }

        updateTradingState(marketType);

        return mIsTrading;
    }

    public boolean isTradingOrCallauction(final int marketType) {
        if (mTradingTimeRsp == null) {
            return false;
        }

        updateTradingState(marketType);

        return mIsTrading || mIsInCallauction;
    }

    public int getTradingMinutes(int marketType) {
        updateTradingState(marketType);
        return mTradingMinutes;
    }

    public int getOpenTime(int marketType) {
        updateTradingState(marketType);
        return mOpenTime;
    }

    public String getOpenTimeStr(int marketType) {
        updateTradingState(marketType);
        return mOpenTimeStr;
    }

    public String getMiddleTimeStr(int marketType) {
        updateTradingState(marketType);
        return mMiddleTimeStr;
    }

    public String getCloseTimeStr(int marketType) {
        updateTradingState(marketType);
        return mCloseTimeStr;
    }

    public int getCloseTime(int marketType) {
        updateTradingState(marketType);
        return mCloseTime;
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success) {
            DtLog.d(TAG, "loadTradingTimeData failed");

            synchronized (this) {
                if (mRetryTimes == RETRY_COMPLETE) {
                    mRetryTimes = MAX_RETRY_TIMES;
                }

                if (mRetryTimes > 0) {
                    DtLog.d(TAG, "loadTradingTimeData failed, now retry... times = " + mRetryTimes);
                    mRetryTimes--;
                    loadTradingTimeData(); //失败时最多重试3次
                } else {
                    mRetryTimes = RETRY_COMPLETE;
                }
            }

            return;
        }

        synchronized (this) {
            mRetryTimes = RETRY_COMPLETE;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_QUERY_TRADING_TIME:
                final TradingTimeRsp tradingTimeRsp = (TradingTimeRsp) entity.getEntity();
                if (tradingTimeRsp != null) {
                    if(tradingTimeRsp.getINow() <= 0) {
                        tradingTimeRsp.setINow((int) (System.currentTimeMillis() / 1000));
                    }
                    DtLog.d(TAG, "loadTradingTimeData succeed");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            setTradingTime(tradingTimeRsp);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    private void setTradingTime(TradingTimeRsp tradingTimeRsp) {
        mGetTradingStateTime = (int) (SystemClock.elapsedRealtime() / 1000);
        setTradingTimeRsp(tradingTimeRsp);

        ArrayList<TradingTimeDesc> timeDescs = tradingTimeRsp.getVTradingTimeDesc();

        if (timeDescs == null || timeDescs.size() == 0) {
            return;
        }

        triggerNextAlarm();

        notifyTradingStateUpdated();
    }

    private void setTradingTimeRsp(TradingTimeRsp tradingTimeRsp) {
        mTradingTimeRsp = tradingTimeRsp;
    }

    private void triggerNextAlarm() {
        if (mTradingTimeRsp == null) {
            return;
        }

        for (int marketType : mMarketTypeList) {
            triggerNextAlarmByMarketType(marketType);
        }
    }

    private void triggerNextAlarmByMarketType(int marketType) {
        int tradingState;
        int now = getNowServerTime(mTradingTimeRsp);
        if (now <= 0) {
            return;
        }

        ArrayList<TradingTimeDesc> timeDescs = mTradingTimeRsp.getVTradingTimeDesc();

        ArrayList<TradingTimeDesc> currentMarketList = new ArrayList<>();
        for (TradingTimeDesc timeDesc : timeDescs) {
            int type = timeDesc.getEMarketType();
            if (marketType == type) {
                currentMarketList.add(timeDesc);
            }
        }

        Collections.sort(currentMarketList, new Comparator<TradingTimeDesc>() {
            @Override
            public int compare(TradingTimeDesc o1, TradingTimeDesc o2) {
                return o1.getIOpenTime() - o2.getIOpenTime();
            }
        });

        int nextAlarmTime = -1;
        boolean nextIsTrading = false;
        boolean nextIsCallauction = false;
        for(TradingTimeDesc timeDesc : currentMarketList) {
            int openTime = timeDesc.getIOpenTime();
            int closeTime = timeDesc.getICloseTime();
            int tradingTimeType = timeDesc.getETradingTimeType();
            if (now < openTime) {
                nextAlarmTime = openTime - now;
                if (tradingTimeType == E_TRADING_TIME_TYPE.E_TTT_CALLAUCTION) {
                    nextIsCallauction = true;
                    nextIsTrading = false;
                } else {
                    nextIsTrading = true;
                    nextIsCallauction = false;
                }
                break;
            } else if (now <= closeTime) {
                nextAlarmTime = closeTime - now;
                if (tradingTimeType == E_TRADING_TIME_TYPE.E_TTT_CALLAUCTION) {
                    nextIsCallauction = false;
                    nextIsTrading = true;
                } else {
                    nextIsCallauction = false;
                    nextIsTrading = false;
                }
                break;
            }
        }

        //更新集合竞价状态
        if (nextIsTrading) {
            tradingState = TRADING_STATE_TRADING;
        } else if(nextIsCallauction) {
            tradingState = TRADING_STATE_CALLAUCTION;
        } else {
            tradingState = TRADING_STATE_RESTING;
        }

        if (nextAlarmTime != -1 && nextAlarmTime != 0) {
            DtLog.d(TAG, "triggerNextAlarmByMarketType setAlarm marketType = " + marketType + ", nextAlarmTime = " + nextAlarmTime + ", tradingState = " + tradingState);
            setAlarm(marketType, nextAlarmTime, tradingState);
        }
    }

    private void notifyTradingStateUpdated() {
        Intent intent = new Intent(ACTION_TRADING_STATE_UPDATED);
        LocalBroadcastManager.getInstance(SDKManager.getInstance().getContext()).sendBroadcast(intent);
    }

    /**
     * 根据服务器返回的交易时段列表，更新当前查询的marketType对应的各个值
     * @param marketType 当前查询的具体市场类别，如深市、沪市、纳斯达克等
     */
    private void updateTradingState(final int marketType) {
        if (mTradingTimeRsp == null) {
            return;
        }

        int now = getNowServerTime(mTradingTimeRsp);
        ArrayList<TradingTimeDesc> timeDescs = mTradingTimeRsp.getVTradingTimeDesc();

        // 各种交易状态值全部复位
        mIsTrading = false;
        mIsInCallauction = false;
        mTradingMinutes = 0;
        mOpenTimeStr = "";
        mMiddleTimeStr = "";
        mCloseTimeStr = "";

        int firstHalfCloseTime = 0;
        int sencondHalfOpenTime = 0;
        for (TradingTimeDesc timeDesc : timeDescs) {
            int type = timeDesc.getEMarketType();
            if (marketType != type) {
                continue; //不关心的marketType不作处理;
            }

            int tradingDealType = timeDesc.getETradingDealType();

            int openTime = timeDesc.getIOpenTime();
            int closeTime = timeDesc.getICloseTime();

            int tradingTimeType = timeDesc.getETradingTimeType();
            //更新集合竞价状态
            if (tradingTimeType == E_TRADING_TIME_TYPE.E_TTT_CALLAUCTION) {
                if (now >= openTime && now <= closeTime) {
                    mIsInCallauction = true;
                }
            }

            //更新交易状态
            if (!mIsTrading && (tradingDealType == E_TRADING_DEAL_TYPE.E_TDT_OPEN) && !mIsInCallauction) {
                if (now >= openTime && now <= closeTime) {
                    mIsTrading = true;
                }
            }

            if (tradingTimeType != E_TRADING_TIME_TYPE.E_TTT_TRADING) {
                continue; //非正常交易状态不更新显示的交易时间（排除掉集合竞价）
            }

            // 更新表示交易时段的显示字符串，同时累计得出当天总的交易时长
            if (TextUtils.isEmpty(mOpenTimeStr)) {
                mOpenTimeStr = TimeUtils.getTimeString("HH:mm", openTime * 1000L);
                mOpenTime = openTime;
            }

            if (firstHalfCloseTime == 0) {
                firstHalfCloseTime = closeTime;
            } else {
                sencondHalfOpenTime = openTime;
            }

            mCloseTimeStr = TimeUtils.getTimeString("HH:mm", closeTime * 1000L);
            mCloseTime = closeTime;
            mTradingMinutes += closeTime - openTime;
        }

        if (sencondHalfOpenTime != 0) { //有下半场的情况，如A股、港股
            mMiddleTimeStr = TimeUtils.getTimeString("HH:mm", firstHalfCloseTime * 1000L);
            mMiddleTimeStr += "/";
            mMiddleTimeStr += TimeUtils.getTimeString("HH:mm", sencondHalfOpenTime * 1000L);
        }

        mTradingMinutes /= 60;
    }

    private int getNowServerTime(TradingTimeRsp tradingTimeRsp) {
        if (tradingTimeRsp == null) {
            return 0;
        }
        return tradingTimeRsp.getINow() + (int) (SystemClock.elapsedRealtime() / 1000) - mGetTradingStateTime;
    }

    public int getServerTime() {
        if (mTradingTimeRsp == null) {
            return (int) (System.currentTimeMillis() / 1000);
        }
        return mTradingTimeRsp.getINow() + (int) (SystemClock.elapsedRealtime() / 1000) - mGetTradingStateTime;
    }

    private void setAlarm(int marketType, int ellapseTime, int tradingState) {
        if (ellapseTime == -1) {
            return;
        }

        final PendingIntent sender = getSender(marketType, tradingState);

        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long time = elapsedRealtime + ellapseTime * CommonConst.MILLIS_FOR_SECOND;

        DtLog.d(TAG, "setAlarm() marketType = " + marketType
                + ", tradingState = " + tradingState
                + ", time = " + time);

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) SDKManager.getInstance().getContext().getSystemService(Context.ALARM_SERVICE);
        try {
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getSender(final int marketType, final int tradingState) {
        final Context context = SDKManager.getInstance().getContext();
        Intent intent = new Intent(context, OneShotAlarm.class);
        intent.setAction(ACTION_MARKET_TRADING_STATE_CHANGED);
        intent.putExtra(KEY_MARKET_TYPE, marketType);
        intent.putExtra(KEY_TRADING_STATE, tradingState);
        intent.setData(Uri.parse(String.valueOf(marketType)));
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
