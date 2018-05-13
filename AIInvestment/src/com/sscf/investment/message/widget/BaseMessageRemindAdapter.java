package com.sscf.investment.message.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;

public abstract class BaseMessageRemindAdapter extends CommonRecyclerViewAdapter {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_DATE = 1;

    public BaseMessageRemindAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getNormalViewType(int position) {
        final List listData = mListData;
        final int size = listData == null ? 0 : listData.size();
        if (size > position && position >= 0) {
            final Object item = listData.get(position);
            if (item != null && item instanceof String) {
                return TYPE_DATE;
            }
        }
        return TYPE_MESSAGE;
    }

    final class DateHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
        @BindView(R.id.date)
        TextView date;

        public DateHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            if (itemData instanceof String) {
                date.setText(itemData.toString());
            }
        }
    }
}
