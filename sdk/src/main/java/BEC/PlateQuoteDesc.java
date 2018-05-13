package BEC;

public final class PlateQuoteDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fClose = 0;

    public float fNow = 0;

    public long lVolume = 0;

    public float fAmout = 0;

    public float fFhsl = 0;

    public String sHeadName = "";

    public float fHeadNow = 0;

    public float fHeadClose = 0;

    public float fYclose = 0;

    public int iTpFlag = 0;

    public BEC.SecAttr stSecAttr = null;

    public int eSecStatus = BEC.E_SEC_STATUS.E_SS_NORMAL;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public long getLVolume()
    {
        return lVolume;
    }

    public void  setLVolume(long lVolume)
    {
        this.lVolume = lVolume;
    }

    public float getFAmout()
    {
        return fAmout;
    }

    public void  setFAmout(float fAmout)
    {
        this.fAmout = fAmout;
    }

    public float getFFhsl()
    {
        return fFhsl;
    }

    public void  setFFhsl(float fFhsl)
    {
        this.fFhsl = fFhsl;
    }

    public String getSHeadName()
    {
        return sHeadName;
    }

    public void  setSHeadName(String sHeadName)
    {
        this.sHeadName = sHeadName;
    }

    public float getFHeadNow()
    {
        return fHeadNow;
    }

    public void  setFHeadNow(float fHeadNow)
    {
        this.fHeadNow = fHeadNow;
    }

    public float getFHeadClose()
    {
        return fHeadClose;
    }

    public void  setFHeadClose(float fHeadClose)
    {
        this.fHeadClose = fHeadClose;
    }

    public float getFYclose()
    {
        return fYclose;
    }

    public void  setFYclose(float fYclose)
    {
        this.fYclose = fYclose;
    }

    public int getITpFlag()
    {
        return iTpFlag;
    }

    public void  setITpFlag(int iTpFlag)
    {
        this.iTpFlag = iTpFlag;
    }

    public BEC.SecAttr getStSecAttr()
    {
        return stSecAttr;
    }

    public void  setStSecAttr(BEC.SecAttr stSecAttr)
    {
        this.stSecAttr = stSecAttr;
    }

    public int getESecStatus()
    {
        return eSecStatus;
    }

    public void  setESecStatus(int eSecStatus)
    {
        this.eSecStatus = eSecStatus;
    }

    public PlateQuoteDesc()
    {
    }

    public PlateQuoteDesc(String sDtSecCode, String sSecName, float fClose, float fNow, long lVolume, float fAmout, float fFhsl, String sHeadName, float fHeadNow, float fHeadClose, float fYclose, int iTpFlag, BEC.SecAttr stSecAttr, int eSecStatus)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fClose = fClose;
        this.fNow = fNow;
        this.lVolume = lVolume;
        this.fAmout = fAmout;
        this.fFhsl = fFhsl;
        this.sHeadName = sHeadName;
        this.fHeadNow = fHeadNow;
        this.fHeadClose = fHeadClose;
        this.fYclose = fYclose;
        this.iTpFlag = iTpFlag;
        this.stSecAttr = stSecAttr;
        this.eSecStatus = eSecStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
        ostream.writeFloat(2, fClose);
        ostream.writeFloat(3, fNow);
        ostream.writeInt64(4, lVolume);
        ostream.writeFloat(5, fAmout);
        ostream.writeFloat(6, fFhsl);
        if (null != sHeadName) {
            ostream.writeString(7, sHeadName);
        }
        ostream.writeFloat(8, fHeadNow);
        ostream.writeFloat(9, fHeadClose);
        ostream.writeFloat(10, fYclose);
        ostream.writeInt32(11, iTpFlag);
        if (null != stSecAttr) {
            ostream.writeMessage(12, stSecAttr);
        }
        ostream.writeInt32(13, eSecStatus);
    }

    static BEC.SecAttr VAR_TYPE_4_STSECATTR = new BEC.SecAttr();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.fClose = (float)istream.readFloat(2, false, this.fClose);
        this.fNow = (float)istream.readFloat(3, false, this.fNow);
        this.lVolume = (long)istream.readInt64(4, false, this.lVolume);
        this.fAmout = (float)istream.readFloat(5, false, this.fAmout);
        this.fFhsl = (float)istream.readFloat(6, false, this.fFhsl);
        this.sHeadName = (String)istream.readString(7, false, this.sHeadName);
        this.fHeadNow = (float)istream.readFloat(8, false, this.fHeadNow);
        this.fHeadClose = (float)istream.readFloat(9, false, this.fHeadClose);
        this.fYclose = (float)istream.readFloat(10, false, this.fYclose);
        this.iTpFlag = (int)istream.readInt32(11, false, this.iTpFlag);
        this.stSecAttr = (BEC.SecAttr)istream.readMessage(12, false, VAR_TYPE_4_STSECATTR);
        this.eSecStatus = (int)istream.readInt32(13, false, this.eSecStatus);
    }

}

