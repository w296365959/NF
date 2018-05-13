package BEC;

public final class TransStat extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.TransStatDesc> vTransStatDesc = null;

    public java.util.ArrayList<BEC.TransStatDesc> getVTransStatDesc()
    {
        return vTransStatDesc;
    }

    public void  setVTransStatDesc(java.util.ArrayList<BEC.TransStatDesc> vTransStatDesc)
    {
        this.vTransStatDesc = vTransStatDesc;
    }

    public TransStat()
    {
    }

    public TransStat(java.util.ArrayList<BEC.TransStatDesc> vTransStatDesc)
    {
        this.vTransStatDesc = vTransStatDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vTransStatDesc) {
            ostream.writeList(0, vTransStatDesc);
        }
    }

    static java.util.ArrayList<BEC.TransStatDesc> VAR_TYPE_4_VTRANSSTATDESC = new java.util.ArrayList<BEC.TransStatDesc>();
    static {
        VAR_TYPE_4_VTRANSSTATDESC.add(new BEC.TransStatDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vTransStatDesc = (java.util.ArrayList<BEC.TransStatDesc>)istream.readList(0, false, VAR_TYPE_4_VTRANSSTATDESC);
    }

}

