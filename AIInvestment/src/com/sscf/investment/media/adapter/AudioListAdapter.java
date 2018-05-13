package com.sscf.investment.media.adapter;

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
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.media.TeacherYanAudioPlayerActivity;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.teacherYan.AudioPlayedManager;
import com.sscf.investment.teacherYan.manager.MediaControl;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import BEC.WxWalkRecord;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/25.
 */

public class AudioListAdapter extends CommonRecyclerViewAdapter{

    private int mPlayindID;

    private MediaControl mMediaControl;

    private boolean isPaused;


    private int mColorBase;
    private int mColorTitle;

    public void setPlayID(int playIndex) {
        this.mPlayindID = playIndex;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        notifyDataSetChanged();
    }

    public AudioListAdapter(Context context, MediaControl pMediaControl) {
        super(context);
        this.mMediaControl = pMediaControl;
        mColorTitle = ContextCompat.getColor(mContext, R.color.default_text_color_100);
        mColorBase = ContextCompat.getColor(mContext, R.color.default_text_color_60);
    }

    public boolean isNeedShare(int realPosition) {
        if (realPosition == 0) {
            WxWalkRecord wxWalkRecord = (WxWalkRecord) getItemData(realPosition);
            return !isItemShared(String.valueOf(wxWalkRecord.getIID()));
        }
        return false;
    }

    private boolean isItemShared(String id) {
        String sharedId = SettingPref.getString(SettingConst.KEY_SHARED_AUDIO_ID, "");
        if (TextUtils.isEmpty(sharedId)) {
            return false;
        }else {
            return sharedId.equals(id);
        }
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new AudioHolder(mInflater.inflate(R.layout.item_teacher_yan_audio_player, parent, false));
    }

    final class AudioHolder extends CommonViewHolder {

        @BindView(R.id.ivPlaying) ImageView mIvAudioPlaying;
        @BindView(R.id.tvAudioTitle) TextView mTvTitle;
        @BindView(R.id.tvAudioListenerNum) TextView mTvListenerNum;
        @BindView(R.id.tvUploadTime) TextView mTvTime;
        private boolean isNeedShared;


        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            WxWalkRecord item = (WxWalkRecord) itemData;
            mTvTitle.setTextColor(AudioPlayedManager.isPlayed(item) ? mColorBase : mColorTitle);
            mTvTitle.setText(item.getSTitle());
            mTvListenerNum.setText(StringUtil.getAmountString(item.getINumber(), 1));
            mTvTime.setText(TimeUtils.transForDate(item.getSInformationTime()));


            if (isPaused) {
                mIvAudioPlaying.setImageResource(R.drawable.icon_stop_song);
            }else {
                mIvAudioPlaying.setImageResource(mPlayindID == item.getIID() ?
                        R.drawable.icon_playing : R.drawable.icon_stop_song);
            }
            isNeedShared = mRealPosition == 0 && !isItemShared(String.valueOf(item.getIID()));
            if (isNeedShared) {
                mIvAudioPlaying.setImageResource(R.drawable.icon_teacher_yan_lock);
            }
        }

        private boolean isItemShared(String id) {
            String sharedId = SettingPref.getString(SettingConst.KEY_SHARED_AUDIO_ID, "");
            if (TextUtils.isEmpty(sharedId)) {
                return false;
            }else {
                return sharedId.equals(id);
            }
        }

        @Override
        public void onItemClicked() {
            WxWalkRecord item = (WxWalkRecord) mItemData;
            if (isNeedShared) {
                getShareDialog().showShareDialog(getShareParams());
                return;
            }
            if (isPaused && item.getIID() == mPlayindID ) {
                mMediaControl.playFromPause();
            }else if (!isPaused && item.getIID() == mPlayindID) {
                mMediaControl.pause();
            } else {
                mPlayindID = item.getIID();
                ((TeacherYanAudioPlayerActivity)mContext).setPlayingID(mPlayindID);
                mMediaControl.playAudio(item.getSUrl());
                AudioPlayedManager.addPlayedList(item);
            }
        }

        private ShareDialog mShareDialog;

        private ShareDialog getShareDialog() {
            if (mShareDialog == null) {
                mShareDialog = new ShareDialog((Activity) mContext, ShareType.MOMENTS);
            }
            mShareDialog.setShareListener( (state, plat) ->  {
                if (state == IShareManager.STATE_SUCCESS){//分享chengg
                    isNeedShared = true;
                    SettingPref.putString(SettingConst.KEY_SHARED_AUDIO_ID,
                            String.valueOf(((WxWalkRecord)mItemData).getIID()));
                    notifyItemChanged(1);
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_SPEECH_SHARE_SUCCESS);
                }
            });
            return mShareDialog;
        }

        public ShareParams getShareParams() {
            ShareParams params = new ShareParams();
            WxWalkRecord record = (WxWalkRecord) mItemData;
            final String stockDetailUrl = DengtaApplication.getApplication().getUrlManager().
                    getTeacherYanAudioPlayerUrl(String.valueOf(record.getIID()));
            params.url = stockDetailUrl;
            params.imageUrl = DengtaApplication.getApplication().getUrlManager().getShareIconUrl();
            params.title = record.getSTitle();
            return params;
        }
    }

}
