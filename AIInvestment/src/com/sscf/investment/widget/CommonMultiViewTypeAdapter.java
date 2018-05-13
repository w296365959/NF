package com.sscf.investment.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonMultiViewTypeAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mData;
    protected final int[] mItemLayoutIds;

    public CommonMultiViewTypeAdapter(Context context, List<T> data, int[] itemLayoutIds) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mItemLayoutIds = itemLayoutIds;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        final List<T> data = mData;
        final int size = data == null ? 0 : data.size();
        return position >= 0 && position < size ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int viewType = getItemViewType(position);
        final int layoutId =  mItemLayoutIds[viewType];

        final CommonViewHolder viewHolder = CommonViewHolder.get(mContext, convertView, parent, layoutId, position);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(CommonViewHolder holder, T item, int position);

    public void setData(List<T> data) {
        this.mData = data;
    }

    public List<T> getData() {
        return mData;
    }
}
