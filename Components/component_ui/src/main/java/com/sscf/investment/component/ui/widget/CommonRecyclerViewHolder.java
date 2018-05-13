package com.sscf.investment.component.ui.widget;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public final class CommonRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final SparseArray<View> mViews;
    private final CommonBaseRecyclerViewAdapter mAdapter;
    public Object tag;

    public CommonRecyclerViewHolder(final View itemView, final CommonBaseRecyclerViewAdapter adapter) {
        super(itemView);
        mAdapter = adapter;
        this.mViews = new SparseArray<View>();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public CommonRecyclerViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        getView(viewId).setBackgroundDrawable(drawable);
        return this;
    }

    public CommonRecyclerViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonRecyclerViewHolder setText(int viewId, int textId) {
        if (viewId != 0 && textId != 0) {
            final TextView view = getView(viewId);
            view.setText(textId);
        }
        return this;
    }

    public CommonRecyclerViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public CommonRecyclerViewHolder setDefaultClickListener(int viewId) {
        getView(viewId).setOnClickListener(this);
        return this;
    }

    @Override
    public void onClick(View view) {
        mAdapter.onItemChildClick(view, this, getLayoutPosition());
    }
}
