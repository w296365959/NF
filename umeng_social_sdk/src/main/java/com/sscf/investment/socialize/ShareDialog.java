package com.sscf.investment.socialize;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import com.dengtacj.component.entity.ShareParams;
import com.dengtacj.component.entity.ShareType;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.component.ui.utils.ActivityUtils;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.sdk.R;
import java.io.File;
import java.util.Map;

/**
 * Created by davidwei on 2015-08-30.
 * 分享界面
 */
public final class ShareDialog extends Dialog implements View.OnClickListener, UMShareListener {
	private static final String TAG = ShareDialog.class.getSimpleName();

	public static final String ACTION_SHARE_SUCCESS = "action_share_success";

	private static final long ANIMATION_DURATION = 200;
	private final Activity mActivity;

	public static final int MAX_SHARE_TITLE_LENGTH = 100; //以防微博有发文的字数限制

	private ShareParams mParams;
	private Map<String, ShareParams> mParamsMaps;

	public static final String KEY_DEFAULT = "default";

	private String mStatisticsKeySuccess;
	private String mStatisticsKeyFailed;

	private IShareManager.ShareListener mShareListener;

	public ShareDialog(final Activity activity) {
		this(activity, ShareType.MOMENTS, ShareType.QQ, ShareType.SINA, ShareType.WECHAT);
	}

	public ShareDialog(final Activity activity, ShareType... shareTypes){
		super(activity, R.style.dialog_share_theme);
		DeviceUtil.enableDialogTranslucentStatus(getWindow(), getContext().getResources().getColor(R.color.black_40));
		this.mActivity = activity;

		setContentView(R.layout.dialog_share);

		final View contentFrame = findViewById(R.id.share_dialog_frame);
		final View contentView = findViewById(R.id.share_dialog_layout);

		getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				RectF frame = new RectF(
						contentView.getX(),
						contentView.getY(),
						contentView.getX() + contentView.getWidth(),
						contentView.getY() + contentView.getHeight());
				if (!frame.contains(event.getX(), event.getY())) {
					dismissWithAnimation(contentFrame, contentView);
				}
				return false;
			}
		});

		setCanceledOnTouchOutside(true);

		setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				ValueAnimator animatorExpand = ObjectAnimator.ofFloat(contentView, "translationY", contentView.getMeasuredHeight(), 0);
				animatorExpand.setDuration(ANIMATION_DURATION);
				animatorExpand.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
				animatorExpand.start();

				ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0, 255).setDuration(ANIMATION_DURATION);
				alphaAnimator.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
				alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float value = (float) animation.getAnimatedValue();
						contentFrame.getBackground().setAlpha((int) value);
					}
				});
				alphaAnimator.start();
			}
		});
		if (null == shareTypes){
			findViewById(R.id.shareByWechatFriends).setOnClickListener(this);
			findViewById(R.id.shareByWechatMoments).setOnClickListener(this);
			findViewById(R.id.shareBySinaWeibo).setOnClickListener(this);
			findViewById(R.id.shareByQq).setOnClickListener(this);
			findViewById(R.id.shareIntentByWechatMoments).setOnClickListener(this);
		}else{
			configureShareTypes(shareTypes);
		}
	}

	private void configureShareTypes(ShareType[] shareTypes){
		for (int i = 0 ; i < shareTypes.length ; i++){
			if (isContain(ShareType.WECHAT, shareTypes)){
				findViewById(R.id.shareByWechatFriends).setOnClickListener(this);
				findViewById(R.id.shareByQq).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.shareByWechatFriends).setVisibility(View.GONE);
			}

			if (isContain(ShareType.MOMENTS, shareTypes)){
				findViewById(R.id.shareByWechatMoments).setOnClickListener(this);
				findViewById(R.id.shareByQq).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.shareByWechatMoments).setVisibility(View.GONE);
			}

			if (isContain(ShareType.SINA, shareTypes)){
				findViewById(R.id.shareBySinaWeibo).setOnClickListener(this);
				findViewById(R.id.shareByQq).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.shareBySinaWeibo).setVisibility(View.GONE);
			}

			if (isContain(ShareType.QQ, shareTypes)){
				findViewById(R.id.shareByQq).setOnClickListener(this);
				findViewById(R.id.shareByQq).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.shareByQq).setVisibility(View.GONE);
			}

			if (isContain(ShareType.INTENT_MOMENTS, shareTypes)){
				findViewById(R.id.shareIntentByWechatMoments).setOnClickListener(this);
				findViewById(R.id.shareIntentByWechatMoments).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.shareIntentByWechatMoments).setVisibility(View.GONE);
			}
		}
	}

	private boolean isContain(ShareType type, ShareType[] shareTypes){
		for (int i = 0 ; i < shareTypes.length ; i++){
			if (shareTypes[i] == type){
				return true;
			}
		}
		return false;
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
				if(isShowing()) {
					dismiss();
				}
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
		alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				contentFrame.getBackground().setAlpha((int) value);
			}
		});
		alphaAnimator.start();
	}

	@Override
	public void dismiss() {
		if (ActivityUtils.isDestroy(mActivity)) {
			return;
		}
		if (isShowing()) {
			super.dismiss();
		}
	}

	public void setShareListener(final IShareManager.ShareListener l) {
		this.mShareListener = l;
	}

	private void setShareParams(final Map<String, ShareParams> paramsMaps) {
		mParams = null;
		mParamsMaps = paramsMaps;
	}

	private void setShareParams(final ShareParams params) {
		mParams = params;
		mParamsMaps = null;
	}

	public ShareParams getShareParams(final String plat) {
		ShareParams params = null;
		if (mParamsMaps != null) {
			params = mParamsMaps.get(plat);
			if (params == null) {
				params = mParamsMaps.get(KEY_DEFAULT);
			}
		}
		if (mParams != null) {
			params = mParams;
		}
		return params;
	}

	public void showShareDialog(ShareParams params, String statisticsKeySuccess, String statisticsKeyFailed) {
		if (!checkParams(params)) {
			// 没有地址就不去分享
			return;
		}

		mStatisticsKeySuccess = statisticsKeySuccess;
		mStatisticsKeyFailed = statisticsKeyFailed;
		setShareParams(params);

		show();
	}

	private boolean checkParams(ShareParams params) {
		return params != null && params.checkParams();
	}

	public void showShareDialog(ShareParams params) {
		showShareDialog(params, null, null);
	}

	public void showShareDialog(Map<String, ShareParams> paramsMaps) {
		setShareParams(paramsMaps);
		show();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.shareByWechatFriends) {
			shareByWechatFriends();
		} else if (id == R.id.shareByWechatMoments) {
			shareByWechatMoments();
		} else if (id == R.id.shareBySinaWeibo) {
			shareBySinaWeibo();
		} else if (id == R.id.shareByQq) {
			shareByQq();
		} else if(id == R.id.shareIntentByWechatMoments) {
			shareByIntent();
			mShareListener.onShareListener(IShareManager.STATE_SUCCESS, IShareManager.PLAT_INTENT_MOMENTS);
		}
		dismiss();
	}

	private void shareByWechatFriends() {
		final ShareParams params = getShareParams(IShareManager.PLAT_WECHAT);
		if (params == null) {
			return;
		}
//		params.setShareType(Platform.SHARE_WEBPAGE);
		share(mActivity, SHARE_MEDIA.WEIXIN, params, this);
	}

	private void shareByWechatMoments() {
		final ShareParams params = getShareParams(IShareManager.PLAT_WECHAT_MOMENTS);
		if (params == null) {
			return;
		}
//		params.setShareType(SHARE_WEBPAGE);
		share(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE, params, this);
	}

	private void shareBySinaWeibo() {
		// 避免多次使用微博分享，会重复拼接字符串，需要new一个新的params
		ShareParams params = getShareParams(IShareManager.PLAT_SINA_WEIBO);
		if (params == null) {
			return;
		}

		share(mActivity, SHARE_MEDIA.SINA, params, this);
	}

	private void shareByIntent() {
		WeiXinShareUtil.sharePhotoToWX(mActivity, getShareParams(IShareManager.PLAT_WECHAT_MOMENTS).title,
				getShareParams(IShareManager.PLAT_WECHAT_MOMENTS).shareFile.getAbsolutePath());
	}

	private void shareByQq() {
		final ShareParams params = getShareParams(IShareManager.PLAT_QQ);
		if (params == null) {
			return;
		}

		share(mActivity, SHARE_MEDIA.QQ, params, this);
	}

	private static void share(final Activity activity, final SHARE_MEDIA plat, final ShareParams params, final UMShareListener l) {
		if(params.shareType == ShareParams.TYPE_FILE) {
			SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
				@Override
				public void run() {
					if(params.decodeFile(activity.getApplicationContext(),
                            R.drawable.share_qrcode, R.string.share_qrcode_text)) {
                        File file = params.file;
                        if(file.getPath().contains(FileUtil.getScreenShotFileDir())) {
                            file.delete();
                        }
                        UmengSocialSDKUtils.share(activity, plat, l, params);
                    }
				}
			});
		} else {
			UmengSocialSDKUtils.share(activity, plat, l, params);
		}
	}

	@Override
	public void onStart(SHARE_MEDIA plat) {
		DtLog.d(TAG, "onStart plat : " + plat);
	}

	@Override
	public void onResult(SHARE_MEDIA plat) {
		if (!TextUtils.isEmpty(mStatisticsKeySuccess)) {
			StatisticsUtil.reportAction(mStatisticsKeySuccess);
		}

		if (!mActivity.isFinishing() && plat == SHARE_MEDIA.SINA) {
			CommonToast.showToast(R.string.sina_weibo_share_success);
		}

		clearShareTmp();

		if (mShareListener != null) {
			mShareListener.onShareListener(IShareManager.STATE_SUCCESS, getPlatName(plat));
		}

		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(ACTION_SHARE_SUCCESS));
	}

	@Override
	public void onError(SHARE_MEDIA plat, Throwable t) {
		if (!TextUtils.isEmpty(mStatisticsKeyFailed)) {
			StatisticsUtil.reportAction(mStatisticsKeyFailed);
		}

		if (plat != null && (SHARE_MEDIA.WEIXIN.equals(plat) || SHARE_MEDIA.WEIXIN_CIRCLE.equals(plat))
				&& t instanceof UmengSocialSDKUtils.WeChatNotInstalledException) {
			CommonToast.showToast(R.string.not_install_wechat);
		} else if(plat != null && (SHARE_MEDIA.QQ.equals(plat))
				&& t instanceof UmengSocialSDKUtils.QQNotInstalledException) {
			CommonToast.showToast(R.string.not_install_qq);
		}

		clearShareTmp();

		DtLog.d(TAG, "onError plat : " + plat);
		DtLog.d(TAG, "onError throwable : " + t);

		if (mShareListener != null) {
			mShareListener.onShareListener(IShareManager.STATE_ERROR, getPlatName(plat));
		}
	}

	@Override
	public void onCancel(final SHARE_MEDIA plat) {
		DtLog.d(TAG, "onCancel platform : " + plat);
		clearShareTmp();
		if (mShareListener != null) {
			mShareListener.onShareListener(IShareManager.STATE_CANCEL, getPlatName(plat));
		}
	}

	private void clearShareTmp() {
		SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if(mParamsMaps != null) {
					for(String key : mParamsMaps.keySet()) {
						ShareParams params = mParamsMaps.get(key);
						if(clearShareFile(params)) {
							break;
						}
					}
				} else if(mParams != null) {
					clearShareFile(mParams);
				}
			}
		});
	}

	private boolean clearShareFile(ShareParams params) {
		if(params != null && params.shareType == ShareParams.TYPE_FILE) {
			File shareFile = params.shareFile;
			if(shareFile != null && shareFile.exists()) {
				shareFile.delete();
				return true;
			}
		}
		return false;
	}


	private static String getPlatName(final SHARE_MEDIA plat) {
		String platName = "";
		switch (plat) {
			case QQ:
				platName = IShareManager.PLAT_QQ;
				break;
			case WEIXIN:
				platName = IShareManager.PLAT_WECHAT;
				break;
			case WEIXIN_CIRCLE:
				platName = IShareManager.PLAT_WECHAT_MOMENTS;
				break;
			case SINA:
				platName = IShareManager.PLAT_SINA_WEIBO;
				break;
			default:
				break;
		}
		return platName;
	}

}
