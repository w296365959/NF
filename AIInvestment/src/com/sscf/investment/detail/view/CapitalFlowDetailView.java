package com.sscf.investment.detail.view;

import BEC.CapitalFlow;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by xuebinliu on 26/04/2017.
 */
public class CapitalFlowDetailView extends LinearLayout {
    private ArrayList<TextView> valueTextViews;
    private ArrayList<TextView> percentTextViews;

    private float [] capitalValue = new float[8];
    private float [] capitalPercent = new float[8];

    public CapitalFlowDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View root = LayoutInflater.from(context).inflate(R.layout.capital_flow_detail_view, this);

        valueTextViews = new ArrayList<>();
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_super_in));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_big_in));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_middle_in));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_small_in));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_super_out));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_big_out));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_middle_out));
        valueTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_volume_small_out));

        percentTextViews = new ArrayList<>();
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_super_in));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_big_in));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_middle_in));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_small_in));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_super_out));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_big_out));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_middle_out));
        percentTextViews.add((TextView)root.findViewById(R.id.capital_flow_detail_percent_small_out));
    }

    public void setData(CapitalFlow capitalData) {
        float totalFund = capitalData.getFSuperin() + capitalData.getFBigin() + capitalData.getFMidin() + capitalData.getFSmallin() +
                capitalData.getFSuperout() + capitalData.getFBigout() + capitalData.getFMidout() + capitalData.getFSmallout();

        capitalValue[0] = capitalData.getFSuperin();
        capitalValue[1] = capitalData.getFBigin();
        capitalValue[2] = capitalData.getFMidin();
        capitalValue[3] = capitalData.getFSmallin();

        capitalValue[4] = capitalData.getFSuperout();
        capitalValue[5] = capitalData.getFBigout();
        capitalValue[6] = capitalData.getFMidout();
        capitalValue[7] = capitalData.getFSmallout();

        capitalPercent[0] = getRoundedValue(capitalData.getFSuperin() / totalFund);
        capitalPercent[1] = getRoundedValue(capitalData.getFBigin() / totalFund);
        capitalPercent[2] = getRoundedValue(capitalData.getFMidin() / totalFund);
        capitalPercent[3] = getRoundedValue(capitalData.getFSmallin() / totalFund);

        capitalPercent[4] = getRoundedValue(capitalData.getFSuperout() / totalFund);
        capitalPercent[5] = getRoundedValue(capitalData.getFBigout() / totalFund);
        capitalPercent[6] = getRoundedValue(capitalData.getFMidout() / totalFund);
        capitalPercent[7] = getRoundedValue(capitalData.getFSmallout() / totalFund);

        post(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }

    private void updateUI() {
        for(int i=0; i<8; i++) {
            valueTextViews.get(i).setText(StringUtil.getAmountString(capitalValue[i], 1));
            percentTextViews.get(i).setText(DataUtils.rahToStr(capitalPercent[i] * 100, 0) + "%");
        }
    }

    private float getRoundedValue(float value) {
        return Math.round(value * 100) / 100f;
    }
}
