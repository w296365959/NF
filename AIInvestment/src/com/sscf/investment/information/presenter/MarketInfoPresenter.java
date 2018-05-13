package com.sscf.investment.information.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.sscf.investment.R;
import com.sscf.investment.information.MarketInfoFragment;
import com.sscf.investment.information.model.MarketInfoModel;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import BEC.BannerInfo;
import BEC.E_NEWS_FLAG;
import BEC.MarketAd;
import BEC.NewsDesc;

/**
 * Created by davidwei on 2017-05-13.
 */

public final class MarketInfoPresenter implements Handler.Callback, Comparator<MarketAd> {
    private static final String TAG = MarketInfoPresenter.class.getSimpleName();

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_UPDATE_LIST_FAILED = 2;
    private static final int MSG_UPDATE_LIST_MORE = 3;
    private static final int MSG_UPDATE_LIST_NO_MORE =4;
    private static final int MSG_UPDATE_LIST_MORE_FAILED = 5;
    private static final int MSG_UPDATE_BANNER = 6;
    private static final int MSG_UPDATE_AD = 7;

    private final Handler mHandler;
    private final MarketInfoFragment mFragment;
    private final MarketInfoModel mModel;

    private ArrayList<NewsDesc> mNewsList;
    private String mLastNewsId;
    private ArrayList<MarketAd> mAdList;
    private ArrayList<BannerInfo> mBannerList;

    public MarketInfoPresenter(final MarketInfoFragment fragment) {
        mFragment = fragment;
        mModel = new MarketInfoModel(this);
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void requestData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            return;
        }

        mModel.requestAdList();

        final List<NewsDesc> newsList = mNewsList;
        final int size = newsList == null ? 0 : newsList.size();
        String endId = "";
        if (size > 0) {
            endId = newsList.get(0).sNewsID;
        }
        mModel.requestNewsList("", endId, E_NEWS_FLAG.E_NF_TOP);

        mModel.requestBannerList();
    }

    public void requestMoreData() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            return;
        }

        final List<NewsDesc> newsList = mNewsList;
        final int size = newsList == null ? 0 : newsList.size();
        int newsFlag = E_NEWS_FLAG.E_NF_NORMAL;
        if (size > 0) {
            newsFlag = newsList.get(0).eNewsFlag;
        }
        mModel.requestNewsList(mLastNewsId, "", newsFlag);
    }

    public void onGetNewsList(final ArrayList<NewsDesc> newsList, final String startId) {
        DtLog.d(TAG, "onGetNewsInfo newsList = " + newsList);
        if (TextUtils.isEmpty(startId)) { // 首页数据
            if (newsList == null) {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_FAILED);
            } else {
                final int size = newsList.size();
                if (size > 0) {
                    mLastNewsId = newsList.get(size - 1).sNewsID;
                }
                mHandler.obtainMessage(MSG_UPDATE_LIST, newsList).sendToTarget();
            }
        } else { // 分页数据
            if (newsList == null) {
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST_MORE_FAILED);
            } else {
                if (startId.equals(mLastNewsId)) { // 过滤重复数据
                    final int size = newsList.size();
                    if (size > 0) {
                        mLastNewsId = newsList.get(size - 1).sNewsID;
                        mHandler.obtainMessage(MSG_UPDATE_LIST_MORE, newsList).sendToTarget();
                    } else {
                        mHandler.sendEmptyMessage(MSG_UPDATE_LIST_NO_MORE);
                    }
                }
            }
        }
    }

    public void onGetMarketAdList(final ArrayList<MarketAd> adList) {
        final int size = adList == null ? 0 : adList.size();
        DtLog.d(TAG, "onGetMarketAdList size = " + size);
        if (adList != null) {
            mHandler.obtainMessage(MSG_UPDATE_AD, adList).sendToTarget();
        }
    }

    public void onGetBannerList(final ArrayList<BannerInfo> bannerList) {
        final int size = bannerList == null ? 0 : bannerList.size();
        DtLog.d(TAG, "onGetBannerList size = " + size);
        if (size > 0) {
            mHandler.obtainMessage(MSG_UPDATE_BANNER, bannerList).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        DtLog.d(TAG, "handleMessage msg.what : " + msg.what);
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                mFragment.refreshComplete();
                final ArrayList<NewsDesc> newsList = (ArrayList<NewsDesc>) msg.obj;
                final int size = newsList.size();
                if (size > 0) {
                    mNewsList = newsList;
                    mFragment.updateList(getNewsAndAdList(newsList, mAdList));
                    if (mBannerList != null) { // 有缓存需要显示的banner就刷新显示
                        mFragment.refreshBannerLayout(mBannerList);
                        mBannerList = null; // 显示完就清空
                    }
                } else {
                    mFragment.showEmptyLayout();
                }
                mHandler.removeMessages(MSG_UPDATE_LIST); // 防止重复刷新数据
                break;
            case MSG_UPDATE_LIST_FAILED:
                mFragment.refreshComplete();
                final ArrayList<NewsDesc> list = mNewsList;
                if (list != null && list.size() > 0) {
                    DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
                } else {
                    mFragment.showRetryLayout();
                }
                break;
            case MSG_UPDATE_LIST_MORE:
                mNewsList.addAll((ArrayList<NewsDesc>) msg.obj);
                mFragment.updateList(getNewsAndAdList(mNewsList, mAdList));
                break;
            case MSG_UPDATE_LIST_NO_MORE:
                mFragment.showFooterNoMoreLayout();
                break;
            case MSG_UPDATE_LIST_MORE_FAILED:
                mFragment.showFooterRetryLayout();
                break;
            case MSG_UPDATE_BANNER:
                final ArrayList<BannerInfo> bannerList = (ArrayList<BannerInfo>) msg.obj;
                if (mNewsList != null) { // 有新闻数据才显示banner
                    mFragment.refreshBannerLayout(bannerList);
                } else { // 没有新闻数据，需要缓存banner数据，在拉取到新闻数据以后显示banner
                    mBannerList = bannerList;
                }
                break;
            case MSG_UPDATE_AD:
                final ArrayList<MarketAd> adList = (ArrayList<MarketAd>) msg.obj;
                Collections.sort(adList, this);
                mAdList = adList;
                if (mNewsList != null) { // 防止重复刷新数据
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_UPDATE_LIST, mNewsList), 50L);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private ArrayList<Object> getNewsAndAdList(final ArrayList<NewsDesc> newsList, final ArrayList<MarketAd> adList) {
        final int newsSize = newsList == null ? 0 : newsList.size();
        final int adSize = adList == null ? 0 : adList.size();
        final ArrayList<Object> list = new ArrayList<>(newsSize + adSize);
        if (newsSize > 0) {
            list.addAll(newsList);
            if (adSize > 0) {
                final Iterator<MarketAd> it = adList.iterator();
                MarketAd ad;
                int insertPosition;
                int size;
                while (it.hasNext()) {
                    ad = it.next();
                    insertPosition = ad.iPos;
                    size = list.size();
                    if (insertPosition >= 0 && insertPosition <= size) {
                        list.add(insertPosition, ad);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public int compare(MarketAd o1, MarketAd o2) {
        return o1.iPos - o2.iPos;
    }
}
