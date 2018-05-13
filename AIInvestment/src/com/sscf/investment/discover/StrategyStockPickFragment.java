package com.sscf.investment.discover;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.discover.manager.DiscoverRequestManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.MultiTagView;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import java.io.File;
import java.util.ArrayList;
import BEC.BEACON_STAT_TYPE;
import BEC.ConditionPickStrategy;
import BEC.ConditionPickStrategyListRsp;
import BEC.TagInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by davidwei on 2016/11/15
 * 策略选股
 */
public final class StrategyStockPickFragment extends BaseFragment implements PtrHandler, View.OnClickListener,
        DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_FAILED = 2;

    @BindView(R.id.ptr) PtrFrameLayout mPtrLayout;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    View mTitle;
    StrategyListAdapter mAdapter;
    Handler mHandler;
    File mStrategyListFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Activity activity = getActivity();
        if (activity == null) {
            return null;
        }

        final View root = inflater.inflate(R.layout.ptr_recycler_view, null);
        ButterKnife.bind(this, root);
        initViews(activity);

        mHandler = new Handler(this);

        mStrategyListFile = FileUtil.getSelectedStrategyListFile(activity);
        requestData();
        return root;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SMART_PICK);
        helper.setKey(StatConsts.DISCOVER_STRATEGY_STOCK_PICK);
        return helper;
    }

    private void initViews(final Activity activity) {
        if (activity == null) {
            return;
        }
        mPtrLayout.setPtrHandler(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new StrategyListAdapter(activity);
        final View header = View.inflate(activity, R.layout.discover_strategy_list_header, null);
        header.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mTitle = header.findViewById(R.id.title);
        header.findViewById(R.id.customizedStrategy).setOnClickListener(this);
        header.findViewById(R.id.portfolioStrategy).setOnClickListener(this);
        mAdapter.setHeaderView(header);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        requestData();
        StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_STRATEGY_TAB_DISPLAY);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        requestData();
        StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_STRATEGY_TAB_DISPLAY);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            return RecyclerViewHelper.isOnTop(mRecyclerView);
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        requestData();
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.customizedStrategy:
                WebBeaconJump.showCustomizedStrategy(activity);
                StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_STRATEGY_CLICK_CUSTOMIZED_STRATEGY);
                break;
            case R.id.portfolioStrategy:
                WebBeaconJump.showPortfolioStrategy(activity);
                StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_STRATEGY_CLICK_PORTFOLIO_STRATEGY);
                break;
        }
    }

    void requestData() {
        DiscoverRequestManager.getSelectedStragetyList(this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null) {
                final ConditionPickStrategyListRsp rsp = (ConditionPickStrategyListRsp) entity;
                if (rsp.iRet == 0) {
                    final ArrayList<ConditionPickStrategy> stratetyList = rsp.vtConditionPickStrategy;
                    mHandler.obtainMessage(MSG_UPDATE_LIST, stratetyList).sendToTarget();
                    FileUtil.saveObjectToFile(stratetyList, mStrategyListFile);
                    return;
                }
            }
        }
        // 失败处理
        if (mAdapter == null || mAdapter.getItemCount() <= 0) {
            DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<ConditionPickStrategy> stratetyList = (ArrayList<ConditionPickStrategy>) FileUtil.getObjectFromFile(mStrategyListFile);
                    if (stratetyList != null) {
                        mHandler.obtainMessage(MSG_UPDATE_LIST, stratetyList).sendToTarget();
                    }
                }
            });
        }
        mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
    }

    @Override
    public boolean handleMessage(Message msg) {
        mPtrLayout.refreshComplete();
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                updateStrategyList((ArrayList<ConditionPickStrategy>) msg.obj);
                break;
            case MSG_UPDATE_FAILED:
                break;
        }
        return true;
    }

    void updateStrategyList(final ArrayList<ConditionPickStrategy> stratetyList) {
        final int size = stratetyList.size();
        mAdapter.setListData(stratetyList);
        mAdapter.notifyDataSetChanged();
        mTitle.setVisibility(size > 0 ? View.VISIBLE : View.GONE);
    }
}

final class StrategyListAdapter extends CommonRecyclerViewAdapter {

    public StrategyListAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new StrategyHolder(mInflater.inflate(R.layout.discover_strategy_list_item, parent, false));
    }

    final class StrategyHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.condition) TextView condition;
        @BindView(R.id.multiTagView) MultiTagView multiTagView;

        public StrategyHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);

            final ConditionPickStrategy item = (ConditionPickStrategy) itemData;
            title.setText(item.sStrategyName);

            condition.setText(getConditionText(item.vCondition));

            final ArrayList<String> tags = new ArrayList<String>(3);
            String tagName = null;
            for (TagInfo tagInfo : item.vtTagInfo) {
                tagName = tagInfo.sTagName;
                if (!TextUtils.isEmpty(tagName)) {
                    tags.add(tagName);
                }
            }
            multiTagView.addTags(tags, R.drawable.tag_round_rect_bg_stock_pick, R.color.default_text_color_60);
        }

        private CharSequence getConditionText(final ArrayList<String> conditions) {
            final int size = conditions == null ? 0 : conditions.size();
            if (size <= 0) {
                return "";
            }

            final StringBuilder text = new StringBuilder();
            for (int i = 0; i < size; i++) {
                text.append(conditions.get(i));
                if (i < size - 1) {
                    text.append(',');
                }
            }
            return text;
        }

        @Override
        public void onItemClicked() {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            final ConditionPickStrategy item = (ConditionPickStrategy) mItemData;
            if (item != null) {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    scheme.handleUrl(mContext, item.sUrl);
                }
                StatisticsUtil.reportAction(StatisticsConst.STOCK_PICK_STRATEGY_CLICK_SELECTED_STRATEGY);
            }
        }
    }
}
