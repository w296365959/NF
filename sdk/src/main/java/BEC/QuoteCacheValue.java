package BEC;

public final class QuoteCacheValue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.SecQuote stSecQuote = null;

    public int iFlushTime = 0;

    public BEC.SecQuote getStSecQuote()
    {
        return stSecQuote;
    }

    public void  setStSecQuote(BEC.SecQuote stSecQuote)
    {
        this.stSecQuote = stSecQuote;
    }

    public int getIFlushTime()
    {
        return iFlushTime;
    }

    public void  setIFlushTime(int iFlushTime)
    {
        this.iFlushTime = iFlushTime;
    }

    public QuoteCacheValue()
    {
    }

    public QuoteCacheValue(BEC.SecQuote stSecQuote, int iFlushTime)
    {
        this.stSecQuote = stSecQuote;
        this.iFlushTime = iFlushTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecQuote) {
            ostream.writeMessage(0, stSecQuote);
        }
        ostream.writeInt32(1, iFlushTime);
    }

    static BEC.SecQuote VAR_TYPE_4_STSECQUOTE = new BEC.SecQuote();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecQuote = (BEC.SecQuote)istream.readMessage(0, false, VAR_TYPE_4_STSECQUOTE);
        this.iFlushTime = (int)istream.readInt32(1, false, this.iFlushTime);
    }

}

