package com.sscf.investment.media.inter;

/**
 * Created by LEN on 2018/5/2.
 */

public interface OnAudioActionCallback {

    void onPrepared();

    void onAudioPaused();

    void onStopped();

    void onStartPlay();

    void onPlayFromPause();

    void onPlayComplete();
}
