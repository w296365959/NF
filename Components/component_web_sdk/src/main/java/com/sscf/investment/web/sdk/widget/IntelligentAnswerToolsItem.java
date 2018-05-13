package com.sscf.investment.web.sdk.widget;

import android.content.Context;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.web.sdk.R;
import com.dengtacj.component.router.WebBeaconJump;

/**
 * Created by davidwei on 2017-09-07.
 */

public class IntelligentAnswerToolsItem extends ToolsItem {

    public static ToolsItem createIntelligentAnswerSchool() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.intelligent_answer_school;
        item.textId = R.string.intelligent_answer_school;
        return item;
    }

    public static ToolsItem createIntelligentDiagnosisItem() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.setting_intelligent_diagnosis;
        item.textId = R.string.setting_intelligent_diagnosis;
        return item;
    }

    public static ToolsItem createConditionSelection() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.condition_selection;
        item.textId = R.string.condition_selection;
        return item;
    }

    public static ToolsItem createBSSignal() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.setting_bs_signal;
        item.textId = R.string.operation_dk;
        return item;
    }

    public static ToolsItem createCYQItem() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.setting_cyq;
        item.textId = R.string.setting_cyq;
        return item;
    }

    public static ToolsItem createSimilarKLineItem() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.setting_similar_k_line_icon;
        item.textId = R.string.setting_similar_k_line;
        return item;
    }

    public static ToolsItem createSecHistoryItem() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.setting_sec_history_icon;
        item.textId = R.string.setting_sec_history;
        return item;
    }

    public static ToolsItem createIntelligentAnswerHelp() {
        final IntelligentAnswerToolsItem item = new IntelligentAnswerToolsItem();
        item.drawableId = R.drawable.intelligent_answer_help;
        item.textId = R.string.intelligent_answer_help;
        return item;
    }

    @Override
    public void click(Context context) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        if (textId == R.string.intelligent_answer_school) {
            StatisticsUtil.reportAction(StatisticsConst.INTELLIGENT_ANSWER_CLICK_INTELLIGENT_ANSWER_SCHOOL);
            WebBeaconJump.showIntelligentAnswerSchool(context);
        } else if (textId == R.string.setting_intelligent_diagnosis) {
            WebBeaconJump.showIntelligentDiagnosisSearch(context);
        } else if (textId == R.string.condition_selection) {
            WebBeaconJump.showCustomizedStrategy(context);
        } else if (textId == R.string.operation_dk) {
            WebBeaconJump.showBS(context);
        } else if (textId == R.string.setting_cyq) {
            WebBeaconJump.showCYQ(context);
        } else if (textId == R.string.setting_similar_k_line) {
            WebBeaconJump.showSimilarKLine(context);
        } else if (textId == R.string.setting_sec_history) {
            WebBeaconJump.showSecHistory(context);
        } else if (textId == R.string.intelligent_answer_help) {
            WebBeaconJump.showIntelligentAnswerHelp(context);
        }
    }
}
