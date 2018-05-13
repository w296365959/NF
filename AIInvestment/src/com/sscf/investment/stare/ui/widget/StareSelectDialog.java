package com.sscf.investment.stare.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dengtacj.component.entity.stare.StareGroupInfo;
import com.dengtacj.component.entity.stare.StareItemInfo;
import com.dengtacj.component.entity.stare.StareSubGroupInfo;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yorkeehuang on 2017/9/12.
 */

public class StareSelectDialog extends Dialog implements View.OnClickListener, DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private static final String TAG = StareSelectDialog.class.getSimpleName();

    public static final int LIST_LAYOUT_TYPE_FLOW = 0;
    public static final int LIST_LAYOUT_TYPE_VERTICAL = 1;

    private SparseArray<GroupUI> mUIArray = new SparseArray<>(2);

    private StareGroupInfo mGroupInfo;

    private OnConfirmListener mOnConfirmListener;

    private final ViewGroup mContentView;

    private int mListLayoutType;

    public StareSelectDialog(@NonNull final Context context, int listLayoutType, Set<Integer> allSelectedItemData, StareGroupInfo groupInfo) {
        super(context, com.sscf.investment.component.ui.R.style.dialog_center_theme);
        setContentView(R.layout.dialog_stare_select);
        mContentView = (ViewGroup) findViewById(R.id.content);
        if(groupInfo != null) {
            mGroupInfo = groupInfo;
            getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        final RectF frame = new RectF(
                                mContentView.getX(),
                                mContentView.getY(),
                                mContentView.getX() + mContentView.getWidth(),
                                mContentView.getY() + mContentView.getHeight());
                        if (!frame.contains(event.getX(), event.getY())) {
                            cancel();
                        }
                    }
                    return false;
                }
            });
            mListLayoutType = listLayoutType;
            initSubGroup(allSelectedItemData, groupInfo.subGroupInfos);
            findViewById(R.id.ok_button).setOnClickListener(this);
            setOnShowListener(this);
            setOnDismissListener(this);
        }
    }

    @Override
    public void show() {
        if(mGroupInfo != null) {
            super.show();
        }
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mOnConfirmListener = listener;
    }

    private GroupUI initRecyclerView(Context context, View groupView, int type, int index) {
        RecyclerView recyclerView = (RecyclerView) groupView.findViewById(R.id.group_list);
        RecyclerView.LayoutManager layoutManager = null;
        switch (type) {
            case LIST_LAYOUT_TYPE_FLOW:
                layoutManager = new GridItemLayoutManager(context, recyclerView) ;
                break;
            case LIST_LAYOUT_TYPE_VERTICAL:
                layoutManager = new LinearLayoutManager(context);
                break;
        }
        if(layoutManager != null) {
            layoutManager.setAutoMeasureEnabled(true);
            int top = DeviceUtil.dip2px(getContext(), 6);
            int bottom = DeviceUtil.dip2px(getContext(), 6);
            recyclerView.addItemDecoration(new SpaceItemDecoration(top, 0, 0, bottom));
            recyclerView.setLayoutManager(layoutManager);
            GroupListAdapter adapter = new GroupListAdapter(context);
            TextView groupTitle = (TextView) groupView.findViewById(R.id.group_title);
            GroupUI ui = new GroupUI(groupTitle, adapter);
            mUIArray.put(index, ui);
            recyclerView.setAdapter(adapter);
            return ui;
        }
        return null;
    }

    private void initSubGroup(Set<Integer> allSelectedItemData, List<StareSubGroupInfo> subGroupInfos) {
        if(subGroupInfos != null) {
            for(int i=0, size=subGroupInfos.size(); i<size; i++) {
                StareSubGroupInfo subGroupInfo = subGroupInfos.get(i);
                addSubGroupInfo(i, subGroupInfo.name, allSelectedItemData, subGroupInfo.itemInfoList);
            }
        }
    }

    private void addSubGroupInfo(int groupIdex, String groupTitle, Set<Integer> allSelectedItemData, List<StareItemInfo> itemDatas) {
        GroupUI ui = mUIArray.get(groupIdex);
        if(ui == null) {
            View groupView = View.inflate(getContext(), R.layout.dialog_stare_select_group, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginLeftRight = DeviceUtil.dip2px(getContext(), 12);
            lp.setMargins(marginLeftRight, 0, marginLeftRight, 0);
            mContentView.addView(groupView, lp);
            ui = initRecyclerView(getContext(), groupView, mListLayoutType, groupIdex);
        }

        Set<Integer> selectedItems = null;
        if(!allSelectedItemData.isEmpty()) {
            for(StareItemInfo itemInfo : itemDatas) {
                if(allSelectedItemData.contains(itemInfo.value)) {
                    if(selectedItems == null) {
                        selectedItems = new HashSet<>();
                    }
                    selectedItems.add(itemInfo.value);
                }
            }
        }

        ui.groupTitle.setText(groupTitle);
        ui.adapter.setItemDatas(selectedItems, itemDatas);
        ui.adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                if(mOnConfirmListener != null) {
                    Set<Integer> selectedItems = new HashSet<>();
                    for(int i=0, size=mUIArray.size(); i<size; i++) {
                        GroupUI groupUI = mUIArray.valueAt(i);
                        selectedItems.addAll(groupUI.adapter.getSelectedItems());
                    }
                    mOnConfirmListener.onConfirm(mGroupInfo, selectedItems);
                }
                dismiss();
                break;
            default:
        }
    }


    @Override
    public void onShow(DialogInterface dialog) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);
        if(scrollView != null) {
            scrollView.scrollTo(0, 0);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mOnConfirmListener = null;
    }


    private class GroupUI {
        TextView groupTitle;
        GroupListAdapter adapter;

        GroupUI(TextView groupTitle, GroupListAdapter adapter) {
            this.groupTitle = groupTitle;
            this.adapter = adapter;
        }
    }

    private class GridItemLayoutManager extends GridLayoutManager {

        private RecyclerView mRecyclerView;

        public GridItemLayoutManager(Context context, @NonNull RecyclerView recyclerView) {
            super(context, 3);
            mRecyclerView = recyclerView;
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            int measuredWidth = mRecyclerView.getMeasuredWidth();
            int measuredHeight = mRecyclerView.getMeasuredHeight();
            DtLog.d(TAG, "measuredWidth = " + measuredWidth + ", measuredHeight = " + measuredHeight);
            int myMeasureHeight = 0;
            int count = state.getItemCount();
            for (int i = 0; i < count; i++) {
                View view = recycler.getViewForPosition(i);
                if (view != null) {
                    if (myMeasureHeight < measuredHeight && i % 3 == 0) {
                        RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                                getPaddingLeft() + getPaddingRight(), p.width);
                        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                                getPaddingTop() + getPaddingBottom(), p.height);
                        view.measure(childWidthSpec, childHeightSpec);
                        myMeasureHeight += view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                    }
                    recycler.recycleView(view);
                }
            }

            setMeasuredDimension(measuredWidth, Math.min(measuredHeight, myMeasureHeight));
        }
    }

    private class GroupListAdapter extends CommonRecyclerViewAdapter {

        private Set<Integer> mSelectedItems = new HashSet<>();

        public GroupListAdapter(Context context) {
            super(context);
        }

        public void setItemDatas(Set<Integer> selectedItems, List<StareItemInfo> itemList) {
            mSelectedItems.clear();
            if(selectedItems != null) {
                mSelectedItems.addAll(selectedItems);
            }
            setListData(itemList);
        }

        public Set<Integer> getSelectedItems() {
            return mSelectedItems;
        }

        @Override
        protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
            return new GroupListItemHolder(mInflater.inflate(R.layout.dialog_stare_select_group_list_item, parent, false));
        }

        class GroupListItemHolder extends CommonRecyclerViewAdapter.CommonViewHolder {

             private CheckBox checkBox;
             private TextView title;
             private StareItemInfo itemInfo;

             public GroupListItemHolder(View itemView) {
                 super(itemView);
                 checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
                 title = (TextView) itemView.findViewById(R.id.title);
             }

             @Override
             public void bindData(Object itemData) {
                 super.bindData(itemData);
                 itemInfo = (StareItemInfo) itemData;
                 title.setText(itemInfo.name);
                 checkBox.setChecked(mSelectedItems.contains(itemInfo.value));
             }

            @Override
            public void onItemClicked() {
                super.onItemClicked();
                if(itemInfo.value >= 0) {
                    if(checkBox.isChecked()) {
                        mSelectedItems.remove(itemInfo.value);
                    } else {
                        mSelectedItems.clear();
                        mSelectedItems.add(itemInfo.value);
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

    public interface OnConfirmListener {
        void onConfirm(StareGroupInfo groupInfo, Set<Integer> selectedItems);
    }
}
