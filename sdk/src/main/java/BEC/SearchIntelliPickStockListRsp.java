package BEC;

public final class SearchIntelliPickStockListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<IntelliPickStockEx> vIntelliPickStockEx = null;

    public java.util.ArrayList<IntelliPickStockEx> getVIntelliPickStockEx()
    {
        return vIntelliPickStockEx;
    }

    public void  setVIntelliPickStockEx(java.util.ArrayList<IntelliPickStockEx> vIntelliPickStockEx)
    {
        this.vIntelliPickStockEx = vIntelliPickStockEx;
    }

    public SearchIntelliPickStockListRsp()
    {
    }

    public SearchIntelliPickStockListRsp(java.util.ArrayList<IntelliPickStockEx> vIntelliPickStockEx)
    {
        this.vIntelliPickStockEx = vIntelliPickStockEx;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vIntelliPickStockEx) {
            ostream.writeList(0, vIntelliPickStockEx);
        }
    }

    static java.util.ArrayList<IntelliPickStockEx> VAR_TYPE_4_VINTELLIPICKSTOCKEX = new java.util.ArrayList<IntelliPickStockEx>();
    static {
        VAR_TYPE_4_VINTELLIPICKSTOCKEX.add(new IntelliPickStockEx());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vIntelliPickStockEx = (java.util.ArrayList<IntelliPickStockEx>)istream.readList(0, false, VAR_TYPE_4_VINTELLIPICKSTOCKEX);
    }

}

