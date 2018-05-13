package com.sscf.investment.widget;

import android.support.v4.view.ViewPager;
import android.view.View;
import com.sscf.investment.sdk.utils.DtLog;

/**
 * Created by liqf on 2015/11/3.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        DtLog.d("liqf", "transformPage: view = " + view.toString() + ", position = " + position);
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position < 0) { // [-1,0)
            // Use the default slide transition when moving to the left page
            view.setAlpha(1 + position);
//            view.setTranslationX(0);
//            view.setScaleX(1);
//            view.setScaleY(1);

        } else if (position <= 1) { // [0,1]
            // Fade the page out.
//            view.setAlpha(1 - position);
            view.setAlpha(1 - position);

            // Counteract the default slide transition
//            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
//            float scaleFactor = MIN_SCALE
//                + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            float scaleFactor = 1.0f;
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
