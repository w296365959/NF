package BEC;

public final class MarketAlertRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iDtAIndex = 0;

    public int iMarketMoney = 0;

    public int iMarketHot = 0;

    public BEC.MaxDateValue stMaxMarketAlert = null;

    public BEC.MaxDateValue stMaxDtAIndex = null;

    public BEC.MaxDateValue stMaxMarginTrade = null;

    public BEC.MaxDateValue stMaxAHExtend = null;

    public BEC.MaxDateValue stMaxNewInvestor = null;

    public BEC.MaxDateValue stMaxTradeAInvestorPer = null;

    public java.util.ArrayList<DtMarketAlert> vDtMarketAlert = null;

    public int getIDtAIndex()
    {
        return iDtAIndex;
    }

    public void  setIDtAIndex(int iDtAIndex)
    {
        this.iDtAIndex = iDtAIndex;
    }

    public int getIMarketMoney()
    {
        return iMarketMoney;
    }

    public void  setIMarketMoney(int iMarketMoney)
    {
        this.iMarketMoney = iMarketMoney;
    }

    public int getIMarketHot()
    {
        return iMarketHot;
    }

    public void  setIMarketHot(int iMarketHot)
    {
        this.iMarketHot = iMarketHot;
    }

    public BEC.MaxDateValue getStMaxMarketAlert()
    {
        return stMaxMarketAlert;
    }

    public void  setStMaxMarketAlert(BEC.MaxDateValue stMaxMarketAlert)
    {
        this.stMaxMarketAlert = stMaxMarketAlert;
    }

    public BEC.MaxDateValue getStMaxDtAIndex()
    {
        return stMaxDtAIndex;
    }

    public void  setStMaxDtAIndex(BEC.MaxDateValue stMaxDtAIndex)
    {
        this.stMaxDtAIndex = stMaxDtAIndex;
    }

    public BEC.MaxDateValue getStMaxMarginTrade()
    {
        return stMaxMarginTrade;
    }

    public void  setStMaxMarginTrade(BEC.MaxDateValue stMaxMarginTrade)
    {
        this.stMaxMarginTrade = stMaxMarginTrade;
    }

    public BEC.MaxDateValue getStMaxAHExtend()
    {
        return stMaxAHExtend;
    }

    public void  setStMaxAHExtend(BEC.MaxDateValue stMaxAHExtend)
    {
        this.stMaxAHExtend = stMaxAHExtend;
    }

    public BEC.MaxDateValue getStMaxNewInvestor()
    {
        return stMaxNewInvestor;
    }

    public void  setStMaxNewInvestor(BEC.MaxDateValue stMaxNewInvestor)
    {
        this.stMaxNewInvestor = stMaxNewInvestor;
    }

    public BEC.MaxDateValue getStMaxTradeAInvestorPer()
    {
        return stMaxTradeAInvestorPer;
    }

    public void  setStMaxTradeAInvestorPer(BEC.MaxDateValue stMaxTradeAInvestorPer)
    {
        this.stMaxTradeAInvestorPer = stMaxTradeAInvestorPer;
    }

    public java.util.ArrayList<DtMarketAlert> getVDtMarketAlert()
    {
        return vDtMarketAlert;
    }

    public void  setVDtMarketAlert(java.util.ArrayList<DtMarketAlert> vDtMarketAlert)
    {
        this.vDtMarketAlert = vDtMarketAlert;
    }

    public MarketAlertRsp()
    {
    }

    public MarketAlertRsp(int iDtAIndex, int iMarketMoney, int iMarketHot, BEC.MaxDateValue stMaxMarketAlert, BEC.MaxDateValue stMaxDtAIndex, BEC.MaxDateValue stMaxMarginTrade, BEC.MaxDateValue stMaxAHExtend, BEC.MaxDateValue stMaxNewInvestor, BEC.MaxDateValue stMaxTradeAInvestorPer, java.util.ArrayList<DtMarketAlert> vDtMarketAlert)
    {
        this.iDtAIndex = iDtAIndex;
        this.iMarketMoney = iMarketMoney;
        this.iMarketHot = iMarketHot;
        this.stMaxMarketAlert = stMaxMarketAlert;
        this.stMaxDtAIndex = stMaxDtAIndex;
        this.stMaxMarginTrade = stMaxMarginTrade;
        this.stMaxAHExtend = stMaxAHExtend;
        this.stMaxNewInvestor = stMaxNewInvestor;
        this.stMaxTradeAInvestorPer = stMaxTradeAInvestorPer;
        this.vDtMarketAlert = vDtMarketAlert;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iDtAIndex);
        ostream.writeInt32(1, iMarketMoney);
        ostream.writeInt32(2, iMarketHot);
        if (null != stMaxMarketAlert) {
            ostream.writeMessage(3, stMaxMarketAlert);
        }
        if (null != stMaxDtAIndex) {
            ostream.writeMessage(4, stMaxDtAIndex);
        }
        if (null != stMaxMarginTrade) {
            ostream.writeMessage(5, stMaxMarginTrade);
        }
        if (null != stMaxAHExtend) {
            ostream.writeMessage(6, stMaxAHExtend);
        }
        if (null != stMaxNewInvestor) {
            ostream.writeMessage(7, stMaxNewInvestor);
        }
        if (null != stMaxTradeAInvestorPer) {
            ostream.writeMessage(8, stMaxTradeAInvestorPer);
        }
        if (null != vDtMarketAlert) {
            ostream.writeList(9, vDtMarketAlert);
        }
    }

    static BEC.MaxDateValue VAR_TYPE_4_STMAXMARKETALERT = new BEC.MaxDateValue();

    static BEC.MaxDateValue VAR_TYPE_4_STMAXDTAINDEX = new BEC.MaxDateValue();

    static BEC.MaxDateValue VAR_TYPE_4_STMAXMARGINTRADE = new BEC.MaxDateValue();

    static BEC.MaxDateValue VAR_TYPE_4_STMAXAHEXTEND = new BEC.MaxDateValue();

    static BEC.MaxDateValue VAR_TYPE_4_STMAXNEWINVESTOR = new BEC.MaxDateValue();

    static BEC.MaxDateValue VAR_TYPE_4_STMAXTRADEAINVESTORPER = new BEC.MaxDateValue();

    static java.util.ArrayList<DtMarketAlert> VAR_TYPE_4_VDTMARKETALERT = new java.util.ArrayList<DtMarketAlert>();
    static {
        VAR_TYPE_4_VDTMARKETALERT.add(new DtMarketAlert());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iDtAIndex = (int)istream.readInt32(0, false, this.iDtAIndex);
        this.iMarketMoney = (int)istream.readInt32(1, false, this.iMarketMoney);
        this.iMarketHot = (int)istream.readInt32(2, false, this.iMarketHot);
        this.stMaxMarketAlert = (BEC.MaxDateValue)istream.readMessage(3, false, VAR_TYPE_4_STMAXMARKETALERT);
        this.stMaxDtAIndex = (BEC.MaxDateValue)istream.readMessage(4, false, VAR_TYPE_4_STMAXDTAINDEX);
        this.stMaxMarginTrade = (BEC.MaxDateValue)istream.readMessage(5, false, VAR_TYPE_4_STMAXMARGINTRADE);
        this.stMaxAHExtend = (BEC.MaxDateValue)istream.readMessage(6, false, VAR_TYPE_4_STMAXAHEXTEND);
        this.stMaxNewInvestor = (BEC.MaxDateValue)istream.readMessage(7, false, VAR_TYPE_4_STMAXNEWINVESTOR);
        this.stMaxTradeAInvestorPer = (BEC.MaxDateValue)istream.readMessage(8, false, VAR_TYPE_4_STMAXTRADEAINVESTORPER);
        this.vDtMarketAlert = (java.util.ArrayList<DtMarketAlert>)istream.readList(9, false, VAR_TYPE_4_VDTMARKETALERT);
    }

}

