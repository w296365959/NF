package com.dengtacj.component.entity.stare;

import java.util.ArrayList;
import java.util.List;

import BEC.StareStrategyClass;
import BEC.StareStrategySubClass;

/**
 * Created by yorkeehuang on 2017/9/18.
 */

public class StareGroupInfo {
    public String groupTitle;
    public int straType;
    public List<StareSubGroupInfo> subGroupInfos;
    public List<StareItemInfo> allItems;

    public StareGroupInfo(String groupTitle, int straType, List<StareSubGroupInfo> subGroupInfos) {
        this.groupTitle = groupTitle;
        this.straType = straType;
        this.subGroupInfos = subGroupInfos;
        allItems = new ArrayList<>();
        for(StareSubGroupInfo subGroupInfo : subGroupInfos) {
            allItems.addAll(subGroupInfo.itemInfoList);
        }
    }

    public StareGroupInfo(StareStrategyClass strategyClass) {
        this.groupTitle = strategyClass.getSName();
        this.straType = strategyClass.getIStraType();
        this.subGroupInfos = createSubGroupInfos(strategyClass.getVSubClass());
        allItems = new ArrayList<>();
        for(StareSubGroupInfo subGroupInfo : subGroupInfos) {
            allItems.addAll(subGroupInfo.itemInfoList);
        }
    }

    private List<StareSubGroupInfo> createSubGroupInfos(List<StareStrategySubClass> subclassList) {
        if(subclassList != null && !subclassList.isEmpty()) {
            List<StareSubGroupInfo> subGroupInfos = new ArrayList<>(subclassList.size());
            for(StareStrategySubClass subClass : subclassList) {
                subGroupInfos.add(new StareSubGroupInfo(subClass));
            }
            return subGroupInfos;
        }
        return null;
    }
}
