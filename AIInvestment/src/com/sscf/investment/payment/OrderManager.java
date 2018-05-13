package com.sscf.investment.payment;

import android.content.Context;

import com.dengtacj.component.entity.payment.CommodityInfo;
import com.dengtacj.component.entity.payment.LoadingOrderListener;
import com.dengtacj.component.managers.IOrderManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.sdk.utils.NetUtil;

/**
 * Created by yorkeehuang on 2017/9/20.
 */

public class OrderManager implements IOrderManager {

    @Override
    public void showLoadingOrder(Context context, CommodityInfo commodityInfo, LoadingOrderListener listener) {
        try {
            if(context != null && context instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) context;
                if(NetUtil.isNetWorkConnected(context)) {
                    LoadingOrderDialog loadingOrderDialog = new LoadingOrderDialog(activity);
                    loadingOrderDialog.setCommodityInfo(commodityInfo);
                    loadingOrderDialog.show();
                } else {
                    if(listener != null) {
                        listener.OnError(LoadingOrderListener.NETWORK_ERROR);
                    } else {
                        showErrorDialog(activity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isActivityDestroy(BaseActivity activity) {
        if(activity != null) {
            return activity.isDestroy();
        }
        return true;
    }

    private void showErrorDialog(BaseActivity activity) {
        if (isActivityDestroy(activity)) {
            return;
        }
        activity.runOnUiThread(() -> {
            if (isActivityDestroy(activity)) {
                return;
            }
            final CommonDialog dialog = new CommonDialog(activity);
            dialog.setMessage(activity.getString(R.string.network_error));
            dialog.addButton(R.string.ok);
            dialog.setButtonClickListener((dialog1, view, position) -> {
                dialog1.dismiss();
            });
            dialog.show();
        });
    }
}
