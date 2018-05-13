package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.sscf.investment.R;

/**
 * Created by davidwei on 2017/03/21
 */
public final class BeaconPullToRefreshWarningFrameLayout extends BeaconPtrFrameLayout {

    public BeaconPullToRefreshWarningFrameLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setResistance(2.0f);
        setRatioOfHeaderHeightToRefresh(1.0f);
        setDurationToClose(600);
        setDurationToCloseHeader(300);
        // default is false
        setPullToRefresh(false);
        // default is true
        setKeepHeaderWhenRefresh(true);
        setLoadingMinTime(800);
        disableWhenHorizontalMove(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final BeaconPullToRefreshWarningHeader header = (BeaconPullToRefreshWarningHeader)
                View.inflate(getContext(),  R.layout.ptr_warning_header, null);
        setHeaderView(header);
        addPtrUIHandler(header);
        getContentView().bringToFront();
    }
}
