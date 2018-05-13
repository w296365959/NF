package com.sscf.investment.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.sscf.investment.afinal.FinalDb;
import com.sscf.investment.sdk.ContextHolder;
import com.sscf.investment.sdk.utils.DtLog;

import java.util.List;

/**
 * Created by xuebinliu on 2015/8/10.
 *
 * 灯塔终端数据库管理
 */
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    // 数据库名字及版本号
    private static final String DB_NAME = "beacon.db";
    /**
     * 4.闪屏增加字段
     * 5.收藏增加字段
     * 6.自选股增加智能提醒字段
     */
    private static final int VERSION_CODE = 6;
    private static final String TABLE_PORTFOLIO_STOCK = "portfolio_stock";
    private static final String TABLE_SPLASH = "splash";
    private static final String TABLE_FAVOR = "setting_favor";
    private static final String TABLE_FAVOR_OPERATION = "setting_favor_operation";

    private static DBHelper instance;
    private static FinalDb DB;

    private static final String COMMENT = "comment";
    private static final String COMMENT_CREATE_TIME = "comment_create_time";
    private static final String COMMENT_UPDATE_TIME = "comment_update_time";
    private static final String AI_ALERT = "aiAlert";
    // VERSION_CODE 6增加字段
    private static final String DK_ALERT = "dkAlert";  // 多空提醒
    private static final String BROADCAST_TIME = "broadcastTime";  // 盘中播报时间列表
    private static final String CHIP_HIGH_PRICE = "chipHighPrice";  // 筹码平均成本最高价
    private static final String CHIP_LOW_PRICE = "chipLowPrice";  // 筹码平均成本最低价
    private static final String MAIN_HIGH_PRICE = "mainHighPrice";  // 主力筹码平均成本最高价
    private static final String MAIN_LOW_PRICE = "mainLowPrice";  // 主力筹码平均成本最低价
    private static final String STRATEGY_ID = "strategyId";  // 策略提醒列表

    private static final String SKIP_URL = "skipUrl";
    private static final String SKIP_STATE = "skipState";
    private static final String SKIP_SECOND = "skipSecond";
    private static final String PLAY = "play";

    private static final String THIRD_URL = "third_url";

    private DBHelper(Context context) {
        DtLog.d(TAG, "DBHelper create");
        DB = FinalDb.create(context, DB_NAME, true, VERSION_CODE, new FinalDb.DbUpdateListener() {
            @Override
            public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
                DtLog.d(TAG, "onUpgrade newVersion:" + newVersion);
                upgradePortfolioStockTable(dataBase, oldVersion, newVersion);
                upgradeSplashTable(dataBase, oldVersion, newVersion);
                upgradeFavorTable(dataBase, oldVersion, newVersion);
            }
        });
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper(ContextHolder.getCtx());
        }
        return instance;
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    /**
     * 查找所有的数据
     * @param clazz
     */
    public <T> List<T> findAll(Class<T> clazz) {
        if (DB != null && clazz != null) {
            return DB.findAll(clazz);
        }
        return null;
    }

    /**
     * 根据条件查找所有数据
     * @param clazz
     * @param strWhere 条件为空的时候查找所有数据
     */
    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
        if (DB != null && clazz != null) {
            try {
                return DB.findAllByWhere(clazz, strWhere);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据条件查找所有数据
     * @param clazz
     * @param strWhere 条件为空的时候查找所有数据
     */
    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy) {
        if (DB != null && clazz != null) {
            try {
                return DB.findAllByWhere(clazz, strWhere, orderBy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存数据库，速度要比save快
     * @param entity
     */
    public void add(Object entity) {
        if (DB != null && entity != null) {
            DB.save(entity);
        }
    }

    /**
     * 保存数据到数据库<br />
     * <b>注意：</b><br />
     * 保存成功后，entity的主键将被赋值（或更新）为数据库的主键， 只针对自增长的id有效
     *
     * @param entity
     *            要保存的数据
     * @return ture： 保存成功 false:保存失败
     */
    public boolean saveBindId(Object entity) {
        if (DB != null && entity != null) {
            return DB.saveBindId(entity);
        }
        return false;
    }

    /**
     * 删除数据
     * @param entity entity的主键不能为空
     */
    public void delete(Object entity) {
        if (DB != null && entity != null) {
            DB.delete(entity);
        }
    }

    public void deleteAll(Class<?> clazz) {
        if (DB != null) {
            DB.deleteAll(clazz);
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param clazz
     *            要删除的实体类
     * @param id
     *            主键值
     */
    public void deleteById(Class<?> clazz, Object id) {
        DB.deleteById(clazz, id);
    }

    public <T> T findById(Class<T> clazz, Object id) {
        return DB.findById(id, clazz);
    }

    /**
     * 根据条件删除数据
     *
     * @param clazz
     * @param strWhere
     *            条件为空的时候 将会删除所有的数据
     */
    public void deleteByWhere(Class<?> clazz, String strWhere) {
        DB.deleteByWhere(clazz, strWhere);
    }


    /**
     * 更新数据 （主键ID必须不能为空）
     * @param entity
     */
    public void update(Object entity) {
        try {
            if (DB != null && entity != null) {
                DB.update(entity);
            }
        } catch (Exception e) {
            DtLog.e(TAG, e.getMessage());
        }
    }

    /**
     * 删除自选股表的SQL语句
     */
    public static final String DROP_PORTFOLIO_STOCK_TABLE = "DROP TABLE IF EXISTS " + TABLE_PORTFOLIO_STOCK;

    /**
     * 数据库升级到Version 2
     * 添加备注相关的comment, comment_create_time和comment_update_time三个字段
     */
    public static final String[] UPDATE_PORTFOLIO_STOCK_TABLE_TO_VERSION_2 = new String[]{
        "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
            + " ADD COLUMN " + COMMENT + " TEXT;",
        "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
            + " ADD COLUMN " + COMMENT_CREATE_TIME + " INTEGER NOT NULL DEFAULT 0;",
        "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
            + " ADD COLUMN " + COMMENT_UPDATE_TIME + " TEXT;"
    };

    /**
     * 数据库升级到Version 3
     * 添加智能预警相关的
     */
    public static final String[] UPDATE_PORTFOLIO_STOCK_TABLE_TO_VERSION_3 = new String[]{
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + AI_ALERT + " REAL NOT NULL DEFAULT 1;"
    };

    /**
     * 数据库升级到Version 6
     * 添加多空提醒
     * 添加盘中播报时间列表
     * 添加筹码平均成本最高价
     * 添加筹码平均成本最低价
     * 添加主力筹码平均成本最高价
     * 添加策略提醒列表
     */
    public static final String[] UPDATE_PORTFOLIO_STOCK_TABLE_TO_VERSION_6 = new String[]{
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + DK_ALERT + " REAL NOT NULL DEFAULT 0;",
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + BROADCAST_TIME + " TEXT;",
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + CHIP_HIGH_PRICE + " FLOAT DEFAULT -1;",
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + CHIP_LOW_PRICE + " FLOAT DEFAULT -1;",
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + MAIN_LOW_PRICE + " FLOAT DEFAULT -1;",
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + MAIN_HIGH_PRICE + " FLOAT DEFAULT -1;",
            "ALTER TABLE " + TABLE_PORTFOLIO_STOCK
                    + " ADD COLUMN " + STRATEGY_ID + " TEXT;"
    };

    public static final String[] UPDATE_SPLASH_TABLE_TO_VERSION_2 = new String[]{
        "ALTER TABLE " + TABLE_SPLASH
            + " ADD COLUMN " + SKIP_URL + " TEXT;",
        "ALTER TABLE " + TABLE_SPLASH
            + " ADD COLUMN " + SKIP_STATE + " INTEGER NOT NULL DEFAULT -1;",
        "ALTER TABLE " + TABLE_SPLASH
            + " ADD COLUMN " + SKIP_SECOND + " INTEGER NOT NULL DEFAULT -1;"
    };

    public static final String[] UPDATE_SPLASH_TABLE_TO_VERSION_4 = new String[]{
            "ALTER TABLE " + TABLE_SPLASH
                    + " ADD COLUMN " + PLAY + " INTEGER NOT NULL DEFAULT 0"
    };

    public static final String[] UPDATE_FAVOR_TABLE_TO_VERSION_5 = new String[]{
            "ALTER TABLE " + TABLE_FAVOR
                    + " ADD COLUMN " + THIRD_URL + " INTEGER NOT NULL DEFAULT 0",
            "ALTER TABLE " + TABLE_FAVOR_OPERATION
                    + " ADD COLUMN " + THIRD_URL + " INTEGER NOT NULL DEFAULT 0",
    };

    private void upgradePortfolioStockTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion == 1) {
                DtLog.d(TAG, "upgradePortfolioStockTable");
                for(String sql : UPDATE_PORTFOLIO_STOCK_TABLE_TO_VERSION_2) {
                    execSQL(db, sql);
                }
                DtLog.d(TAG, "upgradePortfolioStockTable success");
            }
            if (oldVersion <= 2) {
                for(String sql : UPDATE_PORTFOLIO_STOCK_TABLE_TO_VERSION_3) {
                    execSQL(db, sql);
                }
            }

            if(oldVersion <= 5) {
                for(String sql : UPDATE_PORTFOLIO_STOCK_TABLE_TO_VERSION_6) {
                    execSQL(db, sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upgradeSplashTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            DtLog.d(TAG, "upgradeSplashTable");
            if (oldVersion == 1) {
                for(String sql : UPDATE_SPLASH_TABLE_TO_VERSION_2) {
                    execSQL(db, sql);
                }
            }
            if (oldVersion <= 3) {
                for(String sql : UPDATE_SPLASH_TABLE_TO_VERSION_4) {
                    execSQL(db, sql);
                }
            }
            DtLog.d(TAG, "upgradeSplashTable success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upgradeFavorTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            DtLog.d(TAG, "upgradeSplashTable");
            if (oldVersion <= 4) {
                for(String sql : UPDATE_FAVOR_TABLE_TO_VERSION_5) {
                    execSQL(db, sql);
                }
            }
            DtLog.d(TAG, "upgradeSplashTable success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void execSQL(SQLiteDatabase db, String sql) {
        if (db == null || TextUtils.isEmpty(sql)) {
            return ;
        }

        try {
            db.execSQL(sql);
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("duplicate column")) {
                throw new IllegalArgumentException(ex.getMessage(), ex.getCause());
            }
        }
    }
}
