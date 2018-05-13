package com.sscf.investment.widget.linechart;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.TextureView;

import java.util.ArrayList;

import BEC.TrendDesc;

/**
 * Created by yorkeehuang on 2017/3/31.
 */

public class TimeLineChartTextureView extends TextureView {
    public TimeLineChartTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private TimeLineChartSurfaceTextureListener getTextureListener() {
        return (TimeLineChartSurfaceTextureListener) getSurfaceTextureListener();
    }

    public void setTimeValueChangeListener(TimeLineChartTextureView.TimeValueChangeListener timeValueChangeListener) {
        TimeLineChartSurfaceTextureListener listener = getTextureListener();
        if(listener != null) {
            listener.setTimeValueChangeListener(timeValueChangeListener);
        }
    }

    public void setTimeLineEntities(TimeLineInfo info, ArrayList<TrendDesc> entities) {
        TimeLineChartSurfaceTextureListener listener = getTextureListener();
        if(listener != null) {
            listener.setTimeEntities(info, entities);
        }
    }

    public void setTradingMinutes(int tradingMinutes) {
        TimeLineChartSurfaceTextureListener listener = getTextureListener();
        if(listener != null) {
            listener.setTradingMinutes(tradingMinutes);
        }
    }


    public void setTradingTime(final String openTimeStr, final String middleTimeStr, final String closeTimeStr) {
        TimeLineChartSurfaceTextureListener listener = getTextureListener();
        if(listener != null) {
            listener.setTradingTime(openTimeStr, middleTimeStr, closeTimeStr);
        }
    }

    public void setInfo(TimeLineInfo info) {
        TimeLineChartSurfaceTextureListener listener = getTextureListener();
        if(listener != null) {
            listener.setTimeLineInfo(info);
        }
    }

    public interface TimeValueChangeListener {
        void onValuePositionChanged(Point point);
    }
}
