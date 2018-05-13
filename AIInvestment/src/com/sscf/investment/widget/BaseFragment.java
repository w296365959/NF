package com.sscf.investment.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sscf.investment.R;
import com.sscf.investment.common.BaseLazyFragment;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;

public class BaseFragment extends BaseLazyFragment {
    private static final String TAG = BaseLazyFragment.class.getSimpleName();
    protected TimeStatHelper mTimeStatHelper;
    private boolean mIsStatusSetEnable = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeStatHelper = createTimeStatHelper();
    }

    protected TimeStatHelper createTimeStatHelper() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isRealHidden() {
        return isHidden();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        DtLog.d(TAG, "setUserVisibleHint() isVisibleToUser = " + isVisibleToUser + " : " + this);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "lifecycle onUserVisible() : first = true " + this);
        if(!isRealHidden() && mTimeStatHelper != null) {
            mTimeStatHelper.start();
        }

        setStatusBarIfNeed();
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "lifecycle onUserVisible() : first = false " + this);
        if(!isRealHidden() && mTimeStatHelper != null) {
            mTimeStatHelper.start();
        }

        setStatusBarIfNeed();
    }

    private void setStatusBarIfNeed() {
        if(mIsStatusSetEnable && getActivity() instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();
            if(!activity.isDestroy()) {
                DeviceUtil.enableTranslucentStatus(activity, ContextCompat.getColor(activity, getStatusbarRes()));
            }
        }
    }

    protected void enableStatusSet() {
        mIsStatusSetEnable = true;
    }

    protected void disableStatusSet() {
        mIsStatusSetEnable = false;
    }

    protected int getStatusbarRes() {
        return R.color.actionbar_bg;
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.d(TAG, "lifecycle onUserInvisible() : first = true " + this);
        if(mTimeStatHelper != null) {
            mTimeStatHelper.end();
        } else {
//            DtLog.d(TAG, "onFirstUserInvisible() mTimeStatHelper == null: " + this);
        }
    }

    @Override
    public void onUserInvisible() {
        DtLog.d(TAG, "lifecycle onUserInvisible() : first = false " + this);
        if(mTimeStatHelper != null) {
            mTimeStatHelper.end();
        } else {
//            DtLog.d(TAG, "onUserInvisible() mTimeStatHelper == null: " + this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        DtLog.d(TAG, "onHiddenChanged : " + this + " , hidden = " + hidden);
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
    }
}
