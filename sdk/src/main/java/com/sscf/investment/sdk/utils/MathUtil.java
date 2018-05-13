package com.sscf.investment.sdk.utils;

/**
 * Created by liqf on 2016/3/4.
 */
public class MathUtil {
    public static float getRoundedFloat(final float value, final int precision) {
        double pow = Math.pow(10, precision);
        return (float) (Math.round(pow * value) / pow);
    }

    public static float getMaxVal(float maxval, Float... args) {
        for (Float item : args) {
            if (item > maxval)
                maxval = item;
        }
        return maxval;
    }

    public static float getMaxVal(int maxval, Integer... args) {
        for (Integer item : args) {
            if (item > maxval)
                maxval = item;
        }
        return maxval;
    }

    public static float getMinVal(float minval, Float... args) {
        for (Float item : args) {
            if (/*item != 0 && */item < minval)
                minval = item;
        }
        return minval;
    }

    public static float getMinVal(int minval, int... args) {
        for (int item : args) {
            if (/*item != 0 && */item < minval)
                minval = item;
        }
        return minval;
    }
}
