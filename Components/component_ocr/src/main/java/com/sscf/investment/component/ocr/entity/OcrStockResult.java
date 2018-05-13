package com.sscf.investment.component.ocr.entity;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yorkeehuang on 2017/5/24.
 */

public class OcrStockResult extends OcrSuccessResult {

    private ArrayList<Stock> mStockList;

    private boolean isParsed = false;

    public OcrStockResult(String rsp) {
        super(rsp);
    }

    public ArrayList<Stock> getStockList() {
        if(!isParsed) {
            mStockList = parseStockList();
        }
        return mStockList;
    }

    private ArrayList<Stock> parseStockList() {
        ArrayList<Stock> stockList = null;
        if(!TextUtils.isEmpty(rsp)) {
            JSONObject jsonObject = null;
            if(!TextUtils.isEmpty(rsp)) {
                try {
                    jsonObject = new JSONObject(rsp);
                    JSONArray array = jsonObject.getJSONArray("root");
                    int size=array.length();
                    stockList = new ArrayList<>(size);
                    for(int i=0; i<size; i++) {
                        JSONObject jsonObj = (JSONObject) array.get(i);
                        String dtCode = jsonObj.optString("dtcode", "");
                        String secName = jsonObj.optString("secname", "");
                        if(!TextUtils.isEmpty(dtCode) && !TextUtils.isEmpty(secName)) {
                            stockList.add(new Stock(dtCode, secName));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        isParsed = true;
        return stockList;
    }
}
