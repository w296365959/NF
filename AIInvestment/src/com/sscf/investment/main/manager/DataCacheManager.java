package com.sscf.investment.main.manager;

import android.os.AsyncTask;
import com.dengtacj.component.managers.IDataCacheManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import BEC.SecQuote;
import BEC.SecSimpleQuote;
import BEC.WxWalkRecord;

/**
 * Created by liqf on 2016/3/29.
 */
public final class DataCacheManager implements IDataCacheManager {
    private static final String TAG = DataCacheManager.class.getSimpleName();
    private boolean mIsSavingToFile = false; //正在把对象序列化存储到文件时禁止写缓存对象，避免并发修改错误
    private ArrayList<WxWalkRecord> mWalkRecords;

    public DataCacheManager() {
    }

    @Override
    public void setSecSimpleQuote(final SecSimpleQuote quote) {
        if (mIsSavingToFile) return;
        if (quote != null) {
            mSimpleQuoteMap.put(quote.sDtSecCode, quote);
        }
    }

    @Override
    public void setSecSimpleQuotes(final List<SecSimpleQuote> simpleQuotes) {
        if (simpleQuotes == null || simpleQuotes.size() == 0) {
            return;
        }

        ThreadUtils.runOnUiThread(() -> {
            if (mIsSavingToFile) return;
            for (SecSimpleQuote simpleQuote : simpleQuotes) {
                String dtSecCode = simpleQuote.getSDtSecCode();
                mSimpleQuoteMap.put(dtSecCode, simpleQuote);
            }
        });
    }

    @Override
    public SecSimpleQuote getSecSimpleQuote(final String dtSecCode) {
        return mSimpleQuoteMap.get(dtSecCode);
    }

    @Override
    public void setSecQuote(final SecQuote quote) {
        if (mIsSavingToFile) return;
        if (quote != null) {
            mQuoteMap.put(quote.sDtSecCode, quote);
        }
    }

    @Override
    public SecQuote getSecQuote(final String dtSecCode) {
        return mQuoteMap.get(dtSecCode);
    }

    public Map<String, String> getLastSecLiveMsgIdMap() {
        return mLastSecLiveMsgIdMap;
    }

    public Map<String, Boolean> getHasUnreadLiveMsgMap() {
        return mHasUnreadLiveMsgMap;
    }

    public String getLastLiveMsgId(final String dtSecCode) {
        String id = getLastSecLiveMsgIdMap().get(dtSecCode);
        if (id == null) {
            id = "";
        }
        return id;
    }

    public boolean hasUnreadLiveMsg(final String dtSecCode) {
        // 处理SM-A8000的crash java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Boolean
        final Object value = getHasUnreadLiveMsgMap().get(dtSecCode);
        Boolean hasUnread = false;
        if (value != null && value instanceof Boolean) {
            hasUnread = (Boolean) value;
        }
        return hasUnread;
    }

    //write functions
    public void setHasUnreadLiveMsg(final String dtSecCode, final boolean hasUnread) {
        if (mIsSavingToFile) return;
        getHasUnreadLiveMsgMap().put(dtSecCode, hasUnread);
    }

    public void setLastLiveMsgId(final String dtSecCode, final String id) {
        if (mIsSavingToFile) return;
        getLastSecLiveMsgIdMap().put(dtSecCode, id);
    }

    //IO functions
    public void loadCachedDataFromFile() {
        DtLog.d(TAG, "loadCachedDataFromFile");
        new LoadTask().execute(DengtaApplication.getApplication().defaultExecutor);
    }

    private static final class LoadTask extends AsyncTask {
        private Map<String, SecQuote> quoteMap = null;
        private Map<String, SecSimpleQuote> simpleQuoteMap = null;
        private Map<String, String> lastLiveMsgIdMap = null;
        private Map<String, Boolean> hasUnreadLiveMsgMap = null;

        @Override
        protected Void doInBackground(Object... params) {
            DataCacheManager dataCacheManager = DengtaApplication.getApplication().getDataCacheManager();
            quoteMap = dataCacheManager.loadQuotesFromFile();
            simpleQuoteMap = dataCacheManager.loadSimpleQuotesFromFile();
            lastLiveMsgIdMap = dataCacheManager.loadLastLiveMsgIdMapFromFile();
            hasUnreadLiveMsgMap = dataCacheManager.loadHasUnreadLiveMsgFromFile();
            return null;
        }

        @Override
        protected void onPostExecute(Object res) {
            DataCacheManager dataCacheManager = DengtaApplication.getApplication().getDataCacheManager();
            if (quoteMap != null) {
                dataCacheManager.setQuoteMap(quoteMap);
            }
            if (simpleQuoteMap != null) {
                dataCacheManager.setSimpleQuoteMap(simpleQuoteMap);
            }
            if (lastLiveMsgIdMap != null) {
                dataCacheManager.setLastSecLiveMsgIdMap(lastLiveMsgIdMap);
            }
            if (hasUnreadLiveMsgMap != null) {
                dataCacheManager.setHasUnreadLiveMsgMap(hasUnreadLiveMsgMap);
            }
        }
    }

    public void saveDataToFiles() {
        DtLog.d(TAG, "saveDataToFiles");
        if (mIsSavingToFile) {
            return;
        }

        new SaveTask().execute(DengtaApplication.getApplication().defaultExecutor);
    }

    private final class SaveTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsSavingToFile = true;
        }

        @Override
        protected Void doInBackground(Object... params) {
            saveQuotesToFile();
            saveSimpleQuotesToFile();
            saveLastLiveMsgIdMapToFile();
            saveHasUnreadLiveMsgMapToFile();
            return null;
        }

        @Override
        protected void onPostExecute(Object res) {
            mIsSavingToFile = false;
        }
    }

    //data structures
    private Map<String, String> mLastSecLiveMsgIdMap = new HashMap<>();
    private Map<String, Boolean> mHasUnreadLiveMsgMap = new HashMap<>();
    private Map<String, SecQuote> mQuoteMap = new HashMap<>();
    private Map<String, SecSimpleQuote> mSimpleQuoteMap = new HashMap<>();

    private void setQuoteMap(Map<String, SecQuote> quoteMap) {
        mQuoteMap = quoteMap;
    }

    private void setSimpleQuoteMap(Map<String, SecSimpleQuote> simpleQuoteMap) {
        mSimpleQuoteMap = simpleQuoteMap;
    }

    private void setLastSecLiveMsgIdMap(Map<String, String> lastSecLiveMsgIdMap) {
        mLastSecLiveMsgIdMap = lastSecLiveMsgIdMap;
    }

    private void setHasUnreadLiveMsgMap(Map<String, Boolean> hasUnreadLiveMsgMap) {
        mHasUnreadLiveMsgMap = hasUnreadLiveMsgMap;
    }

    private Map<String, SecQuote> loadQuotesFromFile() {
        Map<String, SecQuote> quoteMap = null;
        File quotesCacheFile = FileUtil.getQuotesCacheFile(DengtaApplication.getApplication());
        if (quotesCacheFile.exists()) {
            try {
                quoteMap = (Map<String, SecQuote>) FileUtil.getObjectFromFile(quotesCacheFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DtLog.d(TAG, "loadQuotesFromFile complete");
        if (quoteMap != null && quoteMap.size() > 0) {
            DtLog.d(TAG, "loadQuotesFromFile size = " + quoteMap.size());
        }
        return quoteMap;
    }

    private Map<String, SecSimpleQuote> loadSimpleQuotesFromFile() {
        Map<String, SecSimpleQuote> simpleQuoteMap = null;
        File quotesCacheFile = FileUtil.getSimpleQuotesCacheFile(DengtaApplication.getApplication());
        if (quotesCacheFile.exists()) {
            try {
                simpleQuoteMap = (Map<String, SecSimpleQuote>) FileUtil.getObjectFromFile(quotesCacheFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DtLog.d(TAG, "loadSimpleQuotesFromFile complete");
        if (simpleQuoteMap != null && simpleQuoteMap.size() > 0) {
            DtLog.d(TAG, "loadSimpleQuotesFromFile size = " + simpleQuoteMap.size());
        }
        return simpleQuoteMap;
    }

    private Map<String, String> loadLastLiveMsgIdMapFromFile() {
        Map<String, String> data = null;
        File cacheFile = FileUtil.getLastLiveMsgIdMapCacheFile(DengtaApplication.getApplication());
        if (cacheFile.exists()) {
            try {
                data = (Map<String, String>) FileUtil.getObjectFromFile(cacheFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DtLog.d(TAG, "loadLastLiveMsgIdMapFromFile complete");
        if (data != null && data.size() > 0) {
            DtLog.d(TAG, "loadLastLiveMsgIdMapFromFile size = " + data.size());
        }
        return data;
    }

    private Map<String, Boolean> loadHasUnreadLiveMsgFromFile() {
        Map<String, Boolean> data = null;
        File cacheFile = FileUtil.getHasUnreadLiveMsgCacheFile(DengtaApplication.getApplication());
        if (cacheFile.exists()) {
            try {
                data = (Map<String, Boolean>) FileUtil.getObjectFromFile(cacheFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DtLog.d(TAG, "loadHasUnreadLiveMsgFromFile complete");
        if (data != null && data.size() > 0) {
            DtLog.d(TAG, "loadHasUnreadLiveMsgFromFile size = " + data.size());
        }
        return data;
    }

    private void saveQuotesToFile() {
        FileUtil.saveObjectToFile(mQuoteMap, FileUtil.getQuotesCacheFile(DengtaApplication.getApplication()));
        DtLog.d(TAG, "saveQuotesToFile complete, size = " + mQuoteMap.size());
    }

    private void saveSimpleQuotesToFile() {
        FileUtil.saveObjectToFile(mSimpleQuoteMap, FileUtil.getSimpleQuotesCacheFile(DengtaApplication.getApplication()));
        DtLog.d(TAG, "saveSimpleQuotesToFile complete, size = " + mSimpleQuoteMap.size());
    }

    private void saveLastLiveMsgIdMapToFile() {
        FileUtil.saveObjectToFile(mLastSecLiveMsgIdMap, FileUtil.getLastLiveMsgIdMapCacheFile(DengtaApplication.getApplication()));
        DtLog.d(TAG, "saveLastLiveMsgIdMapToFile complete, size = " + mLastSecLiveMsgIdMap.size());
    }

    private void saveHasUnreadLiveMsgMapToFile() {
        FileUtil.saveObjectToFile(mHasUnreadLiveMsgMap, FileUtil.getHasUnreadLiveMsgCacheFile(DengtaApplication.getApplication()));
        DtLog.d(TAG, "saveHasUnreadLiveMsgMapToFile complete, size = " + mHasUnreadLiveMsgMap.size());
    }

    public ArrayList<WxWalkRecord> getWalkRecords() {
        return mWalkRecords;
    }

    public void setWalkRecords(ArrayList<WxWalkRecord> mWalkRecords) {
        this.mWalkRecords = mWalkRecords;
    }
}
