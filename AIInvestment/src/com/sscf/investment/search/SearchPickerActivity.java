package com.sscf.investment.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.entity.db.SearchHistoryItem;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.widget.keyboard.KeyBoardHelper;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import BEC.CommonSearchReq;
import BEC.PlateInfo;
import BEC.SecInfo;

/**
 * davidwei
 * 搜索界面
 */
@Route("SearchPickerActivity")
public final class SearchPickerActivity extends BaseFragmentActivity implements View.OnClickListener, TextWatcher, DataSourceProxy.IRequestCallback, Handler.Callback {
    private static final String TAG = SearchPickerActivity.class.getSimpleName();
    private static final long SEND_REQUEST_DELAY = 500L;

    private static final int MSG_SEND_SEARCH_REQUEST = 1;
    private static final int MSG_UPDATE_SEARCH_RESULT = 2;

    private TextView mCancelButton;
    private EditText mSearchInputview;
    private KeyBoardHelper mKeyBoardHelper;
    private int mInputBottom;
    private RecyclerView mRecyclerView;

    private Handler mMainHandler;

    private String mInputWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_picker);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        initViews();
        mMainHandler = new Handler(this);
        DengtaApplication.getApplication().defaultExecutor.execute(() ->
                mMainHandler.obtainMessage(MSG_UPDATE_SEARCH_RESULT, SearchHistoryItem.findAllItemFromDb()).sendToTarget());
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
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void initViews() {
        mSearchInputview = (EditText) findViewById(R.id.searchInput);
        mKeyBoardHelper = new KeyBoardHelper(this, mSearchInputview);
        mSearchInputview.addTextChangedListener(this);
        mCancelButton = (TextView) findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(this);
//        new OperationButtonEditTextListener(this, mSearchInputview);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.cancelButton:
                DeviceUtil.hideInputMethod(mSearchInputview);
                mMainHandler.postDelayed(() -> {
                    setResult(RESULT_CANCELED);
                    finish();
                }, 100);
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
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (TextUtils.isEmpty(mInputWord)) {
            return;
        }
        final ArrayList<com.dengtacj.thoth.Message> resultList = EntityUtil.entityToSearchResultList(success, data);
        if (resultList != null) {
            String name;
            String dtSecCode;
            SearchHistoryItem item;
            ArrayList<SearchHistoryItem> items = new ArrayList<>();
            for (com.dengtacj.thoth.Message result : resultList) {
                if (result instanceof SecInfo) {
                    final SecInfo secInfo = (SecInfo) result;
                    name = secInfo.sCHNShortName;
                    dtSecCode = secInfo.sDtSecCode;
                }  else if (result instanceof PlateInfo) {
                    final PlateInfo plateInfo = (PlateInfo) result;
                    name = plateInfo.sPlateName;
                    dtSecCode = plateInfo.sDtSecCode;
                } else {
                    continue;
                }
                item = new SearchHistoryItem();
                item.setName(name);
                item.setUnicode(dtSecCode);
                items.add(item);
            }
            mMainHandler.obtainMessage(MSG_UPDATE_SEARCH_RESULT, items).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SEND_SEARCH_REQUEST:
                sendSearchRequest((String) msg.obj, this);
                break;
            case MSG_UPDATE_SEARCH_RESULT:
                updateSearchResult((ArrayList<SearchHistoryItem>) msg.obj);
                break;
            default:
                break;
        }
        return true;
    }

    private void updateSearchResult(final ArrayList<SearchHistoryItem> stockItems) {
        mRecyclerView.setAdapter(new StockItemAdapter(this, stockItems));
    }

    final class StockItemAdapter extends CommonBaseRecyclerViewAdapter<SearchHistoryItem> {

        public StockItemAdapter(Context context, List<SearchHistoryItem> data) {
            super(context, data, R.layout.activity_search_picker_item);
            setItemClickable(true);
        }

        @Override
        public void convert(CommonRecyclerViewHolder holder, SearchHistoryItem item, int position) {
            final String name = item.getName();
            final String dtSecCode = item.getUnicode();
            final String secCode = StockUtil.getSecCode(dtSecCode);

            final TextView tagView = holder.getView(R.id.tag);
            final String tagText = StockUtil.getSearchTagText(dtSecCode);
            if (TextUtils.isEmpty(tagText)) {
                tagView.setVisibility(View.INVISIBLE);
            } else {
                tagView.setVisibility(View.VISIBLE);
                tagView.setText(tagText);
            }

            holder.getView(R.id.notSupport).setVisibility(StockUtil.
                    supportCustomStockCompare(dtSecCode) ? View.INVISIBLE : View.VISIBLE);

            final TextView nameView = holder.getView(R.id.name);
            nameView.setText(name);
            final TextView codeView = holder.getView(R.id.code);
            codeView.setText(secCode);
        }

        @Override
        protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
            final SearchHistoryItem item = getItem(position);
            if (item == null) {
                return;
            }

            final String dtSecCode = item.getUnicode();
            if (!StockUtil.supportCustomStockCompare(dtSecCode)) {
                return;
            }

            final Intent intent = new Intent();
            intent.putExtra(CommonConst.KEY_SEC_CODE, dtSecCode);
            Bundle extra = getIntent().getBundleExtra(CommonConst.EXTRA_SEARCH_PICK);
            if(extra != null) {
                intent.putExtra(CommonConst.EXTRA_SEARCH_PICK, extra);
            }
            setResult(RESULT_OK, intent);
            finish();
            DengtaApplication.getApplication().defaultExecutor.execute(
                    () -> SearchHistoryItem.addItemToDb(item.getName(), dtSecCode));
        }
    }
}