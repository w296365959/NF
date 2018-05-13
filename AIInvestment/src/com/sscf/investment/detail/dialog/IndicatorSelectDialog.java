package com.sscf.investment.detail.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.afinal.core.Arrays;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.setting.manager.KLineSettingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidwei on 2017/04/25
 * 选择指标的对话框
 */
public final class IndicatorSelectDialog extends Dialog implements View.OnClickListener {
    private RecyclerView mRecyclerView;

    public IndicatorSelectDialog(@NonNull Context context) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.dialog_indicator_select);
        final ConstraintLayout content = (ConstraintLayout) findViewById(R.id.content);
        content.findViewById(R.id.close_button).setOnClickListener(this);
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final RectF frame = new RectF(
                            content.getX(),
                            content.getY(),
                            content.getX() + content.getWidth(),
                            content.getY() + content.getHeight());
                    if (!frame.contains(event.getX(), event.getY())) {
                        cancel();
                    }
                }
                return false;
            }
        });

        final TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.indicator);
        title.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    public void init(int type, int selectedIndicator, SelectIndicatorCallback callback) {
        final Context context = getContext();
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        ArrayList<String> data = new ArrayList<String>();
        ArrayList<Integer> dataValues = new ArrayList<Integer>();
        switch (type) {
            case 0://分时图，含分时和五日
                data.add("成交量");
                dataValues.add(SettingConst.TIME_LINE_INDICATOR_VOLUME);
                data.add("资金博弈");
                dataValues.add(SettingConst.TIME_LINE_INDICATOR_CAPITAL_DDZ);
                data.add("MACD");
                dataValues.add(SettingConst.TIME_LINE_INDICATOR_MACD);
                data.add("量比");
                dataValues.add(SettingConst.TIME_LINE_INDICATOR_VOLUME_RATIO);
                break;
            case 1://K线图
                initSelectedCandicator(data, dataValues);
                break;
        }

        IndicatorAdapter adapter = new IndicatorAdapter(context, data, dataValues, selectedIndicator,
                R.layout.dialog_indicator_select_item, this, callback);
        adapter.setItemClickable(true);
        mRecyclerView.setAdapter(adapter);
    }

    private void initSelectedCandicator(ArrayList<String> data, ArrayList<Integer> dataValues){
        List<String> showCandicator = Arrays.asList(DengtaApplication.getApplication().getKLineSettingManager().getKlineSettingConfigure().showIndicators.split(","));
        for(int i = 0 ; i < showCandicator.size() ; i++){
            data.add(showCandicator.get(i));
            dataValues.add(KLineSettingManager.getDisplayIndex(showCandicator.get(i)));
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface SelectIndicatorCallback {
        void onSelectIndicatorCallback(int selectIndicator);
    }
}

final class IndicatorAdapter extends CommonBaseRecyclerViewAdapter<String> {
    final Dialog mDialog;
    final ArrayList<Integer> mDataValues;
    int mSelectedIndicator;
    final IndicatorSelectDialog.SelectIndicatorCallback mCallback;
    final float mRadius;
    final int selectedBgColor;
    final int unselectBgColor;
    final int selectedTextColor;
    final int unselectTextColor;

    public IndicatorAdapter(Context context, List<String> data, ArrayList<Integer> dataValues,
                            int selectedIndicator, int itemLayoutId, Dialog dialog, IndicatorSelectDialog.SelectIndicatorCallback callback) {
        super(context, data, itemLayoutId);
        mDialog = dialog;
        mDataValues = dataValues;
        mSelectedIndicator = selectedIndicator;
        mCallback = callback;
        final Resources res = context.getResources();
        mRadius = res.getDimension(R.dimen.tag_round_rect_corner_radius);
        selectedBgColor = ContextCompat.getColor(context, R.color.actionbar_bg_color);
        unselectBgColor = ContextCompat.getColor(context, R.color.default_background);
        selectedTextColor = ContextCompat.getColor(context, R.color.white_100);
        unselectTextColor = ContextCompat.getColor(context, R.color.default_text_color_100);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, String item, int position) {
        final TextView textView = holder.getView(R.id.indicator);
        final boolean isSelected = mDataValues.get(position) == mSelectedIndicator;
        if (isSelected) {
            textView.setTextColor(selectedTextColor);
            textView.setBackgroundDrawable(BitmapUtils.getRoundDrawable(selectedBgColor, mRadius));
        } else {
            textView.setTextColor(unselectTextColor);
            textView.setBackgroundDrawable(BitmapUtils.getRoundDrawable(unselectBgColor, mRadius));
        }
        textView.setText(item);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        mSelectedIndicator = mDataValues.get(position);
        notifyItemChanged(position);
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mCallback.onSelectIndicatorCallback(mSelectedIndicator);
    }
}
