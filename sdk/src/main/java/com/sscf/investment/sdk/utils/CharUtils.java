package com.sscf.investment.sdk.utils;

import android.os.Build;

/**
 * Created by davidwei on 2017-09-27.
 */

public final class CharUtils {

    /**
     * 输入的字符是否是汉字
     * @param a char
     * @return boolean
     */
    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    public static int getCharCount(String trimmedText) {
        int count = 0;
        for (int i = 0; i < trimmedText.length(); i++) {
            char c = trimmedText.charAt(i);
            if (!isChinese(c)) {
                count++;
            } else {
                count += 2;
            }
        }
        return count;
    }

    /**
     *  根据UnicodeBlock方法判断中文标点符号
      */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (ub == Character.UnicodeBlock.VERTICAL_FORMS) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 判断字符是否是字母或者数字
     * @param c
     * @return
     */
    public static boolean isAlphabetOrDigit(char c) {
        if ((c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a')
                || (c >= '0' && c <= '9')) {
            return true;
        }
        return false;
    }
}
