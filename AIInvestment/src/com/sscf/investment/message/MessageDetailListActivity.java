package com.sscf.investment.message;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.message.widget.BaseMessageRemindAdapter;
import com.sscf.investment.message.widget.ImportantNewsMessageListAdapter;
import com.sscf.investment.message.widget.InteractMessageListAdapter;
import com.sscf.investment.message.widget.MessageRemindAdapter;
import com.sscf.investment.message.manager.MessageRequestManager;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.message.widget.NewsMessageListAdapter;
import com.sscf.investment.message.widget.PortfolioDailyRemindAdapter;
import com.sscf.investment.message.widget.StockPriceRemindAdapter;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.setting.manager.RemindRequestManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ConfirmDialog;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import java.util.ArrayList;
import java.util.HashSet;
import BEC.AlertMsgClassDetailRsp;
import BEC.E_MSG_CLASS;
import BEC.PushData;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * davidwei
 * 提醒消息列表
 */
public final class MessageDetailListActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback,
        DataSourceProxy.IRequestCallback, CommonRecyclerViewAdapter.OnFloatingViewStateChangedListener,
        PtrHandler, RecyclerViewManager.OnLoadMoreListener {
    private static final int PAGE_SIZE = 20;

    private static final int MSG_UPDATE_DATA = 1;
    private static final int MSG_UPDATE_FAILED = 2;
    Handler mHandler;

    @BindView(R.id.ptr) PtrFrameLayout mPtrFrame;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.dateSticky) TextView mDateSticky;
    @BindView(R.id.loading_layout) View mLoadingLayoutCenter;
    @BindView(R.id.fail_retry) View mFailRetryLayoutCenter;
    @BindView(R.id.emptyView) TextView mEmptyView;
    @BindDimen(R.dimen.default_tab_height) int mStickyHeight;
    @BindView(R.id.actionbar_right_button) TextView mClearButton;
    private CommonRecyclerViewAdapter mAdapter;
    RecyclerViewManager mRecyclerViewManager;

    ArrayList mItemsList = null;
    HashSet<RemindedMessageItem> mItemsSet;
    int mStartOffset;

    int mClassId;

    private Dialog mClearConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int classId = getIntent().getIntExtra(DengtaConst.EXTRA_TYPE, -1);
        if (classId < 0) {
            finish();
            return;
        }

        mClassId = classId;
        setContentView(R.layout.message_common_list);
        ButterKnife.bind(this);
        initViews();
        mHandler = new Handler(this);
        requestData();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(getIntent().getStringExtra(DengtaConst.EXTRA_TITLE));
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mClearButton.setOnClickListener(this);
        mClearButton.setText(R.string.clear_all);
        if (DengtaApplication.getApplication().getAccountManager().getAccountId() <= 0) {
            mClearButton.setVisibility(View.GONE);
        }

        mPtrFrame.setPtrHandler(this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final CommonRecyclerViewAdapter adapter = getAdapter();
        if (adapter == null) {
            return;
        }

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = adapter;
        adapter.setFloatingViewCallback(mRecyclerView, BaseMessageRemindAdapter.TYPE_DATE, mStickyHeight, this);

        mEmptyView.setText(R.string.blank_content);

        mRecyclerViewManager = new RecyclerViewManager(this, mRecyclerView, linearLayoutManager,
                adapter, mLoadingLayoutCenter, mFailRetryLayoutCenter, mEmptyView, this);

        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_LOADING);
    }

    private CommonRecyclerViewAdapter getAdapter() {
        CommonRecyclerViewAdapter adapter = null;
        switch (mClassId) {
            case E_MSG_CLASS.E_MC_DISC_NEWS:
                adapter = new ImportantNewsMessageListAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_ANN_REP:
                adapter = new NewsMessageListAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_SEC_PRICE:
                adapter = new StockPriceRemindAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_DAILY_REPORT:
                adapter = new PortfolioDailyRemindAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_TG:
            case E_MSG_CLASS.E_MC_INTERACTION:
                adapter = new InteractMessageListAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_NEW_STOCK:
                adapter = new MessageRemindAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_ACTIVITY:
                adapter = new MessageRemindAdapter(this);
                break;
            case E_MSG_CLASS.E_MC_VALUE_ADDED_SERVICE:
                adapter = new MessageRemindAdapter(this);
                break;
            default:
                break;
        }
        return adapter;
    }

    @Override
    public void onFloatingViewStateChanged(int translationY, Object itemData) {
        if (itemData == null) { // 没有数据的时候隐藏
            mDateSticky.setVisibility(View.GONE);
        } else {
            mDateSticky.setVisibility(View.VISIBLE);
            mDateSticky.setTranslationY(translationY);
            mDateSticky.setText(itemData.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                showClearConfirmDialog();
                break;
            case R.id.ok:
                dismissClearConfirmDialog();

                final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
                if (accountInfo == null) {
                    finish();
                    return;
                }

                if (NetUtil.isNetWorkConnected(this)) {
                    RemindRequestManager.clearClassRemindList(accountInfo.ticket, accountInfo.id, mClassId, this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            return RecyclerViewHelper.isOnTop(mRecyclerView);
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(final PtrFrameLayout frame) {
        refresh();
    }

    private void refresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
            return;
        }

        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_FAILED, DengtaConst.REFRESH_TIME_OUT);
        mStartOffset = 0;
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    private void requestData() {
        MessageRequestManager.getMsgDetailListRequest(mClassId, mStartOffset, PAGE_SIZE, this);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_MSG_DETAIL_LIST:
                getListCallback(success, data);
                break;
            case EntityObject.ET_REMIND_CLEAR_LIST:
                clearClassListCallback(success, data);
                break;
            default:
                break;
        }
    }

    private void getListCallback(boolean success, EntityObject data) {
        if (success) {
            final AlertMsgClassDetailRsp rsp = (AlertMsgClassDetailRsp) data.getEntity();

            final ArrayList<PushData> datas = rsp.vPushData;
            HashSet<RemindedMessageItem> itemsSet = null;

            final int size = datas == null ? 0 : datas.size();

            ArrayList itemsList = null;
            if (mStartOffset == 0) { // 下拉取第一页

                itemsSet = new HashSet<RemindedMessageItem>(size);
                itemsList = new ArrayList(size);

                RemindRequestManager.decodePushDataListWithDate(datas, itemsList, itemsSet);

                mStartOffset = size;

                mItemsList = itemsList;
                mItemsSet = itemsSet;
                mHandler.obtainMessage(MSG_UPDATE_DATA, new ArrayList<RemindedMessageItem>(itemsList)).sendToTarget();
                if (size <= 0) {
                    mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NO_CONTENT);
                } else if (size < PAGE_SIZE && size > 0) {
                    mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                    mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
                } else {
                    mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
                    mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NORMAL);
                }
            } else { // 上拉取后一页
                if (size <= 0) { // 没有更多的了
                    mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                    return;
                }

                itemsList = mItemsList;
                itemsSet = mItemsSet;

                RemindRequestManager.decodePushDataListWithDate(datas, itemsList, itemsSet);
                mStartOffset += size;
                // new ArrayList防止出现java.lang.IllegalStateException: The content of the adapter has changed but ListView did not receive a notification.
                mHandler.obtainMessage(MSG_UPDATE_DATA, new ArrayList<RemindedMessageItem>(itemsList)).sendToTarget();
            }
        } else {
            if (mStartOffset == 0) { // 下拉取第一页
                mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            } else { // 上拉取后一页
                mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            }
        }
    }

    private void clearClassListCallback(boolean success, EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof Integer) {
                switch ((Integer) entity) {
                    case 0: // 清除成功
                        mHandler.sendEmptyMessage(MSG_UPDATE_DATA);
                        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NO_CONTENT);
                        break;
                    default:
                        break;
                }
                return;
            }
        }
        mHandler.sendEmptyMessage(MSG_UPDATE_FAILED);
    }

    @Override
    public boolean handleMessage(Message msg) {
        mPtrFrame.refreshComplete();
        switch (msg.what) {
            case MSG_UPDATE_DATA:
                updateList((ArrayList) msg.obj);
                mPtrFrame.refreshComplete();
                mHandler.removeMessages(MSG_UPDATE_FAILED);
                break;
            case MSG_UPDATE_FAILED:
                DengtaApplication.getApplication().showToast(R.string.error_tips_network_connect_error);
                mHandler.removeMessages(MSG_UPDATE_FAILED);
                break;
            default:
                break;
        }
        return true;
    }

    private void updateList(ArrayList list) {
        final int size = list == null ? 0 : list.size();
        if (size <= 0) {
            mDateSticky.setVisibility(View.GONE);
        }
        mAdapter.setListData(list);
        mAdapter.notifyDataSetChanged();
        if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
            mClearButton.setVisibility(size > 0 ? View.VISIBLE :  View.GONE);
        }
    }

    private void showClearConfirmDialog() {
        if (isDestroy()) {
            return;
        }

        if (mClearConfirmDialog == null) {
            final ConfirmDialog dialog = new ConfirmDialog(this);
            dialog.setMessage(R.string.setting_reminded_message_clear_confirm_msg);
            dialog.setOkButton(R.string.ok, this);
            mClearConfirmDialog = dialog;
        }
        mClearConfirmDialog.show();
    }

    private void dismissClearConfirmDialog() {
        if (mClearConfirmDialog != null) {
            mClearConfirmDialog.dismiss();
        }
    }

    public static void show(final Context context, final int classId, final String title) {
        final Intent intent = new Intent(context, MessageDetailListActivity.class);
        intent.putExtra(DengtaConst.EXTRA_TYPE, classId);
        intent.putExtra(DengtaConst.EXTRA_TITLE, title);
        context.startActivity(intent);
    }
}
