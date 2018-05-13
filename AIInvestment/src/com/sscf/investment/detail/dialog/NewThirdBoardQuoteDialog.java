package com.sscf.investment.detail.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.utils.ViewUtils;
import BEC.E_SEC_STATUS;
import BEC.SecQuote;

/**
 * Created by davidwei on 2017/05/02
 */
public final class NewThirdBoardQuoteDialog extends Dialog implements IQuoteDialog,
        DialogInterface.OnDismissListener, View.OnClickListener {
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
    private TextView mText82;
    private TextView mText84;
    private TextView mText92;
    private TextView mText94;
    private TextView mText102;

    private final SecurityDetailPresenter mPresenter;

    public NewThirdBoardQuoteDialog(@NonNull Context context, SecurityDetailPresenter presenter) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.dialog_new_third_board_quote);
        mPresenter = presenter;
        final View content = findViewById(R.id.content);
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final RectF frame = new RectF(
                            content.getX(),
                            content.getY(),
                            content.getX() + content.getWidth(),
                            content.getY() + content.getHeight());
                    if (!frame.contains(event.getX(), event.getY())) {
                        cancel();
                    }
                }
                return false;
            }
        });
        findViewById(R.id.close_button).setOnClickListener(this);
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
        mText82 = (TextView) findViewById(R.id.text82);
        mText84 = (TextView) findViewById(R.id.text84);
        mText92 = (TextView) findViewById(R.id.text92);
        mText94 = (TextView) findViewById(R.id.text94);
        mText102 = (TextView) findViewById(R.id.text102);

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_QUOTE_DETAIL);
    }

    public void updateQuote(final SecQuote quote) {
        final int tpFlag = quote.getITpFlag();
        final float close = quote.fClose;
        final boolean suspended = quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED;
        ViewUtils.setQuoteValueText(mText12, quote.fNow, close, tpFlag);
        ViewUtils.setQuoteValueText(mText14, quote.fAverageprice, close, tpFlag);
        ViewUtils.setQuoteValueText(mText22, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText24, quote.fMin, close, tpFlag);
        ViewUtils.setQuoteValueText(mText32, quote.fOpen, close, tpFlag);
        mText34.setText(StringUtil.getFormattedFloat(quote.fClose, tpFlag));
        mText42.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        mText44.setText(StringUtil.getAmplitudeString(quote));
        mText52.setText(StringUtil.getAmountString(quote));
        mText54.setText(StringUtil.getVolumeString(quote.lVolume, false, true, false));
        mText62.setText(StringUtil.getHandicapString(quote.lOutside, false, true, false, suspended));
        mText64.setText(StringUtil.getHandicapString(quote.lInside, false, true, false, suspended));
        if (quote.lOutside > 0) {
            mText62.setTextColor(StringUtil.getColorRed());
        } else {
            mText62.setTextColor(ContextCompat.getColor(getContext(), R.color.default_text_color_100));
        }
        if (quote.lInside > 0) {
            mText64.setTextColor(StringUtil.getColorGreen());
        } else {
            mText64.setTextColor(ContextCompat.getColor(getContext(), R.color.default_text_color_100));
        }
        mText62.setTextColor(StringUtil.getColorRed());
        mText64.setTextColor(StringUtil.getColorGreen());
        mText72.setText(StringUtil.getAppointString(quote));

        mText82.setText(StringUtil.getTradableSharesString(quote));
        mText84.setText(StringUtil.getAmountString(quote.fCirculationmarketvalue));
        mText92.setText(StringUtil.getTotalSharesString(quote));
        mText94.setText(StringUtil.getAmountString(quote.fTotalmarketvalue));
        mText102.setText(StringUtil.getPeString(quote.fSyl));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mPresenter.releaseDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                dismiss();
                break;
            default:
                break;
        }
    }
}
