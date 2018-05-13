package com.sscf.investment.detail.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.utils.ViewUtils;
import BEC.E_SEC_STATUS;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017/04/26.
 */
public final class HandicapMarketView extends ConstraintLayout {
    private TextView mText12;
    private TextView mText14;
    private TextView mText22;
    private TextView mText24;
    private TextView mText32;
    private TextView mText34;
    private TextView mText42;
    private TextView mText44;
    private TextView mText52;
    private TextView mText54;
    private TextView mText62;
    private TextView mText64;
    private TextView mText72;
    private TextView mText74;
    private TextView mText82;
    private TextView mText84;
    private TextView mText92;
    private TextView mText94;
    private TextView mText102;
    private TextView mText104;
    private TextView mText112;

    private AHPremiumLayout mAHLayout;

    private SecQuote mQuote;

    public HandicapMarketView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mText12 = (TextView) findViewById(R.id.text12);
        mText14 = (TextView) findViewById(R.id.text14);
        mText22 = (TextView) findViewById(R.id.text22);
        mText24 = (TextView) findViewById(R.id.text24);
        mText32 = (TextView) findViewById(R.id.text32);
        mText34 = (TextView) findViewById(R.id.text34);
        mText42 = (TextView) findViewById(R.id.text42);
        mText44 = (TextView) findViewById(R.id.text44);
        mText52 = (TextView) findViewById(R.id.text52);
        mText54 = (TextView) findViewById(R.id.text54);
        mText62 = (TextView) findViewById(R.id.text62);
        mText64 = (TextView) findViewById(R.id.text64);
        mText72 = (TextView) findViewById(R.id.text72);
        mText74 = (TextView) findViewById(R.id.text74);
        mText82 = (TextView) findViewById(R.id.text82);
        mText84 = (TextView) findViewById(R.id.text84);
        mText92 = (TextView) findViewById(R.id.text92);
        mText94 = (TextView) findViewById(R.id.text94);
        mText102 = (TextView) findViewById(R.id.text102);
        mText104 = (TextView) findViewById(R.id.text104);
        mText112 = (TextView) findViewById(R.id.text112);

        mAHLayout = (AHPremiumLayout) findViewById(R.id.ahLayout);
        mAHLayout.setType(AHPremiumLayout.SEC_TYPE_CHINA);
    }

    public void updateQupte(SecQuote quote) {
        mQuote = quote;
        final int tpFlag = quote.getITpFlag();
        final float close = quote.fClose;
        final boolean suspended = quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED;
        ViewUtils.setQuoteValueText(mText12, quote.fNow, close, tpFlag);
        ViewUtils.setQuoteValueText(mText14, quote.fAverageprice, close, tpFlag);
        ViewUtils.setQuoteValueText(mText22, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText24, quote.fMin, close, tpFlag);
        ViewUtils.setQuoteValueText(mText32, quote.fMaxLimit, close, tpFlag);
        ViewUtils.setQuoteValueText(mText34, quote.fMinLimit, close, tpFlag);
        ViewUtils.setQuoteValueText(mText42, quote.fOpen, close, tpFlag);
        mText44.setText(StringUtil.getFormattedFloat(quote.fClose, tpFlag));
        mText52.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        mText54.setText(StringUtil.getAmplitudeString(quote));
        mText62.setText(StringUtil.getAmountString(quote));
        mText64.setText(StringUtil.getVolumeString(quote.lVolume, false, true, false));

        mText72.setText(StringUtil.getVolumeRatioString(quote));
        mText74.setText(StringUtil.getAppointString(quote));


        mText82.setText(StringUtil.getHandicapString(quote.lOutside, false, true, false, suspended));
        mText84.setText(StringUtil.getHandicapString(quote.lInside, false, true, false, suspended));
        if (quote.lOutside > 0) {
            mText82.setTextColor(StringUtil.getColorRed());
        } else {
            mText82.setTextColor(ContextCompat.getColor(getContext(), R.color.default_text_color_100));
        }
        if (quote.lInside > 0) {
            mText84.setTextColor(StringUtil.getColorGreen());
        } else {
            mText84.setTextColor(ContextCompat.getColor(getContext(), R.color.default_text_color_100));
        }

        mText92.setText(StringUtil.getTradableSharesString(quote));
        mText94.setText(StringUtil.getAmountString(quote.fCirculationmarketvalue));
        mText102.setText(StringUtil.getTotalSharesString(quote));
        mText104.setText(StringUtil.getAmountString(quote.fTotalmarketvalue));
        mText112.setText(StringUtil.getPeString(quote.fSyl));

        mAHLayout.setAQuote(StockUtil.getSimpleQuoteFromQuote(quote));
    }

    public void updateAHpremuim(final SecSimpleQuote hQuote) {
        mAHLayout.setVisibility(VISIBLE);
        mAHLayout.setHQuote(hQuote);
    }
}
