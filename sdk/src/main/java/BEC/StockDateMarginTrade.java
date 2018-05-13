package BEC;

public final class StockDateMarginTrade extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sOpDate = "";

    public float fMarginBalance = 0;

    public float fBuyValue = 0;

    public String sMarginBalance = "";

    public String sBuyValue = "";

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

    public String getSMarginBalance()
    {
        return sMarginBalance;
    }

    public void  setSMarginBalance(String sMarginBalance)
    {
        this.sMarginBalance = sMarginBalance;
    }

    public String getSBuyValue()
    {
        return sBuyValue;
    }

    public void  setSBuyValue(String sBuyValue)
    {
        this.sBuyValue = sBuyValue;
    }

    public StockDateMarginTrade()
    {
    }

    public StockDateMarginTrade(String sOpDate, float fMarginBalance, float fBuyValue, String sMarginBalance, String sBuyValue)
    {
        this.sOpDate = sOpDate;
        this.fMarginBalance = fMarginBalance;
        this.fBuyValue = fBuyValue;
        this.sMarginBalance = sMarginBalance;
        this.sBuyValue = sBuyValue;
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
        if (null != sMarginBalance) {
            ostream.writeString(3, sMarginBalance);
        }
        if (null != sBuyValue) {
            ostream.writeString(4, sBuyValue);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sOpDate = (String)istream.readString(0, false, this.sOpDate);
        this.fMarginBalance = (float)istream.readFloat(1, false, this.fMarginBalance);
        this.fBuyValue = (float)istream.readFloat(2, false, this.fBuyValue);
        this.sMarginBalance = (String)istream.readString(3, false, this.sMarginBalance);
        this.sBuyValue = (String)istream.readString(4, false, this.sBuyValue);
    }

}

