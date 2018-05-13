package com.sscf.investment.market.view;

import BEC.SecSimpleQuote;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by liqf on 2015/8/10.
 */
public final class MarketIndexInfoView extends RelativeLayout {

    private TextView mNameText;
    private TextView mValueText;
    private TextView mDeltaText;
    private View mAllAView;

    public MarketIndexInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Drawable mBackground = ContextCompat.getDrawable(getContext(), R.drawable.list_item_bg);
        setBackground(mBackground);
        mNameText = (TextView) findViewById(R.id.name);
        mValueText = (TextView) findViewById(R.id.value);
        mDeltaText = (TextView) findViewById(R.id.delta);
        mAllAView = findViewById(R.id.allA);
    }

    public void updateInfos(SecSimpleQuote secQuote) {
        if (!TextUtils.isEmpty(secQuote.sSecName)) {
            mNameText.setText(secQuote.sSecName);
        }

        if (DengtaConst.DENGTA_DT_CODE.equals(secQuote.sDtSecCode)) {
            mAllAView.setVisibility(VISIBLE);
        } else {
            mAllAView.setVisibility(GONE);
        }

        final SpannableStringBuilder indexText = new SpannableStringBuilder();
        final SpannableStringBuilder deltaText = new SpannableStringBuilder();

        TextAppearanceSpan span = null;

        if (secQuote.fClose > 0 && secQuote.fNow > 0) {
            final float delta = secQuote.fNow - secQuote.fClose;

            final float updown = (secQuote.fNow / secQuote.fClose - 1) * 100;
            if (delta > 0) {
                span = StringUtil.getUpStyle();
                indexText.append(' ');
                final ImageSpan imageSpan = new ImageSpan(getContext(), R.drawable.price_up_arrow, DynamicDrawableSpan.ALIGN_BASELINE);
                indexText.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                deltaText.append('+').append(StringUtil.getFormattedFloat(delta, secQuote.iTpFlag)).append(' ').append(' ').append('+').append(StringUtil.getFormatedFloat(updown)).append('%');
            } else if (delta < 0) {
                span = StringUtil.getDownStyle();
                indexText.append(' ');
                final ImageSpan imageSpan = new ImageSpan(getContext(), R.drawable.price_down_arrow, DynamicDrawableSpan.ALIGN_BASELINE);
                indexText.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                deltaText.append(StringUtil.getFormattedFloat(delta, secQuote.iTpFlag)).append(' ').append(' ').append(StringUtil.getFormatedFloat(updown)).append('%');
            } else {
                span = StringUtil.getSuspensionStyle();
                deltaText.append(StringUtil.getFormattedFloat(delta, secQuote.iTpFlag)).append(' ').append(' ').append(StringUtil.getFormatedFloat(updown)).append('%');
            }
        } else {
            span = StringUtil.getSuspensionStyle();
            deltaText.append("--").append(' ').append(' ').append("--");
        }

        final float now = secQuote.fNow > 0f ? secQuote.fNow : secQuote.fClose;

        indexText.append(now > 0 ? StringUtil.getFormattedFloat(now, secQuote.iTpFlag) : "--");
        indexText.setSpan(span, 0, indexText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mValueText.setText(indexText);

        deltaText.setSpan(span, 0, deltaText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mDeltaText.setText(deltaText);
    }
}
