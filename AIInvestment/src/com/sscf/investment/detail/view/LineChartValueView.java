package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import com.sscf.investment.R;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.detail.view.TimeLineInfosView.TimeLineTouchEvent;
import com.sscf.investment.detail.view.KLineInfosView.KLineAverageInfo;
import com.sscf.investment.detail.view.KLineInfosView.KLineAverageInfo.Average;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/5/4.
 */

public class LineChartValueView extends AppCompatTextView {

    private int mTitleColor;
    private int mNormalTextColor;
    private int mRedColor;
    private int mGreenColor;
    private int mGrayColor;

    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;
    private boolean mIsHongKongOrUsa;
    private String mDtSecCode;
    private int mLineType;
    private static final String VALUE_GAP = "        ";

    private TimeLineTouchEvent mTimeLineData;
    private KLineAverageInfo mAverageInfo;

    public LineChartValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initResource(context.getResources());
    }

    private void initResource(Resources resources) {
        mTitleColor = ContextCompat.getColor(getContext(), R.color.default_text_color_80);
        mNormalTextColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
        mRedColor = resources.getColor(R.color.stock_red_color);
        mGreenColor = resources.getColor(R.color.stock_green_color);
        mGrayColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
    }

    public void setLineType(int lineType) {
        if(mLineType != lineType) {
            clearValue();
        }
        mLineType = lineType;
    }

    public void clearValue() {
        setText(null);
        mTimeLineData = null;
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;
        mIsHongKongOrUsa = StockUtil.isHongKongOrUSA(mDtSecCode);
    }

    public void setTimeLineEvent(TimeLineTouchEvent event) {
        mTimeLineData = event;
        drawText();
    }

    public void setAverageInfo(KLineAverageInfo averageInfo) {
        mAverageInfo = averageInfo;
        drawText();
    }

    public void setTpFlag(int tpFlag) {
        if (mTpFlag != tpFlag) {
            mTpFlag = tpFlag;
            drawText();
        }
    }

    private void drawText() {
        if(isTimeLine(mLineType)) {
            drawTimeLineText();
        } else if(isKLine(mLineType)) {
            drawKLineText();
        }
    }

    private boolean isTimeLine(int lineType) {
        switch (lineType) {
            case LineChartTextureView.TYPE_TIME:
            case LineChartTextureView.TYPE_FIVE_DAY:
                return true;
            default:
                return false;
        }
    }

    private boolean isKLine(int lineType) {
        switch (lineType) {
            case LineChartTextureView.TYPE_DAILY_K:
            case LineChartTextureView.TYPE_WEEKLY_K:
            case LineChartTextureView.TYPE_MONTHLY_K:
            case LineChartTextureView.TYPE_ONE_MIN_K:
            case LineChartTextureView.TYPE_FIVE_MIN_K:
            case LineChartTextureView.TYPE_FIFTEEN_MIN_K:
            case LineChartTextureView.TYPE_THIRTY_MIN_K:
            case LineChartTextureView.TYPE_SIXTY_MIN_K:
                return true;
            default:
                return false;
        }
    }

    private void drawTimeLineText() {
        TimeLineTouchEvent event = mTimeLineData;
        if(event != null) {
            SpannableStringBuilder sb = new SpannableStringBuilder();
            int color;
            SpannableString str;

            //时间
            String timeStr = StringUtil.minuteToTime(event.getMinute());
            str = createTimeLineString(timeStr);
            sb.append(str).append(VALUE_GAP);


            //现价
            String price = StringUtil.getFormattedFloat(event.getNow(), mTpFlag);
            if (event.getDelta() > 0) {
                color = mRedColor;
            } else if (event.getDelta() == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
            str = createTimeLineString("价", price, color);
            sb.append(str).append(VALUE_GAP);

            //涨幅
            String deltaPercent = StringUtil.getChangePercentString(event.getDelta()).toString();
            if (event.getDelta() > 0) {
                color = mRedColor;
            } else if (event.getDelta() == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
            str = createTimeLineString("幅", deltaPercent, color);
            sb.append(str).append(VALUE_GAP);

            //均价
            if (event.getAverage() != 0) {
                String average = StringUtil.getFormattedFloat(event.getAverage(), mTpFlag);
                float deltaByYesterday = event.getAverage() - event.getYesterdayClose();
                if (deltaByYesterday > 0) {
                    color = mRedColor;
                } else if (deltaByYesterday == 0) {
                    color = mGrayColor;
                } else {
                    color = mGreenColor;
                }
                str = createTimeLineString("均", average, color);
                sb.append(str).append(VALUE_GAP);
            }

            //成交
            String volume = mIsHongKongOrUsa ? StringUtil.getVolumeString(event.getVolume(), true, true) : StringUtil.getVolumeString(event.getVolume(), false, true);
            color = mNormalTextColor;
            str = createTimeLineString("量", volume, color);
            sb.append(str).append(VALUE_GAP);

            setText(sb);
        } else {
            setText(null);
        }
    }

    private void drawKLineText() {
        KLineAverageInfo averageInfo = mAverageInfo;
        if(averageInfo != null) {
            List<Average> averageList = averageInfo.getAverageList();
            if(!averageList.isEmpty()) {
                SpannableStringBuilder sb = new SpannableStringBuilder();
                for(Average average : averageList) {
                    sb.append(createAverageString(average.text, average.color)).append(VALUE_GAP);
                }
                setText(sb);
                return;
            }
        }
        setText(null);
    }

    private SpannableString createAverageString(String text, int color) {
        ForegroundColorSpan valueSpan = new ForegroundColorSpan(color);
        SpannableString str = new SpannableString(text);
        str.setSpan(valueSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return str;
    }

    private SpannableString createTimeLineString(String value) {
        ForegroundColorSpan valueSpan = new ForegroundColorSpan(mTitleColor);
        SpannableString str = new SpannableString(value);
        str.setSpan(valueSpan, 0, value.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return str;
    }

    private SpannableString createTimeLineString(String title, String value, int valueColor) {
        ForegroundColorSpan titleSpan = new ForegroundColorSpan(mTitleColor);
        ForegroundColorSpan valueSpan = new ForegroundColorSpan(valueColor);
        SpannableString str = new SpannableString(title + ":" + value);
        str.setSpan(titleSpan, 0, title.length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        str.setSpan(valueSpan, title.length() + 1, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return str;
    }
}
