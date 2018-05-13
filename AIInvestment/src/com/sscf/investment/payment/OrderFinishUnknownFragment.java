package com.sscf.investment.payment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dengtacj.component.entity.payment.OrderInfo;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PaymentInfoUtils;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.component.ui.widget.CommonDialog;
import BEC.E_DT_PAY_ERROR_CODE;
import BEC.E_DT_PAY_STATUS;
import BEC.GetOrderPayResultRsp;

/**
 * Created by yorkeehuang on 2017/2/15.
 */

public class OrderFinishUnknownFragment extends BaseFragment implements View.OnClickListener, DataSourceProxy.IRequestCallback {

    private OrderInfo mOrderInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mOrderInfo = (OrderInfo) getActivity().getIntent().getSerializableExtra(DengtaConst.EXTRA_ORDER_INFO);
        final View root = inflater.inflate(R.layout.fragment_order_finish_known, container, false);
        initView(root);
        return root;
    }

    private void initView(View rootView) {
        final TextView tipsView = (TextView) rootView.findViewById(R.id.tips);
        final Resources res = getResources();
        final SpannableString text = new SpannableString(res.getString(R.string.order_unknown));
        text.setSpan(new ForegroundColorSpan(0xffffbe00), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(24, true), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tipsView.append(text);
        tipsView.append("\n");
        tipsView.append(res.getString(R.string.unknown_buy_vip));

        ((TextView) rootView.findViewById(R.id.commodity_info)).setText(mOrderInfo.name);
        PaymentInfoUtils.setAmountText((TextView) rootView.findViewById(R.id.order_amount), mOrderInfo.amount);

        Button refreshBtn = (Button) rootView.findViewById(R.id.refresh);
        refreshBtn.setOnClickListener(this);

        Button exitBtn = (Button) rootView.findViewById(R.id.exit);
        exitBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                if(!TextUtils.isEmpty(mOrderInfo.id)) {
                    showLoadingDialog();
                    PaymentRequestManager.requestRefreshOrderResult(mOrderInfo.id, mOrderInfo.payType, this);
                }
                break;
            case R.id.exit:
                getActivity().finish();
                break;
        }
    }

    private void showLoadingDialog() {
        Activity activity = getActivity();
        if(activity != null && activity instanceof OrderFinishActivity) {
            ((OrderFinishActivity) activity).showLoadingDialog();
        }
    }

    private void dismissLoadingDialog() {
        Activity activity = getActivity();
        if(activity != null && activity instanceof OrderFinishActivity) {
            ((OrderFinishActivity) activity).dismissLoadingDialog();
        }
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_ORDER_RESULT:
                handleGetOrderPayResult(success, data);
                break;
            default:
        }
    }

    private void handleGetOrderPayResult(final boolean success, final EntityObject data) {
        dismissLoadingDialog();
        if(success && data.getEntity() != null) {
            GetOrderPayResultRsp rsp = (GetOrderPayResultRsp) data.getEntity();
            if(rsp.getIReturnCode() == E_DT_PAY_ERROR_CODE.E_DT_PAY_SUCC) {
                switch (rsp.getIPayStatus()) {
                    case E_DT_PAY_STATUS.E_DT_PAY_SUCCESS:
                        switchFragmentOnUIHandler(getActivity());
                        break;
                    case E_DT_PAY_STATUS.E_DT_PAY_FAIL:
                        showErrorDialog(getActivity());
                        break;
                    default:
                }
            }
        }
    }

    private void showErrorDialog(Activity activity) {
        if(activity != null) {
            activity.runOnUiThread(() -> {
                CommonDialog dialog = new CommonDialog(activity);
                dialog.setTitle(R.string.dialog_title_tips);
                dialog.setMessage(R.string.pay_error_msg);
                dialog.addButton(R.string.ok);
                dialog.setButtonClickListener((dialog1, view, position) -> activity.finish());
                dialog.show();
            });
        }
    }

    private void switchFragmentOnUIHandler(Activity activity) {
        if(activity != null && activity instanceof OrderFinishActivity) {
            activity.runOnUiThread(() ->
                    ((OrderFinishActivity) activity).switchFragment(E_DT_PAY_STATUS.E_DT_PAY_SUCCESS));
        }
    }
}
