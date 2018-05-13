package com.sscf.investment.popup;

import android.content.Context;

import com.sscf.investment.R;

/**
 * Created by LEN on 2018/4/16.
 */

public class PopupWindowNewFunctionUpLeft extends PopupWindowNewFunction {

    public PopupWindowNewFunctionUpLeft(Context context, int introID) {
        super(context, introID);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.popup_window_new_function_upleft;
    }
}
