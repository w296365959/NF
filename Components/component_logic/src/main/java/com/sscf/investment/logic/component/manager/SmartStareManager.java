package com.sscf.investment.logic.component.manager;

import com.dengtacj.component.entity.stare.StareGroupInfo;
import com.dengtacj.component.entity.stare.StareItemInfo;
import com.dengtacj.component.entity.stare.StareSubGroupInfo;
import com.dengtacj.component.managers.ISmartStareManager;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BEC.QueryStareStrategyReq;
import BEC.QueryStareStrategyRsp;
import BEC.StareStrategyClass;

/**
 * Created by yorkeehuang on 2017/9/18.
 */

public class SmartStareManager implements ISmartStareManager, DataSourceProxy.IRequestCallback {

    private static final String CFG_SMART_STARE = "smart_stare.cfg";

    private static final String KEY_TIME = "time";
    private static final String KEY_STRATEGY = "strategy";

    private static final String KEY_GROUP_NAME = "groupName";
    private static final String KEY_STRA_TYPE = "straType";
    private static final String KEY_SUB_GROUP = "subGroup";
    private static final String KEY_SUB_GROUP_NAME = "subGroupName";
    private static final String KEY_ITEM_LIST = "itemList";
    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE = "value";

    private List<StareGroupInfo> mTimeGroupInfo = new ArrayList<>();
    private List<StareGroupInfo> mStrategyGroupInfo = new ArrayList<>();

    @Override
    public void init() {
        initDefaultData();
        requestStrategy();
    }

    private void initDefaultData() {
        byte[] content = new byte[0];
        try {
            content = FileUtil.getByteArrayFromInputStream(FileUtil.openAssetsInput(CFG_SMART_STARE));
            String contentText = new String(content,"UTF-8");
            JSONTokener tokener = new JSONTokener(contentText);
            JSONObject root = new JSONObject(tokener);
            mTimeGroupInfo = parseSubGroup(root.getJSONArray(KEY_TIME));
            mStrategyGroupInfo = parseSubGroup(root.getJSONArray(KEY_STRATEGY));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestStrategy() {
        QueryStareStrategyReq req = new QueryStareStrategyReq();
        req.setStUserInfo(SDKManager.getInstance().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_QUERY_STARE_STRATEGY, req, this);
    }

    private List<StareGroupInfo> parseSubGroup(JSONArray groupRoot) throws JSONException {
        if(groupRoot != null) {
            int groupCount = groupRoot.length();
            if(groupCount > 0) {
                List<StareGroupInfo> stareGroupInfos = new ArrayList<>(groupCount);
                for(int groupIndex=0; groupIndex<groupCount; groupIndex++) {
                    JSONObject group = groupRoot.getJSONObject(groupIndex);
                    String groupName = group.optString(KEY_GROUP_NAME);
                    int straType = group.optInt(KEY_STRA_TYPE);
                    JSONArray subGroupJsonArray = group.getJSONArray(KEY_SUB_GROUP);

                    List<StareSubGroupInfo> subGroupInfoList = new ArrayList<>(subGroupJsonArray.length());
                    for(int subGroupIndex=0, size=subGroupJsonArray.length(); subGroupIndex<size; subGroupIndex++) {
                        JSONObject subGroupJson = subGroupJsonArray.getJSONObject(subGroupIndex);
                        String subGroupName = subGroupJson.getString(KEY_SUB_GROUP_NAME);
                        JSONArray itemArray = subGroupJson.getJSONArray(KEY_ITEM_LIST);
                        List<StareItemInfo> itemList = new ArrayList<>(itemArray.length());
                        for(int itemIndex=0, itemListSize=itemArray.length(); itemIndex<itemListSize; itemIndex++) {
                            JSONObject item = itemArray.getJSONObject(itemIndex);
                            String name = item.getString(KEY_NAME);
                            int value = item.getInt(KEY_VALUE);
                            itemList.add(new StareItemInfo(value, name));
                        }
                        subGroupInfoList.add(new StareSubGroupInfo(subGroupName, itemList));
                    }
                    stareGroupInfos.add(new StareGroupInfo(groupName, straType, subGroupInfoList));
                }

                return stareGroupInfos;
            }
        }
        return null;
    }

    @Override
    public List<StareGroupInfo> getStareTimeList() {
        return mTimeGroupInfo;
    }

    @Override
    public List<StareGroupInfo> getStrategyGroupList() {
        return mStrategyGroupInfo;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_QUERY_STARE_STRATEGY:
                handleStareStrategyRsp(success, data);
                break;
            default:
        }
    }

    private void handleStareStrategyRsp(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            QueryStareStrategyRsp rsp = (QueryStareStrategyRsp) data.getEntity();
            List<StareStrategyClass> timeClassList = rsp.getVBroadcastTime();
            if(timeClassList != null) {
                mTimeGroupInfo.clear();
                for(StareStrategyClass timeClass : timeClassList) {
                    mTimeGroupInfo.add(new StareGroupInfo(timeClass));
                }
            }

            List<StareStrategyClass> strategyClassList = rsp.getVClass();
            if(strategyClassList != null) {
                mStrategyGroupInfo.clear();
                for(StareStrategyClass strategyClass : strategyClassList) {
                    mStrategyGroupInfo.add(new StareGroupInfo(strategyClass));
                }
            }
        }
    }
}
