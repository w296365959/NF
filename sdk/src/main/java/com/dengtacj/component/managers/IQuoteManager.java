package com.dengtacj.component.managers;

import com.dengtacj.component.callback.OnGetDataCallback;
import java.util.ArrayList;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017-09-30
 */
public interface IQuoteManager {
    void requestQuote(String dtSecCode, OnGetDataCallback<SecQuote> callback);
    void requestQuote(ArrayList<String> dtSecCodeList, OnGetDataCallback<ArrayList<SecQuote>> callback);
    void requestSimpleQuote(String dtSecCode, OnGetDataCallback<SecSimpleQuote> callback);
    void requestSimpleQuote(ArrayList<String> dtSecCodeList, OnGetDataCallback<ArrayList<SecSimpleQuote>> callback);
}
