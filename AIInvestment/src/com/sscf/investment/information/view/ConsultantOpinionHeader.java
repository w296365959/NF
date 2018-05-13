package com.sscf.investment.information.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sscf.investment.R;
import com.sscf.investment.information.ConsultantOpinionFragment;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.social.FeedRequestManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.dengtacj.component.router.CommonBeaconJump;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import BEC.FeedUserBaseInfo;
import BEC.GetInvestRecommendRsp;

/**
 * Created by davidwei on 2016/12/05.
 */
public final class ConsultantOpinionHeader extends LinearLayout implements View.OnClickListener, DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final int MSG_UPDATE_INVESTMENT_ADVISER_RECOMMENDED_LIST = 1;
    private static final int LINE_COUNT = 4;

    private ConsultantOpinionFragment.ConsultantRecyclerViewAdapter mParentAdapter;
    private RecyclerView mRecyclerView;
    private InvestmentAdviserAdapter mAdapter;
    private final Handler mHandler;
    private final int mItemHeight;

    private final File mDateFile;

    public ConsultantOpinionHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler(this);
        mItemHeight = context.getResources().getDimensionPixelSize(R.dimen.discover_stock_pick_strategy_item_height);
        mDateFile = FileUtil.getInvestmentAdviserListRecommendedFile(context);
    }

    @Override
    protected void onFinishInflate() {
        findViewById(R.id.more).setOnClickListener(this);
        findViewById(R.id.moreIcon).setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        requestData();
    }

    public void setParentAdapter(ConsultantOpinionFragment.ConsultantRecyclerViewAdapter parentAdapter) {
        mParentAdapter = parentAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreIcon:
            case R.id.more:
                CommonBeaconJump.showInvestmentAdviserList(getContext());
                StatisticsUtil.reportAction(StatisticsConst.INVESTMENT_ADVISER_RECOMMENDED_CLICK_MORE);
                break;
            default:
                break;
        }
    }

    public void requestData() {
        FeedRequestManager.getInvestmentAdviserRecommendedListRequest(this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success && data.getEntity() != null) {
            final GetInvestRecommendRsp rsp = (GetInvestRecommendRsp) data.getEntity();
            final ArrayList<FeedUserBaseInfo> investmentAdviserInfos = rsp.vFeedUserBaseInfo;
            mHandler.obtainMessage(MSG_UPDATE_INVESTMENT_ADVISER_RECOMMENDED_LIST, investmentAdviserInfos).sendToTarget();
            final int size = investmentAdviserInfos != null ? investmentAdviserInfos.size() : 0;
            if (size > 0) {
                FileUtil.saveObjectToFile(investmentAdviserInfos, mDateFile);
            } else {
                if (mDateFile.exists()) {
                    mDateFile.delete();
                }
            }
        } else {
            if (mAdapter == null) {
                // 请求失败，没数据就从本地获得数据
                getDataFromLocal();
            }
        }
    }

    private void getDataFromLocal() {
        final ArrayList<FeedUserBaseInfo> investmentAdviserInfos = (ArrayList<FeedUserBaseInfo>) FileUtil.getObjectFromFile(mDateFile);
        final int size = investmentAdviserInfos != null ? investmentAdviserInfos.size() : 0;
        if (size > 0) {
            mHandler.obtainMessage(MSG_UPDATE_INVESTMENT_ADVISER_RECOMMENDED_LIST, investmentAdviserInfos).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_INVESTMENT_ADVISER_RECOMMENDED_LIST:
                updateList((ArrayList<FeedUserBaseInfo>) msg.obj);
                break;
            default:
                break;
        }
        return true;
    }

    private void updateList(final ArrayList<FeedUserBaseInfo> investmentAdviserInfos) {
        final int size = investmentAdviserInfos != null ? investmentAdviserInfos.size() : 0;
        if (size == 0) {
            if (mParentAdapter.getHeaderView() != null) {
                mParentAdapter.removeHeaderView();
            }
        } else {
            InvestmentAdviserAdapter adapter = mAdapter;
            if (adapter == null) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), LINE_COUNT));
                adapter = new InvestmentAdviserAdapter(getContext(), investmentAdviserInfos, R.layout.fragment_consultant_opinion_header_item);
                mRecyclerView.setAdapter(adapter);
                adapter.setItemClickable(true);
                mAdapter = adapter;
            } else {
                adapter.setData(investmentAdviserInfos);
                adapter.notifyDataSetChanged();
            }

            // 根据返回的个数，设置高度
            final ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
            final int strategyListHeight = ((size - 1) / LINE_COUNT + 1) * mItemHeight;
            if (params.height != strategyListHeight) {
                params.height = strategyListHeight;
                mRecyclerView.setLayoutParams(params);
            }
            if ( mParentAdapter.getHeaderView() == null) {
                mParentAdapter.setHeaderView(this);
            }
        }
    }
}

final class InvestmentAdviserAdapter extends CommonBaseRecyclerViewAdapter<FeedUserBaseInfo> {
    final DisplayImageOptions defaultOptions;

    public InvestmentAdviserAdapter(Context context, List<FeedUserBaseInfo> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
        defaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
        setItemClickable(true);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, FeedUserBaseInfo item, int position) {
        holder.setText(R.id.name, item.sNickName);
        final ImageView iconView = holder.getView(R.id.icon);
        ImageLoaderUtils.getImageLoader().displayImage(item.sFaceUrl, iconView, defaultOptions);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final FeedUserBaseInfo item = getItem(position);
        if (item != null) {
            CommonBeaconJump.showHomepage(mContext, item.iAccountId);
            StatisticsUtil.reportAction(StatisticsConst.INVESTMENT_ADVISER_RECOMMENDED_CLICK_INVESTMENT_ADVISER);
        }
    }
}
