package com.sscf.investment.scan;

import BEC.ReportAckLoginRsp;
import BEC.ReportScanRsp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.router.ScanJump;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.dengtacj.thoth.MapProtoLite;

public final class ScanLoginActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private int mTargetType;
    private ReportScanRsp mRsp;
    private int mResult = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_scan_login);
        final Intent intent = getIntent();
        mTargetType = intent.getIntExtra("targetType", -1);
        mRsp = (ReportScanRsp) intent.getSerializableExtra("rsp");

        if (mRsp == null || mTargetType < 0) {
            finish();
            return;
        }
        mResult = intent.getIntExtra("result", -1);

        initView();

        StatisticsUtil.reportAction(StatisticsConst.SETTING_SCAN_LOGIN_DISPLAY);
    }

    private void initView() {
        findViewById(R.id.close_button).setOnClickListener(this);

        final ImageView iconView = (ImageView) findViewById(R.id.icon);
        switch (mTargetType) {
            case 0: // tv
                iconView.setImageResource(R.drawable.scan_login_tv_icon);
                break;
            case 1: // pc
                iconView.setImageResource(R.drawable.scan_login_windows_icon);
                break;
            case 2: // mac
                iconView.setImageResource(R.drawable.scan_login_mac_icon);
                break;
            case 3:// tesla
                iconView.setImageResource(R.drawable.scan_login_tesla_icon);
                break;
            default:
                iconView.setImageResource(R.drawable.scan_login_windows_icon);
                break;
        }
        final TextView okButton = (TextView) findViewById(R.id.ok);
        final TextView cancelButton = (TextView) findViewById(R.id.cancal);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        if (mResult != 0) {
            okButton.setText(R.string.scan_retry);
            cancelButton.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.msg1)).setText(mRsp.sTargetMsg);
        ((TextView) findViewById(R.id.msg2)).setText(mRsp.sTipMsg);
    }

    @Override
    public boolean isShakeEnable() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.ok:
                if (mResult != 0) {
                    ScanJump.showScan(this);
                    finish();
                } else {
                    if (NetUtil.isNetWorkConnected(this)) {
                        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                        CloudRequestManager.confirmLogin(accountManager.getAccountInfo().ticket, mRsp.sTicket, mTargetType, this);
                    } else {
                        DengtaApplication.getApplication().showToast(R.string.error_tips_network_error);
                    }
                }
                break;
            case R.id.close_button:
            case R.id.cancal:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_CLOUD_CONFIRM_LOGIN:
                if (success && data.getEntity() != null) {
                    final MapProtoLite packet = (MapProtoLite) data.getEntity();
                    final int res = packet.read("", -1);
                    final ReportAckLoginRsp rsp = packet.read(NetworkConst.RSP, new ReportAckLoginRsp());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res == 0) {
                                DengtaApplication.getApplication().showToast(R.string.login_success);
                            } else {
                                DengtaApplication.getApplication().showToast(rsp.sTipMsg);
                            }
                            finish();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}