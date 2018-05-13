package com.sscf.investment.teacherYan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;
import com.tencent.smtt.sdk.TbsVideo;

import BEC.VideoInfo;
import BEC.WxWalkRecord;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherYanCourseAdapter extends CommonRecyclerViewAdapter{

    public TeacherYanCourseAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new RecordHolder(mInflater.inflate(R.layout.item_teacher_yan_course, parent, false));
    }

    final class RecordHolder extends CommonRecyclerViewAdapter.CommonViewHolder{

        @BindView(R.id.tvVideoTitle) TextView mTvTitle;
        @BindView(R.id.tvVideoListenerNum) TextView mTvListenerNum;
        @BindView(R.id.tvUploadTime) TextView mTvTime;
        @BindView(R.id.ivCurseCover) ImageView mIvCurseCover;

        public RecordHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            WxWalkRecord item = (WxWalkRecord) itemData;
            mTvTitle.setText(item.getSTitle());
            mTvListenerNum.setText(StringUtil.getAmountString(item.getINumber(), 1));
            mTvTime.setText(TimeUtils.transForDate(item.getSInformationTime()));
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_COURSE_JUMP);
            WxWalkRecord item = (WxWalkRecord) mItemData;
//            if (TbsVideo.canUseTbsPlayer(mContext))
            {
                CountNumUtil.readTeacherYanVideo(item.getIID());
                TbsVideo.openVideo(mContext, item.getSUrl());
            }
        }
    }
}
