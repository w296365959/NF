package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.component.ui.R;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsUtil;

/**
 * Created by liqf on 2015/7/16.
 */
public class TabLayout extends LinearLayout implements View.OnClickListener {
    public static final int TEXT_SCALE_DURATION = 100;
    private final Context mContext;
    private int mIndicatorExtraWidth;

    private int mCurrentTabIndex = -1;
    private OnTabSelectionListener mTabSelectionListener;
    private int mTabHeight;
    private String[] mTitles;
    private String[] mTags;

    private PopupWindow mPopupWindow;
    private boolean mCollapsed = false;
    private int mMaxShowCount;
    private String mFolderTitle;

    int mPopupItemHeight;
    int mPopupDividerHeight;
    int mPopupLayoutWidth;
    int mPopupLayoutEdgeWidth;
    int mPopupTextSize;
    int mPopupDividerColor;
    Drawable mPopupWindowBg;
    ColorStateList mTabTextColor;

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        mContext = context;

        setBackgroundResource(R.color.default_background);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
        mTabHeight = a.getDimensionPixelSize(R.styleable.TabLayout_tabHeight, 0);
        a.recycle();

        mPopupItemHeight = DeviceUtil.dip2px(context, 30);
        mPopupDividerHeight = DeviceUtil.dip2px(context, 0.5f);
        mPopupLayoutWidth = DeviceUtil.dip2px(context, 72);
        mPopupLayoutEdgeWidth = DeviceUtil.dip2px(context, 1);
        mPopupTextSize = DeviceUtil.dip2px(context, 14);
        mPopupDividerColor = ContextCompat.getColor(context, R.color.popup_window_divider_color);
        mPopupWindowBg = ContextCompat.getDrawable(context, R.drawable.round_rect_bg_popup_window);
        mTabTextColor = ContextCompat.getColorStateList(context, R.color.tab_title_text);
        mIndicatorExtraWidth = DeviceUtil.dip2px(context, 10);
        if (mTabHeight == 0) {
            mTabHeight = DeviceUtil.dip2px(context, 36);
        }
    }

    public String getCurrentTag() {
        final int length = mTags == null ? 0 : mTags.length;
        if (length == 0) {
            return "";
        }
        if (mCurrentTabIndex < 0 || mCurrentTabIndex >= length) {
            return "";
        }
        return mTags[mCurrentTabIndex];
    }

    public int getCurrentTabIndex() {
        return mCurrentTabIndex;
    }

    public String getTagByIndex(final int index) {
        return mTags[index];
    }

    public boolean switchTab(final int index) {
        int finalIndex = 0;
        if (index < mTitles.length) {
            finalIndex = index;
        }
        final boolean changed = mCurrentTabIndex != finalIndex;

        refreshSelectedTab(finalIndex);

        if (mTabSelectionListener != null && changed) {
            mTabSelectionListener.onTabSelected(finalIndex);
        }
        return changed;
    }

    public void switchTabNoCallback(final int index) {
        int finalIndex = 0;
        if (index < mTitles.length) {
            finalIndex = index;
        }

        refreshSelectedTab(finalIndex);
    }

    public void switchTab(final String tag) {
        if (mTags == null) {
            return;
        }

        if (TextUtils.isEmpty(tag)) {
            switchTab(0);
            return;
        }

        for (int i = 0; i < mTags.length; i++) {
            if (TextUtils.equals(tag, mTags[i])) {
                switchTab(i);
                return;
            }
        }
    }

    public void setRedDotVisible(final int index, final int visibility) {
        if (index < mTitles.length && index > 0) {
            final RelativeLayout relativeLayout = (RelativeLayout) getChildAt(index);
            relativeLayout.findViewById(R.id.tabRedDot).setVisibility(visibility);
        }
    }

    /**
     * Initailize the TabLayout with args.
     * @param titles Title array.
     * @param tags Tag array. The tag is used as the key for constructing fragment under the tab.
     */
    public void initWithTitles(int[] titles, String[] tags) {
        initWithTitles(titles, tags, titles.length, -1);
    }

    public void initWithTitles(String[] titles, String[] tags) {
        initWithTitles(titles, tags, titles.length, -1);
    }

    /**
     * Initailize the TabLayout with args.
     * @param tags Tag array. The tag is used as the key for constructing fragment under the tab.
     * @param maxShowCount maxShowCount specify the count of tabs that can be seen on screen, others will be foldered.
     * @param folderTitleRes The title that represents the folded tab titles.
     */
    public void initWithTitles(int[] titlesRes, String[] tags, int maxShowCount, int folderTitleRes) {
        final Resources resources = getResources();
        final int length = titlesRes.length;
        final String[] titles = new String[length];
        for (int i = 0; i < length; i++) {
            titles[i] = resources.getString(titlesRes[i]);
        }
        initWithTitles(titles, tags, maxShowCount, folderTitleRes);
    }

    /**
     * Initailize the TabLayout with args.
     * @param titles Title array.
     * @param tags Tag array. The tag is used as the key for constructing fragment under the tab.
     * @param maxShowCount maxShowCount specify the count of tabs that can be seen on screen, others will be foldered.
     * @param folderTitleRes The title that represents the folded tab titles.
     */
    public void initWithTitles(String[] titles, String[] tags, int maxShowCount, int folderTitleRes) {
        mTitles = titles;
        mTags = tags;
        mMaxShowCount = maxShowCount;

        int count = 0;
        mCollapsed = false;

        if (maxShowCount == titles.length) {
            count = titles.length;
        } else if (maxShowCount < titles.length) {
            count = maxShowCount;
            mCollapsed = true;
        }

        final Resources resources = getResources();
        RelativeLayout relativeLayout;
        TextView textView;
        for (int i = 0; i < count; i++) {
            String title = titles[i];
            if (mCollapsed && i == count - 1) {
                String folderTitle = resources.getString(folderTitleRes);
                mFolderTitle = folderTitle;
                title = folderTitle;
                relativeLayout = (RelativeLayout) View.inflate(mContext, R.layout.tab_layout_collapsed_item, null);
            } else {
                relativeLayout = (RelativeLayout) View.inflate(mContext, R.layout.tab_layout_item, null);
            }

            textView = (TextView) relativeLayout.findViewById(R.id.tabTitle);
            textView.setText(title);

            LayoutParams lp = new LayoutParams(0, mTabHeight);
            lp.weight = 1.0f;
            relativeLayout.setOnClickListener(this);
            addView(relativeLayout, lp);
        }
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == v) {
                index = i;
                break;
            }
        }

        if (mCollapsed && index == mMaxShowCount - 1) {
            showPopupWindow();
        } else {
            switchTab(index);
        }
    }

    private void showPopupWindow() {
        if (mPopupWindow == null) {
            makePopupWindow();
        }

        final int[] positon = new int[2];
        getLocationInWindow(positon);

        RelativeLayout layout = (RelativeLayout) getChildAt(mMaxShowCount - 1);
        int layoutWidth = layout.getWidth();

        final int xOffset = positon[0] + layoutWidth * (mMaxShowCount - 1)/* + (layoutWidth - mPopupLayoutWidth) / 2*/;
        final int yOffset = positon[1] + getHeight();

        try {
            mPopupWindow.showAtLocation(this, Gravity.TOP | Gravity.LEFT, xOffset, yOffset);
        } catch (Exception e) {
            e.printStackTrace();
            StatisticsUtil.reportError(e);
        }
    }

    private void makePopupWindow() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        int popupCount = mTitles.length + 1 - mMaxShowCount;
        int offset = mMaxShowCount;

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setMinimumWidth(mPopupLayoutWidth);
        linearLayout.setPadding(mPopupLayoutEdgeWidth, mPopupLayoutEdgeWidth * 2, mPopupLayoutEdgeWidth, mPopupLayoutEdgeWidth * 2);
        linearLayout.setBackground(mPopupWindowBg);
        for (int i = 0; i < popupCount; i++) {
            int index = offset + i - 1;

            String title = mTitles[index];

            TextView textView = new NumberTextView(getContext());
            textView.setTag(index);
            textView.setSelected(index == mCurrentTabIndex);
            textView.setText(title);
            Drawable popupItemBg = ContextCompat.getDrawable(mContext, R.drawable.popup_item_bg);
            textView.setBackground(popupItemBg);
            textView.setTextColor(mTabTextColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mPopupTextSize);
            textView.setGravity(Gravity.CENTER);

            textView.setOnClickListener(v -> {
                mPopupWindow.dismiss();

                int index1 = (int) v.getTag();
                switchTab(index1);
            });

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mPopupItemHeight);
            linearLayout.addView(textView, -1, lp);

            if (i != popupCount - 1) {
                ImageView divider = new ImageView(mContext);
                divider.setBackgroundColor(mPopupDividerColor);
                lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mPopupDividerHeight);
                linearLayout.addView(divider, -1, lp);
            }
        }

        mPopupWindow.setContentView(linearLayout);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setFocusable(true);

        mPopupWindow.setOnDismissListener(() -> mPopupWindow = null);
    }

    public void refreshSelectedTab(int selectedIndex) {
        mCurrentTabIndex = selectedIndex;
        float textScale = 16.0f / 14.0f;
        int count = mMaxShowCount;
        if (mCollapsed && selectedIndex >= mMaxShowCount - 1) { //如果当前tab是被折叠起来的
            RelativeLayout layout = (RelativeLayout) getChildAt(mMaxShowCount - 1);
            TextView textView = (TextView) layout.findViewById(R.id.tabTitle);
            View indicator = layout.findViewById(R.id.tabIndicator);
            ImageView arrowView = (ImageView) layout.findViewById(R.id.collapsedArrow);
            arrowView.setImageResource(R.drawable.tab_collapsed_arrow_selected);

            textView.setText(mTitles[selectedIndex]);

            refreshIndicatorByText(textView, indicator);

            textView.setSelected(true);
            textView.animate().scaleX(textScale).scaleY(textScale).setDuration(TEXT_SCALE_DURATION);
            indicator.setVisibility(VISIBLE);


            for (int i = 0; i < count - 1; i++) {
                layout = (RelativeLayout) getChildAt(i);
                textView = (TextView) layout.findViewById(R.id.tabTitle);
                indicator = layout.findViewById(R.id.tabIndicator);
                textView.setSelected(false);
                textView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(TEXT_SCALE_DURATION);
                indicator.setVisibility(GONE);
            }

            return;
        }

        for (int i = 0; i < count; i++) {
            final RelativeLayout frameLayout = (RelativeLayout) getChildAt(i);
            TextView textView = (TextView) frameLayout.findViewById(R.id.tabTitle);
            View indicator = frameLayout.findViewById(R.id.tabIndicator);
            if (selectedIndex == i) {
                if (mCollapsed) {
                    if (selectedIndex >= mMaxShowCount - 1) {
                        textView.setText(mTitles[selectedIndex]);
                        refreshIndicatorByText(textView, indicator);
                        ImageView arrowView = (ImageView) frameLayout.findViewById(R.id.collapsedArrow);
                        arrowView.setImageResource(R.drawable.tab_collapsed_arrow_selected);
                    }
                }

                textView.setSelected(true);
                textView.animate().scaleX(textScale).scaleY(textScale).setDuration(TEXT_SCALE_DURATION);
                indicator.setVisibility(VISIBLE);
            } else {
                if (mCollapsed) {
                    if (i == mMaxShowCount - 1) {
                        textView.setText(mFolderTitle);
                        refreshIndicatorByText(textView, indicator);
                        ImageView arrowView = (ImageView) frameLayout.findViewById(R.id.collapsedArrow);
                        arrowView.setImageResource(R.drawable.tab_collapsed_arrow_normal);
                    }
                }

                textView.setSelected(false);
//                textView.setScaleX(1.0f);
//                textView.setScaleY(1.0f);
                textView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(TEXT_SCALE_DURATION);
                indicator.setVisibility(GONE);
            }

        }
    }

    private void refreshIndicatorByText(TextView textView, View indicator) {
        int width = (int) textView.getPaint().measureText(textView.getText().toString());
        int textWidth = width + mIndicatorExtraWidth;
        ViewGroup.LayoutParams lp = indicator.getLayoutParams();
        lp.width = textWidth;
        indicator.setLayoutParams(lp);
    }

    public void setOnTabSelectionListener(OnTabSelectionListener listener) {
        mTabSelectionListener = listener;
    }

    public interface OnTabSelectionListener {
        void onTabSelected(final int index);
    }

}
