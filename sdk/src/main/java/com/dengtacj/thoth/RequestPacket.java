package com.dengtacj.thoth;

public final class RequestPacket extends Message implements Cloneable
{
    public short iVersion = 0;

    public byte cPacketType = 0;

    public int iMessageType = 0;

    public int iRequestId = 0;

    public String sServantName = "";

    public String sFuncName = "";

    public byte [] sBuffer = null;

    public int iTimeout = 0;

    public java.util.Map<String, String> context = null;

    public java.util.Map<String, String> status = null;

    public byte bCompress = 0;
    
    public short getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(short iVersion)
    {
        this.iVersion = iVersion;
    }

    public byte getCPacketType()
    {
        return cPacketType;
    }

    public void  setCPacketType(byte cPacketType)
    {
        this.cPacketType = cPacketType;
    }

    public int getIMessageType()
    {
        return iMessageType;
    }

    public void  setIMessageType(int iMessageType)
    {
        this.iMessageType = iMessageType;
    }

    public int getIRequestId()
    {
        return iRequestId;
    }

    public void  setIRequestId(int iRequestId)
    {
        this.iRequestId = iRequestId;
    }

    public String getSServantName()
    {
        return sServantName;
    }

    public void  setSServantName(String sServantName)
    {
        this.sServantName = sServantName;
    }

    public String getSFuncName()
    {
        return sFuncName;
    }

    public void  setSFuncName(String sFuncName)
    {
        this.sFuncName = sFuncName;
    }

    public byte [] getSBuffer()
    {
        return sBuffer;
    }

    public void  setSBuffer(byte [] sBuffer)
    {
        this.sBuffer = sBuffer;
    }

    public int getITimeout()
    {
        return iTimeout;
    }

    public void  setITimeout(int iTimeout)
    {
        this.iTimeout = iTimeout;
    }

    public java.util.Map<String, String> getContext()
    {
        return context;
    }

    public void  setContext(java.util.Map<String, String> context)
    {
        this.context = context;
    }

    public java.util.Map<String, String> getStatus()
    {
        return status;
    }

    public void  setStatus(java.util.Map<String, String> status)
    {
        this.status = status;
    }

    public byte getBCompress()
    {
        return bCompress;
    }
    
    public void  setBCompress(byte bCompress)
    {
        this.bCompress = bCompress;
    }
    
    public RequestPacket()
    {
    }

    public RequestPacket(short iVersion, byte cPacketType, int iMessageType, int iRequestId, String sServantName, String sFuncName, byte [] sBuffer, int iTimeout, java.util.Map<String, String> context, java.util.Map<String, String> status, byte bCompress)
    {
        this.iVersion = iVersion;
        this.cPacketType = cPacketType;
        this.iMessageType = iMessageType;
        this.iRequestId = iRequestId;
        this.sServantName = sServantName;
        this.sFuncName = sFuncName;
        this.sBuffer = sBuffer;
        this.iTimeout = iTimeout;
        this.context = context;
        this.status = status;
        this.bCompress = bCompress;
    }

    public void write(BaseEncodeStream eos)
    {
        BaseEncodeStream ostream = new BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt16(1, iVersion);
        ostream.writeInt8(2, cPacketType);
        ostream.writeInt32(3, iMessageType);
        ostream.writeInt32(4, iRequestId);
        ostream.writeString(5, sServantName);
        ostream.writeString(6, sFuncName);
        ostream.writeBytes(7, sBuffer);
        ostream.writeInt32(8, iTimeout);
        ostream.writeMap(9, context);
        ostream.writeMap(10, status);
        ostream.writeInt8(11, bCompress);
    }

    static java.util.Map<String, String> VAR_TYPE_4_CONTEXT = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_CONTEXT.put("", "");
    }

    static java.util.Map<String, String> VAR_TYPE_4_STATUS = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_STATUS.put("", "");
    }

    public void read(BaseDecodeStream dos)
    {
        BaseDecodeStream istream = new BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iVersion = (short)istream.readInt16(1, true, this.iVersion);
        this.cPacketType = (byte)istream.readInt8(2, true, this.cPacketType);
        this.iMessageType = (int)istream.readInt32(3, true, this.iMessageType);
        this.iRequestId = (int)istream.readInt32(4, true, this.iRequestId);
        this.sServantName = (String)istream.readString(5, true, this.sServantName);
        this.sFuncName = (String)istream.readString(6, true, this.sFuncName);
        this.sBuffer = (byte [])istream.readBytes(7, true, this.sBuffer);
        this.iTimeout = (int)istream.readInt32(8, true, this.iTimeout);
        this.context = (java.util.Map<String, String>)istream.readMap(9, true, VAR_TYPE_4_CONTEXT);
        this.status = (java.util.Map<String, String>)istream.readMap(10, true, VAR_TYPE_4_STATUS);
        this.bCompress = (byte)istream.readInt8(11, false, this.bCompress);
    }
}
