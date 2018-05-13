package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.entity.BuySellEntity;

/**
 * 多空View
 */
public class DKView extends LinearLayout {
    private TextView duoTextView;
    private TextView kongTextView;

    private float viewCorner;
    private float viewWidth;

    private BuySellEntity mBuySellEntity;

    public DKView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View root = LayoutInflater.from(context).inflate(R.layout.capital_flow_dkview, this);

        duoTextView = (TextView)root.findViewById(R.id.transaction_capital_up_text);
        kongTextView = (TextView)root.findViewById(R.id.transaction_capital_down_text);

        viewCorner = getContext().getResources().getDimensionPixelSize(R.dimen.capital_flow_detail_duo_kong_cornor);
        viewWidth = getContext().getResources().getDimensionPixelSize(R.dimen.capital_flow_duo_kong_width);
    }

    public void setBuySellData(BuySellEntity entity) {
        if (entity == null) {
            return;
        }

        mBuySellEntity = entity;

        post(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (mBuySellEntity != null) {
            float totalBuy = 0;
            float totalSell = 0;
            for(int i = 0; i < mBuySellEntity.getBuyValues().length; i++) {
                totalBuy += mBuySellEntity.getBuyValues()[i] * mBuySellEntity.getBuyVolumes()[i];
                totalSell += mBuySellEntity.getSellValues()[i] * mBuySellEntity.getSellVolumes()[i];
            }

            float buyWidth;
            if (totalBuy == 0 && totalSell == 0) {
                // 无5档数据的情况，如停牌的股票
                buyWidth = viewWidth / 2;
            } else {
                buyWidth = (totalBuy / (totalBuy + totalSell)) * viewWidth;
            }

            ViewGroup.LayoutParams buyParams = duoTextView.getLayoutParams();
            buyParams.width = (int)buyWidth;

            GradientDrawable buyShape = (GradientDrawable) duoTextView.getBackground();
            if(buyWidth == viewWidth) {
                // 涨停
                buyShape.setCornerRadii(new float[] {viewCorner, viewCorner, viewCorner, viewCorner, viewCorner, viewCorner, viewCorner, viewCorner});
            } else {
                buyShape.setCornerRadii(new float[] {viewCorner, viewCorner, 0, 0, 0, 0, viewCorner, viewCorner});
            }
            duoTextView.setLayoutParams(buyParams);

            ViewGroup.LayoutParams sellParams = kongTextView.getLayoutParams();
            sellParams.width = (int)(viewWidth - buyWidth);

            GradientDrawable sellShape = (GradientDrawable) kongTextView.getBackground();
            if(buyWidth == 0) {
                // 跌停
                sellShape.setCornerRadii(new float[] {viewCorner, viewCorner, viewCorner, viewCorner, viewCorner, viewCorner, viewCorner, viewCorner});
            } else {
                sellShape.setCornerRadii(new float[] {0, 0, viewCorner, viewCorner, viewCorner, viewCorner, 0, 0});
            }
            kongTextView.setLayoutParams(sellParams);
        }
    }

}
