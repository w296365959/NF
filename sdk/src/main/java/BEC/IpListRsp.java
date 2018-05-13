package BEC;

public final class IpListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.IpInfo> vtIpInfo = null;

    public java.util.ArrayList<BEC.IpInfo> getVtIpInfo()
    {
        return vtIpInfo;
    }

    public void  setVtIpInfo(java.util.ArrayList<BEC.IpInfo> vtIpInfo)
    {
        this.vtIpInfo = vtIpInfo;
    }

    public IpListRsp()
    {
    }

    public IpListRsp(java.util.ArrayList<BEC.IpInfo> vtIpInfo)
    {
        this.vtIpInfo = vtIpInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtIpInfo) {
            ostream.writeList(0, vtIpInfo);
        }
    }

    static java.util.ArrayList<BEC.IpInfo> VAR_TYPE_4_VTIPINFO = new java.util.ArrayList<BEC.IpInfo>();
    static {
        VAR_TYPE_4_VTIPINFO.add(new BEC.IpInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtIpInfo = (java.util.ArrayList<BEC.IpInfo>)istream.readList(0, false, VAR_TYPE_4_VTIPINFO);
    }

}

