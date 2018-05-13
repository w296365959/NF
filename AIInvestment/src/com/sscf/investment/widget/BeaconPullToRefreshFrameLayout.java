package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.sscf.investment.R;

/**
 * Created by liqf on 2016/8/12.
 */
public class BeaconPullToRefreshFrameLayout extends BeaconPtrFrameLayout {

    public BeaconPullToRefreshFrameLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setResistance(2.0f);
        setRatioOfHeaderHeightToRefresh(1.0f);
        setDurationToClose(600);
        setDurationToCloseHeader(300);//收回去的时间
        // default is false
        setPullToRefresh(false);
        // default is true
        setKeepHeaderWhenRefresh(true);
        setLoadingMinTime(500);//最少刷新时间
        disableWhenHorizontalMove(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final BeaconPullToRefreshHeader header = (BeaconPullToRefreshHeader)
                LayoutInflater.from(getContext()).inflate(R.layout.beacon_header, null);
        setHeaderView(header);
        addPtrUIHandler(header);

        View contentLayout = getContentView();
        contentLayout.bringToFront();
    }
}
