package com.sscf.investment.utils;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.widget.SettingTwoTabSelectorView;

import BEC.E_SEC_STATUS;
import BEC.SecQuote;

/**
 * Created by liqf on 2016/3/1.
 */
public final class ViewUtils {

    private static int titleColor;
    private static int contentColor;
    private static int datetimeColor;

    private static float titleSize;
    private static float contentSize;
    private static float datetimeSize;

    public static void setNotificationStyle(final Context context, final RemoteViews remoteViews, final int titleId, final int contentId, final int datetimeId) {
        getNotificationTextStyle(context);

        if (titleId != 0) {
            if (titleColor != 0) {
                remoteViews.setTextColor(titleId, titleColor);
            }
            if (titleSize != 0) {
                remoteViews.setTextViewTextSize(titleId, TypedValue.COMPLEX_UNIT_PX, titleSize);
            }
        }

        if (contentId != 0) {
            if (contentColor != 0) {
                remoteViews.setTextColor(contentId, contentColor);
            }
            if (contentSize != 0) {
                remoteViews.setTextViewTextSize(contentId, TypedValue.COMPLEX_UNIT_PX, contentSize);
            }
        }

        if (datetimeId != 0) {
            if (datetimeColor != 0) {
                remoteViews.setTextColor(datetimeId, datetimeColor);
            }
            if (datetimeSize != 0) {
                remoteViews.setTextViewTextSize(datetimeId, TypedValue.COMPLEX_UNIT_PX, datetimeSize);
            }
        }
    }

    private static void getNotificationTextStyle(final Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("SearchTitleColor").setContentText("SearchContentColor");
        final Notification notification = builder.build();
        try {
            final ViewGroup viewGroup = (ViewGroup) notification.contentView.apply(context, null);
            getDefaultTextStyle(viewGroup);
        } catch (Exception e) {
            e.printStackTrace();
            getDefaultTextStyleByAndroidStyle(context);
        }
    }

    public static void getDefaultTextStyleByAndroidStyle(final Context context) {
        try {
            final TextView textView = new TextView(context);
            textView.setTextAppearance(context, Resources.getSystem().getIdentifier("TextAppearance.StatusBar.EventContent.Title", "style", "android"));
            titleColor = textView.getTextColors().getDefaultColor();
            titleSize = textView.getTextSize();

            textView.setTextAppearance(context, Resources.getSystem().getIdentifier("TextAppearance.StatusBar.EventContent.Info", "style", "android"));
            contentColor = textView.getTextColors().getDefaultColor();
            contentSize = textView.getTextSize();

            textView.setTextAppearance(context, Resources.getSystem().getIdentifier("TextAppearance.StatusBar.EventContent.Time", "style", "android"));
            datetimeColor = textView.getTextColors().getDefaultColor();
            datetimeSize = textView.getTextSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getDefaultTextStyle(final ViewGroup parent) {
        final int childCount = parent.getChildCount();
        TextView textView = null;
        View view = null;
        String text = null;
        for (int i = 0; i < childCount; i++) {
            view = parent.getChildAt(i);
            if (view != null) {
                if (view instanceof TextView) {
                    textView = (TextView) view;
                    text = textView.getText().toString();
                    if ("SearchTitleColor".equals(text)) {
                        titleColor = textView.getTextColors().getDefaultColor();
                        titleSize = textView.getTextSize();
                    } else if ("SearchContentColor".equals(text)) {
                        contentColor = textView.getTextColors().getDefaultColor();
                        contentSize = textView.getTextSize();
                    } else if ("DateTimeView".equals(view.getClass().getSimpleName())) {
                        datetimeColor = textView.getTextColors().getDefaultColor();
                        datetimeSize = textView.getTextSize();
                    }
                } else if (view instanceof ViewGroup) {
                    getDefaultTextStyle((ViewGroup) view);
                }
            }
        }
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 还原View的触摸和点击响应范围,最小不小于View自身范围
     *
     * @param view
     */
    public static void restoreViewTouchDelegate(final View view) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                bounds.setEmpty();
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    public static View initSettingItem(final Activity activity, final int layoutId, final int stringId, View.OnClickListener l) {
        final View parent = activity.findViewById(layoutId);
        parent.setOnClickListener(l);
        final TextView textView = (TextView) parent.findViewById(R.id.settingItemTitle);
        textView.setText(stringId);
        return parent;
    }

    public static void initSettingItem(final View root, final int layoutId, final int stringId, final int drawableId, View.OnClickListener l) {
        final View parent = root.findViewById(layoutId);
        parent.setOnClickListener(l);
        final TextView textView = (TextView) parent.findViewById(R.id.settingItemTitle);
        textView.setText(stringId);
        textView.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
    }

    public static View initSettingRightTextItem(final View parentView, final int layoutId, final int titleId,
                                                final String[] titles, int currentIndex,
                                                SettingTwoTabSelectorView.OnTabSelectedListener l) {
        final View parent = parentView.findViewById(layoutId);
        ((TextView)parent.findViewById(R.id.settingItemTitle)).setText(titleId);
        SettingTwoTabSelectorView view = (SettingTwoTabSelectorView) parent.findViewById(R.id.settingItemSettingTwoTabSelectorView);
        view.setTabTitles(titles[0], titles[1]);
        view.switchToIndex(currentIndex);
        view.setOnTabSelectedListener(l);
        return parent;
    }

    public static void setQuoteView(final TextView valueView, final TextView deltaView, final SecQuote quote) {
        if (quote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            float now = quote.fNow > 0 ? quote.fNow : quote.fClose;
            valueView.setText(now > 0 ? StringUtil.getFormattedFloat(now, quote.iTpFlag) : "--");
            deltaView.setText(R.string.suspended);
        } else {
            setQuoteView(valueView, deltaView, quote.fClose, quote.fNow, quote.iTpFlag);
        }
    }

    public static void setQuoteView(final TextView valueView, final TextView deltaView, final float close, final float now, final int tpFlag) {
        final SpannableStringBuilder deltaText = new SpannableStringBuilder();

        TextAppearanceSpan span;

        if (close > 0 && now > 0) {
            final float delta = now - close;

            final float updown = (now / close - 1) * 100;
            if (delta > 0) {
                span = StringUtil.getUpStyle();
                deltaText.append('+').append(StringUtil.getFormattedFloat(delta, tpFlag)).append(' ').append(' ').append('+').append(StringUtil.getFormatedFloat(updown)).append('%');
            } else if (delta < 0) {
                span = StringUtil.getDownStyle();
                deltaText.append(StringUtil.getFormattedFloat(delta, tpFlag)).append(' ').append(' ').append(StringUtil.getFormatedFloat(updown)).append('%');
            } else {
                span = StringUtil.getSuspensionStyle();
                deltaText.append(StringUtil.getFormattedFloat(delta, tpFlag)).append(' ').append(' ').append(StringUtil.getFormatedFloat(updown)).append('%');
            }
        } else if (close != 0 && now != 0) {
            final float delta = now - close;
            if (delta > 0) {
                span = StringUtil.getUpStyle();
                deltaText.append('+').append(StringUtil.getFormattedFloat(delta, tpFlag)).append(' ').append(' ').append("--");
            } else if (delta < 0) {
                span = StringUtil.getDownStyle();
                deltaText.append(StringUtil.getFormattedFloat(delta, tpFlag)).append(' ').append(' ').append("--");
            } else {
                span = StringUtil.getSuspensionStyle();
                deltaText.append(StringUtil.getFormattedFloat(delta, tpFlag)).append(' ').append(' ').append("--");
            }
        } else {
            span = StringUtil.getSuspensionStyle();
            deltaText.append("--").append(' ').append(' ').append("--");
        }

        final float finalNow = now != 0f ? now : close;

        final SpannableString valueText = new SpannableString(finalNow != 0 ? StringUtil.getFormattedFloat(finalNow, tpFlag) : "--");
        valueText.setSpan(span, 0, valueText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        valueView.setText(valueText);

        deltaText.setSpan(span, 0, deltaText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        deltaView.setText(deltaText);
    }

    public static void setQuoteValueText(final TextView textView, float value, float baseValue, int tpFlag) {
        if (value != 0) {
            textView.setText(StringUtil.getFormattedFloat(value, tpFlag));
            if (baseValue != 0) {
                if (value > baseValue) {
                    textView.setTextColor(StringUtil.getColorRed());
                    return;
                } else if (value < baseValue) {
                    textView.setTextColor(StringUtil.getColorGreen());
                    return;
                }
            }
            textView.setTextColor(StringUtil.getColorBase());
        } else {
            textView.setText("--");
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.default_text_color_100));
        }
    }
}
