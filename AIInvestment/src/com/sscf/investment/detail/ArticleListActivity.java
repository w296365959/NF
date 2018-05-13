package com.sscf.investment.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.detail.fragment.NewsListAdapter;
import com.sscf.investment.detail.manager.SecurityDetailRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.utils.DengtaConst;
import java.util.ArrayList;
import java.util.List;
import BEC.E_NEMS_GET_SOURCE;
import BEC.E_NEWS_TYPE;
import BEC.NewsDesc;
import BEC.NewsRsp;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2015/8/5
 *
 */
@Route("ArticleListActivity")
public class ArticleListActivity extends BaseFragmentActivity implements DataSourceProxy.IRequestCallback, View.OnClickListener {
    private static final String TAG = ArticleListActivity.class.getSimpleName();

    public static final int REFRESH_TYPE_UP = 1;
    public static final int REFRESH_TYPE_DOWN = 2;

    private ListView mListView;
    private NewsListAdapter mAdapter;
    private List<NewsDesc> mNewsList;
    private View mNoMoreLayout;
    private PtrFrameLayout mPtrFrame;

    private View mLoadingLayout;
    private View mFailRetryLayout;
    private View mLoadingLayoutCenter;
    private View mFailRetryLayoutCenter;

    private int mBottomViewState = DengtaConst.UI_STATE_NORMAL;

    private RefreshButton mRefreshButton;

    private String mName;
    private String mDtSecCode;
    private int mNewsType = E_NEWS_TYPE.NT_NEWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_article_list);

        Intent intent = getIntent();
        if (intent != null) {
            mDtSecCode = intent.getStringExtra(CommonConst.KEY_SEC_CODE);
            mNewsType = intent.getIntExtra(CommonConst.KEY_NEWS_TYPE, E_NEWS_TYPE.NT_NEWS);
            mName = intent.getStringExtra(CommonConst.KEY_SEC_NAME);
        }

        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mRefreshButton = (RefreshButton) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.actionbar_title)).setText(mName + " - " + getTitleTypeStr(mNewsType));

        mListView = (ListView) findViewById(R.id.list);
        mListView.setFooterDividersEnabled(false);
        mListView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.list_divider_height));

        mNewsList = new ArrayList<>();
        mAdapter = new NewsListAdapter(this, false, mDtSecCode, mNewsType);
        mListView.setAdapter(mAdapter);

        LayoutInflater inflater = LayoutInflater.from(this);
        mLoadingLayout = inflater.inflate(R.layout.list_item_loading, null);
        mNoMoreLayout = inflater.inflate(R.layout.no_more_text, null);
        mFailRetryLayout = inflater.inflate(R.layout.fail_retry_item, null);
        mFailRetryLayout.setOnClickListener(this);

        mLoadingLayoutCenter = findViewById(R.id.loading_layout);
        mLoadingLayoutCenter.setVisibility(View.VISIBLE);
        mFailRetryLayoutCenter = findViewById(R.id.fail_retry);
        mFailRetryLayoutCenter.setOnClickListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                DtLog.d(TAG, "onScrollStateChanged: scrollState = " + scrollState);
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        DtLog.d(TAG, "onScroll: onScrollStateChanged !!!need bottom refresh!!!");
                        if (mBottomViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT) {
                            showViewByState(DengtaConst.UI_STATE_LOADING);
                            loadNetworkData(REFRESH_TYPE_DOWN);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        mPtrFrame = (PtrFrameLayout) findViewById(R.id.ptr);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mListView.getVisibility() == View.VISIBLE) {
                    boolean notOnTop = mListView.getChildCount() > 0
                        && (mListView.getFirstVisiblePosition() > 0
                        || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
                    return !notOnTop;
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                DtLog.d(TAG, "onRefreshBegin: onUIRefresh");
                doRefresh();
            }
        });
    }

    private void doRefresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            onLoadComplete(false);
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }

        loadNetworkData(REFRESH_TYPE_UP);
    }

    private String getTitleTypeStr(int newsType) {
        switch (newsType) {
            case E_NEWS_TYPE.NT_NEWS:
                return getString(R.string.tab_news);
            case E_NEWS_TYPE.NT_REPORT:
                return getString(R.string.tab_report);
            case E_NEWS_TYPE.NT_ANNOUNCEMENT:
                return getString(R.string.tab_notice);
            default:
                return getString(R.string.tab_news);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SwipeBackLayout.attachSwipeLayout(this);

        ThreadUtils.runOnUiThreadDelay(() -> mPtrFrame.autoRefresh(), 100); //必须延时，否则不能触发onRefresh执行
    }

    private void loadNetworkData(final int refreshType) {
        mRefreshButton.startLoadingAnim();

        String startId = "", endId = "";
        String newestId = "", oldestId = "";
        int count = mNewsList.size();
        if (count > 0) {
            newestId = mNewsList.get(0).getSNewsID();
            oldestId = mNewsList.get(count - 1).getSNewsID();
        }

        switch (refreshType) {
            case REFRESH_TYPE_UP:
                startId = "";
                endId = newestId;
                break;
            case REFRESH_TYPE_DOWN:
                startId = oldestId;
                endId = "";
                break;
            default:
                break;
        }

        if (mAdapter.getCount() <= 0) {
            mFailRetryLayoutCenter.setVisibility(View.GONE);
            mLoadingLayoutCenter.setVisibility(View.VISIBLE);
        }
        SecurityDetailRequestManager.requestNewsList(mDtSecCode, mNewsType, E_NEMS_GET_SOURCE._E_STOCK_NEWSLIST_GET,
                startId, endId, this, refreshType);
    }

    @Override
    public void callback(final boolean success, final EntityObject entity) {
        final NewsRsp rsp = EntityUtil.entityToNewsRsp(success, entity);
        ThreadUtils.runOnUiThread(() -> {
            if (isDestroy()) {
                return;
            }
            if (rsp == null) {
                onLoadComplete(false);
                showViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            } else {
                onLoadComplete(true);
                final Integer refreshType = (Integer) entity.getExtra();
                if (refreshType != null) {
                    setData(rsp, refreshType);
                }
            }
        });
    }

    private void onLoadComplete(final boolean success) {
        mRefreshButton.stopLoadingAnim();

        mPtrFrame.refreshComplete();

        mLoadingLayoutCenter.setVisibility(View.GONE);
        if (mAdapter.getCount() <= 0) {
            if (!success) {
                mFailRetryLayoutCenter.setVisibility(View.VISIBLE);
            } else {
                mFailRetryLayoutCenter.setVisibility(View.GONE);
            }
        } else {
            mListView.setVisibility(View.VISIBLE);
            mFailRetryLayoutCenter.setVisibility(View.GONE);
        }
    }

    private void setData(final NewsRsp rsp, int refreshType) {
        final ArrayList<NewsDesc> newsDesc = rsp.stNewsList.vNewsDesc;
        final String nextNewsID = rsp.getSNextNewsID();
        final int size = newsDesc == null ? 0 : newsDesc.size();
        DtLog.d(TAG, "setData : size = " + size);
        if (size == 0) {
            if (refreshType == REFRESH_TYPE_DOWN) {
                showViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                return;
            }
        }

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();

        String nextNewestId = size > 0 ? newsDesc.get(0).getSNewsID() : "";
        String nextOldestId = size > 0 ? newsDesc.get(size - 1).getSNewsID() : "";
        String newestId = mNewsList.size() > 0 ? mNewsList.get(0).getSNewsID() : "";
        String oldestId = mNewsList.size() > 0 ? mNewsList.get(mNewsList.size() - 1).getSNewsID() : "";

        if (refreshType == REFRESH_TYPE_DOWN || (refreshType == REFRESH_TYPE_UP && nextNewsID.compareTo(newestId) == 0)) { //下拉刷新，或者上拉刷新且新数据和缓存数据能衔接上时
            if (nextOldestId.compareTo(newestId) > 0) { //上拉刷新且数据衔接上没有断层的情况，新数据插入队列顶部
                mNewsList.addAll(0, newsDesc);
            } else if (nextNewestId.compareTo(oldestId) < 0) { //下拉刷新，新数据插入队列尾部
                mNewsList.addAll(newsDesc);
            } else {
                if (TextUtils.isEmpty(newestId) && TextUtils.isEmpty(oldestId)) { //首次拉取
                    mNewsList.addAll(newsDesc);
                }
            }
        } else { //上拉刷新并且数据出现断层的情况，废弃缓存数据
            mNewsList.clear();
            mNewsList.addAll(newsDesc);
        }

        mAdapter.setListData(mNewsList);
        mListView.setAdapter(mAdapter);
        mListView.setSelectionFromTop(firstVisiblePosition, top);
        mListView.setVisibility(View.VISIBLE);
    }

    private void showViewByState(int state) {
        if (mAdapter.getCount() <= 0) {
            return;
        }

        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                break;
            case DengtaConst.UI_STATE_LOADING:
                if (mBottomViewState != DengtaConst.UI_STATE_LOADING) {
                    mListView.removeFooterView(mNoMoreLayout);
                    mListView.removeFooterView(mFailRetryLayout);
                    mListView.addFooterView(mLoadingLayout);
                    ensureAdapter();
                    mBottomViewState = DengtaConst.UI_STATE_LOADING;
                }
                break;
            case DengtaConst.UI_STATE_NO_MORE_CONTENT:
                if (mBottomViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT) {
                    mListView.removeFooterView(mLoadingLayout);
                    mListView.removeFooterView(mFailRetryLayout);
                    mListView.addFooterView(mNoMoreLayout);
                    ensureAdapter();
                    mBottomViewState = DengtaConst.UI_STATE_NO_MORE_CONTENT;
                }
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                if (mBottomViewState != DengtaConst.UI_STATE_FAILED_RETRY) {
                    mListView.removeFooterView(mLoadingLayout);
                    mListView.removeFooterView(mNoMoreLayout);
                    mListView.addFooterView(mFailRetryLayout);
                    ensureAdapter();
                    mBottomViewState = DengtaConst.UI_STATE_FAILED_RETRY;
                }
                break;
            default:
                break;
        }
    }

    private void ensureAdapter() {
        ListAdapter adapter = mListView.getAdapter();
        if(adapter != null && !(adapter instanceof HeaderViewListAdapter)) {
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mFailRetryLayout || v == mFailRetryLayoutCenter) {
            showViewByState(DengtaConst.UI_STATE_LOADING);
            loadNetworkData(REFRESH_TYPE_DOWN);
        } else if (v == mRefreshButton) {
            if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
                onLoadComplete(false);
                DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                return;
            }

            mListView.setSelection(0);
            mPtrFrame.autoRefresh();
        } else if (v.getId() == R.id.actionbar_back_button) {
            finish();
        }
    }
}
