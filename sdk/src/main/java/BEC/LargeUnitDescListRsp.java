package BEC;

public final class LargeUnitDescListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.LargeUnitDesc> vLargeUnitDesc = null;

    public java.util.ArrayList<BEC.LargeUnitDesc> getVLargeUnitDesc()
    {
        return vLargeUnitDesc;
    }

    public void  setVLargeUnitDesc(java.util.ArrayList<BEC.LargeUnitDesc> vLargeUnitDesc)
    {
        this.vLargeUnitDesc = vLargeUnitDesc;
    }

    public LargeUnitDescListRsp()
    {
    }

    public LargeUnitDescListRsp(java.util.ArrayList<BEC.LargeUnitDesc> vLargeUnitDesc)
    {
        this.vLargeUnitDesc = vLargeUnitDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vLargeUnitDesc) {
            ostream.writeList(0, vLargeUnitDesc);
        }
    }

    static java.util.ArrayList<BEC.LargeUnitDesc> VAR_TYPE_4_VLARGEUNITDESC = new java.util.ArrayList<BEC.LargeUnitDesc>();
    static {
        VAR_TYPE_4_VLARGEUNITDESC.add(new BEC.LargeUnitDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vLargeUnitDesc = (java.util.ArrayList<BEC.LargeUnitDesc>)istream.readList(0, false, VAR_TYPE_4_VLARGEUNITDESC);
    }

}

