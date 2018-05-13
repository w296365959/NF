package BEC;

public final class StockMarginTradeRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sIndustry = "";

    public java.util.ArrayList<BEC.StockDateMarginTrade> vtStockDateMarginTrade = null;

    public BEC.IndustryRank stBalanceRank = null;

    public BEC.IndustryRank stBuyValueRank = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSIndustry()
    {
        return sIndustry;
    }

    public void  setSIndustry(String sIndustry)
    {
        this.sIndustry = sIndustry;
    }

    public java.util.ArrayList<BEC.StockDateMarginTrade> getVtStockDateMarginTrade()
    {
        return vtStockDateMarginTrade;
    }

    public void  setVtStockDateMarginTrade(java.util.ArrayList<BEC.StockDateMarginTrade> vtStockDateMarginTrade)
    {
        this.vtStockDateMarginTrade = vtStockDateMarginTrade;
    }

    public BEC.IndustryRank getStBalanceRank()
    {
        return stBalanceRank;
    }

    public void  setStBalanceRank(BEC.IndustryRank stBalanceRank)
    {
        this.stBalanceRank = stBalanceRank;
    }

    public BEC.IndustryRank getStBuyValueRank()
    {
        return stBuyValueRank;
    }

    public void  setStBuyValueRank(BEC.IndustryRank stBuyValueRank)
    {
        this.stBuyValueRank = stBuyValueRank;
    }

    public StockMarginTradeRsp()
    {
    }

    public StockMarginTradeRsp(String sDtSecCode, String sIndustry, java.util.ArrayList<BEC.StockDateMarginTrade> vtStockDateMarginTrade, BEC.IndustryRank stBalanceRank, BEC.IndustryRank stBuyValueRank)
    {
        this.sDtSecCode = sDtSecCode;
        this.sIndustry = sIndustry;
        this.vtStockDateMarginTrade = vtStockDateMarginTrade;
        this.stBalanceRank = stBalanceRank;
        this.stBuyValueRank = stBuyValueRank;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sIndustry) {
            ostream.writeString(1, sIndustry);
        }
        if (null != vtStockDateMarginTrade) {
            ostream.writeList(2, vtStockDateMarginTrade);
        }
        if (null != stBalanceRank) {
            ostream.writeMessage(3, stBalanceRank);
        }
        if (null != stBuyValueRank) {
            ostream.writeMessage(4, stBuyValueRank);
        }
    }

    static java.util.ArrayList<BEC.StockDateMarginTrade> VAR_TYPE_4_VTSTOCKDATEMARGINTRADE = new java.util.ArrayList<BEC.StockDateMarginTrade>();
    static {
        VAR_TYPE_4_VTSTOCKDATEMARGINTRADE.add(new BEC.StockDateMarginTrade());
    }

    static BEC.IndustryRank VAR_TYPE_4_STBALANCERANK = new BEC.IndustryRank();

    static BEC.IndustryRank VAR_TYPE_4_STBUYVALUERANK = new BEC.IndustryRank();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sIndustry = (String)istream.readString(1, false, this.sIndustry);
        this.vtStockDateMarginTrade = (java.util.ArrayList<BEC.StockDateMarginTrade>)istream.readList(2, false, VAR_TYPE_4_VTSTOCKDATEMARGINTRADE);
        this.stBalanceRank = (BEC.IndustryRank)istream.readMessage(3, false, VAR_TYPE_4_STBALANCERANK);
        this.stBuyValueRank = (BEC.IndustryRank)istream.readMessage(4, false, VAR_TYPE_4_STBUYVALUERANK);
    }

}

