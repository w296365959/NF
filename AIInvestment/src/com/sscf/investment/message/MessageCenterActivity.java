package com.sscf.investment.message;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.stat.StatConsts;
import com.sscf.investment.sdk.stat.StatManager;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.message.manager.MessageCenterManager;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import java.util.ArrayList;
import java.util.List;
import BEC.AlertMsgClassDesc;
import BEC.BEACON_STAT_TYPE;
import BEC.E_MSG_CLASS;

/**
 * davidwei
 * 消息列表
 */
@Route("MessageCenterActivity")
public final class MessageCenterActivity extends BaseFragmentActivity implements View.OnClickListener, MessageCenterManager.OnGetMessageClassListListener {
    private RecyclerView mRecyclerView;
    private CommonBaseRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center);

        initViews();

        DengtaApplication.getApplication().getMessageCenterManager().setOnGetMessageClassListListener(this);

        StatisticsUtil.reportAction(StatisticsConst.SETTING_MESSAGE_CENTER_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DengtaApplication.getApplication().getMessageCenterManager().setOnGetMessageClassListListener(null);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.message_center);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MessageCenterAdapter adapter = new MessageCenterAdapter(this, DengtaApplication.getApplication()
                .getMessageCenterManager().getMessageClassList(), R.layout.message_center_item);
        mAdapter = adapter;
        adapter.setItemClickable(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void requestData() {
        DengtaApplication.getApplication().getMessageCenterManager().requestMessageCenter();
    }

    @Override
    public void onGetMessageClassList(ArrayList<AlertMsgClassDesc> classList) {
        mAdapter.setData(classList);
        mAdapter.notifyDataSetChanged();
    }
}

final class MessageCenterAdapter extends CommonBaseRecyclerViewAdapter<AlertMsgClassDesc> {
    final DisplayImageOptions mDefaultOptions;

    MessageCenterAdapter(Context context, List<AlertMsgClassDesc> data, int itemLayoutId) {
        super(context, data, itemLayoutId);

        mDefaultOptions = ImageLoaderUtils.buildDisplayImageOptions(R.drawable.default_consultant_face);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, AlertMsgClassDesc item, int position) {
        final ImageView icon = holder.getView(R.id.icon);
        ImageLoaderUtils.getImageLoader().displayImage(item.sAndroidIconUrl, icon, mDefaultOptions);

        holder.setText(R.id.title, item.sClassName);

        if (TextUtils.isEmpty(item.sMsg)) {
            holder.setText(R.id.message, getMessageBlankTextId(item.iClassID));
        } else {
            holder.setText(R.id.message, item.sMsg);
        }

        if (item.iPushTime > 0) {
            holder.setText(R.id.time, TimeUtils.timeStamp2Date(item.iPushTime * 1000L));
        } else {
            holder.setText(R.id.time, "");
        }

        final TextView countView = holder.getView(R.id.count);
        final int count = DengtaApplication.getApplication().getMessageCenterManager().getUnreadMessageCount(item.iClassID);
        if (count > 0) {
            countView.setText(String.valueOf(count));
            countView.setVisibility(View.VISIBLE);
        } else {
            countView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final AlertMsgClassDesc item = getItem(position);
        final int classId = item.iClassID;
        switch (classId) {
            case E_MSG_CLASS.E_MC_DISC_NEWS:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.DISC_NEWS);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_IMPORTANT_NEWS_LIST);
                break;
            case E_MSG_CLASS.E_MC_SEC_PRICE:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.SEC_PRICE);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_STOCK_REMIND_LIST);
                break;
            case E_MSG_CLASS.E_MC_ANN_REP:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.ANNOUNCEMENT);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_ANNOUNCEMENT_RESEARCH_LIST);
                break;
            case E_MSG_CLASS.E_MC_DAILY_REPORT:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.DAILY_REPORT);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_PORTFOLIO_DAILY_LIST);
                break;
            case E_MSG_CLASS.E_MC_TG:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.CONSULTANT);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_INVESTMENT_ADVISER_EVENTS_LIST);
                break;
            case E_MSG_CLASS.E_MC_INTERACTION:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.INTERACTION);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_INTERACT_MESSAGE_LIST);
                break;
            case E_MSG_CLASS.E_MC_NEW_STOCK:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.NEW_STOCK);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_NEW_SHARE_LIST);
                break;
            case E_MSG_CLASS.E_MC_ACTIVITY:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.ACTIVITY);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_ACTIVITIES_LIST);
                break;
            case E_MSG_CLASS.E_MC_VALUE_ADDED_SERVICE:
                StatManager.getInstance().stat(BEACON_STAT_TYPE.E_BST_VIEW_MSG_CENTER, StatConsts.VALUE_ADDED_SERVICE);
                StatisticsUtil.reportAction(StatisticsConst.MESSAGE_CENTER_CLICK_VALUEADDED_SERVICES_LIST);
                break;
            default: // 不支持的类型直接返回
                return;
        }
        MessageDetailListActivity.show(mContext, classId, item.sClassName);
        final MessageCenterManager manager = DengtaApplication.getApplication().getMessageCenterManager();
        manager.updateEndTime(classId, item.iPushTime);
        manager.clearUnreadMessageCount(classId);
        notifyItemChanged(position);
    }

    int getMessageBlankTextId(final int classId) {
        int messageId = 0;
        switch (classId) {
            case E_MSG_CLASS.E_MC_DISC_NEWS:
                messageId = R.string.message_important_news_tips;
                break;
            case E_MSG_CLASS.E_MC_SEC_PRICE:
                messageId = R.string.message_stock_price_remind_tips;
                break;
            case E_MSG_CLASS.E_MC_ANN_REP:
                messageId = R.string.message_announcement_research_report_tips;
                break;
            case E_MSG_CLASS.E_MC_DAILY_REPORT:
                messageId = R.string.message_portfolio_daily_report_tips;
                break;
            case E_MSG_CLASS.E_MC_TG:
                messageId = R.string.message_investment_adviser_events_tips;
                break;
            case E_MSG_CLASS.E_MC_INTERACTION:
                messageId = R.string.message_interact_message_tips;
                break;
            case E_MSG_CLASS.E_MC_NEW_STOCK:
                messageId = R.string.message_new_share_remind_tips;
                break;
            case E_MSG_CLASS.E_MC_ACTIVITY:
                messageId = R.string.message_activities_remind_tips;
                break;
            case E_MSG_CLASS.E_MC_VALUE_ADDED_SERVICE:
                messageId = R.string.message_valueadded_services_remind_tips;
                break;
            default:
                break;
        }
        return messageId;
    }
}
