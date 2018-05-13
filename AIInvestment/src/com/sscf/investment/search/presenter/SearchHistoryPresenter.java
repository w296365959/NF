package com.sscf.investment.search.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dengtacj.component.entity.db.SearchHistoryItem;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.search.fragment.SearchHistoryFragment;
import com.sscf.investment.search.manager.SearchRequestManager;
import com.sscf.investment.utils.SecListItemUtils;
import java.util.ArrayList;
import java.util.List;

import BEC.RealTimeStockItem;

/**
 * Created by davidwei on 2017-10-30
 *
 */
public final class SearchHistoryPresenter implements DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final int REQUEST_NUM = 7;

    private static final int MSG_UPDATE_HISTORY_LIST = 1;
    private static final int MSG_UPDATE_HEADER_LIST = 2;

    public static final int TYPE_HOT_STOCK = 1;
    private int mType = TYPE_HOT_STOCK;

    private final SearchHistoryFragment mFragment;
    private final Handler mHandler;

    private ArrayList<SecListItem> mHotStockList;

    public SearchHistoryPresenter(SearchHistoryFragment fragment) {
        this.mFragment = fragment;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void getHistoryList() {
        DengtaApplication.getApplication().defaultExecutor.execute(() ->
                mHandler.obtainMessage(MSG_UPDATE_HISTORY_LIST, SearchHistoryItem.findAllItemFromDb()).sendToTarget());
    }

    public void requestData() {
        if (NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            requestHotStock();
        }
    }

    private void requestHotStock() {
        SearchRequestManager.requestHotStock(REQUEST_NUM, this);
    }

    public void setType(final int type) {
        if (mType == type) {
            return;
        }
        mType = type;
        updateHeaderList();
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_HOT_STOCK:
                final ArrayList<RealTimeStockItem> hotStockList = EntityUtil.entityToRealTimeStockItemList(success, data);
                final ArrayList<SecListItem> hotStockSecList = SecListItemUtils.getSecListFromRealTimeStockItemList(hotStockList);
                if (hotStockSecList != null && hotStockSecList.size() > 0) {
                    mHotStockList = hotStockSecList;
                    if (mType == TYPE_HOT_STOCK) {
                        mHandler.sendEmptyMessage(MSG_UPDATE_HEADER_LIST);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_HISTORY_LIST:
                mFragment.updateHistory((List<SearchHistoryItem>) msg.obj);
                break;
            case MSG_UPDATE_HEADER_LIST:
                updateHeaderList();
                break;
            default:
                break;
        }
        return true;
    }

    private void updateHeaderList() {
        switch (mType) {
            case TYPE_HOT_STOCK:
                if (mHotStockList == null && NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
                    requestHotStock();
                }
                mFragment.updateHeaderList(mHotStockList, mType);
                break;
            default:
                break;
        }
    }
}
