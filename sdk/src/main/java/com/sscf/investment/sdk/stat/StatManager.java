package com.sscf.investment.sdk.stat;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseArray;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import BEC.BEACON_STAT_FUNCTION;
import BEC.BEACON_STAT_TYPE;
import BEC.BeaconStat;
import BEC.BeaconStatData;
import BEC.UserInfo;

/**
 * Created by yorkeehuang on 2017/6/12.
 */

public class StatManager implements DataSourceProxy.IRequestCallback {

    private static final String TAG = StatManager.class.getSimpleName();

    private static final String LOG_PREFIX = "log_";
    private static final String COUNT_PREFIX = "count_";
    private static final String GLOBAL_PREFIX = "global_";

    private static final int FILE_INFO_PREFIX_INDEX = 0;
    private static final int FILE_INFO_TIME_INDEX = 1;
    private static final int FILE_INFO_VERSION_INDEX = 2;

    private static final int VERSION = 3;

    private static StatManager sInstance = null;

    private long mStartupTime = 0L;
    private int mTempCount = 0;
    private int mLastSaveTotalFrontTime = 0;

    public static final int INVALID_STAT_KEY = -1;
    public static final int INVALID_STAT_TYPE = -1;

    private static final int MAX_LOG_CACHE = 100;
    private static final int MAX_COUNT_CACHE = 10;
    private static final int SAVE_FRONT_TIME_INTERVAL = 30 * 1000;
    private static final int STAT_FILE_TIME_LIMIT = 24 * 60 * 60 * 1000;

    private List<LogInfo> mLogCache = new ArrayList<>();

    private GlobalStat mGlobalStat = new GlobalStat();
    private Map<Integer, CountStat> mCountStatCache = new HashMap<>();

    private Handler mUiHandler;

    private File mLogFile;
    private File mCountFile;
    private File mGlobalFile;

    public static final String PARAM_SEPERATOR = ":";
    public static final String DATA_SEPERATOR = ";";
    public static final String EXTRA_SEPERATOR = ",";

    private ExecutorService mExecutor;

    public static synchronized StatManager getInstance() {
        if (sInstance == null) {
            sInstance = new StatManager();
        }
        return sInstance;
    }

    private StatManager() {
        long timeStamp = System.currentTimeMillis();
        mStartupTime = timeStamp;
        mLogFile = new File(FileUtil.getStatFileDir(), LOG_PREFIX + timeStamp + "_" + VERSION);
        mCountFile = new File(FileUtil.getStatFileDir(), COUNT_PREFIX + timeStamp + "_" + VERSION);
        mGlobalFile = new File(FileUtil.getStatFileDir(), GLOBAL_PREFIX + timeStamp + "_" + VERSION);
        mUiHandler = new Handler(Looper.getMainLooper());
        mExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void stat(final int type, final int key) {
        if(type < 0 || key < 0) {
            return;
        }
        DtLog.d(TAG, "stat() type = " + type + ", key = " + key + ", mTempCount = " + mTempCount);
        executeOnUiThread(new Runnable() {
            @Override
            public void run() {
                CountStat countStat = mCountStatCache.get(key);
                if(countStat == null) {
                    countStat = new CountStat(type, key);
                    mCountStatCache.put(key, countStat);
                }
                countStat.incCount();
                if((++mTempCount) >= MAX_COUNT_CACHE) {
                    saveCount();
                }
            }
        });
    }

    public void incTotalFrontTime(int frontTime) {
        DtLog.d(TAG, "incTotalFrontTime() frontTime = " + frontTime + ", mTotalFrontTime = " + mGlobalStat.getTotalFrontTime());
        if(mGlobalStat.incTotalFrontTime(frontTime)) {
            int totalFrontTime = mGlobalStat.getTotalFrontTime();
            if(totalFrontTime > 0 && totalFrontTime - mLastSaveTotalFrontTime > SAVE_FRONT_TIME_INTERVAL) {
                saveTotalFrontTime();
            }
        }
    }

    public void log(final int key, final int type) {
        log(key, type, "");
    }

    public void log(final int type, final int key, final String extra) {
        if(type < 0) {
            return;
        }
        DtLog.d(TAG, "log() type = "+ parseKey(type) + ", key = " + key + ", extra = " + extra);
        executeOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogCache.add(new LogInfo(key, type, System.currentTimeMillis(), extra));
                if(mLogCache.size() >= MAX_LOG_CACHE) {
                    saveLog();
                }
            }
        });
    }

    private static String parseKey(int type) {
        switch (type) {
            case BEACON_STAT_TYPE.E_BST_PORTFOLIO_PAGE:      	// 自选页面
                return "E_BST_PORTFOLIO_PAGE";
            case BEACON_STAT_TYPE.E_BST_MARKET_QUOTE_PAGE:     	// 市场行情-行情页面
                return "E_BST_MARKET_QUOTE_PAGE";
            case BEACON_STAT_TYPE.E_BST_MARKET_QUOTE_FUNC:		// 市场行情-功能页面
                return "E_BST_MARKET_QUOTE_FUNC";
            case BEACON_STAT_TYPE.E_BST_SEC_DETAIL_PAGE:        	// 个股详情-个股页面
                return "E_BST_SEC_DETAIL_PAGE";
            case BEACON_STAT_TYPE.E_BST_SEC_DETAIL_KLINE:			// 个股详情-分时K线
                return "E_BST_SEC_DETAIL_KLINE";
            case BEACON_STAT_TYPE.E_BST_SEC_DETAIL_TECHNOLOGY:	// 个股详情-技术指标
                return "E_BST_SEC_DETAIL_TECHNOLOGY";
            case BEACON_STAT_TYPE.E_BST_SEC_DETAIL_FUNC:			// 个股详情-功能页面
                return "E_BST_SEC_DETAIL_FUNC";
            case BEACON_STAT_TYPE.E_BST_MARKET_NEWS:       		// 市场要闻页面
                return "E_BST_MARKET_NEWS";
            case BEACON_STAT_TYPE.E_BST_SEC_NEWS:           		// 个股资讯页面
                return "E_BST_SEC_NEWS";
            case BEACON_STAT_TYPE.E_BST_SMART_PICK:         		// 智能选股页面
                return "E_BST_SMART_PICK";
            case BEACON_STAT_TYPE.E_BST_VIEW_NOTIFY_BAR:  		// 消息查看-通知栏消息
                return "E_BST_VIEW_NOTIFY_BAR";
            case BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER:			// 消息查看-消息中心
                return "E_BST_VIEW_MSG_CENTER";
            case BEACON_STAT_TYPE.E_BST_VIEW_PRIVI:              // 特权功能
                return "E_BST_VIEW_PRIVI";
            case BEACON_STAT_TYPE.E_BST_BIAOGE_PAGE:          	// 灯塔表哥
                return "E_BST_BIAOGE_PAGE";
            case BEACON_STAT_TYPE.E_BST_SEARCH_PAGE:
                return "E_BST_SEARCH_PAGE";
            default:
                return "";
        }
    }

    public void save() {
        saveLog();
        saveCount();
        saveTotalFrontTime();
    }


    private void saveLog() {
        DtLog.d(TAG, "saveLog()");
        execute(new SaveLogTask());
    }

    private void saveCount() {
        DtLog.d(TAG, "saveCount()");
        execute(new SaveCountTask());
    }

    private void saveTotalFrontTime() {
        int totalFrontTime = mGlobalStat.getTotalFrontTime();
        DtLog.d(TAG, "saveTotalFrontTime() mTotalFrontTime = " + totalFrontTime);
        if(totalFrontTime > 0) {
            boolean result = FileUtil.saveObjectToFile(mGlobalStat, mGlobalFile);
            DtLog.d(TAG, "saveObjectToFile result = " + result);
            if(result) {
                mLastSaveTotalFrontTime = totalFrontTime;
            }
        }
    }

    public void loadStat() {
        DtLog.d(TAG, "loadStat()");
        Context context = SDKManager.getInstance().getContext();
        if(context != null) {
            boolean ignoreLog = (NetUtil.getNetworkType(context) != NetUtil.NETWORK_TYPE_WIFI);

            new LoadTask(new LoadCallback() {

                @Override
                public void onLoadFinish(BeaconStat stat, boolean ignoreLog) {
                    if(stat != null) {
                        requestStatReport(stat, StatManager.this, ignoreLog);
                    }
                }
            }, ignoreLog).executeOnExecutor(SDKManager.getInstance().getDefaultExecutor());
        }
    }

    public static void requestStatReport(BeaconStat stat, final DataSourceProxy.IRequestCallback callback, boolean ignoreLog) {
        if(stat != null && stat.getVBeaconStatData() != null && !stat.getVBeaconStatData().isEmpty()) {
            printStat(stat);
            UserInfo userInfo = SDKManager.getInstance().getUserInfo();
            if(userInfo != null) {
                stat.stUserInfo = userInfo;
                DataEngine.getInstance().request(EntityObject.ET_STAT_REPORT, stat, callback, String.valueOf(ignoreLog));
            }
        }
    }

    private static void printStat(BeaconStat stat) {
        ArrayList<BeaconStatData> statDatas = stat.getVBeaconStatData();
        for(BeaconStatData statData : statDatas) {
            switch (statData.getEFunc()) {
                case BEACON_STAT_FUNCTION.LOG_STAT:
                    DtLog.d(TAG, "printStat log: type = " + statData.getEType() + ", time = " + statData.getITime() + ", data = " + statData.getSData());
                    break;
                case BEACON_STAT_FUNCTION.NUMBER_STAT:
                    StringBuilder sb = new StringBuilder("printStat number: type = " + statData.getEType() + ", time = " + statData.getITime() + ", data = ");
                    for(Iterator<Map.Entry<Integer, Integer>> it = statData.getMNumberData().entrySet().iterator(); it.hasNext();) {
                        Map.Entry<Integer, Integer> entry = it.next();
                        sb.append("<" + entry.getKey() + ", " + entry.getValue() + ">, ");
                    }
                    DtLog.d(TAG, sb.toString());
                    break;
                default:
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        DtLog.d(TAG, "callback success = " + success);
        switch (data.getEntityType()) {
            case EntityObject.ET_STAT_REPORT:
                handleStatReport(success, data);
                break;
            default:
        }
    }

    private void handleStatReport(boolean success, EntityObject data) {
        if(success) {
            if(TextUtils.equals(String.valueOf(true), (String) data.getExtra())) {
                clearAllStatFile(true);
            } else {
                clearAllStatFile(false);
            }
        }
    }

    public interface LoadCallback {
        void onLoadFinish(BeaconStat stat, boolean ignoreLog);
    }

    private class SaveLogTask extends AsyncTask {

        private List<LogInfo> mSaveLogs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSaveLogs = new ArrayList<>(mLogCache);
            mLogCache.clear();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BufferedWriter buffer = null;
            FileWriter fwriter = null;

            if(mSaveLogs != null && !mSaveLogs.isEmpty()) {
                try {
                    fwriter = new FileWriter(mLogFile, true);
                    buffer = new BufferedWriter(fwriter);

                    for (LogInfo log : mSaveLogs) {
                        buffer.write(log.generateStr());
                        buffer.newLine();
                    }
                    buffer.flush();
                    buffer.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(buffer != null) {
                        try {
                            buffer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(fwriter != null) {
                        try {
                            fwriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o != null && o instanceof Boolean) {
                boolean result = ((Boolean) o).booleanValue();
                DtLog.d(TAG, "saveLog result = " + result);
            }
        }
    }

    private class SaveCountTask extends AsyncTask {

        Map<Integer, CountStat> mSaveCountStat;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSaveCountStat = new HashMap<>(mCountStatCache);
            mTempCount = 0;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if(mSaveCountStat != null && !mSaveCountStat.isEmpty()) {
                return FileUtil.saveObjectToFile(mSaveCountStat, mCountFile);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o != null && o instanceof Boolean) {
                boolean result = ((Boolean) o).booleanValue();
                DtLog.d(TAG, "saveCount result = " + result);
            }
        }
    }

    private void clearOutdatedFile(long now) {
        File[] files = getAllStatFiles();
        if(files != null) {
            for (File file : files) {
                if(isOutdatedFile(file, now)) {
                    DtLog.d(TAG, "delete outdated file:" + file.getName());
                    file.delete();
                }
            }
        }
    }

    private boolean isOutdatedFile(File file, long now) {
        String name = file.getName();
        if(!TextUtils.isEmpty(name)) {
            int version = parseFileVersion(file);
            if(version == VERSION) {
                String timeStr = null;
                int versionLen = String.valueOf(version).length();
                int nameLen = name.length();
                int end = nameLen - versionLen - 1;
                if(name.startsWith(LOG_PREFIX)) {
                    timeStr = name.substring(LOG_PREFIX.length(), end);
                } else if(name.startsWith(COUNT_PREFIX)) {
                    timeStr = name.substring(COUNT_PREFIX.length(), end);
                } else if(name.startsWith(GLOBAL_PREFIX)) {
                    timeStr = name.substring(GLOBAL_PREFIX.length(), end);
                }

                if(timeStr != null) {
                    try {
                        long time = Long.valueOf(timeStr);
                        DtLog.d(TAG, "isOutdatedFile() name = " + name + ", now = " + now + ", time = " + time);
                        return !(time < now && now - time < STAT_FILE_TIME_LIMIT);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private void clearAllStatFile(boolean ignoreLog) {
        File[] files = getAllStatFiles();
        if(files != null) {
            for (File file : files) {
                if(ignoreLog && file.getName().startsWith(LOG_PREFIX)) {
                    continue;
                }
                file.delete();
            }
        }
    }

    private File[] getAllStatFiles() {
        File root = new File(FileUtil.getStatFileDir());
        return root.listFiles(new UsingFileFilter());
    }

    private class LoadTask extends AsyncTask {

        private boolean mIgnoreLog;
        private LoadCallback mCallback;

        private BeaconStat mStat;

        public LoadTask(LoadCallback callback, boolean ignoreLog) {
            mCallback = callback;
            mIgnoreLog = ignoreLog;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            clearOutdatedFile(mStartupTime);
            File[] files = getAllStatFiles();
            boolean result = false;
            if(files != null) {
                BeaconStat stat = new BeaconStat();
                for(File file : files) {
                    boolean parseResult = parseFile(file, stat, mIgnoreLog);
                    if(parseResult) {
                        result = parseResult;
                    }
                }



                if(result) {
                    loadLocation(stat);
                    mStat = stat;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o != null && o instanceof Boolean) {
                boolean result = ((Boolean) o).booleanValue();
                DtLog.d(TAG, "load result = " + result);
                if(result) {
                    if(mCallback != null) {
                        mCallback.onLoadFinish(mStat, mIgnoreLog);
                    }
                }
            }
            mCallback = null;
        }
    }

    private boolean parseFile(File file, BeaconStat stat, boolean ignoreLog) {
        if(file != null && file.exists() && file.isFile()) {
            if(file.getName().startsWith(LOG_PREFIX) && !ignoreLog) {
                return parseLogFile(file, stat);
            } else if(file.getName().startsWith(COUNT_PREFIX)) {
                return parseCountFile(file, stat);
            } else if(file.getName().startsWith(GLOBAL_PREFIX)) {
                return parseGlobalFile(file, stat);
            }
        }
        return false;
    }

    private boolean parseLogFile(File file, BeaconStat stat) {
        FileReader fileReader = null;
        BufferedReader bufferReader = null;
        int time = parseFileTime(file);
        if(time > 0) {
            try {
                fileReader = new FileReader(file);
                bufferReader = new BufferedReader(fileReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferReader.readLine()) != null) {
                    sb.append(line);
                }
                DtLog.d(TAG, "parseLogFile() file name:" + file.getName() + ", str = " + sb);
                if(sb.length() > 0) {
                    String[] datas = sb.toString().split(DATA_SEPERATOR);
                    SparseArray<StringBuilder> dataSbCache = new SparseArray<>();
                    for(String data : datas) {
                        int index = data.indexOf(PARAM_SEPERATOR);
                        int type = Integer.valueOf(data.substring(0, index));
                        String detail = data.substring(index + 1, data.length()) + DATA_SEPERATOR;
                        StringBuilder dataSb = dataSbCache.get(type);
                        if(dataSb == null) {
                            dataSb = new StringBuilder();
                            dataSbCache.put(type, dataSb);
                        }
                        dataSb.append(detail);
                    }

                    int size = dataSbCache.size();
                    if(size > 0) {
                        ArrayList<BeaconStatData> statDatas = new ArrayList<>(size);
                        for (int i=0; i<size; i++) {
                            int type = dataSbCache.keyAt(i);
                            StringBuilder dataSb = dataSbCache.valueAt(i);
                            BeaconStatData statData = new BeaconStatData();
                            statData.setEFunc(BEACON_STAT_FUNCTION.LOG_STAT);
                            statData.setEType(type);
                            statData.setITime(time);
                            statData.setSData(dataSb.toString());
                            statDatas.add(statData);
                        }

                        if(stat.getVBeaconStatData() == null) {
                            stat.setVBeaconStatData(statDatas);
                        } else {
                            stat.getVBeaconStatData().addAll(statDatas);
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(bufferReader != null) {
                    try {
                        bufferReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }

    private int parseFileTime(File file) {
        String info = parseFileInfo(file, FILE_INFO_TIME_INDEX);
        if(!TextUtils.isEmpty(info)) {
            try {
                long timeStamp = Long.valueOf(info);
                return (int)(timeStamp / 1000);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int parseFileVersion(File file) {
        String info = parseFileInfo(file, FILE_INFO_VERSION_INDEX);
        if(!TextUtils.isEmpty(info)) {
            try {
                return Integer.valueOf(info);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private String parseFileInfo(File file, int index) {
        if(file != null && file.isFile() && file.exists()) {
            String name = file.getName();
            String[] infos = name.split("_");
            if(infos.length > index) {
                return infos[index];
            }
        }
        return "";
    }

    private boolean parseCountFile(File file, BeaconStat stat) {
        int time = parseFileTime(file);
        if(time > 0) {
            Object obj = FileUtil.getObjectFromFile(file);
            if(obj != null && obj instanceof Map) {
                Map<Integer, CountStat> countCache = (Map<Integer, CountStat>) obj;
                if(!countCache.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    SparseArray<BeaconStatData> statDataCache = new SparseArray<>();
                    for(Iterator<Map.Entry<Integer, CountStat>> it = countCache.entrySet().iterator(); it.hasNext();) {
                        Map.Entry<Integer, CountStat> entry = it.next();
                        CountStat countStat = entry.getValue();
                        sb.append("< key = " + entry.getKey() + ", type = " + countStat.getType() + ", count = " + countStat.getCount() + ">;");
                        BeaconStatData statData = statDataCache.get(countStat.getType());
                        if(statData == null) {
                            statData = new BeaconStatData();
                            statData.setEFunc(BEACON_STAT_FUNCTION.NUMBER_STAT);
                            statData.setEType(countStat.getType());
                            statData.setITime(time);
                            statDataCache.put(countStat.getType(), statData);
                        }

                        Map<Integer, Integer> map = statData.getMNumberData();
                        if(map == null) {
                            map = new HashMap<>();
                            statData.setMNumberData(map);
                        }

                        map.put(countStat.getKey(), countStat.getCount());
                    }
                    DtLog.d(TAG, "parseCountFile() countCache:" + sb);

                    int size = statDataCache.size();
                    if(size > 0) {
                        ArrayList<BeaconStatData> statDatas = stat.getVBeaconStatData();
                        if(statDatas == null) {
                            statDatas = new ArrayList<>();
                            stat.setVBeaconStatData(statDatas);
                        }

                        for(int i=0; i<size; i++) {
                            BeaconStatData statData = statDataCache.valueAt(i);
                            statDatas.add(statData);
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean parseGlobalFile(File file, BeaconStat stat) {
        int time = parseFileTime(file);
        if(time > 0) {
            Object obj = FileUtil.getObjectFromFile(file);
            if(obj != null && obj instanceof GlobalStat) {
                GlobalStat globalStat = (GlobalStat) obj;
                DtLog.d(TAG, "parseGlobalFile() totalFrontTime:" + globalStat.getTotalFrontTime());
                ArrayList<BeaconStatData> statDatas = stat.getVBeaconStatData();
                if(statDatas == null) {
                    statDatas = new ArrayList<>();
                    stat.setVBeaconStatData(statDatas);
                }
                BeaconStatData statData = new BeaconStatData();
                statData.setEFunc(BEACON_STAT_FUNCTION.NUMBER_STAT);
                statData.setEType(BEACON_STAT_TYPE.E_BST_STAY_TIME);
                Map<Integer, Integer> map = new HashMap<>(1);
                map.put(StatConsts.TOTAL_FRONT_TIME, globalStat.getTotalFrontTime() / 1000);
                statData.setMNumberData(map);
                statData.setITime(time);
                statDatas.add(statData);
                return true;
            }
        }
        return false;
    }

    private boolean isUsingFile(String name) {
        DtLog.d(TAG, "mLogFile.getName() = " + mLogFile.getName() + ", name = " + name);
        if(TextUtils.equals(mLogFile.getName(), name)) {
            return true;
        } else if(TextUtils.equals(mCountFile.getName(), name)) {
            return true;
        }
        return false;
    }

    private boolean loadLocation(BeaconStat stat) {
        Context context = SDKManager.getInstance().getContext();
        if(context != null) {
            final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location loc = null;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
            criteria.setAltitudeRequired(false);//无海拔要求
            criteria.setBearingRequired(false);//无方位要求
            criteria.setCostAllowed(true);//允许产生资费
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            try {
                // 获取最佳服务对象
                String provider = locationManager.getBestProvider(criteria, true);
                DtLog.d(TAG, "loadLocation() provider = " + provider);
                loc = locationManager.getLastKnownLocation(provider);
                if(loc == null) {
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    provider = locationManager.getBestProvider(criteria, true);
                    DtLog.d(TAG, "loadLocation() provider = " + provider);
                    loc = locationManager.getLastKnownLocation(provider);
                }
                if(loc != null) {
                    DtLog.d(TAG, "loadLocation() longitude = " + loc.getLongitude() + ", latitude = " + loc.getLatitude());
                    BeaconStatData statData = new BeaconStatData();
                    statData.setEFunc(BEACON_STAT_FUNCTION.LOG_STAT);
                    statData.setEType(BEACON_STAT_TYPE.E_BST_MAP_POSIITON);
                    long timeStamp = System.currentTimeMillis();
                    statData.setITime((int) (timeStamp / 1000));
                    statData.setSData("0" + PARAM_SEPERATOR + timeStamp + PARAM_SEPERATOR + loc.getLongitude() + EXTRA_SEPERATOR+ loc.getLatitude()+ DATA_SEPERATOR);
                    ArrayList<BeaconStatData> statDataList = stat.getVBeaconStatData();
                    if(statDataList == null) {
                        statDataList = new ArrayList<>();
                        stat.setVBeaconStatData(statDataList);
                    }
                    statDataList.add(statData);
                } else {
                    DtLog.d(TAG, "获取位置失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void executeOnUiThread(Runnable runnable) {
        if(isUiThread()) {
            runnable.run();
        } else {
            mUiHandler.post(runnable);
        }
    }

    public void execute(final AsyncTask task) {
        if(isUiThread()) {
            task.executeOnExecutor(mExecutor);
        } else {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    task.executeOnExecutor(mExecutor);
                }
            });
        }
    }

    public void execute(final Runnable runnable) {
        if (mUiHandler == null) {
            mUiHandler = new Handler(Looper.getMainLooper());
        }

        if(isUiThread()) {
            mExecutor.execute(runnable);
        } else {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mExecutor.execute(runnable);
                }
            });
        }
    }

    private static boolean isUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private class LogInfo {
        private int mKey;
        private int mType;
        private long mTime;
        private String mExtra = "";
        public LogInfo(int key, int type, long time, String extra) {
            mKey = key;
            mType = type;
            mTime = time;
            if(!TextUtils.isEmpty(extra)) {
                mExtra = extra;
            }
        }

        public String generateStr() {
            return mType + PARAM_SEPERATOR + mKey + PARAM_SEPERATOR + mTime + PARAM_SEPERATOR + mExtra + DATA_SEPERATOR;
        }
    }

    private static class CountStat implements Serializable {
        private int type;
        private int key;
        private int count;
        public CountStat(int type, int key) {
            this.type = type;
            this.key = key;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void incCount() {
            if(count > 0) {
                count++;
            } else {
                count = 1;
            }
        }

        public int getCount() {
            return count;
        }

        public int getType() {
            return type;
        }

        public int getKey() {
            return key;
        }
    }

    private static class GlobalStat implements Serializable {
        private int totalFrontTime = 0;

        public void setTotalFrontTime(int totalFrontTime) {
            this.totalFrontTime = totalFrontTime;
        }

        public int getTotalFrontTime() {
            return totalFrontTime;
        }

        public boolean incTotalFrontTime(int frontTime) {
            if(frontTime > 0) {
                totalFrontTime += frontTime;
                return true;
            }
            return false;
        }
    }

    private class UsingFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            DtLog.d(TAG, "name = " + name);
            return !isUsingFile(name);
        }
    }
}
