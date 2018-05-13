package BEC;

public final class STMTBuy extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public float fBuy = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public float getFBuy()
    {
        return fBuy;
    }

    public void  setFBuy(float fBuy)
    {
        this.fBuy = fBuy;
    }

    public STMTBuy()
    {
    }

    public STMTBuy(String sDate, float fBuy)
    {
        this.sDate = sDate;
        this.fBuy = fBuy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeFloat(1, fBuy);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.fBuy = (float)istream.readFloat(1, false, this.fBuy);
    }

}

