package com.sscf.investment.detail.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.utils.ViewUtils;
import BEC.E_MARKET_TYPE;
import BEC.E_SEC_TYPE;
import BEC.SecQuote;

/**
 * Created by davidwei on 2017/05/10
 */
public final class LandscapeDetailQuoteLayout extends ConstraintLayout {

    // ---------- quote区域 ----------
    private TextView mText11;
    private TextView mText13;
    private TextView mText14;
    private TextView mText15;
    private TextView mText16;
    private TextView mText17;
    private TextView mText21;
    private TextView mText23;
    private TextView mText24;
    private TextView mText25;
    private TextView mText26;
    private TextView mText27;
    // ---------- quote区域 ----------

    public LandscapeDetailQuoteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mText11 = (TextView) findViewById(R.id.text11);
        mText13 = (TextView) findViewById(R.id.text13);
        mText14 = (TextView) findViewById(R.id.text14);
        mText15 = (TextView) findViewById(R.id.text15);
        mText16 = (TextView) findViewById(R.id.text16);
        mText17 = (TextView) findViewById(R.id.text17);
        mText21 = (TextView) findViewById(R.id.text21);
        mText23 = (TextView) findViewById(R.id.text23);
        mText24 = (TextView) findViewById(R.id.text24);
        mText25 = (TextView) findViewById(R.id.text25);
        mText26 = (TextView) findViewById(R.id.text26);
        mText27 = (TextView) findViewById(R.id.text27);
    }

    public void setKeysText(final String dtSecCode) {
        int secType = StockUtil.convertSecInfo(dtSecCode).getESecType();
        int marketType = StockUtil.getMarketType(dtSecCode);

        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                    case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                        mText16.setText(R.string.turnover);
                        mText26.setText(R.string.pe);
                        break;
                    case E_MARKET_TYPE.E_MT_HK: //港股
                    case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
                    case E_MARKET_TYPE.E_MT_NYSE: //纽交所
                    case E_MARKET_TYPE.E_MT_AMEX: //美交所
                    case E_MARKET_TYPE.E_MT_TB: // 新三板
                        mText16.setText(R.string.turnover_short);
                        mText26.setText(R.string.volume_short);
                        break;
                    default:
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                mText16.setText(R.string.turnover);
                mText26.setText(R.string.volume);
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                // 股指期货
                mText16.setText(R.string.detail_daily_change);
                mText26.setText(R.string.detail_postion);
                break;
            case E_SEC_TYPE.E_ST_INDEX: // 指数
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_DT:
                    default:
                        mText14.setText(R.string.today_open);
                        mText16.setVisibility(INVISIBLE);
                        mText17.setVisibility(INVISIBLE);
                        mText24.setText(R.string.yestoday_close);
                        mText26.setVisibility(INVISIBLE);
                        mText27.setVisibility(INVISIBLE);
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                mText14.setText(R.string.today_open);
                mText16.setVisibility(INVISIBLE);
                mText17.setVisibility(INVISIBLE);
                mText24.setText(R.string.yestoday_close);
                mText26.setVisibility(INVISIBLE);
                mText27.setVisibility(INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void updateQuote(final SecQuote quote) {
        final String dtSecCode = quote.sDtSecCode;;
        int secType = StockUtil.convertSecInfo(dtSecCode).getESecType();
        int marketType = StockUtil.getMarketType(dtSecCode);

        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                    case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                        updateStockQuote(quote);
                        break;
                    case E_MARKET_TYPE.E_MT_HK: //港股
                        updateHongKongStockQuote(quote);
                        break;
                    case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
                    case E_MARKET_TYPE.E_MT_NYSE: //纽交所
                    case E_MARKET_TYPE.E_MT_AMEX: //美交所
                        updateUSAStockQuote(quote);
                        break;
                    case E_MARKET_TYPE.E_MT_TB: // 新三板
                        updateNewThirdBoardQuote(quote);
                        break;
                    default:
                        updateStockQuote(quote);
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                updateFundQuote(quote);
                break;
            case E_SEC_TYPE.E_ST_INDEX:                // 指数
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_DT:
                        updateDengtaAQuote(quote);
                        break;
                    default:
                        updateIndexQuote(quote);
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                updatePlateQuote(quote);
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                // 股指期货
                updateIndexFutureQuote(quote);
                break;
            default:
                updateStockQuote(quote);
                break;
        }
    }

    private void updateStockQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.volume_ratio);
        mText27.setText(StringUtil.getVolumeRatioString(quote));
    }

    private void updateHongKongStockQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.volume_short);
        mText27.setText(StringUtil.getVolumeString(quote.lVolume, false, true, false));
    }

    private void updateUSAStockQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.volume_short);
        mText27.setText(StringUtil.getVolumeString(quote.lVolume, false, true, false));
    }

    private void updateNewThirdBoardQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.volume_short);
        mText27.setText(StringUtil.getVolumeString(quote.lVolume, false, true, false));
    }

    private void updateFundQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(StringUtil.getTurnOverRateString(quote.fFhsl));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.volume_ratio);
        mText27.setText(StringUtil.getVolumeRatioString(quote));
    }

    private void updateIndexQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText24.setText(R.string.yestoday_close);
        mText25.setText(StringUtil.getFormattedFloat(quote.fClose, tpFlag));
    }

    private void updatePlateQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText24.setText(R.string.yestoday_close);
        mText25.setText(StringUtil.getFormattedFloat(quote.fClose, tpFlag));
    }

    private void updateIndexFutureQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        mText17.setText(String.valueOf((int)quote.fDayincrease));
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText25.setText(StringUtil.getAmountString(quote));
        mText26.setText(R.string.detail_postion);
        mText27.setText(String.valueOf(quote.lVolinstock));
    }

    private void updateDengtaAQuote(final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = quote.fClose;
        ViewUtils.setQuoteView(mText11, mText21, quote);
        ViewUtils.setQuoteValueText(mText13, quote.fMax, close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, quote.fOpen, close, tpFlag);
        ViewUtils.setQuoteValueText(mText23, quote.fMin, close, tpFlag);
        mText24.setText(R.string.yestoday_close);
        mText25.setText(StringUtil.getFormattedFloat(quote.fClose, tpFlag));
    }

    public void updateKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        if (quote == null) {
            return;
        }

        final String dtSecCode = quote.sDtSecCode;
        int secType = StockUtil.convertSecInfo(dtSecCode).getESecType();
        int marketType = StockUtil.getMarketType(dtSecCode);

        switch (secType) {
            case E_SEC_TYPE.E_ST_STOCK:
                // 股票
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                    case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                        updateStockKLineQuote(event, quote);
                        break;
                    case E_MARKET_TYPE.E_MT_HK: //港股
                        updateHongKongStockKLineQuote(event, quote);
                        break;
                    case E_MARKET_TYPE.E_MT_NASDAQ: //纳斯达克
                    case E_MARKET_TYPE.E_MT_NYSE: //纽交所
                    case E_MARKET_TYPE.E_MT_AMEX: //美交所
                        updateUSAStockKLineQuote(event, quote);
                        break;
                    case E_MARKET_TYPE.E_MT_TB: // 新三板
                        updateNewThirdBoardKLineQuote(event, quote);
                        break;
                    default:
                        updateStockKLineQuote(event, quote);
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_FUND:
                // 基金
                updateFundKLineQuote(event, quote);
                break;
            case E_SEC_TYPE.E_ST_INDEX:                // 指数
                switch (marketType) {
                    case E_MARKET_TYPE.E_MT_DT:
                        updateDengtaAKLineQuote(event, quote);
                        break;
                    default:
                        updateIndexKLineQuote(event, quote);
                        break;
                }
                break;
            case E_SEC_TYPE.E_ST_PLATE:                // 行业板块
                updatePlateKLineQuote(event, quote);
                break;
            case E_SEC_TYPE.E_ST_FUTURES:
                // 股指期货
                updateIndexFutureKLineQuote(event, quote);
                break;
            default:
                updateStockKLineQuote(event, quote);
                break;
        }
    }

    public void updateStockKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText(StringUtil.getKLineTurnOverRateString(event.getVolume(), quote));
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
    }

    public void updateHongKongStockKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText(StringUtil.getKLineTurnOverRateString(event.getVolume(), quote));
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
    }

    public void updateUSAStockKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText(StringUtil.getKLineTurnOverRateString(event.getVolume(), quote));
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
    }

    public void updateNewThirdBoardKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText(StringUtil.getKLineTurnOverRateString(event.getVolume(), quote));
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
    }

    public void updateFundKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText(StringUtil.getKLineTurnOverRateString(event.getVolume(), quote));
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
    }

    public void updateDengtaAKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText24.setText(time);
        mText25.setText("");
    }

    public void updateIndexKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText24.setText(time);
        mText25.setText("");
    }

    public void updatePlateKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText24.setText(time);
        mText25.setText("");
    }

    public void updateIndexFutureKLineQuote(final KLineInfosView.KLineLineTouchEvent event, final SecQuote quote) {
        final int tpFlag = quote.iTpFlag;
        final float close = event.getYesterdayClose();
        final float now = event.getClose();

        ViewUtils.setQuoteView(mText11, mText21, close, now, tpFlag);
        ViewUtils.setQuoteValueText(mText13, event.getHigh(), close, tpFlag);
        ViewUtils.setQuoteValueText(mText15, event.getOpen(), close, tpFlag);
        mText17.setText("--");
        ViewUtils.setQuoteValueText(mText23, event.getLow(), close, tpFlag);
        mText25.setText(StringUtil.getAmountString(event.getAmount()));
        String time;
        if (!event.isMinuteK()) {
            time = StringUtil.getFormattedDateString(event.getTimeStr());
        } else {
            time = StringUtil.getFormattedDateString(event.getTimeStr()) + " " + StringUtil.minuteToTime((int) event.getMinute());
        }
        mText26.setText(time);
        mText27.setText("");
    }
}
