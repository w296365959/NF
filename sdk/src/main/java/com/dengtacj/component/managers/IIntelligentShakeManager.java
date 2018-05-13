package com.dengtacj.component.managers;

import android.content.Context;

/**
 * Created by davidwei on 2017-09-21.
 */
public interface IIntelligentShakeManager {
    void registerShakeListener(Context context, OnGetStockInfoCallback callback);
    void unregisterShakeListener();
    void setSensorThreshold(int threshold);
    int getSensorThreshold();
    void setEnable(boolean enable);
    boolean isEnable();

    interface OnGetStockInfoCallback {
        boolean isShakeEnable();
        String getDtCode();
        String getSecName();
    }
}
