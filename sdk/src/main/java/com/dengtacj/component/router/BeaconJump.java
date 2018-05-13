package com.dengtacj.component.router;

import android.content.Context;
import android.os.Bundle;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.payment.CommodityInfo;
import com.dengtacj.component.entity.payment.LoadingOrderListener;
import com.dengtacj.component.entity.payment.OrderInfo;
import com.dengtacj.component.managers.IOrderManager;
import com.sscf.investment.sdk.utils.CommonConst;

import BEC.E_DT_PAY_STATUS;

/**
 * Created by davidwei on 2016/08/01
 */
public final class BeaconJump {

    public static void showPdfViewer(final Context context, final String uri) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CommonConst.EXTRA_DATA, uri);
        CommonBeaconJump.showActivity(context, "PdfViewerActivity", bundle);
    }

    public static void showLoadingOrder(final Context context, int type, int number, String desc, int value, int unit, String extra) {
        showLoadingOrder(context, new CommodityInfo(type, number, desc, value, unit, extra), null);
    }

    public static void showLoadingOrder(final Context context, final CommodityInfo commodityInfo, final LoadingOrderListener listener) {
        IOrderManager orderManager = (IOrderManager) ComponentManager.getInstance().getManager(IOrderManager.class.getName());
        if(orderManager != null) {
            orderManager.showLoadingOrder(context, commodityInfo, listener);
        }
    }

    public static void showOrderFinish(final Context context, final OrderInfo order) {
        order.status = E_DT_PAY_STATUS.E_DT_PAY_SUCCESS;
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CommonConst.EXTRA_ORDER_INFO, order);
        CommonBeaconJump.showActivity(context, "OrderFinishActivity", bundle);
    }

    public static void showOrderUnknown(final Context context, final OrderInfo order) {
        order.status = E_DT_PAY_STATUS.E_DT_PAY_WATING_PAY;
        final Bundle bundle = new Bundle();
        bundle.putSerializable(CommonConst.EXTRA_ORDER_INFO, order);
        CommonBeaconJump.showActivity(context, "OrderFinishActivity", bundle);
    }

    public static void showPortfolioRemindActivity(final Context context, final String dtSecCode) {
        final Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEC_CODE, dtSecCode);
        CommonBeaconJump.showActivity(context, "PortfolioRemindActivity", bundle);
    }

    public static void showSmartStockStareActivity(final Context context, final String dtSecCode, final String secName) {
        final Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEC_CODE, dtSecCode);
        bundle.putString(CommonConst.KEY_SEC_NAME, secName);
        CommonBeaconJump.showActivity(context, "SmartStockStareActivity", bundle);
    }
}
