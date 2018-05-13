package com.sscf.investment.market;

import android.content.Context;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;

/**
 * Created by davidwei on 2017/04/01
 * 工具区的小工具item
 */
public final class MarketToolsItem extends ToolsItem {

    public static ToolsItem createCapitalFlow() {
        final MarketToolsItem item = new MarketToolsItem();
        item.drawableId = R.drawable.market_tools_capital_flow_icon;
        item.textId = R.string.market_tools_capital_flow;
        return item;
    }

    public static ToolsItem createNewShareCenter() {
        final MarketToolsItem item = new MarketToolsItem();
        item.drawableId = R.drawable.market_tools_new_share_icon;
        item.textId = R.string.setting_new_share;
        return item;
    }

    public static ToolsItem createDragonTigerBillboard() {
        final MarketToolsItem item = new MarketToolsItem();
        item.drawableId = R.drawable.market_tools_dragon_tiger_billboard_icon;
        item.textId = R.string.setting_dragon_tiger_billboard;
        return item;
    }

    public static ToolsItem createMarketDirectionAddItem() {
        final MarketToolsItem item = new MarketToolsItem();
        item.drawableId = R.drawable.market_tools_direction_add_icon;
        item.textId = R.string.direction_add_nuggets;
        return item;
    }

    public static ToolsItem createMarketMargin() {
        final MarketToolsItem item = new MarketToolsItem();
        item.drawableId = R.drawable.market_tools_margin_icon;
        item.textId = R.string.market_tools_margin;
        return item;
    }

    @Override
    public void click(Context context) {
        switch (textId) {
            case R.string.setting_new_share:
                WebBeaconJump.showNewShare(context);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_NEW_SHARE);
                break;
            case R.string.setting_dragon_tiger_billboard:
                WebBeaconJump.showDragonTigerBillboard(context);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_DRAGON_TIGER_BILLBOARD);
                break;
            case R.string.market_tools_capital_flow: // 资金流
                CommonBeaconJump.showCapitalFlow(context);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_CAPITAL_FLOW);
                break;
            case R.string.direction_add_nuggets:
                WebBeaconJump.showDirectionAddListList(context);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_DIRECTION_ADD_NUGGETS);
                break;
            case R.string.market_tools_margin:
                WebBeaconJump.showMarketMargin(context);
                StatisticsUtil.reportAction(StatisticsConst.MARKET_CHINA_CLICK_MARKET_MARGIN);
                break;
            default:
                break;
        }
    }
}
