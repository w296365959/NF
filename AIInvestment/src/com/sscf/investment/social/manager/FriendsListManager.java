package com.sscf.investment.social.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.message.manager.RecyclerViewManager;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.social.FriendsRequestManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import BEC.E_FEED_USER_TYPE;
import BEC.FeedUserBaseInfo;
import BEC.GetUserRelationRsp;
import BEC.SetUserRelationRsp;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * davidwei
 * 好友列表界面管理器，adapter
 */
public final class FriendsListManager extends CommonRecyclerViewAdapter implements View.OnClickListener,
        DataSourceProxy.IRequestCallback, Handler.Callback, PtrHandler, RecyclerViewManager.OnLoadMoreListener {
    static final int MSG_UPDATE_LIST = 1;
    static final int MSG_FAILED = 2;

    final LongSparseArray<Boolean> mAttentionsStates;
    final int mType;
    final long mAccountId;

    final DisplayImageOptions mDefaultOptions;
    final Handler mHandler;

    final View mRootView;
    final PtrFrameLayout mPtrLayout;
    final RecyclerView mRecyclerView;
    final RecyclerViewManager mRecyclerViewManager;
    String mStartId = "";
    ArrayList<FeedUserBaseInfo> mList;

    public FriendsListManager(Context context, LongSparseArray<Boolean> attentionsStates, int type, long accountId) {
        super(context);

        mAttentionsStates = attentionsStates;
        mAccountId = accountId;
        mType = type;
        mHandler = new Handler(this);
        mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);

        final View root = View.inflate(context, R.layout.common_ptr_recycler_view, null);
        mRootView = root;
        final PtrFrameLayout ptrLayout = (PtrFrameLayout) root.findViewById(R.id.ptr);
        final RecyclerView recyclerView = (RecyclerView) ptrLayout.findViewById(R.id.recyclerview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(this);
        recyclerView.setItemAnimator(null);

        mRecyclerView = recyclerView;
        mPtrLayout = ptrLayout;
        final View loadingLayoutCenter = root.findViewById(R.id.loading_layout);
        final View failRetryLayoutCenter = root.findViewById(R.id.fail_retry);
        failRetryLayoutCenter.setOnClickListener(this);
        final TextView emptyView = (TextView) root.findViewById(R.id.emptyView);
        emptyView.setText(type == CommonConst.TYPE_ATTENTION ? R.string.friend_attentions_empty : R.string.friend_fans_empty);

        ptrLayout.setPtrHandler(this);

        mRecyclerViewManager = new RecyclerViewManager(context, mRecyclerView, linearLayoutManager,
                this, loadingLayoutCenter, failRetryLayoutCenter, emptyView, this);

        mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_LOADING);
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new FriendHolder(mInflater.inflate(R.layout.social_friends_list_item, parent, false));
    }

    final class FriendHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.iconV) ImageView iconV;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.message) TextView message;
        @BindView(R.id.setAttentionButton) TextView setAttentionButton;

        public FriendHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final FeedUserBaseInfo item = (FeedUserBaseInfo) itemData;
            ImageLoaderUtils.getImageLoader().displayImage(item.sFaceUrl, icon, mDefaultOptions);
            iconV.setVisibility(item.eUserType == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V ? View.VISIBLE : View.GONE);
            title.setText(item.sNickName);
            final boolean isMember = AccountManager.isMember(item);
            title.setTextColor(ContextCompat.getColor(mContext, isMember ?
                    R.color.member_user_name_color : R.color.default_text_color_100));
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, isMember ? R.drawable.vip_icon : 0, 0);
            message.setText(item.sProfile);
            final long myAccountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
            setAttentionButton.setVisibility(item.iAccountId == myAccountId ? View.INVISIBLE : View.VISIBLE);
            if (getState(item)) {
                setAttentionButton.setText(R.string.attended);
                setAttentionButton.setBackgroundResource(R.drawable.attended_button_bg);
                setAttentionButton.setTextColor(mContext.getResources().getColor(R.color.text_color_attended));
            } else {
                setAttentionButton.setText(R.string.attend);
                setAttentionButton.setBackgroundResource(R.drawable.attend_button_bg);
                setAttentionButton.setTextColor(mContext.getResources().getColor(R.color.text_color_attend));
            }
        }

        @Override
        public void onItemClicked() {
            final FeedUserBaseInfo item = (FeedUserBaseInfo) mItemData;
            if (item != null) {
                CommonBeaconJump.showHomepage(mContext, item.iAccountId);
            }
        }

        @OnClick(R.id.setAttentionButton)
        public void setAttention() {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }

            final long myAccountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
            if (myAccountId <= 0) {
                CommonBeaconJump.showLogin(mContext);
            } else {
                final FeedUserBaseInfo item = (FeedUserBaseInfo) mItemData;
                if (item != null) {
                    new AttentionManager(item, getLayoutPosition()).setAttention();
                }
            }
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
        if (NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            mStartId = "";
            requestData();
        } else {
            handleFailed();
        }
    }

    boolean getState(final FeedUserBaseInfo item) {
        return getState(item.iAccountId);
    }

    boolean getState(long id) {
        Boolean state = mAttentionsStates.get(id);
        if (state == null) {
            state = false;
        }
        return state;
    }

    void setState(final Map<Long, Integer> relations) {
        final Iterator<Map.Entry<Long, Integer>> it = relations.entrySet().iterator();
        Map.Entry<Long, Integer> entry = null;
        Long userId = null;
        Integer state = null;
        while (it.hasNext()) {
            entry = it.next();
            userId = entry.getKey();
            if (userId != null) {
                state = entry.getValue();
                mAttentionsStates.put(userId, state != null && state == 1);
            }
        }
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    @Override
    public void onClick(View v) {
        mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_LOADING);
        mStartId = "";
        requestData();
    }

    public void requestData() {
        switch (mType) {
            case CommonConst.TYPE_ATTENTION:
                FriendsRequestManager.getAttentionsListRequest(mAccountId, mStartId, this);
                break;
            case CommonConst.TYPE_FANS:
                FriendsRequestManager.getFansListRequest(mAccountId, mStartId, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success && data != null) {
            mHandler.obtainMessage(MSG_UPDATE_LIST, data).sendToTarget();
        } else {
            if (TextUtils.isEmpty(mStartId)) { // 下拉取第一页
                mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            } else { // 上拉取后一页
                mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_FAILED_RETRY);
            }
        }
    }

    @Override
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_LIST:
                updateList((EntityObject) msg.obj);
                mPtrLayout.refreshComplete();
                break;
            case MSG_FAILED:
                handleFailed();
                break;
        }
        return true;
    }

    private void handleFailed() {
        DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
        mPtrLayout.refreshComplete();
    }

    private void updateList(final EntityObject data) {
        final String startId = (String) data.getExtra();
        if (!TextUtils.equals(startId, mStartId)) {
            return;
        }

        final GetUserRelationRsp rsp = (GetUserRelationRsp) data.getEntity();
        setState(rsp.mRelation);
        final ArrayList<FeedUserBaseInfo> list = rsp.stFeedUserRelation.vFeedUserBaseInfo;
        final int size = list == null ? 0 : list.size();

        if (TextUtils.isEmpty(mStartId)) {
            mList = list;
            if (size <= 0) {
                mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NO_CONTENT);
            } else if (rsp.iStatus == 0) {
                mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
                mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
            } else {
                mRecyclerViewManager.showRecyclerViewByState(DengtaConst.UI_STATE_NORMAL);
                mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NORMAL);
            }
        } else {
            if (size <= 0) {
                mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                return;
            }
            if (rsp.iStatus == 0) {
                mRecyclerViewManager.showFooterViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
            }
            mList.addAll(list);
        }
        mStartId = rsp.sNextStartId;
        setListData(mList);
        notifyDataSetChanged();
    }

    final class AttentionManager implements DataSourceProxy.IRequestCallback, Handler.Callback {
        private static final int MSG_UPDATE_ITEM = 1;
        private static final int MSG_SHOW_TOAST = 2;

        final long userId;
        final int position;
        final Handler mHandler;
        final boolean attended;

        public AttentionManager(final FeedUserBaseInfo item, final int position) {
            this.userId = item.iAccountId;
            this.position = position;
            attended = getState(item);
            mHandler = new Handler(Looper.getMainLooper(), this);
        }

        public void setAttention() {
            if (attended) {
                FriendsRequestManager.cancelAttendRequest(userId, this);
            } else {
                FriendsRequestManager.attendRequest(userId, this);
            }
        }

        @Override
        public void callback(boolean success, EntityObject data) {
            if (success && data.getEntity() != null) {
                final SetUserRelationRsp rsp = (SetUserRelationRsp) data.getEntity();
                if (rsp.iRetCode == 0) {
                    mHandler.sendEmptyMessage(MSG_UPDATE_ITEM);
                    return;
                }
            }
            mHandler.sendEmptyMessage(MSG_SHOW_TOAST);
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_TOAST:
                    DengtaApplication.getApplication().showToast(attended ? "关注失败" : "取消关注失败");
                    break;
                case MSG_UPDATE_ITEM:
                    mAttentionsStates.put(userId, !attended);
                    notifyItemChanged(position);
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
