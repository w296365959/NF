package com.sscf.investment.component.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonBaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> implements View.OnClickListener {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mData;
    protected final int mItemLayoutId;
    protected boolean mItemClickable;

    public CommonBaseRecyclerViewAdapter(Context context, List<T> data, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = mInflater.inflate(mItemLayoutId, parent, false);
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract void convert(CommonRecyclerViewHolder holder, T item, int position);

    public void setItemClickable(final boolean clickable) {
        mItemClickable = clickable;
    }

    @Override
    public final void onClick(View view) {
        final CommonRecyclerViewHolder holder = (CommonRecyclerViewHolder) view.getTag();
        onItemClick(view, holder, holder.getLayoutPosition());
    }

    protected void onItemClick(final View v, CommonRecyclerViewHolder holder, final int position) {
    }

    protected void onItemChildClick(final View v, CommonRecyclerViewHolder holder, final int position) {
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public List<T> getData() {
        return mData;
    }
}
