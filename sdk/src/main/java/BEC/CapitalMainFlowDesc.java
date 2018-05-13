package BEC;

public final class CapitalMainFlowDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public float fNow = 0;

    public float fChangeRate = 0;

    public float fChangeValue = 0;

    public float fZljlr = 0;

    public float fZljzb = 0;

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

    public CapitalMainFlowDesc()
    {
    }

    public CapitalMainFlowDesc(String sDtSecCode, String sSecName, float fNow, float fChangeRate, float fChangeValue, float fZljlr, float fZljzb, int eSecStatus, BEC.SecAttr stSecAttr)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.fNow = fNow;
        this.fChangeRate = fChangeRate;
        this.fChangeValue = fChangeValue;
        this.fZljlr = fZljlr;
        this.fZljzb = fZljzb;
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
        ostream.writeInt32(7, eSecStatus);
        if (null != stSecAttr) {
            ostream.writeMessage(8, stSecAttr);
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
        this.eSecStatus = (int)istream.readInt32(7, false, this.eSecStatus);
        this.stSecAttr = (BEC.SecAttr)istream.readMessage(8, false, VAR_TYPE_4_STSECATTR);
    }

}

