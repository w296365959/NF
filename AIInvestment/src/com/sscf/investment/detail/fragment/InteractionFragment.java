package com.sscf.investment.detail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.thoth.Message;
import com.sscf.investment.R;
import com.sscf.investment.detail.interaction.DongmiQaListAdapter;
import com.sscf.investment.detail.interaction.FeedListAdapter;
import com.sscf.investment.detail.interaction.InteractionGroupAdapter;
import com.sscf.investment.detail.manager.SecurityDetailRequestManager;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.widget.BaseFragment;


import java.util.ArrayList;
import java.util.Map;

import BEC.DongmiQaDetail;
import BEC.DongmiQaListRsp;
import BEC.FeedItem;
import BEC.FeedUserBaseInfo;
import BEC.GetFeedListRsp;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class InteractionFragment extends BaseFragment implements DataSourceProxy.IRequestCallback {

    private String mDtSecCode;
    private String mSecName;
    private DongmiQaGroup mDongmiQaGroup;
    private FeeGroup mFeedGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction, container, false);
        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(CommonConst.KEY_SEC_CODE);
            mSecName = args.getString(CommonConst.KEY_SEC_NAME);
        }
        initView(view);
        return view;
    }

    private void initView(View root) {
        mDongmiQaGroup = new DongmiQaGroup(getContext(), root.findViewById(R.id.dongmi_qa));
        mDongmiQaGroup.retry();
        mFeedGroup = new FeeGroup(getContext(), root.findViewById(R.id.feed));
        mFeedGroup.retry();
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_DONGMI_QA_LIST:
                handleDongmiQaList(success, data);
                break;
            case EntityObject.ET_GET_FEED_LIST:
                handleFeedList(success, data);
                break;
                default:
        }
    }

    private void handleDongmiQaList(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            DongmiQaListRsp rsp = (DongmiQaListRsp) data.getEntity();
            ThreadUtils.runOnUiThread(() -> {
                mDongmiQaGroup.setData(rsp.getVDongmiQaDetail());
            });
        } else {
            ThreadUtils.runOnUiThread(() -> {
                mDongmiQaGroup.fail();
            });
        }
    }

    private void handleFeedList(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            GetFeedListRsp rsp = (GetFeedListRsp) data.getEntity();
            ThreadUtils.runOnUiThread(() -> {
                mFeedGroup.putUserBaseInfos(rsp.getMFeedUserBaseInfo());
                mFeedGroup.setData(rsp.getVFeedItem());
            });
        } else {
            ThreadUtils.runOnUiThread(() -> {
                mFeedGroup.fail();
            });
        }
    }

    private class DongmiQaGroup extends InteractionGroup<DongmiQaDetail> {

        DongmiQaGroup(Context context, View groupView) {
            super(context, groupView, 3, R.string.dongmi_qa, new DongmiQaListAdapter(context));
        }

        @Override
        public void more() {
            if(!TextUtils.isEmpty(mDtSecCode) && !TextUtils.isEmpty(mSecName)) {
                CommonBeaconJump.showCommonWebActivity(getContext(), WebUrlManager.getInstance().getBoardSecretary(mDtSecCode, mSecName));
            }
        }

        @Override
        public void retry() {
            super.retry();
            SecurityDetailRequestManager.requestDongmiQaList(mDtSecCode, InteractionFragment.this);
        }
    }

    private class FeeGroup extends InteractionGroup<FeedItem> {

        FeeGroup(Context context, View groupView) {
            super(context, groupView, 4, R.string.user_comments, new FeedListAdapter(context));
        }

        @Override
        public void more() {
            CommonBeaconJump.showCommentList(mContext, mDtSecCode, mSecName);
        }

        public void putUserBaseInfos(Map<Long, FeedUserBaseInfo> userBaseInfoMap) {
            FeedListAdapter adapter = (FeedListAdapter) mAdapter;
            adapter.putUserBaseInfos(userBaseInfoMap);
        }

        @Override
        public void retry() {
            super.retry();
            SecurityDetailRequestManager.requestSimpleFeedList(mDtSecCode, InteractionFragment.this);
        }
    }

    private abstract class InteractionGroup<T extends Message> implements View.OnClickListener {

        protected Context mContext;
        private int mMaxCount;
        private ListView mListView;
        protected InteractionGroupAdapter mAdapter;
        private View mEmptyView;
        private View mLoadingView;
        private View mFailView;

        InteractionGroup(Context context, View groupView, int maxCount, int titleRes, InteractionGroupAdapter adapter) {
            mContext = context;
            mMaxCount = maxCount;
            ((TextView) groupView.findViewById(R.id.title)).setText(titleRes);
            groupView.findViewById(R.id.expand_icon).setOnClickListener(this);
            groupView.findViewById(R.id.expand_text).setOnClickListener(this);
            mListView = (ListView) groupView.findViewById(R.id.list);
            mListView.setFocusable(false);
            mLoadingView = groupView.findViewById(R.id.loading_layout);
            mEmptyView = groupView.findViewById(R.id.empty_view);
            mFailView = groupView.findViewById(R.id.fail_retry);
            mFailView.setOnClickListener(this);
            mAdapter = adapter;
            mListView.setAdapter(adapter);
        }

        public void fail() {
            mLoadingView.setVisibility(View.GONE);
            mFailView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }

        public void setData(ArrayList<T> datas) {
            mLoadingView.setVisibility(View.GONE);
            mFailView.setVisibility(View.GONE);
            if(datas == null || datas.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                while(datas.size() > mMaxCount) {
                    datas.remove(datas.size() - 1);
                }
                mAdapter.setData(datas);
            }
        }

        public abstract void more();

        public void retry() {
            mLoadingView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mFailView.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.expand_icon:
                case R.id.expand_text:
                    more();
                    break;
                case R.id.fail_retry:
                    retry();
                    break;
                default:
            }
        }
    }
}
