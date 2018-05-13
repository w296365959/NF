package com.sscf.investment.portfolio;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.InputDialog;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.db.GroupEntity;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.BottomEditLayout;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import com.sscf.investment.widget.ConfirmCheckBoxDialog;
import com.mobeta.android.dslv.DragSortListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidwei on 2016/06/17.
 *
 * 自选股编辑界面
 */
public final class PortfolioGroupEditActivity extends BaseFragmentActivity implements View.OnClickListener,
        DragSortListView.ItemCheckedChangedListener, BottomEditLayout.OnEditOperationListener {
    private static final String TAG = PortfolioGroupEditActivity.class.getSimpleName();

    private DragSortListView mListView;
    private PortfolioStockEditAdapter mAdapter;
    private List<GroupEntity> mData;
    private BottomEditLayout mEditLayout;

    private DragSortListView.DropListener onDrop =
            (from, to) -> {
                if (from != to) {
                    changePosition(from, to);
                    StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_EDIT_SUCCESS);
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
        setContentView(R.layout.activity_portfolio_group_edit_list);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));

        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.portfolio_edit_group);
        findViewById(R.id.actionbar_cancel_button).setOnClickListener(this);

        mListView = (DragSortListView) findViewById(android.R.id.list);

        // init footer
        final TextView footerView = new TextView(this);
        footerView.setText(R.string.portfolio_edit_group_tips);
        footerView.setTextColor(ContextCompat.getColor(this, R.color.default_text_color_60));
        footerView.setGravity(Gravity.CENTER);
        footerView.setMinHeight(getResources().getDimensionPixelSize(R.dimen.default_tab_height));
        mListView.addFooterView(footerView);

        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager != null) {
            final List<GroupEntity> groupList = portfolioDataManager.getAllGroup(false, true);
            mData = groupList;
        }

        final PortfolioStockEditAdapter adapter = new PortfolioStockEditAdapter(this, mData,
                R.layout.activity_portfolio_edit_group_list_item);
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
        if (from != to) {
            final int min = Math.min(from, to);
            final int max = Math.max(from, to) + 1;
            final int size = max - min;

            ArrayList<Integer> changedBeforeTimes = new ArrayList<Integer>();

            for (GroupEntity item : mData.subList(min, max)) {
                changedBeforeTimes.add(item.getUpdateSortTime());
            }

            GroupEntity item = mData.remove(from);
            mData.add(to, item);
            mListView.moveCheckState(from, to);
            mAdapter.notifyDataSetChanged();

            ArrayList<GroupEntity> changedAfterItems = new ArrayList<GroupEntity>(size);
            changedAfterItems.addAll(mData.subList(min, max));

            for (int i = 0; i < size; i++) {
                changedAfterItems.get(i).setUpdateSortTime(changedBeforeTimes.get(i));
            }

            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null) {
                portfolioDataManager.updateGroupList(changedAfterItems);
            }
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

    public void showDeleteDialog() {
        final ConfirmCheckBoxDialog deleteDialog = new ConfirmCheckBoxDialog(this);
        deleteDialog.setMessage(R.string.portfolio_delete_group_msg);
        deleteDialog.setCheckBoxText(R.string.portfolio_delete_group_check_msg);
        deleteDialog.setOkButton(R.string.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                deleteGroups(deleteDialog.isCheck());
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_EDIT_SUCCESS);
            }
        });
        deleteDialog.show();
    }

    private void deleteGroups(final boolean isDeleleStock) {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }
        final SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();
        final int size = checkedPositions.size();

        int position;
        boolean checked;
        for (int i = size - 1; i >= 0; i--) { // 倒着删除
            position = checkedPositions.keyAt(i);
            checked = checkedPositions.valueAt(i);
            if (checked) {
                List<GroupEntity> data = mData;
                if(data != null && position >= 0 && position < data.size()) {
                    portfolioDataManager.delGroup(data.get(position).getCreateTime(), isDeleleStock, true);
                }
            }
        }

        final List<GroupEntity> groupList = portfolioDataManager.getAllGroup(false, true);
        if (groupList != null && groupList.size() > 0) {
            // 刷新自选列表
            mListView.clearChoices();
            mAdapter.setData(groupList);
            mAdapter.notifyDataSetChanged();
            mData = groupList;
        } else {
            finish(); // 全部删除后退出界面
        }
    }

    @Override
    public void onItemCheckedChanged(int position, boolean value) {
        mEditLayout.setEditState(mAdapter.getCount(), mListView.getCheckedItemCount());
    }

    private final class PortfolioStockEditAdapter extends CommonAdapter<GroupEntity> implements View.OnClickListener {
        public PortfolioStockEditAdapter(Context context, List<GroupEntity> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, GroupEntity item, int position) {
            final TextView titleView = holder.getView(R.id.portfolioEditTitle);
            titleView.setText(item.getName());

            final View stickView = holder.getView(R.id.portfolioEditStick);
            stickView.setTag(holder);
            stickView.setOnClickListener(this);

            final View renameView = holder.getView(R.id.portfolioEditRename);
            renameView.setTag(item);
            renameView.setOnClickListener(this);
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
                        StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_EDIT_SUCCESS);
                    }
                    break;
                case R.id.portfolioEditRename:
                    showRenameDialog((GroupEntity) v.getTag());
                    break;
                default:
                    break;
            }
        }
    }

    private void showRenameDialog(final GroupEntity item) {
        final InputDialog inputDialog = new InputDialog(this);
        final int MAX_INPUT_LENGTH = 12;
        inputDialog.setMaxInputLength(MAX_INPUT_LENGTH);
        inputDialog.setTitle(R.string.portfolio_rename_group);
        inputDialog.setInputText(item.getName());
        inputDialog.setInputHint(R.string.portfolio_add_group_hint);
        inputDialog.setOkButton(R.string.ok, v -> {
            final String srcName = item.getName();
            final String destName = inputDialog.getInputText();
            inputDialog.dismiss();

            if (TextUtils.equals(srcName, destName)) {
                return;
            }
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());

            if (portfolioDataManager == null) {
                return;
            }
            final int checkResult = portfolioDataManager.checkGroupNameValid(destName);

            final DengtaApplication application = DengtaApplication.getApplication();
            if (checkResult == IPortfolioDataManager.GROUP_NAME_VALID) {
                portfolioDataManager.modifyGroupName(item.getCreateTime(), destName);
                final List<GroupEntity> groupList = portfolioDataManager.getAllGroup(false, true);
                // 刷新自选列表
                mAdapter.setData(groupList);
                mAdapter.notifyDataSetChanged();
                mData = groupList;
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_EDIT_SUCCESS);
            } else if (checkResult == IPortfolioDataManager.GROUP_NAME_INVALID_TOO_LONG) {
                application.showToast(R.string.portfolio_group_name_too_long);
            } else if (checkResult == IPortfolioDataManager.GROUP_NAME_INVALID_ALREADY_EXIST) {
                application.showToast(R.string.portfolio_group_name_already_exist);
            } else if (checkResult == IPortfolioDataManager.GROUP_NAME_INVALID_CHAR_NOT_ALLOWED) {
                application.showToast(R.string.portfolio_group_name_invalid_char);
            }
        });
        inputDialog.show();
    }
}
