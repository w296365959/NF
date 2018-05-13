package com.sscf.investment.message.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import butterknife.BindView;

public final class MessageRemindAdapter extends BaseMessageRemindAdapter {

    public MessageRemindAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MESSAGE) {
            return new MessageHolder(mInflater.inflate(R.layout.message_announcement_research_report_list_item, parent, false));
        } else if (viewType == TYPE_DATE) {
            return new DateHolder(mInflater.inflate(R.layout.message_date_item, parent, false));
        }
        return null;
    }

    final class MessageHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
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
            name.setText(item.title);
            time.setText(item.getPublishTimeString());
            title.setText(item.content);
        }

        @Override
        public void onItemClicked() {
            if (mItemData != null && mItemData instanceof RemindedMessageItem) {
                final RemindedMessageItem item = (RemindedMessageItem) mItemData;
                switch (item.messageType) {
                    case RemindedMessageItem.TYPE_DAILY_REPORT:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_PORTFOLIO_DAILY_ITEM);
                        break;
                    case RemindedMessageItem.TYPE_NEW_SHARE:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_NEW_SHARE_ITEM);
                        break;
                    case RemindedMessageItem.TYPE_ACTIVITY:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_ACTIVITIES_ITEM);
                        break;
                    case RemindedMessageItem.TYPE_VALUEADDED_SERVICES:
                        StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_VALUEADDED_SERVICES_ITEM);
                        break;
                    default:
                        break;
                }
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    scheme.handleUrl(mContext, item.infoUrl);
                }
            }
        }
    }
}