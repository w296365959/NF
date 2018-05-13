package com.sscf.investment.detail.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sscf.investment.R;

import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import BEC.SeniorExecutive;

/**
 * Created by yorkeehuang on 2017/6/29.
 */

public class ExecutiveProfileListView extends LinearLayout {

    public ExecutiveProfileListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(List<SeniorExecutive> seniorExecutiveList) {
        removeAllViews();
        for(SeniorExecutive seniorExecutive : seniorExecutiveList) {
            ExecutiveProfileView item = (ExecutiveProfileView)View.inflate(getContext(), R.layout.profile_item_layout, null);
            addView(item);
            item.setData(seniorExecutive);
        }
    }
}
