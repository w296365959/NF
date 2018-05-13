package BEC;

public final class MarginTradeDt extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fBuyValue = 0;

    public float fBuyPercent = 0;

    public String sBuyValue = "";

    public String sBuyPercent = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFBuyValue()
    {
        return fBuyValue;
    }

    public void  setFBuyValue(float fBuyValue)
    {
        this.fBuyValue = fBuyValue;
    }

    public float getFBuyPercent()
    {
        return fBuyPercent;
    }

    public void  setFBuyPercent(float fBuyPercent)
    {
        this.fBuyPercent = fBuyPercent;
    }

    public String getSBuyValue()
    {
        return sBuyValue;
    }

    public void  setSBuyValue(String sBuyValue)
    {
        this.sBuyValue = sBuyValue;
    }

    public String getSBuyPercent()
    {
        return sBuyPercent;
    }

    public void  setSBuyPercent(String sBuyPercent)
    {
        this.sBuyPercent = sBuyPercent;
    }

    public MarginTradeDt()
    {
    }

    public MarginTradeDt(String sDtSecCode, float fBuyValue, float fBuyPercent, String sBuyValue, String sBuyPercent)
    {
        this.sDtSecCode = sDtSecCode;
        this.fBuyValue = fBuyValue;
        this.fBuyPercent = fBuyPercent;
        this.sBuyValue = sBuyValue;
        this.sBuyPercent = sBuyPercent;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fBuyValue);
        ostream.writeFloat(2, fBuyPercent);
        if (null != sBuyValue) {
            ostream.writeString(3, sBuyValue);
        }
        if (null != sBuyPercent) {
            ostream.writeString(4, sBuyPercent);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fBuyValue = (float)istream.readFloat(1, false, this.fBuyValue);
        this.fBuyPercent = (float)istream.readFloat(2, false, this.fBuyPercent);
        this.sBuyValue = (String)istream.readString(3, false, this.sBuyValue);
        this.sBuyPercent = (String)istream.readString(4, false, this.sBuyPercent);
    }

}

