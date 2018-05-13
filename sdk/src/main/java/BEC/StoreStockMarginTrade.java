package BEC;

public final class StoreStockMarginTrade extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.StockDateMarginTrade> vtStockDateMarginTrade = null;

    public java.util.ArrayList<BEC.StockDateMarginTrade> getVtStockDateMarginTrade()
    {
        return vtStockDateMarginTrade;
    }

    public void  setVtStockDateMarginTrade(java.util.ArrayList<BEC.StockDateMarginTrade> vtStockDateMarginTrade)
    {
        this.vtStockDateMarginTrade = vtStockDateMarginTrade;
    }

    public StoreStockMarginTrade()
    {
    }

    public StoreStockMarginTrade(java.util.ArrayList<BEC.StockDateMarginTrade> vtStockDateMarginTrade)
    {
        this.vtStockDateMarginTrade = vtStockDateMarginTrade;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtStockDateMarginTrade) {
            ostream.writeList(0, vtStockDateMarginTrade);
        }
    }

    static java.util.ArrayList<BEC.StockDateMarginTrade> VAR_TYPE_4_VTSTOCKDATEMARGINTRADE = new java.util.ArrayList<BEC.StockDateMarginTrade>();
    static {
        VAR_TYPE_4_VTSTOCKDATEMARGINTRADE.add(new BEC.StockDateMarginTrade());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtStockDateMarginTrade = (java.util.ArrayList<BEC.StockDateMarginTrade>)istream.readList(0, false, VAR_TYPE_4_VTSTOCKDATEMARGINTRADE);
    }

}

