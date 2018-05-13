package com.sscf.investment.teacherYan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.router.WebBeaconJump;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import BEC.E_NEWS_TYPE;
import BEC.NewsDesc;
import BEC.WxTeachInfo;
import BEC.WxTeachTitle;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/23.
 */

public class TeacherYanArticleAdapter extends CommonRecyclerViewAdapter {
    public TeacherYanArticleAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new ArticleHolder(mInflater.inflate(R.layout.item_teacher_yan_article, parent, false));
    }

    final class ArticleHolder extends CommonRecyclerViewAdapter.CommonViewHolder {


        @BindView(R.id.tvArticleTitle)TextView mTvArticleTitle;
        @BindView(R.id.tvArticleReadNum) TextView mTvArticleReadNum;
        @BindView(R.id.tvArticleLikeNum) TextView mTvArticleLikeNum;
        @BindView(R.id.ivArticleCover) ImageView mIvArticleCover;

        public ArticleHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            WxTeachTitle mItem = (WxTeachTitle) itemData;
            mTvArticleTitle.setText(mItem.getSTitle());
            mTvArticleLikeNum.setVisibility(TextUtils.isEmpty(mItem.getSReadCount()) ? View.GONE : View.VISIBLE);
            mTvArticleReadNum.setVisibility(TextUtils.isEmpty(mItem.getSLaudCount()) ? View.GONE : View.VISIBLE);
            mTvArticleReadNum.setText(StringUtil.getAmountString(Integer.valueOf(mItem.getSReadCount()), 1));
            mTvArticleLikeNum.setText(StringUtil.getAmountString(Integer.valueOf(mItem.getSLaudCount()), 1));
            displayImage(mItem.getSTitleImage());
        }

        private void displayImage(String url){
            if (!TextUtils.isEmpty(url)) {
                ImageLoaderUtils.getImageLoader().displayImage(url, mIvArticleCover, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        mIvArticleCover.setImageResource(R.drawable.teacher_yan_video_cover);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        mIvArticleCover.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        mIvArticleCover.setImageResource(R.drawable.teacher_yan_video_cover);
                    }
                });
            }else {
                mIvArticleCover.setImageResource(R.drawable.teacher_yan_video_cover);
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            WxTeachTitle mItem = (WxTeachTitle) mItemData;
            StatisticsUtil.reportAction(StatisticsConst.A_NEWS_YANDASHI_EDUCATION_JUMP);
            CountNumUtil.readTeacherYanArticle(mItem.getIID());

            WebBeaconJump.showTeacherYanArticleDetail(mContext, String.valueOf(mItem.getIID()), mItem.getSTitle());
        }
    }
}
