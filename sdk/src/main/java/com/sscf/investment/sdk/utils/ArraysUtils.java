package com.sscf.investment.sdk.utils;


import java.util.ArrayList;

/**
 * Created by davidwei on 2016/1/13.
 */
public final class ArraysUtils {

    public static void arrayToList(String[] array, ArrayList<String> list) {
        for (String unicode : array) {
            list.add(unicode);
        }
    }
}
