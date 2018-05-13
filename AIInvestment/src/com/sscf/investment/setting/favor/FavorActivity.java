package com.sscf.investment.setting.favor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseArray;
import android.view.View;
import android.widget.*;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IFavorManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.db.FavorItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.widget.*;
import java.util.ArrayList;
import java.util.List;
import BEC.E_NEWS_TYPE;

/**
 * davidwei
 * 用户信息界面
 */
@Route("FavorActivity")
public final class FavorActivity extends BaseFragmentActivity implements View.OnClickListener, BottomEditLayout.OnEditOperationListener {
    private static final int MODE_NORMAL = 1;
    private static final int MODE_EDIT = 2;

    private int mMode = MODE_NORMAL;

    private ListView mFavorListView;
    private View mBackButton;
    private View mEditButton;
    private View mCancelButton;
    private BottomEditLayout mFavorDeleteLayout;
    private FavorAdapter mFavorAdapter;
    private IFavorManager mFavorManager;

    private FavorChangedReceiver mFavorChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavorManager = (IFavorManager) ComponentManager.getInstance().getManager(IFavorManager.class.getName());
        if (mFavorManager == null) {
            return;
        }
        setContentView(R.layout.activity_setting_favor);
        initViews();

        updateFavorList();

        registerFavorReceiver();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_FAVOR_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterFavorReceiver();
    }

    private void registerFavorReceiver() {
        if (mFavorChangedReceiver == null) {
            mFavorChangedReceiver = new FavorChangedReceiver();
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication())
                    .registerReceiver(mFavorChangedReceiver, new IntentFilter(SettingConst.ACTION_FAVOR_CHANGED));
        }
    }

    private void unregisterFavorReceiver() {
        if (mFavorChangedReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mFavorChangedReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        if (switchMode(MODE_NORMAL)) {
            return;
        }
        super.onBackPressed();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_favor_title);

        mBackButton = findViewById(R.id.actionbar_back_button);
        mBackButton.setOnClickListener(this);
        mEditButton = findViewById(R.id.actionbar_edit_button);
        mEditButton.setOnClickListener(this);
        mCancelButton = findViewById(R.id.actionbar_cancel_button);
        mCancelButton.setOnClickListener(this);

        mFavorDeleteLayout = (BottomEditLayout) findViewById(R.id.bottomEditLayout);
        mFavorDeleteLayout.setOnEditOperationListener(this);
        mFavorDeleteLayout.setVisibility(View.GONE);

        mFavorListView = (ListView) findViewById(android.R.id.list);
        mFavorListView.setEmptyView(findViewById(R.id.emptyView));
    }

    private void updateFavorList() {
        ArrayList<FavorItem> favorItems = mFavorManager.getFavorItems();
        if (favorItems == null) {
            favorItems = new ArrayList<FavorItem>(0);
        }

        if (favorItems.size() == 0) {
            mEditButton.setVisibility(View.GONE);
        }

        if (mFavorAdapter == null) {
            final FavorAdapter adapter = new FavorAdapter(FavorActivity.this, favorItems, R.layout.activity_setting_favor_item);
            mFavorListView.setAdapter(adapter);
            mFavorAdapter = adapter;
        } else {
            mFavorAdapter.setData(favorItems);
            mFavorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_edit_button:
                switchMode(MODE_EDIT);
                break;
            case R.id.actionbar_cancel_button:
                switchMode(MODE_NORMAL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelectAll() {
        if (mFavorAdapter != null) {
            mFavorAdapter.selectAll();
        }
    }

    @Override
    public void onSelectNone() {
        if (mFavorAdapter != null) {
            mFavorAdapter.selectNone();
        }
    }

    @Override
    public void onDelete() {
        if (isDestroy()) {
            return;
        }
        final ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setMessage(R.string.setting_favor_delete_confirm_tips);
        dialog.setOkButton(R.string.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavorAdapter != null) {
                    mFavorAdapter.deleteCheckedItem();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean switchMode(int mode) {
        if (mMode != mode) {
            mMode = mode;
            if (mFavorAdapter != null) {
                mFavorAdapter.switchMode(mode);
            }
            switch (mode) {
                case MODE_NORMAL:
                    mBackButton.setVisibility(View.VISIBLE);
                    mEditButton.setVisibility(View.VISIBLE);
                    mCancelButton.setVisibility(View.GONE);
                    mFavorDeleteLayout.setVisibility(View.GONE);
                    break;
                case MODE_EDIT:
                    mBackButton.setVisibility(View.GONE);
                    mEditButton.setVisibility(View.GONE);
                    mCancelButton.setVisibility(View.VISIBLE);
                    mFavorDeleteLayout.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            return true;
        }
        return false;
    }

    private final class FavorChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_FAVOR_CHANGED.equals(action)) {
                updateFavorList();
            }
        }
    }

    public final class FavorAdapter extends CommonAdapter<FavorItem>
            implements View.OnClickListener, View.OnLongClickListener {
        private final SparseArray<FavorItem> mCheckedItems;

        public FavorAdapter(Context context, List<FavorItem> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
            mCheckedItems = new SparseArray<FavorItem>(datas.size());
        }

        @Override
        public void convert(CommonViewHolder holder, FavorItem item, int position) {
            final CheckedTextView checkBox = holder.getView(R.id.favorCheckBox);

            final View favorItemLayout = holder.getView(R.id.favorItemLayout);
            favorItemLayout.setTag(holder);
            favorItemLayout.setOnClickListener(this);
            favorItemLayout.setOnLongClickListener(this);

            final Resources res = getResources();

            switch (mMode) {
                case MODE_NORMAL:
                    checkBox.setVisibility(View.GONE);
                    favorItemLayout.setPadding(res.getDimensionPixelSize(R.dimen.favor_item_padding_left_normal),
                            favorItemLayout.getPaddingTop(), favorItemLayout.getPaddingRight(), favorItemLayout.getPaddingBottom());
                    break;
                case MODE_EDIT:
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(mCheckedItems.indexOfKey(position) >= 0);
                    favorItemLayout.setPadding(res.getDimensionPixelSize(R.dimen.favor_item_padding_left_edit),
                            favorItemLayout.getPaddingTop(), favorItemLayout.getPaddingRight(), favorItemLayout.getPaddingBottom());
                    break;
                default:
                    break;
            }

            TextView titleView = holder.getView(R.id.favorTitle);
            titleView.setText(item.getTitle());

            TextView publishTimeView = holder.getView(R.id.favorPublishTime);
            publishTimeView.setText(TimeUtils.timeStamp2Date(item.getFavorTime()));
        }

        @Override
        public void onClick(View v) {
            final CommonViewHolder holder = (CommonViewHolder) v.getTag();
            switch (mMode) {
                case MODE_NORMAL:
                    final FavorItem favorItem = getItem(holder.getPosition());

                    if (favorItem.getFavorType() == E_NEWS_TYPE.NT_NEWS_PAGE) {
                        WebBeaconJump.showThirdPartyNews(mContext, favorItem.getThirdUrl(), favorItem);
                    } else {
                        WebBeaconJump.showWebActivity(mContext, favorItem.getInfoUrl(), favorItem);
                    }
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_FAVOR_LIST_CLICK_ITEM);
                    break;
                case MODE_EDIT:
                    final CheckedTextView checkBox = holder.getView(R.id.favorCheckBox);
                    final boolean checked = !checkBox.isChecked();
                    final int position = holder.getPosition();
                    if (checked) {
                        mCheckedItems.put(position, getItem(position));
                    } else {
                        mCheckedItems.remove(position);
                    }
                    onItemCheckedChanged();
                    checkBox.setChecked(checked);
                    break;
                default:
                    break;
            }
        }

        private void onItemCheckedChanged() {
            mFavorDeleteLayout.setEditState(getCount(), mCheckedItems.size());
        }

        @Override
        public boolean onLongClick(View v) {
            if (FavorActivity.this.switchMode(MODE_EDIT)) {
                final CommonViewHolder holder = (CommonViewHolder) v.getTag();
                final CheckedTextView checkBox = holder.getView(R.id.favorCheckBox);
                checkBox.setChecked(true);
                final int position = holder.getPosition();
                mCheckedItems.put(position, getItem(position));
                onItemCheckedChanged();
            }
            return true;
        }

        public void switchMode(int mode) {
            if (mode == MODE_EDIT) {
                mCheckedItems.clear();
                onItemCheckedChanged();
            }
            notifyDataSetChanged();
        }

        public void selectAll() {
            final int count = getCount();
            for (int i = 0; i < count; i++) {
                mCheckedItems.put(i, getItem(i));
            }
            onItemCheckedChanged();
            notifyDataSetChanged();
        }

        public void selectNone() {
            mCheckedItems.clear();
            onItemCheckedChanged();
            notifyDataSetChanged();
        }

        public void deleteCheckedItem() {
            final int size = mCheckedItems.size();
            if (size <= 0) {
                return;
            }

            FavorItem item = null;
            for (int i = 0; i < size; i++) {
                item = mCheckedItems.valueAt(i);
                mFavorManager.deleteFavor(item);
            }
            mCheckedItems.clear();
            onItemCheckedChanged();
            FavorActivity.this.switchMode(MODE_NORMAL);
            updateFavorList();
        }
    }
}