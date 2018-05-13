package com.sscf.investment.market.view;

import BEC.PlateQuoteDesc;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.utils.StringUtil;

/**
 * Created by davidwei on 2015/06/15
 *
 */
public final class MarketPlateInfoView extends LinearLayout {

    private TextView mTitle;
    private TextView mValue;
    private TextView mCompany;
    private TextView mDelta;

    public MarketPlateInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.market_plate_info_view, this, true);
    }

    @Override
    protected void onFinishInflate() {
        mTitle = (TextView) findViewById(R.id.title);
        mValue = (TextView) findViewById(R.id.value);
        mCompany = (TextView) findViewById(R.id.company);
        mDelta = (TextView) findViewById(R.id.delta);
    }

    public void updateInfos(PlateQuoteDesc plateQuoteDesc) {
        mTitle.setText(plateQuoteDesc.sSecName);
        mValue.setText(StringUtil.getUpDownStringSpannable(plateQuoteDesc.fNow, plateQuoteDesc.fClose));
        mCompany.setText(plateQuoteDesc.sHeadName);
        final String updown = DataUtils.calculateUpDownString(plateQuoteDesc.fHeadNow, plateQuoteDesc.fHeadClose);
        mDelta.setText(StringUtil.getFormattedFloat(plateQuoteDesc.fHeadNow, plateQuoteDesc.iTpFlag) + "  " + updown);
    }
}
