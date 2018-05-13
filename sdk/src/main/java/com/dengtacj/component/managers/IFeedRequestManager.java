package com.dengtacj.component.managers;

import android.content.Context;

import org.json.JSONObject;

import BEC.FeedItem;

/**
 * Created by davidwei on 2017-09-05.
 */

public interface IFeedRequestManager {
    void doLikeComment(final JSONObject jsonObject);
    boolean queryIsLike(String feedId);
    void showFeedDeleteDialog(Context context, String dtSecCode, String feedId, String commentId);
}
