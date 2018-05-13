package com.sscf.investment.portfolio.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.widget.recyclerview.DividerItemDecoration;
import java.util.List;

/**
 * Created by davidwei on 2017-10-10.
 */
public final class CommonListDialog extends Dialog {
    private RecyclerView mRecyclerView;
    public CommonListDialog(@NonNull Context context) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.dialog_common_list);

        final RecyclerView contentView = (RecyclerView) findViewById(R.id.dialogContentView);
        getWindow().getDecorView().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final RectF frame = new RectF(
                        contentView.getX(),
                        contentView.getY(),
                        contentView.getX() + contentView.getWidth(),
                        contentView.getY() + contentView.getHeight());
                if (!frame.contains(event.getX(), event.getY())) {
                    cancel();
                }
            }
            return false;
        });
        mRecyclerView = contentView;
//
//        contentView.findViewById(R.id.portfolio_edit_del).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        findViewById(R.id.portfolio_edit_top).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//            }
//        });
    }

    public void showListDialog(List<String> texts, OnListItemClickListener l) {
        final Context context = getContext();
        final RecyclerView recyclerView = mRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new CommonListAdapter(context, texts, l));
        recyclerView.setItemAnimator(null);
    }

    public interface OnListItemClickListener {
        void onItemClick(int position);
    }
}

final class CommonListAdapter extends CommonBaseRecyclerViewAdapter<String> {

    private final CommonListDialog.OnListItemClickListener mListener;
    CommonListAdapter(Context context, List<String> texts, CommonListDialog.OnListItemClickListener l) {
        super(context, texts, R.layout.dialog_common_list_item);
        setItemClickable(true);
        mListener = l;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, String item, int position) {
        holder.setText(R.id.text, item);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        if (mListener != null) {
            mListener.onItemClick(position);
        }
    }
}