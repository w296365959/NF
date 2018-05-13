package BEC;

public final class MarketStatRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.MarketStatList stMarketStatList = null;

    public BEC.MarketStatList getStMarketStatList()
    {
        return stMarketStatList;
    }

    public void  setStMarketStatList(BEC.MarketStatList stMarketStatList)
    {
        this.stMarketStatList = stMarketStatList;
    }

    public MarketStatRsp()
    {
    }

    public MarketStatRsp(BEC.MarketStatList stMarketStatList)
    {
        this.stMarketStatList = stMarketStatList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stMarketStatList) {
            ostream.writeMessage(0, stMarketStatList);
        }
    }

    static BEC.MarketStatList VAR_TYPE_4_STMARKETSTATLIST = new BEC.MarketStatList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stMarketStatList = (BEC.MarketStatList)istream.readMessage(0, false, VAR_TYPE_4_STMARKETSTATLIST);
    }

}

