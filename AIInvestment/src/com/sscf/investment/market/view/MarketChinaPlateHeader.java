package com.sscf.investment.market.view;

import BEC.PlateQuoteDesc;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IMarketManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import java.util.ArrayList;

/**
 * Created by davidwei on 2016/06/15.
 * 市场行情沪深界面的板块的HeaderView
 */
public final class MarketChinaPlateHeader extends LinearLayout implements View.OnClickListener, Handler.Callback,
        OnGetDataCallback<ArrayList<PlateQuoteDesc>> {

    private static final int PLATE_COUNT = 6;

    public static final int PLATE_TYPE_INDUSTRY = 1;
    public static final int PLATE_TYPE_CONCEPT = 2;

    private int mPlateType;

    private ArrayList<MarketPlateInfoView> mPlateInfoViews;

    private Handler mHandler;

    private ViewGroup mPlateTitleLayout;
    private View mPlateListLayout;
    private ImageView mExpandImageView;

    public MarketChinaPlateHeader(Context context, AttributeSet attrs) {
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
            case PLATE_TYPE_INDUSTRY:
                textId = R.string.hot_industry;
                break;
            case PLATE_TYPE_CONCEPT:
                textId = R.string.hot_concept;
                break;
            default:
                return;
        }
        ((TextView) mPlateTitleLayout.findViewById(R.id.group_title)).setText(textId);
    }

    private void initTitleLayout() {
        final ViewGroup plateTitleLayout = (ViewGroup) findViewById(R.id.plateTitleLayout);
        plateTitleLayout.setOnClickListener(this);
        plateTitleLayout.findViewById(R.id.group_more).setOnClickListener(this);
        mExpandImageView = (ImageView) plateTitleLayout.findViewById(R.id.group_expand_image);
        mPlateTitleLayout = plateTitleLayout;
    }

    private void initPlateInfos() {
        final Resources resources = getResources();
        final View plateListLayout = findViewById(R.id.plateListLayout);
        final ArrayList<MarketPlateInfoView> plateInfoViews = new ArrayList<MarketPlateInfoView>(PLATE_COUNT);
        MarketPlateInfoView infoView = null;
        int identifier = 0;
        for (int i = 0; i < PLATE_COUNT; i++) {
            identifier = resources.getIdentifier("plateLayout" + i, "id", DengtaApplication.getApplication().getPackageName());
            if (identifier != 0) {
                infoView = (MarketPlateInfoView) plateListLayout.findViewById(identifier);
                infoView.setOnClickListener(this);
                plateInfoViews.add(infoView);
            }
        }
        mPlateInfoViews = plateInfoViews;
        mPlateListLayout = plateListLayout;
    }

    public void requestData(final IMarketManager marketManager) {
        switch (mPlateType) {
            case PLATE_TYPE_INDUSTRY:
                marketManager.requestIndustryPlateList(PLATE_COUNT, this);
                break;
            case PLATE_TYPE_CONCEPT:
                marketManager.requestConceptPlateList(PLATE_COUNT, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetData(ArrayList<PlateQuoteDesc> data) {
        if (data != null) {
            mHandler.obtainMessage(0, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        updatePlateInfos((ArrayList<PlateQuoteDesc>) msg.obj);
        return true;
    }

    private void updatePlateInfos(ArrayList<PlateQuoteDesc> plates) {
        final int size = Math.min(plates.size(), PLATE_COUNT);

        MarketPlateInfoView infoView = null;
        PlateQuoteDesc plateQuote = null;
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
                final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mPlateTitleLayout.getLayoutParams();
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
                final PlateQuoteDesc plateQuote = (PlateQuoteDesc) v.getTag();
                if (plateQuote == null) {
                    return;
                }

                final Context context = getContext();
                CommonBeaconJump.showStockListInPlate(context, plateQuote.sDtSecCode, plateQuote.sSecName);
                switch (mPlateType) {
                    case PLATE_TYPE_INDUSTRY:
                        StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_INDUSTRY_PLATE);
                        break;
                    case PLATE_TYPE_CONCEPT:
                        StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_CONCEPT_PLATE);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void clickMore() {
        switch (mPlateType) {
            case PLATE_TYPE_INDUSTRY:
                WebBeaconJump.showIndustryPlateList(getContext());
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MORE_INDUSTRY_PLATE_LIST);
                break;
            case PLATE_TYPE_CONCEPT:
                WebBeaconJump.showConceptPlateList(getContext());
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MORE_CONCEPT_PLATE_LIST);
                break;
            default:
                break;
        }
    }
}
