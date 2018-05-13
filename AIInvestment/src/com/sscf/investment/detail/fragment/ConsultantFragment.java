package com.sscf.investment.detail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.ExclusiveConsultantActivity;
import com.sscf.investment.detail.OnReloadDataListener;
import com.sscf.investment.detail.view.ConsultantOpinionListLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.StateFrameLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import java.util.ArrayList;
import java.util.List;
import BEC.BEACON_STAT_TYPE;
import BEC.E_FEED_GROUP_TYPE;
import BEC.E_FEED_TYPE;
import BEC.FeedItem;
import BEC.GetFeedListReq;
import BEC.GetFeedListRsp;
import BEC.GetInvestAdvisorListReq;
import BEC.GetInvestAdvisorListRsp;
import BEC.InvestAdviseInfo;
import BEC.InvestAdviseInfoList;
import BEC.InvestAdvisor;

/**
 * Created by liqf on 2016/7/15.
 */
public class ConsultantFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, OnReloadDataListener {
    public static final String TAG = ConsultantFragment.class.getSimpleName();

    private StateFrameLayout mConsultantOpinionContainer;
    private ConsultantOpinionListLayout mConsultantOpinionListLayout;

    private StateFrameLayout mConsultantContainer;
    private ListView mListView;
    private ConsultantListAdapter mAdapter;
    private View mExpandIcon;
    private View mExpandText;

    private Handler mUiHandler = new Handler();

    private String mDtSecCode;
    private String mSecName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DtLog.d(TAG, "ConsultantFragment: onCreateView");
        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mSecName = args.getString(DengtaConst.KEY_SEC_NAME);
        }

        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_consultant, container, false);

        mConsultantOpinionContainer = (StateFrameLayout) contextView.findViewById(R.id.consultant_opinion_container);
        mConsultantOpinionContainer.setState(DengtaConst.UI_STATE_LOADING);
        mConsultantOpinionContainer.setOnReloadDataListener(this::loadFeedData);
        mConsultantOpinionListLayout = (ConsultantOpinionListLayout) contextView.findViewById(R.id.consultant_opinion_list_layout);

        mConsultantContainer = (StateFrameLayout) contextView.findViewById(R.id.consultant_container);
        mConsultantContainer.setState(DengtaConst.UI_STATE_LOADING);
        mConsultantContainer.setOnReloadDataListener(this::loadConsultantData);
        mListView = (ListView) contextView.findViewById(R.id.consultant_listview);
        mExpandIcon = contextView.findViewById(R.id.expand_icon);
        mExpandText = contextView.findViewById(R.id.expand_text);

        contextView.findViewById(R.id.title_bar).setOnClickListener(view -> {
            if (mExpandText.getVisibility() == View.VISIBLE) {
                ExclusiveConsultantActivity.show(getContext(), mDtSecCode, mSecName);
            }
        });

        loadData();

        return contextView;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_NEWS);
        helper.setKey(StatConsts.STOCK_INFO_CONSULTANT_OPINION);
        return helper;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        DtLog.d(TAG, "onHiddenChanged = " + hidden + "-----------" + toString());
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
        if (!hidden) { //切换页面的时候如果需要重新拉取则放开以下的注释
//            loadData();
        }
    }

    private void loadData() {
        loadConsultantData();

        loadFeedData();
    }

    private void loadConsultantData() {
        mConsultantContainer.setState(DengtaConst.UI_STATE_LOADING);
        GetInvestAdvisorListReq req = new GetInvestAdvisorListReq();
        req.setSDtSecCode(mDtSecCode);
        DataEngine.getInstance().request(EntityObject.ET_GET_CONSULTANT, req, this);
    }

    private void loadFeedData() {
        mConsultantOpinionContainer.setState(DengtaConst.UI_STATE_LOADING);
        GetFeedListReq req = new GetFeedListReq();
        req.setESelfType(E_FEED_TYPE.E_FT_INVEST_STOCK);
        req.setEFeedGroupType(E_FEED_GROUP_TYPE.E_FGT_SEC);
        req.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        req.setSStartFeedId("");
        req.setIDirection(0);
        req.setSDtSecCode(mDtSecCode);
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED_LIST, req, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DtLog.d(TAG, "ConsultantFragment: onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DtLog.d(TAG, "ConsultantFragment: onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DtLog.d(TAG, "ConsultantFragment: onDetach");
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        int entityType = entity.getEntityType();
        if (!success) {
            DtLog.w(TAG, "callback requestBriefInfo faild");
            onLoadFailed(entityType);
            return;
        }

        switch (entityType) {
            case EntityObject.ET_GET_CONSULTANT:
                final GetInvestAdvisorListRsp rsp = (GetInvestAdvisorListRsp) entity.getEntity();
                InvestAdviseInfoList adviseInfoList = rsp.getStInvestAdviseInfoList();
                final ArrayList<InvestAdviseInfo> adviseInfos = adviseInfoList.getVInvestAdviseInfo();
                if (adviseInfos != null) {
                    mUiHandler.post(() -> {
                        if (isAdded()) {
                            handleGetConsultantList(adviseInfos);
                        }
                    });
                }
                break;
            case EntityObject.ET_GET_FEED_LIST:
                final GetFeedListRsp getFeedListRsp = (GetFeedListRsp) entity.getEntity();
                mUiHandler.post(() -> {
                    if (isAdded()) {
                        handleGetFeedList(getFeedListRsp);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void handleGetFeedList(GetFeedListRsp getFeedListRsp) {
        ArrayList<FeedItem> feedItems = getFeedListRsp.getVFeedItem();
        boolean hasContent = feedItems.size() > 0;
        if (hasContent) {
            mExpandIcon.setVisibility(View.VISIBLE);
            mExpandText.setVisibility(View.VISIBLE);
        } else {
            mExpandIcon.setVisibility(View.INVISIBLE);
            mExpandText.setVisibility(View.INVISIBLE);
        }
        mConsultantOpinionContainer.setState(hasContent ? DengtaConst.UI_STATE_NORMAL : DengtaConst.UI_STATE_NO_CONTENT);
        mConsultantOpinionListLayout.setData(getFeedListRsp);
    }

    private void onLoadFailed(int entityType) {
        mUiHandler.post(() -> {
            switch (entityType) {
                case EntityObject.ET_GET_CONSULTANT:
                    mConsultantContainer.setState(DengtaConst.UI_STATE_FAILED_RETRY);
                    mExpandIcon.setVisibility(View.INVISIBLE);
                    mExpandText.setVisibility(View.INVISIBLE);
                    break;
                case EntityObject.ET_GET_FEED_LIST:
                    mConsultantOpinionContainer.setState(DengtaConst.UI_STATE_FAILED_RETRY);
                    break;
                default:
                    break;
            }
        });
    }

    private void handleGetConsultantList(List<InvestAdviseInfo> adviseInfos) {
        boolean hasContent = adviseInfos.size() > 0;
        mConsultantContainer.setState(hasContent ? DengtaConst.UI_STATE_NORMAL : DengtaConst.UI_STATE_NO_CONTENT);

        if (mConsultantContainer.getState() == DengtaConst.UI_STATE_NORMAL) {
            mListView.setFocusable(false); //这一行可以解决listview抢焦点把自己置于屏幕中央的问题

            mAdapter = new ConsultantListAdapter(getContext());
            mAdapter.setListData(adviseInfos);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onReloadData() {
        loadData();
    }

    public static class ConsultantListAdapter extends BaseAdapter {
        private final Context mContext;
        private List<InvestAdviseInfo> mAdviseInfos = new ArrayList<>();

        private LayoutInflater mInflater;

        private DisplayImageOptions mDefaultOptions;

        public ConsultantListAdapter(Context context) {
            mContext = context;
            mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
        }

        @Override
        public int getCount() {
            return mAdviseInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mAdviseInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            DtLog.d(TAG, "getView(): position = " + position);
            if (isEmpty()) {
                return null;
            }

            InvestAdviseInfo adviseInfo = mAdviseInfos.get(position);

            if (mInflater == null) {
                mInflater = LayoutInflater.from(mContext);
            }

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.consultant_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mFace = (ImageView) convertView.findViewById(R.id.face);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
                viewHolder.mSummary = (TextView) convertView.findViewById(R.id.summary);
                viewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
                viewHolder.mSource = (TextView) convertView.findViewById(R.id.source);
                viewHolder.mName = (TextView) convertView.findViewById(R.id.name);

                viewHolder.mPosition = position;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.mPosition = position;
            }

            viewHolder.mTitle.setText(adviseInfo.getSQuestion());
            viewHolder.mSummary.setText(adviseInfo.getSAnwser());
            InvestAdvisor investAdvisor = adviseInfo.getStInvestAdvisor();
            viewHolder.mName.setText(investAdvisor.getSName());
            viewHolder.mSource.setText(investAdvisor.getSOrgName());

            viewHolder.mTime.setText(TimeUtils.timeStamp2Date(adviseInfo.getIUpdateTime() * 1000L));

            ImageLoaderUtils.getImageLoader().displayImage(investAdvisor.getSFaceUrl(), viewHolder.mFace, mDefaultOptions);

            return convertView;
        }

        public void setListData(List<InvestAdviseInfo> adviseInfos) {
            mAdviseInfos.clear();
            mAdviseInfos.addAll(adviseInfos);
        }

        private class ViewHolder {
            public ImageView mFace;
            public TextView mTitle;
            public TextView mSummary;
            public TextView mName;
            public TextView mTime;
            public TextView mSource;
            public int mPosition;
        }
    }
}
