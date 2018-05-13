package com.sscf.investment.setting;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IFavorManager;
import com.dengtacj.component.managers.IThemeManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.ScanJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sscf.investment.R;
import com.sscf.investment.bonus.BonusPointManager;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.component.ui.widget.SettingToolsLayout;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.payment.PaymentInfoManager;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.favor.FavorActivity;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.social.FeedListActivity;
import com.sscf.investment.social.FriendsRequestManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.widget.BaseFragment;

import java.util.ArrayList;

import BEC.DtActivityInfo;
import BEC.FeedUserBaseInfo;
import BEC.GetFeedUserInfoRsp;

/**
 * Created by xuebinliu on 2015/7/22.
 * <p>
 * 我的 功能界面
 */
public final class TabFragmentSetting extends BaseFragment implements View.OnClickListener, DataSourceProxy.IRequestCallback, AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = TabFragmentSetting.class.getSimpleName();

    private BroadcastReceiver mLocalReceiver;
    private TextView mMessageCountView;
    private TextView mUserInfoView;
    private TextView mProfileView;
    private View mIconVView;
    private TextView mMyEventView;
    private TextView mMyFavorView;
    private TextView mMyAttentionView;
    private TextView mMyFansView;
    private TextView mMyCouponView;
    private ImageView mAvatarView;
    private View mSettingDetailRetDotView;
    private View mSettingActivitiesRetDotView;
    private View mSettingBonusPointsRetDotView;
    private View mPrivilegeRetDot;
    private View mValueaddedServicesRetDot;
    private TextView mSettingActivitiesTextView;
    private TextView mSettingBonusPointsTextView;
    private TextView mSettingRiskEvaluationTextView;
    private TextAppearanceSpan mSpan;
    private FeedUserBaseInfo mFeedInfo;

    private AppBarLayout mAppBarLayout;
    private View mActionBarSticky;
    private TextView mMessageCountStickyView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mVipCardView;
    private ObjectAnimator mAnimator = null;
    private int mOffsetVertical;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_fragment_setting, container, false);
        enableStatusSet();
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        initResource();
        initViews(root);
        registerLocalBroadcast();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_DISPLAY);
        dengtaApplication.getMessageCenterManager().requestMessageCenter();
        return root;
    }

    protected int getStatusbarRes() {
        return DengtaApplication.getApplication().getAccountManager().isMember() ?
                R.color.member_actionbar_bg_color : R.color.default_actionbar_bg_color;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        doOnShow(true);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doOnShow(false);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        getAnimator().cancel();
        mVipCardView.setTranslationY(0f);
    }

    private ObjectAnimator getAnimator() {
        if(mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(mVipCardView, "translationY", 0f, (float)DeviceUtil.dip2px(getContext(), 10));
            mAnimator.setDuration(1000);
            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimator.setRepeatCount(5);
            mAnimator.setRepeatMode(ValueAnimator.REVERSE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mAnimator.setAutoCancel(true);
            }
        }
        return mAnimator;
    }

    private void playVipCardAnimator() {
        mVipCardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAnimator().start();
            }
        }, 500);
    }

    private void doOnShow(boolean isFirst) {
        updateUserInfoView();
        updateMemberState(isFirst);
        updateDot();
        playVipCardAnimator();
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        dengtaApplication.getRedDotManager().requestConfigActivitiesAndOpenAccountInfo();
        dengtaApplication.getBonusPointManager().requestBonusPointDescription();
        dengtaApplication.getPaymentInfoManager().requestEvalResult();

        StatisticsUtil.reportAction(StatisticsConst.SETTING_DISPLAY);
    }

    private void initViews(final View root) {
        ((TextView) root.findViewById(R.id.actionbar_title)).setText(R.string.main_tab_name_setting);
        root.findViewById(R.id.actionbarScan).setOnClickListener(this);
        root.findViewById(R.id.actionbarMessage).setOnClickListener(this);
        mMessageCountView = (TextView) root.findViewById(R.id.count);
        mMessageCountStickyView = (TextView) root.findViewById(R.id.count_sticky);
        updateMessageCount();
        initUserInfoView(root);
        initActionbar(root);
        initItemList(root);
    }

    private void initResource() {
        mOffsetVertical = getResources().getDimensionPixelSize(R.dimen.tab_setting_header_offset_vertical);
    }

    private void initActionbar(final View root) {
        mAppBarLayout = (AppBarLayout) root.findViewById(R.id.app_bar_layout);
        mActionBarSticky = root.findViewById(R.id.actionBar_sticky);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) root.findViewById(R.id.collapsing_toolbar);
        mVipCardView = (ImageView) root.findViewById(R.id.vip_card);
        mVipCardView.setOnClickListener(this);
    }

    private boolean mIsMember = false;

    private void updateMemberState(boolean forceUpdate) {
        boolean isMember = DengtaApplication.getApplication().getAccountManager().isMember();
        if(forceUpdate || isMember != mIsMember) {
            DtLog.d(TAG, "updateMemberState() update needed");
            View root = getView();
            if(root != null) {
                initToolsItem(root, isMember);
                updateItemList(root, isMember);
            }
            mIsMember = isMember;
        } else {
            DtLog.d(TAG, "updateMemberState() update no need");
        }
    }

    private void updateDot() {
        AccountManager manager = DengtaApplication.getApplication().getAccountManager();
        if(manager.getAccountInfo() != null) {
            // 当会员过期时，判断是否已经点击进入过
            if(manager.isExpired()) {
                // 如果点击进入过，则不展示红点
                if(SettingPref.getBoolean(SettingConst.KEY_SETTING_CLICK_PRIVILEGE, false)) {
                    DengtaApplication.getApplication().getRedDotManager().setPrivilegeState(false);
                } else { // 如果还未点击过，则不展示红点
                    DengtaApplication.getApplication().getRedDotManager().setPrivilegeState(true);
                }
            } else {
                // 如果会员未过期（无会员，或者会员还在有效期），则不展示红点，并且清除掉点击进入的记录
                DengtaApplication.getApplication().getRedDotManager().setPrivilegeState(false);
                SettingPref.putBoolean(SettingConst.KEY_SETTING_CLICK_PRIVILEGE, false);
            }
        }
    }

    private void initToolsItem(final View root, boolean isMember) {
        final SettingToolsLayout toolsLayout = (SettingToolsLayout) root.findViewById(R.id.settingToolsLayout);
        final int PAGE_COUNT = 2;
        final int COUNT_PER_LINE = 4;

        final ArrayList<ToolsItem> firstPagerItems = new ArrayList<>(8);
        // page1 line1
        firstPagerItems.add(SettingToolsItem.createSettingsMarginTrackingItem(isMember));
        firstPagerItems.add(SettingToolsItem.createDirectionAddNuggets(isMember));
        firstPagerItems.add(SettingToolsItem.createSettingsCYQItem(isMember));
        firstPagerItems.add(SettingToolsItem.createSettingsBSSignalItem(isMember));

        // page1 line2
        firstPagerItems.add(SettingToolsItem.createSettingsSimilarKLineItem(isMember));
        firstPagerItems.add(SettingToolsItem.createSettingsSecHistoryItem(isMember));
        firstPagerItems.add(SettingToolsItem.createSettingsIntelligentDiagnosisItem(isMember));
        firstPagerItems.add(SettingToolsItem.createSettingsLiveItem(isMember));

        // page2 line1
        final ArrayList<ToolsItem> secondPagerItems = new ArrayList<>(4);
        secondPagerItems.add(SettingToolsItem.createSettingsIntelligentAnswerSchoolItem(isMember));
        secondPagerItems.add(SettingToolsItem.createSettingsPlateItem(isMember));
        secondPagerItems.add(SettingToolsItem.createSettingsPrivatizationTrackingItem(isMember));

        final ArrayList<ToolsItem>[] items = new ArrayList[PAGE_COUNT];
        items[0] = firstPagerItems;
        items[1] = secondPagerItems;

        // 设置间距
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolsLayout.getLayoutParams();
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.setting_item_small_margin_top);
        params.bottomMargin = params.topMargin;
        toolsLayout.setLayoutParams(params);

        toolsLayout.init(PAGE_COUNT, COUNT_PER_LINE, items);
    }

    private void updateMessageCount() {
        final int count = DengtaApplication.getApplication()
                .getMessageCenterManager().getTotalUnreadMessageCount();
        if (count > 0) {
            mMessageCountView.setText(String.valueOf(count));
            mMessageCountView.setVisibility(View.VISIBLE);
            mMessageCountStickyView.setText(String.valueOf(count));
        } else {
            mMessageCountView.setVisibility(View.INVISIBLE);
        }
    }

    private void updateFavorCount() {
        if (mFeedInfo == null) {
            setUserCountText(mMyFavorView,  getString(R.string.value_null));
        } else {
            IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance().getManager(IFavorManager.class.getName());

            if (favorManager != null) {
                setUserCountText(mMyFavorView, String.valueOf(favorManager.getFavorCount()));
            } else {
                setUserCountText(mMyFavorView, getString(R.string.value_null));
            }
        }
    }

    private void initUserInfoView(final View root) {
        final View settingUserInfoLayout = root.findViewById(R.id.settingUserInfoLayout);
        settingUserInfoLayout.setOnClickListener(this);
        mUserInfoView = (TextView) settingUserInfoLayout.findViewById(R.id.settingNikename);
        mAvatarView = (ImageView) settingUserInfoLayout.findViewById(R.id.settingUserIcon);
        mProfileView = (TextView) settingUserInfoLayout.findViewById(R.id.profile);
        mIconVView = settingUserInfoLayout.findViewById(R.id.iconV);
        mMyEventView = (TextView) root.findViewById(R.id.myEventCount);
        mMyEventView.setOnClickListener(this);
        mMyFavorView = (TextView) root.findViewById(R.id.myFavorCount);
        mMyFavorView.setOnClickListener(this);
        mMyAttentionView = (TextView) root.findViewById(R.id.myAttentionCount);
        mMyAttentionView.setOnClickListener(this);
        mMyFansView = (TextView) root.findViewById(R.id.myFansCount);
        mMyFansView.setOnClickListener(this);
        mMyCouponView = (TextView) root.findViewById(R.id.myCouponCount);
        mMyCouponView.setOnClickListener(this);

        root.findViewById(R.id.myCoupon).setOnClickListener(this);
        root.findViewById(R.id.myFans).setOnClickListener(this);
        root.findViewById(R.id.myFavor).setOnClickListener(this);
        root.findViewById(R.id.myEvent).setOnClickListener(this);
        root.findViewById(R.id.myAttention).setOnClickListener(this);

        mSpan = new TextAppearanceSpan(DengtaApplication.getApplication(), R.style.user_info_count_text_style);
    }

    private void updateUserInfoView() {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final AccountInfoExt accountInfoExt = dengtaApplication.getAccountManager().getAccountInfoExt();

        updateFeedInfo(null);
        updateCouponInfo();
        boolean isMember = false;
        if (accountInfoExt != null) { // 登录过了
            final AccountInfoEntity accountInfo = accountInfoExt.accountInfo;
            FriendsRequestManager.getFeedUserInfo(accountInfo.id, this);
            DengtaApplication.getApplication().getPaymentInfoManager().requsetCouponNumber();

            final String url = accountInfo.iconUrl;
            setAvatarImage(url);
            isMember = accountInfoExt.isMember();

            mUserInfoView.setText(accountInfo.nickname);

            if (TextUtils.isEmpty(accountInfoExt.profile)) {
                mProfileView.setText(R.string.profile_empty);
            } else {
                mProfileView.setText(accountInfoExt.profile);
            }
            mProfileView.setVisibility(View.VISIBLE);
            mIconVView.setVisibility(accountInfoExt.isV() ? View.VISIBLE : View.GONE);
        } else { // 没有登录
            mAvatarView.setImageResource(R.drawable.default_consultant_face);
            mUserInfoView.setText(R.string.setting_user_info_click_to_login);
            mUserInfoView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mProfileView.setVisibility(View.GONE);
            mProfileView.setText("");
            mIconVView.setVisibility(View.GONE);
        }
        updateVipState(getContext().getApplicationContext(), isMember);
    }

    private void updateVipState(final Context context, boolean isMember) {

        int actionBarBgColorRes = isMember ? R.color.member_actionbar_bg_color : R.color.default_actionbar_bg_color;
        mCollapsingToolbarLayout.setContentScrimResource(actionBarBgColorRes);
        mActionBarSticky.setBackgroundResource(actionBarBgColorRes);

        mUserInfoView.setTextColor(ContextCompat.getColor(context, isMember ?
                R.color.setting_vip_name_color : R.color.actionbar_title_text_color));

        mUserInfoView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                isMember ? R.drawable.vip_text_icon : 0, 0);

        boolean isDefaultTheme = true;
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        if (themeManager != null) {
            isDefaultTheme = themeManager.isDefaultTheme();
        }
        if(isMember && isDefaultTheme) {
            mVipCardView.setBackgroundResource(R.drawable.vip_card_gold);
        } else {
            mVipCardView.setBackgroundResource(R.drawable.vip_card_black);
        }

        int msgCountBgRes = isMember ? R.drawable.message_count_vip_bg : R.drawable.message_count_none_vip_bg;
        mMessageCountView.setBackgroundResource(msgCountBgRes);
        mMessageCountStickyView.setBackgroundResource(msgCountBgRes);

        int msgCountTextColorRes = isMember ? R.color.message_count_vip_color : R.color.message_count_none_vip_color;
        mMessageCountView.setTextColor(ContextCompat.getColor(context, msgCountTextColorRes));
        mMessageCountStickyView.setTextColor(ContextCompat.getColor(context, msgCountTextColorRes));
    }

    private void updateFeedInfo(final FeedUserBaseInfo feedInfo) {
        if (mFeedInfo != null && feedInfo == null) {
            return;
        }

        mFeedInfo = feedInfo;
        if (feedInfo == null) {
            setUserCountText(mMyAttentionView,  getString(R.string.value_null));
            setUserCountText(mMyFansView, getString(R.string.value_null));
            setUserCountText(mMyEventView, getString(R.string.value_null));
            setUserCountText(mMyFavorView, getString(R.string.value_null));
        } else {
            setUserCountText(mMyAttentionView, String.valueOf(feedInfo.iFollower));
            setUserCountText(mMyFansView, String.valueOf(feedInfo.iFans));
            setUserCountText(mMyEventView, String.valueOf(feedInfo.iDynamic));
            IFavorManager favorManager = (IFavorManager) ComponentManager.getInstance().getManager(IFavorManager.class.getName());

            if (favorManager != null) {
                setUserCountText(mMyFavorView, String.valueOf(favorManager.getFavorCount()));
            } else {
                setUserCountText(mMyFavorView, getString(R.string.value_null));
            }
        }
    }

    private void updateCouponInfo() {
        int couponNum = DengtaApplication.getApplication().getPaymentInfoManager().getUserCouponNum();
        if(couponNum < 0) {
            setUserCountText(mMyCouponView, getString(R.string.value_null));
        } else {
            setUserCountText(mMyCouponView, String.valueOf(couponNum));
        }
    }

    private void updateUserEvalResult() {
        String evalResult = DengtaApplication.getApplication().getPaymentInfoManager().getUserEvalResult();
        mSettingRiskEvaluationTextView.setText(evalResult);
    }

    private void setUserCountText(final TextView textView, final String count) {
        textView.setText(count);
    }

    private void initItemList(View root) {
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        // 风险评测
        root.findViewById(R.id.settingRiskEvaluationLayout).setOnClickListener(this);
        mSettingRiskEvaluationTextView = (TextView) root.findViewById(R.id.settingRiskEvaluationText);

        // 邀请好友
        root.findViewById(R.id.settingInvite).setOnClickListener(this);

        root.findViewById(R.id.settingDetailLayout).setOnClickListener(this);

        final RedDotManager redDotManager = dengtaApplication.getRedDotManager();

        mSettingDetailRetDotView = root.findViewById(R.id.settingDetailRetDot);
        mSettingDetailRetDotView.setVisibility(redDotManager.getSettingState() ? View.VISIBLE : View.INVISIBLE);

        final View settingBonusPointsLayout = root.findViewById(R.id.settingBonusPointsLayout);
        settingBonusPointsLayout.setOnClickListener(this);
        mSettingBonusPointsRetDotView = root.findViewById(R.id.settingBonusPointsRetDot);
        mSettingBonusPointsTextView = (TextView) root.findViewById(R.id.settingBonusPointsText);

        mSettingActivitiesRetDotView = root.findViewById(R.id.settingActivitiesRetDot);
        root.findViewById(R.id.settingActivitiesLayout).setOnClickListener(this);
        mSettingActivitiesTextView = (TextView) root.findViewById(R.id.settingActivitiesText);
        setNewActivityState();

        // 特权
        final View settingPrivilegeLayout = root.findViewById(R.id.settingPrivilegeLayout);
        settingPrivilegeLayout.setOnClickListener(this);
        mPrivilegeRetDot = settingPrivilegeLayout.findViewById(R.id.settingPrivilegeRetDot);
        mPrivilegeRetDot.setVisibility(redDotManager.getPrivilegeState() ? View.VISIBLE : View.INVISIBLE);

        // 增值服务
        final View settingValueaddedServicesLayout = root.findViewById(R.id.settingValueaddedServicesLayout);
        settingValueaddedServicesLayout.setOnClickListener(this);
        settingValueaddedServicesLayout.setVisibility(View.GONE);
        mValueaddedServicesRetDot = settingValueaddedServicesLayout.findViewById(R.id.settingValueaddedServicesRetDot);
        mValueaddedServicesRetDot.setVisibility(redDotManager.getValueaddedServicesState() ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateItemList(View root, boolean isMember) {
        // 增值服务
        updateItem(root, R.id.settingValueaddedServices, isMember, R.drawable.settings_valueadded_services_vip , R.drawable.settings_valueadded_services_none_vip);
        // 会员特权
        updateItem(root, R.id.settingPrivilege, isMember, R.drawable.settings_privilege_vip , R.drawable.settings_privilege_none_vip);
        // 活动广场
        updateItem(root, R.id.settingActivities, isMember, R.drawable.settings_activities_vip , R.drawable.settings_activities_none_vip);
        // 积分任务
        updateItem(root, R.id.settingBonusPoints, isMember, R.drawable.settings_bonus_vip , R.drawable.settings_bonus_none_vip);
        // 股票开户
//        updateItem(root, R.id.settingOpenAccount, isMember, R.drawable.settings_open_account_vip , R.drawable.settings_open_account_none_vip);


        // 风险评测
        updateItem(root, R.id.settingRiskEvaluation, isMember, R.drawable.settings_risk_evaluation_vip , R.drawable.settings_risk_evaluation_none_vip);
        // 邀请好友
        updateItem(root, R.id.settingInvite, isMember, R.drawable.settings_invite_vip , R.drawable.settings_invite_none_vip);
        // 设置
        updateItem(root, R.id.settingDetailSettings, isMember, R.drawable.settings_detail_vip , R.drawable.settings_detail_none_vip);
    }

    private void updateItem(View root, int textViewId, boolean isMember, int vipRes, int noneVipRes) {
        TextView textView = (TextView) root.findViewById(textViewId);
        textView.setCompoundDrawablesWithIntrinsicBounds(isMember ? vipRes : noneVipRes, 0, 0, 0);
    }

    private void setBonusPointsState() {
        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
        mSettingBonusPointsRetDotView.setVisibility(redDotManager.getNeverEnterBonus() ? View.VISIBLE : View.INVISIBLE);
    }

    private void setNewActivityState() {
        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
        mSettingActivitiesRetDotView.setVisibility(redDotManager.getNewActivityState() ? View.VISIBLE : View.INVISIBLE);

        if (redDotManager.getNewActivityState()) {
            final DtActivityInfo newActivityInfo = redDotManager.getActivitiesInfo();
            mSettingActivitiesTextView.setText(newActivityInfo != null ? newActivityInfo.sMsg : "");
        } else {
            mSettingActivitiesTextView.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterLocalBroadcast();
    }

    private void registerLocalBroadcast() {
        if (mLocalReceiver == null) {
            mLocalReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(SettingConst.ACTION_LOGOUT_SUCCESS);
            intentFilter.addAction(SettingConst.ACTION_UPDATE_ACCOUNT_INFO);
            intentFilter.addAction(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            intentFilter.addAction(DengtaConst.ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED);
            intentFilter.addAction(BonusPointManager.DESC_CHANGE);
            intentFilter.addAction(PaymentInfoManager.USER_COUPON_NUM_CHANGED);
            intentFilter.addAction(PaymentInfoManager.USER_RISK_EVAL_CHANGED);
            intentFilter.addAction(SettingConst.ACTION_VIDEO_FAVOR_CHANGED);
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mLocalReceiver,intentFilter);
        }
    }

    private void unregisterLocalBroadcast() {
        if (mLocalReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mLocalReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.actionbarScan:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_SCAN);
                ScanJump.showScan(activity);
                break;
            case R.id.actionbarMessage://消息中心
                CommonBeaconJump.showMessageCenter(activity);
                break;
            // userinfo相关
            case R.id.settingUserInfoLayout:
                if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    CommonBeaconJump.showHomepage(getContext(), DengtaApplication.getApplication().getAccountManager().getAccountId());
                    StatisticsUtil.reportAction(StatisticsConst.MINE_CLICK_HOME_PAGE);
                } else {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    StatisticsUtil.reportAction(StatisticsConst.A_ME_LOGIN_CLICK);//点击登录
                }
                break;
            case R.id.myEvent:
                if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    FeedListActivity.show(activity);
                    StatisticsUtil.reportAction(StatisticsConst.MINE_CLICK_EVENT);
                } else {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.myFavor:
                if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    activity.startActivity(new Intent(activity, FavorActivity.class));
                    StatisticsUtil.reportAction(StatisticsConst.MINE_CLICK_FAVOR);
                } else {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.myAttention:
                long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
                if (accountId > 0) {
                    CommonBeaconJump.showAttentionList(activity, accountId);
                    StatisticsUtil.reportAction(StatisticsConst.MINE_CLICK_ATTENTION);
                } else {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.myFans:
                accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
                if (accountId > 0) {
                    CommonBeaconJump.showFansList(activity, accountId);
                    StatisticsUtil.reportAction(StatisticsConst.MINE_CLICK_FANS);
                } else {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.myCoupon:
                accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
                if (accountId > 0) {
                    StatisticsUtil.reportAction(StatisticsConst.MINE_CLICK_MY_COUPON);
                    WebBeaconJump.showCommonWebActivity(activity, WebUrlManager.getInstance().getMyCoupons());
                } else {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            // 风险测评
            case R.id.settingRiskEvaluationLayout:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_RISK_EVAL);
                WebBeaconJump.showRiskEval(activity);
                break;
            case R.id.settingDetailLayout:
                activity.startActivity(new Intent(activity, SettingActivity.class));
                break;
            //积分任务
            case R.id.settingBonusPointsLayout:
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_BONUS_TASK_CENTER);
                CommonBeaconJump.showBonusPoints(activity);
                DengtaApplication.getApplication().getRedDotManager().setNeverEnterBonus(false);
                String bonusDescText = mSettingBonusPointsTextView.getText().toString();
                if(!TextUtils.isEmpty(bonusDescText)) {
                    SettingPref.putString(SettingConst.KEY_SETTING_BONUS_DESC, bonusDescText);
                }
                mSettingBonusPointsTextView.setText("");

                break;
            case R.id.settingActivitiesLayout://活动广场
                StatisticsUtil.reportAction(StatisticsConst.SETTING_ACTIVITY_DISPLAY);
                WebBeaconJump.showActivities(activity);
                DengtaApplication.getApplication().getRedDotManager().saveNewActivityVersion();
                break;
            case R.id.settingInvite://邀请好友
                WebBeaconJump.showInviteFriends(activity);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_CLICK_INVITE_FRIENDS);
                break;
            case R.id.vip_card:
                showPrivilege(activity);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_VIP_CARD_SHOW_PRIVILEGE);
                break;
            case R.id.settingPrivilegeLayout://会员特权
                showPrivilege(activity);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_PRIVILEGE_DISPLAY);
                break;
            case R.id.settingValueaddedServicesLayout://增值服务
                WebBeaconJump.showValueAddedService(activity);
//                WebBeaconJump.showCommonWebActivity(activity, "https://www.gp51.com/index.php?m=content&c=index&a=lists&catid=10");
                DengtaApplication.getApplication().getRedDotManager().setValueaddedServicesState(false);
                StatisticsUtil.reportAction(StatisticsConst.SETTING_VALUE_ADDED_SERVICE_DISPLAY);
                break;
            default:
                break;
        }
    }

    private void showPrivilege(Activity activity) {
        WebBeaconJump.showPrivilege(activity);
        DengtaApplication.getApplication().getRedDotManager().setPrivilegeState(false);
        SettingPref.putBoolean(SettingConst.KEY_SETTING_CLICK_PRIVILEGE, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(verticalOffset > -mOffsetVertical) {
            mActionBarSticky.setVisibility(View.GONE);
        } else {
            mActionBarSticky.setVisibility(View.VISIBLE);
            mMessageCountStickyView.setVisibility(mMessageCountView.getVisibility());
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action) || SettingConst.ACTION_UPDATE_ACCOUNT_INFO.equals(action)) {
                mFeedInfo = null;
                final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
                if (accountInfo != null) {
                    mUserInfoView.setText(accountInfo.nickname);
                    setAvatarImage(accountInfo.iconUrl);
                }
            } else if (SettingConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
                mFeedInfo = null;
                mUserInfoView.setText(R.string.setting_user_info_not_login);
                mAvatarView.setImageResource(R.drawable.default_consultant_face);
            } else if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                mSettingDetailRetDotView.setVisibility(redDotManager.getSettingState() ? View.VISIBLE : View.INVISIBLE);
                mValueaddedServicesRetDot.setVisibility(redDotManager.getValueaddedServicesState() ? View.VISIBLE : View.INVISIBLE);
                mPrivilegeRetDot.setVisibility(redDotManager.getPrivilegeState() ? View.VISIBLE : View.INVISIBLE);

                setNewActivityState();
                setBonusPointsState();
            } else if (DengtaConst.ACTION_MESSAGE_TOTAL_UNREAD_COUNT_CHANGED.equals(action)) {
                updateMessageCount();
            } else if(BonusPointManager.DESC_CHANGE.equals(intent.getAction())) {
                setBonusDesc();
            } else if(PaymentInfoManager.USER_COUPON_NUM_CHANGED.equals(intent.getAction())) {
                updateCouponInfo();
            } else if(PaymentInfoManager.USER_RISK_EVAL_CHANGED.equals(intent.getAction())) {
                updateUserEvalResult();
            } else if(SettingConst.ACTION_VIDEO_FAVOR_CHANGED.equals(intent.getAction())) {
                updateFavorCount();
            }
        }
    }

    private void setAvatarImage(final String url) {
        mAvatarView.setImageResource(R.drawable.default_consultant_face);

        if (TextUtils.isEmpty(url)) {
            return;
        }

        mRetry = 0;
        if (!TextUtils.isEmpty(url)) {
            ImageLoaderUtils.getImageLoader().displayImage(url, mAvatarView, getImageLoadingListener());
            DtLog.d(TAG, "ImageLoader.displayImage");
        }
    }

    private static final int MAX_RETRY = 5;

    private int mRetry;

    private SimpleImageLoadingListener mImageLoadingListener;

    private SimpleImageLoadingListener getImageLoadingListener() {
        if (mImageLoadingListener == null) {
            mImageLoadingListener = new SimpleImageLoadingListener() {

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    DtLog.d(TAG, "ImageLoader.onLoadingFailed : " + failReason.getCause());
                    if (mRetry <= MAX_RETRY) {
                        mRetry++;
                        ImageLoaderUtils.getImageLoader().displayImage(imageUri, mAvatarView, this);
                    }
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    DtLog.d(TAG, "ImageLoader.onLoadingComplete");
                    if (!DengtaApplication.getApplication().getAccountManager().isLogined()) {
                        mAvatarView.setImageResource(R.drawable.default_consultant_face);
                    }
                }
            };
        }

        return mImageLoadingListener;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            switch (data.getEntityType()) {
                case EntityObject.ET_GET_FEED_USER_INFO:
                    final GetFeedUserInfoRsp userInfoRsp = (GetFeedUserInfoRsp) data.getEntity();
                    final FeedUserBaseInfo feedInfo = userInfoRsp.stFeedUserBaseInfo;
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateFeedInfo(feedInfo);
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setBonusDesc() {
        String descText = DengtaApplication.getApplication().getBonusPointManager().getDescText();
        mSettingBonusPointsTextView.setText(descText);
        DengtaApplication.getApplication().getRedDotManager().setNeverEnterBonus(true);
        setBonusPointsState();
    }
}