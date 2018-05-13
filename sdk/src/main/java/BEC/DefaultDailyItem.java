package BEC;

public final class DefaultDailyItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sCode = "";

    public String sName = "";

    public float fClose = 0;

    public float fUpPct = 0;

    public long lAmout = 0;

    public float fAmoutUpPct = 0;

    public float fFundIn = 0;

    public float fFundInUpPct = 0;

    public String getSCode()
    {
        return sCode;
    }

    public void  setSCode(String sCode)
    {
        this.sCode = sCode;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFUpPct()
    {
        return fUpPct;
    }

    public void  setFUpPct(float fUpPct)
    {
        this.fUpPct = fUpPct;
    }

    public long getLAmout()
    {
        return lAmout;
    }

    public void  setLAmout(long lAmout)
    {
        this.lAmout = lAmout;
    }

    public float getFAmoutUpPct()
    {
        return fAmoutUpPct;
    }

    public void  setFAmoutUpPct(float fAmoutUpPct)
    {
        this.fAmoutUpPct = fAmoutUpPct;
    }

    public float getFFundIn()
    {
        return fFundIn;
    }

    public void  setFFundIn(float fFundIn)
    {
        this.fFundIn = fFundIn;
    }

    public float getFFundInUpPct()
    {
        return fFundInUpPct;
    }

    public void  setFFundInUpPct(float fFundInUpPct)
    {
        this.fFundInUpPct = fFundInUpPct;
    }

    public DefaultDailyItem()
    {
    }

    public DefaultDailyItem(String sCode, String sName, float fClose, float fUpPct, long lAmout, float fAmoutUpPct, float fFundIn, float fFundInUpPct)
    {
        this.sCode = sCode;
        this.sName = sName;
        this.fClose = fClose;
        this.fUpPct = fUpPct;
        this.lAmout = lAmout;
        this.fAmoutUpPct = fAmoutUpPct;
        this.fFundIn = fFundIn;
        this.fFundInUpPct = fFundInUpPct;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sCode) {
            ostream.writeString(0, sCode);
        }
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        ostream.writeFloat(2, fClose);
        ostream.writeFloat(3, fUpPct);
        ostream.writeInt64(4, lAmout);
        ostream.writeFloat(5, fAmoutUpPct);
        ostream.writeFloat(6, fFundIn);
        ostream.writeFloat(7, fFundInUpPct);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sCode = (String)istream.readString(0, false, this.sCode);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.fClose = (float)istream.readFloat(2, false, this.fClose);
        this.fUpPct = (float)istream.readFloat(3, false, this.fUpPct);
        this.lAmout = (long)istream.readInt64(4, false, this.lAmout);
        this.fAmoutUpPct = (float)istream.readFloat(5, false, this.fAmoutUpPct);
        this.fFundIn = (float)istream.readFloat(6, false, this.fFundIn);
        this.fFundInUpPct = (float)istream.readFloat(7, false, this.fFundInUpPct);
    }

}

