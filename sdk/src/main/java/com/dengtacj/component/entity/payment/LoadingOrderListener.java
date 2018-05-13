package com.dengtacj.component.entity.payment;

/**
 * Created by yorkeehuang on 2017/9/20.
 */

public interface LoadingOrderListener {

    public static final int NETWORK_ERROR = -1;

    void OnError(int error);
}
