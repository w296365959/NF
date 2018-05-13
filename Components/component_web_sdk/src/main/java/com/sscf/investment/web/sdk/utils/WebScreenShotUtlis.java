package com.sscf.investment.web.sdk.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.view.View;
import com.sscf.investment.sdk.utils.BitmapUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.web.sdk.R;
import com.sscf.investment.web.sdk.widget.DtWebView;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import java.io.File;

/**
 * Created by yorkeehuang on 2017/8/25.
 */

public class WebScreenShotUtlis {

    private static final String TAG = WebScreenShotUtlis.class.getSimpleName();
    /**
     * 对webview进行截图
     * @param webView
     * @return
     */
    public static Bitmap getWebViewBitmap(Resources resources, DtWebView webView, View actionbar) {
        IX5WebViewExtension extension = webView.getX5WebViewExtension();
        View view = webView.getView();
        if(extension != null && view != null) {
            view.requestLayout();
            webView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int actionbarHeight = resources.getDimensionPixelSize(R.dimen.actionbar_height);
            int webViewWidth = webView.getMeasuredWidth();
            int webViewHeight = webView.getMeasuredHeight();
            DtLog.d(TAG, "getWebViewBitmap() webViewWidth = " + webViewWidth + ", webViewHeight = " + webViewHeight);

            webView.layout(0, actionbarHeight, webViewWidth, webViewHeight);

            Bitmap longImage = BitmapUtils.createBitmap(webViewWidth, webViewHeight + actionbarHeight, Bitmap.Config.RGB_565);
            if(longImage != null) {
                Canvas longImageCanvas = new Canvas(longImage);
                longImageCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
                // 绘制actionbar
                actionbar.draw(longImageCanvas);

                Bitmap x5Bitmap = BitmapUtils.createBitmap(webViewWidth, webViewHeight, Bitmap.Config.RGB_565);
                if(x5Bitmap != null) {
                    float scale = resources.getDisplayMetrics().density;

                    Canvas x5Canvas = new Canvas(x5Bitmap);
                    x5Canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                    x5Canvas.scale(scale, scale);
                    extension.snapshotWholePage(x5Canvas, false, false); // 少了这行代码就无法正常生成长图

                    // 绘制webview截图
                    longImageCanvas.drawBitmap(x5Bitmap, 0, actionbarHeight, new Paint(Paint.ANTI_ALIAS_FLAG));

                    x5Bitmap.recycle();
                    view.requestLayout();
                    return longImage;
                } else {
                    longImage.recycle();
                }
            }
        }

        return null;
    }

    public static void clearAllScreenShots() {
        File dir = new File(FileUtil.getScreenShotFileDir());
        if(dir.isDirectory()) {
            String[] fileNameList = dir.list();
            if(fileNameList != null && fileNameList.length > 0) {
                for(String fileName : fileNameList) {
                    File file = new File(dir, fileName);
                    file.delete();
                }
            }
        }
    }

    public static String createScreenShotFilePath() {
        return FileUtil.getScreenShotFileDir() + "/screenshot" + "_" + System.currentTimeMillis() + ".png";
    }
}
