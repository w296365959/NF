package com.sscf.investment.detail.presenter;

import com.sscf.investment.detail.fragment.AStockNoticeFragment;
import com.sscf.investment.detail.model.AStockNoticeModel;
import com.sscf.investment.sdk.utils.ThreadUtils;
import java.util.ArrayList;
import BEC.AnnoucementType;
import BEC.NewsDesc;
import BEC.NewsRsp;

/**
 * Created by davidwei on 2017/12/14
 */
public final class AStockNoticePresenter {
    private AStockNoticeFragment mFragment;
    private AStockNoticeModel mModel;
    private String mDtSecCode;
    private ArrayList<AnnoucementType> mAnnoucementTypes;
    private String mCurrentType = "";

    public AStockNoticePresenter(final AStockNoticeFragment fragment, final String dtSecCode) {
        mFragment = fragment;
        mModel = new AStockNoticeModel(this);
        mDtSecCode = dtSecCode;
    }

    public void requestData() {
        mModel.requestAnnouncementRemind(mDtSecCode);
        mModel.requestNoticeList(mDtSecCode, mCurrentType);
        if (mAnnoucementTypes == null) {
            mModel.requestAnnoucementType();
        }
    }

    public void onGetAnnouncementRemind(final ArrayList<NewsDesc> newsList) {
        ThreadUtils.runOnUiThread(() -> mFragment.updateAnnouncementRemind(newsList));
    }

    public void onGetNoticeList(NewsRsp newsRsp) {
        ThreadUtils.runOnUiThread(() -> {
            if (mFragment.isAdded()) {
                if (newsRsp != null) {
                    mFragment.onLoadComplete();
                    final ArrayList<NewsDesc> newsDesc = newsRsp.stNewsList.vNewsDesc;
                    mFragment.updateNewsList(newsDesc, newsRsp.iStatus);
                } else {
                    mFragment.onLoadFailed();
                }
            }
        });
    }

    public void onGetAnnoucementType(final ArrayList<AnnoucementType> list) {
        mAnnoucementTypes = list;
    }

    public ArrayList<AnnoucementType> getAnnoucementTypeList() {
        return mAnnoucementTypes;
    }

    public void setCurrentAnnoucementType(final String type) {
        mCurrentType = type;
        mModel.requestNoticeList(mDtSecCode, type);
    }
}
