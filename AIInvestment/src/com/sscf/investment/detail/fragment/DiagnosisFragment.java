package com.sscf.investment.detail.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dengtacj.component.router.WebBeaconJump;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.widget.BaseFragment;
import com.sscf.investment.widget.RadarView;

import java.util.ArrayList;
import java.util.List;

import BEC.BEACON_STAT_TYPE;
import BEC.ConsultScore;
import BEC.ConsultScoreRsp;
import BEC.ScoreDesc;


/**
 * Created by yorkeehuang on 2017/3/1. 诊股
 */

public class DiagnosisFragment extends BaseFragment implements DataSourceProxy.IRequestCallback, View.OnClickListener {
    private static final String TAG = DiagnosisFragment.class.getSimpleName();

    private String mDtSecCode;
    private String mSecName;

    private RadarView mRadarView;
    private TextView mScoreTitleView;
    private TextView mScoreView;
    private TextView mScoreDescriptionView;
    private ConsultScore mScoreData;
    private ViewGroup mScoreDetailPanel;

    private ScrollView mScorePanel;
    private View mLoadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dianosis, container, false);
        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mSecName = args.getString(DengtaConst.KEY_SEC_NAME);
        }
        initView(inflater, container, view);
        return view;
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        TimeStatHelper helper = new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEC_NEWS);
        helper.setKey(StatConsts.STOCK_INFO_DIAGNOSIS);
        return helper;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DtLog.d(TAG, "onActivityCreated()");
        mScoreData = null;
        requestScoreDetail();
    }

    private void initView(LayoutInflater inflater, ViewGroup container, View rootView) {
        mRadarView = (RadarView) rootView.findViewById(R.id.radar_view);
        mScoreTitleView = (TextView) rootView.findViewById(R.id.score_title);
        mScoreView = (TextView) rootView.findViewById(R.id.score);
        mScoreDescriptionView = (TextView) rootView.findViewById(R.id.score_description);
        mScorePanel = (ScrollView) rootView.findViewById(R.id.score_panel);
        rootView.findViewById(R.id.score_info_panel).setOnClickListener(this);
        mLoadingView = rootView.findViewById(R.id.loading_layout);
        mScoreDetailPanel = (ViewGroup) rootView.findViewById(R.id.score_detail_panel);
        List<ScoreDetailItem> detailItemList = initScoreDetailItemList();
        for(ScoreDetailItem detailItem : detailItemList) {
            View itemView = inflater.inflate(R.layout.layout_score_item, container, false);
            ((ImageView) itemView.findViewById(R.id.icon)).setImageResource(detailItem.icon);
            ((TextView) itemView.findViewById(R.id.title)).setText(detailItem.titleRes);
            itemView.setTag(detailItem);
            mScoreDetailPanel.addView(itemView);
            itemView.setOnClickListener(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
        if(!hidden) {
            DtLog.d(TAG, "onHiddenChanged() mDtSecCode = " + mDtSecCode + ", mScoreData == " + mScoreData);
            requestScoreDetail();
        }
    }

    private List<ScoreDetailItem> initScoreDetailItemList() {
        List<ScoreDetailItem> items = new ArrayList<>(6);
        items.add(new ScoreDetailItem(ScoreDetailItem.TYPE_RISE, R.string.score_title_rise_text, R.drawable.rise_icon));
        items.add(new ScoreDetailItem(ScoreDetailItem.TYPE_MARKET_HOT, R.string.score_title_market_hot_text, R.drawable.mkhot_icon));
        items.add(new ScoreDetailItem(ScoreDetailItem.TYPE_MAIN, R.string.score_title_main_text, R.drawable.main_icon));
        items.add(new ScoreDetailItem(ScoreDetailItem.TYPE_TREND, R.string.score_title_trend_text, R.drawable.trend_icon));
        items.add(new ScoreDetailItem(ScoreDetailItem.TYPE_VALUE, R.string.score_title_value_text, R.drawable.value_icon));
        items.add(new ScoreDetailItem(ScoreDetailItem.TYPE_CONSULT, R.string.score_title_consult_text, R.drawable.consult_icon));
        return items;
    }

    private static class DiagnosisConsts {
        public static final int TAB_RISE = 0;
        public static final int TAB_MARKET_HOT = 1;
        public static final int TAB_MAIN = 2;
        public static final int TAB_TREND = 3;
        public static final int TAB_VALUE = 4;
        public static final int TAB_CONSULT = 5;
    }

    @Override
    public void onClick(View v) {
        if(TimeUtils.isFrequentOperation()) {
            return;
        }
        StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_ZHENGU_PLATE_CLICK);
        switch (v.getId()) {
            case R.id.score_info_panel:
                startDetail(DiagnosisConsts.TAB_RISE);
                break;
            default:
                if(v.getTag() != null && v.getTag() instanceof ScoreDetailItem) {
                    if(TextUtils.isEmpty(mDtSecCode) || TextUtils.isEmpty(mSecName)) {
                        return;
                    }

                    ScoreDetailItem item = (ScoreDetailItem) v.getTag();
                    int tabIndex = -1;
                    switch(item.type) {
                        case ScoreDetailItem.TYPE_RISE://上涨潜力
                            tabIndex = DiagnosisConsts.TAB_RISE;
                            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_ZHENGU_PLATE_SHANGZHANG_CLICK);
                            break;
                        case ScoreDetailItem.TYPE_MARKET_HOT://市场热度
                            tabIndex = DiagnosisConsts.TAB_MARKET_HOT;
                            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_ZHENGU_PLATE_MARKET_CLICK);
                            break;
                        case ScoreDetailItem.TYPE_MAIN://主力强度
                            tabIndex = DiagnosisConsts.TAB_MAIN;
                            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_ZHENGU_PLATE_BROUT_CLICK);

                            break;
                        case ScoreDetailItem.TYPE_TREND://走势分析
                            tabIndex = DiagnosisConsts.TAB_TREND;
                            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_ZHENGU_PLATE_TRENT_CLICK);

                            break;
                        case ScoreDetailItem.TYPE_VALUE://价值评估
                            tabIndex = DiagnosisConsts.TAB_VALUE;
                            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_ZHENGU_PLATE_WORTH_CLICK);

                            break;
                        case ScoreDetailItem.TYPE_CONSULT://牛人解读
                            tabIndex = DiagnosisConsts.TAB_CONSULT;
                            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHOW_ZHENGU_PLATE_NIUREN_CLICK);

                            break;
                        default:
                    }

                    startDetail(tabIndex);
                }
        }
    }

    private void startDetail(int tabIndex) {
        final Activity activity = getActivity();
        if(activity == null) {
            return;
        }
        if(tabIndex >= 0) {
            WebBeaconJump.showIntelligentDiagnosisDetail(activity, mDtSecCode, mSecName, tabIndex);
        }
    }

    private class ScoreDetailItem {
        public int type;
        public int titleRes;
        public int icon;

        public static final int TYPE_RISE = 1;
        public static final int TYPE_MAIN = 2;
        public static final int TYPE_TREND = 3;
        public static final int TYPE_MARKET_HOT = 4;
        public static final int TYPE_VALUE = 5;
        public static final int TYPE_CONSULT = 6;

        public ScoreDetailItem(int type, int titleRes, int icon) {
            this.type = type;
            this.titleRes = titleRes;
            this.icon = icon;
        }
    }

    private void switchLoadingState(boolean isLoading) {
        DtLog.d(TAG, "switchLoadingState() isLoading = " + isLoading);
        Activity activity = getActivity();
        if(activity != null) {
            activity.runOnUiThread(() -> {
                if(isLoading) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    mScorePanel.setVisibility(View.GONE);
                } else {
                    mLoadingView.setVisibility(View.GONE);
                    mScorePanel.setVisibility(View.VISIBLE);
                }
            });
        };
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_SCORE_DETAIL:
                handleScoreDetail(success, data);
                break;
            default:
        }
    }

    private void handleScoreDetail(boolean success, EntityObject data) {
        Activity activity = getActivity();
        if(success && data.getEntity() != null) {
            ConsultScoreRsp rsp = (ConsultScoreRsp) data.getEntity();
            switchLoadingState(false);
            if(activity != null) {
                activity.runOnUiThread(() -> {
                    if(rsp.getVtConsultScore() != null && !rsp.getVtConsultScore().isEmpty()) {
                        ConsultScore score = rsp.getVtConsultScore().get(0);
                        mRadarView.setData(score);
                        refreshItemList(score);
                        int scoreVal = (int)score.getFVal();
                        if(scoreVal == 0) {
                            hideScoreView();
                        } else {
                            showScoreView(scoreVal, score.getSScoreDesc());
                        }
                        mScoreData = score;
                    }
                });
            }
        } else {
            switchLoadingState(false);
            hideScoreView();
        }
    }

    private void showScoreView(int scoreVal, String desc) {
        Activity activity = getActivity();
        if(activity != null) {
            activity.runOnUiThread(() -> {
                mScoreTitleView.setTextSize(12);
                mScoreTitleView.setText(R.string.score_title_text);
                mScoreView.setVisibility(View.VISIBLE);
                mScoreView.setText(String.valueOf(scoreVal));
                mScoreDescriptionView.setVisibility(View.VISIBLE);
                mScoreDescriptionView.setText(desc);
            });
        }
    }

    private void hideScoreView() {
        Activity activity = getActivity();
        if(activity != null) {
            activity.runOnUiThread(() -> {
                mScoreTitleView.setTextSize(18);
                mScoreTitleView.setText(R.string.no_score);
                mScoreView.setVisibility(View.GONE);
                mScoreDescriptionView.setVisibility(View.GONE);
            });
        }
    }

    private void refreshItemList(ConsultScore score) {
        for(int i=0, count = mScoreDetailPanel.getChildCount(); i<count; i++) {
            View child = mScoreDetailPanel.getChildAt(i);
            Object tag = child.getTag();
            if(tag instanceof ScoreDetailItem) {
                ScoreDetailItem item = (ScoreDetailItem) tag;
                ScoreDesc scoreDesc = null;
                switch(item.type) {
                    case ScoreDetailItem.TYPE_RISE:
                        scoreDesc = score.getStRiseNewScoreDesc();
                        break;
                    case ScoreDetailItem.TYPE_MARKET_HOT:
                        scoreDesc = score.getStMktHotScoreDesc();
                        break;
                    case ScoreDetailItem.TYPE_MAIN:
                        scoreDesc = score.getStMainScoreDesc();
                        break;
                    case ScoreDetailItem.TYPE_TREND:
                        scoreDesc = score.getStTrendScoreDesc();
                        break;
                    case ScoreDetailItem.TYPE_VALUE:
                        scoreDesc = score.getStValueScoreDesc();
                        break;
                    case ScoreDetailItem.TYPE_CONSULT:
                        scoreDesc = score.getStConsultScoreDesc();
                        break;
                    default:
                }
                if(scoreDesc != null) {
                    TextView descTextView = (TextView)child.findViewById(R.id.description);
                    descTextView.setVisibility(View.VISIBLE);
                    descTextView.setText(scoreDesc.getSScoreDesc());
                }
            }
        }
    }

    private void requestScoreDetail() {
        if(mScoreData == null) {
            DtLog.d(TAG, "requestScoreDetail() mDtSecCode = " + mDtSecCode);
            switchLoadingState(true);
            QuoteRequestManager.getStockScoreDetailRequest(mDtSecCode, this);
        }
    }
}
