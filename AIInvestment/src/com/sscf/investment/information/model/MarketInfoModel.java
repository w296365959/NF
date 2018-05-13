package com.sscf.investment.information.model;

import com.sscf.investment.information.manager.MarketInfoRequestManager;
import com.sscf.investment.information.presenter.MarketInfoPresenter;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;

/**
 * Created by davidwei on 2017-06-08
 */
public final class MarketInfoModel implements DataSourceProxy.IRequestCallback {
    private static final String TAG = MarketInfoModel.class.getSimpleName();

    private final MarketInfoPresenter mPresenter;

    public MarketInfoModel(final MarketInfoPresenter presenter) {
        mPresenter = presenter;
    }

    public void requestNewsList(final String startId, final String endId, final int newsFlag) {
        MarketInfoRequestManager.requestNewsList(startId, endId, newsFlag, this);
    }

    public void requestAdList() {
        MarketInfoRequestManager.requestAdList(this);
    }

    public void requestBannerList() {
        MarketInfoRequestManager.requestBannerList(this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_DISCOVER_NEWS_LIST:
                mPresenter.onGetNewsList(EntityUtil.entityToMarketNewsList(success, data), (String) data.getExtra());
                break;
            case EntityObject.ET_MARKET_INFO_AD:
                mPresenter.onGetMarketAdList(EntityUtil.entityToAdList(success, data));
                break;
            case EntityObject.ET_GET_DISCOVER_BANNER:
                mPresenter.onGetBannerList(EntityUtil.entityToBannerList(success, data));
                break;
            default:
                break;
        }
    }
}
