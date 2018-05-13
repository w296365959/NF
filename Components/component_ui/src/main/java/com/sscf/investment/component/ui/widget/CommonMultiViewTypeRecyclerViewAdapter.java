package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public abstract class CommonMultiViewTypeRecyclerViewAdapter<T> extends CommonBaseRecyclerViewAdapter<T> implements View.OnClickListener {

    public CommonMultiViewTypeRecyclerViewAdapter(Context context, List<T> data) {
        super(context, data, 0);
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int itemLayoutId = getItemLayoutId(viewType);
        final View convertView = mInflater.inflate(itemLayoutId, parent, false);
        final CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(convertView, this);
        convertView.setTag(holder);
        if (mItemClickable) {
            convertView.setOnClickListener(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        convert(holder, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {
        final List<T> data = mData;
        final int size = data == null ? 0 : data.size();
        return position >= 0 && position < size ? data.get(position) : null;
    }

    protected abstract int getItemLayoutId(int viewType);
}
