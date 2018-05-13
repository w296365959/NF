package com.sscf.investment.sdk.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

/**
 * Created by liqf on 2015/10/27.
 */
public class DownloadUtils {

    private static final String TAG = DownloadUtils.class.getSimpleName();

    public static boolean httpDownload(String httpUrl, String saveFile) {
        return httpDownload(httpUrl, saveFile, null);
    }

    public static boolean httpDownload(String httpUrl, String saveFile, HttpDownloadCallback callback) {
        return httpDownload(httpUrl, new File(saveFile), callback);
    }

    public static boolean httpDownload(String httpUrl, File saveFile) {
        return httpDownload(httpUrl, saveFile, null);
    }

    /**
     * http下载
     */
    public static boolean httpDownload(String httpUrl, File saveFile, HttpDownloadCallback callback) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        boolean success = true;

        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            success = false;
        }

        if (!success) {
            if (null != callback) {
                callback.onDownloadComplete(httpUrl, false);
            }
            return false;
        }

        saveFile.mkdirs();
        if (saveFile.exists()) {
            saveFile.delete();
        }

        FileOutputStream fs = null;
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            InputStream inStream = conn.getInputStream();
            int contentLength = conn.getContentLength();
            fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1024 * 10];
            float progress = 0;
            float prevProgress = 0;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                DtLog.d(TAG, "httpDownload: bytesum = " + bytesum + ", contentLength = " + contentLength);
                progress = (float) bytesum / contentLength;
                DtLog.d(TAG, "httpDownload: progress = " + progress + ", url = " + httpUrl);
                if (progress - prevProgress >= 0.01f || progress == 1.0f) {
                    prevProgress = progress;
                    if (null != callback) {
                        callback.onProgressUpdated(httpUrl, progress);
                    }
                }
                fs.write(buffer, 0, byteread);
            }
            success = true;
        } catch (IOException e) {
            saveFile.delete();
            e.printStackTrace();
            success = false;
        } finally {
            if (null != callback) {
                callback.onDownloadComplete(httpUrl, success);
            }
            if (null != fs) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return success;
    }

    public interface HttpDownloadCallback {
        void onProgressUpdated(final String url, final float progress);
        void onDownloadComplete(final String url, boolean success);
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (Exception e) {
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;
    }

    private static final char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    private static String bytesToHexString(byte[] bytes) {
        int j = bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

}
