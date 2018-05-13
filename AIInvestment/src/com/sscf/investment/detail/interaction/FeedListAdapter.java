package com.sscf.investment.detail.interaction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IFeedRequestManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.detail.view.CommentReplyLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.social.FeedRequestManager;
import com.sscf.investment.utils.StringUtil;

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
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class FeedListAdapter extends InteractionGroupAdapter<FeedItem> {

    private static final String TAG = "FeedRecyclerViewAdapter";
    private static final int MAX_FEED_CONTENT_LENGTH = 80;
    public static final int MAX_CONSULTANT_CONTENT_LENGTH = 68;

    private int mLikeTextColor;
    private int mLikeClickedTextColor;
    private DisplayImageOptions mDefaultOptions;

    private Drawable mAttiDrawableGood;
    private Drawable mAttiDrawableBad;
    private int mTextHighlightColor;

    private Drawable mDrawableLike;
    private Drawable mDrawableLikeClicked;

    private FeedRequestManager mFeedRequestManager;

    private boolean mIsCommentType;
    private boolean mIsHomepage;
    private long mAccountId;
    private boolean mFromStock;

    private Map<Long, FeedUserBaseInfo> mFeedUserBaseInfos = new HashMap<>();

    public FeedListAdapter(Context context) {
        super(context);
        initRes();
        mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
        mFeedRequestManager = DengtaApplication.getApplication().getFeedRequestManager();
    }

    private void initRes() {
        mLikeTextColor = ContextCompat.getColor(mContext, R.color.default_text_color_60);
        mLikeClickedTextColor = ContextCompat.getColor(mContext, R.color.tab_bar_text_selected_color);
        mTextHighlightColor = ContextCompat.getColor(mContext, R.color.tag_relatedStock);

        mAttiDrawableGood = ContextCompat.getDrawable(mContext, R.drawable.attitude_rise);
        mAttiDrawableBad = ContextCompat.getDrawable(mContext, R.drawable.attitude_down);
        mDrawableLike = ContextCompat.getDrawable(mContext, R.drawable.comment_like);
        mDrawableLikeClicked = ContextCompat.getDrawable(mContext, R.drawable.comment_like_clicked);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return null;
        }

        if (mInflater == null) {
            mInflater = LayoutInflater.from(mContext);
        }

        FeedViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.feed_list_item, null);
            viewHolder = new FeedViewHolder(convertView);
            viewHolder.mPosition = position;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FeedViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }

        viewHolder.update(getItem(position));
        return convertView;
    }

    public void putUserBaseInfos(Map<Long, FeedUserBaseInfo> feedUserBaseInfos) {
        mFeedUserBaseInfos.putAll(feedUserBaseInfos);
    }

    final class FeedViewHolder implements View.OnClickListener {
        private ImageView mUserIcon;
        private ImageView mUserIconV;
        private TextView mUserName;
        private ImageView mMemberStatusIcon;
        private TextView mTitle;
        private TextView mComment;
        private TextView mSummary;
        private TextView mTime;
        private TextView mCommentCount;
        private TextView mLikeCount;
        private ImageView mLikeIcon;
        private ImageView mCommentIcon;
        private CommentReplyLayout mReplyLayout;
        private String mDtSecCode;
        private String mSecName;


        private FeedItem mFeedItem;
        public int mPosition;

        public FeedViewHolder(View root) {
            mUserIconV = (ImageView) root.findViewById(R.id.user_icon_v);
            mUserIcon = (ImageView) root.findViewById(R.id.user_icon);
            mUserName = (TextView) root.findViewById(R.id.user_name);
            mMemberStatusIcon = (ImageView) root.findViewById(R.id.member_status_icon);
            mTitle = (TextView) root.findViewById(R.id.consultant_title);
            mComment = (TextView) root.findViewById(R.id.comment_text);
            root.findViewById(R.id.user_icon).setOnClickListener(this);
            root.findViewById(R.id.user_name).setOnClickListener(this);
            root.findViewById(R.id.comment_icon_layout).setVisibility(View.GONE);
            root.findViewById(R.id.like_layout).setVisibility(View.GONE);
            root.findViewById(R.id.more_operation).setVisibility(View.GONE);
            mSummary = (TextView) root.findViewById(R.id.comment_summary);
            mTime = (TextView) root.findViewById(R.id.time);
            mCommentCount = (TextView) root.findViewById(R.id.comment_count);
            mLikeCount = (TextView) root.findViewById(R.id.like_count);
            mLikeIcon = (ImageView) root.findViewById(R.id.like_icon);
            mCommentIcon = (ImageView) root.findViewById(R.id.comment_icon);
            mReplyLayout = (CommentReplyLayout) root.findViewById(R.id.reply_layout);
        }


        public void update(FeedItem feedItem) {
            mFeedItem = feedItem;
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
                    String content = StringUtil.getStringWithMaxLength(feedContent.getSContent(), MAX_FEED_CONTENT_LENGTH);
                    mComment.setText(content);
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
            refreshByLikeClicked(isLike, feedItem);
        }

        private SpannableString getSummaryString(FeedContent feedContent) {
            int attiType = feedContent.getEAttiType();
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

        void refreshByLikeClicked(final boolean clicked, FeedItem feedItem) {
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_icon:
                case R.id.user_name:
                    onUserIconClicked(mFeedItem);
                    break;
                default:
            }
        }

        private void onUserIconClicked(FeedItem feedItem) {
            long pubAccountId = feedItem.getStFeedContent().getIPubAccountId();
            if (mIsHomepage && mAccountId == pubAccountId) {
                return;
            }
            CommonBeaconJump.showHomepage(mContext, pubAccountId);
        }
    }
}


