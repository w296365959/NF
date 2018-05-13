package com.sscf.investment.sdk.download;

import android.text.TextUtils;

import com.sscf.investment.sdk.ContextHolder;
import com.sscf.investment.sdk.download.entity.DownloadTaskEntity;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/2/28.
 */

public final class DownloadTask implements Runnable {

    private static  final String TAG = DownloadTask.class.getSimpleName();

    final private DownloadTaskEntity mData;

    private List<DownloadObserver> mObservers = new ArrayList<>();
    private InnerDownloadObserver mInnerObserver;
    final private DownloadController mController;

    private State mState = new State();

    public static final int STATE_DEFAULT = 1;
    public static final int STATE_INITIALING = 2;
    public static final int STATE_INITIALED = 3;
    public static final int STATE_DOWNLOADING = 4;
    public static final int STATE_PAUSE = 5;
    public static final int STATE_FINISH = 6;
    public static final int STATE_FAILED = 7;

    private int mDownloadProgress;

    private File mTempFile = null;
    private File mFile = null;

    public DownloadTask(DownloadController controller, DownloadTaskEntity data, InnerDownloadObserver innerObserver) {
        mData = data;
        mFile = createFile(data);
        mTempFile = createTempFile(data);
        mController = new DownloadController(controller);
        mInnerObserver = innerObserver;
    }

    private static File createFile(DownloadTaskEntity data) {
        return new File(data.getPath(), data.getFileName());
    }

    public static File createTempFile(DownloadTaskEntity data) {
        return new File(data.getPath(), data.getFileName() +".tmp");
    }

    void attachController(DownloadController controller) {
        mController.attach(controller);
    }

    void detachController() {
        mController.detach();
    }

    public File getFile() {
        return mFile;
    }

    public void addObserver(DownloadObserver observer) {
        addObserver(observer, true);
    }

    public void addObserver(final DownloadObserver observer, final boolean single) {
        mController.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if(single) {
                    mObservers.clear();
                }
                mObservers.add(observer);
            }
        });
    }

    public DownloadTaskEntity getData() {
        return mData;
    }



    public int getTaskId() {
        return mData.getTaskId();
    }

    public void delete() {
        if(mFile.exists()) {
            mFile.delete();
        }

        if(mTempFile.exists()) {
            mTempFile.delete();
        }
    }

    public void init() {
        mState.initState();
        switch (getData().getState()) {
            // 如果为默认状态，则进行初始化
            case STATE_DEFAULT:
                mController.runOnExecutor(this);
                break;
            // 如果正在初始化，则不进行任何操作
            case STATE_INITIALING:
                break;
            // 如果先前初始化过，则恢复为默认状态,重新初始化
            case STATE_INITIALED:
                mState.setState(STATE_DEFAULT);
                mController.runOnExecutor(this);
                break;
            // 如果处于下载状态，则通知开始下载
            case STATE_DOWNLOADING:
                notifyStart(computeProgress(getData().getDownloadedSize()));
                break;
            // 如果处于暂停状态，则通知处于暂停
            case STATE_PAUSE:
                notifyPause(computeProgress(getData().getDownloadedSize()));
                break;
            // 如果处于结束状态，设置为初始状态，进行重新初始化检查
            case STATE_FINISH:
                mState.setState(STATE_DEFAULT);
                mController.runOnExecutor(this);
                break;
            default:

        }
    }

    public void start() {
        switch (mState.getState()) {
            case DownloadTask.STATE_INITIALED:
            case DownloadTask.STATE_PAUSE:
            case DownloadTask.STATE_FINISH:
            case DownloadTask.STATE_FAILED:
                mController.runOnExecutor(this);
                break;
            default:
        }
    }

    public void pause() {
        mController.runOnUIThread(new UITask() {
            @Override
            void runTask() {
                synchronized (mState) {
                    if(mState.getState() == STATE_DOWNLOADING) {
                        mState.setState(STATE_PAUSE);
                    }
                }
            }
        });
    }

    public void resetState() {
        mState.setState(STATE_DEFAULT);
        mData.setDownloadedSize(0);
        mController.saveEntity(mData);
    }

    private class State {
        private int mState = STATE_DEFAULT;

        public void initState() {
            DownloadTaskEntity entity = getData();
            mState = entity.getState();
        }

        public void setState(int state) {
            mState = state;
            DownloadTaskEntity entity = getData();
            entity.setState(state);
            mController.saveEntity(entity);
        }

        public int getState() {
            return mState;
        }
    }


    public boolean isSameTask(String url) {
        return TextUtils.equals(url, mData.getUrl());
    }

    @Override
    public void run() {
        synchronized (mState) {
            switch (mState.getState()) {
                case STATE_DEFAULT:
                    mState.setState(STATE_INITIALING);
                    break;
                case STATE_INITIALED:
                case STATE_FAILED:
                    prepareDownload();
                    break;
                case STATE_PAUSE:
                    prepareDownload();
                    break;
                default:
            }
        }

        switch (mState.getState()) {
            case STATE_INITIALING:
                long downloadedSize = initFile();
                if(downloadedSize == getData().getTotalSize()) {
                    mState.setState(STATE_FINISH);
                    notifyFinish(true);
                } else {
                    mState.setState(STATE_INITIALED);
                    notifyInitialed(computeProgress(downloadedSize));
                }
                break;
            case STATE_DOWNLOADING:
                download();
                break;
            default:
        }
    }

    private void prepareDownload() {
        if(getData().getDownloadedSize() == getData().getTotalSize()) {
            mState.setState(STATE_FINISH);
            notifyFinish(false);
            return;
        } else {
            if (!NetUtil.isNetWorkConnected(ContextHolder.getCtx())) {
                notifyError(DownloadObserver.ERROR_NETWORK_UNAVAILABLE);
                return;
            } else if(getData().getDownloadedSize() > 0) {
                if(mTempFile.exists()) {
                    if(mTempFile.length() != getData().getDownloadedSize()) {
                        mTempFile.delete();
                        storeDownloadedLength(0);
                    }
                } else {
                    storeDownloadedLength(0);
                }
            }
            mState.setState(STATE_DOWNLOADING);
        }
    }

    private long checkFile() {
        // 检查apk文件有没问题
        DownloadTaskEntity entity = getData();
        if (hasApk()) {
            // 如果文件与信息不符，说明apk文件有问题，重新下载
            if (checkApk()) {
                // apk文件没问题，就不检查temp文件了，存在temp文件就直接删除
                if (hasTemp()) {
                    mTempFile.delete();
                }
                return entity.getTotalSize();
            } else {
                mFile.delete();
            }
        }

        // 检查apk temp文件有没问题，判断是否可以续传
        if (hasTemp()) {
            final long downloadedSize = mTempFile.length();
            DtLog.d(TAG, "checkFile() mTempFile.length() = " + downloadedSize);
            if(entity.getDownloadedSize() == downloadedSize) {
                // 如果下载完成，则校验md5，如果md5匹配，则修改成apk，否则删除掉temp文件
                if(downloadedSize == entity.getTotalSize()) {
                    if(convertTemp2Apk()) {
                        return downloadedSize;
                    }
                } else {
                    return downloadedSize;
                }
            } else {
                mTempFile.delete();
            }
        }
        return 0L;
    }

    private long initFile() {
        DownloadTaskEntity entity = getData();
        DtLog.d(TAG, "initFile() entity.getDownloadedSize() = " + entity.getDownloadedSize());
        long downloadedSize = checkFile();
        return downloadedSize;
    }

    private void download() {
        long downloadedLength = getData().getDownloadedSize();
        notifyStart(computeProgress(downloadedLength));
        final byte[] buffer = new byte[DownloadConsts.DEFAULT_DOWNLOAD_BUFFER_SIZE];
        int byteread = 0;

        InputStream in = null;
        OutputStream out = null;
        if (downloadedLength >= getData().getTotalSize()) {
            downloadedLength = 0;
            mDownloadProgress = 0;
        } else {
            mDownloadProgress = computeProgress(downloadedLength);
        }

        if (downloadedLength == 0) { // 全新下载
            notifyProgress(0);
        }

        final String urlStr = mData.getUrl();

        try {
            final URL url = new URL(urlStr);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Range", "bytes=" + downloadedLength + "-");

            in = conn.getInputStream();

            final String etag = conn.getHeaderField("ETag");
            DtLog.d(TAG, "etag : " + etag);
            String acceptRanges = conn.getHeaderField("Accept-Ranges");
            DtLog.d(TAG, "acceptRanges : " + acceptRanges);
            String contentRange = conn.getHeaderField("Content-Range");
            DtLog.d(TAG, "contentRange : " + contentRange);
            final int contentLength = conn.getContentLength();
            DtLog.d(TAG, "contentLength : " + contentLength);

            if (TextUtils.isEmpty(contentRange)) { // 服务器不支持断点续传
                downloadedLength = 0;
                mDownloadProgress = 0;
                notifyProgress(0);
                if (mTempFile.exists() && mTempFile.isFile()) {
                    mTempFile.delete();
                }
            }

            out = new BufferedOutputStream(new FileOutputStream(mTempFile, true));

            while ((byteread = in.read(buffer)) != -1 && mState.getState() == STATE_DOWNLOADING) {
                out.write(buffer, 0, byteread);
                downloadedLength += byteread;
                int progress = computeProgress(downloadedLength);
                if (progress > mDownloadProgress) {
                    out.flush();
                    storeDownloadedLength(downloadedLength);
                    DtLog.d(TAG, "mTempFile.length() = " + mTempFile.length());
                    notifyProgress(progress);
                    mDownloadProgress = progress;
                }
            }
            out.flush();

            synchronized (mState) {
                if (mState.getState() == STATE_DOWNLOADING) {
                    if (convertTemp2Apk()) {
                        mState.setState(STATE_FINISH);
                        notifyFinish(false);
                    } else {
                        mTempFile.delete();
                        mState.setState(STATE_FAILED);
                        storeDownloadedLength(0);
                        notifyError(DownloadObserver.ERROR_CHECK_FILE_FAILED);
                    }
                } else {
                    notifyPause(computeProgress(downloadedLength));
                    storeDownloadedLength(downloadedLength);
                }
            }

        } catch (SocketException se){
            se.printStackTrace();
            synchronized (mState) {
                mState.setState(STATE_PAUSE);
                storeDownloadedLength(downloadedLength);
            }
            notifyPause(computeProgress(downloadedLength));
        } catch (Exception e) {
            e.printStackTrace();
            synchronized (mState) {
                mTempFile.delete();
                mState.setState(STATE_FAILED);
                storeDownloadedLength(0);
            }
            notifyError(DownloadObserver.ERROR_DOWNLOAD_FAILED);
        } finally {
            DtLog.d(TAG, "mTempFile.length() = " + mTempFile.length());
            FileUtil.closeStream(in, out);
        }
    }

    private int computeProgress(long downloadedLength) {
        return (int) (downloadedLength * 100 / getData().getTotalSize());
    }

    private void notifyInitialed(final int progress) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                if(mInnerObserver != null) {
                    mInnerObserver.onInitialed(DownloadTask.this, progress);
                }

                for(DownloadObserver observer : mObservers) {
                    if(observer != null) {
                        observer.onInitialed(progress);
                    }
                }
            }
        });
    }

    private void notifyProgress(final int progress) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                if(mInnerObserver != null) {
                    mInnerObserver.onProgress(DownloadTask.this, progress);
                }
                for(DownloadObserver observer : mObservers) {
                    observer.onProgress(progress);
                }
            }
        });
    }

    private void notifyStart(final int progress) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                mInnerObserver.onStart(DownloadTask.this, progress);
                for(DownloadObserver observer : mObservers) {
                    observer.onStart(progress);
                }
            }
        });
    }

    private void notifyPause(final int progress) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                mInnerObserver.onPause(DownloadTask.this, progress);
                for(DownloadObserver observer : mObservers) {
                    observer.onPause(progress);
                }
            }
        });

    }

    private void notifyFinish(final boolean isInitCheck) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                for(DownloadObserver observer : mObservers) {
                    observer.onFinish(isInitCheck);
                }
                mInnerObserver.onFinish(DownloadTask.this, isInitCheck);
            }
        });
    }

    private void notifyError(final int error) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                if(mInnerObserver != null) {
                    mInnerObserver.onError(DownloadTask.this, error);
                    for(DownloadObserver observer : mObservers) {
                        observer.onError(error);
                    }
                }
            }
        });
    }

    private void storeDownloadedLength(long downloadedLength) {
        DownloadTaskEntity entity = getData();
        entity.setDownloadedSize(downloadedLength);
        mController.saveEntity(entity);
        DtLog.d(TAG, "storeDownloadedLength() downloadedLength = " + downloadedLength);
    }

    public boolean checkApk() {
        return mFile.length() == getData().getTotalSize()
                && getData().getMd5().equalsIgnoreCase(FileUtil.getFileMD5(mFile));
    }

    public boolean hasApk() {
        return mFile.exists() && mFile.isFile();
    }

    private boolean hasTemp() {
        return mTempFile.exists() && mTempFile.isFile();
    }

    private boolean convertTemp2Apk() {
        if(hasTemp()) {
            if(getData().getMd5().equalsIgnoreCase(FileUtil.getFileMD5(mTempFile))) {
                return mTempFile.renameTo(mFile);
            } else {
                mTempFile.delete();
            }
        }
        return false;
    }

    public void removeObserver(final DownloadObserver observer) {
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                mObservers.remove(observer);
            }
        });
    }

    public void release() {
        detachController();
        mController.runOnUIThread(new UITask() {
            @Override
            public void runTask() {
                mObservers.clear();
                mInnerObserver = null;
            }
        });
    }

    private abstract class UITask implements Runnable {
        @Override
        public void run() {
            if(mInnerObserver != null) {
                runTask();
            }
        }

        abstract void runTask();
    }
}

