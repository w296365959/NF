package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.widget.RectBackgroundDrawable;

import java.util.List;

/**
 * Created by liqf on 2016/9/9.
 */
public class CommentListActionBar extends RelativeLayout {
    private static final String TAG = "CommentListActionBar";

    private View mContentLayout;
    private TextView mTitle;
    private TextView mSubTitle;
    private View mSubTitleLayout;
    private TextView mSubTitleAlternative;

    private boolean mHasSubTitle;

    private LinearLayout mAttrTags;
    private int mSubTitleColor;
    private int mSubTitleMargin;
    private int mSubTitlePadding;
    private int mAttrTextSize;
    private int mAttrHeight;
    public static final int MAX_LETTER_COUNT = 20;

    private int mBigTitleTextSize;
    private String mDtSecCode;
    private String mSecName;

    public CommentListActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.actionbar_comment_list, this, true);

        initResources();

        setBackgroundColor(context);
    }

    private void setBackgroundColor(Context context) {
        int bgColor = ContextCompat.getColor(context, R.color.actionbar_bg);
        setBackgroundColor(bgColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContentLayout = findViewById(R.id.content_layout);

        mTitle = (TextView) findViewById(R.id.stock_title);
        mSubTitle = (TextView) findViewById(R.id.sub_title);
        mSubTitleLayout = findViewById(R.id.sub_title_layout);
        mSubTitleLayout.setVisibility(GONE);
        mSubTitleAlternative = (TextView) findViewById(R.id.sub_title_alternative);
        mSubTitleAlternative.setVisibility(VISIBLE);

        mAttrTags = (LinearLayout) findViewById(R.id.attr_tags);
    }

    private void initResources() {
        Resources resources = getResources();
        mSubTitleMargin = resources.getDimensionPixelSize(R.dimen.sub_title_margin);
        mSubTitlePadding = resources.getDimensionPixelSize(R.dimen.sub_title_padding);
        mAttrTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mAttrHeight = resources.getDimensionPixelSize(R.dimen.stock_subject_title_height);
        mBigTitleTextSize = resources.getDimensionPixelSize(R.dimen.font_size_22);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
                R.attr.actionbar_subtitle_text_color
        });
        mSubTitleColor = a.getColor(0, Color.GRAY);
        a.recycle();
    }

    public void setSubTitleAlternativeText(final String text) {
        mSubTitleAlternative.setText(text);
    }

    public void setDtSecCode(final String dtSecCode, final String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;
    }

    public void setHasSubTitle(boolean hasSubTitle) {
        mHasSubTitle = hasSubTitle;

        if (!mHasSubTitle) {
            FrameLayout contentLayout = (FrameLayout) findViewById(R.id.content_layout);
            contentLayout.removeView(mTitle);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBigTitleTextSize);
            this.addView(mTitle, lp);
        }
    }

    public void setTitleText(final String text) {
        if (!mHasSubTitle) {
            mTitle.setText(text);
            return;
        }

        String trimmedText = text.toString().trim();
//        String trimmedText = "zhongguo安徽黄山海螺水泥工厂(123456)".toString().trim();
//        String trimmedText = "中国安徽黄山海螺水泥工厂(123456)".toString().trim();
//        String trimmedText = "中国安徽黄山海螺(123456)".toString().trim();
//        String trimmedText = "zhongguoanhuihuangshanghailuoshuini(123456)".toString().trim();

        int count = getCount(trimmedText);

        int splitPosition = 0;
        if (count > MAX_LETTER_COUNT) {
            for (int i = 0; i < trimmedText.length(); i++) {
                if (trimmedText.charAt(i) == '(') {
                    splitPosition = i;
                    break;
                }
            }
            String prevStr = "";
            int prevLen = 0;
            String postStr = trimmedText.substring(splitPosition, trimmedText.length());
            int postLen = getCount(postStr);

            prevLen = MAX_LETTER_COUNT - postLen;
            prevStr = getPrevString(trimmedText, prevLen);

            String finalStr = prevStr + postStr;
            mTitle.setText(finalStr);
        } else {
            mTitle.setText(trimmedText);
        }
    }

    public void setSubTitleText(final String text) {
        mSubTitle.setText(text);
    }

    public void setAttrTags(final List<String> attrTags) {
        mAttrTags.removeAllViews();

        if (attrTags == null || attrTags.size() == 0) {
            return;
        }

        int size = attrTags.size();
        for (int i = 0; i < size; i++) {
            String attrTag = attrTags.get(i);
            TextView textView = new TextView(getContext());
            textView.setTextColor(mSubTitleColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrTextSize);
            textView.setText(attrTag);
            textView.setPadding(mSubTitlePadding, 0, mSubTitlePadding, 0);
            RectBackgroundDrawable bgDrawable = new RectBackgroundDrawable(getContext());
            bgDrawable.setColor(mSubTitleColor);
            textView.setBackgroundDrawable(bgDrawable);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mAttrHeight);
            if (i != size - 1) {
                lp.setMargins(0, 0, mSubTitleMargin, 0);
            }
            mAttrTags.addView(textView, lp);
        }

        mAttrTags.setVisibility(VISIBLE);
    }

    private String getPrevString(String trimmedText, int maxPrevLen) {
        String prevStr;

        int count = 0;
        int splitPosition = 0;
        for (int i = 0; i < trimmedText.length(); i++) {
            char c = trimmedText.charAt(i);
            if (isAlphabet(c)) {
                count++;
            } else {
                count += 2;
            }
            if (count > maxPrevLen) {
                splitPosition = i;
                break;
            }
        }

        prevStr = trimmedText.substring(0, splitPosition) + "...";
//        prevStr = prevStr.substring(0, prevStr.length() - 1) + "...";

        return prevStr;
    }

    private int getCount(String trimmedText) {
        int count = 0;
        for (int i = 0; i < trimmedText.length(); i++) {
            char c = trimmedText.charAt(i);
            if (isAlphabet(c)) {
                count++;
            } else {
                count += 2;
            }
        }
        return count;
    }

    private boolean isAlphabet(char c) {
        if ((c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a')
                || c == ' '
                || (c >= '0' && c <= '9')
                || (c == '(' || c == ')')) {
            return true;
        }
        return false;
    }
}
