package com.sscf.investment.detail.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.sscf.investment.detail.dialog.HongKongQuoteDialog;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import com.dengtacj.component.router.WebBeaconJump;

import java.util.ArrayList;
import BEC.SecBaseInfo;
import BEC.SecBaseInfoRsp;
import BEC.SecInfo;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017/05/08
 */
public final class HongKongQuotePresenter implements Handler.Callback, Runnable, DataSourceProxy.IRequestCallback {
    private HongKongQuoteDialog mDialog;
    protected final Handler mHandler;
    protected final PeriodicHandlerManager mPeriodicHandlerManager;
    private final String mHDtSecCode;
    private final String mHSecName;
    private String mADtSecCode;
    private String mASecName;

    public HongKongQuotePresenter(HongKongQuoteDialog dialog, String hDtSecCode, String hSecName) {
        mDialog = dialog;
        mHDtSecCode = hDtSecCode;
        mHSecName = hSecName;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
    }

    public void setBaseInfo(final SecBaseInfoRsp rsp) {
        final ArrayList<SecInfo> stockList = rsp.getVSecInfo();
        if (stockList == null || stockList.size() == 0) {
            return;
        }
        final SecInfo secInfo = stockList.get(0);
        getRelatedADtSecCode(secInfo);
        refresh();
    }

    private void getRelatedADtSecCode(final SecInfo secInfo) {
        final ArrayList<SecBaseInfo> relatedSecBaseInfos = secInfo.getVRelateSecInfo();
        if (relatedSecBaseInfos != null && relatedSecBaseInfos.size() > 0) {
            for (SecBaseInfo relatedSecBaseInfo : relatedSecBaseInfos) {
                String dtSecCode = relatedSecBaseInfo.getSDtSecCode();
                if (StockUtil.isChineseMarket(dtSecCode)) {
                    mADtSecCode = dtSecCode;
                    mASecName = relatedSecBaseInfo.sCHNShortName;
                    break;
                }
            }
        }
    }

    public void refresh() {
        if (!TextUtils.isEmpty(mADtSecCode)) {
            mPeriodicHandlerManager.runPeriodic();
        }
    }

    @Override
    public void run() {
        QuoteRequestManager.getSimpleQuoteRequest(mADtSecCode, this, null);
    }

    public void release() {
        mPeriodicHandlerManager.stop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SIMPLE_QUOTE:
                final SecSimpleQuote quote = EntityUtil.entityToSecSimpleQuote(success, data);
                if (quote != null) {
                    mHandler.obtainMessage(0, quote).sendToTarget();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        mDialog.updateAHpremuim((SecSimpleQuote) msg.obj);
        return true;
    }

    public void clickShowAHPremium(final Context context) {
        WebBeaconJump.showAhPremiumDetail(context, mADtSecCode, mASecName, mHDtSecCode, mHSecName);
    }
}
