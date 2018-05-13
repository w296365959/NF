package BEC;

public final class MarginTradeMkt extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sOpDate = "";

    public float fMarginBalance = 0;

    public float fBuyValue = 0;

    public String getSOpDate()
    {
        return sOpDate;
    }

    public void  setSOpDate(String sOpDate)
    {
        this.sOpDate = sOpDate;
    }

    public float getFMarginBalance()
    {
        return fMarginBalance;
    }

    public void  setFMarginBalance(float fMarginBalance)
    {
        this.fMarginBalance = fMarginBalance;
    }

    public float getFBuyValue()
    {
        return fBuyValue;
    }

    public void  setFBuyValue(float fBuyValue)
    {
        this.fBuyValue = fBuyValue;
    }

    public MarginTradeMkt()
    {
    }

    public MarginTradeMkt(String sOpDate, float fMarginBalance, float fBuyValue)
    {
        this.sOpDate = sOpDate;
        this.fMarginBalance = fMarginBalance;
        this.fBuyValue = fBuyValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sOpDate) {
            ostream.writeString(0, sOpDate);
        }
        ostream.writeFloat(1, fMarginBalance);
        ostream.writeFloat(2, fBuyValue);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sOpDate = (String)istream.readString(0, false, this.sOpDate);
        this.fMarginBalance = (float)istream.readFloat(1, false, this.fMarginBalance);
        this.fBuyValue = (float)istream.readFloat(2, false, this.fBuyValue);
    }

}

