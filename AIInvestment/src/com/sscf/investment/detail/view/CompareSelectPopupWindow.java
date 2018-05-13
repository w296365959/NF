package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.main.DataPref;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.utils.StockUtil;

/**
 * Created by yorkeehuang on 2017/12/15.
 */

public class CompareSelectPopupWindow extends PopupWindow {

    private Context mContext;

    private OnItemClick mOnItemClick;

    public CompareSelectPopupWindow(Context context) {
        super(DeviceUtil.dip2px(context, 74),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mContext = context;
        setContentView(LayoutInflater.from(context).inflate(R.layout.compare_select_window, null));
        setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public void showCompareSelectPopupWindow(String dtSecCode, View anchor, int gravity) {
        View view = getContentView();
        View.OnClickListener listener = v -> {
            if(mOnItemClick == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.market:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_STOCK_COMPARE_ITEM_CLICKED);
                    String marketDtSecCode = StockUtil.getStockMarketDtCode(dtSecCode);
                    if(!TextUtils.isEmpty(marketDtSecCode)) {
                        DataPref.setTimeLineCompareType(SettingConst.TIMELINE_COMPARE_TYPE_MARKET);
                        DataPref.setTimeLineCompareSecCode(marketDtSecCode);
                        mOnItemClick.onMarketClick(marketDtSecCode);
                    }
                    break;
                case R.id.custom:
                    StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_STOCK_COMPARE_ITEM_CLICKED);
                    mOnItemClick.onCustomClick();
                    break;
                case R.id.remove_compare:
                    DataPref.setTimeLineCompareType(SettingConst.TIMELINE_COMPARE_TYPE_NONE);
                    DataPref.setTimeLineCompareSecCode("");
                    mOnItemClick.onRemoveClick();
                    break;
                default:
            }
            dismiss();
        };
        int type = DataPref.getTimeLineCompareType();
        TextView marketItem = (TextView) view.findViewById(R.id.market);
        updateTextColor(type, SettingConst.TIMELINE_COMPARE_TYPE_MARKET, marketItem);
        marketItem.setOnClickListener(listener);
        TextView customItem = (TextView) view.findViewById(R.id.custom);
        updateTextColor(type, SettingConst.TIMELINE_COMPARE_TYPE_CUSTOM, customItem);
        customItem.setOnClickListener(listener);
        TextView removeCompareItem = (TextView) view.findViewById(R.id.remove_compare);
        removeCompareItem.setOnClickListener(listener);
        View dividerLine = view.findViewById(R.id.last_divider);
        if(hasSelectedCompare()) {
            removeCompareItem.setVisibility(View.VISIBLE);
            dividerLine.setVisibility(View.VISIBLE);
        } else {
            removeCompareItem.setVisibility(View.GONE);
            dividerLine.setVisibility(View.GONE);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            showAsDropDown(anchor, 0, 0, gravity);
        } else {
            showAsDropDown(anchor);
        }
    }

    private boolean hasSelectedCompare() {
        return !TextUtils.isEmpty(DataPref.getTimeLineCompareSecCode());
    }

    private void updateTextColor(int selectType, int currentType, TextView item) {
        if(selectType == currentType) {
            item.setTextColor(mContext.getResources().getColor(R.color.select_compare_stock_button_color));
        } else {
            item.setTextColor(mContext.getResources().getColor(R.color.default_text_color_100));
        }
    }

    public interface OnItemClick {
        void onMarketClick(String marketDtCode);

        void onCustomClick();

        void onRemoveClick();
    }
}
