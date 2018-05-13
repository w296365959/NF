package com.sscf.investment.detail.model;

import com.sscf.investment.detail.manager.SecurityDetailRequestManager;
import com.sscf.investment.detail.presenter.AStockNoticePresenter;
import com.sscf.investment.main.manager.AppConfigRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.sdk.utils.EntityUtil;
import java.util.ArrayList;
import BEC.AnnoucementType;
import BEC.AnnoucementTypeList;
import BEC.E_CONFIG_TYPE;
import BEC.NewsRsp;

/**
 * Created by davidwei on 2017/04/24
 */
public final class AStockNoticeModel implements DataSourceProxy.IRequestCallback {
    private final AStockNoticePresenter mPresenter;

    public AStockNoticeModel(final AStockNoticePresenter presenter) {
        mPresenter = presenter;
    }

    public void requestAnnouncementRemind(final String dtSecCode) {
        SecurityDetailRequestManager.requestAnnouncementRemind(dtSecCode, getAnnouncementRemindRequestCallback());
    }

    public void requestNoticeList(final String dtSecCode, final String type) {
        SecurityDetailRequestManager.requestNoticeList(dtSecCode, type, this);
    }

    public void requestAnnoucementType() {
        AppConfigRequestManager.requestAnnoucementType(this);
    }

    private DataSourceProxy.IRequestCallback getAnnouncementRemindRequestCallback() {
        return (success, data) -> mPresenter.onGetAnnouncementRemind(EntityUtil.entityToNewsList(success, data));
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_NEWS:
                final NewsRsp newsRsp = EntityUtil.entityToNewsRsp(success, data);
                mPresenter.onGetNoticeList(newsRsp);
                break;
            case EntityObject.ET_CONFIG_GET_ANNOUCEMENT_TYPE:
                final byte[] configData = EntityUtil.entityToConfigData(success, data, E_CONFIG_TYPE.E_ANNOUCEMWNT_TYPE_LIST);
                ArrayList<AnnoucementType> list = null;
                if (configData != null) {
                    final AnnoucementTypeList typeList = new AnnoucementTypeList();
                    ProtoManager.decode(typeList, configData);
                    list = typeList.vItem;
                }
                mPresenter.onGetAnnoucementType(list);
                break;
            default:
                break;
        }
    }
}
