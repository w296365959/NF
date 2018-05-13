package BEC;

public final class GetPrivDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, PrivInfo> mPrivInfo = null;

    public java.util.Map<String, PrivInfo> getMPrivInfo()
    {
        return mPrivInfo;
    }

    public void  setMPrivInfo(java.util.Map<String, PrivInfo> mPrivInfo)
    {
        this.mPrivInfo = mPrivInfo;
    }

    public GetPrivDetailRsp()
    {
    }

    public GetPrivDetailRsp(java.util.Map<String, PrivInfo> mPrivInfo)
    {
        this.mPrivInfo = mPrivInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mPrivInfo) {
            ostream.writeMap(0, mPrivInfo);
        }
    }

    static java.util.Map<String, PrivInfo> VAR_TYPE_4_MPRIVINFO = new java.util.HashMap<String, PrivInfo>();
    static {
        VAR_TYPE_4_MPRIVINFO.put("", new PrivInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mPrivInfo = (java.util.Map<String, PrivInfo>)istream.readMap(0, false, VAR_TYPE_4_MPRIVINFO);
    }

}

