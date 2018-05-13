package com.sscf.investment.detail.entity;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.ToolsItem;

/**
 * Created by davidwei on 2017/06/13
 */
public final class MoreOperationToolsItem {

    public static ToolsItem createSimilarKItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_similar_k;
        item.textId = R.string.operation_similar_k;
        return item;
    }

    public static ToolsItem createSecHistoryItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_history;
        item.textId = R.string.operation_history;
        return item;
    }

    public static ToolsItem createMarginTrackingItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_margin_tracking;
        item.textId = R.string.operation_margin_tracking;
        return item;
    }

    public static ToolsItem createMemoItem(final boolean hasMemo) {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_memo;
        item.textId = hasMemo ? R.string.operation_memo_edit : R.string.operation_memo_add;
        return item;
    }

    public static ToolsItem createAdd2GroupItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_add2group;
        item.textId = R.string.operation_add_group;
        return item;
    }

    public static ToolsItem createShareItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_share;
        item.textId = R.string.operation_share;
        return item;
    }

    public static ToolsItem createKLineSettingItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_kline_setting;
        item.textId = R.string.operation_kline_setting;
        return item;
    }

    public static ToolsItem createBSItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_bs;
        item.textId = R.string.operation_dk;
        return item;
    }

    public static ToolsItem createStockFigureItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_figure;
        item.textId = R.string.operation_figure;
        return item;
    }

    public static ToolsItem createCommentItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_comment;
        item.textId = R.string.operation_comment;
        return item;
    }

    public static ToolsItem createDirectionAddNuggetsItem() {
        final ToolsItem item = new ToolsItem();
        item.drawableId = R.drawable.operation_bar_direction_add;
        item.textId = R.string.direction_add_nuggets;
        return item;
    }
}
