package com.sscf.investment.media;

import android.os.Bundle;
import android.widget.VideoView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

/**
 * Created by LEN on 2018/4/27.
 */

public class VideoActivity extends BaseFragmentActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoView = (VideoView) findViewById(R.id.videoView);
    }
}
