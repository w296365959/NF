package com.sscf.investment.widget.linechart;

import android.util.SparseArray;

import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;

import BEC.TrendDesc;

/**
 * Created by yorkeehuang on 2017/5/10.
 */

public class TimeLineChartViewHelper {

    public static ArrayList<TrendDesc> adaptEntites(ArrayList<TrendDesc> entities, float close, int startTime, int endTime) {
        ArrayList<TrendDesc> adaptedEntities = null;
        if(entities != null && !entities.isEmpty()) {
            int startHms = StringUtil.enlargeMinuteToHms(startTime);
            int endHms = StringUtil.enlargeMinuteToHms(endTime);

            SparseArray<TrendDesc> cache = new SparseArray<>(TimeLineConsts.DEFAULT_ENTITIES_COUNT);

            for(int i=0, size=entities.size() ; i<size; i++) {
                TrendDesc entity = entities.get(i);
                int entityHms = StringUtil.enlargeMinuteToHms(entity.getIMinute());
                cache.put(entityHms / TimeLineConsts.TIME_INTERVAL, entity);
            }

            adaptedEntities = new ArrayList<>(TimeLineConsts.DEFAULT_ENTITIES_COUNT);

            TrendDesc lastEntity = null;

            float min = -1f;
            for(int hms=startHms; hms<endHms; hms += TimeLineConsts.TIME_INTERVAL) {
                int key = hms / TimeLineConsts.TIME_INTERVAL;
                TrendDesc entity = cache.get(key);
                if(entity != null) {
                    if(entity.getFNow() <= 0) {
                        if(lastEntity != null) {
                            entity.setFNow(lastEntity.getFNow());
                        } else if(min < 0) {
                            min = TimeLineChartDrawingThread.computeMin(entities, close);
                            entity.setFNow(min);
                        }
                    }
                    lastEntity = entity;
                } else if(lastEntity == null) {
                    if(min < 0) {
                        min = TimeLineChartDrawingThread.computeMin(entities, close);
                    }
                    entity = new TrendDesc();
                    entity.setIMinute(StringUtil.timeMinuteToEnlargeMinute(hms));
                    entity.setFNow(min);
                    entity.setFAverage(min);
                }

                if(entity != null) {
                    adaptedEntities.add(entity);
                }
            }
        }
        return adaptedEntities;
    }

    public static int adaptMinute(int minute, int openTime, int closeTime) {
        if(minute - openTime < 15) {
            minute = openTime + 15;
        }

        if(closeTime - minute < 15) {
            minute = closeTime - 15;
        }

        return minute;
    }
}
