package BEC;

public final class PlateBuyValue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sPlate = "";

    public float fBuyValue = 0;

    public String sBuyValue = "";

    public String getSPlate()
    {
        return sPlate;
    }

    public void  setSPlate(String sPlate)
    {
        this.sPlate = sPlate;
    }

    public float getFBuyValue()
    {
        return fBuyValue;
    }

    public void  setFBuyValue(float fBuyValue)
    {
        this.fBuyValue = fBuyValue;
    }

    public String getSBuyValue()
    {
        return sBuyValue;
    }

    public void  setSBuyValue(String sBuyValue)
    {
        this.sBuyValue = sBuyValue;
    }

    public PlateBuyValue()
    {
    }

    public PlateBuyValue(String sPlate, float fBuyValue, String sBuyValue)
    {
        this.sPlate = sPlate;
        this.fBuyValue = fBuyValue;
        this.sBuyValue = sBuyValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sPlate) {
            ostream.writeString(0, sPlate);
        }
        ostream.writeFloat(1, fBuyValue);
        if (null != sBuyValue) {
            ostream.writeString(2, sBuyValue);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sPlate = (String)istream.readString(0, false, this.sPlate);
        this.fBuyValue = (float)istream.readFloat(1, false, this.fBuyValue);
        this.sBuyValue = (String)istream.readString(2, false, this.sBuyValue);
    }

}

