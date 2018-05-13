package com.sscf.investment.detail.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.model.KlineSettingConst;
import com.sscf.investment.utils.StockUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2015/9/11.
 */
public class KLineControlView extends LinearLayout implements View.OnClickListener {
    private float mBorderStrokeWidth;
    private float mDrawingWidth;
    private float mDrawingHeight;
    private Paint mPaintRect;
    private int mBorderColor;
    private int[] mRepairTagResIds = new int[] {R.string.tag_kline_repair, R.string.tag_kline_unrepair};
    private int[] mTradingIndicatorResIds = new int[] {R.string.tag_kline_volume, R.string.tag_kline_capital_flow,
            R.string.tag_kline_macd, R.string.tag_kline_kdj, R.string.tag_kline_rsi, R.string.tag_kline_boll, R.string.tag_kline_dmi,
            R.string.tag_kline_cci, R.string.tag_kline_cci, R.string.tag_kline_ene, R.string.tag_kline_dma,
            R.string.tag_kline_expma, R.string.tag_kline_vr, R.string.tag_kline_bbi, R.string.tag_kline_obv,
            R.string.tag_kline_bias, R.string.tag_kline_wr, R.string.tag_kline_break};
    private String[] mRepairTags;
    private String[] mIndicatorTags;
    private String mRepairTag;
    private String mIndicatorTag;

    private String mDtSecCode;
    private boolean mIsHongKongOrUsa;
    private String mRepairTagStr;
    private String mUnRepairTagStr;
    private String mVolumeTagStr;
    private String mMacdTagStr;
    private String mKdjTagStr;
    private String mRsiTagStr;
    private String mBollTagStr;
    private String mCapitalFlowTagStr;
    private String mBBITagStr;
    private String mDMITagStr;
    private String mCCITagStr;
    private String mENETagStr;
    private String mDMATagStr;
    private String mEXPMATagStr;
    private String mVRTagStr;
    private String mOBVTagStr;
    private String mBIASTagStr;
    private String mWRTagStr;
    private String mBreakTagStr;

    private boolean mInited = false;
    private Resources mResources;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED.equals(action)) {
                int indicatorType = SettingPref.getInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_0, SettingConst.DEFAULT_K_LINE_INDICATOR_TYPE);
                String tag = mVolumeTagStr;
                switch (indicatorType) {
                    case SettingConst.K_LINE_INDICATOR_VOLUME:
                        tag = mVolumeTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_MACD:
                        tag = mMacdTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_KDJ:
                        tag = mKdjTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_RSI:
                        tag = mRsiTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_BOLL:
                        tag = mBollTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW:
                        tag = mCapitalFlowTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_DMI:
                        tag = mDMITagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_CCI:
                        tag = mCCITagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_ENE:
                        tag = mENETagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_DMA:
                        tag = mDMATagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_EXPMA:
                        tag = mEXPMATagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_VR:
                        tag = mVRTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_BBI:
                        tag = mBBITagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_OBV:
                        tag = mOBVTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_BIAS:
                        tag = mBIASTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_WR:
                        tag = mWRTagStr;
                        break;
                    case SettingConst.K_LINE_INDICATOR_BREAK:
                        tag = mBreakTagStr;
                        break;
                    default:
                        break;
                }
                refreshUiByTag(tag);
            }
        }
    };

    public KLineControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initResources();
        initControl();
        mInited = true;
    }

    private void initResources() {
        mResources = getResources();

        mRepairTagStr = mResources.getString(R.string.tag_kline_repair);
        mUnRepairTagStr = mResources.getString(R.string.tag_kline_unrepair);
        mVolumeTagStr = mResources.getString(R.string.tag_kline_volume);
        mMacdTagStr = mResources.getString(R.string.tag_kline_macd);
        mKdjTagStr = mResources.getString(R.string.tag_kline_kdj);
        mRsiTagStr = mResources.getString(R.string.tag_kline_rsi);
        mBollTagStr = mResources.getString(R.string.tag_kline_boll);
        mCapitalFlowTagStr = mResources.getString(R.string.tag_kline_capital_flow);
        mBBITagStr = mResources.getString(R.string.tag_kline_bbi);
        mDMITagStr = mResources.getString(R.string.tag_kline_dmi);
        mCCITagStr = mResources.getString(R.string.tag_kline_cci);
        mENETagStr = mResources.getString(R.string.tag_kline_ene);
        mDMATagStr = mResources.getString(R.string.tag_kline_dma);
        mEXPMATagStr = mResources.getString(R.string.tag_kline_expma);
        mVRTagStr = mResources.getString(R.string.tag_kline_vr);
        mOBVTagStr = mResources.getString(R.string.tag_kline_obv);
        mBIASTagStr = mResources.getString(R.string.tag_kline_bias);
        mWRTagStr = mResources.getString(R.string.tag_kline_wr);
        mBreakTagStr = mResources.getString(R.string.tag_kline_break);

        List<String> tags = new ArrayList<>();
        for (int tagResId : mRepairTagResIds) {
            String tag = mResources.getString(tagResId);
            tags.add(tag);
        }
        mRepairTags = tags.toArray(new String[tags.size()]);

        tags.clear();
        for (int tagResId : mTradingIndicatorResIds) {
            String tag = mResources.getString(tagResId);
            tags.add(tag);
        }
        mIndicatorTags = tags.toArray(new String[tags.size()]);

        if (mRepairTag == null) {
            mRepairTag = getCurrentTag();
        }
        if (mIndicatorTag == null) {
            mIndicatorTag = mResources.getString(R.string.tag_kline_volume);
        }

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.line_chart_border_rect_color,
        });
        mBorderColor = a.getColor(0, Color.LTGRAY);
        a.recycle();

        mBorderStrokeWidth = mResources.getDimensionPixelSize(R.dimen.line_chart_divider_strokeWidth);

        mPaintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRect.setColor(mBorderColor);
        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setStrokeWidth(mBorderStrokeWidth);
    }

    private String getCurrentTag(){
        int status = DengtaApplication.getApplication().getKLineSettingManager().getKlineSettingConfigure().rightStatus;
        return status == KlineSettingConst.K_LINE_REPAIR ? mResources.getString(R.string.tag_kline_repair) :  mResources.getString(R.string.tag_kline_unrepair);
    }

    private void initControl() {
        for (String tag : mRepairTags) {
            TextView textView = (TextView) findViewWithTag(tag);
            if (TextUtils.equals(mRepairTag, tag)) {
                textView.setSelected(true);
            }
            textView.setOnClickListener(this);
        }

        for (String tag : mIndicatorTags) {
            TextView textView = (TextView) findViewWithTag(tag);
            if (TextUtils.equals(mIndicatorTag, tag)) {
                textView.setSelected(true);
            }
            textView.setOnClickListener(this);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        //画外边框
        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();
        canvas.drawRect(mBorderStrokeWidth, mBorderStrokeWidth, mDrawingWidth - mBorderStrokeWidth, mDrawingHeight - mBorderStrokeWidth, mPaintRect);
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof TextView)) {
            return;
        }

        String tag = (String) v.getTag();

        if (TextUtils.equals(tag, mRepairTagStr)) {
            StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_REPAIR_SWITCH);
        } else if (TextUtils.equals(tag, mUnRepairTagStr)) {
            StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_REPAIR_SWITCH);
        }

        refreshUiByTag(tag);

        performControl(tag);
    }

    public void setRepairType(final int repairType) {
        mRepairTag = getRepairTagByType(repairType);
        if (mInited) {
            refreshUiByTag(mRepairTag);
        }
    }

    public void setTradingIndicatorType(final int indicatorType) {
        mIndicatorTag = getIndicatorTagByType(indicatorType);
        if (mInited) {
            refreshUiByTag(mIndicatorTag);
        }
    }

    private String getRepairTagByType(final int repairType) {
        String tag = mRepairTag;
        switch (repairType) {
            case KlineSettingConst.K_LINE_REPAIR:
                tag = getResources().getString(R.string.tag_kline_repair);
                break;
            case KlineSettingConst.K_LINE_UN_REPAIR:
                tag = getResources().getString(R.string.tag_kline_unrepair);
                break;
            default:
                break;
        }

        return tag;
    }

    private String getIndicatorTagByType(final int indicatorType) {
        String tag = mIndicatorTag;
        switch (indicatorType) {
            case SettingConst.K_LINE_INDICATOR_VOLUME:
                tag = getResources().getString(R.string.tag_kline_volume);
                break;
            case SettingConst.K_LINE_INDICATOR_MACD:
                tag = getResources().getString(R.string.tag_kline_macd);
                break;
            case SettingConst.K_LINE_INDICATOR_KDJ:
                tag = getResources().getString(R.string.tag_kline_kdj);
                break;
            case SettingConst.K_LINE_INDICATOR_RSI:
                tag = getResources().getString(R.string.tag_kline_rsi);
                break;
            case SettingConst.K_LINE_INDICATOR_BOLL:
                tag = getResources().getString(R.string.tag_kline_boll);
                break;
            case SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW:
                tag = getResources().getString(R.string.tag_kline_capital_flow);
                break;
            case SettingConst.K_LINE_INDICATOR_DMI:
                tag = getResources().getString(R.string.tag_kline_dmi);
                break;
            case SettingConst.K_LINE_INDICATOR_CCI:
                tag = getResources().getString(R.string.tag_kline_cci);
                break;
            case SettingConst.K_LINE_INDICATOR_ENE:
                tag = getResources().getString(R.string.tag_kline_ene);
                break;
            case SettingConst.K_LINE_INDICATOR_DMA:
                tag = getResources().getString(R.string.tag_kline_dma);
                break;
            case SettingConst.K_LINE_INDICATOR_EXPMA:
                tag = getResources().getString(R.string.tag_kline_expma);
                break;
            case SettingConst.K_LINE_INDICATOR_VR:
                tag = getResources().getString(R.string.tag_kline_vr);
                break;
            case SettingConst.K_LINE_INDICATOR_BBI:
                tag = getResources().getString(R.string.tag_kline_bbi);
                break;
            case SettingConst.K_LINE_INDICATOR_OBV:
                tag = getResources().getString(R.string.tag_kline_obv);
                break;
            case SettingConst.K_LINE_INDICATOR_BIAS:
                tag = getResources().getString(R.string.tag_kline_bias);
                break;
            case SettingConst.K_LINE_INDICATOR_WR:
                tag = getResources().getString(R.string.tag_kline_wr);
                break;
            case SettingConst.K_LINE_INDICATOR_BREAK:
                tag = getResources().getString(R.string.tag_kline_break);
                break;
            default:
                break;
        }

        return tag;
    }

    private void refreshUiByTag(String tag) {
        boolean isRepairGroup = false;
        for (String repairTag : mRepairTags) {
            if (TextUtils.equals(repairTag, tag)) {
                isRepairGroup = true;
                break;
            }
        }

        if (isRepairGroup) {
            for (String s : mRepairTags) {
                View view = findViewWithTag(s);
                view.setSelected(TextUtils.equals(s, tag));
            }
        } else {
            for (String s : mIndicatorTags) {
                View view = findViewWithTag(s);
                view.setSelected(TextUtils.equals(s, tag));
            }
        }
    }

    private void performControl(String tag) {
        if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_repair))) {
            mKLineControlListener.onRepairTypeChanged(KlineSettingConst.K_LINE_REPAIR);
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_unrepair))) {
            if (!mIsHongKongOrUsa) {
                mKLineControlListener.onRepairTypeChanged(KlineSettingConst.K_LINE_UN_REPAIR);
            }
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_volume))) {
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_VOLUME);
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_macd))) {
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_MACD);
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_kdj))) {
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_KDJ);
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_rsi))) {
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_RSI);
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_boll))) {
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_BOLL);
        } else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_capital_flow))) {
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_dmi))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_DMI);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_cci))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_CCI);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_ene))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_ENE);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_dma))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_DMA);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_expma))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_EXPMA);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_vr))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_VR);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_bbi))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_BBI);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_obv))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_OBV);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_bias))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_BIAS);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_wr))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_WR);
        }else if (TextUtils.equals(tag, getStringByResId(R.string.tag_kline_break))){
            mKLineControlListener.onTradingIndicatorTypeChanged(SettingConst.K_LINE_INDICATOR_BREAK);
        }
    }

    private String getStringByResId(int resId) {
        return mResources.getString(resId);
    }

    private OnKLineControlListener mKLineControlListener;

    public void setKLineControlListener(OnKLineControlListener KLineControlListener) {
        mKLineControlListener = KLineControlListener;
    }

    public void setDtSecCode(String dtSecCode) {
        mDtSecCode = dtSecCode;

        mIsHongKongOrUsa = StockUtil.isHongKongOrUSA(mDtSecCode);

        if (mIsHongKongOrUsa) {
            TextView textView = (TextView) findViewWithTag(mRepairTagStr);
            textView.setEnabled(false);
        }
    }

    public interface OnKLineControlListener {
        void onRepairTypeChanged(final int repairType);
        void onTradingIndicatorTypeChanged(final int indicatorType);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        IntentFilter filter = new IntentFilter();
        filter.addAction(LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
    }
}
