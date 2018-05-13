package com.sscf.investment.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.sscf.investment.main.DengtaApplication;
import com.squareup.leakcanary.RefWatcher;

public abstract class BaseLazyFragment extends Fragment {
    private static final String TAG = BaseLazyFragment.class.getSimpleName();
    private boolean isPrepared;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    public boolean isFirstResume() {
        return isFirstResume;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final RefWatcher refWatcher = DengtaApplication.getRefWatcher();
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        final boolean lastIsVisibleToUser = getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if (!isPrepared || lastIsVisibleToUser != isVisibleToUser) { // 避免反复调用
            if (isVisibleToUser) {
                if (isFirstVisible) {
                    isFirstVisible = false;
                    initPrepare();
                } else {
                    onUserVisible();
                }
            } else {
                if (isFirstInvisible) {
                    isFirstInvisible = false;
                    onFirstUserInvisible();
                } else {
                    onUserInvisible();
                }
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume）
     */
    public abstract void onUserVisible();

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public abstract void onFirstUserInvisible();

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public abstract void onUserInvisible();
}