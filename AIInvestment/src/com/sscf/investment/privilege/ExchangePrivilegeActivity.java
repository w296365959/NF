package com.sscf.investment.privilege;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.privilege.manager.AccumulatePointsRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ClearButtonEditTextListener;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;
import BEC.AccuPointErrCode;
import BEC.CommitAccuPointCodeRsp;

/**
 * Created by davidwei on 2016/12/19
 * 兑换会员
 */
@Route("ExchangePrivilegeActivity")
public final class ExchangePrivilegeActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback,
        TextWatcher, DialogInterface.OnDismissListener {
    private EditText mEditText;
    private TextView mRightButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privilege_exchange);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.activate_vip);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRightButton = (TextView) findViewById(R.id.actionbar_right_button);
        mRightButton.setText(R.string.commit);
        mRightButton.setOnClickListener(this);
        mRightButton.setEnabled(false);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setHint(R.string.privilege_exchange_hint);
        final View clearButton = findViewById(R.id.clearInputButton);
        new ClearButtonEditTextListener(mEditText, clearButton);
        mEditText.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mEditText);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                StatisticsUtil.reportAction(StatisticsConst.EXCHANGE_PRIVILEGE_CLICKED);
                requestData();
                break;
            default:
                break;
        }
    }

    private void requestData() {
        final String code = getCode();
        if (!TextUtils.isEmpty(code)) {
            showLoadingDialog();
            AccumulatePointsRequestManager.commitExchangeMemberCodeRequest(code, this);
        }
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        dismissLoadingDialog();
        if (success) {
            final Object obj = data.getEntity();
            if (obj != null) {
                final MapProtoLite packet = (MapProtoLite) obj;
                final int res = packet.read("", -1);
                switch (res) {
                    case AccuPointErrCode.E_ACCU_POINT_SUCC:
                        StatisticsUtil.reportAction(StatisticsConst.EXCHANGE_PRIVILEGE_SUCCESS);
                        final CommitAccuPointCodeRsp rsp = packet.read(NetworkConst.RSP, new CommitAccuPointCodeRsp());
                        final String text = rsp.sRetMsg;
                        runOnUiThread(() -> showSuccessDialog(text));
                        ThreadUtils.runOnUiThreadDelay(this::updateMemberState, 2000L);
                        break;
                    case AccuPointErrCode.E_ACCU_POINT_CODE_USED_ALREADY:
                        CommonToast.showToast(R.string.privilege_exchange_failed_tips1);
                        break;
                    case AccuPointErrCode.E_ACCU_POINT_INVALID_CODE:
                        CommonToast.showToast(R.string.privilege_exchange_failed_tips2);
                        break;
                    default:
                        CommonToast.showToast(R.string.privilege_exchange_failed_tips3);
                        break;
                }
                return;
            }
        }
        CommonToast.showToast(R.string.network_invalid_please_check);
    }

    private void updateMemberState() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null) {
            accountManager.updateAccountInfoFromWeb();
        }
    }

    private void showSuccessDialog(String msg) {
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setCanCancelOnTouchOutside(false);
        dialog.setTitle(R.string.dialog_title_tips);
        dialog.setMessage(msg);
        dialog.addButton(R.string.ok);
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mRightButton.setEnabled(!TextUtils.isEmpty(getCode()));
    }

    private String getCode() {
        return mEditText.getText().toString().trim();
    }
}
