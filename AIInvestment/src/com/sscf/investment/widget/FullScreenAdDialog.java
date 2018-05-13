package com.sscf.investment.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import BEC.DtActivityDetail;

/**
 * Created by yorkeehuang on 2017/3/21.
 */

public class FullScreenAdDialog extends Dialog implements View.OnClickListener {

    private boolean mCanCancelOnTouchOutside = false;
    private ViewGroup mContentView;
    private OnDialogButtonClickListener mButtonClickListener;
    private DtActivityDetail mAdDetail;
    private RoundCornerRectImageView mAdView;

    public FullScreenAdDialog(@NonNull Context context) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.dialog_full_screen);
        final View contentView = findViewById(R.id.content);
        mAdView = (RoundCornerRectImageView) contentView.findViewById(R.id.ad_view);
        mAdView.setOnClickListener(this);
        mAdView.setRadius(context.getResources().getDimensionPixelSize(R.dimen.ad_view_round_radius));
        contentView.findViewById(R.id.ad_off_button).setOnClickListener(this);
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final RectF frame = new RectF(
                            contentView.getX(),
                            contentView.getY(),
                            contentView.getX() + contentView.getWidth(),
                            contentView.getY() + contentView.getHeight());
                    if (mCanCancelOnTouchOutside && !frame.contains(event.getX(), event.getY())) {
                        cancel();
                    }
                }
                return false;
            }
        });

        mContentView = (ViewGroup) contentView;
    }

    public void setAdBmp(Bitmap bmp) {
        mAdView.setBitmap(DengtaApplication.getApplication().getResources(), bmp);
    }

    public ViewGroup getContentView() {
        return mContentView;
    }

    public void setCanCancelOnTouchOutside(final boolean can) {
        mCanCancelOnTouchOutside = can;
    }

    public void setButtonClickListener(OnDialogButtonClickListener listener) {
        mButtonClickListener = listener;
    }

    public void setDetail(DtActivityDetail detail) {
        if(detail != null) {
            mAdDetail = detail;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_off_button:
                if(mButtonClickListener != null) {
                    mButtonClickListener.onDialogButtonClick(this, v, 0);
                }
                dismiss();
                break;
            case R.id.ad_view:
                if(mButtonClickListener != null) {
                    mButtonClickListener.onDialogButtonClick(this, v, 1);
                }
                dismiss();
                if(mAdDetail != null && !TextUtils.isEmpty(mAdDetail.getSUrl())) {
                    final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                    if (scheme != null) {
                        scheme.handleUrl(getContext(), mAdDetail.getSUrl());
                    }
                }
                break;
            default:
        }
    }

    public interface OnDialogButtonClickListener {
        void onDialogButtonClick(final FullScreenAdDialog dialog, final View view, final int position);
    }

    @Override
    public void show() {
        super.show();
        StatisticsUtil.reportAction(StatisticsConst.FULL_SCREEN_AD_DISPLAY);
        DengtaApplication.getApplication().getAdManager().saveOnShow(mAdDetail);
    }
}
