package com.sscf.investment.discover.model;

import com.sscf.investment.discover.manager.DiscoverRequestManager;
import com.sscf.investment.discover.presenter.StockPickPresenter;
import com.sscf.investment.main.manager.AdRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import java.util.ArrayList;
import BEC.CategoryInfo;
import BEC.DT_ACTIVITY_TYPE;
import BEC.GetCategoryListRsp;
import BEC.IntelliPickStockEx;
import BEC.IntelliPickStockRspEx;
import BEC.RecommValueAdded;
import BEC.RecommValueAddedListRsp;

/**
 * Created by davidwei on 2017-11-02.
 *
 */
public final class StockPickModel implements DataSourceProxy.IRequestCallback {
    private final StockPickPresenter mPresenter;

    public StockPickModel(StockPickPresenter presenter) {
        mPresenter = presenter;
    }

    public void requestStrategyList() {
        DiscoverRequestManager.getStrategyListRequest(this);
    }

    public void requestBannerList() {
        AdRequestManager.requestDtActivityList(DT_ACTIVITY_TYPE.T_ACTIVITY_SMART_PICK_AD, this);
    }

    public void requestValueAddedList() {
        DiscoverRequestManager.getValueAddedListRequest(this);
    }

    public void requestIntelligentPickStockList(final String lastId) {
        DiscoverRequestManager.getIntelligentPickStockRequest(lastId, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_DISCOVER_STRATEGY_LIST:
                getStrategyListCallback(success, data);
                break;
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                mPresenter.onGetBannerList(EntityUtil.entityToActivityList(success, data));
                break;
            case EntityObject.ET_GET_STOCK_PICK_VALUEADDED_LIST:
                handleValueAddedListCallback(success, data);
                break;
            case EntityObject.ET_GET_DISCOVER_INTELLIGENT_PICK_STOCK:
                handleIntelligentPickStockListCallback(success, data);
                break;
            default:
                break;
        }
    }

    private void getStrategyListCallback(final boolean success, final EntityObject data) {
        ArrayList<CategoryInfo> strategyList = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetCategoryListRsp) {
                strategyList = ((GetCategoryListRsp) entity).vList;
            }
        }
        mPresenter.onGetStrategyList(strategyList);
    }

    private void handleValueAddedListCallback(boolean success, EntityObject data) {
        ArrayList<RecommValueAdded> valueAddedList = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof RecommValueAddedListRsp) {
                valueAddedList = ((RecommValueAddedListRsp) entity).vRecommValueAdded;
            }
        }
        mPresenter.onGetValueAddedList(valueAddedList);
    }

    private void handleIntelligentPickStockListCallback(final boolean success, final EntityObject data) {
        ArrayList<IntelliPickStockEx> pickStockList = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof IntelliPickStockRspEx) {
                pickStockList = ((IntelliPickStockRspEx) entity).vtIntelliPickStockEx;
            }
        }
        mPresenter.onGetPickStockList(pickStockList, (String) data.getExtra());
    }
}
