package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.social.FeedRecyclerViewAdapter;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import BEC.E_FEED_INVEST_ATTI_TYPE;
import BEC.E_FEED_USER_TYPE;
import BEC.FeedContent;
import BEC.FeedExtendInfo;
import BEC.FeedItem;
import BEC.FeedUserBaseInfo;
import BEC.GetFeedListRsp;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * Created by liqf on 2016/10/10.
 */

public class ConsultantOpinionListLayout extends LinearLayout {
    public static final int MAX_SHOW_COUNT = 3;
    private Context mContext;
    private LayoutInflater mInflater;
    private DisplayImageOptions mDefaultOptions;
    private int mColorBase;

    @BindDrawable(R.drawable.attitude_rise) Drawable mAttiDrawableGood;
    @BindDrawable(R.drawable.attitude_down) Drawable mAttiDrawableBad;

    public Map<Long, FeedUserBaseInfo> mFeedUserBaseInfos = new HashMap<>();

    public ConsultantOpinionListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        ButterKnife.bind(this);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mColorBase = ContextCompat.getColor(mContext, R.color.default_text_color_60);
        mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
    }

    public void setData(GetFeedListRsp rsp) {
        if (rsp == null) {
            return;
        }

        removeAllViews();

        Map<Long, FeedUserBaseInfo> feedUserBaseInfos = rsp.getMFeedUserBaseInfo();
        mFeedUserBaseInfos.putAll(feedUserBaseInfos);

        ArrayList<FeedItem> originFeedItems = rsp.getVFeedItem();
        ArrayList<FeedItem> feedItems = new ArrayList<>();
        if (originFeedItems.size() > MAX_SHOW_COUNT) {
            for (int i = 0; i < MAX_SHOW_COUNT; i++) {
                feedItems.add(originFeedItems.get(i));
            }
        } else {
            feedItems.addAll(originFeedItems);
        }

        int size = feedItems.size();
        for (int i = 0; i < size; i++) {
            FeedItem feedItem = feedItems.get(i);
            addView(generateItemView(feedItem));
            if (i < size - 1) {
                addView(generateDividerView());
            }
        }
    }

    private View generateDividerView() {
        return mInflater.inflate(R.layout.common_divider, this, false);
    }

    private View generateItemView(FeedItem feedItem) {
        View itemView = mInflater.inflate(R.layout.consultant_opinion_item, this, false);

        ImageView mUserIcon = (ImageView) itemView.findViewById(R.id.user_icon);
        ImageView mUserIconV = (ImageView) itemView.findViewById(R.id.user_icon_v);
        TextView mTitle = (TextView) itemView.findViewById(R.id.title);
        TextView mSummary = (TextView) itemView.findViewById(R.id.summary);
        TextView mUserName = (TextView) itemView.findViewById(R.id.name);
        TextView mTime = (TextView) itemView.findViewById(R.id.time);
        TextView mPageViewCount = (TextView) itemView.findViewById(R.id.page_view_count);

        FeedContent feedContent = feedItem.getStFeedContent();
        long pubAccountId = feedContent.getIPubAccountId();
        FeedUserBaseInfo userBaseInfo = mFeedUserBaseInfos.get(pubAccountId);
        FeedExtendInfo feedExtendInfo = feedItem.getStFeedExtendInfo();

        itemView.setOnClickListener(v -> {
            mTitle.setTextColor(mColorBase);
            mSummary.setTextColor(mColorBase);

            int accessCount = feedExtendInfo.getIAccessCount();
            accessCount++;
            feedExtendInfo.setIAccessCount(accessCount);
            mPageViewCount.setText(getResources().getString(R.string.consultant_page_view_count, accessCount));

            //打开投顾详情页面
            WebBeaconJump.showCommonWebActivity(mContext, DengtaApplication.getApplication().getUrlManager().getOpinionDetailUrl(feedContent.getSFeedId()));
            StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_TAB_CONSULTANT_CLICK_ITEM);
        });

        mTitle.setText(feedContent.getSTitle());

        SpannableString summaryString = getSummaryString(feedContent);
        mSummary.setText(summaryString);
        mSummary.setVisibility(TextUtils.isEmpty(summaryString) ? View.GONE : View.VISIBLE);

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
        } else {
            mUserName.setText("");
            mUserIcon.setImageResource(R.drawable.default_consultant_face);
            mUserIconV.setVisibility(View.GONE);
        }

        mTime.setText(TimeUtils.timeStamp2Date(feedContent.getIPubTime() * 1000L));

        int accessCount = feedExtendInfo.getIAccessCount();
        mPageViewCount.setText(mContext.getString(R.string.consultant_page_view_count, accessCount));

        return itemView;
    }

    private SpannableString getSummaryString(FeedContent feedContent) {
        int attiType = feedContent.getEAttiType();
//            attiType = E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD; //for test
        String content = attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_NEUTRAL ? feedContent.getSDescription() : "??" + " " + feedContent.getSDescription();
        Drawable attiDrawable = null;
        if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD) {
            attiDrawable = mAttiDrawableGood;
        } else if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_BAD) {
            attiDrawable = mAttiDrawableBad;
        }
        if (attiDrawable != null) {
            attiDrawable.setBounds(0, 0, attiDrawable.getIntrinsicWidth(), attiDrawable.getIntrinsicHeight());
        }

        SpannableString spannableString = new SpannableString(StringUtil.getStringWithMaxLength(content, FeedRecyclerViewAdapter.MAX_CONSULTANT_CONTENT_LENGTH));
        if (attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_GOOD || attiType == E_FEED_INVEST_ATTI_TYPE.E_FIAT_BAD) {
            spannableString.setSpan(new ImageSpan(attiDrawable, ImageSpan.ALIGN_BASELINE), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
