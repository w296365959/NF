package com.sscf.investment.main;

import android.content.Context;
import android.content.SharedPreferences;
import com.sscf.investment.detail.FragmentFactory;
import com.sscf.investment.setting.SettingConst;

/**
 * Created by liqf on 2016/1/21.
 */
public class DataPref {
    public static final String DATA_PREF = "data_pref";
    private static final String KEY_HW_DEVICE_TOKEN = "hw_device_token";

    public static final String KEY_LINE_CHART_GROUP_ENTRANCE_CLICKED = "KEY_KEY_LINE_CHART_GROUP_ENTRANCE_CLICKED";
    public static final String KEY_LINE_CHART_GROUP_ENTRANCE_EXPANDED = "KEY_KEY_LINE_CHART_GROUP_ENTRANCE_EXPANDED";
    public static final String KEY_STOCK_DETAIL_USER_GUIDE_DISPLAYED = "KEY_STOCK_DETAIL_USER_GUIDE_DISPLAYED";
    private static final String KEY_STOCK_TAB_TAG = "KEY_STOCK_TAB_TAG";
    private static final String KEY_PLATE_TAB_TAG = "KEY_PLATE_TAB_TAG";
    private static final String KEY_INDEX_TAB_TAG = "KEY_INDEX_TAB_TAG";
    private static final String KEY_HONGKONG_STOCK_TAB_TAG = "KEY_HONGKONG_STOCK_TAB_TAG";
    private static final String KEY_USA_STOCK_TAB_TAG = "KEY_USA_STOCK_TAB_TAG";
    private static final String KEY_FUND_TAB_TAG = "KEY_FUND_TAB_TAG";
    private static final String KEY_DENGTA_A_TAB_TAG = "KEY_DENGTA_A_TAB_TAG";
    private static final String KEY_LAST_LIVE_MSG_TIME = "KEY_LAST_LIVE_MSG_TIME";
    private static final String KEY_HOTFIX_PATCH_MD5 = "KEY_HOTFIX_PATCH_MD5";

    private static final String KEY_HONGKONG_DELAY_PROMPT_DISPLAYED = "KEY_HONGKONG_DELAY_PROMPT_DISPLAYED";
    private static final String KEY_USA_DELAY_PROMPT_DISPLAYED = "KEY_USA_DELAY_PROMPT_DISPLAYED";

    private static final String KEY_LAST_REPORT_INSTALLED_APPS_TIME = "KEY_LAST_REPORT_INSTALLED_APPS_TIME";
    private static final String KEY_LAST_REPORT_SEC_DETAIL_PAGE_VIEW_TIME = "KEY_LAST_REPORT_SEC_DETAIL_PAGE_VIEW_TIME";

    public static final String KEY_MORE_OPERATION_GUIDE_CLICKED = "key_more_operation_guide_clicked";

    public static final String KEY_OPERATION_GUIDE_DISPLAYED_TIMES = "key_operation_guide_displayed_times";

    public static final String KEY_VIDEO_TAB_GUIDE_CLICKED = "key_video_tab_guide_clicked";

    public static final String KEY_HUAWEI_DEVICE_TOKEN_ENABLED = "key_huawei_device_token_enabled";

    public static final String KEY_DASHBOARD_INDEX = "key_dash_board_index";

    public static SharedPreferences getSharedPreferences() {
        return DengtaApplication.getApplication().getSharedPreferences(DATA_PREF, Context.MODE_MULTI_PROCESS);
    }

    public static boolean setDashBoardIndex(int index){
        return getSharedPreferences().edit().putInt(KEY_DASHBOARD_INDEX, index).commit();
    }

    public static int getDashBoardIndex(){
        return getSharedPreferences().getInt(KEY_DASHBOARD_INDEX, 0);
    }

    public static boolean isHongkongDelayPromptDisplayed() {
        return  getSharedPreferences().getBoolean(KEY_HONGKONG_DELAY_PROMPT_DISPLAYED, false);
    }

    public static void setHongkongDelayPromptDisplayed(final boolean displayed) {
        getSharedPreferences().edit().putBoolean(KEY_HONGKONG_DELAY_PROMPT_DISPLAYED, displayed).commit();
    }

    public static boolean isUSADelayPromptDisplayed() {
        return  getSharedPreferences().getBoolean(KEY_USA_DELAY_PROMPT_DISPLAYED, false);
    }

    public static void setUSADelayPromptDisplayed(final boolean displayed) {
        getSharedPreferences().edit().putBoolean(KEY_USA_DELAY_PROMPT_DISPLAYED, displayed).commit();
    }

    public static boolean isLineChartGroupEntranceClicked() {
        return getSharedPreferences().getBoolean(KEY_LINE_CHART_GROUP_ENTRANCE_CLICKED, false);
    }

    public static boolean isMoreOperationGuideClicked() {
        return getSharedPreferences().getBoolean(KEY_MORE_OPERATION_GUIDE_CLICKED, false);
    }

    public static void setLineChartGroupEntranceClicked(final boolean clicked) {
        getSharedPreferences().edit().putBoolean(KEY_LINE_CHART_GROUP_ENTRANCE_CLICKED, clicked).commit();
    }

    public static boolean isVideoTabGuideClicked() {
        return getSharedPreferences().getBoolean(KEY_VIDEO_TAB_GUIDE_CLICKED, false);
    }

    public static boolean isLineChartGroupEntranceExpanded() {
        return getSharedPreferences().getBoolean(KEY_LINE_CHART_GROUP_ENTRANCE_EXPANDED, false);
    }

    public static void setLineChartGroupEntranceExpanded(final boolean expanded) {
        getSharedPreferences().edit().putBoolean(KEY_LINE_CHART_GROUP_ENTRANCE_EXPANDED, expanded).commit();
    }

    public static boolean isStockDetailUserGuideDisplayed() {
        return getSharedPreferences().getBoolean(KEY_STOCK_DETAIL_USER_GUIDE_DISPLAYED, false);
    }

    public static void setStockDetailUserGuideDisplayed(final boolean displayed) {
        getSharedPreferences().edit().putBoolean(KEY_STOCK_DETAIL_USER_GUIDE_DISPLAYED, displayed).commit();
    }

    public static void setMoreOperationGuideClicked(final boolean clicked) {
        getSharedPreferences().edit().putBoolean(KEY_MORE_OPERATION_GUIDE_CLICKED, clicked).commit();
    }

    public static int getOperationGuideDisplayedTimes() {
        return getSharedPreferences().getInt(KEY_OPERATION_GUIDE_DISPLAYED_TIMES, 0);
    }

    public static void setOperationGuideDisplayedTimes(final int times) {
        getSharedPreferences().edit().putInt(KEY_OPERATION_GUIDE_DISPLAYED_TIMES, times).commit();
    }

    public static void setVideoTabGuideClicked(final boolean clicked) {
        getSharedPreferences().edit().putBoolean(KEY_VIDEO_TAB_GUIDE_CLICKED, clicked).commit();
    }

    public static String getPlateTabTag() {
        return getSharedPreferences().getString(KEY_PLATE_TAB_TAG, "");
    }

    public static void setPlateTabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_PLATE_TAB_TAG, tag).commit();
    }

    public static String getIndexTabTag() {
        return getSharedPreferences().getString(KEY_INDEX_TAB_TAG, "");
    }

    public static void setIndexTabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_INDEX_TAB_TAG, tag).commit();
    }

    public static String getStockTabTag() {
        return getSharedPreferences().getString(KEY_STOCK_TAB_TAG, FragmentFactory.NEWS);
    }

    public static void setStockTabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_STOCK_TAB_TAG, tag).commit();
    }

    public static String getHongkongStockTabTag() {
        return getSharedPreferences().getString(KEY_HONGKONG_STOCK_TAB_TAG, "");
    }

    public static void setHongkongStockTabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_HONGKONG_STOCK_TAB_TAG, tag).commit();
    }

    public static String getUsaStockTabTag() {
        return getSharedPreferences().getString(KEY_USA_STOCK_TAB_TAG, "");
    }

    public static void setUsaStockTabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_USA_STOCK_TAB_TAG, tag).commit();
    }

    public static String getFundTabTag() {
        return getSharedPreferences().getString(KEY_FUND_TAB_TAG, "");
    }

    public static void setFundTabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_FUND_TAB_TAG, tag).commit();
    }

    public static String getDengtaATabTag() {
        return getSharedPreferences().getString(KEY_DENGTA_A_TAB_TAG, "");
    }

    public static void setDengtaATabTag(final String tag) {
        getSharedPreferences().edit().putString(KEY_DENGTA_A_TAB_TAG, tag).commit();
    }

    public static int getLastLiveMsgTime() {
        return getSharedPreferences().getInt(KEY_LAST_LIVE_MSG_TIME, -1);
    }

    public static void setLastLiveMsgTime(final int time) {
        getSharedPreferences().edit().putInt(KEY_LAST_LIVE_MSG_TIME, time).commit();
    }

    public static String getHotfixPatchMd5() {
        return getSharedPreferences().getString(KEY_HOTFIX_PATCH_MD5, "");
    }

    public static void setHotfixPatchMd5(final String md5) {
        getSharedPreferences().edit().putString(KEY_HOTFIX_PATCH_MD5, md5).commit();
    }

    public static int getLastReportInstalledAppsTime() {
        return getSharedPreferences().getInt(KEY_LAST_REPORT_INSTALLED_APPS_TIME, -1);
    }

    public static void setLastReportInstalledAppsTime(final int time) {
        getSharedPreferences().edit().putInt(KEY_LAST_REPORT_INSTALLED_APPS_TIME, time).commit();
    }

    public static int getLastReportSecDetailPVTime() {
        return getSharedPreferences().getInt(KEY_LAST_REPORT_SEC_DETAIL_PAGE_VIEW_TIME, -1);
    }

    public static void setLastReportSecDetailPVTime(final int time) {
        getSharedPreferences().edit().putInt(KEY_LAST_REPORT_SEC_DETAIL_PAGE_VIEW_TIME, time).commit();
    }

    public static int getTimeLineCompareType() {
        return  getSharedPreferences().getInt(SettingConst.KEY_TIMELINE_COMPARE_TYPE, SettingConst.TIMELINE_COMPARE_TYPE_NONE);
    }

    public static void setTimeLineCompareType(final int type) {
        getSharedPreferences().edit().putInt(SettingConst.KEY_TIMELINE_COMPARE_TYPE, type).commit();
    }

    public static String getTimeLineCompareSecCode() {
        return  getSharedPreferences().getString(SettingConst.KEY_TIMELINE_COMPARE_SEC_CODE, "");
    }

    public static void setTimeLineCompareSecCode(final String secCode) {
        getSharedPreferences().edit().putString(SettingConst.KEY_TIMELINE_COMPARE_SEC_CODE, secCode).commit();
    }
}
