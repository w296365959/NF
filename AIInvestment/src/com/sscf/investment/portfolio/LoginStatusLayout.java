package com.sscf.investment.portfolio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.support.v7.widget.AppCompatTextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.entity.AccountInfoEntity;

/**
 * Created by xuebinliu on 2015/8/11.
 *
 * 自选界面，底部的用户登录状态bar
 */
public class LoginStatusLayout extends AppCompatTextView implements View.OnClickListener {

    public LoginStatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 未登录时，点击进入登录界面
        if (!DengtaApplication.getApplication().getAccountManager().isLogined()) {
            CommonBeaconJump.showLogin(getContext());
        }
    }

    /**
     * 更新用户状态
     */
    public void updateUserInfo() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        final AccountInfoEntity accountInfo = accountManager == null ? null : accountManager.getAccountInfo();
        if (accountInfo == null) {
            setUserName(null);
        } else {
            setUserName(accountInfo.nickname);
        }
    }

    /**
     * 设置登录信息，
     * @param name 如果为null，则没有登录
     */
    public void setUserName(String name) {
        if (name == null) {
            // 没有登录
            setText(R.string.portfolio_unlogin_tips);
        } else {
            setText("");
            append(getResources().getString(R.string.portfolio_login_tips));
            append(name);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerBroadcastReceiver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterBroadcastReceiver();
    }

    private BroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(SettingConst.ACTION_LOGOUT_SUCCESS);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                updateUserInfo();
            } else if (SettingConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
                updateUserInfo();
            }
        }
    }
}