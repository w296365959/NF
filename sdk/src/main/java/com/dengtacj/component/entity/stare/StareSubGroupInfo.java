package com.dengtacj.component.entity.stare;

import java.util.ArrayList;
import java.util.List;

import BEC.StareStrategyDetail;
import BEC.StareStrategySubClass;

/**
 * Created by yorkeehuang on 2017/9/18.
 */

public class StareSubGroupInfo {

    public String name;
    public List<StareItemInfo> itemInfoList;

    public StareSubGroupInfo(String name, List<StareItemInfo> itemInfoList) {
        this.name = name;
        this.itemInfoList = itemInfoList;
    }

    public StareSubGroupInfo(StareStrategySubClass subClass) {
        name = subClass.getSName();
        itemInfoList = createStradegyItemList(subClass.getVDetail());
    }

    private List<StareItemInfo> createStradegyItemList(List<StareStrategyDetail> detailList) {
        List<StareItemInfo> itemInfoList = new ArrayList<>(detailList.size());
        for(StareStrategyDetail detail : detailList) {
            StareItemInfo itemInfo = new StareItemInfo(detail.getIId(), detail.getSName());
            itemInfoList.add(itemInfo);
        }
        return itemInfoList;
    }
}
