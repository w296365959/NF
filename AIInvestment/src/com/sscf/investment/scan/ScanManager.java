package com.sscf.investment.scan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IScanManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.LoadingDialog;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.PackageUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.dengtacj.thoth.MapProtoLite;
import com.sscf.investment.component.ui.widget.CommonDialog;
import BEC.ReportScanRsp;

public final class ScanManager implements DataSourceProxy.IRequestCallback, IScanManager, Handler.Callback {
    private static final String TAG = ScanManager.class.getSimpleName();
    private int mTargetType;
    private Handler mHandler;

    public void handleResult(final Activity activity, final Intent resIntent) {
        if (activity == null || resIntent == null) {
            return;
        }

        final String result = resIntent.getStringExtra("res");
        final Uri uri = resIntent.getData();
        if (!TextUtils.isEmpty(result)) {
            handleScanResult(activity, result);
        } else if (uri != null) {
            handlePictureResult(activity, uri);
        }
    }

    private void handlePictureResult(final Activity activity, Uri uri) {
        if (!NetUtil.isNetWorkConnected(activity)) {
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }

        Intent intent = new Intent(activity, OcrResultActivity.class);
        intent.putExtra(OcrResultActivity.EXTRA_IMAGE_URI, uri);
        activity.startActivity(intent);
    }

    private void handleScanResult(final Activity activity, final String result) {
        DtLog.d(TAG, "result : " + result);

        final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
        String[] domainList = null;
        if (scheme != null) {
            domainList = scheme.getDomainList();
        }
        final int length = domainList == null ? 0 : domainList.length;

        final String qrcodeUrl = DengtaApplication.getApplication().getUrlManager().getQrcodeUrl();
        DtLog.d(TAG, "qrcodeUrl : " + qrcodeUrl);

        if (result.contains(qrcodeUrl)) { // 扫描二维码逻辑
            if (!NetUtil.isNetWorkConnected(activity)) {
                DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                return;
            }
            handleQrcode(activity, result);
            return;
        }

        StatisticsUtil.reportAction(StatisticsConst.SCAN_SUCCESS);

        for (int i = 0; i < length; i++) {
            if (result.contains(domainList[i])) { // 白名单地址,用webview打开
                WebBeaconJump.showCommonWebActivity(activity, result);
                return;
            }
        }

        if (result.startsWith("http")) {
            // 其他链接跳转浏览器
            showConfirmDialog(activity, result);
        } else {
            final Intent intent = new Intent(activity, ScanStringResultActivity.class);
            intent.putExtra("result", result);
            activity.startActivity(intent);
        }
    }

    private void showConfirmDialog(final Activity activity, final String result) {
        final CommonDialog dialog = new CommonDialog(activity);
        dialog.setMessage(R.string.visit_outer_link_msg);
        dialog.addButton(R.string.ok);
        dialog.addButton(R.string.cancel);
        dialog.setButtonClickListener(new ConfirmDialogButtonClickListener(result));
        dialog.setCanCancelOnTouchOutside(true);
        dialog.show();
    }

    /**
     * t 功能，1为扫码登录，方便以后扩展
     * p 平台，1.tv，2.pc
     * s sessionId
     * @param result
     */
    private void handleQrcode(final Activity activity, final String result) {
        final Uri uri = Uri.parse(result);
        final String type = uri.getQueryParameter("t");
        final int scanType = StringUtil.parseInt(type, -1);
        switch (scanType) {
            case 1: // 扫码登录
                final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                if (accountManager.isLogined()) {
                    final int from = StringUtil.parseInt(uri.getQueryParameter("p"), -1);
                    // 0,tv 1,pc
                    mTargetType = from;
                    if (from > -1) {
                        final String session = uri.getQueryParameter("s");
                        mActivity = activity;
                        CloudRequestManager.reportScan(accountManager.getAccountInfo().ticket, session, from, this);
                        showLoadingDialogDelay(activity, 500L);
                    }
                } else { // 未登录跳转到登录界面
                    CommonBeaconJump.showLogin(activity);
                }
                break;
            default:
                break;
        }
    }

    private Activity mActivity;
    @Override
    public void callback(boolean success, EntityObject data) {
        final Activity activity = mActivity;
        mActivity = null;
        dismissLoadingDialog();
        switch (data.getEntityType()) {
            case EntityObject.ET_CLOUD_REPORT_SCAN:
                if (success && data.getEntity() != null) {
                    final MapProtoLite packet = (MapProtoLite) data.getEntity();
                    final Intent intent = new Intent(activity, ScanLoginActivity.class);
                    final ReportScanRsp rsp = packet.read(NetworkConst.RSP, new ReportScanRsp());
                    intent.putExtra("targetType", mTargetType);
                    intent.putExtra("rsp", rsp);
                    intent.putExtra("result", packet.read("", -1));
                    activity.startActivity(intent);
                } else {
                    // 失败处理
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }


    private LoadingDialog mLoadingDialog;

    private void showLoadingDialogDelay(final Activity activity, long delay) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper(), this);
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0, activity), delay);
    }

    @Override
    public boolean handleMessage(Message msg) {
        final Activity activity = (Activity) msg.obj;
        mLoadingDialog = new LoadingDialog(activity);
        mLoadingDialog.show();
        return true;
    }

    private void dismissLoadingDialog() {
        mHandler.removeMessages(0);
        final LoadingDialog loadingDialog = mLoadingDialog;
        if (loadingDialog != null) {
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}

final class ConfirmDialogButtonClickListener implements CommonDialog.OnDialogButtonClickListener {
    private final String mResult;

    ConfirmDialogButtonClickListener(final String result) {
        mResult = result;
    }

    @Override
    public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
        switch (position) {
            case 0:
                //用默认浏览器打开扫描得到的地址
                PackageUtil.openUrl(dialog.getContext(), mResult);
                break;
            default:
                break;
        }
        dialog.dismiss();
    }
}