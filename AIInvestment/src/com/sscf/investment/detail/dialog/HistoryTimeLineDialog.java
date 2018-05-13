package com.sscf.investment.detail.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.detail.view.KLineInfosView;
import com.sscf.investment.utils.StringUtil;

import BEC.RtMinDesc;

/**
 * Created by yorkeehuang on 2017/4/24.
 */

public class HistoryTimeLineDialog extends BottomDoubleButtonDialog implements View.OnClickListener {

    private String mTimeStr = "";

    public HistoryTimeLineDialog(@NonNull Context context) {
        super(context, HISTORY_TIME_LINE);
        mPreButton.setText(R.string.pre_day_text);
        mNextButton.setText(R.string.next_day_text);
    }

    public String getDate() {
        return mTimeStr;
    }

    public void setTradingTime(String openTimeStr, String middleTimeStr, String closeTimeStr) {
        if(mSurface != null) {
            mSurface.setTradingTime(openTimeStr, middleTimeStr, closeTimeStr);
        }
    }

    public void setEvent(KLineInfosView.KLineLineTouchEvent event) {
        runOnUiThread(() -> {
            mTimeStr = event.getTimeStr();
            SecDetailInfo detailInfo = initDetailInfo();
            updateInfoByKLineEvent(detailInfo, event);
            updateValue();
            updateButton(event.getIndex(), event.getKLineStart(), event.getKLineEnd());
        });
    }

    private void updateInfoByKLineEvent(SecDetailInfo info, KLineInfosView.KLineLineTouchEvent event) {
        if(info != null && event != null) {
            info.setYesterdayClose(event.getYesterdayClose());
            info.setOpen(event.getOpen());
            info.setHigh(event.getHigh());
            info.setLow(event.getLow());
            info.setClose(event.getClose());
            info.setAmount(event.getAmount());
            info.setDelta(event.getDelta());
            info.setVolume(event.getVolume());
        }
    }

    @Override
    protected void updateValue() {
        super.updateValue();
        SecDetailInfo detailInfo = getDetailInfo();
        if(detailInfo != null) {
            String deltaPercent;
            int color;
            if(detailInfo.isSuspended()) {
                deltaPercent = "--";
                color = mGrayColor;
            } else {
                deltaPercent = StringUtil.getChangePercentString(detailInfo.getDelta()).toString();
                if (detailInfo.getDelta() > 0) {
                    color = mRedColor;
                } else if (detailInfo.getDelta() == 0) {
                    color = mGrayColor;
                } else {
                    color = mGreenColor;
                }
            }
            mChangeRateView.setTextColor(color);
            mChangeRateView.setText(deltaPercent);
            if(mSecQuote != null) {
                float close = mSecQuote.getFClose();
                float circulationmarketvalue = mSecQuote.getFCirculationmarketvalue();
                mTurnoverView.setText(StringUtil.getPercentString(detailInfo.getVolume()
                        / (circulationmarketvalue / close)));
            }
        }
    }

    public void setTimeLineEntities(KLineInfosView.KLineLineTouchEvent event, RtMinDesc rtMinDesc) {
        if(rtMinDesc != null) {
            SecDetailInfo info = getDetailInfo();
            updateInfoByKLineEvent(info, event);
            boolean isLast = (event.getIndex() == event.getKLineEnd());
            updateTimeLineInfo(info, rtMinDesc, isLast);
            if(info != null) {
                setTimeLineEntities(mTimeLineInfo, rtMinDesc.getVTrendDesc());
            }
        }
    }

    private void updateTimeLineInfo(SecDetailInfo detailInfo, RtMinDesc rtMinDesc, boolean isLast) {
        if(detailInfo != null && rtMinDesc != null) {
//            mTimeLineInfo.setHigh(detailInfo.getHigh());
//            mTimeLineInfo.setLow(detailInfo.getLow());
            mTimeLineInfo.setIsSuspended(detailInfo.isSuspended());
            if(isLast) {
                mTimeLineInfo.setYesterdayClose(detailInfo.getYesterdayClose());
            } else {
                mTimeLineInfo.setYesterdayClose(rtMinDesc.getFPreClose());
            }
        }
    }
}
