package com.sscf.investment.payment;

import android.content.Context;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.payment.entity.PaymentMethodEntity;

import java.util.List;

import BEC.E_DT_PAY_TYPE;

/**
 * Created by yorkeehuang on 2017/10/19.
 */

class PaymentMethodAdapter extends CommonBaseRecyclerViewAdapter<PaymentMethodEntity> {
    private int mCheckedIndex;

    public PaymentMethodAdapter(Context context, List<PaymentMethodEntity> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, PaymentMethodEntity item, int position) {
        holder.setImageResource(R.id.icon, item.iconId);
        holder.setText(R.id.name, item.nameId);
        holder.setText(R.id.intro, item.introId);
        holder.setImageResource(R.id.checkButton, position == mCheckedIndex ? R.drawable.checkbox_checked : R.drawable.checkbox_normal);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        mCheckedIndex = position;
        notifyDataSetChanged();
    }

    int getPayType() {
        switch (mCheckedIndex) {
            case 1:
                return E_DT_PAY_TYPE.E_DT_PAY_WX;
            default:
                return E_DT_PAY_TYPE.E_DT_PAY_ALI;
        }
    }
}