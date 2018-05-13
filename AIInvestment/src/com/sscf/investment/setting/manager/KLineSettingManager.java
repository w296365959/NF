package com.sscf.investment.setting.manager;

import android.text.TextUtils;

import com.dengtacj.json.JSON;
import com.dengtacj.json.JSONException;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.model.IndicatorConfigure;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.model.KlineSettingConst;

import java.util.ArrayList;
import java.util.List;

import BEC.QueryFavorIndReq;
import BEC.QueryFavorIndRsp;
import BEC.SaveFavorIndReq;

/**
 * Created by LEN on 2018/4/4.
 */

public class KLineSettingManager implements DataSourceProxy.IRequestCallback {

    public static final String TAG = KLineSettingManager.class.getSimpleName();

    private KLineSettingConfigure mKlineSettingConfigure;

    public KLineSettingManager(){

    }

    public void init(){
        initDefaultValue();
        requestKlineSettingInfo();
    }

    private void initDefaultValue(){
        try {
            DtLog.e(TAG, KlineSettingConst.DEFAULT_KLINE_CONFIGURE);
            String tjson = SettingPref.getString(SettingConst.KEY_KLINE_CONFIGURE, KlineSettingConst.DEFAULT_KLINE_CONFIGURE);
            DtLog.e(TAG, "local data :" + tjson);
            mKlineSettingConfigure = JSON.parseObject(tjson, KLineSettingConfigure.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestKlineSettingInfo(){
        DtLog.e(TAG, "requestKlineSettingInfo");
        QueryFavorIndReq req = new QueryFavorIndReq();
        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_FAVOR_CANDICATOR_QUERY, req, this);
    }

    public void saveKlineSettingInfo(String info){
        DtLog.e(TAG, info);
        SaveFavorIndReq req = new SaveFavorIndReq();
        req.sIndiData = info;
        req.stUserInfo = SDKManager.getInstance().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_FAVOR_CANDICATOR_SAVE, req, this);
    }

    public IndicatorConfigure getConfigureByName(String name){
        for (int i = 0 ; i < mKlineSettingConfigure.allIndicators.size() ; i++){
            if (mKlineSettingConfigure.allIndicators.get(i).name.equals(name)){
                return mKlineSettingConfigure.allIndicators.get(i);
            }
        }
        return null;
    }

    public KLineSettingConfigure getKlineSettingConfigure(){
        return mKlineSettingConfigure;
    }

    public void saveConfigure(String json){
        if (TextUtils.isEmpty(json)){
            return;
        }
        DtLog.e(TAG, "saveConfigure : " + json);
        SettingPref.putString(SettingConst.KEY_KLINE_CONFIGURE, json);
        try {
            this.mKlineSettingConfigure = JSON.parseObject(json, KLineSettingConfigure.class);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void saveConfigure(){

        String strJson = "";
        try {
            strJson = JSON.toJSONString(mKlineSettingConfigure);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SettingPref.putString(SettingConst.KEY_KLINE_CONFIGURE, strJson);
        if (!TextUtils.isEmpty(strJson)){
            saveKlineSettingInfo(strJson);
        }
        DtLog.e(TAG, "save local configure");
    }

    public boolean isCandicatorHasParams(String candicator){
        if (candicator.equals("主力资金") || candicator.equals("OBV")||
                candicator.equals("横盘突破")){
            return false;
        }
        return true;
    }

    public ArrayList<String> getUndisplayCandicators(){
        ArrayList<String> unDisplayCandicators = new ArrayList<>();
        List<IndicatorConfigure> allIndicators = mKlineSettingConfigure.allIndicators;
        String[] showIndicators = mKlineSettingConfigure.showIndicators.split(",");
        for (int i = 0 ; i < allIndicators.size() ; i++){
            if (!isDisplay(allIndicators.get(i).name, showIndicators)){
                unDisplayCandicators.add(allIndicators.get(i).name);
                continue;
            }
        }
        return unDisplayCandicators;
    }

    public void addDisplayCandicator(ArrayList<String> candicators){
        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < candicators.size() ; i++){
            sb.append(candicators.get(i) + ",");
        }
        mKlineSettingConfigure.showIndicators = sb.toString() + mKlineSettingConfigure.showIndicators;
        saveConfigure();
    }

    public int getNextType(String type){
        String[] displayTypes = mKlineSettingConfigure.showIndicators.split(",");
        for (int i = 0 ; i < displayTypes.length ; i++){
            if (type.equals(displayTypes[i])){
                if (i == displayTypes.length - 1){
                    return getDisplayIndex(displayTypes[0]);
                }else{
                    return getDisplayIndex(displayTypes[i + 1]);
                }

            }
        }
        return getDisplayIndex(displayTypes[0]);
    }

    public  int getNextType(int index){
        return getNextType(getDisplayName(index));
    }

    public boolean isIndicatorDisplay(int indicator){
        return mKlineSettingConfigure.showIndicators.contains(getDisplayName(indicator));
    }

    public int getFirstDisplay(){
        return getDisplayIndex(mKlineSettingConfigure.showIndicators.split(",")[0]);
    }

    private boolean isDisplay(String candicator, String[] showCandicators){
        for (int i = 0 ; i < showCandicators.length ; i++){
            if (candicator.equals(showCandicators[i])){
                return true;
            }
        }
        return false;
    }

    public static String getDisplayName(int index){
        if (index == SettingConst.K_LINE_INDICATOR_VOLUME){
            return "成交量";
        }

        if (index == SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW){
            return "主力资金";
        }

        if (index == SettingConst.K_LINE_INDICATOR_MACD){
            return "MACD";
        }

        if (index == SettingConst.K_LINE_INDICATOR_KDJ){
            return "KDJ";
        }

        if (index == SettingConst.K_LINE_INDICATOR_RSI){
            return "RSI";
        }

        if (index == SettingConst.K_LINE_INDICATOR_BOLL){
            return "BOLL";
        }

        if (index == SettingConst.K_LINE_INDICATOR_DMI){
            return "DMI";
        }

        if (index == SettingConst.K_LINE_INDICATOR_CCI){
            return "CCI";
        }

        if (index == SettingConst.K_LINE_INDICATOR_ENE){
            return "ENE";
        }

        if (index == SettingConst.K_LINE_INDICATOR_DMA){
            return "DMA";
        }

        if (index == SettingConst.K_LINE_INDICATOR_EXPMA){
            return "EXPMA";
        }

        if (index == SettingConst.K_LINE_INDICATOR_VR){
            return "VR";
        }

        if (index == SettingConst.K_LINE_INDICATOR_BBI){
            return "BBI";
        }

        if (index == SettingConst.K_LINE_INDICATOR_ENE){
            return "ENE";
        }

        if (index == SettingConst.K_LINE_INDICATOR_OBV){
            return "OBV";
        }

        if (index == SettingConst.K_LINE_INDICATOR_BIAS){
            return "BIAS";
        }

        if (index == SettingConst.K_LINE_INDICATOR_WR){
            return "WR";
        }

        if (index == SettingConst.K_LINE_INDICATOR_BREAK){
            return "横盘突破";
        }

        return "";
    }

    public static int getDisplayIndex(String candicatorName){
        if (candicatorName.equals("成交量"))
            return SettingConst.K_LINE_INDICATOR_VOLUME;

        if (candicatorName.equals("主力资金"))
            return SettingConst.K_LINE_INDICATOR_CAPITAL_FLOW;

        if (candicatorName.equals("MACD"))
            return SettingConst.K_LINE_INDICATOR_MACD;

        if (candicatorName.equals("KDJ"))
            return SettingConst.K_LINE_INDICATOR_KDJ;

        if (candicatorName.equals("RSI"))
            return SettingConst.K_LINE_INDICATOR_RSI;

        if (candicatorName.equals("BOLL"))
            return SettingConst.K_LINE_INDICATOR_BOLL;

        if (candicatorName.equals("DMI"))
            return SettingConst.K_LINE_INDICATOR_DMI;

        if (candicatorName.equals("CCI"))
            return SettingConst.K_LINE_INDICATOR_CCI;

        if (candicatorName.equals("ENE"))
            return SettingConst.K_LINE_INDICATOR_ENE;

        if (candicatorName.equals("DMA"))
            return SettingConst.K_LINE_INDICATOR_DMA;

        if (candicatorName.equals("EXPMA"))
            return SettingConst.K_LINE_INDICATOR_EXPMA;

        if (candicatorName.equals("VR"))
            return SettingConst.K_LINE_INDICATOR_VR;

        if (candicatorName.equals("BBI"))
            return SettingConst.K_LINE_INDICATOR_BBI;

        if (candicatorName.equals("ENE"))
            return SettingConst.K_LINE_INDICATOR_ENE;

        if (candicatorName.equals("OBV"))
            return SettingConst.K_LINE_INDICATOR_OBV;

        if (candicatorName.equals("BIAS"))
            return SettingConst.K_LINE_INDICATOR_BIAS;

        if (candicatorName.equals("WR"))
            return SettingConst.K_LINE_INDICATOR_WR;

        if (candicatorName.equals("横盘突破"))
            return SettingConst.K_LINE_INDICATOR_BREAK;

        return SettingConst.K_LINE_INDICATOR_VOLUME;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success || data.getEntity() == null ) {
            return;
        }

        switch (data.getEntityType()) {
            case EntityObject.ET_FAVOR_CANDICATOR_QUERY:
                QueryFavorIndRsp req = (QueryFavorIndRsp) data.getEntity();
                saveConfigure(req.getSIndiData());
                break;
            case EntityObject.ET_FAVOR_CANDICATOR_SAVE:
                DtLog.e(TAG, "saveFavor return");
                break;
            default:
                break;
        }
    }
}
