package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.utils.PaymentInfoUtils;

import java.util.ArrayList;
import java.util.List;

import BEC.DtMemberFeeItem;

/**
 * Created by yorkeehuang on 2017/2/8.
 */

public class FeeListView extends LinearLayout implements View.OnClickListener{

    private View.OnClickListener mOnClickListener;
    private int mItemHeight;
    private List<View> mItemViewList = new ArrayList<>();
    private View mSelectedItemView;

    public FeeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItemHeight = getResources().getDimensionPixelSize(R.dimen.fee_list_item_height);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setData(ArrayList<DtMemberFeeItem> feeItemList) {
        initItemViews(feeItemList);
    }

    private void initItemViews(ArrayList<DtMemberFeeItem> feeItemList) {
        removeAllViews();
        mItemViewList.clear();
        int marginLeftRight = DeviceUtil.dip2px(getContext(), 15);

        DtMemberFeeItem selectedFeeItem = null;
        if(mSelectedItemView != null) {
            selectedFeeItem = (DtMemberFeeItem) mSelectedItemView.getTag();
        }

        int itemMarginBottom = DeviceUtil.dip2px(getContext(), 6);
        for(int i=0, size=feeItemList.size(); i<size; i++) {
            DtMemberFeeItem feeItem = feeItemList.get(i);
            View itemView = View.inflate(getContext(), R.layout.member_fee_item, null);
            mItemViewList.add(itemView);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            if(i == size - 1) {
                lp.setMargins(marginLeftRight, 0, marginLeftRight, 0);
            } else {
                lp.setMargins(marginLeftRight, 0, marginLeftRight, itemMarginBottom);
            }
            addView(itemView, lp);

            boolean selected;
            if(selectedFeeItem == null) {
                selected = feeItem.getBDefaultChoosed();
            } else {
                selected = equalsDtMemberFeeItem(selectedFeeItem, feeItem);
            }

            if(selected) {
                mSelectedItemView = itemView;
            }

            itemView.setSelected(selected);
            itemView.setTag(feeItem);

            TextView monthView = (TextView) itemView.findViewById(R.id.month);
            monthView.setText(getContext().getString(R.string.member_fee_item_month, String.valueOf(feeItem.getIMonthNum())));


            final TextView descView = (TextView) itemView.findViewById(R.id.desc);
            if(TextUtils.isEmpty(feeItem.getSDesc())) {
                descView.setVisibility(View.GONE);
            } else {
                descView.setVisibility(View.VISIBLE);
                descView.setText(feeItem.getSDesc());
                if(feeItem.getBLineCrossed()) {
                    descView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                } else {
                    descView.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                }
            }

            final TextView totalMoneyView = (TextView) itemView.findViewById(R.id.total_money);
            PaymentInfoUtils.setPriceText(totalMoneyView, feeItem.getITotalMoney());

            final TextView tagView = (TextView) itemView.findViewById(R.id.tag);
            if(TextUtils.isEmpty(feeItem.getSTag())) {
                tagView.setVisibility(View.GONE);
            } else {
                tagView.setVisibility(View.VISIBLE);
                tagView.setText(feeItem.getSTag());
            }

            itemView.setOnClickListener(this);
        }

        if(!mItemViewList.isEmpty()) {
            Button payButton = (Button) View.inflate(getContext(), R.layout.open_member_button, null);
            payButton.setOnClickListener(mOnClickListener);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dip2px(getContext(), 48));
            int payButtonMarginTopBottom = DeviceUtil.dip2px(getContext(), 24);
            lp.setMargins(marginLeftRight, payButtonMarginTopBottom, marginLeftRight, payButtonMarginTopBottom);
            addView(payButton, lp);
        }
    }

    private boolean equalsDtMemberFeeItem(DtMemberFeeItem src, DtMemberFeeItem target) {
        return src.getIMonthNum() == target.getIMonthNum() && src.getIUnit() == target.getIUnit();
    }

    @Override
    public void onClick(View v) {
        for(View itemView : mItemViewList) {
            itemView.setSelected(itemView == v);
            mSelectedItemView = v;
        }
    }

    public DtMemberFeeItem getSelectedItem() {
        if(mSelectedItemView != null) {
            return (DtMemberFeeItem) mSelectedItemView.getTag();
        }
        return null;
    }
}
