package com.sscf.investment.sdk.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseArray;

import com.sscf.investment.db.DBHelper;
import com.sscf.investment.sdk.download.entity.DownloadTaskEntity;
import com.sscf.investment.sdk.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yorkeehuang on 2017/3/3.
 */

public class DtDownloadManager implements InnerDownloadObserver {

    private Context mContext;

    private DBHelper mDBHelper;

    private SparseArray<DownloadTask> mTaskList = new SparseArray<>();

    private Executor mExecutor;

    private Handler mUIHandler;

    private DownloadController mController;

    public DtDownloadManager(Context context) {
        mContext = context;
        init();
    }

    private synchronized void init() {
        mUIHandler = new Handler(Looper.getMainLooper());
        mExecutor = Executors.newCachedThreadPool();
        mDBHelper = DBHelper.getInstance(mContext);
        mController = new DownloadController(mUIHandler, mExecutor, mDBHelper);
        List<DownloadTaskEntity> entityList = mDBHelper.findAll(DownloadTaskEntity.class);
        mTaskList.clear();
        if(entityList != null && !entityList.isEmpty()) {
            for(DownloadTaskEntity entity : entityList) {
                if(entity.getState() == DownloadTask.STATE_FINISH) {
                    mDBHelper.delete(entity);
                } else {
                    if(entity.getState() == DownloadTask.STATE_DOWNLOADING) {
                        entity.setState(DownloadTask.STATE_PAUSE);
                        recoverFile(entity);
                        mDBHelper.update(entity);
                    }
                    DownloadTask task = new DownloadTask(mController, entity, this);
                    mTaskList.put(task.getData().getTaskId(), task);
                }
            }
        }
    }

    private boolean recoverFile(DownloadTaskEntity data) {
        File file = DownloadTask.createTempFile(data);
        long len;
        if(file.exists() && file.length() > DownloadConsts.DEFAULT_DOWNLOAD_BUFFER_SIZE) {
            len = data.getDownloadedSize() - DownloadConsts.DEFAULT_DOWNLOAD_BUFFER_SIZE;
        } else {
            len = 0L;
        }
        data.setDownloadedSize(len);
        if(len > 0) {
            try {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.setLength(len);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                file.delete();
            }
        } else {
            if(file.exists()) {
                file.delete();
            }
        }
        data.setState(DownloadTask.STATE_DEFAULT);
        return false;
    }

    private DownloadTaskEntity createEntity(String url, String path, String fileName, boolean isUnique, long totalSize, String md5) {
        DownloadTaskEntity entity = new DownloadTaskEntity();
        entity.setUrl(url);
        entity.setPath(path);
        entity.setFileName(fileName);
        entity.setTotalSize(totalSize);
        entity.setUnique(isUnique);
        entity.setMd5(md5);
        entity.setState(DownloadTask.STATE_DEFAULT);
        if(mDBHelper.saveBindId(entity)) {
            return entity;
        }
        return null;
    }

    public synchronized DownloadTask addTask(String url, String fileName, long totalSize, String md5) {
        return addTask(url, getDefaultFolder(), fileName, true, totalSize, md5);
    }

    private String getDefaultFolder() {
        return  FileUtil.getExternalFilesDir(mContext, "download").getPath();
    }

    public synchronized DownloadTask addTask(String url, String path, String fileName, boolean isUnique, long totalSize, String md5) {

        if(TextUtils.isEmpty(url)
                || TextUtils.isEmpty(path)
                || TextUtils.isEmpty(fileName)
                || totalSize <= 0
                || TextUtils.isEmpty(md5)) {
            return null;
        }

        Set<Integer> removeTaskIds = new HashSet<>();
        for(int i = 0, size = mTaskList.size(); i<size; i++) {
            DownloadTask task = mTaskList.valueAt(i);
            // 如果下载
            if(task.isSameTask(url)) {
                if(isUnique || task.getData().isUnique()) {
                    // 如果老任务或新任务必须唯一，
                    // 并且文件信息与新任务完全一致，
                    // 则直接返回老任务id
                    if(isSameDownloadFile(path, fileName, totalSize, md5, task)) {
                        if(task.getData().getState() == DownloadTask.STATE_FINISH) {
                            if(!task.hasApk()) {
                                task.getData().setState(DownloadTask.STATE_DEFAULT);
                            }
                        }
                        task.attachController(mController);
                        return task;
                    } else {
                        // 如果老任务或新任务必须唯一，
                        // 而文件信息有区别，则删除掉老任务
                        task.delete();
                        removeTaskIds.add(task.getData().getTaskId());
                    }
                } else {
                    if(isSameDownloadPath(path, fileName, task)) {
                        if(isSameFileInfo(totalSize, md5, task)) {
                            task.attachController(mController);
                            return task;
                        } else {
                            task.delete();
                            removeTaskIds.add(task.getData().getTaskId());
                        }
                    }
                }
            }
        }

        for(Iterator<Integer> it=removeTaskIds.iterator(); it.hasNext();) {
            int taskId = it.next();
            mTaskList.remove(taskId);
        }

        DownloadTaskEntity entity = createEntity(url, path, fileName, isUnique, totalSize, md5);
        if(entity != null) {
            DownloadTask task = new DownloadTask(mController, entity, this);
            mTaskList.put(task.getTaskId(), task);
            return task;
        }
        return null;
    }

    public void resetTask(DownloadTask task) {
        task.resetState();
        task.attachController(mController);
        task.init();
    }

    public DownloadTask getTask(int taskId) {
        DownloadTask task;
        synchronized (mTaskList) {
            task = mTaskList.get(taskId);
        }
        return task;
    }

    public void startTask(int taskId) {
        DownloadTask task;
        synchronized (mTaskList) {
            task = mTaskList.get(taskId);
        }
        if(task != null) {
            task.start();
        }
    }

    public List<DownloadTask> findTaskByUrl(String url) {
        List<DownloadTask> taskList = new ArrayList<>();
        synchronized (mTaskList) {
            for(int i = 0, size = mTaskList.size(); i<size; i++) {
                DownloadTask task = mTaskList.valueAt(i);
                if(TextUtils.equals(task.getData().getUrl(), url)) {
                    taskList.add(task);
                }
            }
        }
        return taskList;
    }

    public List<DownloadTask> getAllTasks() {
        List<DownloadTask> taskList = new ArrayList<>(mTaskList.size());
        for(int i=0, size=taskList.size(); i<size; i++) {
            taskList.add(mTaskList.valueAt(i));
        }
        return taskList;
    }

    private boolean isSameDownloadPath(String path, String fileName, DownloadTask task) {
        DownloadTaskEntity entity = task.getData();
        return TextUtils.equals(path, entity.getPath())
                && TextUtils.equals(fileName, entity.getFileName());
    }

    private boolean isSameFileInfo(long totalSize, String md5, DownloadTask task) {
        DownloadTaskEntity entity = task.getData();
        return totalSize == entity.getTotalSize()
                && TextUtils.equals(md5, entity.getMd5());
    }

    private boolean isSameDownloadFile(String path, String fileName, long totalSize, String md5, DownloadTask task) {
        return isSameDownloadPath(path, fileName, task)
                && isSameFileInfo(totalSize, md5, task);
    }

    @Override
    public void onInitialed(DownloadTask task, int progress) {

    }

    @Override
    public void onStart(DownloadTask task, int progress) {

    }

    @Override
    public void onPause(DownloadTask task, int progress) {

    }

    @Override
    public void onProgress(DownloadTask task, int progress) {

    }

    @Override
    public void onError(DownloadTask task, int error) {

    }

    @Override
    public void onFinish(DownloadTask task, boolean isInitCheck) {
        task.detachController();
    }

    public void releaseAllTask() {
        synchronized (mTaskList) {
            for(int i = 0, size = mTaskList.size(); i<size; i++) {
                DownloadTask task = mTaskList.valueAt(i);
                task.release();
            }
            mTaskList.clear();
        }
    }
}
