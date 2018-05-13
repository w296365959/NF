package com.sscf.investment.detail.interaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.utils.DengtaConst;

import BEC.DongmiQaDetail;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class DongmiQaListAdapter extends InteractionGroupAdapter<DongmiQaDetail> implements View.OnClickListener {

    public DongmiQaListAdapter(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return null;
        }

        DongmiQaViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dongmi_qa_item, null);
            viewHolder = new DongmiQaViewHolder(convertView);
            viewHolder.mPosition = position;
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(this);
        } else {
            viewHolder = (DongmiQaViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }

        viewHolder.update(getItem(position));
        return convertView;
    }

    private class DongmiQaViewHolder {
        private TextView mQuestionInfoView;
        private TextView mAnswerInfoView;
        private TextView mSourceView;
        private TextView mTimeView;
        public int mPosition;

        public DongmiQaViewHolder(View root) {
            mQuestionInfoView = (TextView) root.findViewById(R.id.question_info);
            mAnswerInfoView = (TextView) root.findViewById(R.id.answer_info);
            mSourceView = (TextView) root.findViewById(R.id.source);
            mTimeView = (TextView) root.findViewById(R.id.time);
        }

        public void update(DongmiQaDetail dongmiQaDetail) {
            mQuestionInfoView.setText(dongmiQaDetail.getSQuestion());
            mAnswerInfoView.setText(dongmiQaDetail.getSAnswer());
            mSourceView.setText("来源：" + dongmiQaDetail.getSFrom());
            mTimeView.setText(TimeUtils.timeStamp2Date(dongmiQaDetail.getIAnswerTime() * DengtaConst.MILLIS_FOR_SECOND));
        }
    }
}