package BEC;

public final class SecSimpleQuote extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fClose = 0;

    public float fNow = 0;

    public float fTotalmarketvalue = 0;

    public int iTpFlag = 0;

    public float fFhsl = 0;

    public float fSyl = 0;

    public int iUpnum = 0;

    public int iDownnum = 0;

    public float fHot = 0;

    public int iTime = 0;

    public int eSecStatus = BEC.E_SEC_STATUS.E_SS_NORMAL;

    public BEC.SecAttr stSecAttr = null;

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

    public float getFTotalmarketvalue()
    {
        return fTotalmarketvalue;
    }

    public void  setFTotalmarketvalue(float fTotalmarketvalue)
    {
        this.fTotalmarketvalue = fTotalmarketvalue;
    }

    public int getITpFlag()
    {
        return iTpFlag;
    }

    public void  setITpFlag(int iTpFlag)
    {
        this.iTpFlag = iTpFlag;
    }

    public float getFFhsl()
    {
        return fFhsl;
    }

    public void  setFFhsl(float fFhsl)
    {
        this.fFhsl = fFhsl;
    }

    public float getFSyl()
    {
        return fSyl;
    }

    public void  setFSyl(float fSyl)
    {
        this.fSyl = fSyl;
    }

    public int getIUpnum()
    {
        return iUpnum;
    }

    public void  setIUpnum(int iUpnum)
    {
        this.iUpnum = iUpnum;
    }

    public int getIDownnum()
    {
        return iDownnum;
    }

    public void  setIDownnum(int iDownnum)
    {
        this.iDownnum = iDownnum;
    }

    public float getFHot()
    {
        return fHot;
    }

    public void  setFHot(float fHot)
    {
        this.fHot = fHot;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public int getESecStatus()
    {
        return eSecStatus;
    }

    public void  setESecStatus(int eSecStatus)
    {
        this.eSecStatus = eSecStatus;
    }

    public BEC.SecAttr getStSecAttr()
    {
        return stSecAttr;
    }

    public void  setStSecAttr(BEC.SecAttr stSecAttr)
    {
        this.stSecAttr = stSecAttr;
    }

    public SecSimpleQuote()
    {
    }

    public SecSimpleQuote(String sDtSecCode, String sSecName, float fClose, float fNow, float fTotalmarketvalue, int iTpFlag, float fFhsl, float fSyl, int iUpnum, int iDownnum, float fHot, int iTime, int eSecStatus, BEC.SecAttr stSecAttr)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fClose = fClose;
        this.fNow = fNow;
        this.fTotalmarketvalue = fTotalmarketvalue;
        this.iTpFlag = iTpFlag;
        this.fFhsl = fFhsl;
        this.fSyl = fSyl;
        this.iUpnum = iUpnum;
        this.iDownnum = iDownnum;
        this.fHot = fHot;
        this.iTime = iTime;
        this.eSecStatus = eSecStatus;
        this.stSecAttr = stSecAttr;
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
        ostream.writeFloat(4, fTotalmarketvalue);
        ostream.writeInt32(5, iTpFlag);
        ostream.writeFloat(6, fFhsl);
        ostream.writeFloat(7, fSyl);
        ostream.writeInt32(8, iUpnum);
        ostream.writeInt32(9, iDownnum);
        ostream.writeFloat(10, fHot);
        ostream.writeInt32(11, iTime);
        ostream.writeInt32(12, eSecStatus);
        if (null != stSecAttr) {
            ostream.writeMessage(13, stSecAttr);
        }
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
        this.fTotalmarketvalue = (float)istream.readFloat(4, false, this.fTotalmarketvalue);
        this.iTpFlag = (int)istream.readInt32(5, false, this.iTpFlag);
        this.fFhsl = (float)istream.readFloat(6, false, this.fFhsl);
        this.fSyl = (float)istream.readFloat(7, false, this.fSyl);
        this.iUpnum = (int)istream.readInt32(8, false, this.iUpnum);
        this.iDownnum = (int)istream.readInt32(9, false, this.iDownnum);
        this.fHot = (float)istream.readFloat(10, false, this.fHot);
        this.iTime = (int)istream.readInt32(11, false, this.iTime);
        this.eSecStatus = (int)istream.readInt32(12, false, this.eSecStatus);
        this.stSecAttr = (BEC.SecAttr)istream.readMessage(13, false, VAR_TYPE_4_STSECATTR);
    }

}

