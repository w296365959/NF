package BEC;

public final class MarginTradeInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public float fBuyBalance = 0;

    public float fSellBalance = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public float getFBuyBalance()
    {
        return fBuyBalance;
    }

    public void  setFBuyBalance(float fBuyBalance)
    {
        this.fBuyBalance = fBuyBalance;
    }

    public float getFSellBalance()
    {
        return fSellBalance;
    }

    public void  setFSellBalance(float fSellBalance)
    {
        this.fSellBalance = fSellBalance;
    }

    public MarginTradeInfo()
    {
    }

    public MarginTradeInfo(String sDate, float fBuyBalance, float fSellBalance)
    {
        this.sDate = sDate;
        this.fBuyBalance = fBuyBalance;
        this.fSellBalance = fSellBalance;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeFloat(1, fBuyBalance);
        ostream.writeFloat(2, fSellBalance);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.fBuyBalance = (float)istream.readFloat(1, false, this.fBuyBalance);
        this.fSellBalance = (float)istream.readFloat(2, false, this.fSellBalance);
    }

}

