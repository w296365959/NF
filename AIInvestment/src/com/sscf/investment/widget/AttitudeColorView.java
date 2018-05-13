package com.sscf.investment.widget;

import BEC.E_ATTITUDE_TYPE;
import BEC.E_TAG_TYPE;
import BEC.TagInfo;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;

import java.util.ArrayList;

/**
 * Created by liqf on 2015/11/23.
 */
public class AttitudeColorView extends View {
    private static final int DEFAULT_LENGTH = 2;
    private final int mWidthPerChar;
    private final int mRadius;
    private final int mColorStrongNegative;
    private final int mColorNegative;
    private final int mColorNeutrual;
    private final int mColorPositive;
    private final int mColorStrongPositive;

    private int mColorText;
    private final int mTextSize;
    private final int mTextHeight;
    private final int mTextBottom;
    private final TextDrawer mTextDrawer;

    private final int mHeight;
    private final int mWidth;
    private final int mAttitudeBarHeight;

    private String mTagName;
    private int mAttitudeType;
    private Paint mPaint;
    private RectF mRectF;

    private int lengthStrongNegative;
    private int lengthNegative;
    private int lengthNeutrual;
    private int lengthPositive;
    private int lengthStrongPositive;


    public AttitudeColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();

        Resources resources = getResources();
        mWidthPerChar = resources.getDimensionPixelSize(R.dimen.attitude_width_per_character);
        mWidth = resources.getDimensionPixelSize(R.dimen.attitude_width);
        mHeight = resources.getDimensionPixelSize(R.dimen.attitude_height);
        mAttitudeBarHeight = resources.getDimensionPixelSize(R.dimen.attitude_bar_height);
        mRadius = resources.getDimensionPixelSize(R.dimen.attitude_radius);

        mColorStrongNegative = resources.getColor(R.color.attitude_strong_negative);
        mColorNegative = resources.getColor(R.color.attitude_negative);
        mColorNeutrual = resources.getColor(R.color.attitude_neutrual);
        mColorPositive = resources.getColor(R.color.attitude_positive);
        mColorStrongPositive = resources.getColor(R.color.attitude_strong_positive);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.attitude_view_text_color
        });
        mColorText = a.getColor(0, Color.GRAY);
        a.recycle();

        mTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);

        mTextDrawer = new TextDrawer();
        mTextHeight = mTextDrawer.measureSingleTextHeight(mTextSize, null);
        mTextBottom = mTextDrawer.measureSingleTextBottom(mTextSize, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(mTagName)) {
            return;
        }

        getEachLength();

//        mAttitudeType = E_ATTITUDE_TYPE.ATT_REDUCTION;

        drawAttitudeBar(canvas);

        drawCurrentAttitude(canvas);
    }

    private void drawCurrentAttitude(Canvas canvas) {
        float left = 0;
        float right = 0;
        float top = 0;
        float bottom = mHeight;

        float textWidth;
        float attitudeWidth;
        float x;
        float y;

        textWidth = mTextDrawer.measureSingleTextWidth(mTagName, mTextSize, null);
        y = mHeight - ((mHeight - mTextHeight + mTextBottom) / 2);

        switch (mAttitudeType) {
            case E_ATTITUDE_TYPE.ATT_SELL:
                left = 0;
                right = mRadius * 2;

                mRectF.set(left, top, right, bottom);
                mPaint.setColor(mColorStrongNegative);
                canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

                left = mRadius;
                right =  left + lengthStrongNegative;
                canvas.drawRect(left, top, right, bottom, mPaint);

                attitudeWidth = lengthStrongNegative + mRadius;
                x = (attitudeWidth - textWidth) / 2;
                mPaint.setColor(mColorText);
                canvas.drawText(mTagName, x, y, mPaint);
                break;
            case E_ATTITUDE_TYPE.ATT_REDUCTION:
                left = lengthStrongNegative + mRadius;
                right = left + lengthNegative;
                mPaint.setColor(mColorNegative);
                canvas.drawRect(left, top, right, bottom, mPaint);

                attitudeWidth = lengthNegative;
                x = left + (attitudeWidth - textWidth) / 2;
                mPaint.setColor(mColorText);
                canvas.drawText(mTagName, x, y, mPaint);
                break;
            case E_ATTITUDE_TYPE.ATT_NEUTRUAL:
                left = lengthStrongNegative + lengthNegative + mRadius;
                right = left + lengthNeutrual;
                mPaint.setColor(mColorNeutrual);
                canvas.drawRect(left, top, right, bottom, mPaint);

                attitudeWidth = lengthNeutrual;
                x = left + (attitudeWidth - textWidth) / 2;
                mPaint.setColor(mColorText);
                canvas.drawText(mTagName, x, y, mPaint);
                break;
            case E_ATTITUDE_TYPE.ATT_HOLDINGS:
                left = lengthStrongNegative + lengthNegative + lengthNeutrual + mRadius;
                right = left + lengthPositive;
                mPaint.setColor(mColorPositive);
                canvas.drawRect(left, top, right, bottom, mPaint);

                attitudeWidth = lengthPositive;
                x = left + (attitudeWidth - textWidth) / 2;
                mPaint.setColor(mColorText);
                canvas.drawText(mTagName, x, y, mPaint);
                break;
            case E_ATTITUDE_TYPE.ATT_BUY:
                left = lengthStrongNegative + lengthNegative + lengthNeutrual + lengthPositive + mRadius;
                right = left + lengthStrongPositive;
                mPaint.setColor(mColorStrongPositive);
                canvas.drawRect(left, top, right, bottom, mPaint);

                left = right - mRadius;
                right = left + mRadius * 2;
                mRectF.set(left, top, right, bottom);
                canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

                attitudeWidth = lengthStrongPositive + mRadius;
                left = lengthStrongNegative + lengthNegative + lengthNeutrual + lengthPositive + mRadius;
                x = left + (attitudeWidth - textWidth) / 2;
                mPaint.setColor(mColorText);
                canvas.drawText(mTagName, x, y, mPaint);
                break;
            default:
                break;
        }
    }

    private void drawAttitudeBar(Canvas canvas) {
        float left;
        float right;
        float top;
        float bottom;

        float gap = (mHeight - mAttitudeBarHeight) / 2;
        top = gap;
        bottom = gap + mAttitudeBarHeight;

        //强烈利空
        left = 0;
        right = mRadius * 2;

        mRectF.set(left, top, right, bottom);
        mPaint.setColor(mColorStrongNegative);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

        left = mRadius;
        right =  left + lengthStrongNegative;
        canvas.drawRect(left, top, right, bottom, mPaint);

        //利空
        left = right;
        right = left + lengthNegative;
        mPaint.setColor(mColorNegative);
        canvas.drawRect(left, top, right, bottom, mPaint);

        //中性
        left = right;
        right = left + lengthNeutrual;
        mPaint.setColor(mColorNeutrual);
        canvas.drawRect(left, top, right, bottom, mPaint);

        //利好
        left = right;
        right = left + lengthPositive;
        mPaint.setColor(mColorPositive);
        canvas.drawRect(left, top, right, bottom, mPaint);

        //强烈利好
        left = right;
        right = left + lengthStrongPositive;
        mPaint.setColor(mColorStrongPositive);
        canvas.drawRect(left, top, right, bottom, mPaint);

        left = right - mRadius;
        right = left + mRadius * 2;
        mRectF.set(left, top, right, bottom);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }

    private void getEachLength() {
        int length = mTagName.length() * mWidthPerChar;
        switch (mAttitudeType) {
            case E_ATTITUDE_TYPE.ATT_SELL:
                lengthStrongNegative = length;
                break;
            case E_ATTITUDE_TYPE.ATT_REDUCTION:
                lengthNegative = length;
                break;
            case E_ATTITUDE_TYPE.ATT_NEUTRUAL:
                lengthNeutrual = length;
                break;
            case E_ATTITUDE_TYPE.ATT_HOLDINGS:
                lengthPositive = length;
                break;
            case E_ATTITUDE_TYPE.ATT_BUY:
                lengthStrongPositive = length;
                break;
            default:
                break;
        }
    }

    public void setData(ArrayList<TagInfo> tagInfos) {
        if (tagInfos == null || tagInfos.size() == 0) {
            setVisibility(GONE);
            return;
        }

        TagInfo info = tagInfos.get(0);
        int attiType = info.getEAttiType();
        if (attiType == E_ATTITUDE_TYPE.ATT_NULL || attiType == E_ATTITUDE_TYPE.ATT_UNRATE) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        for (TagInfo tagInfo : tagInfos) {
            int tagType = tagInfo.getETagType();
            if (tagType != E_TAG_TYPE.E_TT_COMMON) {
                continue;
            }

            mTagName = tagInfo.getSTagName();
            mAttitudeType = tagInfo.getEAttiType();

            //for test
//            mTagName = "强烈利空";
//            mAttitudeType = E_ATTITUDE_TYPE.ATT_SELL;
//            mTagName = "利空";
//            mAttitudeType = E_ATTITUDE_TYPE.ATT_REDUCTION;
//            mTagName = "中性";
//            mAttitudeType = E_ATTITUDE_TYPE.ATT_NEUTRUAL;
//            mTagName = "利好";
//            mAttitudeType = E_ATTITUDE_TYPE.ATT_HOLDINGS;
//            mTagName = "强烈利好";
//            mAttitudeType = E_ATTITUDE_TYPE.ATT_BUY;
            break;
        }

        resetLocalData();

        invalidate();
    }

    private void resetLocalData() {
        int defaultLength = (mWidth - mTagName.length() * mWidthPerChar - mRadius * 2) / 4;
        lengthStrongNegative = defaultLength;
        lengthNegative = defaultLength;
        lengthNeutrual = defaultLength;
        lengthPositive = defaultLength;
        lengthStrongPositive = defaultLength;
    }
}
