package com.sscf.investment.sdk.net;

import BEC.OpenApiReq;
import BEC.OpenApiRsp;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.core.AuthManager;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.NetUtil;
import com.dengtacj.thoth.MapProtoLite;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by xuebinliu on 2015/7/24.
 * 主要功能：
 * Socket链接、收发数据、解析数据
 */
public class SocketDataSource extends DataSourceProxy {
    private static final String TAG = "SocketDataSource";

    // 长连接socket及相关对象
    private Socket mSocket = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;

    // 是否开始接收消息
    private boolean mIsStartReceive = false;

    // 收发消息handler
    private Handler mSendHandler;
    private Handler mReceiveHandler;

    // 收发消息线程
    private long mSendTime;
    private HandlerThread mSendThread;
    private HandlerThread mReceiveThread;

    // 数据源类型：行情、业务
    private String name = "";
    private boolean isQuote;

    public SocketDataSource(boolean isQuote) {
        this.isQuote = isQuote;
        if (isQuote) {
            name = "Quote";
        } else {
            name = "Business";
        }
        DtLog.d(TAG, "constructor name=" + name);

        // init socket发送线程
        mSendThread = new HandlerThread(name + "-" +  "SendThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mSendThread.start();
        mSendHandler = new Handler(mSendThread.getLooper(), mSocketSendCallback);
        DtLog.d(TAG, name + "-" + "init SendThread ok");

        // init socket接收线程
        mReceiveThread = new HandlerThread(name + "-" + "ReceiveThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mReceiveThread.start();
        mReceiveHandler = new Handler(mReceiveThread.getLooper(), mSocketReceiverCallback);
        DtLog.d(TAG, name + "-" + "init ReceiveThread ok");

        // 如果是联网状态，连接服务器
        if (NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
            mSendHandler.sendEmptyMessage(NetworkConst.SOCKET_CONNECT);
        }
    }

    // http联网状态，尝试socket连接服务器
    public void tryToConnectSever() {
        DtLog.w(TAG, name + "-" + "tryToConnectSever");
        if (mSendHandler != null) {
            mSendHandler.sendEmptyMessage(NetworkConst.SOCKET_TRY_CONNECT);
        } else {
            DtLog.w(TAG, name + "-" + "mSendHandler == null");
        }
    }

    @Override
    public void destroy() {
        DtLog.d(TAG, name + "-" + "destroy");
        if (mSendHandler != null) {
            // 关闭socket连接
            mSendHandler.sendEmptyMessage(NetworkConst.SOCKET_CLOSE);
        }
    }

    public void releaseThread() {
        if (mSendHandler != null) {
            mSendHandler.sendEmptyMessage(NetworkConst.THREAD_RELEASE);
        }
        if (mReceiveHandler != null) {
            mReceiveHandler.sendEmptyMessage(NetworkConst.THREAD_RELEASE);
        }
    }

    @Override
    public boolean request(int reqType, Object reqData, IRequestCallback observer, Object extra) {
        MapProtoLite uniPacket = ProtoManager.getRequestProto(reqType, reqData);
        int reqId = generateReqId();
        if (!AuthManager.getInstance().isBeaconApp()) {
            // 第三方APP 使用鉴权通信
            uniPacket.setRequestId(reqId);
            OpenApiReq openApiReq = new OpenApiReq();
            openApiReq.iAppID = SDKManager.getInstance().getAppId();
            openApiReq.sPackageName = SDKManager.getInstance().getContext().getPackageName();
            openApiReq.sSign = AuthManager.getInstance().getCurrentSHA1();
            openApiReq.sGuid = DataUtils.bytesToHexString(AuthManager.getInstance().getGuid());
            openApiReq.sDua = AuthManager.getInstance().getDua();
            openApiReq.vBuffer = uniPacket.encode();

            uniPacket = ProtoManager.getRequestProto(EntityObject.ET_OPEN_API, openApiReq);

            // 设置请求序号
            uniPacket.setRequestId(reqId);
            // 发送请求
            request(uniPacket.encode());
            // 保存请求类型，用于对返回数据包解码
            ReqParams reqParams = new ReqParams(EntityObject.ET_OPEN_API, observer, extra);
            reqParams.openApiType = reqType;
            DtLog.d(TAG, name + "-" + "request openApiType=" + reqType);
            // 放入发送队列
            putCallback(uniPacket.getRequestId(), reqParams);
        } else {
            // 设置请求序号
            uniPacket.setRequestId(reqId);
            // 设置guid
            final String guid = SDKManager.getInstance().getGuid();
            if (!TextUtils.isEmpty(guid)) {
                uniPacket.setGUID(guid);
            }
            final byte[] data = uniPacket.encode();
            // 发送请求
            request(data);
            // 放入发送队列
            putCallback(uniPacket.getRequestId(), new ReqParams(reqType, observer, extra));
            DtLog.d(TAG, name + "-request reqType=" + reqType + ", reqID=" + uniPacket.getRequestId() + " ,size=" + data.length + " reqData=" + reqData.toString());
        }
        return true;
    }

    /**
     * 发送数据请求
     * @param data
     */
    public boolean request(byte[] data) {
        if (data == null || data.length < 1) {
            DtLog.d(TAG, name + "-" + "request param invalid");
            return false;
        }
        Message msg = Message.obtain();
        msg.what = NetworkConst.SOCKET_SEND;
        msg.obj = data;
        return mSendHandler.sendMessage(msg);
    }

    /**
     * 发送线程消息处理
     */
    private final Handler.Callback mSocketSendCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkConst.SOCKET_CONNECT:
                    DtLog.d(TAG, name + "-" + "send thread  handleMessage connect");
                    if (!isSocketConnected() && NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
                        initSocket(false);
                    }
                    break;
                case NetworkConst.SOCKET_TRY_CONNECT:
                    DtLog.d(TAG, name + "-" + "send thread handleMessage try connect");
                    if (!isSocketConnected() && NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
                        initSocket(true);
                    }
                    break;
                case NetworkConst.SOCKET_SEND:
                    if (!isSocketConnected()) {
                        if (NetUtil.isNetWorkConnected(SDKManager.getInstance().getContext())) {
                            DtLog.w(TAG, name + "-" + "send thread handleMessage send, no connection, go connect and resend data");
                            initSocket(false);
                            // 重发消息
                            Message retryMsg = Message.obtain();
                            retryMsg.what = msg.what;
                            retryMsg.obj = msg.obj;
                            mSendHandler.sendMessageDelayed(retryMsg, 1000);
                        } else {
                            DtLog.w(TAG, name + "-" + "send thread handleMessage send, no network");
                        }
                    } else {
                        byte[] sendMsg = (byte[]) msg.obj;
                        sendBytesToServer(sendMsg);
                    }
                    break;
                case NetworkConst.SOCKET_CLOSE:
                    DtLog.d(TAG, name + "-" + "send thread handleMessage close socket");
                    closeSocket();
                    if (mSendThread != null) {
                        mSendHandler.removeCallbacksAndMessages(null);
                    }
                    if (mReceiveThread != null) {
                        mReceiveHandler.removeCallbacksAndMessages(null);
                    }
                    break;
                case NetworkConst.THREAD_RELEASE:
                    DtLog.d(TAG, name + "-" + "send thread handleMessage release thread");
                    if (mSendHandler != null) {
                        mSendHandler.getLooper().quit();
                    }
                    break;
                default:
                    break;
            }

            return true;
        }
    };

    // 接收线程处理消息
    private final Handler.Callback mSocketReceiverCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkConst.SOCKET_RECEIVE:
                    readLoop();
                    break;
                case NetworkConst.THREAD_RELEASE:
                    if (mReceiveHandler != null) {
                        mReceiveHandler.getLooper().quit();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * 初始化socket连接
     * @param isTry 是否是尝试连接，当处于http模式时会尝试连接
     */
    private boolean isConnecting = false;
    private synchronized void initSocket(final boolean isTry) {
        if (mSocket != null && !mSocket.isConnected() && isConnecting) {
            DtLog.d(TAG, name + "-" + "is connecting, return");
            return;
        }

        try {
            isConnecting = true;
            DtLog.d(TAG, name + "-" + "start connect server");

            mSocket = new Socket();
            // 关闭socket时，立即释放socket绑定端口以便端口重用，默认为false
            mSocket.setReuseAddress(true);
            // 关闭传输缓存，默认为false
            mSocket.setTcpNoDelay(true);
            // 关闭socket时，底层socket不会直接关闭，会延迟一会，直到发送完所有数据
            // 等待10秒再关闭底层socket连接，0为立即关闭底层socket连接
            mSocket.setSoLinger(true, 0);
            // 设置性能参数，可设置任意整数，数值越大，相应的参数重要性越高（连接时间，延迟，带宽）
            mSocket.setPerformancePreferences(3, 2, 1);
            // 保持长连接
            mSocket.setKeepAlive(true);

            long startConnectTime = System.currentTimeMillis();
            mSocket.connect(IPManager.getInstance().getIp(isQuote), NetworkConst.SOCKET_CONNECT_TIMEOUT);
            DtLog.i(TAG, name + "-" + "connect complete, total time=" + (System.currentTimeMillis() - startConnectTime) + "ms");

            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();

            // 连接成功开始接收数据
            mIsStartReceive = true;
            mReceiveHandler.removeMessages(NetworkConst.SOCKET_RECEIVE);
            mReceiveHandler.sendEmptyMessageDelayed(NetworkConst.SOCKET_RECEIVE, 0);
        } catch (Throwable e) {
            DtLog.e(TAG, name + "-" + "socket connect failed, e=" + e.getMessage());
            IPManager.getInstance().failed(isQuote);
        } finally {
            isConnecting = false;
        }
    }

    /**
     * 关闭socket连接
     */
    private synchronized void closeSocket() {
        DtLog.d(TAG, name + "-" + "closeSocket");
        mIsStartReceive = false;
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                if (mInputStream != null) {
                    mInputStream.close();
                    mInputStream = null;
                }
                if (mOutputStream != null) {
                    mOutputStream.close();
                    mOutputStream = null;
                }
                mSocket.close();
                DtLog.d(TAG, name + "-" + "Socket closed...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mSocket = null;
    }

    /**
     * socket是否连接上
     * @return true 连接成功; false 连接失败
     */
    private synchronized boolean isSocketConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    /**
     * 接收所有从服务器传送过来的消息，收到之后通过消息发送给UI线程
     */
    private void readLoop() {
        DtLog.d(TAG, name + "-" + "Begin readLoop...");
        while (mIsStartReceive) {
            if (isSocketConnected()) {
                if (mInputStream != null) {
                    byte[] receivedBytes = receiveData();
                    if (receivedBytes != null) {
                        processRspMsg(receivedBytes);
                    } else {
                        cancelAllRequest();
                        DtLog.w(TAG, name + "-" + "receivedBytes == null");
                        if (mIsStartReceive) {
                            destroy();
                        }
                        break;
                    }
                } else {
                    DtLog.w(TAG, name + "-" + "mInputStream == null");
                }
            }
        }

        DtLog.d(TAG, name + "-" + "End readLoop.");
    }

    /**
     * 处理server返回数据，解析后回调到DataEngine
     * @param rspBytes 收到的全部字节
     */
    private void processRspMsg(byte[] rspBytes) {
        final MapProtoLite packet = new MapProtoLite();
        try {
            packet.setCharset("UTF-8");
            packet.decode(rspBytes, NetworkConst.PACKET_HEAD_LENGTH);
            handleResponseData(packet);
        } catch (Exception e) {
            e.printStackTrace();
            DtLog.e(TAG, name + "-" + "processRspMsg Exception e=" + e.getMessage());
            handleError(packet);
        }
        DtLog.d(TAG, name + "-response reqID=" + packet.getRequestId() + " ,size=" + rspBytes.length);
    }

    private void handleError(final MapProtoLite packet) {
        ReqParams reqParams = popCallback(packet.getRequestId());
        if (reqParams != null) {
            reqParams.callback.callback(false, new EntityObject(reqParams.reqType, null, reqParams.extra));
        }
    }

    /**
     * 发送数据
     */
    private void sendBytesToServer(byte[] sendMsg) {
        DtLog.d(TAG, name + "-" + "Begin sendBytesToServer");
        if (mOutputStream == null) {
            return;
        }

        try {
            // 发送长连接包
            long sendTime = System.currentTimeMillis();
            mOutputStream.write(sendMsg);
            mOutputStream.flush();
            DtLog.d(TAG, name + "-" + "sendBytesToServer finished, use time=" + (System.currentTimeMillis() - sendTime) + "ms");
            mSendTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            DtLog.w(TAG, name + "-" + "sendBytesToServer err close");
            destroy();
            IPManager.getInstance().failed(isQuote);
        }
    }

    /**
     * 获取返回数据
     * @return 返回数据
     */
    private byte[] receiveData() {
        int readLen; // 单次read收到的字节数
        int contentLen;

        byte[] head = new byte[NetworkConst.PACKET_HEAD_LENGTH];

        int writeBytes = 0;
        try {
            // 读取包头四个字节，代表包长度
            Arrays.fill(head, (byte) 0);
            readLen = mInputStream.read(head, 0, NetworkConst.PACKET_HEAD_LENGTH);
            if(readLen == -1){
                DtLog.w(TAG, name + "-" + "receiveData head readLen=" + readLen + ", thread=" + Thread.currentThread());
                return null;
            }
        } catch (Throwable e) {
            DtLog.w(TAG, name + "-" + "receiveData read head err when reading packageLen: " + e.getMessage());
            return null;
        }

        contentLen = byte2Int(head) - NetworkConst.PACKET_HEAD_LENGTH;
        DtLog.d(TAG, name + "-" + "receiveData packetLen=" + contentLen + ", readLen=" + readLen);

        // 将四字节的协议头和实际内容拼接起来，形成完整数据包
        byte[] finalBytes = null;
        //读取实际的协议数据内容
        try {
            finalBytes = new byte[NetworkConst.PACKET_HEAD_LENGTH + contentLen];
            System.arraycopy(head, 0, finalBytes, 0, NetworkConst.PACKET_HEAD_LENGTH);
            writeBytes += NetworkConst.PACKET_HEAD_LENGTH;

            final byte[] buffer = new byte[NetworkConst.READ_BUFFER_LEN];
            int readTotalLen = 0;
            while ((contentLen - readTotalLen > 0) && (readLen = mInputStream.read(buffer, 0, Math.min(contentLen - readTotalLen, buffer.length))) != -1) {
                System.arraycopy(buffer, 0, finalBytes, writeBytes, readLen);
                writeBytes += readLen;
                readTotalLen += readLen;
            }
            DtLog.d(TAG, name + "-" + "receiveData read content size=" + writeBytes);
        } catch (Throwable e) {
            DtLog.w(TAG, name + "-" + "receiveData read content err: " + e.getMessage());
            return null;
        } finally {
            if (readLen == -1) {
                DtLog.w(TAG, name + "-" + "receiveData read content err : read() == -1");
                return null;
            }
        }

        DtLog.d(TAG, name + "-" + "receiveData complete time=" + (System.currentTimeMillis() - mSendTime) + ", total byte=" + (contentLen+4));
        return finalBytes;
    }

    private int byte2Int(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (NetworkConst.PACKET_HEAD_LENGTH - 1 - i));
        }
        return intValue;
    }

    private void handleResponseData(MapProtoLite packet) {
        if (packet == null) {
            DtLog.w(TAG, name + "-" + "handleResponseData packet=null");
            return;
        }

        ReqParams reqParams = popCallback(packet.getRequestId());
        if (reqParams == null || reqParams.callback == null) {
            DtLog.w(TAG, name + "-" + "handleResponseData reqParams=null");
            return;
        }

        DtLog.d(TAG, name + "-" + "handleResponseData type=" + reqParams.reqType + ", id=" + packet.getRequestId());

        Object resObject = ProtoManager.getResponseObject(reqParams.reqType, packet);

        if (!AuthManager.getInstance().isBeaconApp()) {
            // 第三方app解包特殊处理
            OpenApiRsp openApiRsp = (OpenApiRsp)resObject;
            if (openApiRsp.eOpenApiRet == 0) {
                MapProtoLite openPacket = new MapProtoLite();
                openPacket.setCharset("UTF-8");
                openPacket.decode(openApiRsp.vBuffer, NetworkConst.PACKET_HEAD_LENGTH);
                resObject = ProtoManager.getResponseObject(reqParams.openApiType, openPacket);
            }
        }

        try {
            if (resObject != null) {
                reqParams.callback.callback(true, new EntityObject(reqParams.reqType, resObject, reqParams.extra));
            } else {
                reqParams.callback.callback(false, new EntityObject(reqParams.reqType, null, reqParams.extra));
            }
        } catch (Exception e) {
            e.printStackTrace();
            DtLog.w(TAG, name + "-" + "handleResponseData Exception msg=" + e.getMessage());
        }
    }
}
