package BEC;

public final class IntelliPickStockRspEx extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<IntelliPickStockEx> vtIntelliPickStockEx = null;

    public java.util.ArrayList<IntelliPickStockEx> getVtIntelliPickStockEx()
    {
        return vtIntelliPickStockEx;
    }

    public void  setVtIntelliPickStockEx(java.util.ArrayList<IntelliPickStockEx> vtIntelliPickStockEx)
    {
        this.vtIntelliPickStockEx = vtIntelliPickStockEx;
    }

    public IntelliPickStockRspEx()
    {
    }

    public IntelliPickStockRspEx(java.util.ArrayList<IntelliPickStockEx> vtIntelliPickStockEx)
    {
        this.vtIntelliPickStockEx = vtIntelliPickStockEx;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtIntelliPickStockEx) {
            ostream.writeList(0, vtIntelliPickStockEx);
        }
    }

    static java.util.ArrayList<IntelliPickStockEx> VAR_TYPE_4_VTINTELLIPICKSTOCKEX = new java.util.ArrayList<IntelliPickStockEx>();
    static {
        VAR_TYPE_4_VTINTELLIPICKSTOCKEX.add(new IntelliPickStockEx());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtIntelliPickStockEx = (java.util.ArrayList<IntelliPickStockEx>)istream.readList(0, false, VAR_TYPE_4_VTINTELLIPICKSTOCKEX);
    }

}

