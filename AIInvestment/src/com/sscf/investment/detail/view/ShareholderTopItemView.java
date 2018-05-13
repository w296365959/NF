package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;

import BEC.ShareholderChange;
import BEC.TopShareholder;

/**
 * Created by yorkeehuang on 2017/8/2.
 */

public class ShareholderTopItemView extends LinearLayout {

    private static final String TAG = ShareholderTopItemView.class.getSimpleName();

    private int mColorRed;
    private int mColorGreen;
    private int mColorBase;

    private TextView mNameView;
    private TextView mTopRatioView;
    private TextView mTopChangeView;

    public ShareholderTopItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
        mColorBase = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNameView = (TextView) findViewById(R.id.shareholder_top_name);
        mTopRatioView = (TextView) findViewById(R.id.shareholder_top_ratio);
        mTopChangeView = (TextView) findViewById(R.id.shareholder_top_change);
    }

    public void setData(TopShareholder topholder) {
        mNameView.setText(topholder.sName);//姓名
        mTopRatioView.setText(topholder.getSRatio());//比例
        setTextAndColor(mTopChangeView, topholder.getSChangeDetail(), topholder.getEShareholderChange()); //持股变动详情
    }

    private void setTextAndColor(TextView changeView, String text, int changeType) {
        changeView.setText(text);
        switch (changeType) {
            case ShareholderChange.SHC_UNCHANGE:
                break;
            case ShareholderChange.SHC_INCREASE:
                changeView.setTextColor(mColorRed);
                break;
            case ShareholderChange.SHC_DECREASE:
                changeView.setTextColor(mColorGreen);
                break;
            default:
                break;
        }
    }
}
