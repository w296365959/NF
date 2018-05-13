package com.sscf.investment.message.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.AttitudeColorView;
import butterknife.BindView;

public final class ImportantNewsMessageListAdapter extends BaseMessageRemindAdapter {

    public ImportantNewsMessageListAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MESSAGE) {
            return new NewsHolder(mInflater.inflate(R.layout.message_important_news_list_item, parent, false));
        } else if (viewType == TYPE_DATE) {
            return new DateHolder(mInflater.inflate(R.layout.message_date_item, parent, false));
        }
        return null;
    }

    final class NewsHolder extends CommonViewHolder {
        @BindView(R.id.time) TextView time;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.attitude_view) AttitudeColorView attitudeView;

        public NewsHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final RemindedMessageItem item = (RemindedMessageItem) itemData;
            title.setText(item.title);
            time.setText(item.getPublishTimeString());
            attitudeView.setData(item.tagInfos);
        }

        @Override
        public void onItemClicked() {
            if (mItemData != null && mItemData instanceof RemindedMessageItem) {
                final RemindedMessageItem item = (RemindedMessageItem) mItemData;
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_IMPORTANT_NEWS_ITEM);
                showNews(mContext, item);
            }
        }
    }

    public static void showNews(final Context context, final RemindedMessageItem item) {
        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        long accountId = 0;
        if (accountInfo != null) {
            accountId = accountInfo.id;
        }

        final FavorItem favorItem = new FavorItem(accountId, item);
        WebBeaconJump.showWebActivity(context, item.infoUrl, favorItem);
    }
}
