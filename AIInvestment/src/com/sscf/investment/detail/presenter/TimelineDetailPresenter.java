package com.sscf.investment.detail.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import com.sscf.investment.detail.dialog.BottomDialog;
import com.sscf.investment.detail.dialog.BottomDoubleButtonDialog;
import com.sscf.investment.detail.dialog.BottomDoubleButtonDialog.OnDialogButtonClickListener;
import com.sscf.investment.detail.dialog.CallauctionDialog;
import com.sscf.investment.detail.dialog.EnlargeTimeLineDialog;
import com.sscf.investment.detail.dialog.HistoryTimeLineDialog;
import com.sscf.investment.detail.fragment.LineChartFragment;
import com.sscf.investment.detail.model.TimeLineDetailModel;
import com.sscf.investment.detail.view.KLineInfosView.KLineLineTouchEvent;
import com.sscf.investment.detail.view.TimeLineInfosView.TimeLineTouchEvent;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import java.util.ArrayList;
import BEC.RtMinRsp;
import BEC.SecQuote;
import BEC.TickDesc;
import BEC.TickRsp;
import BEC.TrendRsp;

/**
 * Created by yorkeehuang on 2017/5/10.
 */

public class TimelineDetailPresenter implements BottomDoubleButtonDialog.OnDialogButtonClickListener {
    private BottomDialog mDialog;
    private TimeLineDetailModel mModel;

    private String mDtSecCode;
    private SecQuote mSecQuote;
    private ArrayList<TickDesc> mTickList;
    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;
    private KLineLineTouchEvent mKLineTouchEvent;
    private TimeLineTouchEvent mTimeLineTouchEvent;
    private String mOpenTimeStr;
    private String mMiddleTimeStr;
    private String mCloseTimeStr;
    private OnDialogButtonClickListener mOnDialogButtonClickListener;

    private final LineChartFragment mFragment;

    public TimelineDetailPresenter(OnDialogButtonClickListener listener, LineChartFragment fragment) {
        mModel = new TimeLineDetailModel(this);
        mOnDialogButtonClickListener = listener;
        mFragment = fragment;
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
    }

    public void updateKLineTouchEvent(final KLineLineTouchEvent event) {
        mKLineTouchEvent = event;
        if (checkDialog(BottomDialog.HISTORY_TIME_LINE)) {
            HistoryTimeLineDialog dialog = (HistoryTimeLineDialog) mDialog;
            final String currentDate = dialog.getDate();
            String newDate = event.getTimeStr();
            dialog.setEvent(event);
            if (!TextUtils.equals(currentDate, newDate) && !TextUtils.isEmpty(mDtSecCode)) {
                mModel.loadHistoryTimeLineData(mDtSecCode, newDate);
            }
        }
    }

    public void updateTimeLineTouchEvent(final TimeLineTouchEvent event) {
        mTimeLineTouchEvent = event;
        if (checkDialog(BottomDialog.ENLARGE_TIME_LINE)) {
            EnlargeTimeLineDialog dialog = (EnlargeTimeLineDialog) mDialog;
            final int currentMinute = dialog.getMinute();
            int minute = event.getMinute();
            dialog.setEvent(event);
            if (currentMinute != minute && !TextUtils.isEmpty(mDtSecCode)) {
                mModel.loadEnlargeTimeLineData(mDtSecCode, minute, mOpenTimeStr, mCloseTimeStr);
            }
        }
    }

    public void updateTradingTimeStr(String openTimeStr, String middleTimeStr, String closeTimeStr) {
        mOpenTimeStr = openTimeStr;
        mMiddleTimeStr = middleTimeStr;
        mCloseTimeStr = closeTimeStr;
    }

    public void setSecQuote(SecQuote secQuote) {
        mTpFlag = secQuote == null ? DengtaConst.DEFAULT_TP_FLAG : secQuote.iTpFlag;
        mSecQuote = secQuote;
        if (mDialog != null) {
            mDialog.updateQuote(secQuote);
        }
    }

    public void setTicksData(int tpFlag, ArrayList<TickDesc> tickDescs) {
        mTpFlag = tpFlag;
        mTickList = tickDescs;
        updateCallauctionDialog();
    }

    public void showDialog(BaseFragmentActivity activity, int type) {
        if(activity == null || activity.isDestroy()) {
            return;
        }

        if(mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        switch (type) {
            case BottomDialog.ENLARGE_TIME_LINE:
                mDialog = initEnlargeTimeLineDialog(activity);
                break;
            case BottomDialog.HISTORY_TIME_LINE:
                mDialog = initHistoryTimeLineDialog(activity);
                break;
            case BottomDialog.CALLAUCTION_LINE:
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TOUCH_LINE_SHOW_CALLAUCTION);
                mDialog = initCallauctionDialog(activity);
                break;
        }
        if(mDialog != null) {
            mDialog.show();
        }
    }

    private BottomDialog initEnlargeTimeLineDialog(Activity activity) {
        EnlargeTimeLineDialog dialog = new EnlargeTimeLineDialog(activity);
        dialog.setOnDialogButtonClickListener(mOnDialogButtonClickListener);
        final TimeLineTouchEvent event = mTimeLineTouchEvent;
        if(dialog != null && event != null) {
            int openTime = StringUtil.time2Minutes(mOpenTimeStr);
            int closeTime = StringUtil.time2Minutes(mCloseTimeStr);
            dialog.setOpenCloseTime(openTime, closeTime);
            if(mSecQuote != null) {
                ThreadUtils.runOnUiThread(() -> {
                    dialog.updateQuote(mSecQuote);
                });
            }
            dialog.setEvent(event);
            if(!TextUtils.isEmpty(mDtSecCode)) {
                mModel.loadEnlargeTimeLineData(mDtSecCode, event.getMinute(), mOpenTimeStr, mCloseTimeStr);
            }
            dialog.show();
        }
        return dialog;
    }

    private BottomDialog initHistoryTimeLineDialog(Activity activity) {
        HistoryTimeLineDialog dialog = new HistoryTimeLineDialog(activity);
        dialog.setOnDialogButtonClickListener(mOnDialogButtonClickListener);
        if(dialog != null && mKLineTouchEvent != null) {
            if(mSecQuote != null) {
                dialog.updateQuote(mSecQuote);
            }
            dialog.setEvent(mKLineTouchEvent);
            if(!TextUtils.isEmpty(mDtSecCode)) {
                mModel.loadHistoryTimeLineData(mDtSecCode, mKLineTouchEvent.getTimeStr());
            }
            dialog.show();
        }
        return dialog;
    }

    private BottomDialog initCallauctionDialog(Activity activity) {
        CallauctionDialog dialog = new CallauctionDialog(activity, this);
        dialog.updateQuote(mSecQuote);
        dialog.setTicksData(mTickList);
        if(!TextUtils.isEmpty(mDtSecCode)) {
            mModel.loadCallauctionData(mDtSecCode);
        }
        return dialog;
    }

    private void updateCallauctionDialog() {
        if(checkDialog(BottomDialog.CALLAUCTION_LINE) && mTickList != null) {
            CallauctionDialog dialog = (CallauctionDialog) mDialog;
            dialog.setTicksData(mTickList);
        }
    }

    public void onResume() {
        if(mDialog != null) {
            mDialog.onResume();
        }
    }

    public void onPause() {
        if(mDialog != null) {
            mDialog.onPause();
        }
    }

    public void onDestroy() {
        if(mDialog != null) {
            if(mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        }
    }

    private boolean checkDialog(int type) {
        return mDialog != null && mDialog.isShowing() && mDialog.getType() == type;
    }

    public void handleHistoryTimeLine(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            final RtMinRsp rtMinRsp = (RtMinRsp) data.getEntity();
            if (rtMinRsp != null) {
                setHistoryTimeLineData(rtMinRsp);
            }
        } else {
            setHistoryTimeLineData(null);
        }
    }

    public void handleEnlargeTimeLine(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            final TrendRsp trendRsp = (TrendRsp) data.getEntity();
            if (trendRsp != null) {
                setEnlargeTimeLineData(trendRsp);
            }
        } else {
            setEnlargeTimeLineData(null);
        }
    }

    public void handleCallauction(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            final TrendRsp trendRsp = (TrendRsp) data.getEntity();
            if (trendRsp != null) {
                setCallauctionData(trendRsp);
            }
        } else {
            setCallauctionData(null);
        }
    }

    private void setHistoryTimeLineData(final RtMinRsp rtMinRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if(checkDialog(BottomDialog.HISTORY_TIME_LINE)) {
                HistoryTimeLineDialog dialog = (HistoryTimeLineDialog) mDialog;
                if(rtMinRsp != null && rtMinRsp.getVRtMinDesc() != null && !rtMinRsp.getVRtMinDesc().isEmpty()) {
                    dialog.setTradingTime(mOpenTimeStr, mMiddleTimeStr, mCloseTimeStr);
                    dialog.setTimeLineEntities(mKLineTouchEvent, rtMinRsp.getVRtMinDesc().get(0));
                } else {
                    dialog.setTimeLineEntities(mKLineTouchEvent, null);
                }
            }
        });
    }

    private void setEnlargeTimeLineData(final TrendRsp trendRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if(checkDialog(BottomDialog.ENLARGE_TIME_LINE)) {
                EnlargeTimeLineDialog dialog = (EnlargeTimeLineDialog) mDialog;
                if(trendRsp != null && trendRsp.getVTrendDesc() != null && !trendRsp.getVTrendDesc().isEmpty()) {
                    dialog.setTrendRsp(mTimeLineTouchEvent, trendRsp);
                } else {
                    dialog.setTrendRsp(mTimeLineTouchEvent, null);
                }
            }
        });
    }

    private void setCallauctionData(final TrendRsp trendRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if(checkDialog(BottomDialog.CALLAUCTION_LINE)) {
                CallauctionDialog dialog = (CallauctionDialog) mDialog;
                if(trendRsp != null && trendRsp.getVTrendDesc() != null && !trendRsp.getVTrendDesc().isEmpty()) {
                    dialog.setTrendRsp(trendRsp);
                } else {
                    dialog.setTrendRsp(null);
                }
            }
        });
    }

    @Override
    public void onDialogButtonClick(BottomDoubleButtonDialog dialog, View view, int position) {
        mOnDialogButtonClickListener.onDialogButtonClick(dialog, view, position);
    }

    public void requestTick(String dtSecCode) {
        mModel.requestTick(dtSecCode);
    }

    public void onGetTickData(final TickRsp tickRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if (tickRsp != null) {
                mFragment.setTicksData(tickRsp);
                setTicksData(mTpFlag, tickRsp.vTickDesc);
            }
        });
    }
}
