package com.dengtacj.component.managers;

import com.dengtacj.component.entity.stare.StareGroupInfo;
import com.dengtacj.component.entity.stare.StareSubGroupInfo;

import java.util.List;

/**
 * Created by yorkeehuang on 2017/9/18.
 */

public interface ISmartStareManager {

    void init();

    List<StareGroupInfo> getStareTimeList();

    List<StareGroupInfo> getStrategyGroupList();
}
