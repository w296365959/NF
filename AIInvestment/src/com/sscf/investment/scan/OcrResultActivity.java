package com.sscf.investment.scan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ocr.OcrCallback;
import com.sscf.investment.component.ocr.OcrManager;
import com.sscf.investment.component.ocr.entity.OcrStockResult;
import com.sscf.investment.component.ocr.entity.Stock;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.MainActivity;
import com.sscf.investment.portfolio.PortfolioGroupManagerActivity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.BottomEditLayout;
import com.sscf.investment.widget.BottomEditLayout.OnEditOperationListener;
import com.sscf.investment.widget.CheckableListView;
import com.sscf.investment.widget.CheckableListView.OnItemCheckedChangedListener;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/5/23.
 */

public class OcrResultActivity extends BaseFragmentActivity implements View.OnClickListener, OcrCallback, OnEditOperationListener, OnItemCheckedChangedListener {

    private static final String TAG = OcrResultActivity.class.getSimpleName();

    public static final String ACTION_SHOW_IMPORT_RESULT = "action_show_import_result";

    public static final String EXTRA_IMAGE_URI = "image_uri";
    private static final int REQUEST_GROUP_CODE = 100;

    private Uri mUri;

    private TextView mActionbarTitleView;
    private View mResultPanel;
    private View mFailedView;
    private CheckableListView mListView;
    private BottomEditLayout mEditLayout;
    private ArrayList<Stock> mStockList;
    private OcrStockListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getIntent().getParcelableExtra(EXTRA_IMAGE_URI);

        if(mUri != null) {
            setContentView(R.layout.activity_orc_result);
            initView();
            DtLog.d("OcrManager", "onCreate()");
            OcrManager.get().setCallback(this);
            OcrManager.get().startOcrTask(mUri);
        } else {
            finish();
        }
    }

    private void initView() {
        mActionbarTitleView = (TextView) findViewById(R.id.actionbar_title);
        mActionbarTitleView.setText(R.string.stock_ocr);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mResultPanel = findViewById(R.id.result_panel);
        mFailedView = findViewById(R.id.ocr_failed);
        mListView = (CheckableListView) findViewById(R.id.list);
        mListView.setItemCheckedChangedListener(this);
        mEditLayout = (BottomEditLayout) findViewById(R.id.bottomEditLayout);
        mEditLayout.setOnEditOperationListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrManager.get().release();
    }

    private void selectAllItem() {
        if(mStockList != null) {
            final int size = mStockList.size();
            for (int i = 0; i < size; i++) {
                if (!mListView.isItemChecked(i)) {
                    mListView.setItemChecked(i, true);
                }
            }
        }
    }

    private void selectNoneItem() {
        final int size = mStockList.size();
        for (int i = 0; i < size; i++) {
            if (mListView.isItemChecked(i)) {
                mListView.setItemChecked(i, false);
            }
        }
    }

    @Override
    public void onOcrStart() {
        DtLog.d(TAG, "onOcrStart()");
        showLoadingDialog();
    }

    @Override
    public void onCompressSuccess() {
        DtLog.d(TAG, "onCompressSuccess()");
    }

    @Override
    public void onRequestStart() {
        DtLog.d(TAG, "onRequestStart()");
    }

    @Override
    public void onRequestSuccess(String rsp) {
        DtLog.d(TAG, "onRequestSuccess()");
        OcrStockResult result = new OcrStockResult(rsp);
        ArrayList<Stock> stockList = result.getStockList();
        runOnUiThread(() -> {
            mActionbarTitleView.setText(R.string.ocr_result);
            if(stockList != null && !stockList.isEmpty()) {
                StatisticsUtil.reportAction(StatisticsConst.OCR_RESULT_SUCCESS);
                showResultView(stockList);
            } else {
                showFailedView();
            }
        });
    }

    @Override
    public void onError(int error) {
        DtLog.d(TAG, "onError() error = " + error);
        runOnUiThread(() -> {
            mActionbarTitleView.setText(R.string.ocr_result);
            showFailedView();
        });

    }

    private void showResultView(ArrayList<Stock> stockList) {
        dismissLoadingDialog();
        mResultPanel.setVisibility(View.VISIBLE);
        mStockList = stockList;
        mAdapter = new OcrStockListAdapter(this, stockList);
        mListView.setAdapter(mAdapter);
        selectAllItem();
    }

    private void showFailedView() {
        dismissLoadingDialog();
        mFailedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectAll() {
        selectAllItem();
    }

    @Override
    public void onSelectNone() {
        selectNoneItem();
    }

    @Override
    public void onDelete() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if(mStockList != null && !mStockList.isEmpty()) {
            if(portfolioDataManager.getAllGroup(false, false).size() > 0) {
                PortfolioGroupManagerActivity.show(this, REQUEST_GROUP_CODE);
            } else {
                saveAndGoMain();
            }
        }
    }

    @Override
    public void onItemCheckedChanged(int position, boolean value) {
        mEditLayout.setEditState(mAdapter.getCount(), mListView.getCheckedItemCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            default:
        }
    }

    private class OcrStockListAdapter extends CommonAdapter<Stock> {

        public OcrStockListAdapter(Context context, List data) {
            super(context, data, R.layout.ocr_stock_item);
        }

        @Override
        public void convert(CommonViewHolder holder, Stock item, int position) {
            final TextView titleView = holder.getView(R.id.portfolioEditTitle);
            titleView.setText(item.getSecName());

            final TextView codeView = holder.getView(R.id.code);
            codeView.setText(StockUtil.convertSecInfo(item.getDtSecCode()).getSSecCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GROUP_CODE) {
            if(data != null) {
                saveAndGoMain(data.getIntExtra("groupId", -1));
            }
        }
    }

    private void saveAndGoMain() {
        List<Stock> stockList = getSelectedStockList();
        if(stockList != null && !stockList.isEmpty()) {
            if(checkStockCount(stockList.size())) {
                final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                        .getManager(IPortfolioDataManager.class.getName());
                if (portfolioDataManager != null) {
                    for(Stock stock : stockList) {
                        portfolioDataManager.addStock(stock.getDtSecCode(), stock.getSecName());
                    }
                }
                DengtaApplication.getApplication().showToast(R.string.import_stock_success);
                startMainActivity();
                finish();
            } else {
                DengtaApplication.getApplication().showToast(R.string.no_more_portfolio);
            }
        }
    }

    private boolean checkStockCount(int newCount) {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        final List<StockDbEntity> allStockList = portfolioDataManager == null ? null : portfolioDataManager.getAllStockList(false, false);
        final int size = allStockList == null ? 0 : allStockList.size();
        return size + newCount <= DengtaConst.MAX_PORTFOLIO_COUNT;
    }

    private void saveAndGoMain(int groupId) {
        if(groupId >= 0) {
            List<Stock> stockList = getSelectedStockList();
            if(stockList != null && !stockList.isEmpty()) {
                if(checkStockCount(stockList.size())) {
                    final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                            .getManager(IPortfolioDataManager.class.getName());
                    if (portfolioDataManager != null) {
                        for(Stock stock : stockList) {
                            portfolioDataManager.addStock2Group(groupId, stock.getDtSecCode(), stock.getSecName());
                        }
                        portfolioDataManager.changeCurrentGroup(groupId);
                    }
                    DengtaApplication.getApplication().showToast(R.string.import_stock_success);
                    startMainActivity();
                    finish();
                } else {
                    DengtaApplication.getApplication().showToast(R.string.no_more_portfolio);
                }
            }
        }
    }

    private List<Stock> getSelectedStockList() {
        final SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();
        final int size = checkedPositions.size();
        int position;
        boolean checked;
        final List<Stock> stockList = mStockList;
        final int dataSize = stockList == null ? 0 : stockList.size();

        final ArrayList<Stock> selectedArray = new ArrayList<Stock>();
        for (int i = size - 1; i >= 0; i--) {
            position = checkedPositions.keyAt(i);
            checked = checkedPositions.valueAt(i);
            if (checked && position < dataSize) {
                selectedArray.add(stockList.get(position));
            }
        }
        return selectedArray;
    }

    private void startMainActivity() {
        // 这里启动的context一定要传递activity，否则在6.0及以前的手机上，以下设置的flag将会无效
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ACTION_SHOW_IMPORT_RESULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
