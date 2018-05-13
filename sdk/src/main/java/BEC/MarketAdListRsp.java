package BEC;

public final class MarketAdListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<MarketAd> vMarketAd = null;

    public java.util.ArrayList<MarketAd> getVMarketAd()
    {
        return vMarketAd;
    }

    public void  setVMarketAd(java.util.ArrayList<MarketAd> vMarketAd)
    {
        this.vMarketAd = vMarketAd;
    }

    public MarketAdListRsp()
    {
    }

    public MarketAdListRsp(java.util.ArrayList<MarketAd> vMarketAd)
    {
        this.vMarketAd = vMarketAd;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vMarketAd) {
            ostream.writeList(0, vMarketAd);
        }
    }

    static java.util.ArrayList<MarketAd> VAR_TYPE_4_VMARKETAD = new java.util.ArrayList<MarketAd>();
    static {
        VAR_TYPE_4_VMARKETAD.add(new MarketAd());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vMarketAd = (java.util.ArrayList<MarketAd>)istream.readList(0, false, VAR_TYPE_4_VMARKETAD);
    }

}

