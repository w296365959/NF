package com.dengtacj.component.managers;

import com.dengtacj.component.callback.OnLocalDataUpdateCallback;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.entity.db.StockDbEntity;

import java.util.List;

/**
 * Created by davidwei on 2017-09-05
 */

public interface IPortfolioDataManager {
    // 系统分组ID
    int SYSTEM_GROUP_ID = 0;

    int GROUP_NAME_VALID = 0;
    int GROUP_NAME_INVALID_TOO_LONG = 1;
    int GROUP_NAME_INVALID_CHAR_NOT_ALLOWED = 2;
    int GROUP_NAME_INVALID_ALREADY_EXIST = 3;

    void reloadData(boolean isUserChange);
    List<StockDbEntity> getAllStockList(boolean includeDelete, boolean isSort);
    List<GroupEntity> getAllGroup(boolean isIncludeDel, boolean isSort);
    int getChineseStockSize();
    int getHKStockSize();
    int getUSAStockSize();
    int getPositionStockSize();
    int getAddStockCount();
    int getDelStockCount();
    GroupEntity getCurrentGroup();
    String getCurrentGroupName();
    boolean isPortfolio(String secCode);
    StockDbEntity getStockDbEntity(String uniCode);
    String getUserGroupName(int groupId);
    GroupEntity getUserGroup(int groupId);
    boolean isEmptyFromCurrentGroup();
    List<StockDbEntity> getAllStockFromCurrentGroup(boolean includeDelete, boolean isSort);
    String getComment(String secCode);
    void addStock(String uniCode, String name);
    GroupEntity addGroup(int createTime, int updateTime, String name, boolean syncNow);
    void addStock2Group(int groupId, String secCode, String name);
    void changeCurrentGroup(int createTime);
    void deleteStockFromAllGroup(List<String> uniCodeList, final boolean sync);
    void delGroup(int createTime, boolean isDelStock, boolean syncNow);
    void deletePortfolioStockFromCurrentGroup(List<String> uniCodeList, final boolean sync);
    void updateReminderInfo(final StockDbEntity entity);
    void updateGroupList(final List<GroupEntity> groupEntities);
    void modifyDeletePortfolio(final String uniCode, final boolean sync);
    void modifyGroupName(int groupId, String destName);
    void updateStockList(final List<StockDbEntity> stockList);
    void updatePositionStatus(String ode, boolean isPosition);
    void setComment(String secCode, String comment, boolean syncNow);
    int checkGroupNameValid(String groupName);
    void addDataUpdateListener(OnLocalDataUpdateCallback dataUpdateCallback);
}
