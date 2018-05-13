package com.sscf.investment.setting.manager;

import BEC.*;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dengtacj.component.managers.IRedDotManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AppConfigRequestManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.utils.DengtaConst;
import java.util.ArrayList;

public final class RedDotManager implements DataSourceProxy.IRequestCallback, IRedDotManager {
    public static final String TAG = RedDotManager.class.getSimpleName();

//    private static final String KEY_REMIND_UNREAD = "key_remind_unread";
    private static final String KEY_UPGRADE = "key_upgrade";
    private static final String KEY_SWITCH_THEME = "key_switch_theme";
    private static final String KEY_NEW_ACTIVITY = "key_new_activity";
    private static final String KEY_SUBSCRIPTION_STATE = "key_subscription_state";
    private static final String KEY_INTELLIGENT_ANSWER_RED_DOT = "key_intelligent_answer_red_dot";
    private static final String KEY_PORTFOLIO_LIVE_STATE = "key_portfolio_live_state";
    private static final String KEY_MAIN_BOARD_LIVE_STATE = "key_main_board_live_state";
    private static final String KEY_PRIVILEGE_STATE = "key_privilege_state";
    private static final String KEY_VALUEADDED_SERVICES_STATE = "key_valueadded_services_state";
    private static final String KEY_MESSAGE_CENTER_STATE = "key_message_center_state";
    private static final String KEY_NEVER_ENTER_BONUS = "key_never_enter_bonus";

    /**
     * 设置里活动相关信息
     */
    private DtActivityInfo mActivitiesInfo;

    public DtActivityInfo getActivitiesInfo() {
        return mActivitiesInfo;
    }

    public void requestConfigActivitiesAndOpenAccountInfo() {
        if (mActivitiesInfo == null) {
            AppConfigRequestManager.getConfigActivitiesAndOpenAccountInfoRequest(this);
        }
    }

    public void requestConfigActivitiesAndOpenAccountInfoForce() {
        mActivitiesInfo = null;
        setNewActivityState(false);
        AppConfigRequestManager.getConfigActivitiesAndOpenAccountInfoRequest(this);
    }

    public void requestIntelligentAnswerInfo() {
        AppConfigRequestManager.getIntelligentAnswerInfoRequest(this);
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        if (data == null) {
            return;
        }
        switch (data.getEntityType()) {
            case EntityObject.ET_CONFIG_GET_NEW_ACTIVITIES:
                getActivitiesAndOpenAccountInfoCallback(success, data);
                break;
            case EntityObject.ET_CONFIG_GET_INTELLIGENT_ANSWER:
                getIntelligentAnswerInfoCallback(success, data);
                break;
            default:
                break;
        }
    }

    private void getIntelligentAnswerInfoCallback(final boolean success, final EntityObject data) {
        if (success) {
            final ArrayList<ConfigDetail> configs = ((GetConfigRsp) data.getEntity()).getVList();
            for (ConfigDetail config : configs) {
                if (config.iType == E_CONFIG_TYPE.E_CONFIG_INTELI_INVEST) {
                    final int version = SettingPref.getInt(DengtaConst.KEY_INTELLIGENT_ANSWER_VERSION, 0);
                    if (config.iVersion > version) {
                        SettingPref.putInt(DengtaConst.KEY_INTELLIGENT_ANSWER_VERSION, config.iVersion);
                        setIntelligentAnswerRedDotState(true);
                    }
                    return;
                }
            }
        }
    }

    private void getActivitiesAndOpenAccountInfoCallback(final boolean success, final EntityObject data) {
        boolean newActivityState = false;
        if (success) {
            final ArrayList<ConfigDetail> configs = ((GetConfigRsp) data.getEntity()).getVList();
            for (ConfigDetail config : configs) {
                if (config.iType == E_CONFIG_TYPE.E_CONFIG_NEW_ACTIVITY) {
                    final DtActivityInfo dtActivityInfo = new DtActivityInfo();
                    if (ProtoManager.decode(dtActivityInfo, config.vData)) {
                        mActivitiesInfo = dtActivityInfo;
                        final int version = SettingPref.getInt(DengtaConst.KEY_NEW_ACTIVITY_VERSION, 0);
                        if (dtActivityInfo.iForceRedDot == 1 || dtActivityInfo.iVersion > version) {
                            newActivityState = true;
                        }
                    }
                }
            }
        }
        setNewActivityState(newActivityState);
    }

    /**
     * 保存新活动的版本号，判断是否已经打开过该版本的活动
     */
    public void saveNewActivityVersion() {
        final DtActivityInfo newActivityInfo = mActivitiesInfo;
        if (newActivityInfo != null) {
            SettingPref.putInt(DengtaConst.KEY_NEW_ACTIVITY_VERSION, newActivityInfo.iVersion);
            setNewActivityState(false);
        }
    }

    public boolean getUpgradeState() {
        return getState(KEY_UPGRADE);
    }

    public void setUpgradeState(final boolean state) {
        putState(KEY_UPGRADE, state);
    }

    public boolean getAboutState() {
        return getUpgradeState();
    }

    public boolean getNewFeatureState() {
        return getSwitchThemeState();
    }

    public void setSwitchThemeState(final boolean state) {
        putState(KEY_SWITCH_THEME, state);
    }

    public boolean getSwitchThemeState() {
        return getState(KEY_SWITCH_THEME);
    }

    public void setNewActivityState(final boolean state) {
        putState(KEY_NEW_ACTIVITY, state);
    }

    public boolean getNewActivityState() {
        return getState(KEY_NEW_ACTIVITY);
    }


    public void setPrivilegeState(final boolean state) {
        putState(KEY_PRIVILEGE_STATE, state);
    }

    public boolean getPrivilegeState() {
        return getState(KEY_PRIVILEGE_STATE);
    }

    public void setValueaddedServicesState(final boolean state) {
        putState(KEY_VALUEADDED_SERVICES_STATE, state);
    }

    public boolean getValueaddedServicesState() {
        return getState(KEY_VALUEADDED_SERVICES_STATE, true);
    }

    public void setInputToolsButtonState(final boolean state) {
//        SettingPref.putIBoolean(KEY_INTELLIGENT_ANSWER_INPUT_TOOLS_BUTTON, state);
    }

    public boolean getInputToolsButtonState() {
//        return getState(KEY_INTELLIGENT_ANSWER_INPUT_TOOLS_BUTTON, true);
        return false;
    }

    public boolean getMoreOperationState() {
        return false;
    }

    public void setIntelligentAnswerRedDotState(final boolean state) {
        putState(KEY_INTELLIGENT_ANSWER_RED_DOT, state);
    }

    public boolean getIntelligentAnswerRedDotState() {
        return getState(KEY_INTELLIGENT_ANSWER_RED_DOT);
    }

    public void setMessageCenterState(final boolean state) {
        putState(KEY_MESSAGE_CENTER_STATE, state);
    }

    public boolean getMessageCenterState() {
        return getState(KEY_MESSAGE_CENTER_STATE);
    }

    public boolean getSettingState() {
        return getAboutState() || getNewFeatureState();
    }

    public boolean getMineState() {
        return getSettingState() ||
                getNewActivityState() ||
                getPrivilegeState() ||
                getMessageCenterState() ||
                getNeverEnterBonus() ||
                getValueaddedServicesState();
    }

    public void setSubscriptionState(final boolean state) {
        putState(KEY_SUBSCRIPTION_STATE, state);
    }

    public boolean getSubscriptionState() {
        return getState(KEY_SUBSCRIPTION_STATE);
    }

    public void setPortfolioLiveState(final boolean state) {
        putState(KEY_PORTFOLIO_LIVE_STATE, state);
    }

    public boolean getPortfolioLiveState() {
        return getState(KEY_PORTFOLIO_LIVE_STATE);
    }

    public void setMainBoardLiveState(final boolean state) {
        putState(KEY_MAIN_BOARD_LIVE_STATE, state);
    }

    public boolean getMainBoardLiveState() {
        return getState(KEY_MAIN_BOARD_LIVE_STATE);
    }

    public boolean getStockPickState() {
        return getSubscriptionState();
    }

    public boolean getMarketState() {
        return false;
    }

    public void setNeverEnterBonus(final boolean state) {
        putState(KEY_NEVER_ENTER_BONUS, state);
    }

    public boolean getNeverEnterBonus() {
        return getState(KEY_NEVER_ENTER_BONUS);
    }

    private boolean getState(final String key) {
        return getState(key, false);
    }

    private boolean getState(final String key, final boolean defaultState) {
        final boolean state = SettingPref.getIBoolean(key, defaultState);
        if (defaultState) {
            if (SettingPref.getIBoolean(key, false) != state) { // 第一次调用时，没有默认值的时候，设置为true
                SettingPref.putIBoolean(key, true);
            }
        }
        return state;
    }

    private void putState(final String key, final boolean state) {
        if (state != getState(key)) {
            SettingPref.putIBoolean(key, state);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                    .sendBroadcast(new Intent(SettingConst.ACTION_RED_DOT_STATE_CHANGED));
        }
    }
}