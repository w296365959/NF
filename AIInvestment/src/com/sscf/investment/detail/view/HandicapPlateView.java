package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.dialog.HandicapDialog;
import com.sscf.investment.detail.manager.PlateBgDrawableManager;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.dengtacj.component.router.CommonBeaconJump;

import java.util.ArrayList;
import java.util.List;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017/04/26.
 */
public final class HandicapPlateView extends ConstraintLayout implements View.OnClickListener{
    private View mIndustryItemView;
    private TextView mIndustryNameView;
    private TextView mIndustryValueView;
    private PlateAdapter mAdapter;

    private String mIndustryDtSecCode;
    private String mIndustryName;

    private final PlateBgDrawableManager mPlateBgDrawableManager;

    private HandicapDialog mDialog;

    public HandicapPlateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPlateBgDrawableManager = new PlateBgDrawableManager(
                context.getResources().getDimension(R.dimen.tag_round_rect_corner_radius));
    }

    public void setDialog(HandicapDialog dialog) {
        mDialog = dialog;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndustryItemView = findViewById(R.id.industryPlateView);
        mIndustryItemView.setOnClickListener(this);
        mIndustryNameView = (TextView) mIndustryItemView.findViewById(R.id.name);
        mIndustryValueView = (TextView) mIndustryItemView.findViewById(R.id.value);
    }

    public void updateIndustryPlateData(final SecSimpleQuote quote) {
        final float updown = StringUtil.getUpDownValue(quote);
        final int textColor = PlateBgDrawableManager.getTextColor(updown);
        mIndustryNameView.setText(quote.sSecName);
        mIndustryNameView.setTextColor(textColor);
        mIndustryValueView.setText(StringUtil.getUpdownString(updown));
        mIndustryValueView.setTextColor(textColor);
        final Drawable bg = mPlateBgDrawableManager.getDrawable(PlateBgDrawableManager.getBackgroundColor(updown));
        mIndustryItemView.setBackgroundDrawable(bg);

        mIndustryDtSecCode = quote.sDtSecCode;
        mIndustryName = quote.sSecName;
    }

    public void updateConceptPlateData(final ArrayList<SecSimpleQuote> quotes) {
        if (mAdapter == null) {
            final Context context = getContext();
            final RecyclerView conceptList = (RecyclerView) findViewById(R.id.recyclerview);
            conceptList.setLayoutManager(new GridLayoutManager(context, 2));
            mAdapter = new PlateAdapter(context, null, R.layout.dialog_handicap_plate_item, mPlateBgDrawableManager, mDialog);
            mAdapter.setItemClickable(true);
            conceptList.setAdapter(mAdapter);
        }
        mAdapter.setData(quotes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        CommonBeaconJump.showSecurityDetailActivity(getContext(), mIndustryDtSecCode, mIndustryName);
        mDialog.dismiss();
    }
}

final class PlateAdapter extends CommonBaseRecyclerViewAdapter<SecSimpleQuote> {
    private final PlateBgDrawableManager mPlateBgDrawableManager;
    private final HandicapDialog mDialog;
    public PlateAdapter(Context context, List<SecSimpleQuote> data, int itemLayoutId, PlateBgDrawableManager plateBgDrawableManager, HandicapDialog dialog) {
        super(context, data, itemLayoutId);
        mPlateBgDrawableManager = plateBgDrawableManager;
        mDialog = dialog;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, SecSimpleQuote item, int position) {
        final float updown = StringUtil.getUpDownValue(item);
        final int textColor = PlateBgDrawableManager.getTextColor(updown);
        final TextView nameView = holder.getView(R.id.name);
        nameView.setText(item.sSecName);
        nameView.setTextColor(textColor);
        final TextView valueView = holder.getView(R.id.value);
        valueView.setText(StringUtil.getUpdownString(updown));
        valueView.setTextColor(textColor);
        final Drawable bg = mPlateBgDrawableManager.getDrawable(PlateBgDrawableManager.getBackgroundColor(updown));
        holder.setBackgroundDrawable(R.id.item, bg);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final SecSimpleQuote plateInfo = getItem(position);
        CommonBeaconJump.showSecurityDetailActivity(mContext, plateInfo.sDtSecCode, plateInfo.sSecName);
        mDialog.dismiss();
    }
}
