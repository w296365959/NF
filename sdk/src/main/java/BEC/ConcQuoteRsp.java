package BEC;

public final class ConcQuoteRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ConcQuoteDesc> vConcQuoteDesc = null;

    public java.util.ArrayList<BEC.ConcQuoteDesc> getVConcQuoteDesc()
    {
        return vConcQuoteDesc;
    }

    public void  setVConcQuoteDesc(java.util.ArrayList<BEC.ConcQuoteDesc> vConcQuoteDesc)
    {
        this.vConcQuoteDesc = vConcQuoteDesc;
    }

    public ConcQuoteRsp()
    {
    }

    public ConcQuoteRsp(java.util.ArrayList<BEC.ConcQuoteDesc> vConcQuoteDesc)
    {
        this.vConcQuoteDesc = vConcQuoteDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vConcQuoteDesc) {
            ostream.writeList(0, vConcQuoteDesc);
        }
    }

    static java.util.ArrayList<BEC.ConcQuoteDesc> VAR_TYPE_4_VCONCQUOTEDESC = new java.util.ArrayList<BEC.ConcQuoteDesc>();
    static {
        VAR_TYPE_4_VCONCQUOTEDESC.add(new BEC.ConcQuoteDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vConcQuoteDesc = (java.util.ArrayList<BEC.ConcQuoteDesc>)istream.readList(0, false, VAR_TYPE_4_VCONCQUOTEDESC);
    }

}

