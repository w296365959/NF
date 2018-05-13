package BEC;

public final class DtMarketAlert extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public int iMakertAlert = 0;

    public float fAIndex = 0;

    public float fMarginTrade = 0;

    public float fAHExtend = 0;

    public float fNewInvestor = 0;

    public float fTradeAInvestorPer = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getIMakertAlert()
    {
        return iMakertAlert;
    }

    public void  setIMakertAlert(int iMakertAlert)
    {
        this.iMakertAlert = iMakertAlert;
    }

    public float getFAIndex()
    {
        return fAIndex;
    }

    public void  setFAIndex(float fAIndex)
    {
        this.fAIndex = fAIndex;
    }

    public float getFMarginTrade()
    {
        return fMarginTrade;
    }

    public void  setFMarginTrade(float fMarginTrade)
    {
        this.fMarginTrade = fMarginTrade;
    }

    public float getFAHExtend()
    {
        return fAHExtend;
    }

    public void  setFAHExtend(float fAHExtend)
    {
        this.fAHExtend = fAHExtend;
    }

    public float getFNewInvestor()
    {
        return fNewInvestor;
    }

    public void  setFNewInvestor(float fNewInvestor)
    {
        this.fNewInvestor = fNewInvestor;
    }

    public float getFTradeAInvestorPer()
    {
        return fTradeAInvestorPer;
    }

    public void  setFTradeAInvestorPer(float fTradeAInvestorPer)
    {
        this.fTradeAInvestorPer = fTradeAInvestorPer;
    }

    public DtMarketAlert()
    {
    }

    public DtMarketAlert(String sDate, int iMakertAlert, float fAIndex, float fMarginTrade, float fAHExtend, float fNewInvestor, float fTradeAInvestorPer)
    {
        this.sDate = sDate;
        this.iMakertAlert = iMakertAlert;
        this.fAIndex = fAIndex;
        this.fMarginTrade = fMarginTrade;
        this.fAHExtend = fAHExtend;
        this.fNewInvestor = fNewInvestor;
        this.fTradeAInvestorPer = fTradeAInvestorPer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeInt32(1, iMakertAlert);
        ostream.writeFloat(2, fAIndex);
        ostream.writeFloat(3, fMarginTrade);
        ostream.writeFloat(4, fAHExtend);
        ostream.writeFloat(5, fNewInvestor);
        ostream.writeFloat(6, fTradeAInvestorPer);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.iMakertAlert = (int)istream.readInt32(1, false, this.iMakertAlert);
        this.fAIndex = (float)istream.readFloat(2, false, this.fAIndex);
        this.fMarginTrade = (float)istream.readFloat(3, false, this.fMarginTrade);
        this.fAHExtend = (float)istream.readFloat(4, false, this.fAHExtend);
        this.fNewInvestor = (float)istream.readFloat(5, false, this.fNewInvestor);
        this.fTradeAInvestorPer = (float)istream.readFloat(6, false, this.fTradeAInvestorPer);
    }

}

