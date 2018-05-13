package BEC;

public final class StrategyListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<IntelliPickStockEx> vtDaySec = null;

    public java.util.ArrayList<IntelliPickStockEx> getVtDaySec()
    {
        return vtDaySec;
    }

    public void  setVtDaySec(java.util.ArrayList<IntelliPickStockEx> vtDaySec)
    {
        this.vtDaySec = vtDaySec;
    }

    public StrategyListRsp()
    {
    }

    public StrategyListRsp(java.util.ArrayList<IntelliPickStockEx> vtDaySec)
    {
        this.vtDaySec = vtDaySec;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtDaySec) {
            ostream.writeList(0, vtDaySec);
        }
    }

    static java.util.ArrayList<IntelliPickStockEx> VAR_TYPE_4_VTDAYSEC = new java.util.ArrayList<IntelliPickStockEx>();
    static {
        VAR_TYPE_4_VTDAYSEC.add(new IntelliPickStockEx());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtDaySec = (java.util.ArrayList<IntelliPickStockEx>)istream.readList(0, false, VAR_TYPE_4_VTDAYSEC);
    }

}

