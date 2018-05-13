package com.sscf.investment.portfolio;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.R;
import com.sscf.investment.portfolio.presenter.PortfolioPresenter;
import com.sscf.investment.sdk.utils.BaseStockUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import com.sscf.investment.widget.MultiTagView;
import java.util.ArrayList;

/**
 * Created by davidwei on 2017/10/13
 *
 */
public final class PortfolioStockListAdapter extends CommonAdapter<StockDbEntity> {

    private final int mUpColor;
    private final int mDownColor;
    private final int mBaseColor;

    private int mChangeState;

    private final View.OnClickListener mChangeListener;

    /**
     * 持仓
     */
    private ArrayList<String> mPositionString;

    public PortfolioStockListAdapter(Context context, View.OnClickListener l) {
        super(context, null, R.layout.portfolio_stock_listitem);

        mChangeListener = l;
        final TypedArray a = context.obtainStyledAttributes(new int[] {
                R.attr.stock_up_color, R.attr.stock_down_color, R.attr.stock_base_color
        });

        final Resources resources = context.getResources();
        mUpColor = a.getColor(0, resources.getColor(R.color.stock_red_color));
        mDownColor = a.getColor(1, resources.getColor(R.color.stock_green_color));
        mBaseColor = a.getColor(2, resources.getColor(R.color.stock_stop_color));

        a.recycle();
    }

    public void setChangeState(final int changeState) {
        mChangeState = changeState;
    }

    @Override
    public void convert(CommonViewHolder holder, StockDbEntity item, int position) {
        holder.setText(R.id.portfolio_stock_listitem_name, item.getSzName());
        holder.setText(R.id.portfolio_stock_listitem_number, BaseStockUtil.getSecCode(item.getDtSecCode()));
        holder.setText(R.id.portfolio_stock_listitem_price, item.getDisplayNowString());
        updateUpdownPercentText(holder.getView(R.id.portfolio_stock_listitem_change), item);
        StockUtil.updateStockTagIcon(holder.getView(R.id.portfolio_tag_icon), item.getDtSecCode());

        final MultiTagView tagView = holder.getView(R.id.portfolio_position_tag);
        if (item.isPosition) {
            tagView.addTags(getPositionString(),
                    R.drawable.tag_round_rect_bg_position,
                    R.color.tab_indicatorColor);
        } else {
            tagView.clearAllTags();
        }
    }

    private void updateUpdownPercentText(TextView textView, StockDbEntity entity) {
        textView.setOnClickListener(mChangeListener);
        switch (mChangeState) {
            case PortfolioPresenter.CHANGE_STATE_PERCENT:
                // 显示涨跌幅的百分比
                textView.setText(entity.getDisplayUpDownPercent());
                break;
            case PortfolioPresenter.CHANGE_STATE_PRICE:
                // 显示涨跌额
                textView.setText(entity.getUpDownPrice());
                break;
            case PortfolioPresenter.CHANGE_STATE_MARKET_VALUE:
                // 显示总市值
                textView.setText(StringUtil.getAmountString(entity.getTotalmarketvalue()));
                break;
            default:
                break;
        }

        final GradientDrawable shapeDrawable = (GradientDrawable) textView.getBackground();

        // 设置颜色
        // 停牌、涨跌价为0、现价或收盘价为0 都显示灰色
        if ("--".equals(textView.getText())) {
            shapeDrawable.setColorFilter(mBaseColor, PorterDuff.Mode.SRC);
            textView.setBackgroundDrawable(shapeDrawable);
        } else {
            if (entity.getFNow() == entity.getFClose() || entity.getIStatus() == 1 ||
                    entity.getFNow() == 0 || entity.getFClose() == 0) {
                shapeDrawable.setColorFilter(mBaseColor, PorterDuff.Mode.SRC);
                textView.setBackgroundDrawable(shapeDrawable);
            } else if (entity.getFNow() < entity.getFClose()) {
                // 跌
                shapeDrawable.setColorFilter(mDownColor, PorterDuff.Mode.SRC);
                textView.setBackgroundDrawable(shapeDrawable);
            } else if (entity.getFNow() > entity.getFClose()) {
                // 涨
                shapeDrawable.setColorFilter(mUpColor, PorterDuff.Mode.SRC);
                textView.setBackgroundDrawable(shapeDrawable);
            } else {
                shapeDrawable.setColorFilter(mBaseColor, PorterDuff.Mode.SRC);
                textView.setBackgroundDrawable(shapeDrawable);
            }
        }
    }

    private ArrayList<String> getPositionString() {
        if (mPositionString == null) {
            mPositionString = new ArrayList<>(1);
            mPositionString.add(mContext.getString(R.string.detail_postion));
        }
        return mPositionString;
    }
}
