package com.sscf.investment.market.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IMarketWarningManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;
import BEC.ChangeStatDesc;
import BEC.ChangeStatRsp;
import BEC.GetSecBsInfoRsp;
import BEC.RealMarketQRRsp;

/**
 * Created by davidwei on 2016/06/20.
 * 市场行情沪深界面的工具区的HeaderView
 */
public final class MarketChinaWarningHeader extends LinearLayout implements View.OnClickListener, OnGetDataCallback<RealMarketQRRsp>, View.OnTouchListener {

    private View mMainBoardWarningLayout;
    private View mWarningPointer;
    private TextView mMarketTips1;
    private TextView mMarketTips2;
    private TextView mChangeStatText;
    private TextView mTvPoints;
    private MarketChinaChangeStatView mChangeStatView;

    public MarketChinaWarningHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        findViewById(R.id.group_more).setOnClickListener(this);
        mMainBoardWarningLayout = findViewById(R.id.mainBoardWarningLayout);
        mMainBoardWarningLayout.setOnClickListener(this);
        mWarningPointer = findViewById(R.id.mainBoardWarningPointer);
        mTvPoints = (TextView) findViewById(R.id.points);
        mMarketTips1 = (TextView) findViewById(R.id.tips1);
        mMarketTips2 = (TextView) findViewById(R.id.tips2);
        mChangeStatText = (TextView) findViewById(R.id.changeStatText);
        mChangeStatView = (MarketChinaChangeStatView) findViewById(R.id.changeStatChart);

        final IMarketWarningManager marketWarningManager = (IMarketWarningManager) ComponentManager.getInstance()
                .getManager(IMarketWarningManager.class.getName());
        if (marketWarningManager != null) {
            updateWarningInfo(marketWarningManager.getMainBoardWarningInfo());
        }

        updateChangeStatText(0, 0, 0, 0);
    }

    @Override
    public void onClick(View v) {
        final Context context = getContext();
        switch (v.getId()) {
            case R.id.mainBoardWarningLayout:
            case R.id.group_more:
                WebBeaconJump.showMarketChangeIndex(context);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MARKET_STENGTH);
                break;
            default:
                break;
        }
    }

    public void requestMarketWarning() {
        final IMarketWarningManager marketWarningManager = (IMarketWarningManager) ComponentManager.getInstance()
                .getManager(IMarketWarningManager.class.getName());
        if (marketWarningManager != null) {
            marketWarningManager.getMainBoardWarningInfoRequest(this);
        }
    }

    public void requestData() {
        final IMarketManager marketManager = (IMarketManager) ComponentManager.getInstance()
                .getManager(IMarketManager.class.getName());
        if (marketManager != null) {
            marketManager.requestChangeStat(mChangeStatCallback);
        }
    }

    @Override
    public void onGetData(final RealMarketQRRsp rsp) {
        ThreadUtils.runOnUiThread(() -> updateWarningInfo(rsp));
    }

    private void updateWarningInfo(final RealMarketQRRsp rsp) {
        if (rsp == null) {
            return;
        }
        final float rotate = rsp.fPoint * 180;
        mWarningPointer.setRotation(rotate);
        mMarketTips1.setText(rsp.sTypeText + " ");
        mTvPoints.setText(StringUtil.getFormatedFloat(rsp.fPoint*100) + "%");
        mMarketTips2.setText(rsp.sDescText);
        mMainBoardWarningLayout.setVisibility(VISIBLE);
    }

    private OnGetDataCallback<ChangeStatRsp> mChangeStatCallback = (rsp) -> {
        if (rsp != null) {
            final ArrayList<ChangeStatDesc> list = rsp.vChangeStatDesc;
            final int size = list == null ? 0 : list.size();
            if (size > 0) {
                ThreadUtils.runOnUiThread(() -> {
                    final ChangeStatDesc changeStatDesc = list.get(size - 1);
                    final int[] num = mChangeStatView.updateData(changeStatDesc);
                    updateChangeStatText(changeStatDesc.iChangeMin, num[0], num[1], changeStatDesc.iChangeMax);
                });
            }
        }
    };

    private void updateChangeStatText(final int min, final int down, final int up, final int max) {
        final Context context = getContext();
        mChangeStatText.setText("");
        mChangeStatText.append("跌停：");
        SpannableString text = new SpannableString(min + "只");
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.stock_green_color)),
                0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mChangeStatText.append(text);

        mChangeStatText.append("    下跌：");
        text = new SpannableString(down + "只");
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.stock_green_color)),
                0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mChangeStatText.append(text);

        mChangeStatText.append("    上涨：");
        text = new SpannableString(up + "只");
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.stock_red_color)),
                0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mChangeStatText.append(text);

        mChangeStatText.append("    涨停：");
        text = new SpannableString(max + "只");
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.stock_red_color)),
                0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mChangeStatText.append(text);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final float y = event.getRawY();
                final int[] location = new int[2];
                mChangeStatView.getLocationInWindow(location);
                if (y < location[1] || y > location[1] + mChangeStatView.getHeight()) {
                    mChangeStatView.setEditMode(false);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
