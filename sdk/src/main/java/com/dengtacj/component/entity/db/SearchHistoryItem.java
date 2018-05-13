package com.dengtacj.component.entity.db;

import com.sscf.investment.afinal.annotation.sqlite.Id;
import com.sscf.investment.afinal.annotation.sqlite.Property;
import com.sscf.investment.afinal.annotation.sqlite.Table;
import com.sscf.investment.db.DBHelper;
import java.util.List;

/**
 * Created by davidwei on 2015-08-13.
 */
@Table(name = "search_history")
public final class SearchHistoryItem {
    @Id(column="id")
    private int id;//自增ID
    @Property(column="name")
    private String name;// 股票名或主题名
    @Deprecated
    @Property(column="type")
    private int type;// 类型，1:股票，2:主题
    @Property(column="uni_code")
    private String unicode;

    /**
     * 必须有空构造
     */
    public SearchHistoryItem() {
    }

    // 给主题使用的构造
    public SearchHistoryItem(String name, String unicode) {
        this.name = name;
        this.unicode = unicode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setType(int type) {
        this.type = type;
    }

    @Deprecated
    public int getType() {
        return type;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return unicode;
    }

    @Override
    public String toString() {
        return name;
    }

    public static void addItemToDb(final String name, final String unicode) {
        final DBHelper dbHelper = DBHelper.getInstance();
        dbHelper.deleteByWhere(SearchHistoryItem.class, String.format("uni_code='%s'", unicode));
        dbHelper.add(new SearchHistoryItem(name, unicode));
    }

    public static void clearAllItemFromDb() {
        DBHelper.getInstance().deleteAll(SearchHistoryItem.class);
    }

    public static List<SearchHistoryItem> findAllItemFromDb() {
        // 主题的数据去掉
        return DBHelper.getInstance().findAllByWhere(SearchHistoryItem.class, "type=0", "id desc");
    }
}
