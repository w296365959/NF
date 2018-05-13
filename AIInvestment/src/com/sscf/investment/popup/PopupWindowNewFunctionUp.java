package com.sscf.investment.popup;

import android.content.Context;

import com.sscf.investment.R;

/**
 * Created by LEN on 2018/3/23.
 */

public class PopupWindowNewFunctionUp extends PopupWindowNewFunction {


    public PopupWindowNewFunctionUp(Context context, int introID) {
        super(context, introID);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.popup_guide_bottom;
    }
}
