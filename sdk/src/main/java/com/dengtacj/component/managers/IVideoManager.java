package com.dengtacj.component.managers;

import android.content.Context;

import com.dengtacj.component.entity.VideoDisplayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/10/13.
 */

public interface IVideoManager {
    interface OnGetVideoCallback {
        void onGetLiveVideoList(List<VideoDisplayItem> liveVideoList);

        void onGetLiveVideoError();

        void onGetRecommendVideoList(List<VideoDisplayItem> recommendList, int channel);

        void onGetRecommendVideoError();
    }

    int loadLiveVideoList(OnGetVideoCallback callback);

    int loadRecommVideoList(ArrayList<String> videoKeys, int channel, OnGetVideoCallback callback);

    void goVideoLiveSchedule(Context context);

    void goVideoList(Context context);

    void goVideoDetail(Context context, String key, int type);
}
