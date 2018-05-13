package BEC;

public final class STChipDist extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.StockChipDist> vStockChipDist = null;

    public java.util.ArrayList<BEC.StockChipDist> getVStockChipDist()
    {
        return vStockChipDist;
    }

    public void  setVStockChipDist(java.util.ArrayList<BEC.StockChipDist> vStockChipDist)
    {
        this.vStockChipDist = vStockChipDist;
    }

    public STChipDist()
    {
    }

    public STChipDist(java.util.ArrayList<BEC.StockChipDist> vStockChipDist)
    {
        this.vStockChipDist = vStockChipDist;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vStockChipDist) {
            ostream.writeList(0, vStockChipDist);
        }
    }

    static java.util.ArrayList<BEC.StockChipDist> VAR_TYPE_4_VSTOCKCHIPDIST = new java.util.ArrayList<BEC.StockChipDist>();
    static {
        VAR_TYPE_4_VSTOCKCHIPDIST.add(new BEC.StockChipDist());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vStockChipDist = (java.util.ArrayList<BEC.StockChipDist>)istream.readList(0, false, VAR_TYPE_4_VSTOCKCHIPDIST);
    }

}

