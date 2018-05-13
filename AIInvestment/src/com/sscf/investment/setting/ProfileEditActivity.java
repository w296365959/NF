package com.sscf.investment.setting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ConfirmDialog;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.dengtacj.thoth.MapProtoLite;
import BEC.E_ACCOUNT_RET;

/**
 * Created by davidwei on 2016/09/29
 */
public final class ProfileEditActivity extends BaseFragmentActivity implements View.OnClickListener,
        DataSourceProxy.IRequestCallback, Handler.Callback, TextWatcher, TextView.OnEditorActionListener {
    private static final int MAX_INPUT_LENGTH = 50;
    private static final int DISPLAY_TIPS_LENGTH = 400;

    private EditText mEditText;

    private ConfirmDialog mExitConfirmDialog;

    private TextView mWordTips;

    private AccountInfoExt mAccountInfo;

    private String mNewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
        if (accountInfo == null) {
            finish();
        }

        setContentView(R.layout.activity_setting_profile_edit);
        mAccountInfo = accountInfo;

        initViews();

        StatisticsUtil.reportAction(StatisticsConst.SETTING_PROFILE_EDIT_DISPLAY);//编辑个人简介
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.profile_edit);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        TextView saveButton = (TextView) findViewById(R.id.actionbar_right_button);
        saveButton.setText(R.string.save);
        saveButton.setOnClickListener(this);

        mWordTips = (TextView) findViewById(R.id.wordTips);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.addTextChangedListener(this);
        final String profile = mAccountInfo.profile;
        mEditText.setText(profile);
        final int length = profile == null ? 0 : profile.length();
        mEditText.setSelection(length);
        mEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.actionbar_right_button:
                save();
                break;
            case R.id.actionbar_back_button:
                exit();
                break;
            case R.id.ok:
                dismissExitConfirmDialog();
                save();
                break;
            case R.id.cancel:
                dismissExitConfirmDialog();
                finish();
                break;
            default:
                break;
        }
    }

    private void exit() {
        if (TextUtils.equals(mAccountInfo.profile, mEditText.getText().toString())) {
            finish();
        } else {
            showExitConfirmDialog();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void showExitConfirmDialog() {
        if (isDestroy()) {
            return;
        }

        if (mExitConfirmDialog == null) {
            final ConfirmDialog dialog = new ConfirmDialog(this);
            dialog.setMessage(R.string.memo_edit_exit_dialog_message);
            dialog.setCancelButton(R.string.quit, this);
            dialog.setOkButton(R.string.save, this);
            mExitConfirmDialog = dialog;
        }
        mExitConfirmDialog.show();
    }

    private void dismissExitConfirmDialog() {
        if (mExitConfirmDialog != null) {
            mExitConfirmDialog.dismiss();
        }
    }

    private void save() {
        final String profile = mEditText.getText().toString();
        if (profile.length() > MAX_INPUT_LENGTH) {
            DengtaApplication.getApplication().showToast(R.string.profile_edit_max_input_error_tips);
            return;
        }

        if (!TextUtils.equals(mAccountInfo.profile, profile)) {
            showLoadingDialog();
            mNewProfile = profile;
            AccountRequestManager.modifyProfile(profile, mAccountInfo, this);
        } else {
            finish();
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            if (data.getEntityType() != EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO) {
                return;
            }
            final Object entity = data.getEntity();
            if (entity == null || !(entity instanceof MapProtoLite)) {
                return;
            }

            final MapProtoLite packet = (MapProtoLite) entity;
            final int code = packet.read("", -1);
            switch (code) {
                case 0: // 修改成功
                    mAccountInfo.profile = mNewProfile;
                    DengtaApplication.getApplication().getAccountManager().updateLocalAccountInfo(mAccountInfo);
                    finish();
                    break;
                // ticket验证不过，删除用户信息，重新登录
                case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                    DengtaApplication.getApplication().getAccountManager().removeAccountInfo();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.ticket_expired);
                            finish();
                        }
                    });
                    break;
                default:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.error_tips_error_code);
                        }
                    });
                    break;
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DengtaApplication.getApplication().showToast(R.string.error_tips_request_failed);
                }
            });
        }
        dismissLoadingDialog();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final int length = s.toString().length();
        if (length > DISPLAY_TIPS_LENGTH) {
            if (mWordTips.getVisibility() != View.VISIBLE) {
                mWordTips.setVisibility(View.VISIBLE);
            }
            if (length <= MAX_INPUT_LENGTH) {
                mWordTips.setText(getString(R.string.memo_edit_tips1, MAX_INPUT_LENGTH - length));
            } else {
                final Resources res = getResources();
                mWordTips.setText("");
                mWordTips.append(res.getString(R.string.memo_edit_tips2));
                final SpannableString count = new SpannableString(String.valueOf(length - MAX_INPUT_LENGTH));
                count.setSpan(getRedSpan(), 0, count.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mWordTips.append(count);
                mWordTips.append(res.getString(R.string.word));
            }
        } else {
            if (mWordTips.getVisibility() != View.INVISIBLE) {
                mWordTips.setVisibility(View.INVISIBLE);
            }
        }
    }

    private ForegroundColorSpan mRedSpan;

    private ForegroundColorSpan getRedSpan() {
        if (mRedSpan == null) {
            final ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.stock_red_color));
            mRedSpan = span;
        }
        return mRedSpan;
    }

    public static void show(final Context context) {
        Intent intent = new Intent(context, ProfileEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }
}
