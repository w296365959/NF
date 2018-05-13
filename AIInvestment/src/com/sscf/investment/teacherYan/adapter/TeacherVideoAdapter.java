package com.sscf.investment.teacherYan.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.tencent.smtt.sdk.TbsVideo;

import BEC.VideoInfo;
import BEC.WxWalkRecord;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/24.
 */

public class TeacherVideoAdapter extends CommonRecyclerViewAdapter {

    private static final String TAG = TeacherVideoAdapter.class.getSimpleName();

    private String playedList;
    private String[] mPlayedVideos;
    private int mColorBase;
    private int mColorTitle;

    public TeacherVideoAdapter(Context context) {
        super(context);
        mColorTitle = ContextCompat.getColor(mContext, R.color.default_text_color_100);
        mColorBase = ContextCompat.getColor(mContext, R.color.default_text_color_60);
        refreshPlayedList();
    }

    private void refreshPlayedList() {
        playedList = SettingPref.getString(SettingConst.K_VIDEO_PLAYED, "");
        mPlayedVideos = playedList.split("\\|");
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(mInflater.inflate(R.layout.item_teacher_video, parent, false));
    }

    final class VideoHolder extends CommonRecyclerViewAdapter.CommonViewHolder {

        @BindView(R.id.ivCurseCover) ImageView mIvCover;
        @BindView(R.id.tvTeacherVideoWatch) TextView mTvVideoWatch;//看点
        @BindView(R.id.tvTeacherVideoNumTitle) TextView mTvTeacherVideoNumTitle;
        @BindView(R.id.tvTeacherName) TextView mTvTeacherName;
        @BindView(R.id.ivNeedShare) ImageView mIvNeedShare;
        @BindView(R.id.tvVideoListenerNum) TextView mTvVideoWatchNum;
        @BindView(R.id.vBg) View vBg;
        private boolean isNeedShare;

        public VideoHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            VideoInfo videoInfo = (VideoInfo) itemData;
            if (!TextUtils.isEmpty(videoInfo.getSImage())){
                ImageLoaderUtils.getImageLoader().displayImage(videoInfo.getSImage(), mIvCover);
            }
            mTvTeacherVideoNumTitle.setTextColor(isPlayed(videoInfo) ? mColorBase : mColorTitle);
            mTvVideoWatch.setTextColor(isPlayed(videoInfo) ? mColorBase : mColorTitle);

            mTvVideoWatch.setText(videoInfo.getSWatch());
            mTvTeacherVideoNumTitle.setText(videoInfo.getSNumberPeriod() + videoInfo.getSColumnTitle());
            mTvTeacherName.setText(videoInfo.getSExpertTitle() + ":" + videoInfo.getSExpertName());
            mTvVideoWatchNum.setText(StringUtil.getAmountString(Integer.valueOf(videoInfo.getSReadCount()), 1));
            isNeedShare = mRealPosition == 0 && !isItemShared(String.valueOf(videoInfo.getIID()));
            mIvNeedShare.setImageResource(isNeedShare ? R.drawable.icon_teacher_yan_lock
                    : R.drawable.icon_teacher_yan_play);
            vBg.setVisibility(isNeedShare ? View.VISIBLE : View.INVISIBLE);
        }

        private boolean isItemShared(String id) {
            String sharedId = SettingPref.getString(SettingConst.KEY_SHARED_VIDEO_ID, "");
            DtLog.e(TAG, "get shared id = " + sharedId);
            if (TextUtils.isEmpty(sharedId)) {
                return false;
            }else {
                return sharedId.equals(id);
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (isNeedShare){
                if (null != mContext){
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_VIDEO_SHARE_CLICK);
                    getShareDialog().showShareDialog(getShareParams());
                }
            }else {
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_VIDEO_JUMP);
                VideoInfo mItem = (VideoInfo) mItemData;

                if (!isPlayed(mItem)) {
                    playedList += "|" + mItem.getIID();
                    SettingPref.putString(SettingConst.K_VIDEO_PLAYED, playedList);
                    refreshPlayedList();
                    notifyDataSetChanged();
                }

                CountNumUtil.readTeacherVideo(mItem.getIID());
                TbsVideo.openVideo(mContext, (mItem.getSUrl()));
            }
        }

        private boolean isPlayed(VideoInfo item) {
            for (int i = 0 ; i < mPlayedVideos.length ; i++) {
                if (mPlayedVideos[i].equals(String.valueOf(item.getIID()))) {
                    return true;
                }
            }
            return false;
        }

        private ShareDialog mShareDialog;

        private ShareDialog getShareDialog() {
            if (mShareDialog == null) {
                mShareDialog = new ShareDialog((Activity) mContext, ShareType.MOMENTS);
            }
            mShareDialog.setShareListener( (state, plat) ->  {
                if (state == IShareManager.STATE_SUCCESS){//分享chengg
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_VIDEO_SHARE_SUCCESS);
                    isNeedShare = false;
                    String id = String.valueOf(((VideoInfo)mItemData).getIID());
                    DtLog.e(TAG, "shared id is " + id);
                    SettingPref.putString(SettingConst.KEY_SHARED_VIDEO_ID, id);
                    notifyNormalItemChanged(1);
                }
            });
            return mShareDialog;
        }
        public ShareParams getShareParams() {
            ShareParams params = new ShareParams();
            VideoInfo videoInfo = (VideoInfo) mItemData;
            params.url = videoInfo.getSUrl();
            params.imageUrl = DengtaApplication.getApplication().getUrlManager().getShareIconUrl();
            params.title = videoInfo.getSWatch();
            // 分享时刻最新价/涨跌幅
            return params;
        }
    }
}
