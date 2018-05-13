package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.detail.entity.BuySellEntity;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by liqf on 2015/7/30.
 */
public class BuySellView extends View {
    public static final int COUNT = 5;
    private int mDrawingWidth;
    private int mDrawingHeight;

    private Paint mDividerPaint;
    private Paint mTextPaint;
    private Paint mPricePaint;
    private Paint mVolumePaint;
    private int mTitleColor;
    private int mVolumeColor;
    private int mColorRed;
    private int mColorGreen;
    private int mDividerMarginTop;
    private int mDividerStrokeWidth;
    private int mTextSize;
    private String mStrBuy;
    private String mStrSell;
    private TextDrawer mTextDrawer;
    private Typeface mTypeface;
    private int mTitleWidth;
    private int mPriceWidth;
    private int mVolumeWidth;
    private int mTextHeight;
    private BuySellEntity mBuySellEntity;

    private int mTextPadding;

    public BuySellView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initResources();
    }

    private void initResources() {
        Resources resources = getResources();
        mTextDrawer = new TextDrawer();
        mTypeface = TextDrawer.getTypeface();
        mTextSize = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_textSize);
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, mTypeface);

        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);

        mTitleColor = ContextCompat.getColor(getContext(), R.color.default_text_color_60);
        mVolumeColor = ContextCompat.getColor(getContext(), R.color.default_text_color_80);

        mDividerStrokeWidth = resources.getDimensionPixelSize(R.dimen.capital_divider_height);
        mDividerMarginTop = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_divider_marginTop);
        mStrBuy = resources.getString(R.string.stock_detail_buy);
        mStrSell = resources.getString(R.string.stock_detail_sell);
        mTextPadding = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_textPadding);

        mTitleWidth = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_title_width);
        mPriceWidth = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_price_width);
        mVolumeWidth = resources.getDimensionPixelSize(R.dimen.stock_buy_sell_volume_width);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTitleColor);

        mPricePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPricePaint.setColor(Color.RED);
        mPricePaint.setTextSize(mTextSize);
        mPricePaint.setTypeface(mTypeface);

        mVolumePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVolumePaint.setColor(mVolumeColor);
        mVolumePaint.setTextSize(mTextSize);
        mVolumePaint.setTypeface(mTypeface);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawingWidth = getMeasuredWidth();
        mDrawingHeight = getMeasuredHeight();

        mVolumeWidth = mDrawingWidth - mTitleWidth - mPriceWidth;

        drawBuySellItems(canvas);
    }

    private void drawBuySellItems(Canvas canvas) {
        int dividerGap = 2 * mDividerMarginTop + mDividerStrokeWidth;
        int areaHeight = (mDrawingHeight - dividerGap) / 2;

        int itemHeight = areaHeight / 5;

        String title = "";

        String price = "";
        int priceWidth;

        String volume = "";
        int volumeWidth;

        float[] sellValues, buyValues;
        long[] sellVolumes, buyVolumes;
        if (mBuySellEntity != null) {
            sellValues = mBuySellEntity.getSellValues();
            sellVolumes = mBuySellEntity.getSellVolumes();
            buyValues = mBuySellEntity.getBuyValues();
            buyVolumes = mBuySellEntity.getBuyVolumes();
        } else {
            sellValues = new float[] {0, 0, 0, 0, 0};
            sellVolumes = new long[] {0, 0, 0, 0, 0};
            buyValues = new float[] {0, 0, 0, 0, 0};
            buyVolumes = new long[] {0, 0, 0, 0, 0};
        }

        int tpFlag = mBuySellEntity != null ? mBuySellEntity.getTpFlag() : DengtaConst.DEFAULT_TP_FLAG;

        int textSize;
        float priceValue;
        int x = 0, y = mTextHeight;
        for (int i = 0; i < COUNT; i++) { //五档卖盘
            x = 0;

            int index = COUNT - i - 1;
            title = mStrSell + (index + 1);
            canvas.drawText(title, x, y, mTextPaint);

            x += mTitleWidth;
            priceValue = sellValues[index];
            price = StringUtil.getFormattedFloat(priceValue, tpFlag);
//            price = "123456.78";
            priceWidth = mTextDrawer.measureSingleTextWidth(price, mTextSize, mTypeface);
            textSize = mTextSize;
            while (priceWidth > mPriceWidth - mTextPadding) {
                priceWidth = mTextDrawer.measureSingleTextWidth(price, --textSize, mTypeface);
            }
            mPricePaint.setTextSize(textSize);
            setPriceColor(priceValue);
            canvas.drawText(price, x, y, mPricePaint);

            x += mPriceWidth;
            volume = StringUtil.getVolumeString(sellVolumes[index], false, true, false);
            volumeWidth = mTextDrawer.measureSingleTextWidth(volume, mTextSize, mTypeface);
            textSize = mTextSize;
            while (volumeWidth > mVolumeWidth - mTextPadding) {
                volumeWidth = mTextDrawer.measureSingleTextWidth(volume, --textSize, mTypeface);
            }
            mVolumePaint.setTextSize(textSize);
            x += mVolumeWidth - volumeWidth;
            canvas.drawText(volume, x, y, mVolumePaint);

            y += itemHeight;
        }

        y = mDrawingHeight - itemHeight * (COUNT - 1) - mTextPadding;
        for (int i = 0; i < COUNT; i++) { //五档买盘
            x = 0;

            title = mStrBuy + (i + 1);
            canvas.drawText(title, x, y, mTextPaint);

            x += mTitleWidth;
            priceValue = buyValues[i];
            price = StringUtil.getFormattedFloat(priceValue, tpFlag);
//            price = "123456.78";
            priceWidth = mTextDrawer.measureSingleTextWidth(price, mTextSize, mTypeface);
            textSize = mTextSize;
            while (priceWidth > mPriceWidth - mTextPadding) {
                priceWidth = mTextDrawer.measureSingleTextWidth(price, --textSize, mTypeface);
            }
            mPricePaint.setTextSize(textSize);
            setPriceColor(priceValue);
            canvas.drawText(price, x, y, mPricePaint);

            x += mPriceWidth;
            volume = StringUtil.getVolumeString(buyVolumes[i], false, true, false);
            volumeWidth = mTextDrawer.measureSingleTextWidth(volume, mTextSize, mTypeface);
            textSize = mTextSize;
            while (volumeWidth > mVolumeWidth - mTextPadding) {
                volumeWidth = mTextDrawer.measureSingleTextWidth(volume, --textSize, mTypeface);
            }
            mVolumePaint.setTextSize(textSize);
            x += mVolumeWidth - volumeWidth;
            canvas.drawText(volume, x, y, mVolumePaint);

            y += itemHeight;
        }
    }

    private void setPriceColor(float priceValue) {
        float yesterdayClose = mBuySellEntity == null ? 0 : mBuySellEntity.getYesterdayClose();
        if (priceValue == 0) {
            mPricePaint.setColor(mTitleColor);
        } else {
            if (priceValue > yesterdayClose) {
                mPricePaint.setColor(mColorRed);
            } else if (priceValue < yesterdayClose) {
                mPricePaint.setColor(mColorGreen);
            } else {
                mPricePaint.setColor(mTitleColor);
            }
        }
    }

    public void setData(BuySellEntity entity) {
        mBuySellEntity = entity;
        invalidate();
    }
}
