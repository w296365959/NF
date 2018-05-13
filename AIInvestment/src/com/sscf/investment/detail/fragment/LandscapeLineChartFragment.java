package com.sscf.investment.detail.fragment;

import BEC.*;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.detail.presenter.LandScapeDetailPresenter;
import com.dengtacj.request.MarketRequestManager;
import com.sscf.investment.detail.presenter.TimeLineComparePresenter;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.detail.LineChartActivity;
import com.sscf.investment.detail.entity.BuySellEntity;
import com.sscf.investment.detail.view.*;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.model.KlineSettingConst;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.LiveMessageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2015/11/2.
 */
public class LandscapeLineChartFragment extends BaseFragment implements View.OnClickListener, TimeLineComparePresenter.ITimeLineCompareDisplayer, DataSourceProxy.IRequestCallback {
    private static final String TAG = LandscapeLineChartFragment.class.getSimpleName();
    private TabLayout mTabLayout;
    private Button mSelectCompareStockButton;
    private CompareSelectPopupWindow mCompareSelectPopupWindow;
    private View mTransactionLayout;
    private TwoTabSelectorView mTabSelector;
    private InnerScrollView mBuySellScrollView;
    private BuySellView mBuySellView;
    private FrameLayout mBuySellFrameLayout;

    private TicksView mTicksView;
    private InnerScrollView mTicksScrollView;
    private FrameLayout mTicksFrameLayout;

    private InnerScrollView mCapitalFlowScrollView;
    private LinearLayout mCapitalFlowFrameLayout;

    private KLineControlView mKLineControlView;
    private LineChartSurfaceTextureListener mSurfaceListener;
    private IndicatorAnimationView mTimeLineIndicator;
    private HorizontalPopupEntranceGroup mEntranceGroupView;
    private LineChartTextureView mSurface;

    private CapitalFlowView capitalFlowView;
    private CapitalFlowDetailView capitalFlowDetailView;
    private DKView dkView;

    private int mRepairType = KlineSettingConst.K_LINE_REPAIR;
    private int mTradingIndicatorType;
    private int mTimeIndicatorType = SettingConst.TIME_LINE_INDICATOR_VOLUME;

    private boolean mIsTrading;
    private int mTradingMinutes;

    private String mOpenTimeStr;
    private String mMiddleTimeStr;
    private String mCloseTimeStr;

    private View mContextView;
    private LineChartActivity mActivity;

    private LiveMessageView mLiveMsgView;
    private String mLastLiveMsgId;

    // 灯塔证券唯一编码
    private String mDtSecCode;
    private String mStockName;
    private int mIndexInList;
    private boolean mSupportCapitalDDZ;

    private SecQuote mQuote;
    /**
     * 价格显示精度
     */
    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;

    private View mLoadingLayout;
    private View mFailRetryLayout;

    private final SparseArray<List<KLineDesc>> mKLineData = new SparseArray<>();
    private final SparseIntArray mKLineRefreshTime = new SparseIntArray();

    private int mKLineType;
    private int mTimeLineCapitalRefreshTime;
    private ArrayList<CapitalDDZDesc> mTimeLineCapitals;
    private int mKLineCapitalRefreshTime;

    private ImageView mToLeftButton;
    private ImageView mToRightButton;

    private LandscapeDetailQuoteLayout mQuoteLayout;

    private LineChartValueView mValueView;

    private LandScapeDetailPresenter mPresenter;

    private TimeStatHelper mStatHelper;

    private KLineSettingManager mKLineSettingManager;
    private KLineSettingConfigure mKLineSettingConfigure;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED.equals(action)) {
                int marketType = intent.getIntExtra(TradingStateManager.KEY_MARKET_TYPE, -1);
                if (marketType == getMarketType()) {
                    int tradingState = intent.getIntExtra(TradingStateManager.KEY_TRADING_STATE, -1);
                    mIsTrading = tradingState == TradingStateManager.TRADING_STATE_TRADING;
                    DtLog.d(TAG, "onReceive: TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED tradingState = " + tradingState);
                    queryTradingValuesAndRefresh(false);
                }
            } else if (TradingStateManager.ACTION_TRADING_STATE_UPDATED.equals(action)){
                queryTradingValuesAndRefresh(true);
            } else if (LineChartTextureView.ACTION_TIME_LINE_INDICATOR_TYPE_CHANGED.equals(action)) {
                int hashCode = intent.getIntExtra(LineChartTextureView.KEY_CHART_VIEW_HASH_CODE, -1);
                if (hashCode == mSurface.hashCode()) {
                    loadTimeLineCapitalDDZData();
                    StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_INDICATOR_SWITCH);
                }
            } else if (LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED.equals(action)) {
                int hashCode = intent.getIntExtra(LineChartTextureView.KEY_CHART_VIEW_HASH_CODE, -1);
                if (hashCode == mSurface.hashCode()) {
                    mTradingIndicatorType = DengtaSettingPref.loadKLineIndicatorType0();
                    loadKLineCapitalDDZData(mKLineType, 0, getKLineCapitalNum());
                    StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_INDICATOR_SWITCH);
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKLineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        mKLineSettingConfigure = mKLineSettingManager.getKlineSettingConfigure();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LineChartActivity activity = (LineChartActivity) getActivity();
        mActivity = activity;

        final View contextView = activity.getLayoutInflater().inflate(R.layout.main_line_chart, container, false);
        mContextView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mStockName = args.getString(DengtaConst.KEY_SEC_NAME);
            mIndexInList = args.getInt(LineChartActivity.KEY_INDEX_IN_LIST);
            mSupportCapitalDDZ = StockUtil.supportCapitalDDZ(mDtSecCode);
        }
        DtLog.d(TAG, "LandscapeLineChartFragment.onCreateView mStockName : " + mStockName);

        initHeaderViews();

        mValueView = (LineChartValueView) contextView.findViewById(R.id.line_chart_value_view);
        mValueView.setDtSecCode(mDtSecCode);

        mSelectCompareStockButton = (Button) contextView.findViewById(R.id.select_compare_stock_button);
        mSelectCompareStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompareSelectPopupWindow();
            }
        });

        mTabLayout = (TabLayout) contextView.findViewById(R.id.line_tabs);
        mTabLayout.initWithTitles(new int[]{R.string.tab_one_day, R.string.tab_five_day, R.string.tab_daily_k, R.string.tab_weekly_k, R.string.tab_monthly_k,
            R.string.tab_one_minute_k, R.string.tab_five_minutes_k, R.string.tab_fifteen_minutes_k,
            R.string.tab_thirty_minutes_k, R.string.tab_sixty_minutes_k}, null);

        mStatHelper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_DETAIL_KLINE);

        mTabLayout.setOnTabSelectionListener(new TabLayout.OnTabSelectionListener() {
            @Override
            public void onTabSelected(int index) {
                final int currentIndex = DengtaSettingPref.getLineTypeIndex();
                int currentKey = getStatKey(currentIndex);
                int newKey = getStatKey(index);
                if(currentKey != newKey) {
                    mStatHelper.end();
                    mStatHelper.setKey(newKey);
                    mStatHelper.start();
                }
                DengtaSettingPref.setLineTypeIndex(index);

                statTabSelection(index);

                boolean isStockCompareShow = false;

                //切换显示的线图类型
                switch(index) {
                    case LineChartTextureView.TYPE_TIME:
                        if (!StockUtil.hasTransaction(mDtSecCode)) {
                            mTransactionLayout.setVisibility(View.GONE);
                        } else {
                            if (SettingPref.getBoolean(SettingConst.KEY_SETTING_TRANSATICON_OPEN, true)) {
                                mTransactionLayout.setVisibility(View.VISIBLE);
                            } else {
                                mTransactionLayout.setVisibility(View.GONE);
                            }
                        }

                        tryLoadStoredCompare();

                        if (mIsTrading && !isSuspended()) {
                            mTimeLineIndicator.startAnimation();
                        }
                        isStockCompareShow = StockUtil.supportStockCompare(mDtSecCode);
                        mKLineControlView.setVisibility(View.GONE);
                        break;
                    case LineChartTextureView.TYPE_FIVE_DAY:
                        if (mIsTrading && !isSuspended()) {
                            mTimeLineIndicator.startAnimation();
                        }
                        mTransactionLayout.setVisibility(View.GONE);
                        mKLineControlView.setVisibility(View.GONE);
                        break;
                    default:
                        mTimeLineIndicator.stopAnimation();
                        mTransactionLayout.setVisibility(View.GONE);
                        mKLineControlView.setVisibility(View.VISIBLE);
                        break;
                }

                mSelectCompareStockButton.setVisibility(isStockCompareShow ? View.VISIBLE : View.GONE);
                mSurfaceListener.onTradingIndicatorTypeChanged(mTradingIndicatorType, 0);
                mSurfaceListener.onTimeIndicatorTypeChanged(mTimeIndicatorType, 0);
                if(mSurfaceListener.getLineType() != index) {
                    mSurface.exitDetailState();
                    mValueView.setLineType(index);
                }
                refreshValueView(index);
                mSurfaceListener.onTabSelected(index);
                mTimeLineIndicator.updateCenterPoint(new Point(0, 0));
                showEntranceGroupViewIfNeed(index);
                updateKLineQuote(null);
                mPresenter.refresh();
            }
        });

        mFailRetryLayout = contextView.findViewById(R.id.fail_retry);
        mFailRetryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.refresh();
            }
        });

        mLoadingLayout = contextView.findViewById(R.id.loading_layout);

        mTimeLineIndicator = (IndicatorAnimationView) contextView.findViewById(R.id.line_chart_indicator);

        initEntranceGroup(contextView);

        mSurface = (LineChartTextureView) contextView.findViewById(R.id.line_chart);
        mSurface.setIsTouchMode(true);
        mSurface.setOnDoubleTapListener(new LineChartTextureView.OnDoubleTapListener() {
            @Override
            public void onDoubleTap() {
                finish();
            }
        });
        mSurfaceListener = new LineChartSurfaceTextureListener(getActivity());
        mSurfaceListener.setDtSecCode(mDtSecCode);
        mSurface.setSurfaceTextureListener(mSurfaceListener);
        mSurfaceListener.setIsFullScreen(getActivity(), true);

        mSurfaceListener.setTimeValueChangeListener(new LineChartTextureView.TimeValueChangeListener() {
            @Override
            public void onValuePositionChanged(final Point point) {
                ThreadUtils.runOnUiThread(() -> mTimeLineIndicator.updateCenterPoint(point) );
            }
        });
        mSurfaceListener.setKLineEventListener(new LineChartTextureView.KLineEventListener() {
            @Override
            public void onMoreDataNeeded(final int lineType, final int start, final int wantNum) {
                if (TimeUtils.isFrequentOperation()) {
                    return;
                }
                Log.d(TAG, "ET_GET_KLINE_DATA: start = " + start + ", wantNum = " + wantNum);
                final int type = mSurfaceListener.getServerKLineType(lineType);
                ThreadUtils.runOnUiThread(() -> loadKLineData(type, start, wantNum) );
            }

            @Override
            public void onTimeLineTouchChanged(final TimeLineInfosView.TimeLineTouchEvent event) {
                ThreadUtils.runOnUiThread(() -> {
                    final int action = event.getAction();
                    //刷新顶栏信息文字
                    switch (action) {
                        case TimeLineInfosView.TimeLineTouchEvent.ACTION_DOWN:
                            mValueView.setTimeLineEvent(event);
                            break;
                        case TimeLineInfosView.TimeLineTouchEvent.ACTION_MOVE:
                            mValueView.setTimeLineEvent(event);
                            break;
                        case TimeLineInfosView.TimeLineTouchEvent.ACTION_UP:
                            break;
                        default:
                            break;
                    }
                } );
            }

            @Override
            public void onKLineTouchChanged(final KLineInfosView.KLineLineTouchEvent event) {
                ThreadUtils.runOnUiThread(() -> {
                    if(mSurface.isDetailState()) {
                        updateKLineQuote(event);
                    }
                });
            }

            @Override
            public void onKLineAverageChanged(KLineInfosView.KLineAverageInfo averageInfo) {
                ThreadUtils.runOnUiThread(() -> mValueView.setAverageInfo(averageInfo));
            }

            @Override
            public void onKLineHeightUpdate(int kChartHeight) {
            }
        });

        mSurface.setOnStateChangeListener(new LineChartTextureView.OnStateChangeListener() {
            @Override
            public void onStateChange(int oldState, int newState) {
                DtLog.d(TAG, "oldState = " + oldState + ", newState = " + newState);
                if(oldState == LineChartTextureView.STATE_CANCEL_DETAIL
                        && newState == LineChartTextureView.STATE_NORMAL) {
                    int lineType = mSurfaceListener.getLineType();
                    if(lineType == LineChartTextureView.TYPE_TIME ||
                            lineType ==  LineChartTextureView.TYPE_FIVE_DAY) {
                        refreshValueView(lineType);
                    }
                    updateKLineQuote(null);
                }
            }
        });

        initTransactionLayout();
        setIsIndex(StockUtil.isIndex(mDtSecCode));

        mKLineControlView = (KLineControlView) contextView.findViewById(R.id.k_line_control);
        mKLineControlView.setDtSecCode(mDtSecCode);
        mKLineControlView.setKLineControlListener(new KLineControlView.OnKLineControlListener() {
            @Override
            public void onRepairTypeChanged(int repairType) {
                if (mRepairType != repairType) { // 清空缓存
                    mKLineData.clear();
                    mKLineRefreshTime.clear();
                    mRepairType = repairType;
                    mKLineSettingConfigure.rightStatus = mRepairType;
                }

                mSurface.stopAnimation();
                mSurfaceListener.clearKLineEntities();
                mPresenter.refresh();
            }

            @Override
            public void onTradingIndicatorTypeChanged(int indicatorType) {
                mTradingIndicatorType = indicatorType;
                SettingPref.putInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_0, mTradingIndicatorType);

                //其他指标类别只需要通知canvas重绘就可以计算出指标值了，但是资金流类型需要发广播触发重新拉取
                Intent intent = new Intent(LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED);
                intent.putExtra(LineChartTextureView.KEY_CHART_VIEW_HASH_CODE, mSurface.hashCode());
                LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).sendBroadcast(intent);

                mSurfaceListener.onTradingIndicatorTypeChanged(indicatorType, 0);
            }
        });

        mLiveMsgView = (LiveMessageView) mContextView.findViewById(R.id.live_message);
        mLiveMsgView.setFullscreen(true);
        mLiveMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_KLINE_LIVE_MSG_CLICKED);
                mLiveMsgView.setVisibility(View.GONE);
                openSecLiveMsgPage();
            }
        });

        mRepairType = mKLineSettingConfigure.rightStatus;
        mTradingIndicatorType = DengtaSettingPref.loadKLineIndicatorType0();

        final SecQuote quote = DengtaApplication.getApplication().getDataCacheManager().getSecQuote(mDtSecCode);
        updateQuoteData(quote);

        mPresenter = new LandScapeDetailPresenter(this, mDtSecCode);

        return contextView;
    }

    private void removeCompare() {
        mComparePresenter = null;
        mSurfaceListener.removeCompare();
        updateCompareButton("");
    }

    private TimeLineComparePresenter mComparePresenter;

    public TimeLineComparePresenter getPresenter(String dtSecCode) {
        if(mComparePresenter != null) {
            if(!TextUtils.equals(mComparePresenter.getDtSecCode(), dtSecCode)) {
                mComparePresenter.releaseDiplayer();
                mComparePresenter = new TimeLineComparePresenter(this, dtSecCode);
            }
        } else {
            mComparePresenter = new TimeLineComparePresenter(this, dtSecCode);
        }
        return mComparePresenter;
    }

    private void tryLoadStoredCompare() {
        int type = DataPref.getTimeLineCompareType();
        String dtSecCode = null;
        switch (type) {
            case SettingConst.TIMELINE_COMPARE_TYPE_MARKET:
                dtSecCode = StockUtil.getStockMarketDtCode(mDtSecCode);
                break;
            case SettingConst.TIMELINE_COMPARE_TYPE_CUSTOM:
                dtSecCode = DataPref.getTimeLineCompareSecCode();
                break;
            default:
        }

        if(!TextUtils.isEmpty(dtSecCode)) {
            DtLog.d(TAG, "tryLoadStoredCompare() mStockName : " + mStockName + ", dtSecCode = " + dtSecCode);
            tryLoadCompare(dtSecCode);
        } else {
            removeCompare();
        }
    }

    private void tryLoadCompare(String compareSecCode) {
        if(StockUtil.supportStockCompare(mDtSecCode) && !TextUtils.isEmpty(compareSecCode)) {
            if(TextUtils.equals(mDtSecCode, compareSecCode)) {
                updateCompareButton(mStockName);
            } else {
                getPresenter(compareSecCode).loadQuote();
            }
        }
    }

    private int getStatKey(int type) {
        switch (type) {
            case LineChartTextureView.TYPE_TIME:
                return StatConsts.TIME_LINE;
            case LineChartTextureView.TYPE_FIVE_DAY:
                return StatConsts.FIVE_DAY;
            case LineChartTextureView.TYPE_DAILY_K:
                return StatConsts.DAILY_KLINE;
            default:
                return StatConsts.OTHER_KLINE;
        }
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "onFirstUserVisible() : " + this + ", mStockName = " + mStockName);
        if(isCurrentPage() && mStatHelper != null) {
            int index = DengtaSettingPref.getLineTypeIndex();
            mStatHelper.setKey(getStatKey(index));
            mStatHelper.start();
        }
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "onUserVisible() : " + this + ", mStockName = " + mStockName);
        if(isCurrentPage() && mStatHelper != null) {
            int index = DengtaSettingPref.getLineTypeIndex();
            mStatHelper.setKey(getStatKey(index));
            mStatHelper.start();
        }
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.d(TAG, "onFirstUserInvisible() : " + this);
        if(mStatHelper != null) {
            mStatHelper.end();
        }
    }

    @Override
    public void onUserInvisible() {
        DtLog.d(TAG, "onUserInvisible() : " + this);
        if(mStatHelper != null) {
            mStatHelper.end();
        }
    }

    private void updateKLineQuote(final KLineInfosView.KLineLineTouchEvent event) {
        if (event != null) {
            mQuoteLayout.updateKLineQuote(event, mQuote);
        } else {
            if (mQuote != null) {
                mQuoteLayout.updateQuote(mQuote);
            }
        }
    }

    public void refreshValueView(final int lineType) {
        if(LineChartSurfaceTextureListener.isTimeLine(lineType)) {
            if(mSurfaceListener.getLineType() == lineType && !mSurface.isDetailState()) {
                if(mQuote != null) {
                    ThreadUtils.runOnUiThread(() -> {
                        final TrendDesc latestEntity = mSurfaceListener.getLatestEntity(lineType);
                        if (latestEntity != null) {
                            TimeLineInfosView.TimeLineTouchEvent event = createTimeLineEvent(latestEntity);
                            mValueView.setTimeLineEvent(event);
                        }
                    });
                }
            }
        }
    }

    private TimeLineInfosView.TimeLineTouchEvent createTimeLineEvent(TrendDesc entity) {
        TimeLineInfosView.TimeLineTouchEvent event = new TimeLineInfosView.TimeLineTouchEvent();
        event.setMinute(entity.getIMinute());
        event.setNow(entity.getFNow());
        event.setDelta((entity.getFNow() - mSurfaceListener.getYesterdayClose()) / mSurfaceListener.getYesterdayClose());
        event.setVolume(entity.getLNowvol());
        event.setAverage(entity.getFAverage());
        event.setYesterdayClose(mSurfaceListener.getYesterdayClose());
        event.setTimeLineStart(0);
        return event;
    }

    private void initEntranceGroup(final View root) {
        boolean show = false;
        final HorizontalPopupEntranceGroup entranceGroupView = (HorizontalPopupEntranceGroup) root.findViewById(R.id.similar_k_entrance_group);
        if (StockUtil.supportBSPoint(mDtSecCode)) {
            entranceGroupView.addChildIcon(R.drawable.entrance_bs, R.drawable.entrance_bs);
            show = true;
        }
        if (StockUtil.supportSimilarK(mDtSecCode)) {
            entranceGroupView.addChildIcon(R.drawable.entrance_similar_k, R.drawable.entrance_similar_k);
            show = true;
        }
        if (StockUtil.supportSecHistory(mDtSecCode)) {
            entranceGroupView.addChildIcon(R.drawable.entrance_history, R.drawable.entrance_history);
            show = true;
        }
        if (show) {
            entranceGroupView.setOnEntranceListener(new HorizontalPopupEntranceGroup.OnEntranceClickedListener() {
                @Override
                public void onEntranceClicked(int tag) {
                    switch (tag) {
                        case R.drawable.entrance_bs:
                            openBsSignalPage();
                            break;
                        case R.drawable.entrance_similar_k:
                            openSimilarKPage();
                            break;
                        case R.drawable.entrance_history:
                            openSecHistoryPage();
                            break;
                        default:
                            break;
                    }
                }
            });
            entranceGroupView.setIsInFullscreen(true);
            mEntranceGroupView = entranceGroupView;
        }
    }

    private void showEntranceGroupViewIfNeed(int index) {
        if (mEntranceGroupView == null) {
            return;
        }
        if (index >= LineChartTextureView.TYPE_DAILY_K) {
            mEntranceGroupView.setVisibility(View.VISIBLE);
            if (DataPref.isLineChartGroupEntranceClicked()) {
                mEntranceGroupView.setExpanded(DataPref.isLineChartGroupEntranceExpanded());
            } else {
                DataPref.setLineChartGroupEntranceClicked(true);
                mEntranceGroupView.expandChildrenWithAnimation(true);
            }
        } else {
            mEntranceGroupView.setVisibility(View.GONE);
        }
    }

    private void openBsSignalPage() {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        WebBeaconJump.showBS(activity, mDtSecCode, mStockName);

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_DAILY_K_ENTRANCE_BS_SIGNAL_CLICKED);
    }

    private void openSimilarKPage() {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        WebBeaconJump.showSimilarKLine(activity, mDtSecCode, mStockName);

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_DAILY_K_ENTRANCE_SIMILAR_K_CLICKED);
    }

    private void openSecHistoryPage() {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        WebBeaconJump.showSecHistory(activity, mDtSecCode, mStockName);

        StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_DAILY_K_ENTRANCE_HISTORY_CLICKED);
    }

    private void openSecLiveMsgPage() {
        DengtaApplication.getApplication().getDataCacheManager().setHasUnreadLiveMsg(mDtSecCode, false);

        WebBeaconJump.showStockLive(getActivity(), mDtSecCode, mStockName);
        StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_PAGE_DISPLAYED);
    }

    private void statTabSelection(int index) {
        switch (index) {
            case LineChartTextureView.TYPE_TIME:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TIME_LINE_SHOW);
                break;
            case LineChartTextureView.TYPE_FIVE_DAY:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_FIVE_DAY_SHOW);
                break;
            case LineChartTextureView.TYPE_DAILY_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_DAILY_K_SHOW);
                break;
            case LineChartTextureView.TYPE_WEEKLY_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            case LineChartTextureView.TYPE_MONTHLY_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            case LineChartTextureView.TYPE_ONE_MIN_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            case LineChartTextureView.TYPE_FIVE_MIN_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            case LineChartTextureView.TYPE_FIFTEEN_MIN_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            case LineChartTextureView.TYPE_THIRTY_MIN_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            case LineChartTextureView.TYPE_SIXTY_MIN_K:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_OTHER_K_SHOW);
                break;
            default:
                break;
        }
    }

    public void setIsIndex(final boolean isIndex) {
        ThreadUtils.runOnUiThread(() -> {
            mSurfaceListener.setIsIndex(isIndex);
            if (mSurfaceListener.isIndex()) {
                mTransactionLayout.setVisibility(View.GONE);
            }
        });
    }

    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(LineChartActivity.KEY_LINE_TYPE, mSurfaceListener.getLineType());
        intent.putExtra(LineChartActivity.KEY_SEC_CODE, mDtSecCode);
        mActivity.setResult(0, intent);

        mActivity.finish();
    }

    public int getCurrentLineType() {
        return DengtaSettingPref.getLineTypeIndex();
    }

    private void initTransactionLayout() {
        mTransactionLayout = mContextView.findViewById(R.id.transaction_layout);
        mTabSelector = (TwoTabSelectorView) mContextView.findViewById(R.id.tab_selector);
        mTabSelector.setAttributes(R.attr.transaction_two_tab_textColor, R.attr.transaction_two_tab_selector_view_bg_drawable,
            R.attr.transaction_two_tab_selector_selected_bg_left_drawable, R.attr.transaction_two_tab_selector_selected_bg_right_drawable,
                R.attr.transaction_two_tab_selector_selected_bg_middle_drawable);

        if (StockUtil.hasTransaction(mDtSecCode) && !StockUtil.isNewThirdBoard(mDtSecCode) && !StockUtil.isFund(mDtSecCode)) {
            mTabSelector.setTabTitles(R.string.transaction_buy_sell, R.string.transaction_ticks, R.string.transaction_capital);
        } else {
            // 新三板、场内基金不显示资金tab
            mTabSelector.setTabTitles(R.string.transaction_buy_sell, R.string.transaction_ticks);
        }

        if (StockUtil.hasTransaction(mDtSecCode)) {
            if (SettingPref.getBoolean(SettingConst.KEY_SETTING_TRANSATICON_OPEN, true)) {
                mTransactionLayout.setVisibility(View.VISIBLE);
            } else {
                mTransactionLayout.setVisibility(View.GONE);
            }
        } else {
            mTransactionLayout.setVisibility(View.GONE);
        }

        mTabSelector.setTabTitlesSize(R.dimen.font_size_14, R.dimen.font_size_14);
        mTabSelector.setOnTabSelectedListener(new TwoTabSelectorView.OnTabSelectedListener() {
            @Override
            public void onFirstTabSelected() {
                mBuySellScrollView.setVisibility(View.VISIBLE);
                mTicksScrollView.setVisibility(View.INVISIBLE);
                mCapitalFlowScrollView.setVisibility(View.INVISIBLE);
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TIME_LINE_SIDE_CLICKED);
            }

            @Override
            public void onSecondTabSelected() {
                mTicksScrollView.setVisibility(View.VISIBLE);
                mBuySellScrollView.setVisibility(View.INVISIBLE);
                mCapitalFlowScrollView.setVisibility(View.INVISIBLE);

                // 切换成交明细，立即手动更新一次
                mPresenter.requestTick();
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TIME_LINE_SIDE_CLICKED);
            }

            @Override
            public void onThirdTabSelected() {
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TICKS_CLICKED);
                mCapitalFlowScrollView.setVisibility(View.VISIBLE);
                mBuySellScrollView.setVisibility(View.INVISIBLE);
                mTicksScrollView.setVisibility(View.INVISIBLE);

                // 切换资金，立即手动更新一次
                if (mQuote != null && mQuote.eSecStatus != E_SEC_STATUS.E_SS_SUSPENDED) {
                    mPresenter.requestCapitalData();
                }
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TIME_LINE_SIDE_CLICKED);
            }
        });
        mBuySellScrollView = (InnerScrollView) mContextView.findViewById(R.id.buy_sell_layout);
        mBuySellView = (BuySellView) mContextView.findViewById(R.id.buy_sell);
        mBuySellFrameLayout = (FrameLayout) mBuySellScrollView.getChildAt(0);
        mBuySellFrameLayout.setOnClickListener(this);

        mTicksScrollView = (InnerScrollView) mContextView.findViewById(R.id.ticks_layout);
        mTicksFrameLayout = (FrameLayout) mTicksScrollView.getChildAt(0);
        mTicksFrameLayout.setOnClickListener(this);
        mTicksView = (TicksView) mContextView.findViewById(R.id.ticks);

        mCapitalFlowScrollView = (InnerScrollView)mContextView.findViewById(R.id.capital_flow_layout);
        mCapitalFlowFrameLayout = (LinearLayout) mCapitalFlowScrollView.getChildAt(0);
        mCapitalFlowFrameLayout.setOnClickListener(this);
        capitalFlowView = (CapitalFlowView)mContextView.findViewById(R.id.capital_flow_pie_transaction);
        capitalFlowView.setType(CapitalFlowView.TYPE_TRANSACTION_PIE);
        capitalFlowDetailView = (CapitalFlowDetailView)mContextView.findViewById(R.id.capital_flow_detail_view);
        dkView = (DKView)mContextView.findViewById(R.id.capital_flow_dkview_id);

        SecQuote quote = DengtaApplication.getApplication().getDataCacheManager().getSecQuote(mDtSecCode);
        if (quote != null) {
            setBuySellData(quote);
        }
    }

    private void initHeaderViews() {
        mQuoteLayout = (LandscapeDetailQuoteLayout) mContextView.findViewById(R.id.quoteLayout);
        mQuoteLayout.setKeysText(mDtSecCode);

        mQuoteLayout.findViewById(R.id.close_button).setOnClickListener(this);
        mToLeftButton = (ImageView) mQuoteLayout.findViewById(R.id.to_left_button);
        mToLeftButton.setOnClickListener(this);
        mToRightButton = (ImageView) mQuoteLayout.findViewById(R.id.to_right_button);
        mToRightButton.setOnClickListener(this);

        ((TextView) mQuoteLayout.findViewById(R.id.name)).setText(mStockName);
        ((TextView) mQuoteLayout.findViewById(R.id.code)).setText(StockUtil.convertSecInfo(mDtSecCode).getSSecCode());
    }

    public void reloadDataByIndex() {
        final int index = DengtaSettingPref.getLineTypeIndex();
        DtLog.d(TAG, "reloadDataByIndex index : " + index);
        showViewByState(DengtaConst.UI_STATE_LOADING);

        switch (index) {
            case LineChartTextureView.TYPE_TIME:
                mKLineType = -1;
                mPresenter.requestTimeLineData();
                tryLoadStoredCompare();
                loadTimeLineCapitalDDZData();
                break;
            case LineChartTextureView.TYPE_FIVE_DAY:
                mKLineType = -1;
                loadFiveDayTimeLineData();
                break;
            case LineChartTextureView.TYPE_DAILY_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_DAY;
                loadKLineData(E_K_LINE_TYPE.E_KLT_DAY);
                // 拉取BS点数据
                final String date = TimeUtils.getTimeString("yyyyMMdd", DengtaConst.MILLIS_FOR_SECOND *
                        DengtaApplication.getApplication().getTradingStateManager().getServerTime());
                MarketRequestManager.getBSInfoRequest(mDtSecCode, date,LineChartActivity.DEFAULT_KLINE_LOAD_NUM, this);
                break;
            case LineChartTextureView.TYPE_WEEKLY_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_WEEK;
                loadKLineData(E_K_LINE_TYPE.E_KLT_WEEK);
                break;
            case LineChartTextureView.TYPE_MONTHLY_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_MONTH;
                loadKLineData(E_K_LINE_TYPE.E_KLT_MONTH);
                break;
            case LineChartTextureView.TYPE_ONE_MIN_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_1_MIN;
                loadKLineData(E_K_LINE_TYPE.E_KLT_1_MIN);
                break;
            case LineChartTextureView.TYPE_FIVE_MIN_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_5_MIN;
                loadKLineData(E_K_LINE_TYPE.E_KLT_5_MIN);
                break;
            case LineChartTextureView.TYPE_FIFTEEN_MIN_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_15_MIN;
                loadKLineData(E_K_LINE_TYPE.E_KLT_15_MIN);
                break;
            case LineChartTextureView.TYPE_THIRTY_MIN_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_30_MIN;
                loadKLineData(E_K_LINE_TYPE.E_KLT_30_MIN);
                break;
            case LineChartTextureView.TYPE_SIXTY_MIN_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_60_MIN;
                loadKLineData(E_K_LINE_TYPE.E_KLT_60_MIN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        DtLog.d(TAG, "onResume: " + mStockName);
        boolean isFirstResume = isFirstResume();
        super.onResume();

        registerReceivers();
        doOnResume(isFirstResume);
    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter(TradingStateManager.ACTION_TRADING_STATE_UPDATED);
        filter.addAction(TradingStateManager.ACTION_MARKET_TRADING_STATE_CHANGED);
        filter.addAction(LineChartTextureView.ACTION_TIME_LINE_INDICATOR_TYPE_CHANGED);
        filter.addAction(LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED);
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mBroadcastReceiver);
    }

    private void doOnResume(boolean switchTab) {
        DtLog.d(TAG, "doOnResume: " + mStockName);

        if (!isCurrentPage()) {
            return;
        }

        StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_SHOW);

        if (!StockUtil.isHongKongOrUSA(mDtSecCode)) {
            mRepairType = mKLineSettingConfigure.rightStatus;
        } else {
            mRepairType = KlineSettingConst.K_LINE_UN_REPAIR;
        }
        mKLineControlView.setRepairType(mRepairType);

        mTradingIndicatorType = DengtaSettingPref.loadKLineIndicatorType0();
        mTimeIndicatorType = DengtaSettingPref.loadTimeLineIndicatorType0();
        mKLineControlView.setTradingIndicatorType(mTradingIndicatorType);

        queryTradingValuesAndRefresh(true);

        if(switchTab) {
            final int type = DengtaSettingPref.getLineTypeIndex();
            if(!mTabLayout.switchTab(type) && type == LineChartTextureView.TYPE_TIME) {
                tryLoadStoredCompare();
            }
        } else {
            mPresenter.refresh();
        }

        refreshControlIcon();
    }

    private boolean isCurrentPage() {
        return mIndexInList == mActivity.getCurrentItem();
    }

    private void queryTradingValuesAndRefresh(final boolean needQueryState) {
        if (needQueryState) {
            TradingStateManager tradingStateManager = DengtaApplication.getApplication().getTradingStateManager();
            int marketType = getMarketType();
            int tradingState = tradingStateManager.getTradingState(marketType);
            mIsTrading = tradingState == TradingStateManager.TRADING_STATE_TRADING;
            if (tradingState == TradingStateManager.TRADING_STATE_NOT_INITED) {
                tradingStateManager.loadTradingTimeData();
                return;
            }
        }

        queryTradingValues();
        refreshByTradingState();
    }

    private void refreshByTradingState() {
        setIsTrading(mIsTrading);
        setTradingMinutes(mTradingMinutes);
        setTradingShowTime(mOpenTimeStr, mMiddleTimeStr, mCloseTimeStr);
    }

    public void setIsTrading(final boolean isTrading) {
        mIsTrading = isTrading;
        if (mIsTrading && !isSuspended()) {
            mTimeLineIndicator.startAnimation();
        } else {
            mTimeLineIndicator.stopAnimation();
        }
        mPresenter.setTrading(isTrading);
    }

    private boolean isSuspended() {
        return mQuote != null && mQuote.getESecStatus() == E_SEC_STATUS.E_SS_SUSPENDED;
    }

    private void queryTradingValues() {
        TradingStateManager tradingStateManager = DengtaApplication.getApplication().getTradingStateManager();
        int marketType = getMarketType();

        mTradingMinutes = tradingStateManager.getTradingMinutes(marketType);
        mOpenTimeStr = tradingStateManager.getOpenTimeStr(marketType);
        mMiddleTimeStr = tradingStateManager.getMiddleTimeStr(marketType);
        mCloseTimeStr = tradingStateManager.getCloseTimeStr(marketType);
    }

    public void setTradingShowTime(String openTimeStr, String middleTimeStr, String closeTimeStr) {
        mSurfaceListener.setTimeStr(openTimeStr, middleTimeStr, closeTimeStr);
    }

    public void setTradingMinutes(int tradingMinutes) {
        mSurfaceListener.setTradingMinutes(tradingMinutes);
    }

    @Override
    public void onPause() {
        DtLog.d(TAG, "onPause: " + mStockName);
        super.onPause();

        mKLineSettingManager.saveConfigure();
        unregisterReceivers();

        doOnPause();
    }

    private void doOnPause() {
        DtLog.d(TAG, "doOnPause: " + mStockName);
        if (mPresenter != null) {
            mPresenter.stopRefresh();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mComparePresenter != null) {
            mComparePresenter.releaseDiplayer();
            mComparePresenter = null;
        }
    }

    private void loadFiveDayTimeLineData() {
        DtLog.d(TAG, "loadFiveDayTimeLineData: " + mStockName);
        if (!isCurrentPage()) {
            return;
        }

        //取分时线数据
        DtLog.d(TAG, "loadFiveDayTimeLineData: start " + mStockName);
        if(mSurfaceListener != null) {
            RtMinReq req = new RtMinReq(mDtSecCode, 5, "", 0);
            mSurfaceListener.setDateAndIMinute(req);
            DataEngine.getInstance().request(EntityObject.ET_GET_RTMIN_DATA_EX, req, this);
        }
    }

    private void loadTimeLineCapitalDDZData() {
        if (mSupportCapitalDDZ && SettingPref.getInt(SettingConst.KEY_SETTING_TIME_INDICATORS_TYPE_INDEX_0,
                SettingConst.DEFAULT_TIME_LINE_INDICATOR_TYPE)
                == SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ) {
            QuoteRequestManager.getTimeLineCapitalDDZDataRequest(mDtSecCode, 0, getTimeLineCapitalNum(), this);
        }
    }

    private void loadKLineCapitalDDZData(final int kLineType, int start, int num) {
        if (mSupportCapitalDDZ && supportKLineCapital(kLineType) && mTradingIndicatorType == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW) {
            QuoteRequestManager.getKLineCapitalDDZDataRequest(mDtSecCode, start, num, this);
        }
    }

    private static boolean supportKLineCapital(int lineType) {
        return lineType == E_K_LINE_TYPE.E_KLT_DAY;
    }

    private final DataSourceProxy.IRequestCallback mKLineDataCallback = new DataSourceProxy.IRequestCallback() {
        @Override
        public void callback(boolean success, EntityObject entity) {
            if (!success) {
                // 可以在这里提醒用户错误信息
                int entityType = entity.getEntityType();
                DtLog.w(TAG, "callback requestBriefInfo faild: type = " + entityType);
                ThreadUtils.runOnUiThread(() -> showViewByState(DengtaConst.UI_STATE_FAILED_RETRY));
                return;
            }

            if (!isAdded()) {
                return;
            }

            ThreadUtils.runOnUiThread(() -> showViewByState(DengtaConst.UI_STATE_NORMAL));

            KLineRsp kLineData = (KLineRsp) entity.getEntity();
            final List<KLineDesc> kLineEntities = kLineData.getVKLineDesc();
            final int kLineType = kLineData.getEKLineType();
            setKLineData(kLineEntities, (short) kLineType);
        }
    };

    private void setKLineData(final List<KLineDesc> kLineEntities, final short kLineType) {
        ThreadUtils.runOnUiThread(() -> {
            final int newDataSize = kLineEntities == null ? 0 : kLineEntities.size();
            DtLog.d(TAG, "setKLineData newDataSize : " + newDataSize);
            if (newDataSize == 0) {
                mSurfaceListener.setHasOldestKLineData(true);
                return;
            }

            mKLineRefreshTime.put(kLineType, DengtaApplication.getApplication().getTradingStateManager().getServerTime());
            List<KLineDesc> kLineData = mKLineData.get(kLineType);
            final int oldSize = kLineData == null ? 0 : kLineData.size();
            DtLog.d(TAG, "setKLineData oldSize : " + oldSize);
            if (oldSize == 0) {
                kLineData = kLineEntities;
            } else {
                KLineDesc newData;
                KLineDesc oldData;

                long newTime;
                long oldTime;
                for (int i = newDataSize - 1; i >= 0; i--) {
                    newData = kLineEntities.get(i);
                    newTime = TimeUtils.getCompareTime(newData);
                    final int size = kLineData.size();
                    in: for (int j = size - 1; j >= 0; j--) {
                        oldData = kLineData.get(j);
                        oldTime = TimeUtils.getCompareTime(oldData);

                        if (newTime > oldTime) {
                            kLineData.add(j + 1, newData);
                            break in;
                        } else if (newTime == oldTime) {
                            kLineData.remove(j);
                            kLineData.add(j, newData);
                            break in;
                        }
                    }
                }
            }
            mKLineData.put(kLineType, kLineData);

            DtLog.d(TAG, "setKLineData kLineData.size() : " + kLineData.size());
            mSurfaceListener.setKLineEntities(new ArrayList<>(kLineData), kLineType);
        });
    }

    private void loadKLineData(int kLineType) {
        if (!isCurrentPage()) {
            return;
        }

        QuoteRequestManager.getKLineDataRequest(mDtSecCode, kLineType, 0, getKLineNum(kLineType),
                mRepairType == KlineSettingConst.K_LINE_REPAIR, mKLineDataCallback);
        loadKLineCapitalDDZData(kLineType, 0, getKLineCapitalNum());
    }

    private void loadKLineData(int kLineType, int startIndex, int wantNum) {
        if (!isCurrentPage()) {
            return;
        }

        QuoteRequestManager.getKLineDataRequest(mDtSecCode, kLineType, startIndex, wantNum, mRepairType == KlineSettingConst.K_LINE_REPAIR, this);
        loadKLineCapitalDDZData(kLineType, startIndex, wantNum);
    }

    private int getTimeLineCapitalNum() {
        final int lastServerTime = mTimeLineCapitalRefreshTime;
        if (lastServerTime <= 0) {
            return LineChartActivity.DEFAULT_TIME_LINE_CAPITAL_NUM;
        }

        final int now = DengtaApplication.getApplication().getTradingStateManager().getServerTime();
        final int interval = now - lastServerTime;
        if (interval < 0) {
            return LineChartActivity.DEFAULT_TIME_LINE_CAPITAL_NUM;
        }
        return interval / 60 + 2;
    }

    private int getKLineCapitalNum() {
        final int lastServerTime = mKLineCapitalRefreshTime;
        if (lastServerTime <= 0) {
            return LineChartActivity.DEFAULT_KLINE_LOAD_NUM;
        }

        final int now = DengtaApplication.getApplication().getTradingStateManager().getServerTime();
        final int interval = now - lastServerTime;
        if (interval < 0) {
            return LineChartActivity.DEFAULT_KLINE_LOAD_NUM;
        }
        // 资金流增量最多拉取2天
        return 2;
    }

    private int getKLineNum(final int kLineType) {
        final int lastServerTime = mKLineRefreshTime.get(kLineType);
        if (lastServerTime <= 0) {
            return LineChartActivity.DEFAULT_KLINE_LOAD_NUM;
        }

        final int now = DengtaApplication.getApplication().getTradingStateManager().getServerTime();
        final int interval = now - lastServerTime;
        if (interval < 0) {
            return LineChartActivity.DEFAULT_KLINE_LOAD_NUM;
        }

        int num = 2;
        switch (kLineType) {
            case E_K_LINE_TYPE.E_KLT_DAY:
            case E_K_LINE_TYPE.E_KLT_WEEK:
            case E_K_LINE_TYPE.E_KLT_MONTH:
                num = 2;
                break;
            case E_K_LINE_TYPE.E_KLT_1_MIN:
                num = interval / 60 + 2;
                break;
            case E_K_LINE_TYPE.E_KLT_5_MIN:
                num = interval / (60 * 5) + 2;
                break;
            case E_K_LINE_TYPE.E_KLT_15_MIN:
                num = interval / (60 * 15) + 2;
                break;
            case E_K_LINE_TYPE.E_KLT_30_MIN:
                num = interval / (60 * 30) + 2;
                break;
            case E_K_LINE_TYPE.E_KLT_60_MIN:
                num = interval / (60 * 60) + 2;
                break;
            default:
                break;
        }
        return num;
    }

    public int getMarketType() {
        return StockUtil.convertSecInfo(mDtSecCode).getEMarketType();
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success) {
            // 可以在这里提醒用户错误信息
            DtLog.w(TAG, "callback requestBriefInfo faild");
            ThreadUtils.runOnUiThread(() -> showViewByState(DengtaConst.UI_STATE_FAILED_RETRY));
            return;
        }

        if (!isAdded()) {
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_RTMIN_DATA_EX:
                final RtMinRsp rtMinRsp = (RtMinRsp) entity.getEntity();
                if (rtMinRsp != null) {
                    setFiveDayTimeLineData(rtMinRsp);
                    refreshValueView(LineChartTextureView.TYPE_FIVE_DAY);
                }
                break;
            case EntityObject.ET_GET_CAPITAL_DDZ:
                CapitalDDZRsp rsp = (CapitalDDZRsp) entity.getEntity();
                final String extra = (String) entity.getExtra();
                int type = Integer.parseInt(extra);
                Log.i(TAG, "callback: landscapelineChartFragment的返回资金博弈");

                if (rsp != null) {
                    setCapitalDDZData(rsp, type);
                }
                break;
            case EntityObject.ET_GET_KLINE_DATA:
                Log.d(TAG, "ET_GET_KLINE_DATA");
                final KLineRsp kLineData = (KLineRsp) entity.getEntity();
                final ArrayList<KLineDesc> kLineEntities = kLineData.getVKLineDesc();
                if (kLineEntities != null) {
                    handleKLineDataChange(kLineData, kLineEntities);
                }
                break;
            case EntityObject.ET_GET_SEC_BS_INFO:
                getBSInfoCallback((GetSecBsInfoRsp) entity.getEntity());
                break;
            default:
                break;
        }
    }

    private void getBSInfoCallback(final GetSecBsInfoRsp rsp) {
        final ArrayList<SecBsInfo> bsPointList = rsp.vSecBsInfo;
        ThreadUtils.runOnUiThread(() -> mSurfaceListener.setBSInfos(bsPointList));
    }

    public void updateLiveMsgView(final ArrayList<SecLiveMsg> secLiveMsgs) {
        DtLog.d(TAG, "updateLiveMsgView: secName = " + mStockName);
        if (!isCurrentPage()) {
            return;
        }

        final SecLiveMsg liveMsg = secLiveMsgs.get(0);
        mLastLiveMsgId = liveMsg.getSId();
        DtLog.d(TAG, "updateLiveMsgView: msg = " + liveMsg.getSMsg() + ", id = " + liveMsg.getSId() + ", secName = " + mStockName);

        DengtaApplication application = DengtaApplication.getApplication();
        String cachedLastMsgId = application.getDataCacheManager().getLastLiveMsgId(mDtSecCode);
        DtLog.d(TAG, "updateLiveMsgView: currentId = " + mLastLiveMsgId + ", cachedId = " + cachedLastMsgId);
        if (!TextUtils.equals(mLastLiveMsgId, cachedLastMsgId)) {
            application.getDataCacheManager().setLastLiveMsgId(mDtSecCode, mLastLiveMsgId);
            if (DengtaSettingPref.isStockLiveMsgEnabled()) {
                ThreadUtils.runOnUiThreadDelay(() -> {
                    mLiveMsgView.setData(liveMsg.getSMsg(), liveMsg.getESecLiveMsgType(), liveMsg.getITime(), LiveMessageView.LIVE_TYPE_SEC_DETAIL);
                }, 500);
            }

            application.getDataCacheManager().setHasUnreadLiveMsg(mDtSecCode, true);
        }
    }

    public void setCapitalData(final CapitalFlow capitalFlow) {
        if (mQuote.eSecStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            return;
        }

        if (capitalFlowView != null) {
            capitalFlowView.setData(capitalFlow);
        }
        if (capitalFlowDetailView != null) {
            capitalFlowDetailView.setData(capitalFlow);
        }
    }

    private void setCapitalDDZData(CapitalDDZRsp rsp, int type) {
        DtLog.d(TAG, "setCapitalDDZData: name = " + mStockName + ", type = " + type);
        final ArrayList<CapitalDDZDesc> ddzDescs = rsp.getVCapitalDDZDesc();
        final int newDataSize = ddzDescs == null ? 0 : ddzDescs.size();
        DtLog.d(TAG, "setCapitalDDZData: size = " + newDataSize);
        if (newDataSize == 0) {
            return;
        }
        ThreadUtils.runOnUiThread(() -> {
            switch (type) {
                case E_CAPITAL_DDZ_TYPE.E_CDT_MIN:
                    mTimeLineCapitalRefreshTime = DengtaApplication.getApplication().getTradingStateManager().getServerTime();
                    final int oldSize = mTimeLineCapitals == null ? 0 : mTimeLineCapitals.size();
                    if (oldSize == 0) {
                        mTimeLineCapitals = ddzDescs;
                    } else {
                        CapitalDDZDesc newData;
                        CapitalDDZDesc oldData;

                        long newTime;
                        long oldTime;
                        for (int i = 0; i < newDataSize; i++) {
                            newData = ddzDescs.get(i);
                            newTime = newData.lTime;
                            final int size = mTimeLineCapitals.size();
                            in:
                            for (int j = 0; j < size; j++) {
                                oldData = mTimeLineCapitals.get(j);
                                oldTime = oldData.lTime;

                                if (newTime > oldTime) {
                                    mTimeLineCapitals.add(j, newData);
                                    break in;
                                } else if (newTime == oldTime) {
                                    mTimeLineCapitals.remove(j);
                                    mTimeLineCapitals.add(j, newData);
                                    break in;
                                }
                            }
                        }
                    }
                    DtLog.d(TAG, "setCapitalDDZData: mTimeLineCapitals.size() = " + mTimeLineCapitals.size());
                    mSurfaceListener.setCapitalDDZEntities(new ArrayList<CapitalDDZDesc>(mTimeLineCapitals));
                    break;
                case E_CAPITAL_DDZ_TYPE.E_CDT_DAY:
                    mKLineCapitalRefreshTime = DengtaApplication.getApplication().getTradingStateManager().getServerTime();
                    mSurfaceListener.setKLineCapitalFlowData(ddzDescs);
                    break;
                default:
                    break;
            }
        });
    }

    public void setChipDist(final HisChipDistRsp chipDistRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if (!isAdded()) {
                return;
            }
            if (chipDistRsp != null && mSurfaceListener != null) {
                mSurfaceListener.setChipDist(chipDistRsp);
            }
        });
    }

    public void updateTimeLineData(final ArrayList<TrendDesc> entities) {
        mSurfaceListener.setTimeEntities(entities);
    }

    @Override
    public void updateCompareTimeEntities(final ArrayList<TrendDesc> entities) {
        mSurfaceListener.setCompareTimeEntities(entities);
    }

    @Override
    public void updateCompareQuote(SecQuote quote) {
        updateCompareButton(quote.getSSecName());
        mSurfaceListener.setCompareQuote(quote);
    }

    public void setSupportAverage(boolean support) {
        mSurfaceListener.setSupportAverage(support);
    }

    private void setFiveDayTimeLineData(final RtMinRsp rtMinRsp) {
        ThreadUtils.runOnUiThread(() -> mSurfaceListener.setRtMinDescs(rtMinRsp.getVRtMinDesc()));
    }

    private void handleKLineDataChange(final KLineRsp rsp, final ArrayList<KLineDesc> kLineEntities) {
        ThreadUtils.runOnUiThread(() -> {
            final int size = kLineEntities.size();
            DtLog.d(TAG, "handleKLineDataChange kLineEntities.size() : " + size);
            if (size == 0) {
                mSurfaceListener.setHasOldestKLineData(true);
            } else {
                if (size < LineChartActivity.DEFAULT_KLINE_LOAD_NUM) {
                    mSurfaceListener.setHasOldestKLineData(true);
                }
                final int kLineType = rsp.getEKLineType();
                List<KLineDesc> kLineData = mKLineData.get(kLineType);
                final int oldSize = kLineData == null ? 0 : kLineData.size();
                if (oldSize > 0) {
                    final KLineDesc oldFirstData = kLineData.get(0);
                    final long oldFirstTime = TimeUtils.getCompareTime(oldFirstData);
                    KLineDesc newData;
                    int index = -1;
                    ArrayList<KLineDesc> newEntities = new ArrayList<KLineDesc>();
                    // 查询最后一个数据在新数据里的位置，以便连接上
                    for (int i = size - 1; i >= 0; i--) {
                        newData = kLineEntities.get(i);
                        if (oldFirstTime == TimeUtils.getCompareTime(newData)) {
                            index = i;
                            break;
                        }
                    }
                    DtLog.d(TAG, "handleKLineDataChange index : " + index);

                    if (index == -1) {// 未找到重复的数据，就直接连接
                        newEntities.addAll(kLineEntities);
                    } else if (index >= 0) { // 有部分重复的数据
                        newEntities.addAll(kLineEntities.subList(0, index));
                    } // index == 0 表示数据全部重复，就不用连接了
                    newEntities.addAll(kLineData);
                    kLineData = newEntities;
                } else {
                    kLineData = kLineEntities;
                }
                DtLog.d(TAG, "handleKLineDataChange kLineData.size() : " + kLineData.size());
                mKLineData.put(kLineType, kLineData);
                mSurfaceListener.setKLineEntities(new ArrayList<>(kLineData), kLineType);
            }
        });
    }

    public void setTicksData(TickRsp tickRsp) {
        mTicksView.setTicksData(tickRsp.getVTickDesc());
    }

    public void setTpFlag(int tpFlag) {
        if (mTpFlag != tpFlag) {
            mTpFlag = tpFlag;
            mTicksView.setTpFlag(mTpFlag);
        }
    }

    public void updateQuoteData(final SecQuote quote) {
        if (quote == null) {
            return;
        }

        if (!mSurface.isDetailState()) {
            mQuoteLayout.updateQuote(quote);
        }

        final int index = DengtaSettingPref.getLineTypeIndex();
        if (LineChartSurfaceTextureListener.isTimeLine(index)
            && isSuspended() && quote.eSecStatus != E_SEC_STATUS.E_SS_SUSPENDED
            && mIsTrading) { //交易时段从停牌状态复牌，则小球开始闪烁
            mTimeLineIndicator.startAnimation();
        }

        mQuote = quote;

        setTpFlag(quote.getITpFlag());

        if (mTicksView != null) {
            mTicksView.setYesterdayClose(quote.fClose);
        }

        mSurfaceListener.setQuote(quote);

        setBuySellData(quote);

        boolean isInitialQuote = mQuote == null;
        if(isInitialQuote) {
            refreshValueView(index);
        }

        mQuote = quote;
    }

    private void setBuySellData(SecQuote quote) {
        final BuySellEntity buySellEntity = new BuySellEntity(quote);
        mBuySellView.setData(buySellEntity);
        if (dkView != null) {
            dkView.setBuySellData(buySellEntity);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBuySellFrameLayout) {
            mTabSelector.switchTab(1);
            return;
        } else if (v == mTicksFrameLayout) {
            mTabSelector.switchTab(2);
            return;
        } else if (v == mCapitalFlowFrameLayout) {
            mTabSelector.switchTab(0);
            return;
        }

        switch (v.getId()) {
            case R.id.to_left_button:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TO_PREV);
                if (mActivity != null) {
                    mActivity.toLeft();
                }
                break;
            case R.id.to_right_button:
                StatisticsUtil.reportAction(StatisticsConst.FULLSCREEN_CHART_TO_NEXT);
                if (mActivity != null) {
                    mActivity.toRight();
                }
                break;
            case R.id.close_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void refreshControlIcon() {
        boolean isLeftMost = false;
        boolean isRightMost = false;
        int pageCount = mActivity.getPageCount();
        int currentItem = mActivity.getCurrentItem();
        if (currentItem == 0) {
            isLeftMost = true;
        }
        if (currentItem == pageCount - 1) {
            isRightMost = true;
        }
        if (isLeftMost) {
            mToLeftButton.setImageResource(R.drawable.to_left_disabled);
        } else {
            mToLeftButton.setImageResource(R.drawable.to_left);
        }
        if (isRightMost) {
            mToRightButton.setImageResource(R.drawable.to_right_disabled);
        } else {
            mToRightButton.setImageResource(R.drawable.to_right);
        }
    }

    public void onPageChanged(int index) {
        DtLog.d(TAG, "onPageChanged: index = " + index + ", " + mStockName);
        if (mIndexInList == index) {
            doOnResume(true);
        } else {
            doOnPause();
        }
    }

    public void showViewByState(int state) {
        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                if (LineChartSurfaceTextureListener.isTimeLine(DengtaSettingPref.getLineTypeIndex()) && mIsTrading
                    && !isSuspended()) {
                    mTimeLineIndicator.startAnimation();
                }
                mSurface.setVisibility(View.VISIBLE);
                cancelShowLoadingView();
                mFailRetryLayout.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_LOADING:
                mTimeLineIndicator.stopAnimation();
                showLoadingView();
                mFailRetryLayout.setVisibility(View.GONE);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mLoadingLayout.setVisibility(View.GONE);
                if (!hasCachedData()) {
                    mTimeLineIndicator.stopAnimation();
                    mSurface.setVisibility(View.GONE);
                    cancelShowLoadingView();
                    mFailRetryLayout.setVisibility(View.VISIBLE);
                } else {
                    mSurface.setVisibility(View.VISIBLE);
                    mSurfaceListener.requestRender();
                }
                break;
            default:
                break;
        }
    }

    private boolean hasCachedData() {
        int lineType = mSurfaceListener.getLineType();
        if (LineChartSurfaceTextureListener.isTimeLine(lineType)) {
            return mSurfaceListener.getTimeEntities(lineType) != null;
        } else {
            return mSurfaceListener.getKLineEntities(mSurfaceListener.getServerKLineType(lineType)) != null;
        }
    }

    private Runnable mShowLoadingViewRunnable = new Runnable() {
        @Override
        public void run() {
            if (!hasCachedData()) {
                mSurface.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private void showLoadingView() {
        cancelShowLoadingView();
        ThreadUtils.runOnUiThreadDelay(mShowLoadingViewRunnable, 500);
    }

    private void cancelShowLoadingView() {
        mLoadingLayout.setVisibility(View.GONE);
        ThreadUtils.removeCallbacksOnUiThread(mShowLoadingViewRunnable);
    }

    public void showCompareSelectPopupWindow() {
        if(mCompareSelectPopupWindow == null) {
            mCompareSelectPopupWindow = new CompareSelectPopupWindow(getContext());
            mCompareSelectPopupWindow.setOnItemClick(new CompareSelectPopupWindow.OnItemClick() {
                @Override
                public void onMarketClick(String marketDtCode) {
                    tryLoadCompare(marketDtCode);
                }

                @Override
                public void onCustomClick() {
                    Bundle extra = new Bundle();
                    extra.putString(CommonConst.KEY_SEC_CODE, mDtSecCode);
                    CommonBeaconJump.showSearchPicker(getContext(), extra, DengtaConst.REQUEST_SEARCH_PICK);
                }

                @Override
                public void onRemoveClick() {
                    removeCompare();
                }
            });
        }
        mCompareSelectPopupWindow.showCompareSelectPopupWindow(mDtSecCode, mSelectCompareStockButton, Gravity.RIGHT);
    }

    private void updateCompareButton(String name) {
        ThreadUtils.runOnUiThread(() -> {
            if(isAdded()) {
                if(TextUtils.isEmpty(name)) {
                    mSelectCompareStockButton.setText(getString(R.string.compare_stock));
                } else {
                    mSelectCompareStockButton.setText(getString(R.string.compare_stock_added, name));
                }
            }
        });
    }

    public void selectCustomStock(String dtSecCode) {
        DtLog.d(TAG, "selectCustomStock() dtSecCode = " + dtSecCode);
        if(!TextUtils.isEmpty(dtSecCode)) {
            tryLoadCompare(dtSecCode);
        }
    }
}
