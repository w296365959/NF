package com.sscf.investment.detail.manager;

import android.graphics.drawable.Drawable;
import com.sscf.investment.sdk.utils.BitmapUtils;
import java.util.HashMap;

/**
 * Created by davidwei on 2017/05/03.
 */
public final class PlateBgDrawableManager {
    private final HashMap<Integer, Drawable> mCache;
    private final float mRadius;

    public PlateBgDrawableManager(float radius) {
        mCache = new HashMap<>();
        mRadius = radius;
    }

    public Drawable getDrawable(final int color) {
        Drawable drawable = mCache.get(color);
        if (drawable == null) {
            drawable = BitmapUtils.getRoundDrawable(color, mRadius);
            mCache.put(color, drawable);
        }
        return drawable;
    }

    public static int getBackgroundColor(final float updown) {
        if (updown > 3f) {
            return 0xffe64b4b;
        } else if (updown > 2f) {
            return 0xffff7676;
        } else if (updown > 1f) {
            return 0xffffcaca;
        } else if (updown > 0f) {
            return 0xffffebeb;
        } else if (updown == 0f) {
            return 0xffccd0d6;
        } else if (updown > -1f) {
            return 0xffe4f4e8;
        } else if (updown > -2f) {
            return 0xffbfeecd;
        } else if (updown > -3f) {
            return 0xff50c672;
        } else {
            return 0xff199d3e;
        }
    }

    public static int getTextColor(final float updown) {
        if (updown > 3f) {
            return 0xffffffff;
        } else if (updown > 2f) {
            return 0xffffffff;
        } else if (updown > 1f) {
            return 0xffe64b4b;
        } else if (updown > 0f) {
            return 0xffe64b4b;
        } else if (updown == 0f) {
            return 0xffffffff;
        } else if (updown > -1f) {
            return 0xff199d3e;
        } else if (updown > -2f) {
            return 0xff199d3e;
        } else if (updown > -3f) {
            return 0xffffffff;
        } else {
            return 0xffffffff;
        }
    }
}
