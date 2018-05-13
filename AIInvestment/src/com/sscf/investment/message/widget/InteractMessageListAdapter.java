package com.sscf.investment.message.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import BEC.E_FEED_TYPE;
import butterknife.BindView;
import butterknife.OnClick;

public final class InteractMessageListAdapter extends BaseMessageRemindAdapter {
    final DisplayImageOptions mDefaultOptions;

    public InteractMessageListAdapter(Context context) {
        super(context);
        mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MESSAGE) {
            return new MessageHolder(mInflater.inflate(R.layout.message_interact_message_list_item, parent, false));
        } else if (viewType == TYPE_DATE) {
            return new DateHolder(mInflater.inflate(R.layout.message_date_item, parent, false));
        }
        return null;
    }

    class MessageHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.iconV) ImageView iconV;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;
        public MessageHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final RemindedMessageItem item = (RemindedMessageItem) itemData;
            ImageLoaderUtils.getImageLoader().displayImage(item.userIconUrl, icon, mDefaultOptions);
            name.setText(item.nickname);
            time.setText(item.getPublishTimeString());
            iconV.setVisibility(item.userType ? View.VISIBLE : View.GONE);
            switch (item.messageType) {
                case RemindedMessageItem.TYPE_INTERACT_MESSAGE:
                    title.setText(mContext.getString(R.string.reply_to, item.message));
                    break;
                case RemindedMessageItem.TYPE_INVESTMENT_ADVISER_EVENTS:
                    title.setText(item.message);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClicked() {
            if (mItemData != null && mItemData instanceof RemindedMessageItem) {
                final RemindedMessageItem item = (RemindedMessageItem) mItemData;
                switch (item.messageType) {
                    case RemindedMessageItem.TYPE_INTERACT_MESSAGE:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_INTERACT_MESSAGE_ITEM);
                        if (item.feedType == E_FEED_TYPE.E_FT_STOCK_REVIEW) {
                            WebBeaconJump.showCommentDetail(mContext, item.newsId);
                        } else {
                            WebBeaconJump.showInvestmentAdviserDetail(mContext, item.newsId);
                        }
                        break;
                    case RemindedMessageItem.TYPE_INVESTMENT_ADVISER_EVENTS:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_INVESTMENT_ADVISER_EVENTS_ITEM);
                        WebBeaconJump.showInvestmentAdviserDetail(mContext, item.newsId);
                        break;
                    default:
                        break;
                }
            }
        }

        @OnClick({R.id.icon, R.id.iconV, R.id.name})
        public void showUserHomePage() {
            CommonBeaconJump.showHomepage(mContext, ((RemindedMessageItem) mItemData).accountId);
        }
    }
}