package com.sscf.investment.media;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.media.adapter.AudioListAdapter;
import com.sscf.investment.media.inter.OnAudioActionCallback;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.socialize.ShareDialog;
import com.sscf.investment.teacherYan.AudioPlayedManager;
import com.sscf.investment.teacherYan.manager.MediaControl;
import com.sscf.investment.teacherYan.viewholder.PlayViewHolder;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import java.util.List;

import BEC.WxWalkRecord;

/**
 * Created by LEN on 2018/4/25.
 */
@Route("TeacherYanAudioPlayerActivity")
public class TeacherYanAudioPlayerActivity extends BaseFragmentActivity implements
        View.OnClickListener, OnAudioActionCallback, PlayViewHolder.AudioAction {

    private static final String TAG = TeacherYanAudioPlayerActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mTvTitle, mTvReadNum;
    private View mViewActionBg;
    private LinearLayoutManager mLayoutManager;

    private AudioListAdapter mAdapter;
    private List<WxWalkRecord> mWordRecords;

    private int mPlayingID;
    private PlayViewHolder mPlayViewHolder;
    private int actionBarHeight;
    private MediaControl mMediaControl;

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            int iResult = 0;
            int position = mLayoutManager.findFirstVisibleItemPosition();
            View firstVisiableChildView = mLayoutManager.findViewByPosition(position);
            if (position > 0) {
                mViewActionBg.setAlpha(1);
                return;
            }
            int itemHeight = firstVisiableChildView.getHeight();
            int itemTop = firstVisiableChildView.getTop();
            int iposition = position * itemHeight;
            iResult = iposition - itemTop;
            float alpha = iResult * 1.0f / actionBarHeight * 1.0f;
            alpha = alpha > 1 ? 1 : alpha;
            mViewActionBg.setAlpha(alpha);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarHeight = DeviceUtil.dip2px(this, getResources().getDimension(R.dimen.actionbar_height));
        mPlayingID = getIntent().getExtras().getInt(CommonConst.EXTRA_PLAY_AUDIO);
        mMediaControl = new MediaControl(this);
        mMediaControl.setCallBack(this);
        setContentView(R.layout.activity_teacher_yan_audio_player);
        initView();
    }

    private void initView() {
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.actionbar_share).setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.actionbar_title);
        mTvReadNum = (TextView) findViewById(R.id.actionbar_listener_num);
        mViewActionBg = findViewById(R.id.viewActionBg);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AudioListAdapter(this, mMediaControl);
        initHeaderView();
        mAdapter.setPlayID(mPlayingID);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        initData();
    }

    private void initHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_audio_player, null, false);
        mAdapter.setHeaderView(headerView);
        mPlayViewHolder = new PlayViewHolder(this, headerView);
        mPlayViewHolder.setMediaControl(mMediaControl);
        mPlayViewHolder.setAudioAction(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initData() {
        mWordRecords = DengtaApplication.getApplication().getDataCacheManager().getWalkRecords();
        if (null != mWordRecords) {
            mAdapter.setListData(mWordRecords);
        }
        playAudio();
    }

    private WxWalkRecord getCurrentItem() {
        for (int i = 0 ; i < mWordRecords.size() ; i++) {
            if (mWordRecords.get(i).getIID() == mPlayingID) {
                return mWordRecords.get(i);
            }
        }
        return null;
    }

    public void setPlayingID(int mPlayingID) {
        this.mPlayingID = mPlayingID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaControl.pause();//修改mediacontrol就可以驱动ui上的变化
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaControl.stopAudio();
        mMediaControl.release();
        if (null != mRecyclerView) {
            mRecyclerView.removeOnScrollListener(mOnScrollListener);
        }
        if (null != mPlayViewHolder) {
            mPlayViewHolder.onDestroy();
        }
    }

    @Override
    public void onPrepared() {
        mTvTitle.setText(getCurrentItem().getSTitle());
        mTvReadNum.setText(StringUtil.getAmountString(Integer.valueOf(getCurrentItem().getINumber()), 1));
        mPlayViewHolder.setDuration(mMediaControl.getDuration());
    }

    @Override
    public void onAudioPaused() {
        mPlayViewHolder.onAudioPaused();
        mAdapter.setPaused(true);
    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onStartPlay() {
        runOnUiThread(() ->{
            mPlayViewHolder.startPlayAudio();
            mAdapter.setPaused(false);
        });
    }

    @Override
    public void onPlayFromPause() {
        mAdapter.setPaused(false);
        mPlayViewHolder.onPlayFromPaused();
    }

    @Override
    public void onPlayComplete() {
        mAdapter.setPaused(true);
        mPlayViewHolder.onAudioPaused();
    }

    private void playAudio() {
        DengtaApplication.getApplication().defaultExecutor.execute(() -> {
            AudioPlayedManager.addPlayedList(getCurrentItem());
            mMediaControl.playAudio(getCurrentItem().getSUrl());
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_share:
                getShareDialog().showShareDialog(getShareParams());
                break;
        }
    }

    private ShareDialog mShareDialog;

    private ShareDialog getShareDialog() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(this, ShareType.MOMENTS);
        }
        return mShareDialog;
    }

    public ShareParams getShareParams() {
        ShareParams params = new ShareParams();

        final String audioPlayerUrl = DengtaApplication.getApplication().
                getUrlManager().getTeacherYanAudioPlayerUrl(String.valueOf(mPlayingID));
        params.url = audioPlayerUrl;
        params.imageUrl = DengtaApplication.getApplication().getUrlManager().getShareIconUrl();
        params.title = getCurrentItem().getSTitle();
        // 分享时刻最新价/涨跌幅
        return params;
    }

    private int getPlayedIndex() {
        for (int i = 0 ; i < mWordRecords.size() ; i++) {
            if (mWordRecords.get(i).getIID() == mPlayingID) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public String nextUrl() {
        int index = getPlayedIndex();
        if (index == mWordRecords.size() - 1) {
            index = 0;
            if (mAdapter.isNeedShare(index)){
                index++;
            }
        }else{
            index++;
        }
        mPlayingID = mWordRecords.get(index).getIID();
        AudioPlayedManager.addPlayedList(mWordRecords.get(index));
        mAdapter.setPlayID(mPlayingID);
        mAdapter.notifyDataSetChanged();
        return mWordRecords.get(index).getSUrl();
    }

    @Override
    public String preUrl() {
        int index = getPlayedIndex();
        if (index == 0) {

            index = mWordRecords.size() - 1;
        }else{
            index--;
            if (mAdapter.isNeedShare(index)) {
                index = mWordRecords.size() - 1;
            }
        }
        mPlayingID = mWordRecords.get(index).getIID();
        AudioPlayedManager.addPlayedList(mWordRecords.get(index));
        mAdapter.setPlayID(mPlayingID);
        mAdapter.notifyDataSetChanged();
        return mWordRecords.get(index).getSUrl();
    }
}
