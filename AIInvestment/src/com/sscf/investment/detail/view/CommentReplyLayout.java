package com.sscf.investment.detail.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.FeedOperationDialog;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import BEC.E_FEED_TYPE;
import BEC.FeedCommentInfo;
import BEC.FeedCommentInfoList;
import BEC.FeedUserBaseInfo;
import BEC.ReplyCommentInfo;

/**
 * Created by liqf on 2016/9/8.
 */
public class CommentReplyLayout extends LinearLayout implements View.OnClickListener {
    private static final int MAX_COMMENT_REPLY_LENGTH = 50;
    private int mNormalNameColor;
    private int mMemberNameColor;
    private int mMoreTextColor;
    private LinearLayout mReplyItems;
    private int mTopSpaceHeight;
    private int mInnerSpaceHeight;
    private int mInnerEdgeSpaceHeight;
    private int mBottomSpaceHeight;
    private ArrayList<FeedCommentInfo> mReplyInfos = new ArrayList<>();
    private Map<Long, FeedUserBaseInfo> mFeedUserBaseInfos = new HashMap<>();

    private TextView mMoreText;
    private LayoutInflater mInflater;

    private String mDtSecCode;
    private String mSecName;
    private String mFeedId;

    private Space mSpaceBelowMore;

    public CommentReplyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.comment_reply_layout, this, true);
        mReplyItems = (LinearLayout) findViewById(R.id.reply_items);

        initResources(context);
    }

    public void setSecInfo(String dtSecCode, String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;
    }

    public void setFeedId(String feedId) {
        mFeedId = feedId;
    }

    private void initViews(Context context) {
        mReplyItems.removeAllViews();

//        Space topSpace = new Space(context);
//        topSpace.setMinimumHeight(mTopSpaceHeight);
//        mReplyItems.addView(topSpace);

        Space topEdgeSpace = new Space(context);
        topEdgeSpace.setMinimumHeight(mInnerEdgeSpaceHeight);
        mReplyItems.addView(topEdgeSpace);

        for (int i = 0; i < mReplyInfos.size(); i++) {
            TextView reply = generateReplyView();
            reply.setTag(i);
            reply.setOnClickListener(this);
            mReplyItems.addView(reply);
        }

        mMoreText = generateReplyView();
        mMoreText.setTextColor(mMoreTextColor);
        mReplyItems.addView(mMoreText);

        mSpaceBelowMore = new Space(context);
        mSpaceBelowMore.setMinimumHeight(mInnerEdgeSpaceHeight);
        mReplyItems.addView(mSpaceBelowMore);

//        Space bottomSpace = new Space(context);
//        bottomSpace.setMinimumHeight(mBottomSpaceHeight);
//        addView(bottomSpace);
    }

    private void initResources(Context context) {
        Resources resources = context.getResources();
        mNormalNameColor = ContextCompat.getColor(context, R.color.normal_user_name_color);
        mMemberNameColor = ContextCompat.getColor(context, R.color.member_user_name_color);
        mMoreTextColor = ContextCompat.getColor(context, R.color.tag_relatedStock);

        mTopSpaceHeight = resources.getDimensionPixelSize(R.dimen.comment_reply_top_space_height);
        mInnerSpaceHeight = resources.getDimensionPixelSize(R.dimen.comment_reply_inner_space_height);
        mInnerEdgeSpaceHeight = resources.getDimensionPixelSize(R.dimen.comment_reply_inner_edge_space_height);
        mBottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.comment_reply_bottom_space_height);
    }

    private int getNickNameTextColor(FeedCommentInfo commentInfo) {
        return isMember(commentInfo) ? mMemberNameColor : mNormalNameColor;
    }

    private int getNickNameTextColor(ReplyCommentInfo replyCommentInfo) {
        return isMember(replyCommentInfo) ? mMemberNameColor : mNormalNameColor;
    }

    private boolean isMember(FeedCommentInfo commentInfo) {
        FeedUserBaseInfo info = mFeedUserBaseInfos.get(commentInfo.getIPubAccountId());
        if(info != null) {
            return AccountManager.isMember(info);
        }
        return false;
    }

    private boolean isMember(ReplyCommentInfo replyCommentInfo) {
        FeedUserBaseInfo info = mFeedUserBaseInfos.get(replyCommentInfo.getIReplyAccountId());
        if(info != null) {
            return AccountManager.isMember(info);
        }
        return false;
    }

    @UiThread
    public void setData(FeedCommentInfoList commentInfos, Map<Long, FeedUserBaseInfo> feedUserBaseInfos, int totalCommentCount, int feedType) {
        mReplyInfos.clear();
        ArrayList<FeedCommentInfo> replyInfos = commentInfos.getVCommentList();
        mReplyInfos.addAll(replyInfos);
        mFeedUserBaseInfos.putAll(feedUserBaseInfos);

        int size = mReplyInfos.size();

        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
            return;
        }

        initViews(getContext());

        for (int i = 0; i < size; i++) {
            TextView replyText = (TextView) mReplyItems.findViewWithTag(i);

            FeedCommentInfo feedCommentInfo = mReplyInfos.get(i);
            String content = feedCommentInfo.getSContent();
            final long pubAccountId = feedCommentInfo.getIPubAccountId();
            final long replyAccountId = feedCommentInfo.getStReplyComment().getIReplyAccountId();
            final String commentNickName = feedCommentInfo.getSCommentNickName();
            final String replyNickName = feedCommentInfo.getStReplyComment().getSReplyNickName();
            if (TextUtils.isEmpty(feedCommentInfo.getStReplyComment().getSReplyCommentId())) {
                content = commentNickName + "：" + content;
                content = StringUtil.getStringWithMaxLength(content, MAX_COMMENT_REPLY_LENGTH);
                SpannableString clickSpan = new SpannableString(content);
                clickSpan.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getNickNameTextColor(feedCommentInfo));
                        ds.setUnderlineText(false);
                    }

                    @Override
                    public void onClick(View widget) {
                        CommonBeaconJump.showHomepage(getContext(), pubAccountId);
                    }
                }, 0, commentNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                replyText.setText(clickSpan);
            } else {
                content = commentNickName + "回复" + replyNickName + "：" + content;
                content = StringUtil.getStringWithMaxLength(content, MAX_COMMENT_REPLY_LENGTH);
                SpannableString clickSpan = new SpannableString(content);

                clickSpan.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getNickNameTextColor(feedCommentInfo));
                        ds.setUnderlineText(false);
                    }

                    @Override
                    public void onClick(View widget) {
                        CommonBeaconJump.showHomepage(getContext(), pubAccountId);
                    }
                }, 0, commentNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                clickSpan.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getNickNameTextColor(feedCommentInfo.getStReplyComment()));
                        ds.setUnderlineText(false);
                    }

                    @Override
                    public void onClick(View widget) {
                        CommonBeaconJump.showHomepage(getContext(), replyAccountId);
                    }
                }, commentNickName.length() + 2, commentNickName.length() + 2 + replyNickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                replyText.setText(clickSpan);
            }
            replyText.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (totalCommentCount > size) {
            mMoreText.setText(getResources().getString(R.string.comment_reply_more, String.valueOf(totalCommentCount - size)));
            mSpaceBelowMore.setVisibility(VISIBLE);
            mMoreText.setVisibility(VISIBLE);
            mMoreText.setOnClickListener(v -> {
                if (feedType == E_FEED_TYPE.E_FT_STOCK_REVIEW) {
                    WebBeaconJump.showCommentDetail(getContext(), mFeedId);
                } else {
                    WebBeaconJump.showInvestmentAdviserDetail(getContext(), mFeedId);
                }
            });
        } else {
            mSpaceBelowMore.setVisibility(VISIBLE);
            mMoreText.setVisibility(GONE);
        }
    }

    private TextView generateReplyView() {
        return (TextView) mInflater.inflate(R.layout.comment_reply_text, mReplyItems, false);
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v;
        int selectionStart = textView.getSelectionStart();
        int selectionEnd = textView.getSelectionEnd();
        if ((selectionStart == -1 && selectionEnd == -1) //一般机型两个都是-1
            || (selectionStart == 0 && selectionEnd == 0)) { //魅族手机两个都是0
            // do your code here this will only call if its not a hyperlink
            int index = (int) v.getTag();
            FeedCommentInfo feedCommentInfo = mReplyInfos.get(index);
            String feedId = feedCommentInfo.getSFeedId();

            long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
            if (feedCommentInfo.getIPubAccountId() == accountId) {
                String commentId = feedCommentInfo.getSCommentId();
                FeedOperationDialog feedOperationDialog = new FeedOperationDialog(getContext(), FeedOperationDialog.TYPE_MY_COMMENT, mDtSecCode, mSecName, null, false);
                feedOperationDialog.setCommentIds(feedId, commentId);
                feedOperationDialog.show();
            } else {
                StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_NEW_REPLY_CLICKED);
                CommonBeaconJump.showCommentEditpage(getContext(), mDtSecCode, mSecName, feedId, E_FEED_TYPE.E_FT_STOCK_REVIEW, feedCommentInfo.getIPubAccountId(), feedCommentInfo.getSCommentId(), feedCommentInfo.getSCommentNickName());
            }
        }
    }
}
