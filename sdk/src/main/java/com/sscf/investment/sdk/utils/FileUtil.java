package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import com.sscf.investment.sdk.SDKManager;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by xuebinliu on 2015/7/24.
 */
public final class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static File getFilesDir(Context context) {
        return context.getFilesDir();
    }

    /**
     * sd卡是否挂载
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取sd卡路径
     * @return
     */
    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getExternalFile(Context context, String fileName) {
        return new File(FileUtil.getExternalFilesDir(context, ""), fileName);
    }

    /**
     * 获取app在sd卡上的程序目录创建的新目录，其中程序目录为xxx/android/data/pkg/files/
     * 为避免空指针，系统方法返回null时，自己拼接路径
     * @param context
     * @param type 用户指定的子目录
     * @return
     */
    public static File getExternalFilesDir(Context context, String type) {
        File file = null;

        try {
            file = context.getExternalFilesDir(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file == null) {
            if (type == null) {
                file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/files");
            } else {
                file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/files/" + type);
            }
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取app在sd卡上的程序目录创建的新目录，其中程序目录为xxx/android/data/pkg/cache/
     * 为避免空指针，系统方法返回null时，自己拼接路径
     * @param context
     * @param type 用户指定的子目录
     * @return
     */
    public static File getExternalCacheDir(Context context, String type) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            if (type == null) {
                file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/cache");
            } else {
                file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/cache/" + type);
            }
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return file;
    }

    public static File getQuotesCacheFile(final Context context) {
        return new File(context.getFilesDir(), "quotesCache.bat");
    }

    public static File getSimpleQuotesCacheFile(final Context context) {
        return new File(context.getFilesDir(), "simpleQuotesCache.bat");
    }

    public static File getLastLiveMsgIdMapCacheFile(final Context context) {
        return new File(context.getFilesDir(), "lastLiveMsgIdMapCache.bat");
    }

    public static File getHasUnreadLiveMsgCacheFile(final Context context) {
        return new File(context.getFilesDir(), "hasUnreadLiveMsgMapCache.bat");
    }

    public static File getAccountInfoFile(final Context context) {
        return new File(context.getFilesDir(), "acct.bat");
    }

    public static File getDiscoverStockPickInfoFile(final Context context) {
        return new File(context.getFilesDir(), "pick.bat");
    }

    public static File getGoldenStockFile(final Context context) {
        return new File(context.getFilesDir(), "stockPick/goldenStock.bat");
    }

    public static File getUserInviteInfoFile(final Context context) {
        return new File(context.getFilesDir(), "stockPick/userInviteInfo.bat");
    }

    public static File getStockPickStrategyListFile(final Context context) {
        return new File(context.getFilesDir(), "stockPick/strategyList.bat");
    }

    public static File getIPInfoFile(final Context context, int env) {
        return new File(context.getFilesDir(), env + "ip.bat");
    }

    public static File getPortfolioIndexesFile(final Context context) {
        return new File(context.getFilesDir(), "portfolio_indexes.bat");
    }

    public static File getPushMsgIdFile(final Context context) {
        return new File(context.getFilesDir(), "msgid.bat");
    }

    public static File getUrlsFile(final Context context) {
        return new File(context.getFilesDir(), "urls.bat");
    }

    public static File getPushSwitchFile(final Context context) {
        return new File(context.getFilesDir(), "push/push_switch.bat");
    }

    public static File getRemindItemsFile(final Context context, final long id) {
        return new File(context.getFilesDir(), "remind/items" + id);
    }

    public static File getUnreadRemindIdsFile(final Context context, final long id) {
        return new File(context.getFilesDir(), "remind/unread" + id);
    }

    public static File getMessageEndTimeFile(final Context context) {
        return new File(context.getFilesDir(), "remind/endTime.bat");
    }

    public static File getInvestmentAdviserListRecommendedFile(final Context context) {
        return new File(context.getFilesDir(), "infomation/InvestmentAdviserListRecommended.bat");
    }

    public static File getVideoGroupListFile(final Context context) {
        return new File(context.getFilesDir(), "infomation/videoGroupList.bat");
    }

    public static byte[] getByteArrayFromInputStream(InputStream in) {
        final byte[] buffer = new byte[1024];
        int byteread = 0;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            out.flush();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(in, out);
        }
        return null;
    }

    public static byte[] getByteArrayFromFile(File file) {
        if (file != null && file.isFile()) {
            FileInputStream in = null;
            try {
                return getByteArrayFromInputStream(new FileInputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean mkParentDir(final File file) {
        final File parentDir = file.getParentFile();
        if (!parentDir.exists() || !parentDir.isDirectory()) {
            return parentDir.mkdirs();
        }
        return true;
    }

    public static boolean saveByteArrayToFile(byte[] data, File file) {
        if (file != null && data != null) {
            if (!mkParentDir(file)) {
                return false;
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeStream(fos);
            }
        }
        return false;
    }

    public static boolean saveObjectToFile(Object obj, File file) {
        if (obj == null || file == null) {
            return false;
        }
        if (!mkParentDir(file)) {
            return false;
        }

        ObjectOutputStream out = null;
        final long start = System.currentTimeMillis();
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeObject(obj);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            if (file.isFile()) {
                file.delete();
            }
        } finally {
            closeStream(out);
            DtLog.d(TAG, "saveObjectToFile spend " + (System.currentTimeMillis() - start) + " " + file.getAbsolutePath());
        }
        return false;
    }

    public static boolean saveInputStreamToFile(InputStream in, File file) {
        if (in == null || file == null) {
            return false;
        }

        if (!mkParentDir(file)) {
            return false;
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            copyStream(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (file.isFile()) {
                file.delete();
            }
        } finally {
            closeStream(out);
        }
        return false;
    }

    public static Object getObjectFromFile(File file) {
        if (file != null && file.isFile() && file.exists()) {
            ObjectInputStream in = null;
            final long start = System.currentTimeMillis();
            try {
                in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                return in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(in);
                DtLog.d(TAG, "getObjectFromFile spend " + (System.currentTimeMillis() - start) + " " + file.getAbsolutePath());
            }
        }
        return null;
    }

    public static void closeStream(InputStream in) {
        closeStream(in, null);
    }

    public static void closeStream(OutputStream out) {
        closeStream(null, out);
    }

    public static void closeStream(Reader reader) {
        closeStream(reader, null);
    }

    public static void closeStream(Writer writer) {
        closeStream(null, writer);
    }

    /**
     * 关闭流
     * @param in
     * @param out
     */
    public static void closeStream(InputStream in, OutputStream out) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭流
     */
    public static void closeStream(Reader reader, Writer writer) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并文件(把assets目录下的多个小文件合并成一个大文件，因其中的文件大小不允许超过1M)
     *
     * @param c
     * @param partFileList 小文件名集合
     * @param dst          目标文件路径
     * @throws IOException
     * @author zuolongsnail
     */
    public static void mergeAssetsFiles(Context c, ArrayList<String> partFileList, String dst) throws IOException {
        File dstFile = new File(dst);
        if (dstFile.exists()) {
            dstFile.delete();
        }

        OutputStream out = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        InputStream in;
        int readLen = 0;
        for (int i = 0; i < partFileList.size(); i++) {
            // 获得输入流
            in = openAssetsInput(partFileList.get(i));
            while ((readLen = in.read(buffer)) != -1) {
                out.write(buffer, 0, readLen);
            }
            out.flush();
            in.close();
        }
        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了
        out.close();
    }

    public static InputStream openAssetsInput(String assetsName)
        throws IOException {

        return SDKManager.getInstance().getContext().getAssets()
            .open(assetsName, AssetManager.ACCESS_STREAMING);
    }

    public static boolean copyFile(InputStream oldFileStream, String newPath) {
        if (oldFileStream == null) {
            return false;
        }

        FileOutputStream fs = null;

        try {
            int byteread = 0;
            // 文件存在时
            fs = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1444];
            while ((byteread = oldFileStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oldFileStream != null) {
                    oldFileStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean copyAssertFile(Context context, String assetFilePath, File destFile) {
        mkParentDir(destFile);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = context.getAssets().open(assetFilePath);
            out = new FileOutputStream(destFile);
            copyStream(in, out);
            return true;
        } catch (IOException e) {
            DtLog.w(TAG, e.getMessage());
        } finally {
            closeStream(in, out);
        }
        return false;
    }

    public static String getFileMD5(final File file) {
        final byte[] buffer = new byte[8 * 1024];
        int byteread = 0;
        InputStream in = null;
        try {
            MessageDigest md5Digest = java.security.MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((byteread = in.read(buffer)) != -1) {
                md5Digest.update(buffer, 0, byteread);
            }
            return DataUtils.bytesToHexString(md5Digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(in);
        }
        return null;
    }

    public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
        copyStream(in, out, 1024 * 8);
    }

    public static void copyStream(final InputStream in, final OutputStream out, final int bufferSize) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int readBytes = 0;
        while ((readBytes = in.read(buffer)) != -1) {
            out.write(buffer, 0, readBytes);
        }
        out.flush();
    }

    public static void cleanDirectory(File directory) {
        if (!directory.exists()) {
            DtLog.w(TAG, directory + " does not exist");
            return;
        }

        if (!directory.isDirectory()) {
            DtLog.w(TAG, directory + " is not a directory");
            return;
        }

        final File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            try {
                forceDelete(file);
            } catch (Exception e) {
                DtLog.w(TAG, e);
            }
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: "
                        + file);
                }
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        cleanDirectory(directory);

        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file
                    + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file
                + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
     * 保存文件
     *
     * @param file
     * @param data
     * @return
     */
    public static boolean save(File file, byte[] data) {
        OutputStream os = null;
        try {
            os = openOutputStream(file);
            os.write(data, 0, data.length);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 读取文件流
     * @param file
     * @return
     */
//    public static byte[] read(File file) {
//        if (file == null || !file.exists()) {
//            return null;
//        }
//        InputStream in = null;
//        try {
//            in = openInputStream(file);
//            int bufLen = 512;
//            byte[] buffer = new byte[bufLen];
//            ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(bufLen);
//
//            int len = 0;
//            while ((len = in.read(buffer) )!= -1) {
//                arrayBuffer.append(buffer, 0, len);
//            }
//
//            return arrayBuffer.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    /**
     * 计算文件md5
     */
//    public static String computeMd5(byte[] data) {
//        String md5 = "";
//        if (data != null && data.length > 0) {
//            md5 = StringUtil.toHexString(StringUtil.getMD5(data));
//        }
//        return md5;
//    }

//    public static String getFileMD5(final File file) {
//        final byte[] buffer = new byte[8 * 1024];
//        int byteread = 0;
//        InputStream in = null;
//        try {
//            MessageDigest md5Digest = java.security.MessageDigest.getInstance("MD5");
//            in = new FileInputStream(file);
//            while ((byteread = in.read(buffer)) != -1) {
//                md5Digest.update(buffer, 0, byteread);
//            }
//            return DataUtils.bytesToHexString(md5Digest.digest());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            FileUtil.closeStream(in);
//        }
//        return null;
//    }

    public static FileOutputStream openOutputStream(String filePath)
        throws IOException {
        return openOutputStream(new File(filePath));
    }

    public static FileOutputStream openOutputStream(File file)
        throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file
                    + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file
                    + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("File '" + file
                        + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    /**
     * 为了调试目的，把一些config项放在SD卡根目录
     */
    public static Map<String, String> getTestConfigFromFile() {
        Map<String, String> configs = new HashMap<>();

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"beacon_config.txt");
        FileInputStream fis = null;
        BufferedReader br = null;
        String line = null;
        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));
            do {
                line = br.readLine();
                if (!TextUtils.isEmpty(line)) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    String[] splits = line.split("=");
                    String key = splits[0];
                    String value = splits[1];
                    configs.put(key.trim(), value.trim());
                }
            } while (line != null);
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            DtLog.d(TAG, "getTestConfigFromFile: Config File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return configs;
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(final File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String path) {
        Uri uri = Uri.fromFile(new File(path));
        return getPdfFileIntent(uri);
    }

    public static Intent getPdfFileIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static String getExternalCacheDir() {
        File externalCacheDir = FileUtil.getExternalCacheDir(SDKManager.getInstance().getContext(), null);
        return externalCacheDir.getAbsolutePath();
    }
//
//    public static String getPdfFilePathByUrl(String url) {
//        String externalCacheDir = SDKManager.getInstance().getContext().getCacheDir().getAbsolutePath();
//        final String filePath = externalCacheDir + File.separator + "pdf" + File.separator + DownloadUtils.hashKeyForDisk(url) + ".pdf";
//        return filePath;
//    }
//
//    public static String getSplashImageFilePathByUrl(final String url) {
//        String externalFileDir = getSplashImageFileDir();
//        final String filePath = externalFileDir + File.separator + DownloadUtils.hashKeyForDisk(url);
//        return filePath;
//    }

    public static String getSplashImageFileDir() {
        return FileUtil.getExternalFilesDir(SDKManager.getInstance().getContext(), "splash").getAbsolutePath();
    }

    public static String getPackageDataPath() {
        return "/data/data/" + SDKManager.getInstance().getContext().getPackageName();
    }

    public static String getSDCardPackageDataPath() {
        return getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + SDKManager.getInstance().getContext().getPackageName();
    }

    public static InputStream getAssetsFileInput(final Context context, final String fileName) {
        InputStream in = null;
        try {
            in = context.getAssets().open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    public static File getSelectedStrategyListFile(final Context context) {
        return new File(context.getFilesDir(), "stockPick/selectedStrategyList.bat");
    }

    public static File getPdfDir() {
        return getExternalFilesDir(SDKManager.getInstance().getContext(), "pdf");
    }

    /**
     * pdf以前放到/data/data下
     * @return
     */
    public static File getOldPdfDir() {
        return new File(SDKManager.getInstance().getContext().getCacheDir(), "pdf");
    }

    public static File getPdfFileByUrl(String url) {
        return new File(getPdfDir(), DownloadUtils.hashKeyForDisk(url) + ".pdf");
    }

    public static String getSplashImageFilePathByUrl(final String url) {
        String externalFileDir = getSplashImageFileDir();
        final String filePath = externalFileDir + File.separator + DownloadUtils.hashKeyForDisk(url);
        return filePath;
    }

    public static String getAdImageFilePathByUrl(String url) {
        String externalFileDir = getAdImageFileDir();
        final String filePath = externalFileDir + File.separator + DownloadUtils.hashKeyForDisk(url);
        return filePath;
    }

    public static String getAdImageFileDir() {
        return FileUtil.getExternalFilesDir(SDKManager.getInstance().getContext(), "ad").getAbsolutePath();
    }

    public static String getAdImageTempFile() {
        String externalFileDir = getAdImageFileDir();
        final String filePath = externalFileDir + File.separator + "cache.tmp";
        return filePath;
    }

    public static String getScreenShotFileDir() {
        return FileUtil.getExternalFilesDir(SDKManager.getInstance().getContext(), "screen_shot").getAbsolutePath();
    }

    public static String getScanFileDir() {
        return FileUtil.getExternalFilesDir(SDKManager.getInstance().getContext(), "scan").getAbsolutePath();
    }

    public static File getMessageCenterDataFile(final Context context, long id) {
        return new File(context.getFilesDir(), "messageCenter/messageCenter" + id);
    }

    public static File getCrashDir(final Context context) {
        return getExternalFilesDir(context, "crash");
    }

    public static String getStatFileDir() {
        return FileUtil.getExternalFilesDir(SDKManager.getInstance().getContext(), "stat").getAbsolutePath();
    }

    public static long getFilesLength(final List<String> paths) {
        long length = 0L;
        if (paths != null) {
            for (Iterator<String> iterator = paths.iterator(); iterator
                    .hasNext();) {
                length += getFileLength(new File(iterator.next()));
            }
        }
        return length;
    }

    public static long getFilesLength(final String[] paths) {
        long length = 0L;
        if (paths != null) {
            for (int i = 0; i < paths.length; i++) {
                length += getFileLength(new File(paths[i]));
            }
        }
        return length;
    }

    public static long getFilesLength(final File[] files) {
        long length = 0L;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                length += getFileLength(files[i]);
            }
        }
        return length;
    }

    public static long getFileLength(File file) {
        long length = 0L;
        if (file == null || !file.exists()) {
            return length;
        }

        if (file.isDirectory()) {
            // 防止栈溢出
            if (getFilePathDeep(file) > 20) {
                return length;
            }

            File[] files = null;
            try {// avoid OutOfMemoryError cause by listFiles()
                files = file.listFiles();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            final int fileCount = files == null ? 0 : files.length;
            for (int i = 0; i < fileCount; i++) {
                length += getFileLength(files[i]);
            }
        } else {
            length = file.length();
        }
        return length;
    }

    private static int getFilePathDeep(final File file) {
        return getFilePathDeep(file.getAbsolutePath());
    }

    private static int getFilePathDeep(final String absolutePath) {
        return absolutePath.split("/").length;
    }
}
