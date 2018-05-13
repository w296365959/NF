package com.sscf.investment.market.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.R;

/**
 * Created by liqf on 2015/8/21.
 */
public class StockSortTitleView extends RelativeLayout implements View.OnClickListener {
    private final String mTitleText;
    private TextView mTitle;
    private ImageView mIcon;

    private Drawable mRightCornerDrawable;
    private Drawable mArrowDownDrawable;
    private Drawable mArrowUpDrawable;

    public void setStateListener(OnSortStateChangeListener listener) {
        this.mListener = listener;
    }

    private OnSortStateChangeListener mListener;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_SORT_DESCEND = 1;
    public static final int STATE_SORT_ASCEND = 2;
    private int mState = STATE_NORMAL;

    public StockSortTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.stock_sort_title, this, true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StockSortTitleView);
        mTitleText = array.getString(R.styleable.StockSortTitleView_titleName);

        array.recycle();

        array = context.obtainStyledAttributes(new int[]{
                R.attr.right_corner,
                R.attr.arrow_down,
                R.attr.arrow_up
        });

        mRightCornerDrawable = array.getDrawable(0);
        mArrowDownDrawable = array.getDrawable(1);
        mArrowUpDrawable = array.getDrawable(2);

        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(mTitleText);
        mIcon = (ImageView) findViewById(R.id.icon);
        mIcon.setImageResource(R.drawable.right_corner);

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switchToNextState();

        refreshUiByState(mState);

        if (mListener != null) {
            mListener.onSortStateChanged(this, mState);
        }
    }

    public void setSortState(final int state) {
        mState = state;
        refreshUiByState(state);
    }

    public int getSortState() {
        return mState;
    }

    private void switchToNextState() {
        switch (mState) {
            case STATE_NORMAL:
                mState = STATE_SORT_DESCEND;
                break;
            case STATE_SORT_DESCEND:
                mState = STATE_SORT_ASCEND;
                break;
            case STATE_SORT_ASCEND:
                mState = STATE_SORT_DESCEND;
                break;
            default:
                break;
        }
    }

    private void refreshUiByState(final int state) {
        switch (state) {
            case STATE_NORMAL:
                mIcon.setImageDrawable(mRightCornerDrawable);
                break;
            case STATE_SORT_DESCEND:
                mIcon.setImageDrawable(mArrowDownDrawable);
                break;
            case STATE_SORT_ASCEND:
                mIcon.setImageDrawable(mArrowUpDrawable);
                break;
            default:
                break;
        }
    }

    public interface OnSortStateChangeListener {
        void onSortStateChanged(final View view, final int newState);
    }
}
