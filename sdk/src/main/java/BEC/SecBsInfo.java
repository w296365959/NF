package BEC;

public final class SecBsInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sDate = "";

    public int iBs = 0;

    public float fClose = 0;

    public float fClosePre = 0;

    public float fBetaValue = 0;

    public float fBsValue = 0;

    public float fProbability = 0;

    public String sName = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getIBs()
    {
        return iBs;
    }

    public void  setIBs(int iBs)
    {
        this.iBs = iBs;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFClosePre()
    {
        return fClosePre;
    }

    public void  setFClosePre(float fClosePre)
    {
        this.fClosePre = fClosePre;
    }

    public float getFBetaValue()
    {
        return fBetaValue;
    }

    public void  setFBetaValue(float fBetaValue)
    {
        this.fBetaValue = fBetaValue;
    }

    public float getFBsValue()
    {
        return fBsValue;
    }

    public void  setFBsValue(float fBsValue)
    {
        this.fBsValue = fBsValue;
    }

    public float getFProbability()
    {
        return fProbability;
    }

    public void  setFProbability(float fProbability)
    {
        this.fProbability = fProbability;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public SecBsInfo()
    {
    }

    public SecBsInfo(String sDtSecCode, String sDate, int iBs, float fClose, float fClosePre, float fBetaValue, float fBsValue, float fProbability, String sName)
    {
        this.sDtSecCode = sDtSecCode;
        this.sDate = sDate;
        this.iBs = iBs;
        this.fClose = fClose;
        this.fClosePre = fClosePre;
        this.fBetaValue = fBetaValue;
        this.fBsValue = fBsValue;
        this.fProbability = fProbability;
        this.sName = sName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sDate) {
            ostream.writeString(1, sDate);
        }
        ostream.writeInt32(2, iBs);
        ostream.writeFloat(3, fClose);
        ostream.writeFloat(4, fClosePre);
        ostream.writeFloat(5, fBetaValue);
        ostream.writeFloat(6, fBsValue);
        ostream.writeFloat(7, fProbability);
        if (null != sName) {
            ostream.writeString(8, sName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sDate = (String)istream.readString(1, false, this.sDate);
        this.iBs = (int)istream.readInt32(2, false, this.iBs);
        this.fClose = (float)istream.readFloat(3, false, this.fClose);
        this.fClosePre = (float)istream.readFloat(4, false, this.fClosePre);
        this.fBetaValue = (float)istream.readFloat(5, false, this.fBetaValue);
        this.fBsValue = (float)istream.readFloat(6, false, this.fBsValue);
        this.fProbability = (float)istream.readFloat(7, false, this.fProbability);
        this.sName = (String)istream.readString(8, false, this.sName);
    }

}

