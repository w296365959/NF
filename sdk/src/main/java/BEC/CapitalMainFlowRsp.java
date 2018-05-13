package BEC;

public final class CapitalMainFlowRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.CapitalMainFlowDesc> vCapitalMainFlowDesc = null;

    public java.util.ArrayList<BEC.CapitalMainFlowDesc> getVCapitalMainFlowDesc()
    {
        return vCapitalMainFlowDesc;
    }

    public void  setVCapitalMainFlowDesc(java.util.ArrayList<BEC.CapitalMainFlowDesc> vCapitalMainFlowDesc)
    {
        this.vCapitalMainFlowDesc = vCapitalMainFlowDesc;
    }

    public CapitalMainFlowRsp()
    {
    }

    public CapitalMainFlowRsp(java.util.ArrayList<BEC.CapitalMainFlowDesc> vCapitalMainFlowDesc)
    {
        this.vCapitalMainFlowDesc = vCapitalMainFlowDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCapitalMainFlowDesc) {
            ostream.writeList(0, vCapitalMainFlowDesc);
        }
    }

    static java.util.ArrayList<BEC.CapitalMainFlowDesc> VAR_TYPE_4_VCAPITALMAINFLOWDESC = new java.util.ArrayList<BEC.CapitalMainFlowDesc>();
    static {
        VAR_TYPE_4_VCAPITALMAINFLOWDESC.add(new BEC.CapitalMainFlowDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCapitalMainFlowDesc = (java.util.ArrayList<BEC.CapitalMainFlowDesc>)istream.readList(0, false, VAR_TYPE_4_VCAPITALMAINFLOWDESC);
    }

}

