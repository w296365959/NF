package com.sscf.investment.stare.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yorkeehuang on 2017/9/19.
 */

public class SelectedResultFlowLayout extends FlowLayout {

    private List<Integer> mValues = new ArrayList<>();

    public SelectedResultFlowLayout(Context context) {
        super(context);
    }

    public SelectedResultFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectedResultFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private int mMarinLeft = DeviceUtil.dip2px(getContext(), 8);
    private int mMarinTop = DeviceUtil.dip2px(getContext(), 8);
    private int mMarinRight = DeviceUtil.dip2px(getContext(), 8);
    private int mMarinBottom = DeviceUtil.dip2px(getContext(), 8);

    public void clearAll() {
        removeAllViews();
        mValues.clear();
    }

    public void addItem(int value, String title) {
        TextView itemView = (TextView) View.inflate(getContext(), R.layout.flow_select_item, null);
        itemView.setText(title);
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DeviceUtil.dip2px(getContext(), 24));
        mlp.setMargins(mMarinLeft, mMarinTop, mMarinRight, mMarinBottom);
        addView(itemView, mlp);
        mValues.add(value);
    }

    public List<Integer> getValues() {
        return mValues;
    }

    public Set<Integer> getValueSet() {
        return new HashSet(mValues);
    }
}
