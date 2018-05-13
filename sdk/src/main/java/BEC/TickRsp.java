package BEC;

public final class TickRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.TickDesc> vTickDesc = null;

    public java.util.ArrayList<BEC.TickDesc> getVTickDesc()
    {
        return vTickDesc;
    }

    public void  setVTickDesc(java.util.ArrayList<BEC.TickDesc> vTickDesc)
    {
        this.vTickDesc = vTickDesc;
    }

    public TickRsp()
    {
    }

    public TickRsp(java.util.ArrayList<BEC.TickDesc> vTickDesc)
    {
        this.vTickDesc = vTickDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vTickDesc) {
            ostream.writeList(0, vTickDesc);
        }
    }

    static java.util.ArrayList<BEC.TickDesc> VAR_TYPE_4_VTICKDESC = new java.util.ArrayList<BEC.TickDesc>();
    static {
        VAR_TYPE_4_VTICKDESC.add(new BEC.TickDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vTickDesc = (java.util.ArrayList<BEC.TickDesc>)istream.readList(0, false, VAR_TYPE_4_VTICKDESC);
    }

}

