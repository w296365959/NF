package BEC;

public final class MarTraRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STMarginTrade> vtMarginTrade = null;

    public java.util.ArrayList<BEC.STMarginTrade> getVtMarginTrade()
    {
        return vtMarginTrade;
    }

    public void  setVtMarginTrade(java.util.ArrayList<BEC.STMarginTrade> vtMarginTrade)
    {
        this.vtMarginTrade = vtMarginTrade;
    }

    public MarTraRsp()
    {
    }

    public MarTraRsp(java.util.ArrayList<BEC.STMarginTrade> vtMarginTrade)
    {
        this.vtMarginTrade = vtMarginTrade;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtMarginTrade) {
            ostream.writeList(0, vtMarginTrade);
        }
    }

    static java.util.ArrayList<BEC.STMarginTrade> VAR_TYPE_4_VTMARGINTRADE = new java.util.ArrayList<BEC.STMarginTrade>();
    static {
        VAR_TYPE_4_VTMARGINTRADE.add(new BEC.STMarginTrade());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtMarginTrade = (java.util.ArrayList<BEC.STMarginTrade>)istream.readList(0, false, VAR_TYPE_4_VTMARGINTRADE);
    }

}

