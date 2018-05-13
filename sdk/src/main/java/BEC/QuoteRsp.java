package BEC;

public final class QuoteRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecQuote> vSecQuote = null;

    public java.util.ArrayList<BEC.SecQuote> getVSecQuote()
    {
        return vSecQuote;
    }

    public void  setVSecQuote(java.util.ArrayList<BEC.SecQuote> vSecQuote)
    {
        this.vSecQuote = vSecQuote;
    }

    public QuoteRsp()
    {
    }

    public QuoteRsp(java.util.ArrayList<BEC.SecQuote> vSecQuote)
    {
        this.vSecQuote = vSecQuote;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecQuote) {
            ostream.writeList(0, vSecQuote);
        }
    }

    static java.util.ArrayList<BEC.SecQuote> VAR_TYPE_4_VSECQUOTE = new java.util.ArrayList<BEC.SecQuote>();
    static {
        VAR_TYPE_4_VSECQUOTE.add(new BEC.SecQuote());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecQuote = (java.util.ArrayList<BEC.SecQuote>)istream.readList(0, false, VAR_TYPE_4_VSECQUOTE);
    }

}

