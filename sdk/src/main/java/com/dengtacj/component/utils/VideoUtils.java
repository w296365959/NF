package com.dengtacj.component.utils;

import com.dengtacj.component.entity.VideoDisplayItem;

import java.util.ArrayList;
import java.util.List;

import BEC.E_VIDEO_TYPE;
import BEC.E_VIDOE_CHANNEL;
import BEC.FineVideoGroup;
import BEC.LiveRoomListRsp;
import BEC.RecommVideoListRsp;
import BEC.UpdateFaverateVideoRsp;
import BEC.VideoBlock;
import BEC.VideoBlockList;
import BEC.VideoChannelDesc;
import BEC.VideoDetail;

/**
 * Created by yorkeehuang on 2017/10/11.
 */

public class VideoUtils {

    private VideoUtils() {

    }

    public static List<VideoDisplayItem> convertVideoBlockList(List<VideoBlock> videoBlockList) {
        if(videoBlockList != null) {
            List<VideoDisplayItem> videoDetailList = new ArrayList<>(videoBlockList.size());
            for(VideoBlock videoBlock : videoBlockList) {
                VideoDisplayItem videoDisplayItem = parseVideoBlock(videoBlock);
                if(videoBlock != null) {
                    videoDetailList.add(videoDisplayItem);
                }
            }
            return videoDetailList;
        }
        return null;
    }

    public static VideoDisplayItem parseVideoBlock(VideoBlock videoBlock) {
        if(videoBlock != null) {
            switch (videoBlock.getEType()) {
                case E_VIDEO_TYPE.VT_JP_GROUP: // 精品课的系列
                    return parseVideoGroup(videoBlock.getStFineVideoGroup());
                case E_VIDEO_TYPE.VT_UNKOWN: // 未知
                case E_VIDEO_TYPE.VT_ZB:    // 直播课
                case E_VIDEO_TYPE.VT_LB:    // 录播课
                case E_VIDEO_TYPE.VT_JP:    // 精品课子课
                    return parseVideoDetail(videoBlock.getStVideoDetail());
                default:
            }
        }
        return null;
    }

    public static VideoDisplayItem parseVideoDetail(VideoDetail videoDetail) {
        if(videoDetail != null) {
            VideoDisplayItem displayItem = new VideoDisplayItem(videoDetail.getEType(), videoDetail.getSVideoKey());
            displayItem.setTitle(videoDetail.getSTitle());
            displayItem.setImageUrl(videoDetail.getSImgUrl());
            displayItem.setStatus(videoDetail.getEStatus());
            displayItem.setChannel(videoDetail.getEChannel());
            VideoChannelDesc channelDesc = videoDetail.getStChannel();
            if(channelDesc != null) {
                displayItem.setChannelName(channelDesc.getSName());
            }
            displayItem.setFavorTime(videoDetail.getIFaverateTime());
            return displayItem;
        }
        return null;
    }

    private static VideoDisplayItem parseVideoGroup(FineVideoGroup videoGroup) {
        if(videoGroup != null) {
            VideoDisplayItem displayItem = new VideoDisplayItem(E_VIDEO_TYPE.VT_JP_GROUP, videoGroup.getSGroupKey());
            displayItem.setTitle(videoGroup.getSTitle());
            displayItem.setImageUrl(videoGroup.getSImgUrl());
            displayItem.setChannel(videoGroup.getEChannel());
            VideoChannelDesc channelDesc = videoGroup.getStChannel();
            if(channelDesc != null) {
                displayItem.setChannelName(channelDesc.getSName());
            }
            displayItem.setFavorTime(videoGroup.getIFaverateTime());
            return displayItem;
        }
        return null;
    }

    public static LiveRoomListRsp createTestLive(final int count) {
        LiveRoomListRsp rsp = new LiveRoomListRsp();
        ArrayList<VideoDetail> liveList = new ArrayList(count);
        String imageUrl = "http://www.52investing.com/52crm/upload/image/20171011/1507711636565910.jpg";
        for(int i=0; i<count; i++) {
            VideoDetail detail = new VideoDetail();
            detail.setSImgUrl(imageUrl);
            detail.setSTitle("测试标题");
            detail.setEChannel(E_VIDOE_CHANNEL.VC_52JRJY);
            liveList.add(detail);
        }
        rsp.setVtVideoDetail(liveList);
        return rsp;
    }

    public static RecommVideoListRsp createTestRecommend(final int count) {
        RecommVideoListRsp recommVideoListRsp = new RecommVideoListRsp();
        recommVideoListRsp.setVVideoBlock(new ArrayList<VideoBlock>(count));
        String imageUrl = "http://www.52investing.com/52crm/upload/image/20171011/1507711636565910.jpg";
        for(int i=0; i<count; i++) {
            VideoBlock videoBlock = new VideoBlock();
            recommVideoListRsp.getVVideoBlock().add(videoBlock);
            videoBlock.setEType(E_VIDEO_TYPE.VT_ZB);
            VideoDetail detail = new VideoDetail();
            detail.setEChannel(E_VIDOE_CHANNEL.VC_52JRJY);
            detail.setSImgUrl(imageUrl);
            detail.setSTitle("测试标题");
            videoBlock.setStVideoDetail(detail);
        }
        return recommVideoListRsp;
    }

    public static VideoBlockList createTestFavor(final int count) {
        VideoBlockList videoBlockList = new VideoBlockList();
        videoBlockList.setVVideoBlock(new ArrayList<VideoBlock>(count));
        String imageUrl = "http://www.52investing.com/52crm/upload/image/20171011/1507711636565910.jpg";
        for(int i=0; i<count; i++) {
            VideoBlock videoBlock = new VideoBlock();

            videoBlockList.getVVideoBlock().add(videoBlock);
            videoBlock.setEType(E_VIDEO_TYPE.VT_ZB);
            VideoDetail detail = new VideoDetail();
            detail.setSVideoKey("key_" + String.valueOf(i));
            detail.setEChannel(E_VIDOE_CHANNEL.VC_52JRJY);
            detail.setSImgUrl(imageUrl);
            detail.setSTitle("测试标题");
            detail.setIFaverateTime(1508137381);
            videoBlock.setStVideoDetail(detail);
        }
        return videoBlockList;
    }

    public static UpdateFaverateVideoRsp createTestUpdateRsp() {
        UpdateFaverateVideoRsp rsp = new UpdateFaverateVideoRsp();
        return rsp;
    }
}
