package com.sscf.investment.sdk.utils;

import android.text.TextUtils;

/**
 * Created by davidwei on 2017/06/29
 */
public final class NumberUtil {

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

    public static double parseDouble(final String s, final double defValue) {
        double value = defValue;
        if (!TextUtils.isEmpty(s)) {
            try {
                value = Double.parseDouble(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
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

    public static String getUpdownString(final float close, final float now) {
        if (close > 0 && now > 0) {
            return getUpdownString((now / close - 1) * 100);
        } else {
            return "--";
        }
    }

    public static String getUpdownString(float change) {
        final StringBuilder sb = new StringBuilder();
        if (change > 0) {
            sb.append('+');
        }
        sb.append(getFormatedFloat(change)).append("%");
        return sb.toString();
    }

    public static String getFormatedFloat(final double value) {
        return String.format("%.2f", value);
    }
}
