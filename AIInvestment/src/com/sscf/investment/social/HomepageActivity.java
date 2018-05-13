package com.sscf.investment.social;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.event.DeleteCommentEvent;
import com.dengtacj.component.event.DeleteFeedEvent;
import com.dengtacj.component.event.DoLikeByWebEvent;
import com.dengtacj.component.event.GetFeedResultEvent;
import com.dengtacj.component.event.PostCommentResultEvent;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.UserInfoActivity;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.widgt.RoundImageView;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.NumberTextView;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.widget.recyclerview.CommonFooterView;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.sscf.investment.widget.recyclerview.EndlessRecyclerOnScrollListener;
import com.sscf.investment.widget.recyclerview.RecyclerViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;
import BEC.AccountTicket;
import BEC.E_FEED_GROUP_TYPE;
import BEC.E_FEED_TYPE;
import BEC.E_FEED_USER_TYPE;
import BEC.FeedExtendInfo;
import BEC.FeedItem;
import BEC.FeedUserBaseInfo;
import BEC.GetFeedListReq;
import BEC.GetFeedListRsp;
import BEC.GetFeedUserInfoReq;
import BEC.GetFeedUserInfoRsp;
import BEC.GetRelationBatchRsp;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by liqf on 2016/9/13.
 */
@Route("HomepageActivity")
public final class HomepageActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private static final String TAG = "HomepageActivity";

    @BindView(R.id.ptr) PtrFrameLayout mPtrFrame;

    //ActionBar区域
    @BindDimen(R.dimen.actionbar_height) int mActionBarHeight;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;
    View mActionBarTitleLayout;
    @BindView(R.id.actionbar_back_button) View mActionBarBackButton;
    @BindView(R.id.actionbar_right_button) TextView mActionBarRightButton;
    @BindColor(R.color.actionbar_bg) int mActionBarBg;

    //Header区域
    RoundImageView mFace;
    ImageView mUserIconV;
    TextView mUserLoginName;
    ImageView mUserMemberStatus;
    NumberTextView mFollowCount;
    NumberTextView mFansCount;
    TextView mUserDescription;
    @BindView(R.id.homepage_header_sticky) View mHeaderSticky;

    //SelectBar区域
    FrameLayout mSelectBar;
    int mSelectBarTop;
    @BindDimen(R.dimen.homepage_select_bar_height) int mSelectBarHeight;
    @BindView(R.id.select_bar_sticky) FrameLayout mSelectBarSticky;
    TextView mSelectTitle;
    TextView mSelectTitleSticky;
    TextView mSelectBtn;
    TextView mSelectBtnSticky;

    //RecyclerView区域
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    private FrameLayout mHeaderView;
    private CommonFooterView mFooterView;

    //加关注区域
    @BindView(R.id.set_attention_layout) FrameLayout mSetAttentionLayout;
    @BindView(R.id.setAttentionButton) Button mSetAttentionButton;
    @BindColor(R.color.text_color_attended) int mColorHasAttention;
    @BindDrawable(R.drawable.attended_button_bg) Drawable mAttendedButtonBg;
    @BindDrawable(R.drawable.attend_button_bg) Drawable mAttendButtonBg;
    @BindColor(R.color.text_color_attend) int mColorNoAttention;
    @BindDimen(R.dimen.homepage_set_attention_bar_height) int mBottomBarHeight;

    private LinearLayoutManager mLayoutManager;
    private FeedRecyclerViewAdapter mAdapter;

    private ArrayList<FeedItem> mFeedList = new ArrayList<>();

    private int mViewState = DengtaConst.UI_STATE_NORMAL;

    public static final int REFRESH_TYPE_UP = 1;
    public static final int REFRESH_TYPE_DOWN = 2;
    public static final int UPDATE_INTERVAL = 5 * 60 * 1000; //页面切回来时如果超过5分钟就刷新一次

    private static final int DIRECTION_PULL_NEWER = 0;
    private static final int DIRECTION_PULL_OLDER = 1;

    private static final int STATUS_NO_MORE = 0;
    private static final int STATUS_HAS_MORE = 1;

    private Handler mUiHandler = new Handler();

    private long mAccountId = 0;
    private boolean mIsSelf = false;
    private boolean mIsInvestUser = false;
    private boolean mHasAttention = false;
    private int mRequestFeedType = E_FEED_TYPE.E_FT_ALL;
    private FeedUserBaseInfo mBaseInfo;

    private boolean mIsCreating = true;

    private DisplayImageOptions mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(
            R.drawable.default_consultant_face);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_homepage);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            mAccountId = intent.getLongExtra(DengtaConst.EXTRA_ACCOUNT_ID, 0);
            mIsSelf = mAccountId == DengtaApplication.getApplication().getAccountManager().getAccountId();
        }

        initViews();

        loadData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsCreating) {
            mIsCreating = false;
        } else {
            long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
            if (accountId == mAccountId) { //回到自己的主页时刷新用户信息，以反映最新编辑的结果
                loadFeedUserInfo();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadFeedList(final int refreshType) {
        DtLog.d(TAG, "loadFeedList: refreshType = " + refreshType);
        if (refreshType == REFRESH_TYPE_DOWN) {
            showViewByState(DengtaConst.UI_STATE_LOADING);
        }

        String startId = "", newestId = "", oldestId = "";
        int count = mFeedList.size();
        if (count > 0) {
            newestId = mFeedList.get(0).getStFeedContent().getSFeedId();
            oldestId = mFeedList.get(count - 1).getStFeedContent().getSFeedId();
        }

        int direction = DIRECTION_PULL_NEWER;
        switch (refreshType) {
            case REFRESH_TYPE_UP:
                startId = /*newestId*/""; //如果顶部刷新不清缓存只需注释掉此行
                direction = DIRECTION_PULL_NEWER;
                break;
            case REFRESH_TYPE_DOWN:
                startId = oldestId;
                direction = DIRECTION_PULL_OLDER;
                break;
            default:
                break;
        }

        GetFeedListReq req = new GetFeedListReq();
        req.setESelfType(mRequestFeedType);
        req.setEFeedGroupType(E_FEED_GROUP_TYPE.E_FGT_SELF);
        req.setStUserInfo(DengtaApplication.getApplication().getAccountManager().getUserInfo());
        req.setSStartFeedId(startId);
        req.setIDirection(direction);
        req.setIOtherAccountId(mIsSelf ? 0 : mAccountId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DengtaConst.KEY_REFRESH_TYPE, refreshType);
            jsonObject.put(DengtaConst.KEY_LAST_OLDEST_ID, oldestId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED_LIST, req, this, jsonObject.toString());
    }

    private void loadFeedUserInfo() {
        GetFeedUserInfoReq req = new GetFeedUserInfoReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setIOtherAccountId(mIsSelf ? 0 : mAccountId);
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED_USER_INFO, req, this);
    }

    private void onLoadComplete(final boolean success) {
        mUiHandler.post(() -> {
            mPtrFrame.refreshComplete();

            final boolean hasContent = mAdapter.getNormalItemCount() > 0;
            int viewState;
            if (hasContent) {
                viewState = success ? DengtaConst.UI_STATE_NORMAL : DengtaConst.UI_STATE_FAILED_RETRY;
            } else {
                viewState = success ? DengtaConst.UI_STATE_NO_CONTENT : DengtaConst.UI_STATE_FAILED_RETRY;
            }
            showViewByState(viewState);
        });
    }

    private void initViews() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        mAdapter = new FeedRecyclerViewAdapter(this, mRecyclerView, true, mAccountId, false);
        mRecyclerView.setAdapter(mAdapter);

        LayoutInflater inflater = LayoutInflater.from(this);
        mHeaderView = (FrameLayout) inflater.inflate(R.layout.recyclerview_header_footer_framelayout, mRecyclerView, false);
        inflater.inflate(R.layout.homepage_header, mHeaderView);
        mSelectTitle = ButterKnife.findById(mHeaderView, R.id.top_title);
        ButterKnife.findById(mHeaderView, R.id.actionbar_title_layout).setVisibility(View.GONE);
        mActionBarIcon = ButterKnife.findById(mHeaderSticky, R.id.actionbar_icon);
        mActionBarTitle = ButterKnife.findById(mHeaderSticky, R.id.actionbar_title);
        mActionBarTitleLayout = ButterKnife.findById(mHeaderSticky, R.id.actionbar_title_layout);
        ButterKnife.findById(mHeaderView, R.id.actionbar_back_button).setOnClickListener(this);
        ButterKnife.findById(mHeaderView, R.id.actionbar_right_button).setOnClickListener(this);
        ButterKnife.findById(mHeaderView, R.id.follow_layout).setOnClickListener(this);
        ButterKnife.findById(mHeaderView, R.id.fans_layout).setOnClickListener(this);
        ((TextView) ButterKnife.findById(mHeaderView, R.id.actionbar_right_button)).setText(R.string.user_infos);

        mFace = ButterKnife.findById(mHeaderView, R.id.face);
        mUserIconV = ButterKnife.findById(mHeaderView, R.id.user_icon_v);
        mUserLoginName = ButterKnife.findById(mHeaderView, R.id.user_login_name);
        mUserMemberStatus = ButterKnife.findById(mHeaderView, R.id.user_member_status);
        mFollowCount = ButterKnife.findById(mHeaderView, R.id.follow_count);
        mFansCount = ButterKnife.findById(mHeaderView, R.id.fans_count);
        mUserDescription = ButterKnife.findById(mHeaderView, R.id.user_description);

        final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        final AccountInfoExt accountInfoExt = accountManager.getAccountInfoExt();
        if (accountInfoExt != null) {
            final AccountInfoEntity accountInfo = accountInfoExt.accountInfo;
            if (accountInfo != null) {
                final long accountId = accountManager.getAccountId();
                if (accountId == mAccountId) {
                    mUserLoginName.setText(accountInfo.nickname);
                    mUserMemberStatus.setImageResource(accountInfoExt.isMember() ? R.drawable.vip_icon : R.drawable.vip_none_icon);
                    mUserDescription.setText(accountInfoExt.profile);
                    ImageLoaderUtils.getImageLoader().displayImage(accountInfo.iconUrl, mFace, mDefaultOptions);
                }
            }
        }

        mActionBarRightButton.setText(R.string.user_infos);
        mActionBarBackButton.setOnClickListener(this);
        mActionBarRightButton.setOnClickListener(this);
        mAdapter.setHeaderView(mHeaderView);

        mSelectBar = (FrameLayout) mHeaderView.findViewById(R.id.select_bar);
        mSelectBtn = (TextView) mSelectBar.findViewById(R.id.select);
        mSelectBtn.setOnClickListener(this);
        mSelectBarSticky.setVisibility(View.GONE);
        mSelectBtnSticky = (TextView) mSelectBarSticky.findViewById(R.id.select);
        mSelectBtnSticky.setOnClickListener(this);
        mSelectTitleSticky = (TextView) mSelectBarSticky.findViewById(R.id.top_title);

        initFooterView();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //加载更多功能的代码
                DtLog.d(TAG, "onScroll: onScrollStateChanged !!!need bottom refresh!!!");
                if (mViewState != DengtaConst.UI_STATE_NO_MORE_CONTENT) {
                    showViewByState(DengtaConst.UI_STATE_LOADING);
                    loadFeedList(REFRESH_TYPE_DOWN);
                }
            }
        });

        mHeaderSticky.setBackgroundColor(mActionBarBg);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View topChild = mLayoutManager.getChildAt(0);
                int headerTop = topChild == mHeaderView ? mHeaderView.getTop() : -mHeaderView.getHeight();
//                DtLog.d(TAG, "onScrolled: headerTop = " + headerTop);
//                DtLog.d(TAG, "onScrolled: headerHeight = " + mHeaderView.getHeight());

                //刷新筛选栏
                if (mSelectBarTop == 0) {
                    mSelectBarTop = mSelectBar.getTop();
                }
                if (mSelectBarTop > 0) {
                    DtLog.d(TAG, "onScrolled: mSelectBarTop = " + mSelectBarTop);
                    if (-headerTop + mActionBarHeight >= mSelectBarTop) {
                        mSelectBarSticky.setVisibility(View.VISIBLE);
                    } else {
                        mSelectBarSticky.setVisibility(View.GONE);
                    }
                }

                //刷新ActionBar
                int selectBarShowingHeight = mSelectBarHeight;
                int remainedTopHeight = mHeaderView.getHeight() - selectBarShowingHeight + headerTop;
                DtLog.d(TAG, "onScrolled: remainedTopHeight = " + remainedTopHeight);
                if (remainedTopHeight <= mActionBarHeight) {
//                    mActionBarTitleLayout.setVisibility(View.VISIBLE);
                    mHeaderSticky.setVisibility(View.VISIBLE);

                    if (mBaseInfo != null) {
                        ImageLoaderUtils.getImageLoader().displayImage(mBaseInfo.getSFaceUrl(), mActionBarIcon, mDefaultOptions);
                        if (!TextUtils.equals(mActionBarTitle.getText(), mBaseInfo.getSNickName())) {
                            mActionBarTitle.setText(mBaseInfo.getSNickName());
                        }
                    }
                } else {
//                    mActionBarTitleLayout.setVisibility(View.GONE);
                    mHeaderSticky.setVisibility(View.GONE);
//                    mActionBarTitle.setText("");
                }
            }
        });

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    return RecyclerViewHelper.isOnTop(mRecyclerView);
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                DtLog.d(TAG, "onRefreshBegin: onUIRefresh");
                doRefresh();
            }
        });

        initAttentionLayout();
    }

    private void initAttentionLayout() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPtrFrame.getLayoutParams();
        if (!mIsSelf) {
            mSetAttentionLayout.setVisibility(View.VISIBLE);
            layoutParams.bottomMargin = mBottomBarHeight;
            refreshAttentionButton();
        } else {
            mSetAttentionLayout.setVisibility(View.GONE);
            layoutParams.bottomMargin = 0;
        }
        mPtrFrame.setLayoutParams(layoutParams);
    }

    private void initFooterView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mFooterView = (CommonFooterView) inflater.inflate(R.layout.common_footer_view_layout, mRecyclerView, false);
        mFooterView.setFailRetryListener(() -> loadFeedList(REFRESH_TYPE_DOWN));
        mAdapter.setFooterView(mFooterView);
    }

    private void doRefresh() {
        if (!NetUtil.isNetWorkConnected(DengtaApplication.getApplication())) {
            onLoadComplete(false);
            DengtaApplication.getApplication().showToast(R.string.network_invalid_please_check);
            return;
        }

        loadData();
    }

    private void loadData() {
        if (DengtaApplication.getApplication().getAccountManager().isLogined() && !mIsSelf) {
            FriendsRequestManager.getRelation(mAccountId, this);
        }
        loadFeedUserInfo();
        loadFeedList(REFRESH_TYPE_UP);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @OnClick(R.id.setAttentionButton)
    public void onAttentionLayoutClicked() {
        if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
            if (!mHasAttention) {
                mHasAttention = true;
                FriendsRequestManager.attendRequest(mAccountId, this);
            } else {
                mHasAttention = false;
                FriendsRequestManager.cancelAttendRequest(mAccountId, this);
            }
            refreshAttentionButton();
        } else {
            CommonBeaconJump.showLogin(this);
        }
    }

    private void refreshAttentionButton() {
        if (mHasAttention) {
            mSetAttentionButton.setText(R.string.attended);
            mSetAttentionButton.setTextColor(mColorHasAttention);
            mSetAttentionButton.setBackground(mAttendedButtonBg);
        } else {
            mSetAttentionButton.setText(R.string.attend);
            mSetAttentionButton.setTextColor(mColorNoAttention);
            mSetAttentionButton.setBackground(mAttendButtonBg);
        }
    }

    private void showViewByState(int state) {
        DtLog.d(TAG, "showViewByState: state = " + state);
        final boolean hasContent = mAdapter.getNormalItemCount() > 0;

        switch (state) {
            case DengtaConst.UI_STATE_NORMAL:
                if (hasContent) {
                    mFooterView.setState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                } else {
                    mFooterView.setState(DengtaConst.UI_STATE_NO_CONTENT);
                }
                break;
            case DengtaConst.UI_STATE_LOADING:
                mFooterView.setState(DengtaConst.UI_STATE_LOADING);
                break;
            case DengtaConst.UI_STATE_NO_MORE_CONTENT:
                mFooterView.setState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                break;
            case DengtaConst.UI_STATE_FAILED_RETRY:
                mFooterView.setState(DengtaConst.UI_STATE_FAILED_RETRY);
                break;
            case DengtaConst.UI_STATE_NO_CONTENT:
                mFooterView.setState(DengtaConst.UI_STATE_NO_CONTENT);
                break;
            default:
                break;
        }
        mViewState = state;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                if (mBaseInfo != null) {
                    UserInfoActivity.show(this, mBaseInfo);
                }
                StatisticsUtil.reportAction(StatisticsConst.HOMEPAGE_PROFILE_CLICKED);
                break;
            case R.id.follow_layout:
                CommonBeaconJump.showAttentionList(this, mAccountId);
                StatisticsUtil.reportAction(StatisticsConst.HOMEPAGE_ATTENTION_CLICKED);
                break;
            case R.id.fans_layout:
                CommonBeaconJump.showFansList(this, mAccountId);
                StatisticsUtil.reportAction(StatisticsConst.HOMEPAGE_FRIENDS_CLICKED);
                break;
            case R.id.select:
                HomepageSelectDialog homepageSelectDialog = new HomepageSelectDialog(this);
                homepageSelectDialog.setHomepageSelectCallback(feedType -> {
                    if (mRequestFeedType != feedType) {
                        mRequestFeedType = feedType;
                        mFeedList.clear();
                        mAdapter.setListData(mFeedList);
                        mAdapter.notifyDataSetChanged();
                        refreshSelectTitle();
                        loadFeedList(REFRESH_TYPE_UP);
                    }
                });
                homepageSelectDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        if (!success) {
            DtLog.w(TAG, "callback requestBriefInfo faild");
            if (entity != null) {
                int entityType = entity.getEntityType();
                if (entityType == EntityObject.ET_GET_FEED_LIST) {
                    onLoadComplete(false);
                }
            }
            return;
        }

        switch (entity.getEntityType()) {
            case EntityObject.ET_GET_FEED_LIST:
                onLoadComplete(true);

                final GetFeedListRsp rsp = (GetFeedListRsp) entity.getEntity();
                final String extra = (String) entity.getExtra();
                mUiHandler.post(() -> handleGetFeedList(rsp, extra));
                break;
            case EntityObject.ET_GET_FEED_USER_INFO:
                final GetFeedUserInfoRsp userInfoRsp = (GetFeedUserInfoRsp) entity.getEntity();
                mUiHandler.post(() -> handleGetFeedUserInfo(userInfoRsp));
                break;
            case EntityObject.ET_DELETE_COMMENT:
                break;
            case EntityObject.ET_GET_RELATION_BATCH:
                final GetRelationBatchRsp relationBatchRsp = (GetRelationBatchRsp) entity.getEntity();
                mUiHandler.post(() -> handleGetRelation(relationBatchRsp));
            default:
                break;
        }
    }

    private void handleGetRelation(GetRelationBatchRsp relationBatchRsp) {
        Map<Long, Integer> mRelation = relationBatchRsp.getMRelation();
        Integer result = mRelation.get(mAccountId);
        mHasAttention = result != null && result != 0;
        refreshAttentionButton();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDeleteFeedComplete(final DeleteFeedEvent event) {
        if (event.success) {
            if (mBaseInfo != null) {
                if (mRequestFeedType == E_FEED_TYPE.E_FT_ALL) { //全部动态
                    int dynamic = mBaseInfo.getIDynamic();
                    dynamic = dynamic > 0 ? dynamic - 1 : 0;
                    mBaseInfo.setIDynamic(dynamic);
                } else { //投资观点
                    int investNum = mBaseInfo.getIInvestNum();
                    investNum = investNum > 0 ? investNum - 1 : 0;
                    mBaseInfo.setIInvestNum(investNum);
                }
                refreshSelectTitle();
            }

            int position = -1;
            int size = mFeedList.size();
            for (int i = 0; i < size; i++) {
                FeedItem feedItem = mFeedList.get(i);
                String id = feedItem.getStFeedContent().getSFeedId();
                if (TextUtils.equals(event.feedId, id)) {
                    position = i;
                }
            }

            if (position >= 0) {
                mFeedList.remove(position);
                mAdapter.getListData().remove(position);
                mAdapter.notifyNormalItemRemoved(position);
            }

            if (mFeedList.size() == 0) {
                showViewByState(DengtaConst.UI_STATE_NO_CONTENT);
            }
        } else {
            DengtaApplication.getApplication().showToast(R.string.comment_delete_failed);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFeedComplete(final GetFeedResultEvent event) {
        String feedId = event.feedItem.getStFeedContent().getSFeedId();

        int positionByFeedId = findPositionByFeedId(feedId);
        if (positionByFeedId < 0) {
            return;
        }

        mFeedList.set(positionByFeedId, event.feedItem);
        mAdapter.setListData(mFeedList);
        mAdapter.notifyNormalItemChanged(positionByFeedId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostCommentComplete(final PostCommentResultEvent event) {
        if (event.success) {
            DengtaApplication.getApplication().showToast(R.string.comment_post_success);
        } else {
            DengtaApplication.getApplication().showToast(R.string.comment_post_failed);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteComment(final DeleteCommentEvent event) {
        if (!event.success) {
            DengtaApplication.getApplication().showToast(R.string.comment_delete_failed);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoLikeByWeb(final DoLikeByWebEvent event) {
        int positionByFeedId = findPositionByFeedId(event.feedId);
        if (positionByFeedId < 0) {
            return;
        }

        FeedItem feedItem = mFeedList.get(positionByFeedId);
        FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();
        int likeSize = feedExtendInfo.getILikeSize();
        if (event.isAdd) {
            likeSize++;
        } else {
            likeSize = likeSize >= 1 ? 0 : likeSize - 1;
        }
        feedExtendInfo.setILikeSize(likeSize);
        mAdapter.setListData(mFeedList);
        mAdapter.notifyNormalItemChanged(positionByFeedId);
    }

    @UiThread
    private void handleGetFeedList(GetFeedListRsp rsp, String extra) {
        Integer refreshType = REFRESH_TYPE_DOWN;
        String lastOldestId = "";
        try {
            JSONObject jsonObject = new JSONObject(extra);
            refreshType = jsonObject.getInt(DengtaConst.KEY_REFRESH_TYPE);
            lastOldestId = jsonObject.getString(DengtaConst.KEY_LAST_OLDEST_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<FeedItem> feedItems = rsp.getVFeedItem();
        int status = rsp.getIStatus();

        if (feedItems != null && feedItems.size() == 0) {
            if (refreshType == REFRESH_TYPE_DOWN) {
                if (mFeedList.size() > 0) {
                    showViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
                } else {
                    showViewByState(DengtaConst.UI_STATE_NO_CONTENT);
                }
            } else if (refreshType == REFRESH_TYPE_UP) {
                if (mAdapter.getNormalItemCount() == 0) {
                    showViewByState(DengtaConst.UI_STATE_NO_CONTENT);
                }
            }
            return;
        }

        if (feedItems != null && feedItems.size() > 0) {
            StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_PAGED_COUNT);
        }

        if (status == STATUS_NO_MORE) {
            showViewByState(DengtaConst.UI_STATE_NO_MORE_CONTENT);
        }

        mAdapter.putUserBaseInfos(rsp.getMFeedUserBaseInfo());

        int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
        View v = mRecyclerView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();

        if (refreshType == REFRESH_TYPE_UP) {
            if (status == STATUS_HAS_MORE) {
                mFeedList.clear();
                mFeedList.addAll(feedItems);
            } else if (status == STATUS_NO_MORE) {
                mFeedList.clear(); //如果顶部刷新不清缓存只需注释掉此行
                mFeedList.addAll(0, feedItems);
            }
            mAdapter.setListData(mFeedList);
            mRecyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        } else if (refreshType == REFRESH_TYPE_DOWN) { //下拉刷新，或者上拉刷新且新数据和缓存数据能衔接上时
            String oldestId = mFeedList.size() > 0 ? mFeedList.get(mFeedList.size() - 1).getStFeedContent().getSFeedId() : "";
            if (TextUtils.equals(lastOldestId, oldestId)) {
                mFeedList.addAll(feedItems);
                mAdapter.setListData(mFeedList);
                mAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPositionWithOffset(firstVisiblePosition, top);
            }
        }

        showViewByState(DengtaConst.UI_STATE_NORMAL);
    }

    private void handleGetFeedUserInfo(GetFeedUserInfoRsp userInfoRsp) {
        mBaseInfo = userInfoRsp.getStFeedUserBaseInfo();
        ImageLoaderUtils.getImageLoader().displayImage(mBaseInfo.getSFaceUrl(), mFace, mDefaultOptions);
        if(AccountManager.isMember(mBaseInfo)) {
            mUserMemberStatus.setImageResource(R.drawable.vip_icon);
        } else {
//            mUserMemberStatus.setImageResource(R.drawable.vip_none_icon);
        }
        mUserLoginName.setText(mBaseInfo.getSNickName());
        mFollowCount.setText(String.valueOf(mBaseInfo.getIFollower()));
        mFansCount.setText(String.valueOf(mBaseInfo.getIFans()));

        int userType = mBaseInfo.getEUserType();
        if (userType == E_FEED_USER_TYPE.E_FEED_USER_INVEST || userType == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V) { //投顾用户
            mIsInvestUser = true;
            mSelectBar.setVisibility(View.VISIBLE);
            refreshSelectTitle();
            if (userType == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V) {
                mUserIconV.setVisibility(View.VISIBLE);
            }
            mSelectBtn.setVisibility(View.VISIBLE);
            mSelectBtnSticky.setVisibility(View.VISIBLE);
        } else { //普通用户
            mIsInvestUser = false;
            mSelectBar.setVisibility(View.VISIBLE);
            refreshSelectTitle();
            mUserIconV.setVisibility(View.GONE);
        }
        mUserDescription.setText(getDescriptionString());
    }

    private String getDescriptionString() {
        String verifyDesc = mBaseInfo.getSVerifyDesc();
        String profile = mBaseInfo.getSProfile();
        String emptyProfile = getString(R.string.profile_empty);

        if (!TextUtils.isEmpty(verifyDesc)) {
            return getString(R.string.profile_verification_prefix, verifyDesc);
        }
        if (!TextUtils.isEmpty(profile)) {
            return getString(R.string.profile_prefix, profile);
        }
        return emptyProfile;
    }

    private void refreshSelectTitle() {
        if (mRequestFeedType == E_FEED_TYPE.E_FT_ALL) {
            String titleAll = getString(R.string.homepage_select_title, mBaseInfo.getIDynamic());
            mSelectTitle.setText(titleAll);
            mSelectTitleSticky.setText(titleAll);
        } else {
            String titleInvest = getString(R.string.homepage_select_title_invest, mBaseInfo.getIInvestNum());
            mSelectTitle.setText(titleInvest);
            mSelectTitleSticky.setText(titleInvest);
        }
    }

    private int findPositionByFeedId(final String feedId) {
        for (int i = 0; i < mFeedList.size(); i++) {
            FeedItem feedItem = mFeedList.get(i);
            String id = feedItem.getStFeedContent().getSFeedId();
            if (TextUtils.equals(feedId, id)) {
                return i;
            }
        }
        return -1;
    }

}
