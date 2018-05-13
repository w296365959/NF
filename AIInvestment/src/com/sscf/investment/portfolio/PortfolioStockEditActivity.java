package com.sscf.investment.portfolio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.setting.LoginActivity;
import com.sscf.investment.stare.ui.SmartStockStareActivity;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.BottomEditLayout;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import com.sscf.investment.widget.ConfirmCheckBoxDialog;
import com.sscf.investment.widget.ConfirmDialog;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuebinliu on 2015/8/5.
 *
 * 自选股编辑界面
 */
public final class PortfolioStockEditActivity extends BaseFragmentActivity implements View.OnClickListener,
        DragSortListView.ItemCheckedChangedListener, BottomEditLayout.OnEditOperationListener {
    private static final String TAG = PortfolioStockEditActivity.class.getSimpleName();

    private DragSortListView mListView;
    private PortfolioStockEditAdapter mAdapter;
    private List<StockDbEntity> mData;
    private BottomEditLayout mEditLayout;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        changePosition(from, to);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_portfolio_edit_list);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {// 设置提醒以后需要刷新，改变icon
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null && portfolioDataManager.getCurrentGroup() != null) { // 不是默认分组，就需要更新StockDbEntity的数据
                final List<StockDbEntity> stockList = portfolioDataManager.getAllStockFromCurrentGroup(false, true);
                mAdapter.setData(stockList);
                mData = stockList;
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.portfolio_edit_stock_title);
        findViewById(R.id.actionbar_cancel_button).setOnClickListener(this);

        mListView = (DragSortListView) findViewById(android.R.id.list);

        // init footer
        final TextView footerView = new TextView(this);
        footerView.setText(R.string.portfolio_edit_stock_tips);
        footerView.setTextColor(ContextCompat.getColor(this, R.color.default_text_color_60));
        footerView.setGravity(Gravity.CENTER);
        footerView.setMinHeight(getResources().getDimensionPixelSize(R.dimen.default_tab_height));
        mListView.addFooterView(footerView);

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        final List<StockDbEntity> stockList = portfolioDataManager.getAllStockFromCurrentGroup(false, true);
        mData = stockList;

        final PortfolioStockEditAdapter adapter = new PortfolioStockEditAdapter(this, stockList,
                R.layout.activity_portfolio_edit_stock_list_item);
        mListView.setAdapter(adapter);
        mAdapter = adapter;

        mListView.setDropListener(onDrop);
        mListView.setItemCheckedChangedListener(this);

        mEditLayout = (BottomEditLayout) findViewById(R.id.bottomEditLayout);
        mEditLayout.setOnEditOperationListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_cancel_button:
                finish();
                break;
            default:
                break;
        }
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
        showDeleteDialog();
    }

    private void changePosition(int from, int to) {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }
        if (from != to) {
            final int min = Math.min(from, to);
            final int max = Math.max(from, to) + 1;
            final int size = max - min;
            final List<StockDbEntity> data = mData;
            if (min >= data.size()) {
                return;
            }

            ArrayList<Integer> changedBeforeTimes = new ArrayList<>();

            for (StockDbEntity item : data.subList(min, max)) {
                changedBeforeTimes.add(item.getIUpdateTime());
            }

            StockDbEntity item = data.remove(from);
            data.add(to, item);
            mListView.moveCheckState(from, to);
            mAdapter.notifyDataSetChanged();

            ArrayList<StockDbEntity> changedAfterItems = new ArrayList<StockDbEntity>(size);
            changedAfterItems.addAll(data.subList(min, max));

            for (int i = 0; i < size; i++) {
                changedAfterItems.get(i).setIUpdateTime(changedBeforeTimes.get(i));
            }

            portfolioDataManager.updateStockList(changedAfterItems);
        }
    }

    private void selectAllItem() {
        final int size = mData.size();
        for (int i = 0; i < size; i++) {
            if (!mListView.isItemChecked(i)) {
                mListView.setItemChecked(i, true);
            }
        }
    }

    private void selectNoneItem() {
        final int size = mData.size();
        for (int i = 0; i < size; i++) {
            if (mListView.isItemChecked(i)) {
                mListView.setItemChecked(i, false);
            }
        }
    }

    private void showDeleteDialog() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }

        final SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();
        final int size = checkedPositions.size();
        int position;
        boolean checked;
        final List<StockDbEntity> data = mData;
        final int dataSize = data == null ? 0 : data.size();

        final ArrayList<String> delArray = new ArrayList<String>();
        for (int i = size - 1; i >= 0; i--) { // 倒着删除
            position = checkedPositions.keyAt(i);
            checked = checkedPositions.valueAt(i);
            if (checked && position < dataSize) {
                delArray.add(data.get(position).getDtSecCode());
            }
        }

        if (delArray.size() == 0) {
            return;
        }

        final GroupEntity groupEntity = portfolioDataManager.getCurrentGroup();

        if (groupEntity == null) { // 默认分组
            final ConfirmDialog deleteDialog = new ConfirmDialog(this);
            deleteDialog.setMessage(R.string.portfolio_edit_confirm_delete_tips1);
            deleteDialog.setOkButton(R.string.delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    deleteStockItems(delArray, true);
                }
            });
            deleteDialog.show();
        } else {
            final ConfirmCheckBoxDialog deleteDialog = new ConfirmCheckBoxDialog(this);
            deleteDialog.setMessage(R.string.portfolio_edit_confirm_delete_tips2);
            deleteDialog.setCheckBoxText(R.string.portfolio_edit_confirm_delete_tips3);
            deleteDialog.setOkButton(R.string.delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    deleteStockItems(delArray, deleteDialog.isCheck());
                }
            });
            deleteDialog.show();
        }
    }

    private void deleteStockItems(final ArrayList<String> delArray, boolean deleteAll) {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }

        if (deleteAll) {
            portfolioDataManager.deleteStockFromAllGroup(delArray, true);
        } else {
            portfolioDataManager.deletePortfolioStockFromCurrentGroup(delArray, true);
        }

        // 在编辑列表里面删除
        for (String code : delArray) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getDtSecCode().equalsIgnoreCase(code)) {
                    mData.remove(i);
                    break;
                }
            }
        }

        // 刷新自选列表
        mListView.clearChoices();
        mAdapter.notifyDataSetChanged();

        // 如果编辑的时候删除了所有自选，则退出编辑界面
        checkFinish();
    }

    private void checkFinish() {
        final int size = mData != null ? mData.size() : 0;
        if (size == 0) {
            finish();
        }
    }

    @Override
    public void onItemCheckedChanged(int position, boolean value) {
        mEditLayout.setEditState(mAdapter.getCount(), mListView.getCheckedItemCount());
    }

    private final class PortfolioStockEditAdapter extends CommonAdapter<StockDbEntity> implements View.OnClickListener {
        public PortfolioStockEditAdapter(Context context, List<StockDbEntity> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, StockDbEntity item, int position) {
            final TextView titleView = holder.getView(R.id.portfolioEditTitle);
            titleView.setText(item.getSzName());

            final TextView codeView = holder.getView(R.id.code);
            codeView.setText(StockUtil.convertSecInfo(item.getDtSecCode()).getSSecCode());

            final View stickView = holder.getView(R.id.portfolioEditStick);
            stickView.setTag(holder);
            stickView.setOnClickListener(this);

            final View remindView = holder.getView(R.id.portfolioEditRemind);
            remindView.setTag(holder);
            remindView.setSelected(StockUtil.isSetRemind(item));
            remindView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CommonViewHolder holder = null;
            switch (v.getId()) {
                case R.id.portfolioEditStick:
                    holder = (CommonViewHolder) v.getTag();
                    final int position = holder.getPosition();
                    if (position != 0) {
                        changePosition(position, 0);
                    }
                    break;
                case R.id.portfolioEditRemind:
                    if (!DengtaApplication.getApplication().getAccountManager().isLogined()) {
                        startActivity(new Intent(PortfolioStockEditActivity.this, LoginActivity.class));
                        return;
                    }

                    holder = (CommonViewHolder) v.getTag();
                    final StockDbEntity item = getItem(holder.getPosition());
                    final String dtSecCode = item.getDtSecCode();
                    final String name = item.getSzName();
                    final Intent intent;
                    if(StockUtil.supportSmartStare(dtSecCode)) {
                        intent = new Intent(PortfolioStockEditActivity.this, SmartStockStareActivity.class);
                        intent.putExtra(DengtaConst.KEY_SEC_CODE, dtSecCode);
                        intent.putExtra(DengtaConst.KEY_SEC_NAME, name);
                    } else {
                        intent = new Intent(PortfolioStockEditActivity.this, PortfolioRemindActivity.class);
                        intent.putExtra(DengtaConst.KEY_SEC_CODE, dtSecCode);
                    }
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
