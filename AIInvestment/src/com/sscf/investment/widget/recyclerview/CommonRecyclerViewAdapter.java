package com.sscf.investment.widget.recyclerview;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sscf.investment.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;

/**
 * Created by liqf on 2016/9/5.
 */
public abstract class CommonRecyclerViewAdapter extends RecyclerView.Adapter<CommonRecyclerViewAdapter.CommonViewHolder> {
    private static final String TAG = "CommonRecyclerViewAdapter";

    public static final int TYPE_NORMAL = 900;  //说明是不带有header和footer的
    public static final int TYPE_HEADER = 901;  //说明是带有Header的
    public static final int TYPE_FOOTER = 902;  //说明是带有Footer的

    protected final Context mContext;
    protected final LayoutInflater mInflater;

    protected List mListData = new ArrayList();

    protected View mHeaderView;
    protected View mFooterView;

    private RecyclerView mRecyclerView;
    private OnFloatingViewStateChangedListener mFloatingListener;

    public CommonRecyclerViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setFloatingViewCallback(final RecyclerView recyclerView, final int floatingViewType, final int floatingViewHeight, OnFloatingViewStateChangedListener listener) {
        mRecyclerView = recyclerView;
        mFloatingListener = listener;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                if (getItemCount() == 0) {
                if (getNormalItemCount() == 0) {
                    if (mFloatingListener != null) {
                        mFloatingListener.onFloatingViewStateChanged(0, null);
                    }
                    return;
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0 && mHeaderView != null) {
                    return;
                }

                int realPosition = getRealPosition(firstVisibleItemPosition);
                int firstViewType = getNormalViewType(realPosition);
                int translationY = 0;
                Object itemData = null;

                if (firstViewType != floatingViewType) {
                    int secondViewType = getNormalViewType(realPosition + 1);

                    int i = realPosition;
                    while (getNormalViewType(i) != floatingViewType) {
                        i--; //向上寻找到应该悬浮显示的item位置
                    }

                    if (secondViewType != floatingViewType) {
                        translationY = 0;
                    } else {
                        View secondChild = mRecyclerView.getChildAt(1);
                        int secondTop = secondChild.getTop();
//                        DtLog.d(TAG, "onScrolled: secondTop = " + secondTop + ", floatingHeight = " + floatingViewHeight);
                        translationY = floatingViewHeight < secondTop ? 0 : -floatingViewHeight + secondTop;
                    }

                    itemData = getItemData(i);
                } else {
                    translationY = 0;
                    itemData = getItemData(realPosition);
                }

                if (mFloatingListener != null) {
                    mFloatingListener.onFloatingViewStateChanged(translationY, itemData);
                }
            }
        });
    }

    public interface OnFloatingViewStateChangedListener {
        void onFloatingViewStateChanged(int translationY, Object itemData);
    }

    public void setListData(List listData) {
        mListData.clear();
        if (listData != null) {
            mListData.addAll(listData);
        }
    }

    public List getListData() {
        return mListData;
    }

    public Object getItemData(final int position) {
        final int size = mListData == null ? 0 : mListData.size();
        return position < size && position >= 0 ? mListData.get(position) : null;
    }

    public boolean hasHeader() {
        return getHeaderView() != null;
    }

    public final View getHeaderView() {
        return mHeaderView;
    }

    public final void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public final void removeHeaderView() {
        if (mHeaderView == null) {
            return;
        }
        mHeaderView = null;
        notifyItemRemoved(0);
    }

    public boolean hasFooter() {
        return getFooterView() != null;
    }

    public final View getFooterView() {
        return mFooterView;
    }

    public final void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public final void removeFooterView() {
        if (mFooterView == null) {
            return;
        }
        mFooterView = null;
        notifyItemRemoved(getItemCount() - 1);
    }

    @Override
    public final CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new CommonViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new CommonViewHolder(mFooterView);
        }

        return createNormalViewHolder(parent, viewType);
    }

    protected abstract CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(CommonViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER) {
//            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//            if (layoutParams != null) {
//                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                holder.itemView.setLayoutParams(layoutParams);
//            }
            return;
        }

        final int realPosition = getRealPosition(position);
        holder.mRealPosition = realPosition;
        bindNormalViewHolder(holder, mListData.get(realPosition), viewType);

        holder.itemView.setTag(R.id.recycler_list_item_position, realPosition);
        holder.itemView.setTag(holder);
    }

    protected int getRealPosition(int position) {
        return mHeaderView != null ? position - 1 : position;
    }

    private void bindNormalViewHolder(CommonViewHolder holder, Object itemData, int viewType) {
        holder.bindData(itemData);
    }

    @Override
    public final int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return getNormalItemCount();
        } else if (mHeaderView == null || mFooterView == null) {
            return getNormalItemCount() + 1;
        } else {
            return getNormalItemCount() + 2;
        }
    }


    public final int getNormalItemCount() {
        return mListData.size();
    }
    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }

        final int realPosition = getRealPosition(position);
        return getNormalViewType(realPosition);
    }

    protected int getNormalViewType(int position) {
        return TYPE_NORMAL;
    }

    public void notifyNormalItemRemoved(int position) {
        if (mHeaderView != null) {
            notifyItemRemoved(position + 1);
        } else {
            notifyItemRemoved(position);
        }
    }

    public void notifyNormalItemChanged(int position) {
        if (mHeaderView != null) {
            notifyItemChanged(position + 1);
        } else {
            notifyItemChanged(position);
        }
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        public int mRealPosition;
        public Object mItemData;
        public Object mTag;

        public CommonViewHolder(View itemView) {
            super(itemView);

            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView || itemView == mFooterView){
                return;
            }

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked();
                }
            });
        }

        @CallSuper
        public void bindData(Object itemData) {
            mItemData = itemData;
        }

        public void onItemClicked() {

        }
    }
}
