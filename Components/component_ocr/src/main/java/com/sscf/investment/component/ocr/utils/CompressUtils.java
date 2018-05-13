package com.sscf.investment.component.ocr.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.sscf.investment.sdk.utils.DtLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yorkeehuang on 2017/5/22.
 */

public class CompressUtils {

    private static final String TAG = CompressUtils.class.getSimpleName();

    /**
     * 图片压缩参数
     *
     * @author Administrator
     *
     */
    public static class CompressOptions {
        public static final int DEFAULT_WIDTH = 1080;
        public static final int DEFAULT_HEIGHT = 1920;

        public int maxWidth = DEFAULT_WIDTH;
        public int maxHeight = DEFAULT_HEIGHT;
        /**
         * 压缩后图片保存的文件
         */
        public File destFile;
        /**
         * 图片压缩格式,默认为jpg格式
         */
        public Bitmap.CompressFormat imgFormat = Bitmap.CompressFormat.JPEG;

        public Uri uri;

        /**
         * 图片压缩比例 默认为30
         */
        public int quality = 30;
    }

    public static boolean compressFromUri(ContentResolver cr, CompressOptions compressOptions) {
        InputStream is = null;
        try {
            Bitmap bmp = null;
            is = cr.openInputStream(compressOptions.uri);
            if(is != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeStream(is, null, options);
                int actualWidth = options.outWidth;
                int actualHeight = options.outHeight;

                int desiredWidth = getResizedDimension(compressOptions.maxWidth,
                        compressOptions.maxHeight, actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(compressOptions.maxHeight,
                        compressOptions.maxWidth, actualHeight, actualWidth);

                options.inJustDecodeBounds = false;
                options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
                        desiredWidth, desiredHeight);
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = cr.openInputStream(compressOptions.uri);
                int rotate = getRotate(compressOptions.uri);
                if(is != null) {
                    Bitmap destBitmap = BitmapFactory.decodeStream(is, null, options);
                    // If necessary, scale down to the maximal acceptable size.
                    if (destBitmap.getWidth() > desiredWidth
                            || destBitmap.getHeight() > desiredHeight) {
                        bmp = Bitmap.createScaledBitmap(destBitmap, desiredWidth,
                                desiredHeight, true);
                        destBitmap.recycle();
                    } else {
                        bmp = destBitmap;
                    }

                    if(rotate > 0) {
                        bmp = rotate(bmp, rotate);
                    }
                    // compress file if need
                    if (bmp!= null && compressOptions.destFile != null) {
                        return compressFile(compressOptions, bmp);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static int getRotate(Uri uri) {
        DtLog.d(TAG, "getRotate() uri = " + uri);
        if("file".equals(uri.getScheme())) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                DtLog.d(TAG, "getRotate() orientation = " + orientation);
                int orientationInt = Integer.valueOf(orientation);
                switch (orientationInt) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                    default:
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * compress file from bitmap with compressOptions
     *
     * @param compressOptions
     * @param bitmap
     */
    private static boolean compressFile(CompressOptions compressOptions, Bitmap bitmap) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(compressOptions.destFile);
            if(os != null) {
                return bitmap.compress(compressOptions.imgFormat, compressOptions.quality, os);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight,
                                          int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                           int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling
        // ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }
}
