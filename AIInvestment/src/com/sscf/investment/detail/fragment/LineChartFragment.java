package com.sscf.investment.detail.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.PopupWindow;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.dengtacj.request.MarketRequestManager;
import com.dengtacj.request.QuoteRequestManager;
import com.iflytek.cloud.Setting;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.TabLayout;
import com.sscf.investment.component.ui.widget.TwoTabSelectorView;
import com.sscf.investment.detail.LineChartActivity;
import com.sscf.investment.detail.SecurityDetailActivity;
import com.sscf.investment.detail.dialog.BottomDialog;
import com.sscf.investment.detail.dialog.BottomDoubleButtonDialog;
import com.sscf.investment.detail.entity.BuySellEntity;
import com.sscf.investment.detail.presenter.SecurityDetailPresenter;
import com.sscf.investment.detail.presenter.TimeLineComparePresenter;
import com.sscf.investment.detail.presenter.TimelineDetailPresenter;
import com.sscf.investment.detail.view.BuySellView;
import com.sscf.investment.detail.view.CapitalFlowDetailView;
import com.sscf.investment.detail.view.CapitalFlowView;
import com.sscf.investment.detail.view.CompareSelectPopupWindow;
import com.sscf.investment.detail.view.DKView;
import com.sscf.investment.detail.view.HorizontalPopupEntranceGroup;
import com.sscf.investment.detail.view.IndicatorAnimationView;
import com.sscf.investment.detail.view.InnerScrollView;
import com.sscf.investment.detail.view.KLineInfosView;
import com.sscf.investment.detail.view.LineChartSurfaceTextureListener;
import com.sscf.investment.detail.view.LineChartTextureView;
import com.sscf.investment.detail.view.LineChartValueView;
import com.sscf.investment.detail.view.TicksView;
import com.sscf.investment.detail.view.TimeLineInfosView;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.popup.PopupWindowNewFunctionUp;
import com.sscf.investment.popup.PopupWindowNewFunctionUpLeft;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.main.manager.TradingStateManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.StatManager;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.model.KlineSettingConst;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.widget.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import BEC.BEACON_STAT_TYPE;
import BEC.CapitalDDZDesc;
import BEC.CapitalDDZMultiRsp;
import BEC.CapitalDDZRsp;
import BEC.CapitalFlow;
import BEC.E_CAPITAL_DDZ_TYPE;
import BEC.E_K_LINE_TYPE;
import BEC.E_SEC_STATUS;
import BEC.GetSecBsInfoRsp;
import BEC.HisChipDistRsp;
import BEC.KLineDesc;
import BEC.KLineRsp;
import BEC.RtMinReq;
import BEC.RtMinRsp;
import BEC.SecBsInfo;
import BEC.SecQuote;
import BEC.TickRsp;
import BEC.TrendDesc;
import BEC.TrendRsp;

import static android.R.attr.type;

/**
 * Created by liqf on 2015/8/12.
 */
public class LineChartFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, TimeLineComparePresenter.ITimeLineCompareDisplayer, Runnable,
        View.OnClickListener, BottomDoubleButtonDialog.OnDialogButtonClickListener {
    private static final String TAG = LineChartFragment.class.getSimpleName();
    private static final int ANIMATE_ENLARGE_TIMELINE_ENTRANCE = 1000;
    private static final int UPDATE_ENTRANCE_POSITION = 1001;
    private static final int ANIMATE_INTERVAL = 1100;
    private Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIMATE_ENLARGE_TIMELINE_ENTRANCE:
                    if(needShowEnlargeTimeline()) {
                        animateEnlargeTimeLineEntrance();
                        mUiHandler.sendEmptyMessageDelayed(ANIMATE_ENLARGE_TIMELINE_ENTRANCE, ANIMATE_INTERVAL);
                    } else {
                        finishEnlargeTimeLineEntrance();
                    }
                    break;
                case UPDATE_ENTRANCE_POSITION:
                    if (null != mEntranceGroupView) {
                        int height = msg.arg1;
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mEntranceGroupView.getLayoutParams();
                        params.setMargins(0, height - mEntranceGroupView.getHeight(), 0, 0 );
                        mEntranceGroupView.setLayoutParams(params);
                    }
                    break;
                default:
            }
        }
    };

    private LineChartTextureView mSurface;//画线
    private LineChartSurfaceTextureListener mSurfaceListener;
    private TimelineDetailPresenter mDetailPresenter;

    private LineChartValueView mValueView;

    private IndicatorAnimationView mTimeLineIndicator;
    private HorizontalPopupEntranceGroup mEntranceGroupView;
    private TabLayout mTabLayout;
    private View mTransactionLayout;
    private TwoTabSelectorView mTabSelector;
    private BuySellView mBuySellView;//五档
    private TicksView mTicksView;//明细
    private InnerScrollView mTicksScrollView;
    private LinearLayout capitalFlowLayout;//资金
    private CapitalFlowView capitalFlowView;
    private CapitalFlowDetailView capitalFlowDetailView;
    private DKView dkView;
    private ImageView tradeCloseBtn;
    private boolean tradeDetailIsShow;

    private String mDtSecCode;
    private String mStockName;
    private boolean mSupportCapitalDDZ;

    private int mRepairType = KlineSettingConst.DEFAULT_K_LINE_REPAIR_TYPE;
    private boolean mIsTrading;
    private boolean mIsCallauction;

    private SecQuote mQuote;
    private int mTpFlag = DengtaConst.DEFAULT_TP_FLAG;

    private View mLoadingLayout;
    private View mFailRetryLayout;
    private ImageView mEnlargeTimeLineEntrance;
    private Button mCallauctionEntrance;
    private Button mSelectCompareStockButton;
    private ViewGroup mSelectCompareStockButtonGroup;

    private boolean mIsResume = false;
    private boolean mAutoShowEnlargeDialog = false;

    private int mKLineType;

    private final SparseArray<List<KLineDesc>> mKLineData = new SparseArray<>();
    private final SparseIntArray mKLineRefreshTime = new SparseIntArray();

    private int mTimeLineCapitalRefreshTime;
    private ArrayList<CapitalDDZDesc> mTimeLineCapitals;
    private ArrayList<CapitalDDZDesc> mFiveDayLineCapitals;//五日资金博弈
    private int mKLineCapitalRefreshTime;

    private PeriodicHandlerManager mPeriodicHandlerManager;

    private SecurityDetailPresenter mPresenter;

    private boolean mIsIndex = false;

    private ArrayList<TrendDesc> mTimeLineDatas;

    private TimeStatHelper mStatHelper;

    private CompareSelectPopupWindow mCompareSelectPopupWindow;

    private boolean isShowCandidatorGuided;

    private KLineSettingManager mKlineConfigureManager;
    private KLineSettingConfigure mKlineConfigure;

    /**
     * 网络广播注册后马上会收到一次，对这一条不做处理
     */
    private boolean mFirstNetStateBroadcast = true;
    private BroadcastReceiver mNetStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mFirstNetStateBroadcast) {
                mFirstNetStateBroadcast = false;
                return;
            }

            boolean down = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (!down) {
                mPeriodicHandlerManager.runPeriodic();
            }
        }
    };

    /**
     * 分时图是嵌入在当前可见页时才进行绘制，主要避免个股列表翻卡片时多个TextureView同时在绘制
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DtLog.i(TAG, "onReceive action : " + action);
            if (LineChartTextureView.ACTION_TIME_LINE_INDICATOR_TYPE_CHANGED.equals(action)) {//指标切换
                int hashCode = intent.getIntExtra(LineChartTextureView.KEY_CHART_VIEW_HASH_CODE, -1);
                if (hashCode == mSurface.hashCode()) {
                    StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_SEC_DETAIL_TECHNOLOGY, StatConsts.SWITCH_INDICATOR_TYPE);
                    loadTimeLineCapitalDDZData();
                    loadFiveDayTimeLineCapitalDDZData();
                    Log.i(TAG, "onReceive: 指标切换完成");
                }
            } else if (LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED.equals(action)) {
                int hashCode = intent.getIntExtra(LineChartTextureView.KEY_CHART_VIEW_HASH_CODE, -1);
                if (hashCode == mSurface.hashCode()) {
                    StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_SEC_DETAIL_TECHNOLOGY, StatConsts.SWITCH_INDICATOR_TYPE);
                    loadKLineCapitalDDZData(mKLineType);
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKlineConfigureManager = DengtaApplication.getApplication().getKLineSettingManager();
        mKlineConfigure = mKlineConfigureManager.getKlineSettingConfigure();
        isShowCandidatorGuided = SettingPref.getBoolean(SettingConst.KEY_CANDIDATOR_GUIDE, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mStockName = args.getString(DengtaConst.KEY_SEC_NAME).trim();
            mSupportCapitalDDZ = StockUtil.supportCapitalDDZ(mDtSecCode);
        }

        DtLog.d(TAG, "onCreateView: mDtSecCode = " + mDtSecCode + ", mStockName = " + mStockName);
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_line_chart, container, false);

        mTabLayout = (TabLayout) contextView.findViewById(R.id.line_tabs);
        mTabLayout.initWithTitles(new int[]{R.string.tab_one_day, R.string.tab_five_day, R.string.tab_daily_k, R.string.tab_weekly_k, R.string.tab_monthly_k,
            R.string.tab_one_minute_k, R.string.tab_five_minutes_k, R.string.tab_fifteen_minutes_k,
            R.string.tab_thirty_minutes_k, R.string.tab_sixty_minutes_k}, null, 6, R.string.tab_minutes_k);

        mRepairType = loadKLineRepairType();

        mStatHelper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_DETAIL_KLINE);

        mTabLayout.setOnTabSelectionListener(new TabLayout.OnTabSelectionListener() {
            @Override
            public void onTabSelected(int index) {
                DtLog.i(TAG, "onTabSelected() mStockName = " + mStockName);
                showIndicatorGuide();
                final int currentIndex = DengtaSettingPref.getLineTypeIndex();
                int currentKey = getStatKey(currentIndex);
                int newKey = getStatKey(index);
                if(currentKey != newKey) {
                    mStatHelper.end();
                    mStatHelper.setKey(newKey);
                    mStatHelper.start();
                }
                DengtaSettingPref.setLineTypeIndex(index);

                statTabSwitch(index);
                //切换显示的线图类型
                boolean isStockCompareShow = false;

                switch (index) {
                    case LineChartTextureView.TYPE_TIME:
                        if (!StockUtil.hasTransaction(mDtSecCode)) {
                            mTransactionLayout.setVisibility(View.GONE);
                            tradeCloseBtn.setVisibility(View.GONE);
                        } else {
                            tradeCloseBtn.setVisibility(View.VISIBLE);
                            if (SettingPref.getBoolean(SettingConst.KEY_SETTING_TRANSATICON_OPEN, true)) {
                                mTransactionLayout.setVisibility(View.VISIBLE);
                            } else {
                                mTransactionLayout.setVisibility(View.GONE);
                            }
                        }

                        tryLoadStoredCompare();
                        isStockCompareShow = StockUtil.supportStockCompare(mDtSecCode);
                        break;
                    case LineChartTextureView.TYPE_FIVE_DAY:
                        mTransactionLayout.setVisibility(View.GONE);
                        tradeCloseBtn.setVisibility(View.GONE);
                        break;
                    default:
                        mTransactionLayout.setVisibility(View.GONE);
                        tradeCloseBtn.setVisibility(View.GONE);
                }

                mSelectCompareStockButtonGroup.setVisibility(isStockCompareShow ? View.VISIBLE : View.GONE);

                if(mSurfaceListener.getLineType() != index) {
                    checkEntrance(index);
                }

                if(mSurfaceListener.getLineType() != index) {
                    mSurface.exitDetailState();
                    mValueView.setLineType(index);
                }
                mSurfaceListener.onTabSelected(index);
                refreshValueView(index);
                mTimeLineIndicator.updateCenterPoint(new Point(0, 0));
                showEntranceGroupViewIfNeed(index);
                mPeriodicHandlerManager.runPeriodicDelay(50);
                if (mPresenter != null) {
                    mPresenter.onKLineTouch(null);
                }
            }
        });

        mFailRetryLayout = contextView.findViewById(R.id.fail_retry);
        mFailRetryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeriodicHandlerManager.runPeriodic();
            }
        });
        getDetailPresenter().setDtSecCode(mDtSecCode);
        mCallauctionEntrance = (Button) contextView.findViewById(R.id.callauction_entrance);
        mCallauctionEntrance.setOnClickListener((View v)-> {
            getDetailPresenter().showDialog((BaseFragmentActivity) getActivity(), BottomDialog.CALLAUCTION_LINE);
        });

        mSelectCompareStockButtonGroup = (ViewGroup) contextView.findViewById(R.id.select_compare_stock_button_group);
        mSelectCompareStockButton = (Button)contextView.findViewById(R.id.select_compare_stock_button);
        mSelectCompareStockButton.setOnClickListener(v -> showCompareSelectPopupWindow());


        mEnlargeTimeLineEntrance = (ImageView) contextView.findViewById(R.id.enlarge_timeline_entrance);
        mEnlargeTimeLineEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimeUtils.isFrequentOperation()) {
                    return;
                }
                if(mSurface.isDetailState()) {
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_CLICK_ENLARGE_TIME_LINE_GUIDE_ENTRANCE);
                    getDetailPresenter().showDialog((BaseFragmentActivity) getActivity(), BottomDialog.ENLARGE_TIME_LINE);
                } else {
                    mAutoShowEnlargeDialog = true;
                    mUiHandler.postDelayed(() -> mAutoShowEnlargeDialog = false, 500);
                    mSurface.enterDetailState();
                }
            }
        });

        mLoadingLayout = contextView.findViewById(R.id.loading_layout);

        mTimeLineIndicator = (IndicatorAnimationView) contextView.findViewById(R.id.line_chart_indicator);

        initEntranceGroup(contextView);

        mSurface = (LineChartTextureView) contextView.findViewById(R.id.line_chart);
        mSurface.setIsTouchMode(true);
        mSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimeUtils.isFrequentOperation()) {
                    return;
                }
                SecurityDetailActivity activity = (SecurityDetailActivity) getActivity();
                if(activity != null) {
                    ArrayList<SecListItem> secItems = activity.getSecItems();
                    Intent intent = new Intent(activity, LineChartActivity.class);
                    intent.putExtra(DengtaConst.KEY_SEC_NAME, mStockName);
                    intent.putExtra(DengtaConst.KEY_SEC_CODE, mDtSecCode);
                    intent.putExtra(DengtaConst.KEY_SEC_LIST, secItems);
                    intent.putExtra(LineChartActivity.KEY_LINE_TYPE, mSurfaceListener.getLineType());
                    activity.startActivityForResult(intent, DengtaConst.REQUEST_FULLSCREEN_LINECHART);
                }
            }
        });

        mSurface.setOnStateChangeListener(new LineChartTextureView.OnStateChangeListener() {
            @Override
            public void onStateChange(int oldState, int newState) {
                DtLog.d(TAG, "oldState = " + oldState + ", newState = " + newState);
                if(oldState == LineChartTextureView.STATE_CANCEL_DETAIL && newState == LineChartTextureView.STATE_NORMAL) {
                    int lineType = mSurfaceListener.getLineType();
                    if(lineType == LineChartTextureView.TYPE_TIME ||
                            lineType ==  LineChartTextureView.TYPE_FIVE_DAY) {
                        refreshValueView(lineType);
                    }
                    mPresenter.onKLineTouch(null);
                }
            }
        });

        mSurface.setOnEntranceClickListener(new LineChartTextureView.OnEntranceClickListener() {
            @Override
            public void onEntranceClick(int lineType) {
                ThreadUtils.runOnUiThread(() -> {
                    switch (lineType) {
                        case LineChartTextureView.TYPE_TIME:
                            getDetailPresenter().showDialog((BaseFragmentActivity) getActivity(), BottomDialog.ENLARGE_TIME_LINE);
                            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_CLICK_ENLARGE_TIME_LINE_ENTRANCE);
                            break;
                        case LineChartTextureView.TYPE_DAILY_K:
                            getDetailPresenter().showDialog((BaseFragmentActivity) getActivity(), BottomDialog.HISTORY_TIME_LINE);
                            break;
                        default:
                            break;
                    }
                });
            }
        });

        mSurfaceListener = new LineChartSurfaceTextureListener(getActivity());
        if(mTradingMinutes > 0) {
            mSurfaceListener.setTradingMinutes(mTradingMinutes);
        }
        mSurfaceListener.setDtSecCode(mDtSecCode);
        mSurfaceListener.setTimeStr(mOpenTimeStr, mMiddleTimeStr, mCloseTimeStr);
        mSurface.setSurfaceTextureListener(mSurfaceListener);
        mSurfaceListener.setTimeValueChangeListener(new LineChartTextureView.TimeValueChangeListener() {
            @Override
            public void onValuePositionChanged(final Point point) {
                ThreadUtils.runOnUiThread(() -> {
                    if(point != null && mSelectCompareStockButtonGroup != null && mSelectCompareStockButtonGroup.getVisibility() == View.VISIBLE) {
                        point.y = point.y + mSelectCompareStockButtonGroup.getHeight();
                    }
                    mTimeLineIndicator.updateCenterPoint(point);
                });
            }
        });
        mSurfaceListener.setIsIndex(mIsIndex);

        mValueView = (LineChartValueView) contextView.findViewById(R.id.line_chart_value_view);
        mValueView.setDtSecCode(mDtSecCode);

        mSurfaceListener.setKLineEventListener(new LineChartTextureView.KLineEventListener() {
            @Override
            public void onMoreDataNeeded(final int lineType, final int start, final int wantNum) {
            }

            @Override
            public void onTimeLineTouchChanged(final TimeLineInfosView.TimeLineTouchEvent event) {
                ThreadUtils.runOnUiThread(() -> {
                    final int action = event.getAction();

                    //刷新顶栏信息文字
                    switch (action) {
                        case TimeLineInfosView.TimeLineTouchEvent.ACTION_DOWN:
                            mValueView.setTimeLineEvent(event);
                            getDetailPresenter().updateTimeLineTouchEvent(event);
                            if(mAutoShowEnlargeDialog) {
                                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_CLICK_ENLARGE_TIME_LINE_GUIDE_ENTRANCE);
                                getDetailPresenter().showDialog((BaseFragmentActivity) getActivity(),  BottomDialog.ENLARGE_TIME_LINE);
                                mAutoShowEnlargeDialog = false;
                            }
                            break;
                        case TimeLineInfosView.TimeLineTouchEvent.ACTION_MOVE:
                            mValueView.setTimeLineEvent(event);
                            getDetailPresenter().updateTimeLineTouchEvent(event);
                            break;
                        case TimeLineInfosView.TimeLineTouchEvent.ACTION_UP:
                            break;
                        default:
                            break;
                    }
                });
            }

            @Override
            public void onKLineTouchChanged(final KLineInfosView.KLineLineTouchEvent event) {
                ThreadUtils.runOnUiThread(() -> {
                    final int action = event.getAction();
                    mPresenter.onKLineTouch(event);
                    //刷新顶栏信息文字
                    switch (action) {
                        case KLineInfosView.KLineLineTouchEvent.ACTION_DOWN:
                            getDetailPresenter().updateKLineTouchEvent(event);
                            break;
                        case KLineInfosView.KLineLineTouchEvent.ACTION_MOVE:
                            getDetailPresenter().updateKLineTouchEvent(event);
                            TimelineDetailPresenter presenter = getDetailPresenter();
                            presenter.updateKLineTouchEvent(event);
                            break;
                        case KLineInfosView.KLineLineTouchEvent.ACTION_UP:
                            break;
                        default:
                            break;
                    }
                });
            }

            @Override
            public void onKLineAverageChanged(KLineInfosView.KLineAverageInfo averageInfo) {
                ThreadUtils.runOnUiThread(() -> {
                    mValueView.setAverageInfo(averageInfo);
                });
            }

            @Override
            public void onKLineHeightUpdate(int kChartHeight) {
                Message msg = Message.obtain();
                msg.what = UPDATE_ENTRANCE_POSITION;
                msg.arg1 = kChartHeight;
                mUiHandler.sendMessage(msg);
            }
        });

        initTransactionLayout(contextView);

        mTabLayout.switchTab(DengtaSettingPref.getLineTypeIndex());
        mTabSelector.switchTab(DataPref.getDashBoardIndex());
        DtLog.i(TAG, "mTabLayout.switchTab(DengtaSettingPref.getLineTypeIndex())");
        showIndicatorGuide();

        return contextView;
    }

    private PopupWindow mPopupGuide;

    private void showIndicatorGuide(){
        mUiHandler.postDelayed(() ->  {
            if (null != getActivity() && !isShowCandidatorGuided && mSurfaceListener.getLineType() > 1)
            {
                SettingPref.putBoolean(SettingConst.KEY_CANDIDATOR_GUIDE, true);
                if (mPopupGuide == null) {
                    mPopupGuide = new PopupWindowNewFunctionUp(getActivity(), R.string.guide_intro_indicator);
                    mPopupGuide.setOnDismissListener(() -> {
                            PopupWindowNewFunctionUpLeft popupWindowNewFunctionUpLeft =
                                    new PopupWindowNewFunctionUpLeft(getActivity(), R.string.guide_intro_indicator1);
                            popupWindowNewFunctionUpLeft.showAsDropDown(mSurface,
                                    DeviceUtil.dip2px(getActivity(), 120),//60为中间10+文字高度
                                    - DeviceUtil.dip2px(getActivity(), 45));
                    });
                    mPopupGuide.showAsDropDown(mSurface,
                            DeviceUtil.dip2px(getActivity(), 5),//60为中间10+文字高度
                            -mSurfaceListener.getIndicatorHeight()*2 - DeviceUtil.dip2px(getActivity(), 60));
                }
            }
        }, 300);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        doOnResume();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doOnResume();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        doOnPause();
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        doOnPause();
    }

    public boolean isDetailState() {
        if(mSurface != null) {
            return mSurface.isDetailState();
        }
        return false;
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

    private TimelineDetailPresenter getDetailPresenter() {
        if(mDetailPresenter == null) {
            mDetailPresenter = new TimelineDetailPresenter(this, this);
        }
        return mDetailPresenter;
    }

    private void refreshValueView(int lineType) {
        if(LineChartSurfaceTextureListener.isTimeLine(lineType)) {
            if(mSurfaceListener.getLineType() == lineType && !mSurface.isDetailState()) {
                if(mQuote != null) {
                    ThreadUtils.runOnUiThread(() -> {
                        TrendDesc latestEntity = mSurfaceListener.getLatestEntity(lineType);
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
        mCompareSelectPopupWindow.showCompareSelectPopupWindow(mDtSecCode, mSelectCompareStockButtonGroup, Gravity.LEFT);
    }

    private void removeCompare() {
        mComparePresenter = null;
        mSurfaceListener.removeCompare();
        updateCompareButton("");
    }

    public void setPresenter(SecurityDetailPresenter presenter) {
        mPresenter = presenter;
    }

    private void statTabSwitch(int index) {
        switch (index) {
            case LineChartTextureView.TYPE_TIME://分时
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TIME_LINE_CLICKED);
                break;
            case LineChartTextureView.TYPE_FIVE_DAY://5日
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FIVE_DAY_CLICKED);
                break;
            case LineChartTextureView.TYPE_DAILY_K://日K
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_DAILY_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_WEEKLY_K://周K
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_WEEKLY_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_MONTHLY_K://月K
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MONTHLY_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_ONE_MIN_K://1分钟
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_ONE_MIN_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_FIVE_MIN_K://5分钟
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FIVE_MIN_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_FIFTEEN_MIN_K://15分钟
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_FIFTEEN_MIN_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_THIRTY_MIN_K://30分钟
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_THIRTY_MIN_K_CLICKED);
                break;
            case LineChartTextureView.TYPE_SIXTY_MIN_K://60分钟
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SIXTY_MIN_K_CLICKED);
                break;
            default:
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_OTHER_K_CLICKED);
                break;
        }
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
                        case R.drawable.entrance_bs://多空信号
                            openBsSignalPage();
                            break;
                        case R.drawable.entrance_similar_k://相似K线
                            openSimilarKPage();
                            break;
                        case R.drawable.entrance_history://回顾历史
                            openSecHistoryPage();
                            break;
                        default:
                            break;
                    }
                }
            });
            entranceGroupView.setVisibility(View.VISIBLE);
            mEntranceGroupView = entranceGroupView;
        }
    }

    private int getIndicatorsHeight() {
        int indicatorNum = DengtaApplication.getApplication().
                getKLineSettingManager().getKlineSettingConfigure().indicatorNum;
        int height = indicatorNum * getIndicatorHeightByIndicatorNum(getResources(), indicatorNum);

        return height;
    }

    private int getIndicatorHeightByIndicatorNum(Resources res, int indicatorNum) {
        if (indicatorNum == 1) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_1);
        }else if (indicatorNum == 2) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_2);
        }else if (indicatorNum == 3) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_3);
        }else if (indicatorNum == 4) {
            return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_4);
        }
        return res.getDimensionPixelSize(R.dimen.line_chart_bottom_rect_height_2);
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

    private void reloadDataByIndex(int index) {
        DtLog.i(TAG, "reloadDataByIndex: index = " + index + ", secName = " + mStockName);

        showViewByState(DengtaConst.UI_STATE_LOADING);

        switch (index) {
            case LineChartTextureView.TYPE_TIME:
                mKLineType = -1;
                loadTimeLineData();
                tryLoadStoredCompare();
                loadTimeLineCapitalDDZData();
                break;
            case LineChartTextureView.TYPE_FIVE_DAY:
                mKLineType = -1;
                loadFiveDayTimeLineData();
                loadFiveDayTimeLineCapitalDDZData();
                break;
            case LineChartTextureView.TYPE_DAILY_K:
                mKLineType = E_K_LINE_TYPE.E_KLT_DAY;
                loadKLineData(E_K_LINE_TYPE.E_KLT_DAY);
                // 拉取BS点数据
                final String date = TimeUtils.getTimeString("yyyyMMdd", DengtaConst.MILLIS_FOR_SECOND *
                        DengtaApplication.getApplication().getTradingStateManager().getServerTime());
//                MarketRequestManager.getBSInfoRequest(mDtSecCode, date,LineChartActivity.DEFAULT_KLINE_LOAD_NUM, this);
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

    private int loadKLineRepairType() {
        return mKlineConfigure.rightStatus;
    }

    private void initTransactionLayout(View contextView) {
        mTransactionLayout = contextView.findViewById(R.id.transaction_layout);
        mTabSelector = (TwoTabSelectorView) contextView.findViewById(R.id.tab_selector);
        mTabSelector.setAttributes(R.attr.transaction_two_tab_textColor, R.attr.transaction_two_tab_selector_view_bg_drawable,
            R.attr.transaction_two_tab_selector_selected_bg_left_drawable, R.attr.transaction_two_tab_selector_selected_bg_right_drawable,
                R.attr.transaction_two_tab_selector_selected_bg_middle_drawable);

        if (StockUtil.hasTransaction(mDtSecCode) && !StockUtil.isNewThirdBoard(mDtSecCode)
                && !StockUtil.isFund(mDtSecCode) && !StockUtil.isChineseStockB(mDtSecCode)) {
            mTabSelector.setTabTitles(R.string.transaction_buy_sell, R.string.transaction_ticks, R.string.transaction_capital);
        } else {
            // 新三板、场内基金不显示资金tab
            mTabSelector.setTabTitles(R.string.transaction_buy_sell, R.string.transaction_ticks);
        }

        tradeCloseBtn = (ImageView)contextView.findViewById(R.id.close_triangle_trade_detail);
        tradeCloseBtn.setOnClickListener(this);
        if (StockUtil.hasTransaction(mDtSecCode)) {
            tradeCloseBtn.setVisibility(View.VISIBLE);
            if (SettingPref.getBoolean(SettingConst.KEY_SETTING_TRANSATICON_OPEN, true)) {
                tradeDetailIsShow = true;
                mTransactionLayout.setVisibility(View.VISIBLE);
            } else {
                tradeDetailIsShow = false;
                mTransactionLayout.setVisibility(View.GONE);
            }
            tradeCloseBtn.setImageResource(tradeDetailIsShow ? R.drawable.open_triangle : R.drawable.close_triangle);
        } else {
            tradeCloseBtn.setVisibility(View.GONE);
            mTransactionLayout.setVisibility(View.GONE);
        }

        mTabSelector.setTabTitlesSize(R.dimen.font_size_14, R.dimen.font_size_14);
        mTabSelector.setOnTabSelectedListener(new TwoTabSelectorView.OnTabSelectedListener() {
            @Override
            public void onFirstTabSelected() {//五档
                DataPref.setDashBoardIndex(0);
                mBuySellView.setVisibility(View.VISIBLE);
                mTicksScrollView.setVisibility(View.INVISIBLE);
                capitalFlowLayout.setVisibility(View.INVISIBLE);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TIME_LINE_SIDE_CLICKED);
            }

            @Override
            public void onSecondTabSelected() {//明细
                DataPref.setDashBoardIndex(1);
                mBuySellView.setVisibility(View.INVISIBLE);
                mTicksScrollView.setVisibility(View.VISIBLE);
                capitalFlowLayout.setVisibility(View.INVISIBLE);

                // 切换成交明细，立即手动更新一次
                getDetailPresenter().requestTick(mDtSecCode);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TIME_LINE_SIDE_CLICKED);
                StatisticsUtil.reportAction(StatisticsConst.A_INDICIDUAL_SHARE_WUDANG_SWITCH);
            }

            @Override
            public void onThirdTabSelected() {//资金
                DataPref.setDashBoardIndex(2);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TICKS_CLICKED);
                mBuySellView.setVisibility(View.INVISIBLE);
                mTicksScrollView.setVisibility(View.INVISIBLE);
                capitalFlowLayout.setVisibility(View.VISIBLE);

                if (mQuote != null && mQuote.eSecStatus != E_SEC_STATUS.E_SS_SUSPENDED) {
                    // 切换资金，立即手动更新一次
                    QuoteRequestManager.getCapitalDataRequest(mDtSecCode, LineChartFragment.this);
                }
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_TIME_LINE_SIDE_CLICKED);
            }
        });
        mBuySellView = (BuySellView) contextView.findViewById(R.id.buy_sell);
        mBuySellView.setOnClickListener(this);

        SecQuote quote = DengtaApplication.getApplication().getDataCacheManager().getSecQuote(mDtSecCode);
        if (quote != null) {
            setBuySellData(quote);
        }

        mTicksScrollView = (InnerScrollView) contextView.findViewById(R.id.ticks_layout);
        mTicksView = (TicksView) contextView.findViewById(R.id.ticks);
        mTicksView.setOnClickListener(this);
        capitalFlowLayout = (LinearLayout)contextView.findViewById(R.id.capital_flow_layout);
        capitalFlowLayout.setOnClickListener(this);
        capitalFlowView = (CapitalFlowView)contextView.findViewById(R.id.capital_flow_pie_transaction);
        capitalFlowView.setType(CapitalFlowView.TYPE_TRANSACTION_PIE);
        capitalFlowDetailView = (CapitalFlowDetailView)contextView.findViewById(R.id.capital_flow_detail_view);
        dkView = (DKView)contextView.findViewById(R.id.capital_flow_dkview_id);
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
                DtLog.d(TAG, "tryLoadCompare() compareSecCode = " + compareSecCode + ", mStockName = " + mStockName);
                getPresenter(compareSecCode).loadQuote();
            }
        }
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

    @Override
    public void onResume() {
        DtLog.d(TAG, "onResume: " + mStockName);
        super.onResume();
        mIsResume = true;
        resumeDialog();
        checkEntrance();
        registerReceivers();
    }

    private void resumeDialog() {
        getDetailPresenter().onResume();
    }

    private void checkEntrance() {
        checkEntrance(DengtaSettingPref.getLineTypeIndex());
    }

    private void checkEntrance(int index) {
        if(needShowCallauction(index)) {
            showCallauctionEntrance();
        } else {
            hideCallauctionEntrance();
        }

        if(needShowEnlargeTimeline(index)) {
            startAnimateEnlargeTimeLineEntrance();
        } else {
            finishEnlargeTimeLineEntrance();
        }
    }

    public void doOnResume() {
        DtLog.d(TAG, "doOnResume: " + mStockName);
        final int repairType = loadKLineRepairType();
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.runPeriodicDelay(200);
        }
        if (mRepairType != repairType) { // 清空缓存
            mKLineData.clear();
            mKLineRefreshTime.clear();
            mRepairType = repairType;
        }

        int index = DengtaSettingPref.getLineTypeIndex();
        if(mStatHelper != null) {
            mStatHelper.setKey(getStatKey(index));
            mStatHelper.start();
        }
        if (mTabLayout != null) {
            mTabLayout.switchTab(index);
        }

        if (null != mTabSelector)
        {
            mTabSelector.switchTab(DataPref.getDashBoardIndex());
        }

        isShowCandidatorGuided = SettingPref.getBoolean(SettingConst.KEY_CANDIDATOR_GUIDE, false);
        DtLog.d(TAG, "doOnResume mTabLayout.switchTab(index)");
    }

    @Override
    public void onPause() {
        DtLog.d(TAG, "onPause: " + mStockName);
        super.onPause();
        mIsResume = false;
        pauseDialog();
        finishEnlargeTimeLineEntrance();
        unregisterReceivers();
    }

    private void pauseDialog() {
        getDetailPresenter().onPause();
    }

    public void doOnPause() {
        DtLog.d(TAG, "doOnPause: " + mStockName);
        if (mPeriodicHandlerManager != null) {
            mPeriodicHandlerManager.stop();
        }

        if (mTimeLineIndicator != null) {
            mTimeLineIndicator.stopAnimation();
        }
        if(getUserVisibleHint() && mStatHelper != null) {
            mStatHelper.end();
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

    private void registerReceivers() {
        // 监听网络状态变化
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        DengtaApplication.getApplication().registerReceiver(mNetStateReceiver, filter);

        filter = new IntentFilter();
        filter.addAction(LineChartTextureView.ACTION_TIME_LINE_INDICATOR_TYPE_CHANGED);
        filter.addAction(LineChartTextureView.ACTION_K_LINE_INDICATOR_TYPE_CHANGED);
        mFirstNetStateBroadcast = true;
        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, filter);
    }

    private void unregisterReceivers() {
        DengtaApplication.getApplication().unregisterReceiver(mNetStateReceiver);

        LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
    }

    private void loadTimeLineData() {
        final int start = getTimeLineDatasStart();
        if (start <= 0) {
            QuoteRequestManager.getTimeLineDataRequest(mDtSecCode, this);
        } else {
            QuoteRequestManager.getTimeLineDataRequest(mDtSecCode, start, this);
        }
    }

    /**
     * @return 如果小于等于0，全量拉取
     */
    private int getTimeLineDatasStart() {
        final ArrayList<TrendDesc> timeLineDatas = mTimeLineDatas;
        final int size = timeLineDatas == null ? 0 : timeLineDatas.size();
        return size - 2;
    }

    private void loadFiveDayTimeLineData() {
        DtLog.d(TAG, "loadFiveDayTimeLineData: " + mStockName);
        if(mSurfaceListener != null) {
            RtMinReq req = new RtMinReq(mDtSecCode, 5, "", 0);
            mSurfaceListener.setDateAndIMinute(req);
            DataEngine.getInstance().request(EntityObject.ET_GET_RTMIN_DATA_EX, req, this);
        }
    }

    private void loadFiveDayTimeLineCapitalDDZData() {
        int indicatorIndex0 = DengtaSettingPref.loadTimeLineIndicatorType0();
        int indicatorIndex1 = DengtaSettingPref.loadTimeLineIndicatorType1();
        if(indicatorIndex0 == SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ
                || indicatorIndex1 == SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ) {
            DtLog.i(TAG,"请求五日资金博弈");
            QuoteRequestManager.getFiveDayTimeLineCapitalDDZDataRequest(mDtSecCode,this);
        }
    }

    private void loadTimeLineCapitalDDZData() {
        if (mSupportCapitalDDZ) {
            int indicatorIndex0 = DengtaSettingPref.loadTimeLineIndicatorType0();
            int indicatorIndex1 = DengtaSettingPref.loadTimeLineIndicatorType1();
            if(indicatorIndex0 == SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ
                    || indicatorIndex1 == SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ) {
                QuoteRequestManager.getTimeLineCapitalDDZDataRequest(mDtSecCode, 0, getTimeLineCapitalNum(), this);
            }
        }
    }

    private void loadKLineCapitalDDZData(final int kLineType) {
        if (mSupportCapitalDDZ && supportKLineCapital(kLineType)) {
            int indicatorIndex0 = DengtaSettingPref.loadKLineIndicatorType0();
            int indicatorIndex1 = DengtaSettingPref.loadKLineIndicatorType1();
            int indicatorIndex2 = DengtaSettingPref.loadKLineIndicatorType2();
            int indicatorIndex3 = DengtaSettingPref.loadKLineIndicatorType3();
            if(indicatorIndex0 == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW
                    || indicatorIndex1 == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW
                    || indicatorIndex2 == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW
                    || indicatorIndex3 == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW) {
                QuoteRequestManager.getKLineCapitalDDZDataRequest(mDtSecCode, 0, getKLineCapitalNum(), this);
            }
        }
    }

    private static boolean supportKLineCapital(int lineType) {
        return lineType == E_K_LINE_TYPE.E_KLT_DAY;
    }

    private void loadKLineData(final int kLineType) {
        DtLog.d(TAG, "loadKLineData: " + mStockName);
        if (TextUtils.isEmpty(mDtSecCode)) {
            return;
        }

        QuoteRequestManager.getKLineDataRequest(mDtSecCode, kLineType, 0, getKLineNum(kLineType),
                mRepairType == KlineSettingConst.K_LINE_REPAIR, this);
        loadKLineCapitalDDZData(kLineType);
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

    private int getFiveDayTimeLineCapitalNum() {
        return 240 * 4 + getTimeLineCapitalNum();
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
            // 日K周K月K最多增量拉取2个K线
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

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success) {
            // 可以在这里提醒用户错误信息
            int entityType = entity.getEntityType();
            DtLog.w(TAG, "callback requestBriefInfo faild: type = " + entityType);
            ThreadUtils.runOnUiThread(() -> {
                showViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            });
            return;
        }

        if (!isAdded()) {
            return;
        }

        ThreadUtils.runOnUiThread(() -> {
            showViewByState(DengtaConst.UI_STATE_NORMAL);
        });
        Log.i(TAG, "callback:请求后返回数据Type== "+entity.getEntityType());
        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_TREND:
                getTimeLineDataCallback(success, entity);
                break;
            case EntityObject.ET_GET_RTMIN_DATA_EX:
                final RtMinRsp rtMinRsp = (RtMinRsp) entity.getEntity();
                if (rtMinRsp != null) {
                    setFiveDayTimeLineData(rtMinRsp);
                    refreshValueView(LineChartTextureView.TYPE_FIVE_DAY);
                }
                break;
            case EntityObject.ET_GET_CAPITAL_DDZ:
                Log.i(TAG, "callback: 得到资金博弈数据");
                CapitalDDZRsp csp = (CapitalDDZRsp) entity.getEntity();
                final String extra = (String) entity.getExtra();
                int type = Integer.parseInt(extra);
                if (csp != null) {
                    setCapitalDDZData(csp, type);
                }
                break;
            case EntityObject.ET_GET_KLINE_INCRCEMENT:
                break;
            case EntityObject.ET_GET_KLINE_DATA:
                KLineRsp kLineData = (KLineRsp) entity.getEntity();
                final List<KLineDesc> kLineEntities = kLineData.getVKLineDesc();
                final int kLineType = kLineData.getEKLineType();
                setKLineData(kLineEntities, (short) kLineType);
                break;
            case EntityObject.ET_GET_SEC_BS_INFO:
                getBSInfoCallback((GetSecBsInfoRsp) entity.getEntity());
                break;
            case EntityObject.ET_GET_FUND:
                setCapitalData(EntityUtil.entityToCapitalFlow(success, entity));
                break;
            case EntityObject.ET_GET_FIVEDAY_CAPITAL_DDZ://五日资金博弈,解析返回数据
                CapitalDDZMultiRsp rsp = (CapitalDDZMultiRsp) entity.getEntity();
                // TODO: 2018/5/10  数据解析还要再看,下面的不知道对不对
                Log.i(TAG, "callback: 拿到五日资金博弈数据");

                if (rsp != null) {
                    DtLog.i(TAG,"得到五日资金博弈数据==="+rsp.getVvCapitalDDZ() +"");
                    setFiveDayCapitalDDZData(rsp);//这个需要重写一个新的,不用这个
                }
                break;
            default:
                break;
        }
    }

    private void getTimeLineDataCallback(boolean success, EntityObject entity) {
        ThreadUtils.runOnUiThread(() -> {
            final TrendRsp trendRsp = (TrendRsp) entity.getEntity();
            if (TextUtils.isEmpty((String) entity.getExtra())) { // 全量分时数据
                handleUpdateTimeLineFullData(trendRsp);
            } else { // 增量分时数据
                handleUpdateTimeLineIncrementData(trendRsp);
            }
        });
    }

    private void handleUpdateTimeLineFullData(final TrendRsp trendRsp) {
        setSupportAverage(trendRsp.bSupport);
        final ArrayList<TrendDesc> timeLineDatas = trendRsp.vTrendDesc;
        if (timeLineDatas != null && timeLineDatas.size() > 0) { // 一直没数据就一直拉取全量数据
            updateTimeLineData(timeLineDatas);
            mTimeLineDatas = timeLineDatas;
        }
        refreshValueView(LineChartTextureView.TYPE_TIME);
    }

    private void handleUpdateTimeLineIncrementData(final TrendRsp trendRsp) {
        final ArrayList<TrendDesc> incrementTimeLineData = trendRsp.vTrendDesc;
        if (incrementTimeLineData == null || incrementTimeLineData.size() == 0) { // 没有增量数据就不处理
            return;
        }

        final ArrayList<TrendDesc> oldTimeLineDatas = mTimeLineDatas;
        final int size = oldTimeLineDatas == null ? 0 : oldTimeLineDatas.size();
        if (size == 0) { // 如果没有全量数据，就直接拉取全量数据
            return;
        }

        int index = -1;
        TrendDesc oldData;
        final TrendDesc firstIncrementData = incrementTimeLineData.get(0);
        // 查询最后一个数据在新数据里的位置，以便连接上
        for (int i = size - 1; i >= 0; i--) {
            oldData = oldTimeLineDatas.get(i);
            if (oldData.iMinute == firstIncrementData.iMinute) {
                index = i;
                break;
            }
        }

        ArrayList<TrendDesc> newTimeLineDatas;
        if (index < 0) {// 未找到重复的数据，就直接连接
            oldTimeLineDatas.addAll(incrementTimeLineData);
            newTimeLineDatas = oldTimeLineDatas;
        } else { // 有部分重复的数据
            newTimeLineDatas = new ArrayList<>(oldTimeLineDatas.subList(0, index));
            newTimeLineDatas.addAll(incrementTimeLineData);
        }
        updateTimeLineData(newTimeLineDatas);
        mTimeLineDatas = newTimeLineDatas;
        refreshValueView(LineChartTextureView.TYPE_TIME);
    }

    private void getBSInfoCallback(final GetSecBsInfoRsp rsp) {
        final ArrayList<SecBsInfo> bsPointList = rsp.vSecBsInfo;
        ThreadUtils.runOnUiThread(() -> mSurfaceListener.setBSInfos(bsPointList));
    }

    /**
     * 设置五日资金博弈
     * @param rsp
     */
    private void setFiveDayCapitalDDZData(final CapitalDDZMultiRsp rsp) {
        DtLog.i(TAG, "setFiveDayCapitalDDZData: name = " + mStockName  );
        ArrayList<ArrayList<CapitalDDZDesc>> vvCapitalDDZ = rsp.getVvCapitalDDZ();

        final int newDataSize = vvCapitalDDZ == null ? 0 : vvCapitalDDZ.size();
        DtLog.i(TAG, "setCapitalDDZData: size = " + newDataSize);
        if (newDataSize == 0) {
            return;
        }
        ThreadUtils.runOnUiThread(() -> {

                    mTimeLineCapitalRefreshTime = DengtaApplication.getApplication().getTradingStateManager().getServerTime();
                    final int oldSize = mFiveDayLineCapitals == null ? 0 : mFiveDayLineCapitals.size();
                    if (oldSize == 0) {
                        mFiveDayLineCapitals = vvCapitalDDZ.get(0);
                    } else {
                        CapitalDDZDesc newData;
                        CapitalDDZDesc oldData;

                        long newTime;
                        long oldTime;
                        for (int i = 0; i < newDataSize; i++) {
                            newData = vvCapitalDDZ.get(0).get(i);
                            newTime = newData.lTime;
                            final int size = mFiveDayLineCapitals.size();
                            in:
                            for (int j = 0; j < size; j++) {
                                oldData = mFiveDayLineCapitals.get(j);
                                oldTime = oldData.lTime;

                                if (newTime > oldTime) {
                                    mFiveDayLineCapitals.add(j, newData);
                                    break in;
                                } else if (newTime == oldTime) {
                                    mFiveDayLineCapitals.remove(j);
                                    mFiveDayLineCapitals.add(j, newData);
                                    break in;
                                }
                            }
                        }
                    }
                    DtLog.i(TAG, "setFiveDayCapitalDDZData: mTimeLineCapitals.size() = " + mFiveDayLineCapitals.size());
                    mSurfaceListener.setFiveDayCapitalDDZEntities(new ArrayList<CapitalDDZDesc>(mFiveDayLineCapitals));

        });

    }
    private void setCapitalData(final CapitalFlow capitalFlow) {
        if (capitalFlow != null) {
            ThreadUtils.runOnUiThread(()-> {
                if (capitalFlowView != null) {
                    capitalFlowView.setData(capitalFlow);
                }
                if (capitalFlowDetailView != null) {
                    capitalFlowDetailView.setData(capitalFlow);
                }
            });
        }
    }

    private void setCapitalDDZData(final CapitalDDZRsp rsp, final int type) {
        DtLog.d(TAG, "setCapitalDDZData: name = " + mStockName + ", type = " + type);
        final ArrayList<CapitalDDZDesc> ddzDescs = rsp.getVCapitalDDZDesc();
        final int newDataSize = ddzDescs == null ? 0 : ddzDescs.size();
        DtLog.d(TAG, "setCapitalDDZData: size = " + newDataSize);
        if (newDataSize == 0) {
            return;
        }
        ThreadUtils.runOnUiThread(() -> {
            switch (type) {
                case E_CAPITAL_DDZ_TYPE.E_CDT_MIN://分时资金
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

    private void setKLineData(final List<KLineDesc> kLineEntities, final short kLineType) {
        ThreadUtils.runOnUiThread(() -> {
            final int newDataSize = kLineEntities == null ? 0 : kLineEntities.size();
            DtLog.d(TAG, "setKLineData newDataSize : " + newDataSize);
            if (newDataSize == 0) {
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

    public void updateTimeLineData(final ArrayList<TrendDesc> entities) {
        mSurfaceListener.setTimeEntities(entities);
    }

    @Override
    public void updateCompareTimeEntities(final ArrayList<TrendDesc> entities) {
        DtLog.d("yorkeehuang", "updateCompareTimeEntities() : " + mStockName + ", entities.size() = " + entities.size());
        mSurfaceListener.setCompareTimeEntities(entities);
    }

    @Override
    public void updateCompareQuote(SecQuote quote) {
        if(quote != null) {
            updateCompareButton(quote.getSSecName());
            mSurfaceListener.setCompareQuote(quote);
        }
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

    public void setSupportAverage(boolean support) {
        mSurfaceListener.setSupportAverage(support);
    }

    private void setFiveDayTimeLineData(final RtMinRsp rtMinRsp) {
        ThreadUtils.runOnUiThread(() -> mSurfaceListener.setRtMinDescs(rtMinRsp.getVRtMinDesc()) );
    }

    public void setTpFlag(int tpFlag) {
        if (mTpFlag != tpFlag && isAdded()) {
            mTpFlag = tpFlag;
            if(mValueView != null) {
                mValueView.setTpFlag(mTpFlag);
            }

            if(mTicksView != null) {
                mTicksView.setTpFlag(mTpFlag);
            }
        }
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

    public void setQuote(final SecQuote quote) {
        setTpFlag(quote.iTpFlag);
        ThreadUtils.runOnUiThread(() -> {
            if (!isAdded()) {
                return;
            }

            final int currentIndex = DengtaSettingPref.getLineTypeIndex();
            if (LineChartSurfaceTextureListener.isTimeLine(currentIndex)
                    && isSuspended() && quote.eSecStatus != E_SEC_STATUS.E_SS_SUSPENDED
                    && mIsTrading) { //交易时段从停牌状态复牌，则小球开始闪烁
                mTimeLineIndicator.startAnimation();
            }
            boolean isInitialQuote = mQuote == null;
            mQuote = quote;

            if (mSurfaceListener != null) {
                mSurfaceListener.setQuote(quote);
                if(isInitialQuote) {
                    refreshValueView(currentIndex);
                }
            }

            if (quote != null) {
                if (mTicksView != null) {
                    mTicksView.setYesterdayClose(quote.fClose);
                }
                setBuySellData(quote);
                getDetailPresenter().setSecQuote(quote);
                QuoteRequestManager.getCapitalDataRequest(mDtSecCode, LineChartFragment.this);
            }
        });
    }

    public void setIsIndex(final boolean isIndex) {
        mIsIndex = isIndex;
    }

    public void setIsTrading(final boolean isTrading) {
        boolean needRefresh = false;
        if (mIsTrading != isTrading && isTrading) {
            needRefresh = true;
        }

        mIsTrading = isTrading;

        if(mIsTrading) {
            setIsCallauction(false);
        }

        if (needRefresh) {
            if (mPeriodicHandlerManager != null) {
                mPeriodicHandlerManager.runPeriodicDelay(200);
            }
        }
        if(needShowEnlargeTimeline()) {
            startAnimateEnlargeTimeLineEntrance();
        } else {
            finishEnlargeTimeLineEntrance();
        }
    }

    public void setIsCallauction(final boolean isCallauction) {
        mIsCallauction = isCallauction;
        if(needShowCallauction()) {
            showCallauctionEntrance();
        } else {
            hideCallauctionEntrance();
        }
    }

    private boolean needShowEnlargeTimeline() {
        return needShowEnlargeTimeline(DengtaSettingPref.getLineTypeIndex());
    }

    private boolean needShowEnlargeTimeline(int index) {
        return mIsResume && StockUtil.supportEnlargeTimeLine(mDtSecCode) && mIsTrading && isEarlyTradingTime()
                && index == LineChartTextureView.TYPE_TIME;
    }

    private boolean isEarlyTradingTime() {
        TradingStateManager manager = DengtaApplication.getApplication().getTradingStateManager();
        int now = manager.getServerTime();
        int openTime = manager.getOpenTime(StockUtil.getMarketType(mDtSecCode));
        return now - openTime < 15 * 60;
    }

    private boolean needShowCallauction() {
        return needShowCallauction(DengtaSettingPref.getLineTypeIndex());
    }

    private boolean needShowCallauction(int index) {
        return mIsResume && StockUtil.supportCallauction(mDtSecCode) && mIsCallauction
                && index == LineChartTextureView.TYPE_TIME;
    }

    private void showCallauctionEntrance() {
        if(mCallauctionEntrance != null) {
            if(mCallauctionEntrance.getVisibility() == View.GONE) {
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_CALLAUCTION_ENTRANCE);
            }
            mCallauctionEntrance.setVisibility(View.VISIBLE);
        }
    }

    private void hideCallauctionEntrance() {
        if(mCallauctionEntrance != null) {
            mCallauctionEntrance.setVisibility(View.GONE);
        }
    }

    private void startAnimateEnlargeTimeLineEntrance() {
        DtLog.d(TAG, "startAnimateEnlargeTimeLineEntrance()");
        if(mIsTrading && mIsResume) {
            mUiHandler.sendEmptyMessage(ANIMATE_ENLARGE_TIMELINE_ENTRANCE);
        }
    }

    private void animateEnlargeTimeLineEntrance() {
        DtLog.d(TAG, "animateEnlargeTimeLineEntrance()");
        if (mIsTrading && mEnlargeTimeLineEntrance != null) {
            if(mEnlargeTimeLineEntrance.getVisibility() == View.GONE) {
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SHOW_ENLARGE_TIMELINE_GUIDE_ENTRANCE);
            }
            mEnlargeTimeLineEntrance.setVisibility(View.VISIBLE);
            mEnlargeTimeLineEntrance.setImageResource(R.drawable.callauction_entrance_animation);
            AnimationDrawable animationDrawable = (AnimationDrawable) mEnlargeTimeLineEntrance.getDrawable();
            animationDrawable.start();
        }
    }

    private void finishEnlargeTimeLineEntrance() {
        DtLog.d(TAG, "finishCallauction()");
        mUiHandler.removeMessages(ANIMATE_ENLARGE_TIMELINE_ENTRANCE);
        if (mEnlargeTimeLineEntrance != null) {
            Drawable drawable = mEnlargeTimeLineEntrance.getDrawable();
            if(drawable instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                if(animationDrawable != null) {
                    animationDrawable.stop();
                }
            }

            mEnlargeTimeLineEntrance.setVisibility(View.GONE);
        }
    }

    private boolean isSuspended() {
        return mQuote != null && mQuote.getESecStatus() == E_SEC_STATUS.E_SS_SUSPENDED;
    }

    public void setTicksData(TickRsp tickRsp) {
        if (mTicksView != null) {
            mTicksView.setTicksData(tickRsp.getVTickDesc());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBuySellView) {
            mTabSelector.switchTab(1);
        } else if (v == mTicksView) {
            mTabSelector.switchTab(2);
        } else if (v == capitalFlowLayout) {
            mTabSelector.switchTab(0);
        }

        if (v == tradeCloseBtn) {
            tradeDetailIsShow = !tradeDetailIsShow;
            tradeCloseBtn.setImageResource(tradeDetailIsShow ? R.drawable.open_triangle : R.drawable.close_triangle);
            if (tradeDetailIsShow) {
                // 显示
                mTransactionLayout.setVisibility(View.VISIBLE);
                SettingPref.putBoolean(SettingConst.KEY_SETTING_TRANSATICON_OPEN, true);
            } else {
                // 隐藏
                mTransactionLayout.setVisibility(View.GONE);
                SettingPref.putBoolean(SettingConst.KEY_SETTING_TRANSATICON_OPEN, false);
            }
        }
    }

    // TODO 需要优化
    private int mTradingMinutes;
    private String mOpenTimeStr = "";
    private String mMiddleTimeStr = "";
    private String mCloseTimeStr = "";

    public void setTradingMinutes(int tradingMinutes) {
        mTradingMinutes = tradingMinutes;
        if (mSurfaceListener != null) {
            mSurfaceListener.setTradingMinutes(tradingMinutes);
        }
    }

    public void setTradingShowTime(String openTimeStr, String middleTimeStr, String closeTimeStr) {
        mOpenTimeStr = openTimeStr;
        mMiddleTimeStr = middleTimeStr;
        mCloseTimeStr = closeTimeStr;
        if (mSurfaceListener != null) {
            mSurfaceListener.setTimeStr(openTimeStr, middleTimeStr, closeTimeStr);
        }
        getDetailPresenter().updateTradingTimeStr(openTimeStr, middleTimeStr, closeTimeStr);
    }

    private void setBuySellData(SecQuote quote) {
        final BuySellEntity buySellEntity = new BuySellEntity(quote);

        if (mBuySellView != null) {
            mBuySellView.setData(buySellEntity);
        }

        if (dkView != null) {
            dkView.setBuySellData(buySellEntity);
        }
    }

    private void showViewByState(int state) {
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
                mTimeLineIndicator.stopAnimation();
            }
        }
    };

    private void showLoadingView() {
        cancelShowLoadingView();
        mUiHandler.postDelayed(mShowLoadingViewRunnable, 500);
    }

    private void cancelShowLoadingView() {
        mLoadingLayout.setVisibility(View.GONE);
        mUiHandler.removeCallbacks(mShowLoadingViewRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyDialog();
    }

    private void destroyDialog() {
        getDetailPresenter().onDestroy();
    }

    public void onReloadData() {
        mPeriodicHandlerManager.runPeriodic();
    }

    @Override
    public void run() {
        DtLog.d(TAG, "mPeriodicHandlerManager run mIsTrading : " + mIsTrading);
        reloadDataByIndex(DengtaSettingPref.getLineTypeIndex());
        if (mTicksScrollView != null && mTicksScrollView.getVisibility() == View.VISIBLE) {
            // 只有成交明细显示时，拉取成交数据
            getDetailPresenter().requestTick(mDtSecCode);
        }
        if (getUserVisibleHint() && mIsTrading) {
            mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
        } else {
            mPeriodicHandlerManager.stop();
        }
    }

    @Override
    public void onDialogButtonClick(BottomDoubleButtonDialog dialog, View view, int position) {
        switch (position) {
            case BottomDoubleButtonDialog.PRE_BUTTON:
                mSurface.moveLeft();
                break;
            case BottomDoubleButtonDialog.NEXT_BUTTON:
                mSurface.moveRight();
                break;
            default:
        }
    }

    public void selectCustomStock(String dtSecCode) {
        DtLog.d(TAG, "selectCustomStock() dtSecCode = " + dtSecCode);
        if(!TextUtils.isEmpty(dtSecCode)) {
            tryLoadCompare(dtSecCode);
        }
    }
}
