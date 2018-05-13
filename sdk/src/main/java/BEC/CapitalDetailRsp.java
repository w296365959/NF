package BEC;

public final class CapitalDetailRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.CapitalDetailDesc> vCapitalDetailDesc = null;

    public java.util.ArrayList<BEC.CapitalDetailDesc> getVCapitalDetailDesc()
    {
        return vCapitalDetailDesc;
    }

    public void  setVCapitalDetailDesc(java.util.ArrayList<BEC.CapitalDetailDesc> vCapitalDetailDesc)
    {
        this.vCapitalDetailDesc = vCapitalDetailDesc;
    }

    public CapitalDetailRsp()
    {
    }

    public CapitalDetailRsp(java.util.ArrayList<BEC.CapitalDetailDesc> vCapitalDetailDesc)
    {
        this.vCapitalDetailDesc = vCapitalDetailDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCapitalDetailDesc) {
            ostream.writeList(0, vCapitalDetailDesc);
        }
    }

    static java.util.ArrayList<BEC.CapitalDetailDesc> VAR_TYPE_4_VCAPITALDETAILDESC = new java.util.ArrayList<BEC.CapitalDetailDesc>();
    static {
        VAR_TYPE_4_VCAPITALDETAILDESC.add(new BEC.CapitalDetailDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCapitalDetailDesc = (java.util.ArrayList<BEC.CapitalDetailDesc>)istream.readList(0, false, VAR_TYPE_4_VCAPITALDETAILDESC);
    }

}

