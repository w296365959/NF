package BEC;

public final class MarketLiveCacheInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.MarketLiveMsgDesc> vMarketLive = null;

    public java.util.ArrayList<BEC.MarketLiveMsgDesc> getVMarketLive()
    {
        return vMarketLive;
    }

    public void  setVMarketLive(java.util.ArrayList<BEC.MarketLiveMsgDesc> vMarketLive)
    {
        this.vMarketLive = vMarketLive;
    }

    public MarketLiveCacheInfo()
    {
    }

    public MarketLiveCacheInfo(java.util.ArrayList<BEC.MarketLiveMsgDesc> vMarketLive)
    {
        this.vMarketLive = vMarketLive;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vMarketLive) {
            ostream.writeList(0, vMarketLive);
        }
    }

    static java.util.ArrayList<BEC.MarketLiveMsgDesc> VAR_TYPE_4_VMARKETLIVE = new java.util.ArrayList<BEC.MarketLiveMsgDesc>();
    static {
        VAR_TYPE_4_VMARKETLIVE.add(new BEC.MarketLiveMsgDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vMarketLive = (java.util.ArrayList<BEC.MarketLiveMsgDesc>)istream.readList(0, false, VAR_TYPE_4_VMARKETLIVE);
    }

}

