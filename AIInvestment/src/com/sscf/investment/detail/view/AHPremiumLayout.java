package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;
import com.sscf.investment.utils.StringUtil;
import BEC.E_SEC_STATUS;
import BEC.SecSimpleQuote;

/**
 * Created by liqf on 2016/5/19.
 */
public final class AHPremiumLayout extends LinearLayout {
    public static final int SEC_TYPE_CHINA = 0;
    public static final int SEC_TYPE_HONGKONG = 1;
    private int mType = SEC_TYPE_CHINA;
    private float mANow;
    private float mHNow;
    private TextView mTitle;
    private TextView mPriceText;
    private TextView mDeltaText;
    private TextView mAHPremium;
    private int mColorRed;
    private int mColorGreen;
    private int mColorBase;

    public AHPremiumLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initResources();
        initViews();
    }

    private void initResources() {
        Resources resources = getResources();
        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
        mColorBase = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
    }

    private void initViews() {
        mTitle = (TextView) findViewById(R.id.title);
        mPriceText = (TextView) findViewById(R.id.price);
        mDeltaText = (TextView) findViewById(R.id.delta);
        mAHPremium = (TextView) findViewById(R.id.h_a_premium);
    }

    public void setType(int type) {
        mType = type;

        if (type == SEC_TYPE_CHINA) {
            mTitle.setText(R.string.sec_hongkong);
        } else if (type == SEC_TYPE_HONGKONG) {
            mTitle.setText(R.string.sec_china);
        }
    }

    public void setAQuote(SecSimpleQuote quote) {
        if (quote == null) {
            return;
        }

        mANow = quote.getFNow();
        if (mANow == 0) {
            mANow = quote.getFClose();
        }
        setAHPremium();

        if (mType == SEC_TYPE_CHINA) {
            return;
        }

        float nowPrice = mANow;
        float yesterdayClose = quote.getFClose();
        int secStatus = quote.getESecStatus();
        int tpFlag = quote.getITpFlag(); //小数精度

        String nullStr = "--";
        if (secStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            mPriceText.setText(R.string.suspended);
            mDeltaText.setText("");
            return;
        }

        if (nowPrice == 0) {
            mPriceText.setText(nullStr);
            mDeltaText.setText(nullStr);
            return;
        }

        mPriceText.setText(StringUtil.getFormattedFloat(nowPrice, tpFlag));

        float changeValue = nowPrice - yesterdayClose;
        float change = changeValue / yesterdayClose;
        mDeltaText.setText(StringUtil.getChangePercentString(change));

        setTextColorByValues(mPriceText, nowPrice, yesterdayClose);
        setTextColorByValues(mDeltaText, nowPrice, yesterdayClose);
    }

    public void setHQuote(SecSimpleQuote quote) {
        if (quote == null) {
            return;
        }

        mHNow = quote.getFNow();
        if (mHNow == 0) {
            mHNow = quote.getFClose();
        }
        setAHPremium();

        if (mType == SEC_TYPE_HONGKONG) {
            return;
        }

        float nowPrice = mHNow;
        float yesterdayClose = quote.getFClose();
        int secStatus = quote.getESecStatus();
        int tpFlag = quote.getITpFlag(); //小数精度

        String nullStr = "--";
        if (secStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            mPriceText.setText(R.string.suspended);
            mDeltaText.setText("");
            return;
        }

        if (nowPrice == 0) {
            mPriceText.setText(nullStr);
            mDeltaText.setText(nullStr);
            return;
        }

        mPriceText.setText(StringUtil.getFormattedFloat(nowPrice, tpFlag));

        float changeValue = nowPrice - yesterdayClose;
        float change = changeValue / yesterdayClose;
        mDeltaText.setText(StringUtil.getChangePercentString(change));

        setTextColorByValues(mPriceText, nowPrice, yesterdayClose);
        setTextColorByValues(mDeltaText, nowPrice, yesterdayClose);
    }

    private void setAHPremium() {
        if (mANow == 0 || mHNow == 0) {
            String nullStr = "--";
            mAHPremium.setText(nullStr);
            return;
        }

        float exchangeRate = 0.8f;
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance().getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            exchangeRate = marketManager.getHKDollarsExchangeRate();
        }

        float premium = 1 - mANow / (mHNow * exchangeRate);
        mAHPremium.setText(StringUtil.getPercentString(premium));
    }

    private void setTextColorByValues(TextView textView, float value, float baseValue) {
        if (value > baseValue) {
            textView.setTextColor(mColorRed);
        } else if (value < baseValue) {
            textView.setTextColor(mColorGreen);
        } else {
            textView.setTextColor(mColorBase);
        }
    }
}
