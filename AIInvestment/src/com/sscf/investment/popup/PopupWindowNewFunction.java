package com.sscf.investment.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sscf.investment.R;

/**
 * Created by LEN on 2018/3/23.
 */

public class PopupWindowNewFunction extends PopupWindow{

    private Context mContext;

    private TextView mTvNewFunctionIntro;

    public PopupWindowNewFunction(Context context, int introID){
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(getLayoutRes(), null, false);
        mTvNewFunctionIntro = (TextView) view.findViewById(R.id.guide_title);
        mTvNewFunctionIntro.setText(introID);
        view.findViewById(R.id.close_button).setOnClickListener((v) -> {
            dismiss();
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    protected int getLayoutRes(){
        return R.layout.popup_guide_top;
    }
}
