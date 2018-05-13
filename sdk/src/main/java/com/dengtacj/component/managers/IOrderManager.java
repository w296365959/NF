package com.dengtacj.component.managers;

import android.content.Context;

import com.dengtacj.component.entity.payment.CommodityInfo;
import com.dengtacj.component.entity.payment.LoadingOrderListener;

/**
 * Created by yorkeehuang on 2017/9/20.
 */

public interface IOrderManager {
    void showLoadingOrder(final Context context, final CommodityInfo commodityInfo, final LoadingOrderListener listener);
}
