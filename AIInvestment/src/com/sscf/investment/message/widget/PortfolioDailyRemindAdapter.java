package com.sscf.investment.message.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sscf.investment.R;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.WebBeaconJump;

import butterknife.BindView;

public final class PortfolioDailyRemindAdapter extends BaseMessageRemindAdapter {

    public PortfolioDailyRemindAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MESSAGE) {
            return new MessageHolder(mInflater.inflate(R.layout.message_portfolio_daily_list_item, parent, false));
        } else if (viewType == TYPE_DATE) {
            return new DateHolder(mInflater.inflate(R.layout.message_date_item, parent, false));
        }
        return null;
    }

    final class MessageHolder extends CommonViewHolder {
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;

        public MessageHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final RemindedMessageItem item = (RemindedMessageItem) itemData;
            title.setText(item.title);
            time.setText(item.getPublishTimeString());
        }

        @Override
        public void onItemClicked() {
            if (mItemData != null && mItemData instanceof RemindedMessageItem) {
                final RemindedMessageItem item = (RemindedMessageItem) mItemData;
                switch (item.messageType) {
                    case RemindedMessageItem.TYPE_DAILY_REPORT:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_PORTFOLIO_DAILY_ITEM);
                        WebBeaconJump.showCommonWebActivity(mContext, item.infoUrl);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}