package com.sscf.investment.web.sdk.widget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.sscf.investment.component.ui.widget.PicPickerDialog;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by xuebinliu on 2015/8/12.d \]
 */
public final class DtWebChromeClient extends WebChromeClient implements PicPickerDialog.PickerCallback {

    private ValueCallback<Uri[]> mValueCallback;
    private PicPickerDialog mPicPickerDialog;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicPickerDialog != null) {
            mPicPickerDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
        }
        mValueCallback = valueCallback;

        PicPickerDialog dialog = null;

        if (fileChooserParams != null) {
            String [] accept = fileChooserParams.getAcceptTypes();
            for (String a : accept) {
                if (a.contains("image")) {
                    dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
                } else if (a.contains("video")) {
                    dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_CAPTURE_VIDEO);
                } else {
                    dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
                }
            }
        } else {
            dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
        }

        dialog.setCrop(false);
        dialog.show();
        mPicPickerDialog = dialog;

        return true;
    }

    @Override
    public void onGetPicSuccess(Uri uri) {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(uri == null ? null : new Uri[] { uri });
            mValueCallback = null;
        }
    }

    @Override
    public void onGetPicCancel() {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
    }

    @Override
    public void onGetPicError() {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
    }
}
