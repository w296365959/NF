package BEC;

public final class ShortLineStrategy extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iTime = 0;

    public int ePanKouType = 0;

    public String sInfo = "";

    public float fNow = 0;

    public float fClose = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public int getEPanKouType()
    {
        return ePanKouType;
    }

    public void  setEPanKouType(int ePanKouType)
    {
        this.ePanKouType = ePanKouType;
    }

    public String getSInfo()
    {
        return sInfo;
    }

    public void  setSInfo(String sInfo)
    {
        this.sInfo = sInfo;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public ShortLineStrategy()
    {
    }

    public ShortLineStrategy(String sDtSecCode, int iTime, int ePanKouType, String sInfo, float fNow, float fClose)
    {
        this.sDtSecCode = sDtSecCode;
        this.iTime = iTime;
        this.ePanKouType = ePanKouType;
        this.sInfo = sInfo;
        this.fNow = fNow;
        this.fClose = fClose;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iTime);
        ostream.writeInt32(2, ePanKouType);
        if (null != sInfo) {
            ostream.writeString(3, sInfo);
        }
        ostream.writeFloat(4, fNow);
        ostream.writeFloat(5, fClose);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iTime = (int)istream.readInt32(1, false, this.iTime);
        this.ePanKouType = (int)istream.readInt32(2, false, this.ePanKouType);
        this.sInfo = (String)istream.readString(3, false, this.sInfo);
        this.fNow = (float)istream.readFloat(4, false, this.fNow);
        this.fClose = (float)istream.readFloat(5, false, this.fClose);
    }

}

