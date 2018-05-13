package com.sscf.investment.discover.manager;

import BEC.GetStrategySubListRsp;
import BEC.IntelliStock;
import BEC.StrategySubItem;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.utils.DengtaConst;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by davidwei on 2016/04/13.
 */
public final class SubscriptionManager implements DataSourceProxy.IRequestCallback {

    private SubscriptionCallback mCallback;

    private GetStrategySubListRsp mRsp;
    private ArrayList<String> mDtSecCodes;

    public SubscriptionManager() {
    }

    public ArrayList<StrategySubItem> getSubscriptionList() {
        return mRsp != null ? mRsp.vList : null;
    }

    public ArrayList<String> getSubscriptionDtSecCodeList() {
        return mDtSecCodes;
    }

    public void getSubscriptionListRequest() {
        if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
            DiscoverRequestManager.getSubscriptionListRequest(this);
        }
    }

    public void getSubscriptionListRequest(final SubscriptionCallback callback) {
        if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
            DiscoverRequestManager.getSubscriptionListRequest(this);
            mCallback = callback;
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_STOCK_PICK_GET_SUBSCRIPTION_LIST:
                ArrayList<StrategySubItem> subscriptionList = null;
                if (success && data.getEntity() != null) {
                    final GetStrategySubListRsp rsp = (GetStrategySubListRsp) data.getEntity();
                    mRsp = rsp;
                    subscriptionList = rsp.vList;
                    mDtSecCodes = createDtSecCodes(subscriptionList);
                    final int time = SettingPref.getInt(DengtaConst.KEY_SUBSCRIPTION_UPDATE_TIME, 0);
                    if (rsp.iUpdateTimeStamp > time) {
                        DengtaApplication.getApplication().getRedDotManager().setSubscriptionState(true);
                    }
                }
                if (mCallback != null) {
                    mCallback.onGetSubscriptionList(success, subscriptionList);
                    mCallback = null;
                }
                break;
            default:
                break;
        }
    }

    private static ArrayList<String> createDtSecCodes(final ArrayList<StrategySubItem> subscriptionList) {
        final int size = subscriptionList == null ? 0 : subscriptionList.size();
        if (size > 0) {
            final HashSet<String> dtCodesSet = new HashSet<>(size);
            int stockSize;
            ArrayList<IntelliStock> stockList;
            for (StrategySubItem item : subscriptionList) {
                stockList = item.vtIntelliStock;
                stockSize = stockList == null ? 0 : stockList.size();
                if (stockSize == 0) {
                    continue;
                }
                int i = 0;
                for (IntelliStock stock : stockList) {
                    dtCodesSet.add(stock.sDtSecCode);
                    i++;
                    if (i >= 3) {
                        break;
                    }
                }
            }
            if (dtCodesSet.size() > 0) {
                return new ArrayList<>(dtCodesSet);
            }
        }
        return null;
    }

    public void saveUpdateTime() {
        if (mRsp != null) {
            SettingPref.putInt(DengtaConst.KEY_SUBSCRIPTION_UPDATE_TIME, mRsp.iUpdateTimeStamp);
        }
        DengtaApplication.getApplication().getRedDotManager().setSubscriptionState(false);
    }

    public interface SubscriptionCallback {
        void onGetSubscriptionList(final boolean success, final ArrayList<StrategySubItem> subscriptionList);
    }
}
