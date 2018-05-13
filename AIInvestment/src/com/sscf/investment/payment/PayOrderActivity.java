package com.sscf.investment.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.entity.payment.OrderInfo;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.payment.entity.PaymentMethodEntity;
import com.sscf.investment.payment.presenter.AppPayPresenter;
import com.sscf.investment.payment.presenter.H5PayPresenter;
import com.sscf.investment.payment.presenter.PayPresenter;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.BeaconJump;
import com.sscf.investment.utils.PaymentInfoUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import BEC.E_DT_PAY_TYPE;
import BEC.E_DT_SUBJECT_RISK_LEVEL;
import BEC.E_PAY_USER_AGREEMENT_TEXT_TYPE;
import BEC.PayUserAddAgreement;
import BEC.PayUserAgreement;
import BEC.PayUserAgreementDesc;
import BEC.PayUserAgreementItem;
import BEC.RiskMatchResult;
import BEC.UserRiskEvalResult;

/**
 * davidwei
 * 支付订单界面
 */
@Route("PayOrderActivity")
public final class PayOrderActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final String TAG = PayOrderActivity.class.getSimpleName();

    private static final int SET_SIGN = 1001;
    public static final int H5_PAY = 1002;

    private static final int TOAST_DURATION = 3000;

    private RecyclerView mRecyclerView;
    private PaymentMethodAdapter mAdapter;
    private CheckBox mCheckBox;
    private Button mPayButton;

    private OrderInfo mOrderInfo;
    private int mNeedSign;
    private UserRiskEvalResult mRiskEval;
    private boolean mIsSigned = false;

    private PayPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderInfo = (OrderInfo) getIntent().getSerializableExtra(DengtaConst.EXTRA_ORDER_INFO);
        mRiskEval = (UserRiskEvalResult) getIntent().getSerializableExtra(DengtaConst.EXTRA_RISK_EVAL);
        mNeedSign = getIntent().getIntExtra(DengtaConst.EXTRA_NEED_SIGN, 0);
        if (mOrderInfo == null || mRiskEval == null) {
            DengtaApplication.getApplication().showToast(R.string.order_invalid);
            finish();
            return;
        }
        mIsSigned = false;
        stat(StatisticsConst.PAY_ORDER_ACTIVITY_DISPLAY);
        setContentView(R.layout.activity_pay_order);
        initViews();
        initPresenter();
    }

    private void initPresenter() {
        if(mOrderInfo.thirdPaySource > 0) {
            mPresenter = new H5PayPresenter(this, mOrderInfo);
        } else {
            mPresenter = new AppPayPresenter(this, mOrderInfo);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.pay_order);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        initRiskEvalPanel();

        mPayButton = (Button) findViewById(R.id.payButton);
        mPayButton.setOnClickListener(this);

        ((TextView) findViewById(R.id.commodity_info)).setText(mOrderInfo.name);
        PaymentInfoUtils.setAmountText((TextView) findViewById(R.id.order_amount), mOrderInfo.amount);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        final ArrayList<PaymentMethodEntity> data = new ArrayList<>(2);
        data.add(PaymentMethodEntity.createAlipayEntity());
        data.add(PaymentMethodEntity.createWechatEntity());
        final PaymentMethodAdapter adapter = new PaymentMethodAdapter(this, data, R.layout.payment_pay_method_item);
        adapter.setItemClickable(true);
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;

        mCheckBox = (CheckBox) findViewById(R.id.check);

        updateInstructions((TextView) findViewById(R.id.pay_instructions_info));
    }

    private void initRiskEvalPanel() {
        if(mRiskEval.getISubjectRiskLevel() > E_DT_SUBJECT_RISK_LEVEL.E_DT_SUBJECT_RISK_NO) {
            stat(StatisticsConst.PAY_ORDER_SHOW_RISK_EVAL_PANEL);
            List<RiskMatchResult> matchResultList = mRiskEval.getVMatchResult();
            if(matchResultList != null && !matchResultList.isEmpty()) {
                TextView riskLevelTitle = (TextView) findViewById(R.id.risk_level_title);
                TextView riskLevelResult = (TextView) findViewById(R.id.risk_level_result);
                setMatchResult(riskLevelTitle, riskLevelResult, mRiskEval.getVMatchResult().get(0));

                TextView investProductsTitle = (TextView) findViewById(R.id.invest_products_title);
                TextView investProductsResult = (TextView) findViewById(R.id.invest_products_result);
                setMatchResult(investProductsTitle, investProductsResult, mRiskEval.getVMatchResult().get(1));

                TextView investTermTitle = (TextView) findViewById(R.id.invest_term_title);
                TextView investTermResult = (TextView) findViewById(R.id.invest_term_result);
                setMatchResult(investTermTitle, investTermResult, mRiskEval.getVMatchResult().get(2));

                findViewById(R.id.info).setOnClickListener(this);
            }
        } else {
            stat(StatisticsConst.PAY_ORDER_HIDE_RISK_EVAL_PANEL);
            findViewById(R.id.risk_eval_panel).setVisibility(View.GONE);
        }
    }

    private void setMatchResult(TextView titleView, TextView resultView, RiskMatchResult matchResult) {
        String text;
        if(matchResult.getIMatchResult() > 0) {
            resultView.setTextColor(ContextCompat.getColor(this, R.color.risk_eval_nomatch_color));
            text = getString(R.string.risk_eval_nomatch);
        } else {
            resultView.setTextColor(ContextCompat.getColor(this, R.color.risk_eval_match_color));
            text = getString(R.string.risk_eval_match);
        }
        titleView.setText(matchResult.getSMatchName());
        resultView.setText(text);
    }

    private void showAgreementCheckedToast() {
        ViewStub viewStub = (ViewStub) findViewById(R.id.viewstub_user_agreement_checked_toast);
        if (viewStub == null) {
            return;
        }
        viewStub.inflate();
        View toastLayout = findViewById(R.id.user_agreement_checked_layout);
        toastLayout.postDelayed(() -> toastLayout.setVisibility(View.GONE), TOAST_DURATION);
    }

    private void updateInstructions(final TextView textView) {
        PayUserAgreement agreement = DengtaApplication.getApplication().getPaymentInfoManager().getPayUserAgreement(mOrderInfo.type);
        if(agreement != null) {
            ArrayList<PayUserAgreementItem>  items = agreement.getVItem();
            if(items != null && !items.isEmpty()) {
                PayUserAgreementItem firstItem = items.get(0);
                boolean hasCheckbox = firstItem.getBCheckBox();
                mCheckBox.setVisibility(hasCheckbox ? View.VISIBLE : View.GONE);
                // 如果有checkbox，则按钮依赖于checkbox的状态，否则支付按钮一直可用
                if(!hasCheckbox) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                    int marginLeftRight = getResources().getDimensionPixelSize(R.dimen.actionbar_margin);
                    lp.setMargins(marginLeftRight, 0, marginLeftRight, 0);
                }
                mCheckBox.setChecked(firstItem.getBDefaultCheck());
                if(hasCheckbox && firstItem.getBDefaultCheck()) {
                    showAgreementCheckedToast();
                }

                SpannableStringBuilder builder = new SpannableStringBuilder();
                for(int index=0, size=items.size(); index<size; index++) {
                    PayUserAgreementItem item = items.get(index);
                    ArrayList<PayUserAgreementDesc> descsList = item.getVDesc();
                    descsList = adaptDesc(index, descsList);
                    if(descsList != null && !descsList.isEmpty()) {
                        for(PayUserAgreementDesc desc : descsList) {
                            append(builder, desc);
                        }
                    }
                    builder.append("\n");
                }
                adaptItem(builder);
                textView.setText(builder.subSequence(0, builder.length() - 1));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private ArrayList<PayUserAgreementDesc> adaptDesc(final int currentIndex, ArrayList<PayUserAgreementDesc> descsList) {
        if(descsList != null) {
            PayUserAddAgreement addAgreement = mRiskEval.getStAddAgreement();
            if(addAgreement != null) {
                if(addAgreement.getIIndex() == currentIndex) {
                    ArrayList<PayUserAgreementDesc> adaptList = new ArrayList<>();
                    for(PayUserAgreementDesc desc : descsList) {
                        adaptList.add(desc);
                    }
                    final int operaType = addAgreement.getIType();
                    final PayUserAgreementDesc desc = addAgreement.getStDynamicAgreement();
                    if(desc != null && operaType == 0) {
                        adaptList.add(desc);
                    }
                    return adaptList;
                }
            }
        }
        return descsList;
    }

    private void adaptItem(SpannableStringBuilder builder) {
        PayUserAddAgreement addAgreement = mRiskEval.getStAddAgreement();
        if(addAgreement != null) {
            final int operaType = addAgreement.getIType();
            final PayUserAgreementDesc desc = addAgreement.getStDynamicAgreement();
            if(desc != null && operaType == 1) {
                append(builder, desc);
            }
        }
    }

    private void append(SpannableStringBuilder builder, PayUserAgreementDesc desc) {
        switch (desc.getITextType()) {
            case E_PAY_USER_AGREEMENT_TEXT_TYPE.E_PAY_USER_AGREEMENT_TEXT_WORD:
                builder.append(createText(desc));
                break;
            case E_PAY_USER_AGREEMENT_TEXT_TYPE.E_PAY_USER_AGREEMENT_TEXT_URL:
                builder.append(createStyle(desc));
                break;
            default:
        }
    }

    private String createText(PayUserAgreementDesc desc) {
        if(desc.getBLineFeed()) {
            return desc.getSText() + "\n";
        } else {
            return desc.getSText();
        }
    }

    private SpannableStringBuilder createStyle(PayUserAgreementDesc desc) {
        final String text = desc.getSText();
        final String url = desc.getSTextUrl();
        DtLog.d(TAG, "createStyle() text = " + text + ", url = " + url);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new TextViewURLSpan(url), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(desc.getBLineFeed()) {
            style.append("\n");
        }
        return style;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoadingDialog();
    }

    @Override
    public boolean isShakeEnable() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.payButton:
                stat(StatisticsConst.PAY_ORDER_CLICK_PAY);
                if(shouldSign()) {
                    if(mIsSigned && mCheckBox.isChecked()) {
                        pay();
                    } else if(mCheckBox.isChecked()) {
                        goSign();
                    } else {
                        showSignDialog();
                    }
                } else {
                    if(mCheckBox.isChecked()) {
                        pay();
                    } else {
                        showAcceptDialog();
                    }
                }
                break;
            case R.id.info:
                RiskEvalInfoDialog dialog = new RiskEvalInfoDialog(this, mRiskEval.getVMatchResult());
                dialog.show();
                break;
            default:
                break;
        }
    }

    private boolean shouldSign() {
        return mNeedSign > 0;
    }

    private void showAcceptDialog() {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(R.string.accept_agreement);
        dialog.setMessage(R.string.agreement_msg);
        dialog.addButton(R.string.cancel);
        dialog.addButton(R.string.confirm_accpet, ContextCompat.getColor(this, R.color.default_orange_color));
        dialog.setButtonClickListener(new CommonDialog.OnDialogButtonClickListener() {

            @Override
            public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        dialog.dismiss();
                        pay();
                        break;
                }
            }
        });
        dialog.show();
    }

    private void showSignDialog() {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(R.string.read_agreement);
        dialog.setMessage(R.string.read_agreement_msg);
        dialog.addButton(R.string.cancel);
        dialog.addButton(R.string.ok, ContextCompat.getColor(this, R.color.default_orange_color));
        dialog.setButtonClickListener(new CommonDialog.OnDialogButtonClickListener() {

            @Override
            public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        dialog.dismiss();
                        goSign();
                        mCheckBox.setChecked(true);
                        break;
                }
            }
        });
        dialog.show();
    }

    private void goSign() {
        StatisticsUtil.reportAction(StatisticsConst.PAY_ORDER_GO_SIGN_AGREEMENT_BEFORE_PAY);
        ArrayList<RiskMatchResult> matchResults = mRiskEval.getVMatchResult();

        if(matchResults != null && !matchResults.isEmpty()) {
            boolean riskSuitable = true;
            for(RiskMatchResult matchResult : matchResults) {
                if(matchResult.getIMatchResult() > 0) {
                    riskSuitable = false;
                    break;
                }
            }
            String url = WebUrlManager.getInstance().getProtocalCollectUp(mOrderInfo.id, mRiskEval.getSSubjectRiskDesc(), riskSuitable ? 1 : 0);
            WebBeaconJump.showCommonWebActivityForResult(PayOrderActivity.this, SET_SIGN, url);
        }
    }

    public void stat(String action) {
        if(mOrderInfo != null) {
            StatisticsUtil.reportAction(action,
                    StatisticsConst.KEY_SUBJECT_TYPE, String.valueOf(mOrderInfo.type));
        }
    }

    private boolean isRiskEvalPanelShow() {
        View view = findViewById(R.id.risk_eval_panel);
        if(view != null) {
            return view.getVisibility() == View.VISIBLE;
        }
        return false;
    }

    private void pay() {
        final int payType = mAdapter.getPayType();
        if(mPresenter.pay(payType)) {
            switch (payType) {
                case E_DT_PAY_TYPE.E_DT_PAY_ALI: // 支付宝
                    stat(StatisticsConst.PAY_ORDER_ALI_PAY);
                    if(isRiskEvalPanelShow()) {
                        stat(StatisticsConst.PAY_ORDER_WITH_RISK_PANEL_SHOW);
                    } else {
                        stat(StatisticsConst.PAY_ORDER_WITH_RISK_PANEL_HIDE);
                    }
                    break;
                case E_DT_PAY_TYPE.E_DT_PAY_WX: // 微信
                    stat(StatisticsConst.PAY_ORDER_WX_PAY);
                    if(isRiskEvalPanelShow()) {
                        stat(StatisticsConst.PAY_ORDER_WITH_RISK_PANEL_SHOW);
                    } else {
                        stat(StatisticsConst.PAY_ORDER_WITH_RISK_PANEL_HIDE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SET_SIGN:
                handleSignResult(resultCode);
                break;
            case H5_PAY:
                if(mPresenter instanceof H5PayPresenter) {
                    ((H5PayPresenter)mPresenter).handleH5PayResult(resultCode, data);
                }
                break;
            default:
        }
    }

    private void handleSignResult(final int resultCode) {
        mIsSigned = (resultCode == RESULT_OK);
        if(mIsSigned) {
            pay();
        }
    }

    private void finishConfirmOrderActivity() {
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(new Intent(ConfirmOrderActivity.ACTION_FINISH));
    }

    public void showErrorDialog() {
        if (isDestroy()) {
            return;
        }

        runOnUiThread(() -> {
            CommonDialog dialog = new CommonDialog(PayOrderActivity.this);
            dialog.setTitle(R.string.dialog_title_tips);
            dialog.setMessage(R.string.pay_error_msg);
            dialog.addButton(R.string.ok);
            dialog.setButtonClickListener((dialog1, view, position) -> finish());
            dialog.show();
        });
    }

    public void showOverTimeDialog() {
        runOnUiThread(() -> {
            CommonDialog dialog = new CommonDialog(PayOrderActivity.this);
            dialog.setTitle(R.string.dialog_title_tips);
            dialog.setMessage(R.string.pay_oreder_overtime_msg);
            dialog.addButton(R.string.ok);
            dialog.setButtonClickListener((dialog1, view, position) -> finish());
            dialog.show();
        });
    }

    public void goOrderFinish() {
        stat(StatisticsConst.ORDER_FINISH_ACTIVITY_DISPLAY);
        BeaconJump.showOrderFinish(this, mOrderInfo);
        // 支付完成后关闭订单确认界面
        finishConfirmOrderActivity();
        finish();
    }

    public void goOrderUnknown() {
        BeaconJump.showOrderUnknown(this, mOrderInfo);
        // 支付完成后关闭订单确认界面
        finishConfirmOrderActivity();
        finish();
    }

    private class TextViewURLSpan extends ClickableSpan {

        private String mUrl;

        public TextViewURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.setColor(getResources().getColor(R.color.link_color));
            paint.setUnderlineText(false); //去掉下划线
            paint.setFakeBoldText(true);
        }

        @Override
        public void onClick(View widget) {
            DtLog.d(TAG, "onClick() mUrl = " + mUrl);
            if(!TextUtils.isEmpty(mUrl)) {
                String url = appendParam(mUrl);
                DtLog.d(TAG, "onClick() url = " + url);
                WebBeaconJump.showCommonWebActivity(PayOrderActivity.this, url);
            }
        }

        private String appendParam(String url) {
            if(!TextUtils.isEmpty(url)) {
                if(url.indexOf("?") > 0) {
                    return url + "&orderId=" + mOrderInfo.id;
                } else {
                    return url + "?orderId=" + mOrderInfo.id;
                }
            }
            return url;
        }
    }
}
