package BEC;

public final class GetPrivBreifInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, String> mPrivBreifInfo = null;

    public java.util.Map<String, String> getMPrivBreifInfo()
    {
        return mPrivBreifInfo;
    }

    public void  setMPrivBreifInfo(java.util.Map<String, String> mPrivBreifInfo)
    {
        this.mPrivBreifInfo = mPrivBreifInfo;
    }

    public GetPrivBreifInfoRsp()
    {
    }

    public GetPrivBreifInfoRsp(java.util.Map<String, String> mPrivBreifInfo)
    {
        this.mPrivBreifInfo = mPrivBreifInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mPrivBreifInfo) {
            ostream.writeMap(0, mPrivBreifInfo);
        }
    }

    static java.util.Map<String, String> VAR_TYPE_4_MPRIVBREIFINFO = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_MPRIVBREIFINFO.put("", "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mPrivBreifInfo = (java.util.Map<String, String>)istream.readMap(0, false, VAR_TYPE_4_MPRIVBREIFINFO);
    }

}

