package com.sscf.investment.teacherYan.viewholder;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.splash.TextureVideoView;
import com.sscf.investment.teacherYan.manager.MediaControl;
import com.sscf.investment.widget.BezierView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LEN on 2018/4/26.
 */

public class PlayViewHolder implements Handler.Callback, SeekBar.OnSeekBarChangeListener{

    private static final String TAG = PlayViewHolder.class.getSimpleName();

    private static final int BLUR_IMAGE_SUCCESS = 1;
    private static final int START_PLAY_AUDIO_FROM_PAUSE = 3;
    private static final int ANIMATION_UPDATE = 5;

    private Context mContext;
    private Handler mHandler;
    private boolean isPause;
    private int degree;
    private MediaControl mMediaControl;
    private AudioAction mAudioAction;

    @BindView(R.id.ivHeader) ImageView mIvHeader;
    @BindView(R.id.ivPlayPause) ImageView mIvPlayPause;
    @BindView(R.id.ivNextSong) ImageView mIvNextSong;
    @BindView(R.id.ivPreSong) ImageView mIvPreSong;
    @BindView(R.id.tvCurrentTime) TextView mTvCurrentTime;
    @BindView(R.id.tvTotalTime) TextView mTvTotalTime;
    @BindView(R.id.llHearder) View vBg;
    @BindView(R.id.seekBar) SeekBar mSeekbar;
    @BindView(R.id.bezier)
    BezierView mBezierView;

    public PlayViewHolder(Context context, View view) {
        mContext = context;
        mHandler = new Handler(this);
        ButterKnife.bind(this, view);
        init();
    }

    public void setMediaControl(MediaControl mMediaControl) {
        this.mMediaControl = mMediaControl;
    }

    public void init() {
        mSeekbar.setOnSeekBarChangeListener(this);
        DengtaApplication.getApplication().defaultExecutor.execute(this::getBlurImage);
    }

    private void getBlurImage() {
        BitmapDrawable drawable = new BitmapDrawable(BitmapUtils.blurBitmap(mContext,
                BitmapUtils.decodeResource(mContext.getResources(), R.drawable.teacher_yan_avator)));
        mHandler.obtainMessage(BLUR_IMAGE_SUCCESS, drawable).sendToTarget();
    }

    public void setDuration(long duration) {
        if (duration > 0){
            mDuration = duration;
            mTvTotalTime.setText(getFormatTime(duration));
            mSeekbar.setMax((int) duration);
        }
    }

    private long mDuration;

    private String getFormatTime(long duration) {
        if (duration > 0) {
            long minutes = duration / 1000 / 60;
            long seconds = (duration - minutes * 1000 * 60) / 1000;
            return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }
        return "";
    }

    public void startPlayAudio() {
        isPause = false;
        startAnima();
        mIvPlayPause.setImageResource(R.drawable.icon_playing);
    }

    private void startAnima() {
        mHandler.removeMessages(ANIMATION_UPDATE);
        degree = 0;
        mHandler.obtainMessage(ANIMATION_UPDATE).sendToTarget();
        mBezierView.startAnimation();
    }

    private void resumeAnima() {
        mHandler.obtainMessage(ANIMATION_UPDATE).sendToTarget();
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        switch (what){
            case BLUR_IMAGE_SUCCESS:
                vBg.setBackground((Drawable) msg.obj);
                break;
            case ANIMATION_UPDATE:
                if (isPause){
                    return true;
                }
                long progress = mMediaControl.getCurrentPosition();
                if (progress <= mDuration) {
                    mSeekbar.setProgress((int) progress);
                    mTvCurrentTime.setText(getFormatTime(progress));
                }
                mIvHeader.setRotation(degree % 360);
                degree += 1;
                mHandler.sendEmptyMessageDelayed(ANIMATION_UPDATE, 7 * 1000 / 360);
                break;
            case START_PLAY_AUDIO_FROM_PAUSE:
                isPause = false;
                if (null != mMediaControl) {
                    mMediaControl.playFromPause();
                }
                break;
        }
        return true;
    }

    public void onAudioPaused() {
        isPause = true;
        mBezierView.stopAnimation();
        mIvPlayPause.setImageResource(R.drawable.icon_stop_song);
    }

    public void onPlayFromPaused() {
        isPause = false;
        mBezierView.startAnimation();
        mHandler.sendEmptyMessage(ANIMATION_UPDATE);
        mIvPlayPause.setImageResource(R.drawable.icon_playing);
    }

    @OnClick(R.id.ivPlayPause)
    public void playOnPauseAudio(View view) {
        if (isPause) {
            mMediaControl.playFromPause();
        }else {
            mMediaControl.pause();
        }
    }

    @OnClick(R.id.ivNextSong)
    public void nextSong(View view) {
        if (null != mAudioAction){
            mMediaControl.playAudio(mAudioAction.nextUrl());
        }
    }

    @OnClick(R.id.ivPreSong)
    public void preSong(View view) {
        if (null != mAudioAction){
            mMediaControl.playAudio(mAudioAction.preUrl());
        }
    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            mMediaControl.setProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        DtLog.e(TAG, "onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        DtLog.e(TAG, "onStopTrackingTouch");
    }

    public void setAudioAction(AudioAction mAudioAction) {
        this.mAudioAction = mAudioAction;
    }

    public interface AudioAction {
        String nextUrl();
        String preUrl();
    }
}
