package BEC;

public final class STCostClose extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.StockCostClose> vtStockCostClose = null;

    public java.util.ArrayList<BEC.StockCostClose> getVtStockCostClose()
    {
        return vtStockCostClose;
    }

    public void  setVtStockCostClose(java.util.ArrayList<BEC.StockCostClose> vtStockCostClose)
    {
        this.vtStockCostClose = vtStockCostClose;
    }

    public STCostClose()
    {
    }

    public STCostClose(java.util.ArrayList<BEC.StockCostClose> vtStockCostClose)
    {
        this.vtStockCostClose = vtStockCostClose;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtStockCostClose) {
            ostream.writeList(0, vtStockCostClose);
        }
    }

    static java.util.ArrayList<BEC.StockCostClose> VAR_TYPE_4_VTSTOCKCOSTCLOSE = new java.util.ArrayList<BEC.StockCostClose>();
    static {
        VAR_TYPE_4_VTSTOCKCOSTCLOSE.add(new BEC.StockCostClose());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtStockCostClose = (java.util.ArrayList<BEC.StockCostClose>)istream.readList(0, false, VAR_TYPE_4_VTSTOCKCOSTCLOSE);
    }

}

