package com.sscf.investment.sdk.net;

import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.dengtacj.thoth.MapProtoLite;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xuebinliu on 2015/7/24.
 * http数据源
 */
public class HttpDataSource extends DataSourceProxy {
    private static final String TAG = DataEngine.TAG;

    private String name = "";

    public HttpDataSource(String name) {
        DtLog.d(TAG, "HttpDataSource constructor name=" + name);

        this.name = name;
    }

    @Override
    public boolean request(int reqType, Object reqData, IRequestCallback observer, Object extra) {
        final MapProtoLite uniPacket = ProtoManager.getRequestProto(reqType, reqData);
        uniPacket.setRequestId(generateReqId());
        // 放入发送队列
        putCallback(uniPacket.getRequestId(), new ReqParams(reqType, observer, extra));
        SDKManager.getInstance().getDefaultExecutor().execute(new RequestThread(uniPacket));

        DtLog.d(TAG, name + "-" + "request reqType=" + reqType + ", reqID=" + uniPacket.getRequestId() + " reqData=" + reqData.toString());
        return true;
    }

    @Override
    public void destroy() {
        DtLog.d(TAG, name + "-" + "destroy");
    }

    private void handleError(MapProtoLite packet) {
        ReqParams reqParams = popCallback(packet.getRequestId());
        if (reqParams != null) {
            reqParams.callback.callback(false, new EntityObject(reqParams.reqType, null, reqParams.extra));
        }
    }

    private void handleResponseData(MapProtoLite packet) {
        if (packet == null) {
            DtLog.w(TAG, name + "-" + "handleResponseData packet=null");
        }

        ReqParams reqParams = popCallback(packet.getRequestId());
        if (reqParams == null) {
            return;
        }
        final Object resObject = ProtoManager.getResponseObject(reqParams.reqType, packet);

        if (resObject != null) {
            reqParams.callback.callback(true, new EntityObject(reqParams.reqType, resObject, reqParams.extra));
        } else {
            reqParams.callback.callback(false, new EntityObject(reqParams.reqType, null, reqParams.extra));
            DtLog.e(TAG, name + "-" + "handleResponseData callback=" + reqParams.callback + ", resObject=" + resObject);
        }
    }

    public class RequestThread implements Runnable {
        private MapProtoLite mapProtoLite;

        public RequestThread(MapProtoLite mapProtoLite) {
            this.mapProtoLite = mapProtoLite;
        }

        @Override
        public void run() {
            try {
                long time = System.currentTimeMillis();
                if (mapProtoLite == null) {
                    DtLog.d(TAG, name + "-" + "RequestThread invalid param");
                    return;
                }

                URL url = new URL(IPManager.DEFAULT_HTTP_DOMAIN);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                DtLog.d(TAG, name + "-" + "RequestThread connect server success, url=" + url + ", use time=" + (System.currentTimeMillis() - time));

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-type", "application/octet-stream");

                // 发送http数据
                time = System.currentTimeMillis();
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(mapProtoLite.encode());
                outputStream.close();
                DtLog.d(TAG, name + "-" + "RequestThread send data success" + ", use time=" + (System.currentTimeMillis() - time));

                time = System.currentTimeMillis();
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    byte tmp[] = new byte[NetworkConst.READ_BUFFER_LEN];
                    int readLen;
                    DtLog.d(TAG, name + "-" + "start read");
                    do {
                        readLen = is.read(tmp);
                        if (readLen != -1) {
                            buffer.write(tmp, 0, readLen);
                        } else {
                            DtLog.d(TAG, name + "-" + "end read total length=" + buffer.size() + ", use time=" + (System.currentTimeMillis() - time));
                        }
                    } while (readLen != -1);
                    is.close();

                    final MapProtoLite packet = new MapProtoLite();
                    packet.decode(buffer.toByteArray(), NetworkConst.PACKET_HEAD_LENGTH);
                    handleResponseData(packet);
                }
                conn.disconnect();
            } catch (Exception e) {
                DtLog.e(TAG, name + "-" + "RequestThread error, req id=" + mapProtoLite.getRequestId() + ", server=" +
                        mapProtoLite.getHandleName() + ", func=" + mapProtoLite.getFuncName());
                handleError(mapProtoLite);
            }
        }
    }
}
