package com.sscf.investment.teacherYan.manager;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Message;
import android.text.TextUtils;

import com.sscf.investment.media.inter.OnAudioActionCallback;
import com.sscf.investment.sdk.utils.DtLog;

import java.io.IOException;

/**
 * Created by LEN on 2018/4/24.
 */

public class MediaControl implements android.os.Handler.Callback, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    private static final String TAG = "MediaControl";

    private static final int INIT_PLAY = 0;
    private static final int START_PLAY = 1;
    private static final int PAUSE_PLAY = 2;
    private static final int STOP_PLAY = 3;
    private static final int RELEASE = 4;

    private Context mContext;

    private MediaPlayer mMediaPlayer;

    private long duration;

    private OnAudioActionCallback mCallBack;

    public OnAudioActionCallback getCallBack() {
        return mCallBack;
    }

    public void setCallBack(OnAudioActionCallback mCallBack) {
        this.mCallBack = mCallBack;
    }

    public long getDuration() {
        return duration;
    }

    public MediaControl(Context mContext) {
        this.mContext = mContext;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    /**
     * 播放多媒体
     * */
    public synchronized void playAudio(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mMediaPlayer.reset();//每次播放先reset，可多次reset
        try{
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     * */
    public void stopAudio() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        if (null != mCallBack) {
            mCallBack.onStopped();
        }
    }

    public void pause() {
        mMediaPlayer.pause();
        if (null != mCallBack) {
            mCallBack.onAudioPaused();
        }
    }

    public void playFromPause() {
        mMediaPlayer.start();
        if (null != mCallBack) {
            mCallBack.onPlayFromPause();
        }
    }

    public void release() {
        mMediaPlayer.release();
    }

    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case INIT_PLAY:
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case START_PLAY:
                mMediaPlayer.start();
                break;
            case PAUSE_PLAY:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case STOP_PLAY:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }
                break;
            case RELEASE:
                mMediaPlayer.release();
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        duration = mp.getDuration();
        if (null != mCallBack) {
            mCallBack.onPrepared();
        }
        mMediaPlayer.start();
        if (null != mCallBack) {
            mCallBack.onStartPlay();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        DtLog.e(TAG, "what = " + what + " extra = " + extra);
        return false;
    }

    public void setOnCompleteListener(MediaPlayer.OnCompletionListener onCompleteListener) {
        mMediaPlayer.setOnCompletionListener(onCompleteListener);
    }

    public void setProgress(int progress) {
        mMediaPlayer.seekTo(progress);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (null != mCallBack) {
            mCallBack.onPlayComplete();
        }
    }
}
