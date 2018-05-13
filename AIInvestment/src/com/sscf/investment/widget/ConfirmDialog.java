package com.sscf.investment.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sscf.investment.R;

/**
 * Created by davidwei on 2015-08-30.
 * 确认对话框
 */
public final class ConfirmDialog extends Dialog implements View.OnClickListener {
	private static final String TAG = ConfirmDialog.class.getSimpleName();

	private Button mOkButton;
	private Button mCancelButton;

	private boolean mCanCancelOnTouchOutside = true;

	public ConfirmDialog(final Context context) {
		super(context, R.style.dialog_center_theme);
		setContentView(R.layout.dialog_confirm);
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

		mOkButton = (Button) findViewById(R.id.ok);
		mOkButton.setOnClickListener(this);
		mCancelButton = (Button) findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(this);
	}

	public void setCanCancelOnTouchOutside(final boolean can) {
		this.mCanCancelOnTouchOutside = can;
	}

	public void setMessage(final int stringId) {
		((TextView) findViewById(R.id.dialogMessage)).setText(stringId);
	}

	public void setMessage(final String text) {
		((TextView) findViewById(R.id.dialogMessage)).setText(text);
	}

	public void setOkButton(final int stringId, final View.OnClickListener l) {
		mOkButton.setText(stringId);
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

	public void setCancelButtonVisibility(final int visibility) {
		findViewById(R.id.dialogButtonDivider).setVisibility(visibility);
		mCancelButton.setVisibility(visibility);
	}

	@Override
	public void onClick(View v) {
		cancel();
	}
}
