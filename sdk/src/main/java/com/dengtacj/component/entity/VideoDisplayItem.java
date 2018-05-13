package com.dengtacj.component.entity;

/**
 * Created by yorkeehuang on 2017/10/16.
 */

public class VideoDisplayItem {

    private int mType;
    private String mKey;
    private String mImageUrl;
    private String mTitle;
    private int mChannel;
    private String mChannelName;
    private int mStatus;
    private int mFavorTime;

    public VideoDisplayItem(int type, String key) {
        mType = type;
        mKey = key;
    }

    public int getType() {
        return mType;
    }

    public String getKey() {
        return mKey;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getChannel() {
        return mChannel;
    }

    public void setChannel(int channel) {
        mChannel = channel;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String channelName) {
        mChannelName = channelName;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getFavorTime() {
        return mFavorTime;
    }

    public void setFavorTime(int favorTime) {
        mFavorTime = favorTime;
    }
}
