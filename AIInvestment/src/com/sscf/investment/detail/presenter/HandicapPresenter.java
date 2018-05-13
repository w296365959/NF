package com.sscf.investment.detail.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.sscf.investment.comparator.SecSimpleQuoteUpdownComparator;
import com.sscf.investment.detail.dialog.HandicapDialog;
import com.sscf.investment.detail.model.HandicapModel;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import com.dengtacj.component.router.WebBeaconJump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import BEC.ConsultScore;
import BEC.PlateInfo;
import BEC.SecBaseInfo;
import BEC.SecBaseInfoRsp;
import BEC.SecInfo;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017/04/26
 */
public final class HandicapPresenter implements Handler.Callback, Runnable {
    private static final int MSG_UPDATE_RELATED_H_SIMPLE_QUOTE_DATA = 2;
    private static final int MSG_UPDATE_INDUSTRY_PLATE_DATA = 3;
    private static final int MSG_UPDATE_CONCEPT_PLATE_DATA = 4;
    private static final int MSG_UPDATE_SCORE_DATA = 5;

    private static final int STATE_MARKET = 1;
    private static final int STATE_PLATE = 2;
    private static final int STATE_INTELLIGENT_DIAGNOSIS = 3;
    private int mState;

    private final HandicapDialog mHandicapDialog;
    private final HandicapModel mHandicapModel;

    private final Handler mHandler;
    private final PeriodicHandlerManager mPeriodicHandlerManager;

    // ---------- market data ----------
    private final String mDtSecCode;
    private final String mSecName;
    // ah股关联港股的code
    private String mRelatedHDtSecCode;
    private String mRelatedHSecName;
    // ---------- market data ----------

    // ---------- plate data ----------
    private String mIndustryPlateCode;
    private ArrayList<String> mConceptPlateCodes;
    private Comparator<SecSimpleQuote> mComparator;
    // ---------- plate data ----------

    // ---------- score data ----------
    private ConsultScore mDiagnosisScore;
    // ---------- score data ----------

    public HandicapPresenter(final HandicapDialog handicapDialog, String dtSecCode, String secName) {
        mHandicapDialog = handicapDialog;
        mHandicapModel = new HandicapModel(this);

        mHandler = new Handler(Looper.getMainLooper(), this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);

        mDtSecCode = dtSecCode;
        mSecName = secName;
    }

    public void setBaseInfo(final SecBaseInfoRsp rsp) {
        final ArrayList<SecInfo> stockList = rsp.getVSecInfo();
        if (stockList == null || stockList.size() == 0) {
            return;
        }
        final SecInfo secInfo = stockList.get(0);
        getRelatedHDtSecCode(secInfo);

        mIndustryPlateCode = secInfo.stPlateInfo.sDtSecCode;
        mConceptPlateCodes = getConceptPlateCodes(secInfo);
    }

    private ArrayList<String> getConceptPlateCodes(SecInfo secInfo) {
        final ArrayList<PlateInfo> conceptPlate = secInfo.vPlateInfo;
        final ArrayList<String> conceptPlateCodes = new ArrayList<String>();
        for (PlateInfo plateInfo : conceptPlate) {
            conceptPlateCodes.add(plateInfo.sDtSecCode);
        }
        return conceptPlateCodes;
    }

    private void getRelatedHDtSecCode(final SecInfo secInfo) {
        final ArrayList<SecBaseInfo> relatedSecBaseInfos = secInfo.getVRelateSecInfo();
        if (relatedSecBaseInfos != null && relatedSecBaseInfos.size() > 0) {
            for (SecBaseInfo relatedSecBaseInfo : relatedSecBaseInfos) {
                String dtSecCode = relatedSecBaseInfo.getSDtSecCode();
                if (StockUtil.isHongKongMarket(dtSecCode)) {
                    mRelatedHDtSecCode = dtSecCode;
                    mRelatedHSecName = relatedSecBaseInfo.sCHNShortName;
                    break;
                }
            }
        }
    }

    public void swichToMarket() {
        mState = STATE_MARKET;
        mHandicapDialog.showMarketData();
        mPeriodicHandlerManager.runPeriodic();
    }

    public void swichToPlate() {
        mState = STATE_PLATE;
        mHandicapDialog.showBelongedPlate();
        mPeriodicHandlerManager.runPeriodic();
    }

    public void swichToIntelligentDiagnosis() {
        mState = STATE_INTELLIGENT_DIAGNOSIS;
        mHandicapDialog.showIntelligentDiagnosis();
        requestStockScore();
    }

    public void onGetRelatedHSimpleQuote(final SecSimpleQuote quote) {
        if (quote != null) {
            mHandler.obtainMessage(MSG_UPDATE_RELATED_H_SIMPLE_QUOTE_DATA, quote).sendToTarget();
        }
    }

    public void clickShowAHPremium(final Context context) {
        WebBeaconJump.showAhPremiumDetail(context, mDtSecCode, mSecName, mRelatedHDtSecCode, mRelatedHSecName);
    }

    public void onGetIndustryPlateQuote(final SecSimpleQuote quote) {
        if (quote != null) {
            mHandler.obtainMessage(MSG_UPDATE_INDUSTRY_PLATE_DATA, quote).sendToTarget();
        }
    }

    public void onGetConceptPlateQuote(final ArrayList<SecSimpleQuote> quotes) {
        if (quotes != null) {
            Collections.sort(quotes, getConceptPlateComparator());
            mHandler.obtainMessage(MSG_UPDATE_CONCEPT_PLATE_DATA, quotes).sendToTarget();
        }
    }

    private Comparator<SecSimpleQuote> getConceptPlateComparator() {
        if (mComparator == null) {
            mComparator = new SecSimpleQuoteUpdownComparator();
        }
        return mComparator;
    }

    private void requestStockScore() {
        if (mDiagnosisScore == null) {
            mHandicapModel.requestStockScore(mDtSecCode);
        }
    }

    public void onGetStockScore(final ConsultScore score) {
        mDiagnosisScore = score;
        if (score != null) {
            mHandler.obtainMessage(MSG_UPDATE_SCORE_DATA, score).sendToTarget();
        }
    }

    public void clickShowIntelligentDiagnosisButton(final Context context) {
        WebBeaconJump.showIntelligentDiagnosisDetail(context, mDtSecCode, mSecName);
    }

    public void refresh() {
        mPeriodicHandlerManager.runPeriodic();
    }

    public void stopRefresh() {
        mPeriodicHandlerManager.stop();
    }

    public void release() {
        mPeriodicHandlerManager.stop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_RELATED_H_SIMPLE_QUOTE_DATA:
                mHandicapDialog.updateAHpremuim((SecSimpleQuote) msg.obj);
                break;
            case MSG_UPDATE_INDUSTRY_PLATE_DATA:
                mHandicapDialog.updateIndustryPlateData((SecSimpleQuote) msg.obj);
                break;
            case MSG_UPDATE_CONCEPT_PLATE_DATA:
                mHandicapDialog.updateConceptPlateData((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            case MSG_UPDATE_SCORE_DATA:
                mHandicapDialog.updateIntelligentDiagnosis((ConsultScore) msg.obj);
                break;
        }
        return true;
    }

    @Override
    public void run() {
        switch(mState) {
            case STATE_MARKET:
                if (mRelatedHDtSecCode != null) {
                    mHandicapModel.requestSimpleQuote(mRelatedHDtSecCode);
                }
                break;
            case STATE_PLATE:
                if (mIndustryPlateCode != null && mConceptPlateCodes != null) {
                    mHandicapModel.requestPlateQuote(mIndustryPlateCode, mConceptPlateCodes);
                }
                break;
            default:
                mPeriodicHandlerManager.stop();
                break;
        }
    }
}
