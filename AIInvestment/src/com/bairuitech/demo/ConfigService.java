package com.bairuitech.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.bairuitech.anychat.AnyChatDefine;

@SuppressLint("WorldWriteableFiles")
public class ConfigService {

    public static ConfigEntity LoadConfig(Context context) {
        ConfigEntity configEntity = new ConfigEntity();
        SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);

        configEntity.name = share.getString("name", "");
        configEntity.password = share.getString("password", "");
        configEntity.IsSaveNameAndPw = share.getString("IsSaveNameAndPw", "").equals("1") ? true : false;

        configEntity.ip = "192.168.1.19";
        configEntity.port = 8906;

        configEntity.configMode = share.getInt("configMode", ConfigEntity.VIDEO_MODE_CUSTOMCONFIG);
        configEntity.resolution_width = 0;
        configEntity.resolution_height = 0;
        configEntity.videoBitrate = share.getInt("videoBitrate", 100 * 1000);
        configEntity.videoFps = share.getInt("videoFps", 10);
        configEntity.videoQuality = share.getInt("videoQuality", ConfigEntity.VIDEO_QUALITY_BEST);
        configEntity.videoPreset = share.getInt("videoPreset", 3);
        configEntity.videoOverlay = share.getInt("videoOverlay", 1);
        configEntity.videorotatemode = share.getInt("VideoRotateMode", 0);
        configEntity.videoCapDriver = share.getInt("VideoCapDriver", AnyChatDefine.VIDEOCAP_DRIVER_JAVA);
        configEntity.fixcolordeviation = share.getInt("FixColorDeviation", 0);
        configEntity.videoShowGPURender = share.getInt("videoShowGPURender", 0);
        configEntity.videoAutoRotation = share.getInt("videoAutoRotation", 1);

        configEntity.enableP2P = share.getInt("enableP2P", 1);
        configEntity.useARMv6Lib = share.getInt("useARMv6Lib", 0);
        configEntity.enableAEC = share.getInt("enableAEC", 1);
        configEntity.useHWCodec = share.getInt("useHWCodec", 0);
        configEntity.videoShowDriver = share.getInt("videoShowDriver", AnyChatDefine.VIDEOSHOW_DRIVER_JAVA);
        configEntity.audioPlayDriver = share.getInt("audioPlayDriver", AnyChatDefine.AUDIOPLAY_DRIVER_JAVA);
        configEntity.audioRecordDriver = share.getInt("audioRecordDriver", AnyChatDefine.AUDIOREC_DRIVER_JAVA);
        return configEntity;
    }

    public static void SaveConfig(Context context, ConfigEntity configEntity) {
        SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
        Editor editor = share.edit();

        editor.putString("name", configEntity.name);
        editor.putString("password", configEntity.password);
        editor.putString("IsSaveNameAndPw", configEntity.IsSaveNameAndPw ? "1" : "0");

        editor.putString("ip", configEntity.ip);
        editor.putInt("port", configEntity.port);

        editor.putInt("configMode", configEntity.configMode);
        editor.putInt("resolution_width", configEntity.resolution_width);
        editor.putInt("resolution_height", configEntity.resolution_height);

        editor.putInt("videoBitrate", configEntity.videoBitrate);
        editor.putInt("videoFps", configEntity.videoFps);
        editor.putInt("videoQuality", configEntity.videoQuality);
        editor.putInt("videoPreset", configEntity.videoPreset);
        editor.putInt("videoOverlay", configEntity.videoOverlay);
        editor.putInt("VideoRotateMode", configEntity.videorotatemode);
        editor.putInt("VideoCapDriver", configEntity.videoCapDriver);
        editor.putInt("FixColorDeviation", configEntity.fixcolordeviation);
        editor.putInt("videoShowGPURender", configEntity.videoShowGPURender);
        editor.putInt("videoAutoRotation", configEntity.videoAutoRotation);

        editor.putInt("enableP2P", configEntity.enableP2P);
        editor.putInt("useARMv6Lib", configEntity.useARMv6Lib);
        editor.putInt("enableAEC", configEntity.enableAEC);
        editor.putInt("useHWCodec", configEntity.useHWCodec);
        editor.putInt("videoShowDriver", configEntity.videoShowDriver);
        editor.putInt("audioPlayDriver", configEntity.audioPlayDriver);
        editor.putInt("audioRecordDriver", configEntity.audioRecordDriver);
        editor.commit();

    }

}
