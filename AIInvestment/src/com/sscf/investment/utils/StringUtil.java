package com.sscf.investment.utils;

import android.content.res.Resources;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TextDrawer;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.widget.CustomTypefaceTextAppearanceSpan;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import BEC.E_FR_UNIT;
import BEC.E_SEC_ATTR;
import BEC.E_SEC_STATUS;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by liqf on 2015/8/11.
 */
public final class StringUtil {

    public static final int YI = 100000000;
    public static final int WAN = 10000;
    public static final String DASH = "-";
    private static final String TAG = StringUtil.class.getSimpleName();

    private static final int mColorRed;
    private static final int mColorGreen;

    static {
        Resources resources = DengtaApplication.getApplication().getResources();
        mColorRed = resources.getColor(R.color.stock_red_color);
        mColorGreen = resources.getColor(R.color.stock_green_color);
    }

    public static int getColorRed() {
        return mColorRed;
    }

    public static int getColorGreen() {
        return mColorGreen;
    }

    public static int getColorBase() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        int suspensionTextColor;
        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        if (isDefaultTheme) {
            suspensionTextColor = dengtaApplication.getResources().getColor(R.color.gray_60);
        } else {
            suspensionTextColor = dengtaApplication.getResources().getColor(R.color.default_text_color_60_night);
        }
        return suspensionTextColor;
    }

    public static String getPercentString(final float value) {
        float percent = value * 100.0f;
        return String.format("%.2f", percent) + "%";
    }

    /**
     * 取得浮点数保留小数点后两位的字符串
     */
    public static String getFormatedFloat(final double value) {
        return String.format("%.2f", value);
    }

    /**
     * 取得浮点数四舍五入保留小数点后两位的字符串
     */
    public static String getRoundedFloat(final float value) {
        return getFormatedFloat(Math.round(value * 100) * 0.01f);
    }

    public static String getFormattedDouble(final double value) {
        return String.format("%.2f", value);
    }

    public static String getFormattedFloat(final float value, int precision) {
        switch (precision) {
            case 0:
                return String.format("%.0f", value);
            case 1:
                return String.format("%.1f", value);
            case 2:
                return String.format("%.2f", value);
            case 3:
                return String.format("%.3f", value);
            case 4:
                return String.format("%.4f", value);
            case 5:
                return String.format("%.5f", value);
            case 6:
                return String.format("%.6f", value);
            default:
                return String.format("%.2f", value);
        }
    }

    /**
     * 一个小数点
     * @param value
     * @return
     */
    public static String getFormatedFloatOneDote(final double value) {
        return String.format("%.1f", value);
    }

    public static String getUnitString(final int unit) {
        switch (unit) {
            case E_FR_UNIT.E_FRU_YUAN:
                return "(元)";
            case E_FR_UNIT.E_FRU_WANYUAN:
                return "(万元)";
            case E_FR_UNIT.E_FRU_MILION:
                return "(百万元)";
            case E_FR_UNIT.E_FRU_YIYUAN:
                return "(亿元)";
            default:
                return "";
        }
    }

    public static String getActionBarSubTitleAlternative(final SecQuote quote) {
        int secStatus = quote.getESecStatus();
        int tpFlag = quote.getITpFlag(); //小数精度
        float nowPrice = quote.getFNow();
        float yesterdayClose = quote.getFClose();

        if (secStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            return StringUtil.getFormattedFloat(yesterdayClose, tpFlag) + "  0.00" + "  0.00%";
        }

        if (nowPrice == 0) {
            String nullStr = DengtaApplication.getApplication().getResources().getString(R.string.value_null);
            return nullStr + "  " + nullStr + "  " + nullStr;
        }

        float changeValue = nowPrice - yesterdayClose;
        String changeStr = changeValue > 0 ? "+" : "";
        changeStr += StringUtil.getFormattedFloat(changeValue, tpFlag);

        float change = changeValue / yesterdayClose;
        return StringUtil.getFormattedFloat(nowPrice, tpFlag) + "  " + changeStr + "  " + StringUtil.getChangePercentString(change);
    }

    /**
     * 计算文本字符的长度
     * 英文数字占用1个字节，中文占两个字节
     * @return
     */
    public static int getTextCharactorLength(final String text) {
        try {
            return text.getBytes("GBK").length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getYiValueString(final float value) {
        int v = (int) (value / YI);
        return String.valueOf(v) + "亿";
    }

    public static String getYiFloatString(final float value) {
        return getFormatedFloat(value / YI);
    }

    public static String getWanFloatString(final float value) {
        return getFormatedFloat(value / WAN);
    }

    /**
     * 融资融券余额
     * @param value
     * @return
     */
    public static String getMarginTradingBalance(final long value) {
        double v;
        if (value >= 0) {
            v = value * 1d / YI;
            return getFormatedFloat(v);
        } else {
            return "0";
        }
    }

    public static String getAmountString(final float value, int precision) {
        float v;
        if (value >= YI) {
            v = value / YI;
            return getFormattedFloat(v, precision) + "亿";
        } else if (value >= WAN) {
            v = value / WAN;
            return getFormattedFloat(v, precision) + "万";
        } else {
            v = value;
            return getFormattedFloat(v, precision);
        }
    }

    public static String getAmountString(final double value) {
        double v;
        double absValue = Math.abs(value);
        if (absValue >= YI) {
            v = value * 1f / YI;
            return getFormattedDouble(v) + "亿";
        } else if (absValue >= WAN) {
            v = value* 1f / WAN;
            return getFormattedDouble(v) + "万";
        } else {
            return String.valueOf(value);
        }
    }

    public static String getAmountString(final int value, int precision) {
        float v;
        if (value >= YI) {
            v = value * 1f / YI;
            return getFormattedFloat(v, precision) + "亿";
        } else if (value >= WAN) {
            v = value* 1f / WAN;
            return getFormattedFloat(v, precision) + "万";
        } else {
            return String.valueOf(value);
        }
    }

    public static String getAmountChangeString(final float value) {
        float v;
        if (value >= YI) {
            v = value / YI;
            return getFormatedFloat(v) + "亿";
        } else if (value >= WAN) {
            v = value / WAN;
            return getFormatedFloat(v) + "万";
        } else {
            v = value;
            return getFormatedFloat(v);
        }
    }

    public static String getAmountString(final float value) {
        if (value <= 0) {
            return "--";
        }
        float v;
        if (value >= YI) {
            v = value / YI;
            return getFormatedFloat(v) + "亿";
        } else if (value >= WAN) {
            v = value / WAN;
            return getFormatedFloat(v) + "万";
        } else {
            v = value;
            return getFormatedFloat(v);
        }
    }

    public static String getAmountStringYi(final float value) {
        float v;
        v = value / YI;
        return getFormatedFloat(v);
    }

    public static String getAmountStringWan(final float value) {
        float v;
        v = value / WAN;
        return getFormatedFloat(v);
    }

    public static String getVolumeString(float value, boolean isHkOrUsa, boolean isHandful) {
        return getVolumeString(value, isHkOrUsa, isHandful, false);
    }

    public static String getVolumeString(float value, boolean isHkOrUsa, boolean isHandful, boolean withUnit) {
        if (value <= 0) {
            return "--";
        }

        float v;
        String result;

        int precision = 2;
        if (!isHandful && !isHkOrUsa) {
            value /= 100;
        }

        if (value >= YI) {
            v = value / YI;
            result = getFormattedFloat(v, precision) + "亿";
        } else if (value >= WAN) {
            v = value / WAN;
            result = getFormattedFloat(v, precision) + "万";
        } else {
            result = String.valueOf((int) value);
        }

        if (withUnit) {
            if (!isHkOrUsa) {
                result += "手";
            } else {
                result += "股";
            }
        }

        return result;
    }


    public static String getHandicapString(float value, boolean isHkOrUsa, boolean isHandful, boolean withUnit, boolean suspended) {
        if (suspended) {
            return "--";
        } else {
            return getVolumeString(value, isHkOrUsa, isHandful, withUnit);
        }
    }

    public static String getPeString(float syl) {
        return syl > 0 ? StringUtil.getFormatedFloat(syl) : "--";
    }

    public static CharSequence getChangePercentString(float change) {
        return getChangePercentStringWithSign(change, true);
    }

    public static CharSequence getChangePercentStringWithSign(float change, boolean withSign) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        if (withSign) {
            String changePercentStr = change > 0 ? "+" : "";
            sb.append(changePercentStr);
        }
        sb.append(StringUtil.getFormatedFloat(change * 100)).append("%");

        int color;
        if (change > 0) {
            color = mColorRed;
        } else if (change < 0) {
            color = mColorGreen;
        } else {
            color = getColorBase();
        }

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public static String getUpdownString(float change) {
        final StringBuilder sb = new StringBuilder();
        if (change > 0) {
            sb.append('+');
        }
        sb.append(StringUtil.getFormatedFloat(change)).append("%");
        return sb.toString();
    }

    public static String getUpdownString(SecQuote quote) {
        if (quote == null) {
            return "--";
        }
        return getUpdownString(quote.fClose, quote.fNow);
    }

    public static String getUpdownString(SecSimpleQuote quote) {
        return getUpdownString(quote.fClose, quote.fNow);
    }

    public static String getUpdownString(final float close, final float now) {
        if (close > 0 && now > 0) {
            return getUpdownString((now / close - 1) * 100);
        } else {
            return "--";
        }
    }

    public static String getFormattedDateString(String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try {
            if (date.length() == 8) { //对于日K、周K、月K，有完整年月日的情况
                sb.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8));
            } else if (date.length() <= 4) { //对于分钟K线，只有月和日的情况
                if (date.length() == 3) { //如912，需要补全为0912
                    date = "0" + date;
                }
                sb.append(date.substring(0, 2)).append("-").append(date.substring(2, 4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String minuteToTime(int minute) {
        StringBuffer result = new StringBuffer();
        int m = minute;
        int h = (m) / 60;
        int n = (m) % 60;
        if (h < 10)
            result.append("0");
        result.append(h);
        result.append(":");
        if (n < 10) {
            result.append("0");
        }
        result.append(n);
        return result.toString();
    }

    public static int timeMinuteToEnlargeMinute(int minute) {
        int h = (minute) / 60;
        int m = (minute) % 60;
        return h * 10000 + m * 100;
    }

    public static String enlargeMinuteToHmStr(int time) {
        StringBuffer result = new StringBuffer();
        time = time / 100;
        int h = (time) / 100;
        int m = (time) % 100;
        if (h < 10) {
            result.append("0");
        }
        result.append(h);
        result.append(":");
        if (m < 10) {
            result.append("0");
        }
        result.append(m);
        return result.toString();
    }

    public static int enlargeMinuteToHms(int time) {
        int h = time / 10000;
        time = time % 10000;
        int m = time / 100;
        int s = time % 100;
        return h * 60 * 60 + m * 60 + s;
    }

    public static String minuteToTimeMinusOne(int minute) {
        StringBuffer result = new StringBuffer();
        int m = minute - 1;
        int h = (m) / 60;
        int n = (m) % 60;
        if (h < 10)
            result.append("0");
        result.append(h);
        result.append(":");
        if (n < 10) {
            result.append("0");
        }
        result.append(n);
        return result.toString();
    }

    public static String getStringWithMaxLength(String text, int maxLen) {
        if (TextUtils.isEmpty(text) || maxLen <= 0) {
            return "";
        }

        int charCount = text.length();
        if (charCount > maxLen) {
            return text.substring(0, maxLen - 3) + "...";
        } else {
            return text;
        }
    }

    private static StringBuilder mFormatSb;
    private static SimpleDateFormat mDateFormat;
    private static Date mDate;

    static {
        mDateFormat = new SimpleDateFormat(/*"yyyy-MM-dd HH:mm:ss"*/);
        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        mDate = new Date();
        mFormatSb = new StringBuilder();
    }

    public static int time2Minutes(String date) {
        if(!TextUtils.isEmpty(date) && date.indexOf(":") > 0) {
            String[] data = date.split(":");
            int h = Integer.parseInt(data[0]);
            int m = Integer.parseInt(data[1]);
            return h * 60 + m;
        }
        return 0;
    }

    public static int timeStamp2DateYmd(long timestamp){
        mFormatSb.delete(0, mFormatSb.length());
        mDate.setTime(timestamp);

        mFormatSb.append("yyyyMMdd");

        mDateFormat.applyPattern(mFormatSb.toString());

        String date = mDateFormat.format(mDate);
        int time = Integer.parseInt(date);
        return time;
    }

    public static String timeStamp2Ymd(int timestamp) {
        mFormatSb.delete(0, mFormatSb.length());
        mDate.setTime(timestamp * 1000L);
        mFormatSb.append("yyyy-MM-dd");

        mDateFormat.applyPattern(mFormatSb.toString());

        String date = mDateFormat.format(mDate);
        return date;
    }

    public static String timeStamp2DetailedDate(long timestamp){
        mFormatSb.delete(0, mFormatSb.length());
        mDate.setTime(timestamp * 1000L);
        mFormatSb.append("MM-dd HH:mm:ss");

        mDateFormat.applyPattern(mFormatSb.toString());

        String date = mDateFormat.format(mDate);
        return date;
    }

    public static String timeStamp2TimeInDay(long timestamp){
        mFormatSb.delete(0, mFormatSb.length());
        mDate.setTime(timestamp * 1000L);
        mFormatSb.append("HH:mm:ss");

        mDateFormat.applyPattern(mFormatSb.toString());

        String date = mDateFormat.format(mDate);
        return date;
    }

    public static String timeStamp2HourMinute(long timestamp){
        mFormatSb.delete(0, mFormatSb.length());
        mDate.setTime(timestamp * 1000L);
        mFormatSb.append("HH:mm");

        mDateFormat.applyPattern(mFormatSb.toString());

        String date = mDateFormat.format(mDate);
        return date;
    }

    public static String getSecActionBarQuoteTime(long timestamp) {
        if (timestamp == 0) {
            return "";
        }
        return timeStamp2DetailedDate(timestamp);
    }

    public static String getAttrStr(int attr) {
        switch (attr) {
            case E_SEC_ATTR.E_SEC_ATTR_SHHK:
                return "沪港通";
            case E_SEC_ATTR.E_SEC_ATTR_MARGIN:
                return "融";
            case E_SEC_ATTR.E_SEC_ATTR_PLATE_FAUCET:
                return "龙头";
            case E_SEC_ATTR.E_SEC_ATTR_SUB_NEW:
                return "次新";
            case E_SEC_ATTR.E_SEC_ATTR_CONC_STRONG:
                return "强势";
            case E_SEC_ATTR.E_SEC_ATTR_SZHK:
                return "深港通";
            default:
                return null;
        }
    }

    private static TextAppearanceSpan upStyle;

    public static TextAppearanceSpan getUpStyle() {
        if (upStyle == null) {
            upStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                    R.style.up_text_style, TextDrawer.getTypeface());
        }
        return upStyle;
    }

    private static TextAppearanceSpan downStyle;

    public static TextAppearanceSpan getDownStyle() {
        if (downStyle == null) {
            downStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                    R.style.down_text_style, TextDrawer.getTypeface());
        }
        return downStyle;
    }

    private static TextAppearanceSpan suspensionStyle;
    private static TextAppearanceSpan suspensionStyleNight;

    public static TextAppearanceSpan getSuspensionStyle() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        TextAppearanceSpan span = null;
        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        if (isDefaultTheme) {
            if (suspensionStyle == null) {
                suspensionStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                        R.style.suspension_text_style, TextDrawer.getTypeface());
            }
            span = suspensionStyle;
        } else {
            if (suspensionStyleNight == null) {
                suspensionStyleNight = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                        R.style.suspension_text_style_night, TextDrawer.getTypeface());
            }
            span = suspensionStyleNight;
        }

        return span;
    }

    /**
     * 格式化资金流
     * @param value
     * @return
     */
    public static CharSequence getCapitalFlowSpannable(float value) {
        TextAppearanceSpan span = null;
        final SpannableStringBuilder text = new SpannableStringBuilder();
        if (value > 0) {
            text.append('+');
            span = getUpStyle();
        } else if (value < 0) {
            text.append('-');
            value = -value;
            span = getDownStyle();
        } else {
            span = getSuspensionStyle();
        }

        String unit = "";
        if (value >= YI) {
            value = value / YI;
            unit = "亿";
        } else if (value >= WAN) {
            value = value / WAN;
            unit = "万";
        }
        text.append(getFormattedFloat(value, 1)).append(unit);
        text.setSpan(span, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return text;
    }

    /**
     * 获取涨跌额的样式
     * @return
     */
    public static CharSequence getPriceSpannable(float price) {
        TextAppearanceSpan span = null;
        final SpannableStringBuilder text = new SpannableStringBuilder();
        if (price > 0) {
            text.append('+');
            span = getUpStyle();
        } else if (price < 0) {
            span = getDownStyle();
        } else {
            span = getSuspensionStyle();
        }
        text.append(getFormatedFloat(price));
        text.setSpan(span, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return text;
    }

    /**
     * 获取涨跌幅样式
     * @return
     */
    public static CharSequence getUpdownPercentSpannable(float updownPercent) {
        TextAppearanceSpan span = null;
        final SpannableStringBuilder text = new SpannableStringBuilder();
        if (updownPercent > 0) {
            text.append('+');
            span = getUpStyle();
        } else if (updownPercent < 0) {
            span = getDownStyle();
        } else {
            span = getSuspensionStyle();
        }
        text.append(getFormatedFloat(updownPercent)).append('%');
        text.setSpan(span, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return text;
    }

    /**
     * 计算涨跌幅
     * @return
     */
    public static CharSequence getUpDownStringSpannable(final SecSimpleQuote quote) {
        if (quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            final String stop = DengtaApplication.getApplication().getResources().getString(R.string.stock_stop);
            final SpannableString text = new SpannableString(stop);
            text.setSpan(getSuspensionStyle(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return text;
        } else {
            return getUpDownStringSpannable(quote.fNow, quote.fClose);
        }
    }

    /**
     * 计算涨跌幅
     * @return
     */
    public static float getUpDownValue(final SecSimpleQuote quote) {
        final float now = quote.fNow;
        final float close = quote.fClose;
        if (close <= 0 || now <= 0) {
            return 0f;
        } else {
            return  (now / close - 1) * 100;
        }
    }

    /**
     * 计算涨跌幅
     * @return
     */
    public static CharSequence getUpDownStringSpannable(final SecQuote quote) {
        if (quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            final String stop = DengtaApplication.getApplication().getResources().getString(R.string.stock_stop);
            final SpannableString text = new SpannableString(stop);
            text.setSpan(getSuspensionStyle(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return text;
        } else {
            return getUpDownStringSpannable(quote.fNow, quote.fClose);
        }
    }

    /**
     * 计算涨跌幅
     * @param now
     * @param close
     * @return
     */
    public static CharSequence getUpDownStringSpannable(float now, float close) {
        TextAppearanceSpan span = null;
        final SpannableStringBuilder text = new SpannableStringBuilder();
        if (close <= 0 || now <= 0) {
            text.append("--");
            span = getSuspensionStyle();
        } else {
            final float updown = (now / close - 1) * 100;
            if (updown > 0) {
                text.append('+');
                span = getUpStyle();
            } else if (updown < 0) {
                span= getDownStyle();
            } else {
                span = getSuspensionStyle();
            }
            text.append(getFormatedFloat(updown)).append('%');
        }

        text.setSpan(span, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return text;
    }

    /**
     * 计算涨跌幅
     * @param now
     * @param close
     * @return
     */
    public static CharSequence getUpDownStringSpannable(float now, float close, int precision) {
        TextAppearanceSpan span = null;
        final SpannableStringBuilder text = new SpannableStringBuilder();
        if (close <= 0 || now <= 0) {
            text.append("--");
            span = getSuspensionStyle();
        } else {
            final float updown = (now / close - 1) * 100;
            if (updown > 0) {
                text.append('+');
                span = getUpStyle();
            } else if (updown < 0) {
                span= getDownStyle();
            } else {
                span = getSuspensionStyle();
            }
            text.append(getFormattedFloat(updown, precision)).append('%');
        }

        text.setSpan(span, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return text;
    }

    private static TextAppearanceSpan ahPriceUpStyle;

    public static TextAppearanceSpan getAHPriceUpStyle() {
        if (ahPriceUpStyle == null) {
            ahPriceUpStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                    R.style.ah_price_up_text_style, TextDrawer.getTypeface());
        }
        return ahPriceUpStyle;
    }

    private static TextAppearanceSpan ahPriceDownStyle;

    public static TextAppearanceSpan getAHPriceDownStyle() {
        if (ahPriceDownStyle == null) {
            ahPriceDownStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                    R.style.ah_price_down_text_style, TextDrawer.getTypeface());
        }
        return ahPriceDownStyle;
    }

    private static TextAppearanceSpan ahPriceSuspensionStyle;
    private static TextAppearanceSpan ahPriceSuspensionNightStyle;

    public static TextAppearanceSpan getAHPriceSuspensionStyle() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        TextAppearanceSpan span = null;
        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        if (isDefaultTheme) {
            if (ahPriceSuspensionStyle == null) {
                ahPriceSuspensionStyle = new CustomTypefaceTextAppearanceSpan(dengtaApplication,
                        R.style.ah_price_suspension_text_style, TextDrawer.getTypeface());
            }
            span = ahPriceSuspensionStyle;
        } else {
            if (ahPriceSuspensionNightStyle == null) {
                ahPriceSuspensionNightStyle = new CustomTypefaceTextAppearanceSpan(dengtaApplication,
                        R.style.ah_price_suspension_text_style_night, TextDrawer.getTypeface());
            }
            span = ahPriceSuspensionNightStyle;
        }
        return span;
    }

    private static TextAppearanceSpan ahUpdownUpStyle;

    public static TextAppearanceSpan getAHUpdownUpStyle() {
        if (ahUpdownUpStyle == null) {
            ahUpdownUpStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                    R.style.ah_updown_up_text_style, TextDrawer.getTypeface());
        }
        return ahUpdownUpStyle;
    }

    private static TextAppearanceSpan ahUpdownDownStyle;

    public static TextAppearanceSpan getAHUpdownDownStyle() {
        if (ahUpdownDownStyle == null) {
            ahUpdownDownStyle = new CustomTypefaceTextAppearanceSpan(DengtaApplication.getApplication(),
                    R.style.ah_updown_down_text_style, TextDrawer.getTypeface());
        }
        return ahUpdownDownStyle;
    }

    private static TextAppearanceSpan ahUpdownSuspensionStyle;
    private static TextAppearanceSpan ahUpdownSuspensionNightStyle;

    public static TextAppearanceSpan getAHUpdownSuspensionStyle() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        TextAppearanceSpan span = null;
        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        if (isDefaultTheme) {
            if (ahUpdownSuspensionStyle == null) {
                ahUpdownSuspensionStyle = new CustomTypefaceTextAppearanceSpan(dengtaApplication,
                        R.style.ah_updown_suspension_text_style, TextDrawer.getTypeface());
            }
            span = ahUpdownSuspensionStyle;
        } else {
            if (ahUpdownSuspensionNightStyle == null) {
                ahUpdownSuspensionNightStyle = new CustomTypefaceTextAppearanceSpan(dengtaApplication,
                        R.style.ah_updown_suspension_text_style_night, TextDrawer.getTypeface());
            }
            span = ahUpdownSuspensionNightStyle;
        }
        return span;
    }

    /**
     * 获得ah股溢价信息
     * @return
     */
    public static CharSequence getAHInfoStringSpannable(float now, float updown) {
        TextAppearanceSpan priceSpan = getAHPriceSuspensionStyle();
        TextAppearanceSpan updownSpan = getAHUpdownSuspensionStyle();
        final SpannableStringBuilder text = new SpannableStringBuilder();

        if (now > 0) {
            text.append(getFormatedFloat(now));
            if (updown > 0) {
                priceSpan = getAHPriceUpStyle();
            } else if (updown < 0) {
                priceSpan = getAHPriceDownStyle();
            }
        } else {
            text.append('-').append('-');
        }

        final int nowLength = text.length();
        text.append('\n');

        if (now > 0) {
            if (updown > 0) {
                updownSpan = getAHUpdownUpStyle();
                text.append('+');
            } else if (updown < 0) {
                updownSpan = getAHUpdownDownStyle();
            }
            text.append(getFormatedFloat(updown * 100)).append('%');
        } else {
            text.append('-').append('-');
        }

        text.setSpan(priceSpan, 0, nowLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        text.setSpan(updownSpan, nowLength, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return text;
    }

    /**
     * 获得ah股溢价值
     * @param hNow 港股当前价
     * @param aNow A股当前价
     * @param exchangeRate 汇率
     * @return
     */
    public static CharSequence getAHPremiumStringSpannable(float hNow, float aNow, float exchangeRate) {
        if (hNow > 0 && aNow > 0) {
            hNow = hNow * exchangeRate;
            float premium = (1 - aNow / hNow) * 100;
            return getUpdownPercentSpannable(premium);
        } else {
            final SpannableStringBuilder text = new SpannableStringBuilder("--");
            text.setSpan(getSuspensionStyle(), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return text;
        }
    }

    public static String getYmdByLong(final long ymd) {
        String ymdStr = String.valueOf(ymd);
        StringBuilder sb = new StringBuilder();
        sb.append(ymdStr.substring(0, 4)).append(DASH).append(ymdStr.substring(4, 6)).append(DASH)
            .append(ymdStr.substring(6, 8));
        return sb.toString();
    }

    public static String sizeToString(final long size) {
        final int KB = 1024;
        final int MB = 1024 * KB;
        final int GB = 1024 * MB;
        double value = 0D;
        String unit = "";
        if (size >= GB) {
            value = 1D * size / GB;
            unit = "GB";
        } else if (size >= MB) {
            value = 1D * size / MB;
            unit = "MB";
        } else if (size >= KB) {
            value = 1D * size / KB;
            unit = "KB";
        } else {
            value = size;
            unit = "B";
        }
        return getFormatedFloat(value) + unit;
    }

    public static String sizeToStringM(final long size) {
        return getFormatedFloat(1D * size / (1024 * 1024)) + "MB";
    }

    // 用来将字节转换成 16 进制表示的字符
    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 将字节数组转换成一个16进制字符串
     *
     * @param bytes
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        int j = bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 对String进行MD5加密
     * @param string
     * @return
     */
    public static String getMD5(String string) {
        String s = null;
        try {
            byte[] source = string.getBytes();
            java.security.MessageDigest md = java.security.MessageDigest
                .getInstance("MD5");
            md.update(source);
            return toHexString(md.digest());
        } catch (Exception e) {
            DtLog.e(TAG, e.getMessage());
        }
        return s;
    }

    /**
     * 对byte[]进行MD5加密
     * @return
     */
    public static byte[] getMD5(byte[] src) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                .getInstance("MD5");
            md.update(src);
            return md.digest();
        } catch (Exception e) {
            DtLog.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 成交额
     */
    public static String getAmountString(final SecQuote quote) {
        final float amount = quote.fAmout;
        if (amount > 0) {
            return StringUtil.getAmountString(amount);
        } else {
            return "--";
        }
    }

    /**
     * 量比
     */
    public static String getVolumeRatioString(final SecQuote quote) {
        final float volumeRatio = quote.fVolumeRatio;
        if (volumeRatio > 0) {
            return StringUtil.getFormattedFloat(volumeRatio, 2);
        } else {
            return "--";
        }
    }

    /**
     * 换手率
     */
    public static String getTurnOverRateString(final float value) {
        if (value > 0) {
            return getPercentString(value);
        } else {
            return "--";
        }
    }

    /**
     * 获得K线里历史的换手率
     * @param volume
     * @param quote
     * @return
     */
    public static String getKLineTurnOverRateString(final long volume, final SecQuote quote) {
        final float marketValue = quote.fCirculationmarketvalue;
        final float close = quote.fClose;
        if (volume > 0 && marketValue > 0 && close > 0) {
            return StringUtil.getPercentString(volume * close / marketValue);
        } else {
            return "--";
        }
    }

    /**
     * 振幅
     */
    public static String getAmplitudeString(final SecQuote quote) {
        final float yClose = quote.fClose;
        if (yClose != 0) {
            return  StringUtil.getPercentString((quote.fMax - quote.fMin) / yClose);
        } else {
            return "--";
        }
    }

    /**
     * 委比
     */
    public static String getAppointString(final SecQuote quote) {
        if (quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED || quote.fNow == 0) {
            return  "0.00%";
        } else {
            ArrayList<Long> vBuyv = quote.vBuyv;
            ArrayList<Long> vSellv = quote.vSellv;
            long totalBuy = 0, totalSell = 0;
            for (Long buy : vBuyv) {
                totalBuy += buy;
            }
            for (Long sell : vSellv) {
                totalSell += sell;
            }
            long total = totalBuy + totalSell;

            if (total != 0) {
                return StringUtil.getPercentString((float) (totalBuy - totalSell) / total);
            } else {
                return "--";
            }
        }
    }

    /**
     * 流通股
     */
    public static String getTradableSharesString(final SecQuote quote) {
        final float shares = quote.fLtg * 10000;
        return shares > 0 ? getAmountString(shares, 2) : "--";
    }

    /**
     * 总股本
     */
    public static String getTotalSharesString(final SecQuote quote) {
        final float now = quote.fNow == 0 ? quote.fClose : quote.fNow;
        final float totalValue = quote.fTotalmarketvalue;
        if (now == 0 || totalValue == 0) {
            return "--";
        } else {
            return getAmountString(totalValue / now, 2);
        }
    }

    public static String getActionBarTitle(String secName, String dtSecCodeForShow) {
        return TextUtils.isEmpty(secName) ? "" : secName.trim() + "(" + dtSecCodeForShow + ")";
    }

    public static int parseInt(final String s, final int defValue) {
        int value = defValue;
        if (!TextUtils.isEmpty(s) && TextUtils.isDigitsOnly(s)) {
            value = Integer.parseInt(s);
        }
        return value;
    }

    public static long parseLong(final String s, final int defValue) {
        long value = defValue;
        if (!TextUtils.isEmpty(s) && TextUtils.isDigitsOnly(s)) {
            value = Long.parseLong(s);
        }
        return value;
    }

    /**
     * 计算文字宽度
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}
