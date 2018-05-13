package com.sscf.investment.detail;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.social.FeedRequestManager;
import com.dengtacj.component.entity.ShareParams;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.socialize.ShareDialog;
import BEC.E_FEED_TYPE;
import BEC.FeedContent;
import BEC.FeedItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/9/19.
 */
public class FeedOperationDialog extends Dialog {
    private static final String TAG = "FeedOperationDialog";
    private static final long ANIMATION_DURATION = 200;

    public static final int TYPE_MY_FEED = 0;
    public static final int TYPE_OTHERS_FEED = 1;
    public static final int TYPE_MY_COMMENT = 2;
    private final int mType;

    @BindView(R.id.dialogContentView) LinearLayout mDialogContentView;
    @BindView(R.id.dialogFrame) FrameLayout mDialogFrame;

    @BindView(R.id.follow) TextView mFollow;
    @BindView(R.id.divider) View mDivider;
    @BindView(R.id.share) TextView mShare;

    private String mFeedId;
    private String mCommentId;

    private ShareDialog mShareDialog;
    private ShareParams mShareParams;
    private Context mContext;
    private String mDtSecCode;
    private String mSecName;
    private FeedItem mFeedItem;
    private boolean mFromStock;

    private FeedRequestManager mFeedRequestManager;

    @OnClick(R.id.follow)
    public void doFollow() {
        if (mType == TYPE_OTHERS_FEED) {
            //do follow
        } else if (mType == TYPE_MY_COMMENT) {
            //do delete comment
            mFeedRequestManager.deleteComment(mFeedId, mCommentId, mFeedRequestManager);
        } else if (mType == TYPE_MY_FEED) {
            //do delete feed
            mFeedRequestManager.deleteFeed(mFeedId, mDtSecCode, mFeedRequestManager, mFromStock);
        }
        doDismiss();
    }

    @OnClick(R.id.share)
    public void doShare() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog((Activity) mContext);
            final Resources resources = mContext.getResources();
            final WebUrlManager urlManager = DengtaApplication.getApplication().getUrlManager();
            FeedContent feedContent = mFeedItem.getStFeedContent();
            int feedType = feedContent.getEType();
            String title = feedType == E_FEED_TYPE.E_FT_STOCK_REVIEW ? resources.getString(R.string.share_comment_title, mSecName) : resources.getString(R.string.share_opinion_title, feedContent.sTitle);
            if (title.length() > ShareDialog.MAX_SHARE_TITLE_LENGTH) {
                title = title.substring(0, ShareDialog.MAX_SHARE_TITLE_LENGTH) + "...";
            }
            String msg = feedType == E_FEED_TYPE.E_FT_STOCK_REVIEW ? resources.getString(R.string.share_comment_msg, mSecName, feedContent.sContent) : resources.getString(R.string.share_opinion_msg);
            final String url = feedType == E_FEED_TYPE.E_FT_STOCK_REVIEW ? urlManager.getCommentDetailUrl(mFeedId) : urlManager.getOpinionDetailUrl(mFeedId);
            mShareParams = ShareParams.createShareParams(title, msg, url, urlManager.getShareIconUrl());
        }

        mShareDialog.showShareDialog(mShareParams, null, null);
        StatisticsUtil.reportAction(StatisticsConst.FEED_LIST_SHARE_CLICKED);

        doDismiss();
    }

    @OnClick(R.id.cancel)
    public void doCancel() {
        doDismiss();
    }

    private void doDismiss() {
        dismissWithAnimation(mDialogFrame, mDialogContentView);
    }

    public FeedOperationDialog(Context context, int type, String dtSecCode, String secName, FeedItem feedItem, boolean fromStock) {
        super(context, R.style.dialog_share_theme);
        DeviceUtil.enableDialogTranslucentStatus(getWindow(), getContext().getResources().getColor(R.color.black_40));
        mContext = context;
        mDtSecCode = dtSecCode;
        mSecName = secName;
        mFeedItem = feedItem;
        mFromStock = fromStock;

        setContentView(R.layout.dialog_feed_operation);
        ButterKnife.bind(this);

        mFeedRequestManager = DengtaApplication.getApplication().getFeedRequestManager();

        mType = type;
        if (type == TYPE_OTHERS_FEED) {
            mFollow.setVisibility(View.GONE);
            mDivider.setVisibility(View.GONE);
        } else if (type == TYPE_MY_COMMENT) {
            mFollow.setText(R.string.delete);
            mDivider.setVisibility(View.GONE);
            mShare.setVisibility(View.GONE);
        } else if (type == TYPE_MY_FEED) {
            mFollow.setText(R.string.delete);
        }

        getWindow().getDecorView().setOnTouchListener((v, event) -> {
            RectF frame = new RectF(
                mDialogContentView.getX(),
                mDialogContentView.getY(),
                mDialogContentView.getX() + mDialogContentView.getWidth(),
                mDialogContentView.getY() + mDialogContentView.getHeight());
            if (!frame.contains(event.getX(), event.getY())) {
                doDismiss();
            }
            return false;
        });

        setCanceledOnTouchOutside(true);

        setOnShowListener(dialog -> {
            ValueAnimator animatorExpand = ObjectAnimator.ofFloat(mDialogContentView, "translationY", mDialogContentView.getMeasuredHeight(), 0);
            animatorExpand.setDuration(ANIMATION_DURATION);
            animatorExpand.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
            animatorExpand.start();

            ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0, 255).setDuration(ANIMATION_DURATION);
            alphaAnimator.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
            alphaAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                mDialogFrame.getBackground().setAlpha((int) value);
            });
            alphaAnimator.start();
        });
    }

    private void dismissWithAnimation(final View contentFrame, View contentView) {
        ValueAnimator animatorCollapse = ObjectAnimator.ofFloat(contentView, "translationY", 0, contentView.getMeasuredHeight());
        animatorCollapse.setDuration(ANIMATION_DURATION);
        animatorCollapse.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        animatorCollapse.start();

        animatorCollapse.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(255, 0).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        alphaAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            contentFrame.getBackground().setAlpha((int) value);
        });
        alphaAnimator.start();
    }

    public void setCommentIds(String feedId, String commentId) {
        mFeedId = feedId;
        mCommentId = commentId;
    }

    public void setFeedId(String feedId) {
        mFeedId = feedId;
    }

}
