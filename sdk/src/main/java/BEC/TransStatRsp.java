package BEC;

public final class TransStatRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.TransStat stTransStatBuy = null;

    public BEC.TransStat stTransStatSell = null;

    public long lTotalBuyAmt = 0;

    public long lTotalSellAmt = 0;

    public float fPriceAvg = 0;

    public long lTotalBuyVol = 0;

    public long lTotalSellVol = 0;

    public BEC.TransStat getStTransStatBuy()
    {
        return stTransStatBuy;
    }

    public void  setStTransStatBuy(BEC.TransStat stTransStatBuy)
    {
        this.stTransStatBuy = stTransStatBuy;
    }

    public BEC.TransStat getStTransStatSell()
    {
        return stTransStatSell;
    }

    public void  setStTransStatSell(BEC.TransStat stTransStatSell)
    {
        this.stTransStatSell = stTransStatSell;
    }

    public long getLTotalBuyAmt()
    {
        return lTotalBuyAmt;
    }

    public void  setLTotalBuyAmt(long lTotalBuyAmt)
    {
        this.lTotalBuyAmt = lTotalBuyAmt;
    }

    public long getLTotalSellAmt()
    {
        return lTotalSellAmt;
    }

    public void  setLTotalSellAmt(long lTotalSellAmt)
    {
        this.lTotalSellAmt = lTotalSellAmt;
    }

    public float getFPriceAvg()
    {
        return fPriceAvg;
    }

    public void  setFPriceAvg(float fPriceAvg)
    {
        this.fPriceAvg = fPriceAvg;
    }

    public long getLTotalBuyVol()
    {
        return lTotalBuyVol;
    }

    public void  setLTotalBuyVol(long lTotalBuyVol)
    {
        this.lTotalBuyVol = lTotalBuyVol;
    }

    public long getLTotalSellVol()
    {
        return lTotalSellVol;
    }

    public void  setLTotalSellVol(long lTotalSellVol)
    {
        this.lTotalSellVol = lTotalSellVol;
    }

    public TransStatRsp()
    {
    }

    public TransStatRsp(BEC.TransStat stTransStatBuy, BEC.TransStat stTransStatSell, long lTotalBuyAmt, long lTotalSellAmt, float fPriceAvg, long lTotalBuyVol, long lTotalSellVol)
    {
        this.stTransStatBuy = stTransStatBuy;
        this.stTransStatSell = stTransStatSell;
        this.lTotalBuyAmt = lTotalBuyAmt;
        this.lTotalSellAmt = lTotalSellAmt;
        this.fPriceAvg = fPriceAvg;
        this.lTotalBuyVol = lTotalBuyVol;
        this.lTotalSellVol = lTotalSellVol;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stTransStatBuy) {
            ostream.writeMessage(0, stTransStatBuy);
        }
        if (null != stTransStatSell) {
            ostream.writeMessage(1, stTransStatSell);
        }
        ostream.writeInt64(2, lTotalBuyAmt);
        ostream.writeInt64(3, lTotalSellAmt);
        ostream.writeFloat(4, fPriceAvg);
        ostream.writeInt64(5, lTotalBuyVol);
        ostream.writeInt64(6, lTotalSellVol);
    }

    static BEC.TransStat VAR_TYPE_4_STTRANSSTATBUY = new BEC.TransStat();

    static BEC.TransStat VAR_TYPE_4_STTRANSSTATSELL = new BEC.TransStat();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stTransStatBuy = (BEC.TransStat)istream.readMessage(0, false, VAR_TYPE_4_STTRANSSTATBUY);
        this.stTransStatSell = (BEC.TransStat)istream.readMessage(1, false, VAR_TYPE_4_STTRANSSTATSELL);
        this.lTotalBuyAmt = (long)istream.readInt64(2, false, this.lTotalBuyAmt);
        this.lTotalSellAmt = (long)istream.readInt64(3, false, this.lTotalSellAmt);
        this.fPriceAvg = (float)istream.readFloat(4, false, this.fPriceAvg);
        this.lTotalBuyVol = (long)istream.readInt64(5, false, this.lTotalBuyVol);
        this.lTotalSellVol = (long)istream.readInt64(6, false, this.lTotalSellVol);
    }

}

