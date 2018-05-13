package BEC;

public final class MarketStatList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.MarketStatDesc> vMarketStatDesc = null;

    public java.util.ArrayList<BEC.MarketStatDesc> getVMarketStatDesc()
    {
        return vMarketStatDesc;
    }

    public void  setVMarketStatDesc(java.util.ArrayList<BEC.MarketStatDesc> vMarketStatDesc)
    {
        this.vMarketStatDesc = vMarketStatDesc;
    }

    public MarketStatList()
    {
    }

    public MarketStatList(java.util.ArrayList<BEC.MarketStatDesc> vMarketStatDesc)
    {
        this.vMarketStatDesc = vMarketStatDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vMarketStatDesc) {
            ostream.writeList(0, vMarketStatDesc);
        }
    }

    static java.util.ArrayList<BEC.MarketStatDesc> VAR_TYPE_4_VMARKETSTATDESC = new java.util.ArrayList<BEC.MarketStatDesc>();
    static {
        VAR_TYPE_4_VMARKETSTATDESC.add(new BEC.MarketStatDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vMarketStatDesc = (java.util.ArrayList<BEC.MarketStatDesc>)istream.readList(0, false, VAR_TYPE_4_VMARKETSTATDESC);
    }

}

