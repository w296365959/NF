package com.dengtacj.component.entity.db;

import android.text.TextUtils;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.afinal.annotation.sqlite.Transient;
import com.sscf.investment.sdk.utils.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import BEC.CommentInfo;
import BEC.E_SEC_STATUS;
import BEC.ProSecInfo;
import BEC.SecSimpleQuote;

/**
 * 自选个股 db存储结构
 */
@Table(name = "portfolio_stock")
public class StockDbEntity implements Comparable<StockDbEntity> , Cloneable {
    @Transient
    private static final long serialVersionUID = 0L;

    @Transient
    private static final String LIST_SEPERATOR = ",";

    @Id(column="id")
    private int _id;            //自增ID
    @Property(column="groupID")
    private String groupID;     //分组
    @Property(column="mType")
    private int mType;          //自选种类
    @Property(column="szName")
    private String szName="";      //名称
    @Property(column="fnow")
    private float fNow;             //现价
    @Property(column="fclose")
    private float fClose;           //昨收价
    @Property(column="itpflag")
    private int iTpFlag;          //小数点精度
    @Property(column="user_id")
    private String user_id;         //用户id
    @Property(column="dt_sec_code")
    private String dtSecCode;       //证券唯一编码
    @Property
    private float totalmarketvalue; // 总市值
    @Property
    public float highPrice = -1;
    @Property
    public float lowPrice = -1;
    @Property
    public float increasePer = -1;
    @Property
    public float decreasesPer = -1;
    @Property
    public boolean pushAnnouncement = false;
    @Property
    public boolean pushResearch = false;
    @Property
    private int iStatus;       // 是否停牌 0正常/1停牌

    @Property(column="create_time")
    public int iCreateTime = -1;    // 自选创建时间
    @Property(column="update_time")
    public int iUpdateTime = -1;    // 自选更新时间
    @Property(column="is_del")
    public boolean isDel = false;    // 是否删除的自选
    @Property(column="is_positions")
    public boolean isPosition = false;    // 是否持仓

    @Property(column="comment")
    public String comment;    // 备注
    @Property(column="comment_create_time")
    public int iCommentCreateTime = -1;    // 备注创建时间
    @Property(column="comment_update_time")
    public int iCommentUpdateTime = -1;    // 备注更新时间
    @Property
    private boolean aiAlert = true;    // 智能预警，默认开启

    @Property
    private boolean dkAlert = false;   // 多空提醒

    @Property
    private String broadcastTime = "";   // 盘中播报时间列表

    @Property
    private float chipHighPrice = -1;   // 筹码平均成本最高价

    @Property
    private float chipLowPrice = -1;   // 筹码平均成本最低价

    @Property
    private float mainHighPrice = -1;   // 主力筹码平均成本最高价

    @Property
    private float mainLowPrice = -1;   // 主力筹码平均成本最低价

    @Property
    private String strategyId = "";   // 策略提醒列表

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // 排序
    @Deprecated
    private static boolean is_asc;      // 是否升序
    @Deprecated
    private static SORT_TYPE sort_type;
    private enum SORT_TYPE {
        UPDATE_TIME,        // 时间排序
        UP_DOWN_PERCENT,    // 涨跌幅
        UP_DOWN_PRICE,      // 涨跌价
        TOTOAL_MARKET_VALUE, // 总市值
        PRICE                // 现价
    }

    public void updateData(SecSimpleQuote quote) {
        if (quote == null || TextUtils.isEmpty(quote.sSecName)) {
            return;
        }
        this.szName = quote.sSecName;
        this.fNow = quote.getFNow();
        this.fClose = quote.getFClose();
        this.iTpFlag = quote.getITpFlag();
        this.iStatus = quote.getESecStatus();
        this.totalmarketvalue = quote.getFTotalmarketvalue();
    }

    /**
     * 把同步实体转换成DB实体
     * @return
     */
    public static StockDbEntity convertSecInfo2DbEntity(ProSecInfo secInfo) {
        StockDbEntity dbEntity = new StockDbEntity();
        dbEntity.dtSecCode = secInfo.getSDtSecCode();
        dbEntity.szName = secInfo.getSName();
        dbEntity.isPosition = secInfo.isHold;
        dbEntity.highPrice = secInfo.getFHighPrice();
        dbEntity.lowPrice = secInfo.getFLowPrice();
        dbEntity.increasePer = secInfo.getFIncreasePer();
        dbEntity.decreasesPer = secInfo.getFDecreasesPer();
        dbEntity.pushAnnouncement = secInfo.getBRecvAnnounce();
        dbEntity.pushResearch = secInfo.getBRecvResearch();
        dbEntity.iCreateTime = secInfo.getICreateTime();
        dbEntity.iUpdateTime = secInfo.getIUpdateTime();
        dbEntity.isDel = secInfo.getIsDel();
        dbEntity.comment = secInfo.stCommentInfo.sComment;
        dbEntity.iCommentCreateTime = secInfo.stCommentInfo.iCreateTime;
        dbEntity.iCommentUpdateTime = secInfo.stCommentInfo.iUpdateTime;
        dbEntity.aiAlert = secInfo.isAiAlert;
        dbEntity.dkAlert = secInfo.isDKAlert;
        dbEntity.broadcastTime = convertIntegerList2Text(secInfo.vBroadcastTime);
        dbEntity.chipHighPrice = secInfo.fChipHighPrice;
        dbEntity.chipLowPrice = secInfo.fChipLowPrice;
        dbEntity.mainHighPrice = secInfo.fMainChipHighPrice;
        dbEntity.mainLowPrice = secInfo.fMainChipLowPrice;
        dbEntity.strategyId = convertIntegerList2Text(secInfo.vStrategyId);
        return dbEntity;
    }

    /**
     * 把当前实体转换成同步用的实体
     * @return
     */
    public ProSecInfo convertDbEntity2SecInfo() {
        ProSecInfo secInfo = new ProSecInfo();
        secInfo.sDtSecCode = getDtSecCode();
        secInfo.sName = getSzName();
        secInfo.isHold = isPosition;
        secInfo.fHighPrice = getHighPrice();
        secInfo.fLowPrice = getLowPrice();
        secInfo.fIncreasePer = getIncreasePer();
        secInfo.fDecreasesPer = getDecreasesPer();
        secInfo.bRecvAnnounce = isPushAnnouncement();
        secInfo.bRecvResearch = isPushResearch();
        secInfo.iCreateTime = getICreateTime();
        secInfo.iUpdateTime = getIUpdateTime();
        secInfo.isDel = isDel();
        secInfo.stCommentInfo = new CommentInfo(comment, iCommentCreateTime, iCommentUpdateTime);
        secInfo.isAiAlert = isAiAlert();
        secInfo.isDKAlert = isDkAlert();
        secInfo.vBroadcastTime = convertText2IntegerList(getBroadcastTime());
        secInfo.fChipHighPrice = getChipHighPrice();
        secInfo.fChipLowPrice = getChipLowPrice();
        secInfo.fMainChipHighPrice = getMainHighPrice();
        secInfo.fMainChipLowPrice = getMainLowPrice();
        secInfo.vStrategyId = convertText2IntegerList(getStrategyId());
        return secInfo;
    }

    public static String convertIntegerList2Text(List<Integer> integerList) {
        if(integerList != null && !integerList.isEmpty()) {
            Collections.sort(integerList, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            });
            return TextUtils.join(LIST_SEPERATOR, integerList);
        }
        return "";
    }

    private static ArrayList<Integer> convertText2IntegerList(String integerListText) {
        if(!TextUtils.isEmpty(integerListText)) {
            String[] broadcastTimeArray = integerListText.split(LIST_SEPERATOR);
            if(broadcastTimeArray != null && broadcastTimeArray.length > 0) {
                ArrayList<Integer> broadcastTimeList = new ArrayList<>(broadcastTimeArray.length);
                for(String timeText : broadcastTimeArray) {
                    try {
                        int time = Integer.valueOf(timeText);
                        broadcastTimeList.add(time);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if(!broadcastTimeList.isEmpty()) {
                    return broadcastTimeList;
                }
            }
        }
        return null;
    }

    public List<Integer> getBroadcastTimeList() {
        return convertText2IntegerList(getBroadcastTime());
    }

    public List<Integer> getStrategyList() {
        return convertText2IntegerList(getStrategyId());
    }

    public float getDisplayNow() {
        final float now = getFNow();
        final float close = getFClose();
        if (now == 0) {
            return close;
        } else {
            return now;
        }
    }

    /**
     * 获取现价显示用的价格字符串
     * @return
     */
    public String getDisplayNowString() {
        final float displayNow = getDisplayNow();
        if (displayNow == 0) {
            return "--";
        } else {
            return NumberUtil.getFormattedFloat(displayNow, getITpFlag());
        }
    }

    /**
     * 获取显示用的涨跌幅百分比
     * @return
     */
    public String getDisplayUpDownPercent() {
        if (iStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            // 停牌状态，显示停牌
            return "停牌";
        }

        return NumberUtil.getUpdownString(fClose, fNow);
    }

    /**
     * 获取显示用的涨跌额
     * @return
     */
    public String getUpDownPrice() {
        if (iStatus == E_SEC_STATUS.E_SS_SUSPENDED) {
            // 停牌状态，显示停牌
            return "停牌";
        }

        if (fClose == 0 || fNow == 0) {
            // 数据无效
            return "--";
        }

        String upDownPrice = String.format("%.2f", (fNow - fClose));
        if (fNow - fClose > 0) {
            upDownPrice = "+" + upDownPrice;
        }
        return upDownPrice;
    }

    public boolean isPosition() {
        return isPosition;
    }

    public void setIsPosition(boolean isPosition) {
        this.isPosition = isPosition;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;

        //删除自选股的时候把之前用户设置的提醒项也全部清除
        if (isDel) {
            this.highPrice = 0;
            this.lowPrice = 0;
            this.increasePer = 0;
            this.decreasesPer = 0;
            this.pushAnnouncement = false;
            this.pushResearch = false;
            this.comment = "";
            this.iCommentCreateTime = -1;
            this.iCommentUpdateTime = -1;
        }
    }

    public int getICreateTime() {
        return iCreateTime;
    }

    public void setICreateTime(int iCreateTime) {
        this.iCreateTime = iCreateTime;
    }

    public int getIUpdateTime() {
        return iUpdateTime;
    }

    public void setIUpdateTime(int iUpdateTime) {
        this.iUpdateTime = iUpdateTime;
    }

    public int getIStatus() {
        return iStatus;
    }

    public void setIStatus(int iStatus) {
        this.iStatus = iStatus;
    }

    public float getFNow() {
        return fNow;
    }

    public void setFNow(float fNow) {
        this.fNow = fNow;
    }

    public float getFClose() {
        return fClose;
    }

    public void setFClose(float fClose) {
        this.fClose = fClose;
    }

    public int getITpFlag() {
        return iTpFlag;
    }

    public void setITpFlag(int iTpFlag) {
        this.iTpFlag = iTpFlag;
    }

    public void setPushAnnouncement(boolean pushAnnouncement) {
        this.pushAnnouncement = pushAnnouncement;
    }

    public boolean isPushAnnouncement() {
        return pushAnnouncement;
    }

    public void setPushResearch(boolean pushResearch) {
        this.pushResearch = pushResearch;
    }

    public boolean isPushResearch() {
        return pushResearch;
    }

    public void setHighPrice(float highPrice) {
        this.highPrice = highPrice;
    }

    public float getHighPrice() {
        return highPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setIncreasePer(float increasePer) {
        this.increasePer = increasePer;
    }

    public float getIncreasePer() {
        return increasePer;
    }

    public void setDecreasesPer(float decreasesPer) {
        this.decreasesPer = decreasesPer;
    }

    public float getDecreasesPer() {
        return decreasesPer;
    }

    public String getDtSecCode() {
        return dtSecCode;
    }

    public void setDtSecCode(String dtSecCode) {
        this.dtSecCode = dtSecCode;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public int getMType() {
        return mType;
    }

    public void setMType(int mType) {
        this.mType = mType;
    }

    public String getSzName() {
        return szName;
    }

    public void setSzName(String szName) {
        this.szName = szName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public float getTotalmarketvalue() {
        return totalmarketvalue;
    }

    public void setTotalmarketvalue(float totalmarketvalue) {
        this.totalmarketvalue = totalmarketvalue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getICommentCreateTime() {
        return iCommentCreateTime;
    }

    public void setICommentCreateTime(int iCommentCreateTime) {
        this.iCommentCreateTime = iCommentCreateTime;
    }

    public int getICommentUpdateTime() {
        return iCommentUpdateTime;
    }

    public void setICommentUpdateTime(int iCommentUpdateTime) {
        this.iCommentUpdateTime = iCommentUpdateTime;
    }

    public boolean isAiAlert() {
        return aiAlert;
    }

    public void setAiAlert(boolean aiAlert) {
        this.aiAlert = aiAlert;
    }


    public boolean isDkAlert() {
        return dkAlert;
    }

    public void setDkAlert(boolean dkAlert) {
        this.dkAlert = dkAlert;
    }

    public String getBroadcastTime() {
        return broadcastTime;
    }

    public void setBroadcastTime(String broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    public float getChipHighPrice() {
        return chipHighPrice;
    }

    public void setChipHighPrice(float chipHighPrice) {
        this.chipHighPrice = chipHighPrice;
    }

    public float getChipLowPrice() {
        return chipLowPrice;
    }

    public void setChipLowPrice(float chipLowPrice) {
        this.chipLowPrice = chipLowPrice;
    }

    public float getMainHighPrice() {
        return mainHighPrice;
    }

    public void setMainHighPrice(float mainHighPrice) {
        this.mainHighPrice = mainHighPrice;
    }

    public float getMainLowPrice() {
        return mainLowPrice;
    }

    public void setMainLowPrice(float mainLowPrice) {
        this.mainLowPrice = mainLowPrice;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public int compareTo(StockDbEntity another) {
        return another.iUpdateTime - this.iUpdateTime;
    }

    public static boolean is_asc() {
        return is_asc;
    }

    public static void setIs_asc(boolean is_asc) {
        StockDbEntity.is_asc = is_asc;
    }

    @Override
    public String toString() {
        return "_id=" + _id + ", dtSecCode=" + dtSecCode + ", szName=" + szName + ", iUpdateTime=" + iUpdateTime ;
    }
}
