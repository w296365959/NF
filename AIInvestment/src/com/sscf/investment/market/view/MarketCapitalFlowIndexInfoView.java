package com.sscf.investment.market.view;

import BEC.CapitalMainFlowDesc;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by davidwei on 2015/9/15.
 */
public final class MarketCapitalFlowIndexInfoView extends LinearLayout {
    private TextView mTitleText;
    private TextView mValueText;
    private TextView mDeltaText;

    public MarketCapitalFlowIndexInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        mTitleText = (TextView) findViewById(R.id.title);
        mValueText = (TextView) findViewById(R.id.value);
        mDeltaText = (TextView) findViewById(R.id.delta);
    }

    public void updateInfos(CapitalMainFlowDesc indexInfo) {
        mTitleText.setText(indexInfo.sSecName);
        mValueText.setText(StringUtil.getCapitalFlowSpannable(indexInfo.fZljlr));

        final StringBuilder text = new StringBuilder(StringUtil.getFormatedFloat(indexInfo.fNow));
        text.append(' ').append(' ').append(' ').append(' ');
        if (indexInfo.fChangeRate > 0) {
            text.append('+');
        }
        text.append(StringUtil.getFormatedFloat(indexInfo.fChangeRate)).append('%');
        mDeltaText.setText(text);
    }
}
