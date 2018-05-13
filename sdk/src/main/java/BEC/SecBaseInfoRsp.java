package BEC;

public final class SecBaseInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecInfo> vSecInfo = null;

    public java.util.ArrayList<BEC.SecInfo> getVSecInfo()
    {
        return vSecInfo;
    }

    public void  setVSecInfo(java.util.ArrayList<BEC.SecInfo> vSecInfo)
    {
        this.vSecInfo = vSecInfo;
    }

    public SecBaseInfoRsp()
    {
    }

    public SecBaseInfoRsp(java.util.ArrayList<BEC.SecInfo> vSecInfo)
    {
        this.vSecInfo = vSecInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecInfo) {
            ostream.writeList(0, vSecInfo);
        }
    }

    static java.util.ArrayList<BEC.SecInfo> VAR_TYPE_4_VSECINFO = new java.util.ArrayList<BEC.SecInfo>();
    static {
        VAR_TYPE_4_VSECINFO.add(new BEC.SecInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecInfo = (java.util.ArrayList<BEC.SecInfo>)istream.readList(0, false, VAR_TYPE_4_VSECINFO);
    }

}

