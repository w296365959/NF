package com.sscf.investment.component.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import com.sscf.investment.component.ui.R;

/**
 * Created by davidwei on 2015-10-08.
 */
public final class LoadingDialog extends Dialog {
	private View mLoadingView;

	public LoadingDialog(Context context) {
		super(context, R.style.dialog_center_theme);
		setContentView(R.layout.dialog_loading);
		mLoadingView = findViewById(R.id.dialogLoading);
	}
}
