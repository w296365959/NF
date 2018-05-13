package com.sscf.investment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by yorkeehuang on 2017/5/24.
 */

public class CheckableListView extends ListView {

    private OnItemCheckedChangedListener mOnItemCheckedChangedListener;

    public CheckableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setItemCheckedChangedListener(OnItemCheckedChangedListener listener) {
        mOnItemCheckedChangedListener = listener;
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        final boolean result = super.performItemClick(view, position, id);
        if (mOnItemCheckedChangedListener != null) {
            mOnItemCheckedChangedListener.onItemCheckedChanged(position, isItemChecked(position));
        }
        return result;
    }

    @Override
    public void setItemChecked(int position, boolean value) {
        super.setItemChecked(position, value);
        if (mOnItemCheckedChangedListener != null) {
            mOnItemCheckedChangedListener.onItemCheckedChanged(position, value);
        }
    }

    @Override
    public void clearChoices() {
        super.clearChoices();
        if (mOnItemCheckedChangedListener != null) {
            mOnItemCheckedChangedListener.onItemCheckedChanged(-1, false);
        }
    }

    public interface OnItemCheckedChangedListener {
        void onItemCheckedChanged(int position, boolean value);
    }
}
