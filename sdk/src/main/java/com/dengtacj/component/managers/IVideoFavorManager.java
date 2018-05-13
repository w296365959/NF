package com.dengtacj.component.managers;

import android.content.Context;

import com.dengtacj.component.entity.VideoDisplayItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yorkeehuang on 2017/10/12.
 */

public interface IVideoFavorManager {

    void init();

    void loadVideoFavorList();

    void deleteVideoFavors(ArrayList<String> keys);

    List<VideoDisplayItem> getVideoFavorList();

    int getVideoFavorCount();

    void goVideoFavorDetail(Context context, String key, int type);
}
