package com.sscf.investment.market.view;

import BEC.CapitalDetailDesc;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by davidwei on 2015/9/15.
 */
public class MarketCapitalFlowPlateInfoView extends LinearLayout {
    private TextView mTitleText;
    private TextView mValueText;

    public MarketCapitalFlowPlateInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleText = (TextView) findViewById(R.id.title);
        mValueText = (TextView) findViewById(R.id.value);
    }

    public void updateInfos(CapitalDetailDesc industry) {
        mTitleText.setText(industry.sSecName);
        mValueText.setText(StringUtil.getCapitalFlowSpannable(industry.fZljlr));
    }
}
