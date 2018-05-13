package com.sscf.investment.sdk.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    private static final String TAG = "ZipUtils";

    public static boolean unzipFiles(File zipFile, String folderPath) {
        return unzipFiles(zipFile, new File(folderPath));
    }

    public static boolean unzipFiles(File zipFile, File desDir) {
        ZipFile zf = null;
        try {
            zf = new ZipFile(zipFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (zf == null) {
            return false;
        }

        boolean result = true;
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
            result = unzipZipEntry(zf, (ZipEntry) entries.nextElement(), desDir);
            if (!result) { // 某一个文件解压失败，就算整个zip解压失败
                break;
            }
        }
        close(zf);
        return result;
    }

    private static boolean unzipZipEntry(final ZipFile zf, final ZipEntry zipEntry, File desDir) {
        String zipEntryName = zipEntry.getName();
        try {
            zipEntryName = new String(zipEntryName.getBytes("8859_1"), "GB2312");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final File desFile = new File(desDir, zipEntryName);
        if (zipEntry.isDirectory()) {
            FileUtil.mkParentDir(desFile);
            return true;
        } else {
            if (!FileUtil.mkParentDir(desFile)) {
                return false;
            }

            InputStream in = null;
            OutputStream out = null;
            try {
                in = zf.getInputStream(zipEntry);
                out = new FileOutputStream(desFile);
                FileUtil.copyStream(in, out);
                DtLog.d(TAG, "unzipZipEntry success : " + desFile);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                DtLog.d(TAG, "unzipZipEntry failed : " + desFile);
                return false;
            } finally {
                FileUtil.closeStream(in, out);
            }
        }
    }

    /**
     * 只保存一级目录的所有文件
     * @return
     */
    public static boolean zipOutput(final File[] files, final OutputStream out) {
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (zipOut == null) {
            return false;
        }

        for (File file : files) {
            zipFile(file, zipOut);
        }

        FileUtil.closeStream(zipOut);
        return true;
    }

    public static boolean zipFile(final File file, final ZipOutputStream zipOut) {
        final ZipEntry zipEntry = new ZipEntry(file.getName());
        FileInputStream in = null;
        try {
            zipOut.putNextEntry(zipEntry);
            in = new FileInputStream(file);
            FileUtil.copyStream(in, zipOut);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(in);
        }
        return false;
    }

    public static void close(final ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] gZIPCompress(final byte[] data) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutput = null;
        try {
            gzipOutput = new GZIPOutputStream(baos);
            gzipOutput.write(data);
            gzipOutput.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(gzipOutput);        }
        return null;
    }

    public static byte[] gZIPUncompress(final byte[] data) {
        final ByteArrayInputStream bais = new ByteArrayInputStream(data);
        GZIPInputStream gzipInput = null;
        try {
            gzipInput = new GZIPInputStream(bais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] result = null;
        if (gzipInput != null) {
            result = FileUtil.getByteArrayFromInputStream(gzipInput);
        }
        return result;
    }
}
