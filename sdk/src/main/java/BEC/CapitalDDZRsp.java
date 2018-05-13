package BEC;

public final class CapitalDDZRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.CapitalDDZDesc> vCapitalDDZDesc = null;

    public java.util.ArrayList<BEC.CapitalDDZDesc> getVCapitalDDZDesc()
    {
        return vCapitalDDZDesc;
    }

    public void  setVCapitalDDZDesc(java.util.ArrayList<BEC.CapitalDDZDesc> vCapitalDDZDesc)
    {
        this.vCapitalDDZDesc = vCapitalDDZDesc;
    }

    public CapitalDDZRsp()
    {
    }

    public CapitalDDZRsp(java.util.ArrayList<BEC.CapitalDDZDesc> vCapitalDDZDesc)
    {
        this.vCapitalDDZDesc = vCapitalDDZDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCapitalDDZDesc) {
            ostream.writeList(0, vCapitalDDZDesc);
        }
    }

    static java.util.ArrayList<BEC.CapitalDDZDesc> VAR_TYPE_4_VCAPITALDDZDESC = new java.util.ArrayList<BEC.CapitalDDZDesc>();
    static {
        VAR_TYPE_4_VCAPITALDDZDESC.add(new BEC.CapitalDDZDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCapitalDDZDesc = (java.util.ArrayList<BEC.CapitalDDZDesc>)istream.readList(0, false, VAR_TYPE_4_VCAPITALDDZDESC);
    }

}

