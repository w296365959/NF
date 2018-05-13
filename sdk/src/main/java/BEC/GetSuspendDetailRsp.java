package BEC;

public final class GetSuspendDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, SuspendDetailInfo> mDetailInfo = null;

    public java.util.Map<String, SuspendDetailInfo> getMDetailInfo()
    {
        return mDetailInfo;
    }

    public void  setMDetailInfo(java.util.Map<String, SuspendDetailInfo> mDetailInfo)
    {
        this.mDetailInfo = mDetailInfo;
    }

    public GetSuspendDetailRsp()
    {
    }

    public GetSuspendDetailRsp(java.util.Map<String, SuspendDetailInfo> mDetailInfo)
    {
        this.mDetailInfo = mDetailInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mDetailInfo) {
            ostream.writeMap(0, mDetailInfo);
        }
    }

    static java.util.Map<String, SuspendDetailInfo> VAR_TYPE_4_MDETAILINFO = new java.util.HashMap<String, SuspendDetailInfo>();
    static {
        VAR_TYPE_4_MDETAILINFO.put("", new SuspendDetailInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mDetailInfo = (java.util.Map<String, SuspendDetailInfo>)istream.readMap(0, false, VAR_TYPE_4_MDETAILINFO);
    }

}

