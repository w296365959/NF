package BEC;

public final class QRTrendReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public byte [] vGuid = null;

    public int eTrendReqType = E_TREND_REQ_TYPE.E_TRT_NORMAL;

    public int iStartxh = 0;

    public int iWantnum = 0;

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

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public QRTrendReq()
    {
    }

    public QRTrendReq(byte [] vGuid, int eTrendReqType, int iStartxh, int iWantnum)
    {
        this.vGuid = vGuid;
        this.eTrendReqType = eTrendReqType;
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vGuid) {
            ostream.writeBytes(0, vGuid);
        }
        ostream.writeInt32(1, eTrendReqType);
        ostream.writeInt32(2, iStartxh);
        ostream.writeInt32(3, iWantnum);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vGuid = (byte [])istream.readBytes(0, false, this.vGuid);
        this.eTrendReqType = (int)istream.readInt32(1, false, this.eTrendReqType);
        this.iStartxh = (int)istream.readInt32(2, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(3, false, this.iWantnum);
    }

}

