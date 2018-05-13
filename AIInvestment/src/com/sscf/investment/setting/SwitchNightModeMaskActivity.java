package com.sscf.investment.setting;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.interpolator.SineEaseIn;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;

/**
 * Created by liqf on 2016/8/11.
 */
public class SwitchNightModeMaskActivity extends BaseActivity {
    private static final long FADE_IN_DURATION = 800;
    private static final long SWITCH_DURATION = 800;
    private static final long STAY_DURATION = 400;
    private static final long FADE_OUT_DURATION = 500;

    private static final float ABOVE_BG_ALPHA_DAY = 0;
    private static final float ABOVE_BG_ALPHA_NIGHT = 1.0f;
    private static final float BEACON_ALPHA_DAY = 1.0f;
    private static final float BEACON_ALPHA_NIGHT = 0.7f;
    private static final float LIGHT_ALPHA_DAY = 0.2f;
    private static final float LIGHT_ALPHA_NIGHT = 1.0f;
    private static final float BLAST_ALPHA_DAY = 0;
    private static final float BLAST_ALPHA_NIGHT = 1.0f;

    private View mContentView;

    private ImageView aboveBg;
    private ImageView beacon;
    private ImageView light;
    private ImageView blast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager == null) {
            finish();
            return;
        }

        setContentView(R.layout.main_activity_switch_night_mode);

        final boolean isNightTheme = themeManager.isNightTheme();

        mContentView = findViewById(R.id.content);

        aboveBg = (ImageView) findViewById(R.id.above_bg);
        beacon = (ImageView) findViewById(R.id.beacon);
        light = (ImageView) findViewById(R.id.light);
        blast = (ImageView) findViewById(R.id.blast);

        setNightMode(isNightTheme);
        playFadeInAnimation(mContentView);

        mContentView.postDelayed(() -> {
            animateToNightMode(!isNightTheme);
            doSwitchTheme();
        }, FADE_IN_DURATION);

        if (isNightTheme) {
            StatisticsUtil.reportAction(StatisticsConst.SETTING_TURN_OFF_NIGHT_MODE);
        } else {
            StatisticsUtil.reportAction(StatisticsConst.SETTING_TURN_ON_NIGHT_MODE);
        }
    }

    private void doSwitchTheme() {
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager == null) {
            return;
        }
        DengtaApplication.getApplication().setMainActivityRestarting(true);
        themeManager.switchTheme();
    }

    private void animateToNightMode(boolean isNightMode) {
        final LinearInterpolator interpolator = new LinearInterpolator();
        if (isNightMode) {
            aboveBg.animate().alpha(ABOVE_BG_ALPHA_NIGHT).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
            beacon.animate().alpha(BEACON_ALPHA_NIGHT).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
            light.animate().alpha(LIGHT_ALPHA_NIGHT).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
            blast.animate().alpha(BLAST_ALPHA_NIGHT).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
        } else {
            aboveBg.animate().alpha(ABOVE_BG_ALPHA_DAY).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
            beacon.animate().alpha(BEACON_ALPHA_DAY).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
            light.animate().alpha(LIGHT_ALPHA_DAY).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
            blast.animate().alpha(BLAST_ALPHA_DAY).setDuration(SWITCH_DURATION).setInterpolator(interpolator);
        }
        mContentView.postDelayed(() -> playFadeOutAnimation(), SWITCH_DURATION + STAY_DURATION);
    }

    private void setNightMode(boolean isNightMode) {
        if (isNightMode) {
            aboveBg.setAlpha(ABOVE_BG_ALPHA_NIGHT);
            beacon.setAlpha(BEACON_ALPHA_NIGHT);
            light.setAlpha(LIGHT_ALPHA_NIGHT);
            blast.setAlpha(BLAST_ALPHA_NIGHT);
        } else {
            aboveBg.setAlpha(ABOVE_BG_ALPHA_DAY);
            beacon.setAlpha(BEACON_ALPHA_DAY);
            light.setAlpha(LIGHT_ALPHA_DAY);
            blast.setAlpha(BLAST_ALPHA_DAY);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, SwitchNightModeMaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    private void playFadeOutAnimation() {
        ViewPropertyAnimator alpha = mContentView.animate().alpha(0).setDuration(FADE_OUT_DURATION);
        alpha.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void playFadeInAnimation(View view) {
        ValueAnimator animatorExpand = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        animatorExpand.setDuration(FADE_IN_DURATION);
        animatorExpand.setEvaluator(new SineEaseIn(FADE_IN_DURATION));
        animatorExpand.start();
    }
}
