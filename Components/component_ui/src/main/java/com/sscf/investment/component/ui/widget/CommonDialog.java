package com.sscf.investment.component.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.component.ui.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

/**
 * Created by davidwei on 2016-08-13
 * 对话框
 */
public final class CommonDialog extends Dialog implements View.OnClickListener {
	private boolean mCanCancelOnTouchOutside = false;

	private LinearLayout mContentView;
	private LinearLayout mButtonLayout;

	private int mButtonCount;

	private OnDialogButtonClickListener mButtonClickListener;

	private Object mTag;

	public CommonDialog(final Context context) {
		super(context, R.style.dialog_center_theme);
		setContentView(R.layout.dialog_common);
		final View contentView = findViewById(R.id.content);

		getWindow().getDecorView().setOnTouchListener((v, event) -> {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					final RectF frame = new RectF(
							contentView.getX(),
							contentView.getY(),
							contentView.getX() + contentView.getWidth(),
							contentView.getY() + contentView.getHeight());
					if (mCanCancelOnTouchOutside && !frame.contains(event.getX(), event.getY())) {
						cancel();
					}
				}
				return false;
		});

		mContentView = (LinearLayout) contentView;
	}

	public void setTag(Object tag) {
		this.mTag = tag;
	}

	public Object getTag() {
		return mTag;
	}

	public void setCanCancelOnTouchOutside(final boolean can) {
		this.mCanCancelOnTouchOutside = can;
	}

	@Override
	public void setTitle(final int textRes) {
		setTitle(getContext().getResources().getString(textRes));
	}

	@Override
	public void setTitle(final CharSequence text) {
		final TextView titleView = (TextView) findViewById(R.id.title);
		if (TextUtils.isEmpty(text)) {
			titleView.setVisibility(View.GONE);
			titleView.setText("");
		} else {
			titleView.setText(text);
			titleView.setVisibility(View.VISIBLE);
		}
	}

	public void setMessage(final int stringId) {
		((TextView) findViewById(R.id.message)).setText(stringId);
	}

	public void setMessage(final String text) {
		((TextView) findViewById(R.id.message)).setText(text);
	}

	public void addButton(final int stringId) {
		addButton(stringId, 0);
	}

	public void addButton(final int stringId, final int textColor) {
		final Context context = getContext();
		addButton(context.getString(stringId), textColor);
	}

	public void addButton(final String text) {
		addButton(text, 0);
	}

	public void addButton(final String text, final int textColor) {
		final Context context = getContext();
		if (mButtonLayout == null) { // 添加第一个button
			mButtonLayout = new LinearLayout(context);
			final int height = DeviceUtil.dip2px(context, 48);
			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
			mContentView.addView(mButtonLayout, params);

			addButton(context, text, textColor);
		} else {
			addButtonDivier(context);
			addButton(context, text, textColor);
		}
	}

	private void addButton(final Context context, final String text, final int textColor) {
		final Button button = (Button) View.inflate(context, R.layout.dialog_button, null);
		button.setText(text);
		if(textColor != 0) {
			button.setTextColor(textColor);
		}
		button.setTag(mButtonCount);
		mButtonCount++;
		button.setOnClickListener(this);
		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
		params.weight = 1f;
		mButtonLayout.addView(button, params);
	}

	private void addButtonDivier(final Context context) {
		final View divider = new View(context);
		divider.setBackgroundResource(R.color.list_divider);
		final int width = context.getResources().getDimensionPixelSize(R.dimen.divider_size);
		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
		mButtonLayout.addView(divider, params);
	}

	public void setButtonClickListener(final OnDialogButtonClickListener l) {
		mButtonClickListener = l;
	}

	@Override
	public void onClick(View v) {
		if (mButtonClickListener != null) {
			final int position = (int) v.getTag();
			mButtonClickListener.onDialogButtonClick(this, v, position);
		} else {
			dismiss();
		}
	}

	public interface OnDialogButtonClickListener {
		void onDialogButtonClick(final CommonDialog dialog, final View view, final int position);
	}
}
