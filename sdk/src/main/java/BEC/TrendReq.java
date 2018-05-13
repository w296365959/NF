package BEC;

public final class TrendReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public byte [] vGuid = null;

    public int eTrendReqType = BEC.E_TREND_REQ_TYPE.E_TRT_NORMAL;

    public int iStartxh = 0;

    public int iMinute = 0;

    public int iWantnum = 0;

    public int iReqDataMinute = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public int getETrendReqType()
    {
        return eTrendReqType;
    }

    public void  setETrendReqType(int eTrendReqType)
    {
        this.eTrendReqType = eTrendReqType;
    }

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getIMinute()
    {
        return iMinute;
    }

    public void  setIMinute(int iMinute)
    {
        this.iMinute = iMinute;
    }

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public int getIReqDataMinute()
    {
        return iReqDataMinute;
    }

    public void  setIReqDataMinute(int iReqDataMinute)
    {
        this.iReqDataMinute = iReqDataMinute;
    }

    public TrendReq()
    {
    }

    public TrendReq(String sDtSecCode, byte [] vGuid, int eTrendReqType, int iStartxh, int iMinute, int iWantnum, int iReqDataMinute)
    {
        this.sDtSecCode = sDtSecCode;
        this.vGuid = vGuid;
        this.eTrendReqType = eTrendReqType;
        this.iStartxh = iStartxh;
        this.iMinute = iMinute;
        this.iWantnum = iWantnum;
        this.iReqDataMinute = iReqDataMinute;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != vGuid) {
            ostream.writeBytes(1, vGuid);
        }
        ostream.writeInt32(2, eTrendReqType);
        ostream.writeInt32(3, iStartxh);
        ostream.writeInt32(4, iMinute);
        ostream.writeInt32(5, iWantnum);
        ostream.writeInt32(6, iReqDataMinute);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.vGuid = (byte [])istream.readBytes(1, false, this.vGuid);
        this.eTrendReqType = (int)istream.readInt32(2, false, this.eTrendReqType);
        this.iStartxh = (int)istream.readInt32(3, false, this.iStartxh);
        this.iMinute = (int)istream.readInt32(4, false, this.iMinute);
        this.iWantnum = (int)istream.readInt32(5, false, this.iWantnum);
        this.iReqDataMinute = (int)istream.readInt32(6, false, this.iReqDataMinute);
    }

}

