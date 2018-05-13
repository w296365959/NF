package com.sscf.investment.detail.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;

import BEC.SecQuote;

/**
 * Created by yorkeehuang on 2017/5/10.
 */

public abstract class BottomDoubleButtonDialog extends BottomDialog {

    public static final int PRE_BUTTON = 1;
    public static final int NEXT_BUTTON = 2;

    private OnDialogButtonClickListener mOnDialogButtonClickListener;

    protected TextView mTodayCloseView;

    protected TextView mTodayOpenView;
    protected TextView mYestodayCloseView;
    protected TextView mHighestView;
    protected TextView mLowestView;

    protected TextView mChangeRateView;
    protected TextView mChangeValueView;
    protected TextView mAmountView;
    protected TextView mTurnoverView;

    protected Button mPreButton;
    protected Button mNextButton;

    public BottomDoubleButtonDialog(@NonNull Context context, int type) {
        super(context, type);
        initResources();
        initView();
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_history_time_line;
    }

    private void initView() {
        mPreButton = (Button) findViewById(R.id.pre_button);
        mPreButton.setOnClickListener(this);

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(this);

        mTodayCloseView = (TextView) findViewById(R.id.today_close_value);

        mTodayOpenView = (TextView) findViewById(R.id.today_open_value);
        mYestodayCloseView = (TextView) findViewById(R.id.yestoday_close_value);
        mHighestView = (TextView) findViewById(R.id.highest_title_value);
        mLowestView = (TextView) findViewById(R.id.lowest_title_value);

        mChangeRateView = (TextView) findViewById(R.id.change_rate_value);
        mChangeValueView = (TextView) findViewById(R.id.change_value);
        mAmountView = (TextView) findViewById(R.id.amount_value);
        mTurnoverView = (TextView) findViewById(R.id.turnover_value);
    }

    private void initResources() {
        Resources resources = getContext().getResources();
        mRedColor = resources.getColor(R.color.stock_red_color);
        mGreenColor = resources.getColor(R.color.stock_green_color);
        mGrayColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
    }

    public boolean updateQuote(SecQuote secQuote) {
        if(super.updateQuote(secQuote)) {
            initDetailInfo();
            updateValue();
            return true;
        }
        return false;
    }

    protected void updateValue() {
        SecDetailInfo info = getDetailInfo();
        if(info == null) {
            return;
        }

        int color;
        float deltaByYesterday;
        boolean isSuspended = info.isSuspended();

        //今收
        String close;
        if(isSuspended) {
            close = "停牌";
            color = mGrayColor;
        } else {
            close = StringUtil.getFormattedFloat(info.getClose(), getTpFlag());
            deltaByYesterday = info.getClose() - info.getYesterdayClose();
            if (deltaByYesterday > 0) {
                color = mRedColor;
            } else if (deltaByYesterday == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
        }
        mTodayCloseView.setTextColor(color);
        mTodayCloseView.setText(close);


        //今开
        String open;
        if(isSuspended) {
            open = "--";
            color = mGrayColor;
        } else {
            open = StringUtil.getFormattedFloat(info.getOpen(), getTpFlag());
            deltaByYesterday = info.getOpen() - info.getYesterdayClose();
            if (deltaByYesterday > 0) {
                color = mRedColor;
            } else if (deltaByYesterday == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
        }
        mTodayOpenView.setTextColor(color);
        mTodayOpenView.setText(open);

        //昨收
        String yestodayClose;
        if(isSuspended) {
            yestodayClose = "--";
        } else {
            yestodayClose = StringUtil.getFormattedFloat(info.getYesterdayClose(), getTpFlag());
        }
        mYestodayCloseView.setText(yestodayClose);

        //最高
        String high;
        if(isSuspended) {
            high = "--";
            color = mGrayColor;
        } else {
            high = StringUtil.getFormattedFloat(info.getHigh(), getTpFlag());
            deltaByYesterday = info.getHigh() - info.getYesterdayClose();
            if (deltaByYesterday > 0) {
                color = mRedColor;
            } else if (deltaByYesterday == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
        }
        mHighestView.setTextColor(color);
        mHighestView.setText(high);

        //最低
        String low;
        if(isSuspended) {
            low = "--";
            color = mGrayColor;
        } else {
            low = StringUtil.getFormattedFloat(info.getLow(), getTpFlag());
            deltaByYesterday = info.getLow() - info.getYesterdayClose();
            if (deltaByYesterday > 0) {
                color = mRedColor;
            } else if (deltaByYesterday == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
        }
        mLowestView.setTextColor(color);
        mLowestView.setText(low);

        String deltaValue;
        if(isSuspended) {
            deltaValue = "--";
            color = mGrayColor;
        } else {
            float delta = info.getClose() - info.getYesterdayClose();
            deltaValue = StringUtil.getFormattedFloat(delta, getTpFlag());
            if (delta > 0) {
                deltaValue = "+" + deltaValue;
                color = mRedColor;
            } else if (delta == 0) {
                color = mGrayColor;
            } else {
                color = mGreenColor;
            }
        }
        mChangeValueView.setTextColor(color);
        mChangeValueView.setText(deltaValue);

        String amount;
        if(isSuspended) {
            amount = "--";
        } else {
            amount = StringUtil.getAmountString(info.getAmount());
        }
        mAmountView.setText(amount);
    }

    protected int getTpFlag() {
        if (mSecQuote != null) {
            return mSecQuote.getITpFlag();
        }
        return DengtaConst.DEFAULT_TP_FLAG;
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        mOnDialogButtonClickListener = onDialogButtonClickListener;
    }

    protected void updateButton(int currentIndex, int start, int end) {
        mPreButton.setEnabled(currentIndex != start);
        mNextButton.setEnabled(currentIndex != end);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pre_button:
                if(mOnDialogButtonClickListener != null) {
                    mOnDialogButtonClickListener.onDialogButtonClick(this, v, PRE_BUTTON);
                }
                break;
            case R.id.next_button:
                if(mOnDialogButtonClickListener != null) {
                    mOnDialogButtonClickListener.onDialogButtonClick(this, v, NEXT_BUTTON);
                }
                break;
            default:
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public interface OnDialogButtonClickListener {
        void onDialogButtonClick(final BottomDoubleButtonDialog dialog, final View view, final int position);
    }
}
