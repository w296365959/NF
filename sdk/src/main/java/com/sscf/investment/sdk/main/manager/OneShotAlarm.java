package com.sscf.investment.sdk.main.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2015/9/23.
 */
public class OneShotAlarm extends BroadcastReceiver {
    public static final String KEY_OBJECT_HASH_CODE = "hash_code";
    public static final String KEY_IS_TRADING = "is_trading";
    public static final String ACTION_ONE_SHOT_ALARM = "one_shot_alarm";
    private static final String TAG = OneShotAlarm.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
            DtLog.d(TAG, "onReceive: action = " + action);
            int marketType = intent.getIntExtra(TradingStateManager.KEY_MARKET_TYPE, -1);
            DtLog.d(TAG, "onReceive: marketType = " + marketType);
            int tradingState = intent.getIntExtra(TradingStateManager.KEY_TRADING_STATE, -1);
            DtLog.d(TAG, "onReceive: tradingState = " + tradingState);
            notifyObserversNew(context, marketType, tradingState);
        } else {
            int hashCode = intent.getIntExtra(KEY_OBJECT_HASH_CODE, -1);
            DtLog.d(TAG, "onReceive: hash_code = " + hashCode);

            boolean isTrading = intent.getBooleanExtra(KEY_IS_TRADING, false);
            DtLog.d(TAG, "onReceive: isTrading = " + isTrading);

            notifyObservers(context, hashCode, isTrading);
        }
    }

    private void notifyObserversNew(Context context, int marketType, int tradingState) {
        Intent intent = new Intent(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
        intent.putExtra(TradingStateManager.KEY_MARKET_TYPE, marketType);
        intent.putExtra(TradingStateManager.KEY_TRADING_STATE, tradingState);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void notifyObservers(Context context, int hashCode, boolean isTrading) {
        Intent notificationIntent = new Intent(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
        notificationIntent.putExtra(KEY_OBJECT_HASH_CODE, hashCode);
        notificationIntent.putExtra(KEY_IS_TRADING, isTrading);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(notificationIntent);
    }
}
