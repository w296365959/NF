package com.sscf.investment.component.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.component.ui.R;

/**
 * Created by davidwei on 2015-08-30.
 * 输入对话框
 */
public final class InputDialog extends Dialog implements View.OnClickListener {
	private TextView mTitleView;
	private EditText mEditText;
	private View mClearButton;
	private Button mOkButton;
	private Button mCancelButton;
	private int mMaxInputLength;

	private boolean mCanCancelOnTouchOutside = true;

	public InputDialog(final Context context) {
		super(context, R.style.dialog_input_theme);
		setContentView(R.layout.dialog_input);

		final Window dialogWindow = getWindow();

		dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		final WindowManager.LayoutParams params = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;

		final View contentView = findViewById(R.id.dialogContentView);

		getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
			}
		});

		mTitleView = (TextView) findViewById(R.id.dialogTitle);
		mEditText = (EditText) findViewById(R.id.dialogEditText);
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = mEditText.getText().toString();
				final int length = text.length();
				mClearButton.setVisibility(length > 0 ? View.VISIBLE : View.INVISIBLE);
				if (length > mMaxInputLength && mMaxInputLength > 0) {
					text = text.substring(0, mMaxInputLength);
					mEditText.setText(text);
					mEditText.setSelection(text.length());
				}
			}
		});
		mClearButton = findViewById(R.id.inputClear);
		mClearButton.setOnClickListener(this);

		mOkButton = (Button) findViewById(R.id.ok);
		mOkButton.setOnClickListener(this);
		mCancelButton = (Button) findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(this);
	}

	public void setMaxInputLength(int maxInputLength) {
		mMaxInputLength = maxInputLength;
	}

	public void setCanCancelOnTouchOutside(final boolean can) {
		this.mCanCancelOnTouchOutside = can;
	}

	public void setTitle(final int stringId) {
		mTitleView.setText(stringId);
		mTitleView.setVisibility(View.VISIBLE);
	}

	public void setTitle(final String text) {
		mTitleView.setText(text);
		mTitleView.setVisibility(View.VISIBLE);
	}

	public void setInputHint(final int hintId) {
		mEditText.setHint(hintId);
	}

	public void setInputHint(final String hint) {
		mEditText.setHint(hint);
	}

	public void setInputText(final String text) {
		mEditText.setText(text);
		if (!TextUtils.isEmpty(text)) {
			mEditText.setSelection(text.length());
		}
	}

	public String getInputText() {
		return mEditText.getText().toString();
	}

	public void setOkButton(final int stringId, final View.OnClickListener l) {
		mOkButton.setText(stringId);
		if (l != null) {
			mOkButton.setOnClickListener(l);
		}
	}

	public void setOkButton(final String text, final View.OnClickListener l) {
		mOkButton.setText(text);
		if (l != null) {
			mOkButton.setOnClickListener(l);
		}
	}

	public void setCancelButton(final int stringId, final View.OnClickListener l) {
		mCancelButton.setText(stringId);
		if (l != null) {
			mCancelButton.setOnClickListener(l);
		}
	}

	public void setCancelButton(final String text, final View.OnClickListener l) {
		mCancelButton.setText(text);
		if (l != null) {
			mCancelButton.setOnClickListener(l);
		}
	}

	public void setCancelButtonVisibility(final int visibility) {
		findViewById(R.id.dialogButtonDivider).setVisibility(visibility);
		mCancelButton.setVisibility(visibility);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.inputClear) {
			mEditText.setText("");
		} else {
			cancel();
		}
	}
}
