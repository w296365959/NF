package com.sscf.investment.sdk.net.ocr;

import android.text.TextUtils;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.net.ocr.result.RequestResult;
import com.sscf.investment.sdk.net.ocr.result.SuccessResult;
import com.sscf.investment.sdk.net.ocr.result.ErrorResult;
import com.sscf.investment.sdk.utils.OcrStatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import BEC.UserInfo;

/**
 * Created by yorkeehuang on 2017/5/22.
 */

public class OcrRequestManager {

    private static final String TAG = OcrRequestManager.class.getSimpleName();

    private static final String OCR_SERVICE_HOST = "https://ocr.wedengta.com/";

    private static final int REQUEST_TIMEOUT = 10 * 1000;

    private static final int BUFFER_SIZE = 1024;

    public RequestResult request(OcrEntity ocrEntity) {
        return new HttpRequestTask(ocrEntity).excute();
    }


    private class HttpRequestTask {

        // 定义数据分隔线
        private final String BOUNDARY = "========7d4a6d158c9";
        // 换行符
        private final String newLine = "\r\n";
        private final String boundaryPrefix = "--";

        private OcrEntity mOcrEntity;

        HttpRequestTask(OcrEntity ocrEntity) {
            mOcrEntity = ocrEntity;
        }

        public RequestResult excute() {
            HttpURLConnection conn = null;
            try {
                long time = System.currentTimeMillis();
                File imageFile = mOcrEntity.getFile();
                if(imageFile == null || !imageFile.exists() || !imageFile.isFile()) {
                    return new ErrorResult(ErrorResult.ERROR_IMAGE_LOAD);
                }

                URL url = new URL(OCR_SERVICE_HOST);
                conn = (HttpURLConnection) url.openConnection();
                DtLog.d(TAG, "RequestThread connect server success, url=" + url + ", use time=" + (System.currentTimeMillis() - time));

                conn.setRequestMethod("POST");
                conn.setConnectTimeout(REQUEST_TIMEOUT);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                // 设置请求头参数
                UserInfo userInfo = SDKManager.getInstance().getUserInfo();
                if(userInfo == null) {
                    return new ErrorResult(ErrorResult.ERROR_USERINFO);
                }
                String guid = DataUtils.bytesToHexString(userInfo.getVGUID());
                String dua = userInfo.getSDUA();
                DtLog.d(TAG, "guid = " + guid + ", dua = " + dua);
                if(TextUtils.isEmpty(guid) || TextUtils.isEmpty(dua)) {
                    return new ErrorResult(ErrorResult.ERROR_USERINFO);
                }
                conn.setRequestProperty("GUID", guid);
                conn.setRequestProperty("DUA", dua);
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("Charsert", "UTF-8");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                // 发送http数据
                time = System.currentTimeMillis();
                // 上传文件
                OutputStream out = new DataOutputStream(conn.getOutputStream());
                StringBuilder sb = new StringBuilder();
                sb.append(boundaryPrefix);
                sb.append(BOUNDARY);
                sb.append(newLine);
                // 文件参数,photo参数名可以随意修改
                sb.append("Content-Disposition: form-data;name=\"fname\";filename=\"pic.png" + imageFile.getPath()
                        + "\"" + newLine);
                sb.append("Content-Type:application/octet-stream");
                // 参数头设置完以后需要两个换行，然后才是参数内容
                sb.append(newLine);
                sb.append(newLine);

                // 将参数头的数据写入到输出流中
                out.write(sb.toString().getBytes());

                // 数据输入流,用于读取文件数据
                DataInputStream in = new DataInputStream(new FileInputStream(imageFile));
                byte[] bufferOut = new byte[BUFFER_SIZE];
                int bytes = 0;
                // 每次读1KB数据,并且将文件数据写入到输出流中
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                // 最后添加换行
                out.write(newLine.getBytes());
                in.close();

                // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
                byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                        .getBytes();
                // 写上结尾标识
                out.write(end_data);
                out.flush();
                out.close();
                DtLog.d(TAG, "RequestThread send data success" + ", use time=" + (System.currentTimeMillis() - time));

                time = System.currentTimeMillis();
                final int rspCode = conn.getResponseCode();
                DtLog.d(TAG, "conn.getResponseCode() = " + rspCode);
                if (rspCode == 200) {
                    StatisticsUtil.reportAction(OcrStatisticsConst.UPLOAD_PICTURE_SUCCESS);
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    byte tmp[] = new byte[BUFFER_SIZE];
                    int readLen;
                    DtLog.d(TAG, "start read");
                    do {
                        readLen = is.read(tmp);
                        if (readLen != -1) {
                            buffer.write(tmp, 0, readLen);
                        } else {
                            DtLog.d(TAG, "end read total length=" + buffer.size() + ", use time=" + (System.currentTimeMillis() - time));
                        }
                    } while (readLen != -1);
                    String rsp = new String(buffer.toByteArray(), "UTF-8");
                    is.close();
                    DtLog.d(TAG, "result = " + rsp.toString());
                    return new SuccessResult(rsp);
                } else {
                    return new ErrorResult(ErrorResult.ERROR_HTTP);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return new ErrorResult(ErrorResult.ERROR_HTTP);
            } finally {
                if(conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
