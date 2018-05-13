package com.sscf.investment.detail.presenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.sscf.investment.detail.dialog.FundQuoteDialog;
import com.sscf.investment.detail.dialog.HandicapDialog;
import com.sscf.investment.detail.dialog.HongKongQuoteDialog;
import com.sscf.investment.detail.dialog.HongKongUSAIndexQuoteDialog;
import com.sscf.investment.detail.dialog.IQuoteDialog;
import com.sscf.investment.detail.dialog.IndexFutureQuoteDialogQuoteDialog;
import com.sscf.investment.detail.dialog.IndexQuoteDialogQuoteDialog;
import com.sscf.investment.detail.dialog.NewThirdBoardQuoteDialog;
import com.sscf.investment.detail.dialog.USAQuoteDialog;
import com.sscf.investment.detail.fragment.LineChartFragment;
import com.sscf.investment.detail.model.SecurityDetailModel;
import com.sscf.investment.detail.view.ISecurityDetailView;
import com.sscf.investment.detail.view.KLineInfosView;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import java.util.ArrayList;
import BEC.E_MARKET_TYPE;
import BEC.HisChipDistRsp;
import BEC.SecBaseInfoRsp;
import BEC.SecLiveMsg;
import BEC.SecLiveMsgRsp;
import BEC.SecQuote;

/**
 * Created by davidwei on 2017/04/24
 */
public final class SecurityDetailPresenter implements Runnable, Handler.Callback {
    private static final int MSG_UPDATE_QUOTE_VIEW = 1;
    private static final int MSG_UPDATE_FAILED = 2;
    private static final int MSG_UPDATE_BASE_INFO = 3;
    private static final int MSG_UPDATE_LIVE_MSG = 4;
    private static final int MSG_UPDATE_CHIP_DIST = 5;

    // V
    private final ISecurityDetailView mView;
    private final LineChartFragment mLineChartFragment;
    private IQuoteDialog mQuoteDialog;

    // M
    private final SecurityDetailModel mModel;

    // P
    private HandicapPresenter mHandicapPresenter;
    private HongKongQuotePresenter mHongKongQuotePresenter;

    protected final Handler mHandler;
    protected final PeriodicHandlerManager mPeriodicHandlerManager;

    private final String mDtSecCode;

    private SecQuote mQuote;
    private SecBaseInfoRsp mBaseInfo;
    private String mLastMsgId;

    private boolean mIsTrading;

    public SecurityDetailPresenter(final ISecurityDetailView view, final LineChartFragment lineChartFragment, String dtSecCode) {
        mView = view;
        mLineChartFragment = lineChartFragment;
        lineChartFragment.setPresenter(this);
        mModel = new SecurityDetailModel(this);

        mHandler = new Handler(Looper.getMainLooper(), this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        mDtSecCode = dtSecCode;
    }

    public void refresh() {
        mPeriodicHandlerManager.runPeriodic();
        if (mHandicapPresenter != null) {
            mHandicapPresenter.refresh();
        }
        if (mHongKongQuotePresenter != null) {
            mHongKongQuotePresenter.refresh();
        }

        if(StockUtil.supportChipEntrance(mDtSecCode) && DengtaApplication.getApplication().getAccountManager().isMember()) {
            mModel.requestChipDist(mDtSecCode);
        }
    }

    public void refreshDelay(int delay) {
        mPeriodicHandlerManager.runPeriodicDelay(delay);
    }

    public void stopRefresh() {
        mPeriodicHandlerManager.stop();
        if (mHandicapPresenter != null) {
            mHandicapPresenter.stopRefresh();
        }
    }

    public void setTrading(final boolean trading) {
        mIsTrading = trading;
    }

    public void release() {
        if (mQuoteDialog != null && mQuoteDialog instanceof Dialog) {
            ((Dialog) mQuoteDialog).dismiss();
        }
        mPeriodicHandlerManager.stop();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void releaseDialog() {
        mQuoteDialog = null;
        mHandicapPresenter = null;
        mHongKongQuotePresenter = null;
    }

    @Override
    public void run() {
        mModel.requestQuote(mDtSecCode);

        if (mBaseInfo == null) {
            mModel.requestBaseInfo(mDtSecCode);
        }

        final boolean visible = mView.getUserVisibleHint();

        if (visible) {
            if (StockUtil.supportSecLiveMsg(mDtSecCode) && DengtaSettingPref.isStockLiveMsgEnabled()) {
                mModel.requestLiveMsg(mDtSecCode, mLastMsgId);
            }
        }

        if (mIsTrading && visible) {
            mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        } else {
            stopRefresh();
        }
    }

    public void onGetQuote(final SecQuote quote) {
        if (quote != null) {
            mHandler.obtainMessage(MSG_UPDATE_QUOTE_VIEW, quote).sendToTarget();
        }
    }

    public void onGetChipDistRsp(final HisChipDistRsp chipDistRsp) {
        if (chipDistRsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_CHIP_DIST, chipDistRsp).sendToTarget();
        }
    }

    public void onGetBaseInfo(final SecBaseInfoRsp rsp) {
        mBaseInfo = rsp;
        mHandler.obtainMessage(MSG_UPDATE_BASE_INFO, rsp).sendToTarget();
    }

    public void onGetLiveMsg(final SecLiveMsgRsp rsp) {
        if (rsp != null) {
            mHandler.obtainMessage(MSG_UPDATE_LIVE_MSG, rsp).sendToTarget();
        }
    }

    public void onKLineTouch(final KLineInfosView.KLineLineTouchEvent event) {
        if (event != null) {
            mView.updateKLineQuoteView(event, mQuote);
        } else {
            mView.updateQuoteView(mQuote);
        }
    }

    public SecQuote getQuote() {
        return mQuote;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_QUOTE_VIEW:
                final SecQuote quote = (SecQuote) msg.obj;
                mQuote = quote;
                DengtaApplication.getApplication().getDataCacheManager().setSecQuote(quote);
                mView.onLoadComplete();
                if (!mLineChartFragment.isDetailState()) {
                    mView.updateQuoteView(quote);
                }
                mLineChartFragment.setQuote(quote);
                if (mQuoteDialog != null) {
                    mQuoteDialog.updateQuote(quote);
                }
                break;
            case MSG_UPDATE_FAILED:
                mView.onLoadComplete();
                break;
            case MSG_UPDATE_BASE_INFO:
                final SecBaseInfoRsp baseInfoRsp = (SecBaseInfoRsp) msg.obj;
                if (baseInfoRsp != null) {
                    if (mHandicapPresenter != null) {
                        mHandicapPresenter.setBaseInfo(baseInfoRsp);
                    }
                    if (mHongKongQuotePresenter != null) {
                        mHongKongQuotePresenter.setBaseInfo(baseInfoRsp);
                    }
                }
                mView.updateBaseInfoView(baseInfoRsp);
                break;
            case MSG_UPDATE_LIVE_MSG:
                final SecLiveMsgRsp rsp = (SecLiveMsgRsp) msg.obj;
                final ArrayList<SecLiveMsg> secLiveMsgs = rsp.getVSecLiveMsg();
                if (secLiveMsgs != null && secLiveMsgs.size() > 0) {
                    mLastMsgId = secLiveMsgs.get(0).sId;
                }
                mView.updateLiveMsgView(rsp);
                break;
            case MSG_UPDATE_CHIP_DIST:
                final HisChipDistRsp chipDistRsp = (HisChipDistRsp) msg.obj;
                mLineChartFragment.setChipDist(chipDistRsp);
                break;
            default:
                break;
        }
        return true;
    }

    public void showFundQuoteDialog(final Context context) {
        final FundQuoteDialog dialog = new FundQuoteDialog(context, this);
        dialog.show();
        mQuoteDialog = dialog;
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }

    public void showNewThirdBoardQuoteDialog(final Context context) {
        final NewThirdBoardQuoteDialog dialog = new NewThirdBoardQuoteDialog(context, this);
        dialog.show();
        mQuoteDialog = dialog;
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }

    public void showUSAQuoteDialog(final Context context) {
        final USAQuoteDialog dialog = new USAQuoteDialog(context, this);
        dialog.show();
        mQuoteDialog = dialog;
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }

    public void showHongKongQuoteDialog(final Context context, final String name) {
        final HongKongQuoteDialog dialog = new HongKongQuoteDialog(context, this);
        mHongKongQuotePresenter = new HongKongQuotePresenter(dialog, mDtSecCode, name);
        dialog.setQuotePresenter(mHongKongQuotePresenter);
        dialog.show();
        mQuoteDialog = dialog;
        if (mBaseInfo != null) {
            mHongKongQuotePresenter.setBaseInfo(mBaseInfo);
        }
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }

    public void showIndexFutureQuoteDialog(final Context context) {
        final IndexFutureQuoteDialogQuoteDialog dialog = new IndexFutureQuoteDialogQuoteDialog(context, this);
        dialog.show();
        mQuoteDialog = dialog;
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }

    public void showIndexQuoteDialog(final Context context) {
        IQuoteDialog dialog;
        final int marketType = StockUtil.getMarketType(mDtSecCode);
        switch (marketType) {
            case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
            case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
            case E_MARKET_TYPE.E_MT_DT: //灯塔自定义交易所
                final IndexQuoteDialogQuoteDialog indexDialog = new IndexQuoteDialogQuoteDialog(context, this);
                dialog = indexDialog;
                indexDialog.show();
                break;
            default:
                final HongKongUSAIndexQuoteDialog hongKongUSADialog = new HongKongUSAIndexQuoteDialog(context, this);
                dialog = hongKongUSADialog;
                hongKongUSADialog.show();
                break;
        }

        mQuoteDialog = dialog;
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }

    public void showHandicapDialog(final Context context, final String name) {
        final HandicapDialog dialog = new HandicapDialog(context, this);
        final HandicapPresenter handicapPresenter = new HandicapPresenter(dialog, mDtSecCode, name);
        mHandicapPresenter = handicapPresenter;
        dialog.setHandicapPresenter(handicapPresenter);
        if (mBaseInfo != null) {
            handicapPresenter.setBaseInfo(mBaseInfo);
        }
        dialog.show();
        handicapPresenter.swichToMarket();
        mQuoteDialog = dialog;
        if (mQuote != null) {
            mQuoteDialog.updateQuote(mQuote);
        }
    }
}
