package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.widget.Toast;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.ThreadUtils;

/**
 * Created by davidwei on 2016-08-13
 * 对话框
 */
public final class CommonToast {

	private CommonToast() {
	}

	public static void showToast(final String text) {
		final Context context = SDKManager.getInstance().getContext();
		if (context == null) {
			return;
		}

		if (ThreadUtils.isMainThread()) {
			showToastOnUi(context, text);
		} else {
			ThreadUtils.runOnUiThread(() -> showToastOnUi(context, text));
		}
	}

	public static void showToast(final int strId) {
		final Context context = SDKManager.getInstance().getContext();
		if (context == null) {
			return;
		}
		showToast(context.getString(strId));
	}

	public static void showToastOnUi(final Context context, final String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
