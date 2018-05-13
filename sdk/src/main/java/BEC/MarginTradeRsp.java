package BEC;

public final class MarginTradeRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.MarginTradeInfo> vMarginTradeInfo = null;

    public java.util.ArrayList<BEC.MarginTradeInfo> getVMarginTradeInfo()
    {
        return vMarginTradeInfo;
    }

    public void  setVMarginTradeInfo(java.util.ArrayList<BEC.MarginTradeInfo> vMarginTradeInfo)
    {
        this.vMarginTradeInfo = vMarginTradeInfo;
    }

    public MarginTradeRsp()
    {
    }

    public MarginTradeRsp(java.util.ArrayList<BEC.MarginTradeInfo> vMarginTradeInfo)
    {
        this.vMarginTradeInfo = vMarginTradeInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vMarginTradeInfo) {
            ostream.writeList(0, vMarginTradeInfo);
        }
    }

    static java.util.ArrayList<BEC.MarginTradeInfo> VAR_TYPE_4_VMARGINTRADEINFO = new java.util.ArrayList<BEC.MarginTradeInfo>();
    static {
        VAR_TYPE_4_VMARGINTRADEINFO.add(new BEC.MarginTradeInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vMarginTradeInfo = (java.util.ArrayList<BEC.MarginTradeInfo>)istream.readList(0, false, VAR_TYPE_4_VMARGINTRADEINFO);
    }

}

