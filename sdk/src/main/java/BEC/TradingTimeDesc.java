package BEC;

public final class TradingTimeDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eMarketType = 0;

    public int iOpenTime = 0;

    public int iCloseTime = 0;

    public int eTradingTimeType = BEC.E_TRADING_TIME_TYPE.E_TTT_TRADING;

    public int eTradingDealType = BEC.E_TRADING_DEAL_TYPE.E_TDT_OPEN;

    public int getEMarketType()
    {
        return eMarketType;
    }

    public void  setEMarketType(int eMarketType)
    {
        this.eMarketType = eMarketType;
    }

    public int getIOpenTime()
    {
        return iOpenTime;
    }

    public void  setIOpenTime(int iOpenTime)
    {
        this.iOpenTime = iOpenTime;
    }

    public int getICloseTime()
    {
        return iCloseTime;
    }

    public void  setICloseTime(int iCloseTime)
    {
        this.iCloseTime = iCloseTime;
    }

    public int getETradingTimeType()
    {
        return eTradingTimeType;
    }

    public void  setETradingTimeType(int eTradingTimeType)
    {
        this.eTradingTimeType = eTradingTimeType;
    }

    public int getETradingDealType()
    {
        return eTradingDealType;
    }

    public void  setETradingDealType(int eTradingDealType)
    {
        this.eTradingDealType = eTradingDealType;
    }

    public TradingTimeDesc()
    {
    }

    public TradingTimeDesc(int eMarketType, int iOpenTime, int iCloseTime, int eTradingTimeType, int eTradingDealType)
    {
        this.eMarketType = eMarketType;
        this.iOpenTime = iOpenTime;
        this.iCloseTime = iCloseTime;
        this.eTradingTimeType = eTradingTimeType;
        this.eTradingDealType = eTradingDealType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eMarketType);
        ostream.writeInt32(1, iOpenTime);
        ostream.writeInt32(2, iCloseTime);
        ostream.writeInt32(3, eTradingTimeType);
        ostream.writeInt32(4, eTradingDealType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eMarketType = (int)istream.readInt32(0, false, this.eMarketType);
        this.iOpenTime = (int)istream.readInt32(1, false, this.iOpenTime);
        this.iCloseTime = (int)istream.readInt32(2, false, this.iCloseTime);
        this.eTradingTimeType = (int)istream.readInt32(3, false, this.eTradingTimeType);
        this.eTradingDealType = (int)istream.readInt32(4, false, this.eTradingDealType);
    }

}

