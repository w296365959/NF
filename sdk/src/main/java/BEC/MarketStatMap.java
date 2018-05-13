package BEC;

public final class MarketStatMap extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Integer, BEC.MarketStatDesc> mMarketStatDesc = null;

    public java.util.Map<Integer, BEC.MarketStatDesc> getMMarketStatDesc()
    {
        return mMarketStatDesc;
    }

    public void  setMMarketStatDesc(java.util.Map<Integer, BEC.MarketStatDesc> mMarketStatDesc)
    {
        this.mMarketStatDesc = mMarketStatDesc;
    }

    public MarketStatMap()
    {
    }

    public MarketStatMap(java.util.Map<Integer, BEC.MarketStatDesc> mMarketStatDesc)
    {
        this.mMarketStatDesc = mMarketStatDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mMarketStatDesc) {
            ostream.writeMap(0, mMarketStatDesc);
        }
    }

    static java.util.Map<Integer, BEC.MarketStatDesc> VAR_TYPE_4_MMARKETSTATDESC = new java.util.HashMap<Integer, BEC.MarketStatDesc>();
    static {
        VAR_TYPE_4_MMARKETSTATDESC.put(0, new BEC.MarketStatDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mMarketStatDesc = (java.util.Map<Integer, BEC.MarketStatDesc>)istream.readMap(0, false, VAR_TYPE_4_MMARKETSTATDESC);
    }

}

