package com.sscf.investment.social;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IFeedRequestManager;
import com.sscf.investment.R;
import com.sscf.investment.common.entity.FeedLikeEntity;
import com.sscf.investment.detail.FeedOperationDialog;
import com.sscf.investment.detail.view.CommentReplyLayout;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import BEC.AccuPointTaskType;
import BEC.E_FEED_INVEST_ATTI_TYPE;
import BEC.E_FEED_TYPE;
import BEC.E_FEED_USER_TYPE;
import BEC.FeedCommentInfoList;
import BEC.FeedContent;
import BEC.FeedExtendInfo;
import BEC.FeedItem;
import BEC.FeedUserBaseInfo;
import BEC.SecCodeName;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/9/26.
 */

public class FeedRecyclerViewAdapter extends CommonRecyclerViewAdapter {
    private static final String TAG = "FeedRecyclerViewAdapter";
    private static final int MAX_FEED_CONTENT_LENGTH = 80;
    public static final int MAX_CONSULTANT_CONTENT_LENGTH = 68;

    @BindColor(R.color.default_text_color_60) int mLikeTextColor;
    @BindColor(R.color.tab_bar_text_selected_color) int mLikeClickedTextColor;
    private DisplayImageOptions mDefaultOptions;
    private RecyclerView mRecyclerView;
    private Map<Long, FeedUserBaseInfo> mFeedUserBaseInfos = new HashMap<>();

    @BindDrawable(R.drawable.attitude_rise) Drawable mAttiDrawableGood;
    @BindDrawable(R.drawable.attitude_down) Drawable mAttiDrawableBad;
    @BindColor(R.color.tag_relatedStock) int mTextHighlightColor;

    @BindDrawable(R.drawable.comment_like) Drawable mDrawableLike;
    @BindDrawable(R.drawable.comment_like_clicked) Drawable mDrawableLikeClicked;

    private FeedRequestManager mFeedRequestManager;

    private boolean mIsCommentType;
    private boolean mIsHomepage;
    private long mAccountId;
    private boolean mFromStock;

    public FeedRecyclerViewAdapter(Context context, RecyclerView recyclerView, boolean isHomepage, long accountId, boolean fromStock) {
        super(context);
        mRecyclerView = recyclerView;
        mIsHomepage = isHomepage;
        mAccountId = accountId;
        mFromStock = fromStock;
        ButterKnife.bind(this, mRecyclerView);
        mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
        mFeedRequestManager = DengtaApplication.getApplication().getFeedRequestManager();
    }

    public void setIsCommentType(boolean isCommentType) {
        mIsCommentType = isCommentType;
    }

    public void putUserBaseInfos(Map<Long, FeedUserBaseInfo> feedUserBaseInfos) {
        mFeedUserBaseInfos.putAll(feedUserBaseInfos);
    }

    @Override
    protected ListViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        DtLog.d(TAG, "createNormalViewHolder: viewType = " + viewType);
        return new ListViewHolder(mInflater.inflate(R.layout.feed_list_item, parent, false));
    }

    class ListViewHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
        @BindView(R.id.user_icon) ImageView mUserIcon;
        @BindView(R.id.user_icon_v) ImageView mUserIconV;
        @BindView(R.id.user_name) TextView mUserName;
        @BindView(R.id.member_status_icon) ImageView mMemberStatusIcon;
        @BindView(R.id.consultant_title) TextView mTitle;
        @BindView(R.id.comment_text) TextView mComment;
        @BindView(R.id.comment_summary) TextView mSummary;
        @BindView(R.id.time) TextView mTime;
        @BindView(R.id.comment_count) TextView mCommentCount;
        @BindView(R.id.like_count) TextView mLikeCount;
        @BindView(R.id.like_icon) ImageView mLikeIcon;
        @BindView(R.id.comment_icon) ImageView mCommentIcon;
        @BindView(R.id.reply_layout) CommentReplyLayout mReplyLayout;

        private String mDtSecCode;
        private String mSecName;

        public ListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            FeedItem feedItem = (FeedItem) itemData;
            final String feedId = feedItem.getStFeedContent().getSFeedId();
            FeedContent feedContent = feedItem.getStFeedContent();
            int feedType = feedContent.getEType();
            FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();
            final long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();

            ArrayList<SecCodeName> secCodeNames = feedContent.getVRelateSec();
            if (secCodeNames.size() > 0) {
                mDtSecCode = secCodeNames.get(0).sDtSecCode;
                mSecName = secCodeNames.get(0).sSecName;
            }

            long pubAccountId = feedContent.getIPubAccountId();
            FeedUserBaseInfo userBaseInfo = mFeedUserBaseInfos.get(pubAccountId);
            mUserName.setTextColor(mContext.getResources().getColor(R.color.normal_user_name_color));
            mMemberStatusIcon.setVisibility(View.GONE);
            if (userBaseInfo != null) {
                mUserName.setText(userBaseInfo.getSNickName());
                String faceUrl = userBaseInfo.getSFaceUrl();
                ImageLoaderUtils.getImageLoader().displayImage(faceUrl, mUserIcon, mDefaultOptions);
                int userType = userBaseInfo.getEUserType();
                if (userType == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V) {
                    mUserIconV.setVisibility(View.VISIBLE);
                } else {
                    mUserIconV.setVisibility(View.GONE);
                }

                if(AccountManager.isMember(userBaseInfo)) {
                    mUserName.setTextColor(mContext.getResources().getColor(R.color.member_user_name_color));
                    mMemberStatusIcon.setVisibility(View.VISIBLE);
                }
            } else {
                mUserName.setText("");
                mUserIcon.setImageResource(R.drawable.default_consultant_face);
                mUserIconV.setVisibility(View.GONE);
            }

            mTime.setText(TimeUtils.timeStamp2Date(feedContent.getIPubTime() * 1000L));

            mReplyLayout.setSecInfo(mDtSecCode, mSecName);
            mReplyLayout.setFeedId(feedId);
            FeedCommentInfoList commentInfos = feedItem.getStFeedCommentInfoList();
            mReplyLayout.setData(commentInfos, mFeedUserBaseInfos, feedExtendInfo.getITotalCommentSize(), feedType);

            if (feedType == E_FEED_TYPE.E_FT_STOCK_REVIEW) {
                mComment.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);

                if (!mIsCommentType) { //在动态和个人主页主贴前面显示可跳转的股票名称
                    String secName = getRelatedSecName(feedContent);
                    String content = "#" + secName + "#" + feedContent.getSContent();
                    content = StringUtil.getStringWithMaxLength(content, MAX_FEED_CONTENT_LENGTH);
                    SpannableString clickSpan = new SpannableString(content);
                    clickSpan.setSpan(new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(mTextHighlightColor);
                            ds.setUnderlineText(false);
                        }

                        @Override
                        public void onClick(View widget) {
                            SecCodeName secCodeName = getRelatedSecCodeName(feedContent);
                            if (secCodeName != null) {
                                CommonBeaconJump.showSecurityDetailActivity(mContext, secCodeName.sDtSecCode, secCodeName.sSecName);
                            }
                        }
                    }, 0, secName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mComment.setText(clickSpan);
                    mComment.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    mComment.setText(StringUtil.getStringWithMaxLength(feedContent.getSContent(), MAX_FEED_CONTENT_LENGTH));
                }

                mSummary.setVisibility(View.GONE);
            } else { //投顾类型
                mComment.setVisibility(View.GONE);
                mTitle.setVisibility(View.VISIBLE);

                mTitle.setText(feedContent.getSTitle());

                SpannableString summaryString = getSummaryString(feedContent);
                mSummary.setText(summaryString);
                mSummary.setVisibility(TextUtils.isEmpty(summaryString) ? View.GONE : View.VISIBLE);
            }

            // 根据自己是否点过赞的cache值刷新点赞图标颜色
            final IFeedRequestManager feedRequestManager = (IFeedRequestManager) ComponentManager.getInstance().getManager(IFeedRequestManager.class.getName());
            if (feedRequestManager == null) {
                return;
            }
            boolean isLike = feedRequestManager.queryIsLike(feedContent.getSFeedId());
            if (isLike && feedExtendInfo.getILikeSize() == 0) { //后台拉取的点赞数为0时重置本地对应的点赞cache条目
                isLike = false;
                DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        FeedRequestManager.saveFeedLike(accountId, feedId, false);
                    }
                });
            }
            refreshByLikeClicked(isLike);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            openFeedDetailPage();
        }

        private SpannableString getSummaryString(FeedContent feedContent) {
            int attiType = feedContent.getEAttiType();
//            attiType = E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD; //for test
            String description = feedContent.getSDescription();
            String content = attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_NEUTRAL ? description : "??" + " " + description;
            Drawable attiDrawable = null;
            if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD) {
                attiDrawable = mAttiDrawableGood;
            } else if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_BAD) {
                attiDrawable = mAttiDrawableBad;
            }
            if (attiDrawable != null) {
                attiDrawable.setBounds(0, 0, attiDrawable.getIntrinsicWidth(), attiDrawable.getIntrinsicHeight());
            }

            SpannableString spannableString = new SpannableString(StringUtil.getStringWithMaxLength(content, MAX_CONSULTANT_CONTENT_LENGTH));
            if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD || attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_BAD) {
                spannableString.setSpan(new ImageSpan(attiDrawable, ImageSpan.ALIGN_BOTTOM), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableString;
        }

        @OnClick({R.id.user_icon, R.id.user_name})
        void onUserIconClicked() {
            FeedItem feedItem = (FeedItem) mItemData;
            long pubAccountId = feedItem.getStFeedContent().getIPubAccountId();
            if (mIsHomepage && mAccountId == pubAccountId) {
                return;
            }
            CommonBeaconJump.showHomepage(mContext, pubAccountId);
            StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_OPEN_HOMEPAGE_CLICKED);
        }

        @OnClick(R.id.more_operation)
        void onMoreOperation() {
            FeedItem feedItem = (FeedItem) mItemData;
            FeedContent feedContent = feedItem.getStFeedContent();
            long pubAccountId = feedContent.getIPubAccountId();
            long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
            int type = pubAccountId == accountId ? FeedOperationDialog.TYPE_MY_FEED : FeedOperationDialog.TYPE_OTHERS_FEED;
            FeedOperationDialog feedOperationDialog = new FeedOperationDialog(mContext, type, mDtSecCode, mSecName, feedItem, mFromStock);
            feedOperationDialog.setFeedId(feedContent.getSFeedId());
            feedOperationDialog.show();
        }

        @OnClick(R.id.comment_text)
        void onCommentContentClicked() {
            FeedItem feedItem = (FeedItem) mItemData;
            FeedContent feedContent = feedItem.getStFeedContent();
            int selectionStart = mComment.getSelectionStart();
            int selectionEnd = mComment.getSelectionEnd();
            if ((selectionStart == -1 && selectionEnd == -1) //一般机型两个都是-1
                || (selectionStart == 0 && selectionEnd == 0)) { //魅族手机两个都是0
                // do your code here this will only call if its not a hyperlink
                openFeedDetailPage();
            }
        }

        private void openFeedDetailPage() {
            FeedItem feedItem = (FeedItem) mItemData;
            FeedContent feedContent = feedItem.getStFeedContent();
            String feedId = feedContent.getSFeedId();
            int feedType = feedContent.getEType();
            switch (feedType) {
                case E_FEED_TYPE.E_FT_STOCK_REVIEW:
                    WebBeaconJump.showCommentDetail(mContext, feedId);
                    StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_OPEN_DETAIL_PAGE_CLICKED);
                    break;
                case E_FEED_TYPE.E_FT_INVEST_MARKET:
                case E_FEED_TYPE.E_FT_INVEST_STOCK:
                    WebBeaconJump.showInvestmentAdviserDetail(mContext, feedId);
                    break;
                default:
                    break;
            }
        }

        @OnClick(R.id.comment_icon_layout)
        void onCommentIconClicked() {
            StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_NEW_REPLY_CLICKED);
            FeedItem feedItem = (FeedItem) mItemData;
            FeedContent feedContent = feedItem.getStFeedContent();
            String feedId = feedContent.getSFeedId();
            AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
            FeedUserBaseInfo userBaseInfo = mFeedUserBaseInfos.get(feedContent.iPubAccountId);
            String pubNickName = userBaseInfo != null ? userBaseInfo.sNickName : "";
            CommonBeaconJump.showCommentEditpage(mContext, mDtSecCode, mSecName, feedId, feedContent.getEType(), accountManager.getAccountId(), "", pubNickName);
        }

        @OnClick(R.id.like_layout)
        void onLikeIconClicked() {
            StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_LIKE_CLICKED);
            FeedItem feedItem = (FeedItem) mItemData;
            FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();
            int likeSize = feedExtendInfo.getILikeSize();
            FeedContent feedContent = feedItem.getStFeedContent();
            final String feedId = feedContent.getSFeedId();
            final long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();

            FeedLikeEntity feedLikeEntity = FeedRequestManager.getFeedLikeEntityFromDb(accountId, feedId);
            boolean isLike = !(feedLikeEntity != null && feedLikeEntity.isLike());
            mFeedRequestManager.doLike(feedId, feedContent.getEType(), isLike, mFeedRequestManager);

            if (isLike) {
                DengtaApplication.getApplication().getBonusPointManager().reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_PRAISE_FEED);
            }

            FeedRequestManager.saveFeedLike(accountId, feedId, isLike);
            feedExtendInfo.setILikeSize(isLike ? likeSize + 1 : likeSize - 1);
            refreshByLikeClicked(isLike);
        }

        void refreshByLikeClicked(final boolean clicked) {
            FeedItem feedItem = (FeedItem) mItemData;
            FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();
            int likeSize = feedExtendInfo.getILikeSize();
            if (clicked) {
                mLikeIcon.setImageDrawable(mDrawableLikeClicked);
                mLikeCount.setTextColor(mLikeClickedTextColor);
                mLikeCount.setText(String.valueOf(likeSize));
            } else {
                mLikeIcon.setImageDrawable(mDrawableLike);
                mLikeCount.setTextColor(mLikeTextColor);
                mLikeCount.setText(String.valueOf(likeSize));
            }
        }
    }

    private String getRelatedSecName(FeedContent feedContent) {
        ArrayList<SecCodeName> relateSecs = feedContent.getVRelateSec();
        if (relateSecs == null || relateSecs.size() == 0) {
            return "";
        }
        return relateSecs.get(0).getSSecName().trim();
    }

    private SecCodeName getRelatedSecCodeName(FeedContent feedContent) {
        ArrayList<SecCodeName> relateSecs = feedContent.getVRelateSec();
        if (relateSecs == null || relateSecs.size() == 0) {
            return null;
        }
        return relateSecs.get(0);
    }
}
