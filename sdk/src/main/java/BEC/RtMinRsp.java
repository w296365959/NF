package BEC;

public final class RtMinRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.RtMinDesc> vRtMinDesc = null;

    public java.util.ArrayList<BEC.RtMinDesc> getVRtMinDesc()
    {
        return vRtMinDesc;
    }

    public void  setVRtMinDesc(java.util.ArrayList<BEC.RtMinDesc> vRtMinDesc)
    {
        this.vRtMinDesc = vRtMinDesc;
    }

    public RtMinRsp()
    {
    }

    public RtMinRsp(java.util.ArrayList<BEC.RtMinDesc> vRtMinDesc)
    {
        this.vRtMinDesc = vRtMinDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vRtMinDesc) {
            ostream.writeList(0, vRtMinDesc);
        }
    }

    static java.util.ArrayList<BEC.RtMinDesc> VAR_TYPE_4_VRTMINDESC = new java.util.ArrayList<BEC.RtMinDesc>();
    static {
        VAR_TYPE_4_VRTMINDESC.add(new BEC.RtMinDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vRtMinDesc = (java.util.ArrayList<BEC.RtMinDesc>)istream.readList(0, false, VAR_TYPE_4_VRTMINDESC);
    }

}

