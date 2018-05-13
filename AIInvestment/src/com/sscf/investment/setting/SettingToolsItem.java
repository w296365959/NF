package com.sscf.investment.setting;

import android.content.Context;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;

/**
 * Created by davidwei on 2017/04/01
 * 工具区的小工具item
 */
public final class SettingToolsItem extends ToolsItem {
    public static ToolsItem createSettingsMarginTrackingItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_margin_vip : R.drawable.settings_margin_none_vip;
        item.textId = R.string.margin_tracking;
        return item;
    }

    public static ToolsItem createDirectionAddNuggets(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_direction_add_nuggets_vip : R.drawable.settings_direction_add_nuggets_none_vip;
        item.textId = R.string.direction_add_nuggets;
        return item;
    }

    public static ToolsItem createSettingsCYQItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_cyq_vip : R.drawable.settings_cyq_none_vip;
        item.textId = R.string.setting_cyq;
        return item;
    }

    public static ToolsItem createSettingsBSSignalItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_bs_signal_vip : R.drawable.settings_bs_signal_none_vip;
        item.textId = R.string.operation_dk;
        return item;
    }

    public static ToolsItem createSettingsSimilarKLineItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_similar_k_line_vip : R.drawable.settings_similar_k_line_none_vip;
        item.textId = R.string.setting_similar_k_line;
        return item;
    }

    public static ToolsItem createSettingsSecHistoryItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_history_vip : R.drawable.settings_history_none_vip;
        item.textId = R.string.setting_sec_history;
        return item;
    }

    public static ToolsItem createSettingsIntelligentDiagnosisItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_intelligent_diagnosis_vip : R.drawable.settings_intelligent_diagnosis_none_vip;
        item.textId = R.string.setting_intelligent_diagnosis;
        return item;
    }

    public static ToolsItem createSettingsLiveItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_my_live_vip : R.drawable.settings_my_live_none_vip;
        item.textId = R.string.setting_my_live;
        return item;
    }

    public static ToolsItem createSettingsIntelligentAnswerSchoolItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_intelligent_answer_help_vip : R.drawable.settings_intelligent_answer_help_none_vip;
        item.textId = R.string.intelligent_answer_school;
        return item;
    }

    public static ToolsItem createSettingsPlateItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_tools_icon_plate_vip : R.drawable.settings_tools_icon_plate_none_vip;
        item.textId = R.string.setting_plate;
        return item;
    }

    public static ToolsItem createSettingsPrivatizationTrackingItem(boolean isMember) {
        final ToolsItem item = new SettingToolsItem();
        item.drawableId = isMember ? R.drawable.settings_privatization_tracking_vip : R.drawable.settings_privatization_tracking_none_vip;
        item.textId = R.string.setting_privatization_tracking;
        return item;
    }

    public void click(final Context context) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (textId) {
            case R.string.margin_tracking:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_MARGIN_TRACKING);
                WebBeaconJump.showMarginTracking(context);
                break;
            case R.string.direction_add_nuggets:
                StatisticsUtil.reportAction(StatisticsConst.A_ME_SETBY_JUEJIN_CLICK);
                WebBeaconJump.showDirectionAddListList(context);
                break;
            case R.string.setting_cyq:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_CYQ);
                WebBeaconJump.showCYQ(context);
                break;
            case R.string.operation_dk:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_BS_SIGNAL);
                WebBeaconJump.showBS(context);
                break;
            case R.string.setting_similar_k_line:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_SIMILAR_K_LINE_DISPLAY);
                WebBeaconJump.showSimilarKLine(context);
                break;
            case R.string.setting_sec_history:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_SEC_HISTORY_DISPLAY);
                WebBeaconJump.showSecHistory(context);
                break;
            case R.string.setting_intelligent_diagnosis:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_DIAGNOSIS);
                WebBeaconJump.showIntelligentDiagnosisSearch(context);
                break;
            case R.string.setting_my_live:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_LIVE);
                WebBeaconJump.showDtLive(context);
                break;
            case R.string.intelligent_answer_school:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_INTELLIGENT_ANSWER_SCHOOL);
                WebBeaconJump.showIntelligentAnswerSchool(context);
                break;
            case R.string.setting_plate:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_PLATE);
                WebBeaconJump.showIndustryPlateList(context);
                break;
            case R.string.setting_privatization_tracking:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_PRIVATIZATION_TRACKING);
                WebBeaconJump.showPrivatizationTrackingList(context);
                break;
            default:
                break;
        }
    }
}
