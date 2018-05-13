package com.sscf.investment.socialize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.dengtacj.component.entity.ShareParams;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

/**
 * Created by davidwei on 2016/05/04
 */
public final class UmengSocialSDKUtils {

	public static final String ALI_APPID = "2017020705557628";
	public static final String WECHAT_APP_ID = "wxa5ac8b1c0a57dbe6";

	/**
	 * 没有做耗时操作，可以放到ui线程
	 */
	public static void initSDK(final Context context) {
		PlatformConfig.setQQZone("1106691754", "aAjSaYkMVFM13p1y");
		PlatformConfig.setWeixin(WECHAT_APP_ID, "a858a62d8d42e5963b12e2c2b1b08ade");
		PlatformConfig.setSinaWeibo("738034168","f90a149b1abe1fe95c76932e798686c1", "http://www.sharesdk.cn");
		Config.OpenEditor = false;
		Log.LOG = false;
		Config.DEBUG = false;
		UMShareAPI.get(context);
	}

	public static void release(final Context context) {
		UMShareAPI.get(context).release();
	}

	/**
	 * 删除第三方登录信息
	 * @param activity
	 * @param plat
     */
	public static void removeAccount(final Activity activity, final SHARE_MEDIA plat) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			removeAccountOnUI(activity, plat);
		} else {
			if (activity != null) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						removeAccountOnUI(activity, plat);
					}
				});
			}
		}
	}

	private static void removeAccountOnUI(final Activity activity, final SHARE_MEDIA plat) {
		final UMShareAPI api = UMShareAPI.get(activity);
		if (api.isAuthorize(activity, plat)) {
			api.deleteOauth(activity, plat, null);
		}
	}

	public static void onActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data) {
		UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 第三方登录
	 * @param activity
	 * @param plat
     * @param l
     */
	public static void loginByThirdParty(final Activity activity, final SHARE_MEDIA plat, final UMAuthListener l) {
		final UMShareAPI api = UMShareAPI.get(activity);
		if (plat == SHARE_MEDIA.WEIXIN || plat == SHARE_MEDIA.WEIXIN_CIRCLE) {
			if (!api.isInstall(activity, plat)) {
				l.onError(plat, 0, new WeChatNotInstalledException("wechat is not installed"));
				return;
			}
		}
		final UMShareConfig config = new UMShareConfig();
		config.isNeedAuthOnGetUserInfo(true);
		api.setShareConfig(config);
		api.doOauthVerify(activity, plat, l);
	}

	/**
	 * 获得对应平台的信息
	 * @param activity
	 * @param plat
     * @param l
     */
	public static void getPlatformInfo(final Activity activity, final SHARE_MEDIA plat, final UMAuthListener l) {
		final UMShareAPI api = UMShareAPI.get(activity);
		if (plat == SHARE_MEDIA.WEIXIN || plat == SHARE_MEDIA.WEIXIN_CIRCLE) {
			if (!api.isInstall(activity, plat)) {
				l.onError(plat, 0, new WeChatNotInstalledException("wechat is not installed"));
				return;
			}
		}
		final UMShareConfig config = new UMShareConfig();
		config.isNeedAuthOnGetUserInfo(true);
		api.setShareConfig(config);
		api.getPlatformInfo(activity, plat, l);
	}

	private static Throwable checkInstall(Activity activity, final SHARE_MEDIA plat) {
		if (plat == SHARE_MEDIA.WEIXIN || plat == SHARE_MEDIA.WEIXIN_CIRCLE) {
			if (!UMShareAPI.get(activity).isInstall(activity, plat)) {
				return new WeChatNotInstalledException("wechat is not installed");
			}
		} else if(plat == SHARE_MEDIA.QQ) {
			if (!UMShareAPI.get(activity).isInstall(activity, plat)) {
				return new QQNotInstalledException("qq is not installed");
			}
		}
		return null;
	}

	/**
	 * 分享
	 * @param activity
	 * @param plat
	 * @param params
     * @param l
     */
	public static void share(final Activity activity, final SHARE_MEDIA plat, final UMShareListener l, ShareParams params) {
		Throwable error = checkInstall(activity, plat);
		if (error != null) {
			l.onError(plat, error);
			return;
		}

		if (plat == SHARE_MEDIA.SINA) {
			// sina微博没有title，没有url，所以把title和url添加到text里
			final StringBuilder text = new StringBuilder(params.title);
			text.append(' ').append(params.msg).append(' ');
			switch (params.shareType) {
				case ShareParams.TYPE_URL:
					text.append(params.url);
					new ShareAction(activity).setPlatform(plat).setCallback(l).withText(text.toString())
							.withMedia(new UMImage(activity, params.imageUrl)).share();
					break;
				case ShareParams.TYPE_FILE:
					if (params.shareFile != null) {
						new ShareAction(activity).setPlatform(plat).setCallback(l).withText(text.toString())
								.withMedia(new UMImage(activity, params.shareFile)).share();
					}
					break;
				default:
			}
		} else {
			switch (params.shareType) {
				case ShareParams.TYPE_URL:
					final UMWeb web = new UMWeb(params.url);
					web.setTitle(params.title);
					web.setThumb(new UMImage(activity, params.imageUrl));
					web.setDescription(params.msg);
					new ShareAction(activity).setPlatform(plat).setCallback(l)
							.withText(params.msg).withMedia(web).share();
					break;
				case ShareParams.TYPE_FILE:
					if (params.shareFile != null) {
						new ShareAction(activity).setPlatform(plat).setCallback(l)
								.withMedia(new UMImage(activity, params.shareFile)).share();
					}
					break;
				default:
			}
		}
	}

	public static final class WeChatNotInstalledException extends SocializeException {
		public WeChatNotInstalledException(int i, String s) {
			super(i, s);
		}

		public WeChatNotInstalledException(String s, Throwable throwable) {
			super(s, throwable);
		}

		public WeChatNotInstalledException(String s) {
			super(s);
		}
	}

	public static final class QQNotInstalledException extends SocializeException {
		public QQNotInstalledException(int i, String s) {
			super(i, s);
		}

		public QQNotInstalledException(String s, Throwable throwable) {
			super(s, throwable);
		}

		public QQNotInstalledException(String s) {
			super(s);
		}
	}
}
