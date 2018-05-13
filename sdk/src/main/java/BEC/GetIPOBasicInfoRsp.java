package BEC;

public final class GetIPOBasicInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecIPOBasicInfo> vInfo = null;

    public java.util.ArrayList<SecIPOBasicInfo> getVInfo()
    {
        return vInfo;
    }

    public void  setVInfo(java.util.ArrayList<SecIPOBasicInfo> vInfo)
    {
        this.vInfo = vInfo;
    }

    public GetIPOBasicInfoRsp()
    {
    }

    public GetIPOBasicInfoRsp(java.util.ArrayList<SecIPOBasicInfo> vInfo)
    {
        this.vInfo = vInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vInfo) {
            ostream.writeList(0, vInfo);
        }
    }

    static java.util.ArrayList<SecIPOBasicInfo> VAR_TYPE_4_VINFO = new java.util.ArrayList<SecIPOBasicInfo>();
    static {
        VAR_TYPE_4_VINFO.add(new SecIPOBasicInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vInfo = (java.util.ArrayList<SecIPOBasicInfo>)istream.readList(0, false, VAR_TYPE_4_VINFO);
    }

}

