package com.sscf.investment.setting;

import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.SettingPref;

/**
 * @author davidwei
 *
 */
public final class DengtaSettingPref extends SettingPref {

	public static final String KEY_SETTING_LINE_TYPE = "setting_line_type_index";
	public static final int DEFAULT_LINE_TYPE_INDEX = 0;

	private static final String KEY_BIND_CELLPHONE_SHOWN = "key_bind_cellphone_shown";

	public static int getRefreshFrequencyMobileNetSeconds() {
		return getInt(SettingConst.KEY_REFRESH_FREQUENCY_MOBILE_NET_SECONDS, SettingConst.DEFAULT_REFRESH_FREQUENCY_MOBILE_NET_SECONDS);
	}

	public static int getRefreshFrequencyWifiSeconds() {
		return getInt(SettingConst.KEY_REFRESH_FREQUENCY_WIFI_SECONDS, SettingConst.DEFAULT_REFRESH_FREQUENCY_WIFI_SECONDS);
	}

	public static int getRefreshDelaySenconds() {
		int refreshDelay = DEFAULT_DELAY_TIME;
		int networkType = NetUtil.getNetworkType(DengtaApplication.getApplication());
		if (networkType != NetUtil.NETWORK_TYPE_NONE) {
			if (networkType == NetUtil.NETWORK_TYPE_WIFI) {
				refreshDelay = getRefreshFrequencyWifiSeconds();
			} else {
				refreshDelay = getRefreshFrequencyMobileNetSeconds();
			}
		}

		return refreshDelay;
	}

	public static int getLineTypeIndex() {
		return getInt(KEY_SETTING_LINE_TYPE, DEFAULT_LINE_TYPE_INDEX);
	}

	public static void setLineTypeIndex(int index) {
		putInt(KEY_SETTING_LINE_TYPE, index);
	}

	public static boolean isLiveMsgEnabled() {
		return getIBoolean(SettingConst.KEY_SETTING_LIVE_SWITCH, true);
	}

	public static boolean isStockLiveMsgEnabled() {
		return isLiveMsgEnabled() && getIBoolean(SettingConst.KEY_STOCK_LIVE_SWITCH, true);
	}

	public static boolean isMainBoardLiveMsgEnabled() {
		return isLiveMsgEnabled() && getIBoolean(SettingConst.KEY_SETTING_MAIN_BOARD_LIVE_SWITCH, true);
	}

	public static boolean isPortfolioLiveMsgEnabled() {
		return isLiveMsgEnabled() && getIBoolean(SettingConst.KEY_SETTING_PORTFOLIO_LIVE_SWITCH, true);
	}

	public static boolean isPushMessageEnabled() {
		return getIBoolean(SettingConst.KEY_SETTING_PUSH_SWITCH, true);
	}

	public static boolean isPushImportantNewsEnabled() {
		return isPushMessageEnabled() && getIBoolean(SettingConst.KEY_SETTING_PUSH_IMPORTANT_NEWS_SWITCH, true);
	}

	public static boolean isPushNewSharesEnabled() {
		return isPushMessageEnabled() && getIBoolean(SettingConst.KEY_SETTING_PUSH_NEW_SHARES_SWITCH, true);
	}


	public static int loadKLineIndicatorType0() {
		return DengtaSettingPref.getInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_0, SettingConst.DEFAULT_K_LINE_INDICATOR_TYPE);
	}

	public static int loadKLineIndicatorType1() {
		return DengtaSettingPref.getInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_1, SettingConst.K_LINE_INDICATOR_BREAK);
	}

	public static int loadKLineIndicatorType2() {
		return DengtaSettingPref.getInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_2, SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW);
	}

	public static int loadKLineIndicatorType3() {
		return DengtaSettingPref.getInt(SettingConst.KEY_SETTING_INDICATORS_TYPE_3, SettingConst.K_LINE_INDICATOR_MACD);
	}

	public static int loadTimeLineIndicatorType0() {
		return DengtaSettingPref.getInt(SettingConst.KEY_SETTING_TIME_INDICATORS_TYPE_INDEX_0, SettingConst.K_LINE_INDICATOR_KDJ);
	}

	public static int loadTimeLineIndicatorType1() {
		return DengtaSettingPref.getInt(SettingConst.KEY_SETTING_TIME_INDICATORS_TYPE_INDEX_1, SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ);
	}

	public static boolean isBindCellphoneShown() {
		return getIBoolean(KEY_BIND_CELLPHONE_SHOWN, false);
	}

	public static void setBindCellphoneShown(final boolean shown) {
		putIBoolean(KEY_BIND_CELLPHONE_SHOWN, shown);
	}
}
