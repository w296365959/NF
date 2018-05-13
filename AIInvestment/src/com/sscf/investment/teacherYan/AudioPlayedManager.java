package com.sscf.investment.teacherYan;

import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.SettingConst;

import BEC.WxWalkRecord;

/**
 * Created by LEN on 2018/5/4.
 */

public class AudioPlayedManager {

    public static boolean isPlayed(WxWalkRecord item) {
        String[] playedAudios = getPlayedList();
        for (int i = 0 ; i < playedAudios.length ; i++) {
            if (playedAudios[i].equals(String.valueOf(item.getIID()))) {
                return true;
            }
        }
        return false;
    }

    public static void addPlayedList(WxWalkRecord item) {
        if (!isPlayed(item)) {
            String playedList = SettingPref.getString(SettingConst.K_AUDIO_PLAYED, "");
            playedList += "|" + item.getIID();
            SettingPref.putString(SettingConst.K_AUDIO_PLAYED, playedList);
        }
    }

    public static String[] getPlayedList() {
        String playedList = SettingPref.getString(SettingConst.K_AUDIO_PLAYED, "");
        return playedList.split("\\|");
    }
}
