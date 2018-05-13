package BEC;

public final class MarginTradeMktRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.MarginTradeMkt> vtSZMarginTrade = null;

    public java.util.ArrayList<BEC.MarginTradeMkt> vtSHMarginTrade = null;

    public java.util.ArrayList<BEC.MarginTradeMkt> getVtSZMarginTrade()
    {
        return vtSZMarginTrade;
    }

    public void  setVtSZMarginTrade(java.util.ArrayList<BEC.MarginTradeMkt> vtSZMarginTrade)
    {
        this.vtSZMarginTrade = vtSZMarginTrade;
    }

    public java.util.ArrayList<BEC.MarginTradeMkt> getVtSHMarginTrade()
    {
        return vtSHMarginTrade;
    }

    public void  setVtSHMarginTrade(java.util.ArrayList<BEC.MarginTradeMkt> vtSHMarginTrade)
    {
        this.vtSHMarginTrade = vtSHMarginTrade;
    }

    public MarginTradeMktRsp()
    {
    }

    public MarginTradeMktRsp(java.util.ArrayList<BEC.MarginTradeMkt> vtSZMarginTrade, java.util.ArrayList<BEC.MarginTradeMkt> vtSHMarginTrade)
    {
        this.vtSZMarginTrade = vtSZMarginTrade;
        this.vtSHMarginTrade = vtSHMarginTrade;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSZMarginTrade) {
            ostream.writeList(0, vtSZMarginTrade);
        }
        if (null != vtSHMarginTrade) {
            ostream.writeList(1, vtSHMarginTrade);
        }
    }

    static java.util.ArrayList<BEC.MarginTradeMkt> VAR_TYPE_4_VTSZMARGINTRADE = new java.util.ArrayList<BEC.MarginTradeMkt>();
    static {
        VAR_TYPE_4_VTSZMARGINTRADE.add(new BEC.MarginTradeMkt());
    }

    static java.util.ArrayList<BEC.MarginTradeMkt> VAR_TYPE_4_VTSHMARGINTRADE = new java.util.ArrayList<BEC.MarginTradeMkt>();
    static {
        VAR_TYPE_4_VTSHMARGINTRADE.add(new BEC.MarginTradeMkt());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSZMarginTrade = (java.util.ArrayList<BEC.MarginTradeMkt>)istream.readList(0, false, VAR_TYPE_4_VTSZMARGINTRADE);
        this.vtSHMarginTrade = (java.util.ArrayList<BEC.MarginTradeMkt>)istream.readList(1, false, VAR_TYPE_4_VTSHMARGINTRADE);
    }

}

