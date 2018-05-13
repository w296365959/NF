package com.sscf.investment.teacherYan.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.widgt.RoundImageView;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.teacherYan.manager.MediaControl;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import BEC.WxTalkFree;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/20.
 */

public class TalkFreeAdapter extends CommonRecyclerViewAdapter implements MediaPlayer.OnCompletionListener {

    private int mPlayIndex = -1;
    private MediaControl mMediaControl;

    public TalkFreeAdapter(Context context) {
        super(context);
        mMediaControl = new MediaControl(context);
        mMediaControl.setOnCompleteListener(this);
    }

    public void reset() {
        mPlayIndex = -1;
        mMediaControl.stopAudio();
        notifyDataSetChanged();
    }

    public void release() {
        mMediaControl.release();
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new TalkFreeHolder(mInflater.inflate(R.layout.item_teacher_yan_talk_free, parent, false));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayIndex = -1;
        notifyDataSetChanged();
    }

    final class TalkFreeHolder extends CommonRecyclerViewAdapter.CommonViewHolder implements View.OnClickListener{

        @BindView(R.id.ivAskUserAvator) RoundImageView mIvUserAvator;
        @BindView(R.id.tvAskUserName) TextView mTvAskUserName;
        @BindView(R.id.tvAskTime) TextView mTvAskTime;
        @BindView(R.id.tvQuestionContent) TextView mTvQuestionContent;
        @BindView(R.id.ivPlayVoice) ImageView mIvPlayVoice;
        @BindView(R.id.llVoicePlay) View mVoicePlayView;

        public TalkFreeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            WxTalkFree item = (WxTalkFree) itemData;
            mIvUserAvator.setImageResource(R.drawable.default_consultant_face);
            ImageLoaderUtils.getImageLoader().displayImage(item.getSQuestionHeadImage(), mIvUserAvator);
            mTvAskUserName.setText(item.getSQuestionName());
            mTvAskTime.setText(TimeUtils.transForDate(item.getSQuestionDate()));
            mTvQuestionContent.setText(item.getSQuestionContent());
            if (mRealPosition != mPlayIndex){
                mIvPlayVoice.setImageResource(R.drawable.voice_play_3);
            } else {
                mIvPlayVoice.setImageResource(R.drawable.voice_play_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) mIvPlayVoice.getDrawable();
                animationDrawable.start();
            }
            mVoicePlayView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mPlayIndex == mRealPosition) {//点击相同项
                mPlayIndex = -1;
                notifyDataSetChanged();
                mMediaControl.stopAudio();
            }else {//有在播放，下一首歌曲
                mPlayIndex = mRealPosition;
                notifyDataSetChanged();
                DengtaApplication.getApplication().defaultExecutor.execute(()->{
                    mMediaControl.playAudio(((WxTalkFree)mItemData).getSExpertAnswer());
                });
            }
            CountNumUtil.readTalkFree(((WxTalkFree)mItemData).getIID());
            StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_ANSWER_PLAY_CLICK);
        }
    }
}
