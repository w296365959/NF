package com.sscf.investment.market.view;

import BEC.AHExtendInfo;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;

/**
 * Created by davidwei on 2015/9/23.
 * 沪港通余额的HeaderView
 */
public final class MarketSHHKConnectBalanceHeader extends LinearLayout implements OnGetDataCallback<AHExtendInfo>, Handler.Callback {
    private Handler mHandler;

    private SpannableString mSHConnectBalance;
    private SpannableString mHKConnectBalance;

    private TextView mSHConnectBalanceView;
    private TextView mHKConnectBalanceView;

    public MarketSHHKConnectBalanceHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHandler = new Handler(this);
        final Resources res = getResources();
        mSHConnectBalance = new SpannableString(res.getString(R.string.market_sh_connect_balance));
        mHKConnectBalance = new SpannableString(res.getString(R.string.market_hk_connect_balance));
        final TextAppearanceSpan span = new TextAppearanceSpan(getContext(), R.style.market_sh_hk_connect_balance_text_style);
        mSHConnectBalance.setSpan(span, 0, mSHConnectBalance.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mHKConnectBalance.setSpan(span, 0, mHKConnectBalance.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mSHConnectBalanceView = (TextView) findViewById(R.id.marketSHConnectBalance);
        mSHConnectBalanceView.append(mSHConnectBalance);
        mHKConnectBalanceView = (TextView) findViewById(R.id.marketHKConnectBalance);
        mHKConnectBalanceView.append(mHKConnectBalance);
    }

    public void requestData(final IMarketManager marketManager) {
        marketManager.requestSHHKConnectBalance(this);
    }

    @Override
    public void onGetData(AHExtendInfo data) {
        if (data != null) {
            mHandler.obtainMessage(0, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        final AHExtendInfo info  = (AHExtendInfo) msg.obj;
        mSHConnectBalanceView.setText(String.valueOf(info.fSHHKFlowInto));
        mSHConnectBalanceView.append(getResources().getString(R.string.yi));
        mSHConnectBalanceView.append(mSHConnectBalance);
        mHKConnectBalanceView.setText(String.valueOf(info.fHKSHFlowInto));
        mHKConnectBalanceView.append(getResources().getString(R.string.yi));
        mHKConnectBalanceView.append(mHKConnectBalance);
        return false;
    }
}
