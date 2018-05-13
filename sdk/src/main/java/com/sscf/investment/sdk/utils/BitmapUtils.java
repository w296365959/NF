package com.sscf.investment.sdk.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.sscf.investment.sdk.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class BitmapUtils {

    public static Drawable getRoundDrawable(final int color, final float radius) {
        final GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    public static Drawable getRoundDrawable(final Bitmap bitmap) {
        final ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        shapeDrawable.getPaint().setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        return shapeDrawable;
    }

    public static Bitmap decodeBitmap(String pathName) {
        if (TextUtils.isEmpty(pathName)) {
            return null;
        }

        try {
            return BitmapFactory.decodeFile(pathName);
        } catch (OutOfMemoryError ex) {
            return null;
        }
    }

    /**
     * 根据指定的宽高，将源Bitmap拉伸，创建新的Bitmap
     *
     * @param src
     * @param dstWidth
     * @param dstHeight
     */
    public static Bitmap scaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        if (src == null || dstWidth <= 0 || dstHeight <= 0) {
            return null;
        }

        try {
            return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
        } catch (OutOfMemoryError ex) {
            return null;
        }
    }

    /**
     * 获取 放缩的 Bitmap
     *
     * @param src         原始的Bitmap
     * @param scaleWidth  宽度放缩系数
     * @param scaleHeight 高度放缩系数
     *                    leonwan 修改 2012-05-30
     */
    public static Bitmap scaleBitmap(Bitmap src, float scaleWidth, float scaleHeight) {
        if (src == null || scaleWidth <= 0 || scaleHeight <= 0) {
            return null;
        }

        final int srcWidth = src.getWidth();
        final int srcWeight = src.getHeight();

        final int dstWidth = (int) Math.ceil(srcWidth * scaleWidth);
        final int dstHeight = (int) Math.ceil(srcWeight * scaleHeight);

        try {
            return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
        } catch (OutOfMemoryError ex) {
            // 解决trying to use a recycled bitmap问题 leonwan
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 1 / 2 , bitmap.getHeight() * 1 /2 , false);

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }

    public static Bitmap appendBitmapToBottom(final Context context, File file, int bottomDrawableRes, int textRes) {
        if(file != null && file.exists() && bottomDrawableRes != 0) {
            return appendBitmapToBottom(context, decodeBitmap(file.getPath()), bottomDrawableRes, textRes);
        }
        return null;
    }

    public static Bitmap appendBitmapToBottom(final Context context, Bitmap src, int bottomDrawableRes, int textRes) {
        if(src != null && bottomDrawableRes != 0) {
            Resources res = context.getResources();

            int width = src.getWidth();
            int height = src.getHeight();

            Bitmap bottom = decodeResource(res, bottomDrawableRes);
            if(bottom != null) {
                int bWidth = bottom.getWidth();
                int bHeight = bottom.getHeight();
                int totalHeight = height + context.getResources().getDimensionPixelSize(R.dimen.screen_shot_bottom_height);
                Bitmap result = createBitmap(width, totalHeight, Bitmap.Config.RGB_565);
                if(result != null) {
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint();
                    canvas.drawBitmap(src, 0, 0, paint);
                    src.recycle();

                    paint.setColor(ContextCompat.getColor(context, R.color.qrcode_background_color));
                    canvas.drawRect(0, height, width, totalHeight, paint);

                    Rect srcRect = new Rect(0, 0, bWidth, bHeight);
                    int bottomPaddingTop = res.getDimensionPixelSize(R.dimen.screen_shot_bottom_padding_top);
                    int targetWidthHeight = res.getDimensionPixelSize(R.dimen.append_qrcode_width_height);
                    int left = (width - targetWidthHeight) / 2;
                    int top = height + bottomPaddingTop;
                    Rect targetRect = new Rect(left, top, left + targetWidthHeight, top + targetWidthHeight);
                    canvas.drawBitmap(bottom, srcRect, targetRect, paint);
                    bottom.recycle();

                    if(textRes != 0) {
                        String text = res.getString(textRes);
                        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setTextSize(res.getDimensionPixelSize(R.dimen.qrcode_text_size));
                        paint.setColor(ContextCompat.getColor(context, R.color.qrcode_text_color));
                        float textWidth = paint.measureText(text);
                        Paint.FontMetrics fm = new Paint.FontMetrics();
                        paint.getFontMetrics(fm);
                        float textHeight = (float) Math.ceil(-fm.ascent);

                        int textTop = targetRect.bottom + context.getResources().getDimensionPixelSize(R.dimen.append_qrcode_text_margin_top);
                        int textLeft = (int)((width - textWidth) / 2);

                        canvas.drawText(text, textLeft, textTop + textHeight, paint);
                    }
                } else {
                    src.recycle();
                    bottom.recycle();
                }
                Runtime.getRuntime().gc();
                return result;
            }
        }
        return null;
    }

    public static Bitmap decodeResource(Resources resources, int res) {
        try {
            return BitmapFactory.decodeResource(resources, res);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveBitmap(Bitmap bmp, String path) {
        if(bmp != null && !TextUtils.isEmpty(path)) {
            // 测试输出
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                if (null != out) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    return path;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtil.closeStream(out);
            }
        }
        return null;
    }

    /**
     * 纯色的图片，修改图片颜色
     */
    public static Bitmap modifyBitmapColor(final Resources res, final int drawableId, final int color) {
        return modifyBitmapColor(BitmapFactory.decodeResource(res, drawableId), color);
    }

    /**
     * 纯色的图片，修改图片颜色
     */
    public static Bitmap modifyBitmapColor(final Bitmap src, final int color) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        final float[] colorMatrixSrc = new float[] {
                0, 0, 0, 0, Color.red(color),
                0, 0, 0, 0, Color.green(color),
                0, 0, 0, 0, Color.blue(color),
                0, 0, 0, 1, Color.alpha(color) - 255};
        // 使用ColorMatrix创建一个ColorMatrixColorFilter对象, 作为画笔的滤镜, 设置Paint的颜色
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrixSrc));

        // 获取一个与baseBitmap大小一致的可编辑的空图片
        final Bitmap dist = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        final Canvas canvas = new Canvas(dist);
        // 5.通过指定了RGBA矩阵的Paint把原图画到空白图片上
        canvas.drawBitmap(src, new Matrix(), paint);
        return dist;
    }
}
