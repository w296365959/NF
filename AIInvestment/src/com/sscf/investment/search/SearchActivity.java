package com.sscf.investment.search;

import BEC.BEACON_STAT_TYPE;
import BEC.CommonSearchReq;
import BEC.CommonSearchRsp;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.stat.TimeStatHelper;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.search.fragment.SearchHistoryFragment;
import com.sscf.investment.search.fragment.SearchResultFragment;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.widget.OperationButtonEditTextListener;
import com.sscf.investment.widget.keyboard.KeyBoardHelper;

/**
 * davidwei
 * 搜索界面
 */
@Route("SearchActivity")
public final class SearchActivity extends BaseFragmentActivity implements View.OnClickListener, TextWatcher, DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final long SEND_REQUEST_DELAY = 500L;

    private static final int MSG_SEND_SEARCH_REQUEST = 1;
    private static final int MSG_UPDATE_SEARCH_RESULT = 2;

    private TextView mCancelButton;
    private EditText mSearchInputview;
    private KeyBoardHelper mKeyBoardHelper;
    private int mInputBottom;

    private Handler mMainHandler;

    private SearchResultFragment mSearchResultFragment;
    private SearchHistoryFragment mSearchHistoryFragment;
    private Fragment mCurrentFragment;

    private String mInputWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        initViews();
        mMainHandler = new Handler(this);

        showSearchHistoryFragment();
        StatisticsUtil.reportAction(StatisticsConst.SEARCH_DISPLAY);
    }

    @Override
    protected TimeStatHelper createTimeStatHelper() {
        return new TimeStatHelper(BEACON_STAT_TYPE.E_BST_SEARCH_PAGE, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainHandler.removeMessages(MSG_UPDATE_SEARCH_RESULT);
        DeviceUtil.hideInputMethod(mSearchInputview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mSearchInputview);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float y = ev.getY();
                if (mInputBottom <= 0) {
                    mInputBottom = findViewById(R.id.input_bar).getHeight() + DeviceUtil.getStatusBarHeight(this);
                }
                int keyboardTop = mKeyBoardHelper.getTop();

                if (y > mInputBottom) {
                    DeviceUtil.hideInputMethod(mSearchInputview);
                    if(keyboardTop == 0 || y < keyboardTop) {
                        mKeyBoardHelper.hideLastKeyboard();
                    }
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if(!mKeyBoardHelper.onBackPressed()) {
            finish();
        }
    }

    private void initViews() {
        mSearchInputview = (EditText) findViewById(R.id.searchInput);
        mKeyBoardHelper = new KeyBoardHelper(this, mSearchInputview);
        mSearchInputview.addTextChangedListener(this);
        mCancelButton = (TextView) findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(this);
        final View searchInputButton = findViewById(R.id.operation);
        new OperationButtonEditTextListener(this, mSearchInputview, searchInputButton);
    }

    private void showSearchResultFragment() {
        if (mSearchResultFragment == null) {
            mSearchResultFragment = new SearchResultFragment();
            addFragment(mSearchResultFragment);
        }
        showFragment(mSearchResultFragment);
    }

    private void showSearchHistoryFragment() {
        if (mSearchHistoryFragment == null) {
            mSearchHistoryFragment = new SearchHistoryFragment();
            addFragment(mSearchHistoryFragment);
        }
        showFragment(mSearchHistoryFragment);
    }

    private void addFragment(final Fragment fragment) {
        if (isDestroy() || isFinishing()) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.searchFragmentContainer, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragment.setUserVisibleHint(true);
    }

    private void showFragment(final Fragment fragment) {
        if (fragment == mCurrentFragment) {
            return;
        }

        if (isDestroy() || isFinishing()) {
            return;
        }

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            ft.hide(mCurrentFragment);
        }
        ft.show(fragment).addToBackStack(null).commitAllowingStateLoss();
        mCurrentFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.cancelButton:
                DeviceUtil.hideInputMethod(mSearchInputview);
                mMainHandler.postDelayed(() -> finish(), 100);
                break;
            default:
                break;
        }
    }

    /**
     * 向后台发送搜索请求
     */
    private static void sendSearchRequest(final String keyword, final DataSourceProxy.IRequestCallback observer) {
        final CommonSearchReq req = new CommonSearchReq();
        req.sUserInputStr = keyword;
        req.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        DataEngine.getInstance().request(EntityObject.ET_SEARCH, req, observer);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        DtLog.d(TAG, "afterTextChanged s : " + s);
        mInputWord = mSearchInputview.getText().toString().trim();

        mMainHandler.removeMessages(MSG_SEND_SEARCH_REQUEST);
        if (mInputWord.length() > 0) {
            mMainHandler.sendMessageDelayed(mMainHandler.obtainMessage(MSG_SEND_SEARCH_REQUEST, mInputWord), SEND_REQUEST_DELAY);
        } else {
            showSearchHistoryFragment();
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            if (data.getEntityType() != EntityObject.ET_SEARCH) {
                return;
            }
            if (TextUtils.isEmpty(mInputWord)) {
                return;
            }
            final CommonSearchRsp searchRsp = (CommonSearchRsp) data.getEntity();
            mMainHandler.obtainMessage(MSG_UPDATE_SEARCH_RESULT, searchRsp).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SEND_SEARCH_REQUEST:
                sendSearchRequest((String) msg.obj, this);
                break;
            case MSG_UPDATE_SEARCH_RESULT:
                updateSearchResult((CommonSearchRsp) msg.obj);
                break;
            default:
                break;
        }
        return true;
    }

    private void updateSearchResult(final CommonSearchRsp searchRsp) {
        showSearchResultFragment();
        mSearchResultFragment.updateResult(mInputWord, searchRsp);
    }
}