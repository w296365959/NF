package com.dengtacj.component.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.sdk.utils.FileUtil;
import java.io.File;

public class ShareParams {

	public static final int TYPE_URL = 0;
	public static final int TYPE_FILE = 1;

	public int shareType;
	public String title;
	public String msg;
	public String url;
	public String imageUrl;
	public File file;
	public File shareFile;

	public ShareParams(int shareType) {
		this.shareType = shareType;
	}

	public ShareParams() {
		this(TYPE_URL);
	}

	public boolean checkParams() {
		switch (shareType) {
			case TYPE_FILE:
				return file != null;
			default:
				return !TextUtils.isEmpty(url);
		}
	}

	public boolean decodeFile(final Context context, final int bottomRes, final int textRes) {
		if(shareFile == null) {
			Bitmap bmp = BitmapUtils.appendBitmapToBottom(context, file, bottomRes, textRes);
			if(bmp != null) {
				String path = FileUtil.getScreenShotFileDir() + "/share_tmp.png";
				File tmpfile = new File(path);
				if(tmpfile.exists()) {
					tmpfile.delete();
				}
				path = BitmapUtils.saveBitmap(bmp, path);

				bmp.recycle();

				Runtime.getRuntime().gc();
				if(!TextUtils.isEmpty(path)) {
					shareFile = new File(path);
				}
			}
		}
		return shareFile != null;
	}

	public boolean putFile(File f) {
		if(f != null && f.exists()) {
			file = f;
			shareType = TYPE_FILE;
			return true;
		}
		return false;
	}

	public boolean putFileByPath(String path) {
		return putFile(new File(path));
	}

	public static ShareParams createShareParams(String title, String msg, String url, String imageUrl) {
		final ShareParams params = new ShareParams();
		params.title = title == null ? "" : title;
		params.msg = msg;
		params.url = url;
		params.imageUrl = imageUrl;
		return params;
	}
}
