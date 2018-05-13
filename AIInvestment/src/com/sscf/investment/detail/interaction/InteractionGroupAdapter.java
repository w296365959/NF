package com.sscf.investment.detail.interaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dengtacj.thoth.Message;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class InteractionGroupAdapter<T extends Message> extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;

    private List<T> mDatas;

    public InteractionGroupAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if(mDatas != null) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
