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
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.teacherYan.AudioPlayedManager;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import java.io.File;

import BEC.WxWalkRecord;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/19.
 */

public class TeacherYanWordAdapter extends CommonRecyclerViewAdapter {

    private static final String TAG = TeacherYanWordAdapter.class.getSimpleName();

    private int mColorBase;
    private int mColorTitle;

    public TeacherYanWordAdapter(Context context) {
        super(context);
        mColorTitle = ContextCompat.getColor(mContext, R.color.default_text_color_100);
        mColorBase = ContextCompat.getColor(mContext, R.color.default_text_color_60);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new RecordHolder(mInflater.inflate(R.layout.item_teacher_yan_record, parent, false));
    }

    final class RecordHolder extends CommonRecyclerViewAdapter.CommonViewHolder {

       @BindView(R.id.tvAudioTitle) TextView mTvTitle;
       @BindView(R.id.tvAudioListenerNum) TextView mTvListenerNum;
       @BindView(R.id.tvUploadTime) TextView mTvTime;
       @BindView(R.id.ivLocked) ImageView mIvLocked;
       @BindView(R.id.vBg) View vBg;
       private boolean isNeedShared;

        public RecordHolder(View itemView) {
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
            isNeedShared = getAdapterPosition() == 0 && !isItemShared(String.valueOf(item.getIID()));
            if (isNeedShared) {
                mIvLocked.setImageResource(R.drawable.icon_teacher_yan_lock);
                vBg.setVisibility(View.VISIBLE);
            }else {
                mIvLocked.setImageResource(R.drawable.icon_teacher_yan_play);
                vBg.setVisibility(View.GONE);
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
            if (isNeedShared) {
                if (null != mContext){
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_SPEECH_SHARE_CLICK);
                    getShareDialog().showShareDialog(getShareParams());
                }
            } else {
                playAudio();
                StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_SPEECH_JUMP);
            }
        }

        private ShareDialog mShareDialog;

        private ShareDialog getShareDialog() {
            if (mShareDialog == null) {
                mShareDialog = new ShareDialog((Activity) mContext, ShareType.INTENT_MOMENTS);
            }
            mShareDialog.setShareListener( (state, plat) ->  {
                if (state == IShareManager.STATE_SUCCESS){//分享chengg
                    isNeedShared = true;
                    SettingPref.putString(SettingConst.KEY_SHARED_AUDIO_ID,
                            String.valueOf(((WxWalkRecord)mItemData).getIID()));
                    notifyItemChanged(0);
                    StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_SPEECH_SHARE_SUCCESS);
                }
            });
            return mShareDialog;
        }
        public ShareParams getShareParams() {
            ShareParams params = new ShareParams();
            WxWalkRecord record = (WxWalkRecord) mItemData;
            final String audioPlayerUrl = DengtaApplication.getApplication().getUrlManager().
                    getTeacherYanAudioPlayerUrl(String.valueOf(record.getIID()));
            params.title = mContext.getResources().getString(R.string.share_title_audio_player);
            final File file = new File(FileUtil.getExternalFilesDir(mContext, "pic"), "share_teacher.jpg");
            FileUtil.saveInputStreamToFile(mContext.getResources().openRawResource(R.raw.icon_share_teacher_yan), file);
            params.shareFile = file;
            params.url = audioPlayerUrl;
            return params;
        }

        private void playAudio() {
            WxWalkRecord record = (WxWalkRecord) mItemData;
            CountNumUtil.readAudioRecord(record.getIID());
            if (!AudioPlayedManager.isPlayed(record)) {
                AudioPlayedManager.addPlayedList(record);
            }
            CommonBeaconJump.showTeacherYanAudioPlayer(mContext, record.getIID());
            notifyItemChanged(getAdapterPosition());
        }
    }
}
