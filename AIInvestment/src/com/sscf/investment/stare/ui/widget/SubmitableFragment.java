package com.sscf.investment.stare.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.stare.ui.Submitable;
import com.sscf.investment.stare.ui.presenter.StarePresenter;
import com.sscf.investment.widget.BaseFragment;

/**
 * Created by yorkeehuang on 2017/9/14.
 */

public abstract class SubmitableFragment extends BaseFragment implements Submitable {

    private static final String TAG = SubmitableFragment.class.getSimpleName();

    protected StarePresenter mPresenter;
    private StockDbEntity mStockEntity;
    private boolean mIsViewCreated = false;

    public void setPresenter(StarePresenter presenter) {
        mPresenter = presenter;
    }

    private boolean mIsResume = false;

    @Override
    public void onResume() {
        DtLog.d(TAG, "onResume(), isVisible() = " + isVisible() + ", this = " + this);
        if(isVisible()) {
            mIsResume = true;
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        mIsResume = false;
        super.onPause();
        DtLog.d(TAG, "onResume(), isVisible() = " + isVisible() + ", this = " + this);
    }

    @Override
    public void onFirstUserVisible() {
        DtLog.d(TAG, "onFirstUserVisible() = " + isVisible() + ", this = " + this);
        super.onFirstUserVisible();
        hideFocus();
        mIsResume = false;
    }

    @Override
    public void onUserVisible() {
        DtLog.d(TAG, "onUserVisible(), isVisible() = " + isVisible() + ", this = " + this + ", mIsResume = " + mIsResume);
        super.onUserVisible();
        if(mIsResume) {
            mIsResume = false;
        } else {
            hideFocus();
        }
    }

    @Override
    public void onFirstUserInvisible() {
        DtLog.d(TAG, "onFirstUserInvisible(), this = " + this + ", mIsResume = " + mIsResume);
        mIsResume = false;
        super.onFirstUserInvisible();
    }

    @Override
    public void onUserInvisible() {
        DtLog.d(TAG, "onUserInvisible(), this = " + this + ", mIsResume = " + mIsResume);
        mIsResume = false;
        super.onUserInvisible();
    }

    private void hideFocus() {
        Context context = getContext();
        View rootView = getView();

        if(context != null && rootView != null) {
            rootView.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()) {
                IBinder windowToken = rootView.getWindowToken();
                if(windowToken != null) {
                    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            rootView.clearFocus();
        }
    }

    @Override
    public void initValue(StockDbEntity stockEntity) {
        if(mIsViewCreated) {
            initFragmentValue(stockEntity);
        } else {
            mStockEntity = stockEntity;
        }
    }

    protected abstract void initFragmentValue(StockDbEntity stockEntity);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mStockEntity != null) {
            initFragmentValue(mStockEntity);
        }
        mIsViewCreated = true;
    }

    @Override
    public int checkInput(StockDbEntity stockEntity) {
        if(mIsViewCreated) {
            return checkFragment(stockEntity);
        }
        return Submitable.RESULT_NOCHANGE;
    }

    @Override
    public int submit(StockDbEntity stockEntity) {
        if(mIsViewCreated) {
            int result = checkFragment(stockEntity);
            if(result == Submitable.RESULT_SHOULD_SUBMIT) {
                submitFragment(stockEntity);
            }
            return result;
        }
        return Submitable.RESULT_NOCHANGE;
    }

    protected abstract int checkFragment(StockDbEntity stockEntity);
    protected abstract void submitFragment(StockDbEntity stockEntity);

    protected boolean floatEquals(float src, float target) {
        if(src > 0 || target > 0) {
            return src == target;
        } else {
            return true;
        }
    }
}
