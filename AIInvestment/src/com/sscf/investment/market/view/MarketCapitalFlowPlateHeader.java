package com.sscf.investment.market.view;

import BEC.CapitalDetailDesc;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.market.CapitalFlowStockListInPlateActivity;
import com.dengtacj.component.router.WebBeaconJump;
import java.util.ArrayList;

/**
 * Created by davidwei on 2016/06/15.
 * 市场行情沪深界面的板块的HeaderView
 */
public final class MarketCapitalFlowPlateHeader extends LinearLayout implements OnGetDataCallback<ArrayList<CapitalDetailDesc>>,
        View.OnClickListener, Handler.Callback {

    private static final int PLATE_COUNT = 6;

    public static final int PLATE_TYPE_INDUSTRY_INCREASE = 1;
    public static final int PLATE_TYPE_INDUSTRY_DECREASE = 2;
    public static final int PLATE_TYPE_CONCEPT_INCREASE = 3;
    public static final int PLATE_TYPE_CONCEPT_DECREASE = 4;
    private int mPlateType;

    private ArrayList<MarketCapitalFlowPlateInfoView> mPlateInfoViews;

    private Handler mHandler;

    private View mPlateTitleLayout;
    private View mPlateListLayout;
    private ImageView mExpandImageView;

    public MarketCapitalFlowPlateHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHandler = new Handler(this);
        initTitleLayout();
        initPlateInfos();
    }

    public void setPlateType(final int type) {
        mPlateType = type;
        int textId = 0;
        switch (type) {
            case PLATE_TYPE_INDUSTRY_INCREASE:
                textId = R.string.market_capital_flow_industry_increase;
                break;
            case PLATE_TYPE_INDUSTRY_DECREASE:
                textId = R.string.market_capital_flow_industry_decrease;
                break;
            case PLATE_TYPE_CONCEPT_INCREASE:
                textId = R.string.market_capital_flow_concept_increase;
                break;
            case PLATE_TYPE_CONCEPT_DECREASE:
                textId = R.string.market_capital_flow_concept_decrease;
                break;
            default:
                return;
        }
        ((TextView) mPlateTitleLayout.findViewById(R.id.group_title)).setText(textId);
    }

    private void initTitleLayout() {
        final View plateTitleLayout = findViewById(R.id.plateTitleLayout);
        plateTitleLayout.setOnClickListener(this);
        plateTitleLayout.findViewById(R.id.group_more).setOnClickListener(this);
        mExpandImageView = (ImageView) plateTitleLayout.findViewById(R.id.group_expand_image);
        mPlateTitleLayout = plateTitleLayout;
    }

    private void initPlateInfos() {
        final Resources resources = getResources();
        final View plateListLayout = findViewById(R.id.plateListLayout);
        final ArrayList<MarketCapitalFlowPlateInfoView> plateInfoViews = new ArrayList<MarketCapitalFlowPlateInfoView>(PLATE_COUNT);
        MarketCapitalFlowPlateInfoView infoView = null;
        int identifier = 0;
        for (int i = 0; i < PLATE_COUNT; i++) {
            identifier = resources.getIdentifier("plateLayout" + i, "id", DengtaApplication.getApplication().getPackageName());
            if (identifier != 0) {
                infoView = (MarketCapitalFlowPlateInfoView) plateListLayout.findViewById(identifier);
                infoView.setOnClickListener(this);
                plateInfoViews.add(infoView);
            }
        }
        mPlateInfoViews = plateInfoViews;
        mPlateListLayout = plateListLayout;
    }

    public void requestData(final IMarketManager marketManager) {
        switch (mPlateType) {
            case PLATE_TYPE_INDUSTRY_INCREASE:
                marketManager.requestIndustryCapitalFlowIncreaseList(PLATE_COUNT, this);
                break;
            case PLATE_TYPE_INDUSTRY_DECREASE:
                marketManager.requestIndustryCapitalFlowDecreaseList(PLATE_COUNT, this);
                break;
            case PLATE_TYPE_CONCEPT_INCREASE:
                marketManager.requestConceptCapitalFlowIncreaseList(PLATE_COUNT, this);
                break;
            case PLATE_TYPE_CONCEPT_DECREASE:
                marketManager.requestConceptCapitalFlowDecreaseList(PLATE_COUNT, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetData(ArrayList<CapitalDetailDesc> data) {
        if (data != null) {
            mHandler.obtainMessage(0, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        updatePlateInfos((ArrayList<CapitalDetailDesc>) msg.obj);
        return true;
    }

    private void updatePlateInfos(ArrayList<CapitalDetailDesc> plates) {
        final int size = Math.min(plates.size(), PLATE_COUNT);

        MarketCapitalFlowPlateInfoView infoView = null;
        CapitalDetailDesc plateQuote = null;
        for (int i = 0; i < size; i++) {
            infoView = mPlateInfoViews.get(i);
            plateQuote = plates.get(i);
            infoView.updateInfos(plateQuote);
            infoView.setTag(plateQuote);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_more:
                clickMore();
                break;
            case R.id.plateTitleLayout:
                // 显示与隐藏list
                final MarginLayoutParams params = (MarginLayoutParams) mPlateTitleLayout.getLayoutParams();
                if (mPlateListLayout.getVisibility() == View.VISIBLE) {
                    mPlateListLayout.setVisibility(View.GONE);
                    mExpandImageView.setImageResource(R.drawable.list_group_collapsed);
                    params.topMargin = 0;
                } else {
                    mPlateListLayout.setVisibility(View.VISIBLE);
                    mExpandImageView.setImageResource(R.drawable.list_group_expanded);
                    params.topMargin = getResources().getDimensionPixelSize(R.dimen.market_block_margin);
                }
                break;
            default:
                final CapitalDetailDesc capitalDesc = (CapitalDetailDesc) v.getTag();
                if (capitalDesc == null) {
                    return;
                }

                final Context context = getContext();
                int sortType = StockSortTitleView.STATE_SORT_DESCEND;
                switch (mPlateType) {
                    case PLATE_TYPE_INDUSTRY_DECREASE:
                    case PLATE_TYPE_CONCEPT_DECREASE:
                        sortType = StockSortTitleView.STATE_SORT_ASCEND;
                        break;
                    default:
                        break;
                }
                CapitalFlowStockListInPlateActivity.show(context, capitalDesc.sDtSecCode, capitalDesc.sSecName, sortType);
                break;
        }
    }

    private void clickMore() {
        final Context context = getContext();
        final WebUrlManager urlManager = DengtaApplication.getApplication().getUrlManager();
        switch (mPlateType) {
            case PLATE_TYPE_INDUSTRY_INCREASE:
                WebBeaconJump.showCommonWebActivity(context, urlManager.getCapitalFlowIndustryIncreaseListUrl());
                break;
            case PLATE_TYPE_INDUSTRY_DECREASE:
                WebBeaconJump.showCommonWebActivity(context, urlManager.getCapitalFlowIndustryDecreaseListUrl());
                break;
            case PLATE_TYPE_CONCEPT_INCREASE:
                WebBeaconJump.showCommonWebActivity(context, urlManager.getCapitalFlowConceptIncreaseListUrl());
                break;
            case PLATE_TYPE_CONCEPT_DECREASE:
                WebBeaconJump.showCommonWebActivity(context, urlManager.getCapitalFlowConceptDecreaseListUrl());
                break;
            default:
                break;
        }
    }
}
