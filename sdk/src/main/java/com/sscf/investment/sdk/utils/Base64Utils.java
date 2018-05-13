package com.sscf.investment.sdk.utils;

import android.util.Base64;

/**
 * davidwei
 * Base64编解码
 */
public final class Base64Utils {

    public static String encode(final byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public static String encodeOnHttp(final byte[] data) {
        String encodeString = encode(data);
        encodeString = encodeString.replaceAll("=", ".");
        encodeString = encodeString.replaceAll("\\+", "!");
        encodeString = encodeString.replaceAll("/", "-");
        return encodeString;
    }

    public static byte[] decode(final String src) {
        return Base64.decode(src, Base64.DEFAULT);
    }
}
