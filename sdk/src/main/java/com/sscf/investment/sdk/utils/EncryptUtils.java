package com.sscf.investment.sdk.utils;

/**
 * Created by liqf on 2016/8/23.
 */
public class EncryptUtils {

    public static byte[] encodeDES(byte[] src, byte[] key)
    {
        return DES.DesEncrypt(key, src, 1);
    }

    public static byte[] decodeDES(byte[] src, byte[] key)
    {
        return DES.DesEncrypt(key, src, 0);
    }
}
