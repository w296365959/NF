package BEC;

public final class PlateStockListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecSimpleQuote> vSecSimpleQuote = null;

    public java.util.ArrayList<BEC.SecSimpleQuote> getVSecSimpleQuote()
    {
        return vSecSimpleQuote;
    }

    public void  setVSecSimpleQuote(java.util.ArrayList<BEC.SecSimpleQuote> vSecSimpleQuote)
    {
        this.vSecSimpleQuote = vSecSimpleQuote;
    }

    public PlateStockListRsp()
    {
    }

    public PlateStockListRsp(java.util.ArrayList<BEC.SecSimpleQuote> vSecSimpleQuote)
    {
        this.vSecSimpleQuote = vSecSimpleQuote;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecSimpleQuote) {
            ostream.writeList(0, vSecSimpleQuote);
        }
    }

    static java.util.ArrayList<BEC.SecSimpleQuote> VAR_TYPE_4_VSECSIMPLEQUOTE = new java.util.ArrayList<BEC.SecSimpleQuote>();
    static {
        VAR_TYPE_4_VSECSIMPLEQUOTE.add(new BEC.SecSimpleQuote());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecSimpleQuote = (java.util.ArrayList<BEC.SecSimpleQuote>)istream.readList(0, false, VAR_TYPE_4_VSECSIMPLEQUOTE);
    }

}

