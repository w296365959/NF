package com.dengtacj.component.entity.db;

import android.text.TextUtils;
import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.afinal.annotation.sqlite.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Free on 2016/6/14.
 *
 * 自选分组
 */
@Table(name = "portfolio_group")
public class GroupEntity implements Comparable<GroupEntity> {
    @Transient
    private static final long serialVersionUID = 0L;

    @Id(column="id")
    private int _id;            //自增ID

    @Property(column="user_id")
    private String user_id;         //对应的用户名

    @Property(column="name")
    private String name;         // 分组名称

    @Property(column="createTime")
    private int createTime;      // 分组排序时间

    @Property(column="update_time")
    private int updateSortTime;      // 分组排序时间

    @Property(column="stocks")
    private String stocks; // 此分组包含的股票,以json方式存储

    @Property(column="isDel")
    private boolean isDel;       // 是否删除的分组

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getUpdateSortTime() {
        return updateSortTime;
    }

    public void setUpdateSortTime(int updateSortTime) {
        this.updateSortTime = updateSortTime;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    @Override
    public int compareTo(GroupEntity another) {
        int lhs = this.updateSortTime;
        int rhs = another.updateSortTime;

        return lhs > rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "group name=" + name + ", isDel=" + isDel + ", updateSortTime=" + updateSortTime + ", stocks=" + stocks;
    }

    /**
     * 检查分组中是否包含了一只股票
     * @param dtcode
     * @return
     */
    public boolean isHaveStock(boolean isIncludeDel, String dtcode) {
        if (TextUtils.isEmpty(stocks)) {
            return false;
        }

        List<GroupStock> allStock = getAllStock(isIncludeDel, false);
        for (GroupStock groupStock : allStock) {
            if (TextUtils.equals(groupStock.dtcode, dtcode)) {
                if (isIncludeDel || !groupStock.isDel) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 添加一个股票到分组
     * @param dtcode
     */
    public void addStock(String dtcode) {
        if (TextUtils.isEmpty(dtcode)) {
            return;
        }

        List<GroupStock> stockList = getAllStock(true, false);
        boolean found = false;

        int now = (int) (System.currentTimeMillis() / 1000);
        for (GroupStock groupStock : stockList) {
            if (TextUtils.equals(groupStock.dtcode, dtcode)) {
                groupStock.isDel = false;
                groupStock.updateTime = now;
                found = true;
                break;
            }
        }
        if (!found) {
            stockList.add(new GroupStock(dtcode, now, false));
        }

        flushStocks(stockList);
    }

    public void addStock(List<GroupStock> stockList) {
        if (stockList == null || stockList.size() < 1) {
            return;
        }

        List<GroupStock> localStockList = getAllStock(true, false);
        localStockList.addAll(stockList);

        flushStocks(localStockList);
    }

    public void flushStocks(List<GroupStock> stockList) {
        JSONObject stocksJson = new JSONObject();
        try {
            JSONArray stockListJson = new JSONArray();
            for (GroupStock entity : stockList) {
                JSONObject stock = new JSONObject();
                stock.put("code", entity.dtcode);
                stock.put("time", entity.updateTime);
                stock.put("isDel", entity.isDel);
                stockListJson.put(stock);
            }

            stocksJson.put("stocks", stockListJson);

            stocks = stocksJson.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从分组删除一个股票
     * @param dtcode
     */
    public boolean delStock(String dtcode) {
        if (TextUtils.isEmpty(dtcode)) {
            return false;
        }

        List<GroupStock> stockList = getAllStock(true, false);
        for (GroupStock stock : stockList){
            if (stock.dtcode.equals(dtcode)) {
                stock.isDel = true;
                flushStocks(stockList);
                return true;
            }
        }
        return false;
    }

    /**
     * 更新分组中一个股票的时间
     * @param dtcode
     * @param updateTime
     */
    public void updateStock(String dtcode, int updateTime, boolean isDel) {
        if (TextUtils.isEmpty(dtcode)) {
            return;
        }

        List<GroupStock> stockList = getAllStock(true, false);
        for (GroupStock stock : stockList){
            if (stock.dtcode.equals(dtcode)) {
                stock.updateTime = updateTime;
                stock.isDel = isDel;
                flushStocks(stockList);
                break;
            }
        }
    }

    /**
     * 获取分组的所有股票
     * @return
     */
    public List<GroupStock> getAllStock(boolean isIncludeDel, boolean isSort) {
        List<GroupStock> entityList = new ArrayList<>();

        if (TextUtils.isEmpty(stocks)) {
            return entityList;
        }

        try {
            JSONTokener tokener = new JSONTokener(stocks);
            JSONObject stockJsonObject = (JSONObject)tokener.nextValue();
            JSONArray stockArray = stockJsonObject.getJSONArray("stocks");
            for (int i = 0; i < stockArray.length(); i++) {
                JSONObject stock = (JSONObject) stockArray.get(i);
                boolean isDel = stock.getBoolean("isDel");
                if (isIncludeDel || !isDel) {
                    entityList.add(new GroupStock(stock.getString("code"), stock.getInt("time"), isDel));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isSort) {
            Collections.sort(entityList);
        }

        return entityList;
    }

    /**
     * 分组中股票存储实体
     */
    public static class GroupStock implements Comparable<GroupStock> {
        public String dtcode;
        public int updateTime;
        public boolean isDel;

        public GroupStock(String dtcode, int updateTime, boolean isDel) {
            this.dtcode = dtcode;
            this.updateTime = updateTime;
            this.isDel = isDel;
        }

        @Override
        public int compareTo(GroupStock another) {
            int lhs = this.updateTime;
            int rhs = another.updateTime;

            return lhs > rhs ? -1 : (lhs == rhs ? 0 : 1);
        }

        @Override
        public String toString() {
            return "group stock dtcode=" + dtcode + ", updateTime=" + updateTime + ", isDel=" + isDel;
        }
    }
}
