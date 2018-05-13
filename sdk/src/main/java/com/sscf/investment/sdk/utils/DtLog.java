package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * davidwei
 */
public final class DtLog {

    private static final int FLAG_LOG_NONE = 0x0;
    private static final int FLAG_LOG_CONSOLE = 0x1;
    private static final int FLAG_LOG_FILE = 0x2;

    private static String filterTag;

    public static int logMode = FLAG_LOG_NONE;

    private DtLog() {
    }

    public static void setFilterTag(String tag) {
        filterTag = tag;
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        log(Log.VERBOSE, tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        log(Log.DEBUG, tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        log(Log.INFO, tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Throwable tr) {
        w(tag, "", tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(Log.WARN, tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(Log.ERROR, tag, msg, tr);
    }

    private static void log(int priority, String tag, String msg, Throwable tr) {
        DtLogManager.getInstance().log(priority, tag, msg, tr);
    }

    public static void init(final Context context) {
        DtLogManager.getInstance().init(context);
    }

    public static void setConsoleLogEnable(final boolean enable) {
        logMode = enable ? logMode | FLAG_LOG_CONSOLE : logMode & ~FLAG_LOG_CONSOLE;
    }

    public static void setFileLogEnable(final boolean enable) {
        logMode = enable ? logMode | FLAG_LOG_FILE : logMode & ~FLAG_LOG_FILE;
        if (enable) {
            DtLogManager.getInstance().initFileThread();
        }
    }

    public static void release() {
        DtLogManager.getInstance().release();
    }

    private static final class DtLogManager implements Handler.Callback {
        private static final int MSG_SAVE_LOG = 1;
        private static final int MSG_CLOSE_OUTPUT_STREAM = 2;
        private static DtLogManager instance;
        private HandlerThread mSaveFileThread;
        private Handler mHanlder;

        private File mLogDir;
        private File mLogFile;
        private String mProcessName;

        private static final long LOG_FILE_MAX_LENGTH = 2L * 1024 * 1024;

        private PrintWriter mOut;
        private StringBuffer mOutBuffer = new StringBuffer();

        private Date mDate = new Date();
        private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

        public String getTimeString() {
            mDate.setTime(System.currentTimeMillis());
            return mDateFormat.format(mDate);
        }

        private DtLogManager() {
        }

        private void init(final Context context) {
            mLogDir = FileUtil.getExternalFilesDir(context, "log");
            mProcessName = PackageUtil.getCurrentProcessName(context);
        }

        private void initFileThread() {
            if (mSaveFileThread == null) {
                mSaveFileThread = new HandlerThread("", android.os.Process.THREAD_PRIORITY_BACKGROUND);
                mSaveFileThread.start();
                mHanlder = new Handler(mSaveFileThread.getLooper(), this);
            }
        }

        private static synchronized DtLogManager getInstance() {
            if (instance == null) {
                instance = new DtLogManager();
            }
            return instance;
        }

        public static synchronized void release() {
            if (instance.mSaveFileThread != null) {
                final Handler handler = instance.mHanlder;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessage(MSG_CLOSE_OUTPUT_STREAM);
                handler.getLooper().quit();
            }
            instance = null;
        }

        private void log(int priority, String tag, String msg, Throwable tr) {
            if ((logMode & FLAG_LOG_CONSOLE) != 0) {
                if (tr != null) {
                    msg = msg + '\n' + Log.getStackTraceString(tr);
                }
                Log.println(priority, tag, msg);
            }
            if ((logMode & FLAG_LOG_FILE) != 0) {
                if (/*FileUtil.isExternalStorageAvailable()*/true) {
                    // tag过滤
                    if (filterTag == null || filterTag.equals(tag)) {
                        if (mSaveFileThread != null && mSaveFileThread.isAlive()) {
                            mHanlder.obtainMessage(MSG_SAVE_LOG, new LogEntity(priority, tag, msg, tr)).sendToTarget();
                        }
                    }
                }
            }
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAVE_LOG:
                    mHanlder.removeMessages(MSG_CLOSE_OUTPUT_STREAM);
                    saveLog((LogEntity) msg.obj);
                    mHanlder.sendEmptyMessageDelayed(MSG_CLOSE_OUTPUT_STREAM, 60L * 1000);
                    break;
                case MSG_CLOSE_OUTPUT_STREAM:
                    FileUtil.closeStream(mOut);
                    mOut = null;
                    break;
                default:
                    break;
            }
            return false;
        }

        private void saveLog(final LogEntity logEntity) {
            if (mLogDir == null) {
                return;
            }
            if (mOut == null) {
                if (mLogFile == null) {
                    mLogFile = getLogFile();
                }
                try {
                    mOut = new PrintWriter(new FileOutputStream(mLogFile, true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (mLogFile.exists()) {
                    final long logfileLength = mLogFile.length();
                    if (logfileLength >= LOG_FILE_MAX_LENGTH) {
                        FileUtil.closeStream(mOut);
                        mOut = null;
                        mLogFile = getLogFile();
                        try {
                            mOut = new PrintWriter(new FileOutputStream(mLogFile, true));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    FileUtil.closeStream(mOut);
                    try {
                        mOut = new PrintWriter(new FileOutputStream(mLogFile, true));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (mOut != null) {
                final String time = getTimeString();
                try {
                    mOutBuffer.delete(0, mOutBuffer.length());
                    mOutBuffer.append(time);
                    mOutBuffer.append("/thread-");
                    mOutBuffer.append(Thread.currentThread().getId());
                    mOutBuffer.append('/');
                    mOutBuffer.append(logEntity.level);
                    mOutBuffer.append('/');
                    mOutBuffer.append(logEntity.tag);
                    mOutBuffer.append(" : ");
                    mOutBuffer.append(logEntity.msg);

                    if (logEntity.tr != null) {
                        mOutBuffer.append("\n");
                        logEntity.tr.printStackTrace(mOut);
                    }
                    mOutBuffer.append("\n");
                    mOut.print(mOutBuffer.toString());
                    mOut.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private File getLogFile() {
            int maxIndex = 1;

            final String time = TimeUtils.getCurrentTimeString("yyyyMMdd");

            final String pre = mProcessName + '_' + time + '_';
            final String suf = ".log";
            final File[] logFiles = mLogDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.contains(pre);
                }
            });

            String filename = null;
            if (logFiles != null && logFiles.length > 0) {
                for (File file : logFiles) {
                    filename = file.getName();
                    int perIndex = filename.indexOf(pre);
                    perIndex += pre.length();
                    int sufIndex = filename.indexOf(suf);
                    int index = Integer.parseInt(filename.substring(perIndex, sufIndex));
                    maxIndex = Math.max(index, maxIndex);
                }
            }

            File file = new File(mLogDir, pre + maxIndex + suf);
            if (file.length() > LOG_FILE_MAX_LENGTH) {
                maxIndex++;
                file = new File(mLogDir, pre + maxIndex + suf);
            }
            return file;
        }
    }

    private static final class LogEntity {
        private final String level;
        private final String tag;
        private final String msg;
        private final Throwable tr;

        public LogEntity(int priority, String tag, String msg, Throwable tr) {
            this.tag = tag;
            this.msg = msg;
            this.tr = tr;

            String level = "";
            switch (priority) {
                case Log.DEBUG:
                    level = "DEBUG";
                    break;
                case Log.WARN:
                    level = "WARN";
                    break;
                case Log.ERROR:
                    level = "ERROR";
                    break;
                case Log.INFO:
                    level = "INFO";
                    break;
                case Log.VERBOSE:
                    level = "VERBOSE";
                    break;
                default:
                    break;
            }
            this.level = level;
        }
    }
}
