package com.sscf.investment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sscf.investment.R;

public final class BottomEditLayout extends FrameLayout implements View.OnClickListener {
    private CheckedTextView mSelectAll;
    private TextView mDeleteButton;
    private OnEditOperationListener mListener;

    private String mRightText;

    public BottomEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BottomEditLayout);
        String rightText = array.getString(R.styleable.BottomEditLayout_right_text);
        if(TextUtils.isEmpty(rightText)) {
            mRightText = getResources().getString(R.string.delete);
        } else {
            mRightText = rightText;
        }
    }

    @Override
    protected void onFinishInflate() {
        mSelectAll = (CheckedTextView) findViewById(R.id.editSelectAll);
        mSelectAll.setOnClickListener(this);
        mDeleteButton = (TextView) findViewById(R.id.editDeleteButton);
        mDeleteButton.setOnClickListener(this);
    }

    public void setOnEditOperationListener(OnEditOperationListener l) {
        this.mListener = l;
    }

    public void setEditState(final int totalCount, final int selectedCount) {
        mSelectAll.setChecked(totalCount > 0 && totalCount == selectedCount);
        final boolean enabled = selectedCount > 0;
        mDeleteButton.setEnabled(enabled);
        String text = null;
        if (enabled) {
            text = mRightText + "(" + selectedCount + ")";
        } else {
            text = mRightText;
        }
        mDeleteButton.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.editSelectAll:
                final boolean checked = !mSelectAll.isChecked();
                mSelectAll.setChecked(checked);
                if (checked) {
                    mListener.onSelectAll();
                } else {
                    mListener.onSelectNone();
                }
                break;
            case R.id.editDeleteButton:
                mListener.onDelete();
                break;
            default:
                break;
        }
    }

    public interface OnEditOperationListener {
        void onSelectAll();
        void onSelectNone();
        void onDelete();
    }
}
