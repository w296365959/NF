package com.sscf.investment.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.payment.CommodityInfo;
import com.dengtacj.component.entity.CouponInfo;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PaymentInfoUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import java.util.ArrayList;
import BEC.CheckUserCouponRsp;
import BEC.E_DT_SUBJECT_RISK_LEVEL;
import BEC.GetPayOrderIdRsp;
import BEC.UserRiskEvalResult;

/**
 * Created by yorkeehuang on 2017/5/13.
 */

public class ConfirmOrderActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {

    private static final String TAG = ConfirmOrderActivity.class.getSimpleName();
    private static final int SELECT_COUPON = 1000;

    private CommodityInfo mCommodityInfo;
    private UserRiskEvalResult mRiskEval;
    private int mNeedSign;
    private CouponInfo mCouponInfo;

    private TextView mCouponTextView;
    private TextView mAmountView;
    private View mRedotView;
    private int mCouponNum;

    private FinishReceiver mReceiver;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    private boolean mIsNotFirstEnter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommodityInfo commodityInfo = (CommodityInfo) getIntent().getSerializableExtra(DengtaConst.EXTRA_COMMODITY_INFO);
        UserRiskEvalResult riskEval = (UserRiskEvalResult) getIntent().getSerializableExtra(DengtaConst.EXTRA_RISK_EVAL);
        int needSign = getIntent().getIntExtra(DengtaConst.EXTRA_NEED_SIGN, 0);
        if(commodityInfo != null && riskEval != null) {
            mCommodityInfo = commodityInfo;
            mRiskEval = riskEval;
            mNeedSign = needSign;
            stat(StatisticsConst.ORDER_CONFIRM_ACTIVITY_DISPLAY);
            DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
            setContentView(R.layout.activity_confirm_order);
            initView();
            registerReceiver();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsNotFirstEnter) {
            if(PaymentRequestManager.requestCheckUserCoupon(this, mCommodityInfo.number,
                    mCommodityInfo.type, mCommodityInfo.unit, mCommodityInfo.extra)) {
                showLoadingDialog();
            }
        } else {
            mIsNotFirstEnter = true;
            if(PaymentInfoUtils.shouldRiskEval(mRiskEval)) {
                showRiskWarningDialog();
            }
        }
    }

    private void stat(String action) {
        if(mCommodityInfo != null) {
            StatisticsUtil.reportAction(action,
                    StatisticsConst.KEY_SUBJECT_TYPE, String.valueOf(mCommodityInfo.type));
        }
    }

    private void registerReceiver() {
        if(mReceiver == null) {
            mReceiver = new FinishReceiver();
        }
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                .registerReceiver(mReceiver, new IntentFilter(ACTION_FINISH));
    }

    private void unregisterReceiver() {
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                    .unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private void initView() {

        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.confirm_order);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        initRiskEvalPanel();

        TextView infoView = (TextView) findViewById(R.id.commodity_info);
        infoView.setText(mCommodityInfo.desc);

        TextView valueView = (TextView) findViewById(R.id.commodity_value);
        valueView.setText(PaymentInfoUtils.getValueText(mCommodityInfo.value));

        mRedotView = findViewById(R.id.redot);
        mRedotView.setVisibility(mCommodityInfo.couponNum > 0 ? View.VISIBLE : View.GONE);
        mCouponNum = mCommodityInfo.couponNum;

        mCouponTextView = (TextView) findViewById(R.id.coupon_text);
        findViewById(R.id.coupon).setOnClickListener(this);
        mAmountView = (TextView) findViewById(R.id.order_amount);
        PaymentInfoUtils.setAmountText(mAmountView, mCommodityInfo.value);

        Button confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(this);
    }

    private void initRiskEvalPanel() {
        if(mRiskEval.getISubjectRiskLevel() > E_DT_SUBJECT_RISK_LEVEL.E_DT_SUBJECT_RISK_NO) {
            stat(StatisticsConst.ORDER_CONFIRM_SHOW_RISK_EVAL_PANEL);
            ((TextView) findViewById(R.id.risk_eval)).setText(mRiskEval.getSUserRiskType());
            findViewById(R.id.risk_eval_panel).setOnClickListener(this);
        } else {
            stat(StatisticsConst.ORDER_CONFIRM_HIDE_RISK_EVAL_PANEL);
            findViewById(R.id.risk_eval_panel).setVisibility(View.GONE);
        }
    }

    private int getCouponValue() {
        return mCouponInfo != null ? mCouponInfo.value : 0;
    }

    private void updateCoupon() {
        int couponValue = getCouponValue();
        if(couponValue > 0) {
            mCouponTextView.setText(PaymentInfoUtils.getValueText(-couponValue));
        } else {
            mCouponTextView.setText(R.string.not_used);
        }
        if(mCommodityInfo != null) {
            PaymentInfoUtils.setAmountText(mAmountView, mCommodityInfo.value - couponValue);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.risk_eval_panel:
                WebBeaconJump.showRiskEval(this, mCommodityInfo.type);
                break;
            case R.id.confirm_button:
                stat(StatisticsConst.ORDER_CONFIRM_SUBMIT);
                if(NetUtil.isNetWorkConnected(this)) {
                    ArrayList<String> couponCodes = null;
                    if(mCouponInfo != null) {
                        couponCodes = new ArrayList<>(1);
                        couponCodes.add(mCouponInfo.code);
                    }
                    PaymentRequestManager.requestPayOrderId(this, mCommodityInfo.number, mCommodityInfo.unit, mCommodityInfo.type, mCommodityInfo.extra, couponCodes);
                    showLoadingDialog();
                } else {
                    DengtaApplication.getApplication().showToast(R.string.network_error);
                }
                break;
            case R.id.coupon:
                stat(StatisticsConst.ORDER_CONFIRM_CLICK_COUPON);
                mRedotView.setVisibility(View.GONE);
                final String couponCode = mCouponInfo != null ? mCouponInfo.code : "";
                String url = DengtaApplication.getApplication().getUrlManager().getUseCoupons(mCommodityInfo.type, mCommodityInfo.value, mCommodityInfo.number, mCommodityInfo.unit, adaptExtra(mCommodityInfo.extra), couponCode);
                DtLog.d(TAG, "url = " + url);
                WebBeaconJump.showCommonWebActivityForResult(this, SELECT_COUPON, url);
                break;
            default:
        }
    }

    private String adaptExtra(String extra) {
        if(!TextUtils.isEmpty(extra)) {
            return extra.replace("\n", "");
        }
        return extra;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_PAY_ORDER_ID:
                handleGetPayOrderId(success, data);
                break;
            case EntityObject.ET_CHECK_USER_COUPON:
                handleCheckUserCoupon(success, data);
                break;
            default:
        }
    }



    private boolean isRiskEvalPanelShow() {
        View view = findViewById(R.id.risk_eval_panel);
        if(view != null) {
            return view.getVisibility() == View.VISIBLE;
        }
        return false;
    }

    private void handleGetPayOrderId(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            if(isRiskEvalPanelShow()) {
                stat(StatisticsConst.ORDER_CONFIRM_SUBMIT_WITH_RISK_EVAL_PANEL_SHOW);
            } else {
                stat(StatisticsConst.ORDER_CONFIRM_SUBMIT_WITH_RISK_EVAL_PANEL_HIDE);
            }
            GetPayOrderIdRsp rsp = (GetPayOrderIdRsp) data.getEntity();
            pay(rsp);
        }
        runOnUiThread(() -> {
            if(!isDestroy()) {
                dismissLoadingDialog();
            }
        });
    }

    private void handleCheckUserCoupon(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            CheckUserCouponRsp rsp = (CheckUserCouponRsp) data.getEntity();
            mRiskEval = rsp.getStRiskResult();
            mNeedSign = rsp.getINeedSign();
            runOnUiThread(() -> {
                ((TextView) findViewById(R.id.risk_eval)).setText(mRiskEval.getSUserRiskType());
                dismissLoadingDialog();
                if(PaymentInfoUtils.shouldRiskEval(rsp)) {
                    showRiskWarningDialog();
                } else if(rsp.getICouponNum() > 0 && rsp.getICouponNum() > mCouponNum) {
                    mCouponNum = rsp.getICouponNum();
                    mRedotView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mUIHandler.post(() -> {
                dismissLoadingDialog();
                showRequestRiskEvalErrorDialog();
            });
        }
    }

    private void showRequestRiskEvalErrorDialog() {
        if(isDestroy()) {
            return;
        }
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(R.string.dialog_title_tips);
        dialog.setMessage(R.string.request_risk_eval_error_msg);
        dialog.addButton(R.string.ok);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                    dialog.dismiss();
                    finish();
                }
                return false;
            }
        });
        dialog.setButtonClickListener((dialog1, view, position) -> {
            switch (position) {
                case 0:
                    dialog1.dismiss();
                    finish();
                    break;
                default:
            }
        });
        dialog.show();
    }

    private void showRiskWarningDialog() {
        if(isDestroy()) {
            return;
        }
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(R.string.risk_eval_title);
        dialog.setMessage(R.string.risk_eval_msg);
        dialog.addButton(R.string.cancel);
        dialog.addButton(R.string.go_risk_eval, ContextCompat.getColor(this, R.color.default_orange_color));
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                    dialog.dismiss();
                    finish();
                }
                return false;
            }
        });
        dialog.setButtonClickListener((dialog1, view, position) -> {
            switch (position) {
                case 0:
                    dialog1.dismiss();
                    finish();
                    break;
                case 1:
                    dialog1.dismiss();
                    WebBeaconJump.showRiskEval(this, mCommodityInfo.type);
                    break;
                default:
            }
        });
        dialog.show();
    }

    private void pay(GetPayOrderIdRsp data) {
        CommonBeaconJump.showPayOrder(this, PaymentInfoUtils.itemToOrderInfo(data.stDtPayItem), mRiskEval, mNeedSign);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode != SELECT_COUPON) {
            return;
        }
        if(resultCode == RESULT_OK) {
            CouponInfo couponInfo = null;
            if(data != null) {
                couponInfo = (CouponInfo) data.getSerializableExtra(DengtaConst.EXTRA_COUPON_INFO);
            }

            if(couponInfo != null && couponInfo.type == mCommodityInfo.type) {
                stat(StatisticsConst.ORDER_CONFIRM_SELECT_COUPON);
                mCouponInfo = couponInfo;
            } else {
                mCouponInfo = null;
            }
        }
        updateCoupon();
    }

    /**
     * 在支付完成后，会收到支付界面的通知，
     * 收到通知后关闭当前订单确认页面。
     */
    public static final String ACTION_FINISH = "action_finish";

    private class FinishReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(ACTION_FINISH.equals(ACTION_FINISH)) {
                if(!ConfirmOrderActivity.this.isDestroy()) {
                    finish();
                }
            }
        }
    }
}
