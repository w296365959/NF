package BEC;

public final class GetLiveRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.PortfolioLiveMsgDesc> vPortfolioLive = null;

    public java.util.ArrayList<BEC.MarketLiveMsgDesc> vMarketLive = null;

    public java.util.ArrayList<BEC.PortfolioLiveMsgDesc> getVPortfolioLive()
    {
        return vPortfolioLive;
    }

    public void  setVPortfolioLive(java.util.ArrayList<BEC.PortfolioLiveMsgDesc> vPortfolioLive)
    {
        this.vPortfolioLive = vPortfolioLive;
    }

    public java.util.ArrayList<BEC.MarketLiveMsgDesc> getVMarketLive()
    {
        return vMarketLive;
    }

    public void  setVMarketLive(java.util.ArrayList<BEC.MarketLiveMsgDesc> vMarketLive)
    {
        this.vMarketLive = vMarketLive;
    }

    public GetLiveRsp()
    {
    }

    public GetLiveRsp(java.util.ArrayList<BEC.PortfolioLiveMsgDesc> vPortfolioLive, java.util.ArrayList<BEC.MarketLiveMsgDesc> vMarketLive)
    {
        this.vPortfolioLive = vPortfolioLive;
        this.vMarketLive = vMarketLive;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vPortfolioLive) {
            ostream.writeList(0, vPortfolioLive);
        }
        if (null != vMarketLive) {
            ostream.writeList(1, vMarketLive);
        }
    }

    static java.util.ArrayList<BEC.PortfolioLiveMsgDesc> VAR_TYPE_4_VPORTFOLIOLIVE = new java.util.ArrayList<BEC.PortfolioLiveMsgDesc>();
    static {
        VAR_TYPE_4_VPORTFOLIOLIVE.add(new BEC.PortfolioLiveMsgDesc());
    }

    static java.util.ArrayList<BEC.MarketLiveMsgDesc> VAR_TYPE_4_VMARKETLIVE = new java.util.ArrayList<BEC.MarketLiveMsgDesc>();
    static {
        VAR_TYPE_4_VMARKETLIVE.add(new BEC.MarketLiveMsgDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vPortfolioLive = (java.util.ArrayList<BEC.PortfolioLiveMsgDesc>)istream.readList(0, false, VAR_TYPE_4_VPORTFOLIOLIVE);
        this.vMarketLive = (java.util.ArrayList<BEC.MarketLiveMsgDesc>)istream.readList(1, false, VAR_TYPE_4_VMARKETLIVE);
    }

}

