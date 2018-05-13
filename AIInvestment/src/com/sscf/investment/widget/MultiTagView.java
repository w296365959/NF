package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;

import java.util.ArrayList;

/**
 * Created by davidwei on 2015-10-24.
 * 多个tag的View
 */
public final class MultiTagView extends LinearLayout {
	private final int mTagTextSize;
	private final int mTagPaddingTopBottom;
	private final int mTagPaddingLeftRight;
	private final int mTagMargin;
	private ArrayList<TextView> mTagViews;

	public MultiTagView(Context context, AttributeSet attrs) {
		super(context, attrs);

		int defaultTextSize = getResources().getDimensionPixelSize(R.dimen.font_size_8);
		TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MultiTagView);
		mTagTextSize = array.getDimensionPixelSize(R.styleable.MultiTagView_tagTextSize, defaultTextSize);
		mTagPaddingTopBottom = array.getDimensionPixelSize(R.styleable.MultiTagView_tagPaddingTopBottom, 0);
		final Resources resources = getResources();
		mTagPaddingLeftRight = array.getDimensionPixelSize(R.styleable.MultiTagView_tagPaddingLeftRight,
				resources.getDimensionPixelSize(R.dimen.stock_mark_padding));
		mTagMargin = array.getDimensionPixelSize(R.styleable.MultiTagView_tagMargin,
				resources.getDimensionPixelSize(R.dimen.tag_margin));
		array.recycle();
	}

	public void addTags(ArrayList<String> tags, @DrawableRes final int tagBgResId, final int textColorResId) {
		if (tags == null) {
			clearAllTags();
			return;
		}

		final int tagSize = tags.size();
		if (tagSize <= 0) {
			clearAllTags();
			return;
		}

		setVisibility(VISIBLE);
		if (mTagViews == null) {
			mTagViews = new ArrayList<TextView>(3);
		}

		int textColor = ContextCompat.getColor(getContext(), textColorResId);
		final int tagViewSize = mTagViews.size();

		if (tagSize > tagViewSize) { // 如果tag的个数比当前的tagview的个数多，就需要添加TextView
			for (int i = 0; i < tagViewSize; i++) {
				mTagViews.get(i).setText(tags.get(i));
			}

			final Context context = getContext();

			TextView tagView = null;

			final LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = mTagMargin;

			for (int i = tagViewSize; i < tagSize; i++) {
				tagView = new TextView(context);
				tagView.setMaxLines(1);
				tagView.setIncludeFontPadding(false);
				tagView.setEllipsize(TextUtils.TruncateAt.END);
				tagView.setGravity(Gravity.CENTER);
				tagView.setBackgroundResource(tagBgResId);
				tagView.setText(tags.get(i));
				tagView.setTextColor(textColor);
				tagView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTagTextSize);
				tagView.setPadding(mTagPaddingLeftRight, mTagPaddingTopBottom, mTagPaddingLeftRight, mTagPaddingTopBottom);
				addView(tagView, params);
				mTagViews.add(tagView);
			}
		} else { // 如果tag的个数比当前的tagview的个数少，就需要删除TexView
			TextView tagView = null;
			for (int i = 0; i < tagSize; i++) {
				tagView = mTagViews.get(i);
				tagView.setText(tags.get(i));
				tagView.setTextColor(textColor);
				tagView.setBackgroundResource(tagBgResId);
			}
			if (tagSize < tagViewSize) { // 删除多余的TextView
				removeViews(tagSize, tagViewSize - tagSize);
				for (int i = tagViewSize - 1; i >= tagSize; i--) {
					mTagViews.remove(i);
				}
			}
		}
	}

	public void clearAllTags() {
		setVisibility(INVISIBLE);
		if (mTagViews != null && mTagViews.size() > 0) {
			mTagViews.clear();
			removeAllViews();
		}
	}
}
