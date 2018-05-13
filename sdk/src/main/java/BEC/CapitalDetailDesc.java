package BEC;

public final class CapitalDetailDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fNow = 0;

    public float fChangeRate = 0;

    public float fChangeValue = 0;

    public float fZljlr = 0;

    public float fZljzb = 0;

    public float fShjlr = 0;

    public float fShjzb = 0;

    public float fCddjlr = 0;

    public float fCddjzb = 0;

    public float fDdjlr = 0;

    public float fDdjzb = 0;

    public float fZdjlr = 0;

    public float fZdjzb = 0;

    public float fXdjlr = 0;

    public float fXdjzb = 0;

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

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public float getFChangeRate()
    {
        return fChangeRate;
    }

    public void  setFChangeRate(float fChangeRate)
    {
        this.fChangeRate = fChangeRate;
    }

    public float getFChangeValue()
    {
        return fChangeValue;
    }

    public void  setFChangeValue(float fChangeValue)
    {
        this.fChangeValue = fChangeValue;
    }

    public float getFZljlr()
    {
        return fZljlr;
    }

    public void  setFZljlr(float fZljlr)
    {
        this.fZljlr = fZljlr;
    }

    public float getFZljzb()
    {
        return fZljzb;
    }

    public void  setFZljzb(float fZljzb)
    {
        this.fZljzb = fZljzb;
    }

    public float getFShjlr()
    {
        return fShjlr;
    }

    public void  setFShjlr(float fShjlr)
    {
        this.fShjlr = fShjlr;
    }

    public float getFShjzb()
    {
        return fShjzb;
    }

    public void  setFShjzb(float fShjzb)
    {
        this.fShjzb = fShjzb;
    }

    public float getFCddjlr()
    {
        return fCddjlr;
    }

    public void  setFCddjlr(float fCddjlr)
    {
        this.fCddjlr = fCddjlr;
    }

    public float getFCddjzb()
    {
        return fCddjzb;
    }

    public void  setFCddjzb(float fCddjzb)
    {
        this.fCddjzb = fCddjzb;
    }

    public float getFDdjlr()
    {
        return fDdjlr;
    }

    public void  setFDdjlr(float fDdjlr)
    {
        this.fDdjlr = fDdjlr;
    }

    public float getFDdjzb()
    {
        return fDdjzb;
    }

    public void  setFDdjzb(float fDdjzb)
    {
        this.fDdjzb = fDdjzb;
    }

    public float getFZdjlr()
    {
        return fZdjlr;
    }

    public void  setFZdjlr(float fZdjlr)
    {
        this.fZdjlr = fZdjlr;
    }

    public float getFZdjzb()
    {
        return fZdjzb;
    }

    public void  setFZdjzb(float fZdjzb)
    {
        this.fZdjzb = fZdjzb;
    }

    public float getFXdjlr()
    {
        return fXdjlr;
    }

    public void  setFXdjlr(float fXdjlr)
    {
        this.fXdjlr = fXdjlr;
    }

    public float getFXdjzb()
    {
        return fXdjzb;
    }

    public void  setFXdjzb(float fXdjzb)
    {
        this.fXdjzb = fXdjzb;
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

    public CapitalDetailDesc()
    {
    }

    public CapitalDetailDesc(String sDtSecCode, String sSecName, float fNow, float fChangeRate, float fChangeValue, float fZljlr, float fZljzb, float fShjlr, float fShjzb, float fCddjlr, float fCddjzb, float fDdjlr, float fDdjzb, float fZdjlr, float fZdjzb, float fXdjlr, float fXdjzb, int eSecStatus, BEC.SecAttr stSecAttr)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fNow = fNow;
        this.fChangeRate = fChangeRate;
        this.fChangeValue = fChangeValue;
        this.fZljlr = fZljlr;
        this.fZljzb = fZljzb;
        this.fShjlr = fShjlr;
        this.fShjzb = fShjzb;
        this.fCddjlr = fCddjlr;
        this.fCddjzb = fCddjzb;
        this.fDdjlr = fDdjlr;
        this.fDdjzb = fDdjzb;
        this.fZdjlr = fZdjlr;
        this.fZdjzb = fZdjzb;
        this.fXdjlr = fXdjlr;
        this.fXdjzb = fXdjzb;
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
        ostream.writeFloat(2, fNow);
        ostream.writeFloat(3, fChangeRate);
        ostream.writeFloat(4, fChangeValue);
        ostream.writeFloat(5, fZljlr);
        ostream.writeFloat(6, fZljzb);
        ostream.writeFloat(7, fShjlr);
        ostream.writeFloat(8, fShjzb);
        ostream.writeFloat(9, fCddjlr);
        ostream.writeFloat(10, fCddjzb);
        ostream.writeFloat(11, fDdjlr);
        ostream.writeFloat(12, fDdjzb);
        ostream.writeFloat(13, fZdjlr);
        ostream.writeFloat(14, fZdjzb);
        ostream.writeFloat(15, fXdjlr);
        ostream.writeFloat(16, fXdjzb);
        ostream.writeInt32(17, eSecStatus);
        if (null != stSecAttr) {
            ostream.writeMessage(18, stSecAttr);
        }
    }

    static BEC.SecAttr VAR_TYPE_4_STSECATTR = new BEC.SecAttr();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.fNow = (float)istream.readFloat(2, false, this.fNow);
        this.fChangeRate = (float)istream.readFloat(3, false, this.fChangeRate);
        this.fChangeValue = (float)istream.readFloat(4, false, this.fChangeValue);
        this.fZljlr = (float)istream.readFloat(5, false, this.fZljlr);
        this.fZljzb = (float)istream.readFloat(6, false, this.fZljzb);
        this.fShjlr = (float)istream.readFloat(7, false, this.fShjlr);
        this.fShjzb = (float)istream.readFloat(8, false, this.fShjzb);
        this.fCddjlr = (float)istream.readFloat(9, false, this.fCddjlr);
        this.fCddjzb = (float)istream.readFloat(10, false, this.fCddjzb);
        this.fDdjlr = (float)istream.readFloat(11, false, this.fDdjlr);
        this.fDdjzb = (float)istream.readFloat(12, false, this.fDdjzb);
        this.fZdjlr = (float)istream.readFloat(13, false, this.fZdjlr);
        this.fZdjzb = (float)istream.readFloat(14, false, this.fZdjzb);
        this.fXdjlr = (float)istream.readFloat(15, false, this.fXdjlr);
        this.fXdjzb = (float)istream.readFloat(16, false, this.fXdjzb);
        this.eSecStatus = (int)istream.readInt32(17, false, this.eSecStatus);
        this.stSecAttr = (BEC.SecAttr)istream.readMessage(18, false, VAR_TYPE_4_STSECATTR);
    }

}

