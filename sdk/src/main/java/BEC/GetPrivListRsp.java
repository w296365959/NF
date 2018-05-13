package BEC;

public final class GetPrivListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, java.util.ArrayList<PrivInfo>> mPrivInfo = null;

    public java.util.Map<String, java.util.ArrayList<PrivInfo>> getMPrivInfo()
    {
        return mPrivInfo;
    }

    public void  setMPrivInfo(java.util.Map<String, java.util.ArrayList<PrivInfo>> mPrivInfo)
    {
        this.mPrivInfo = mPrivInfo;
    }

    public GetPrivListRsp()
    {
    }

    public GetPrivListRsp(java.util.Map<String, java.util.ArrayList<PrivInfo>> mPrivInfo)
    {
        this.mPrivInfo = mPrivInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mPrivInfo) {
            ostream.writeMap(1, mPrivInfo);
        }
    }

    static java.util.Map<String, java.util.ArrayList<PrivInfo>> VAR_TYPE_4_MPRIVINFO = new java.util.HashMap<String, java.util.ArrayList<PrivInfo>>();
    static {
        java.util.ArrayList<PrivInfo> VAR_TYPE_4_MPRIVINFO_V_C = new java.util.ArrayList<PrivInfo>();
        VAR_TYPE_4_MPRIVINFO_V_C.add(new PrivInfo());
        VAR_TYPE_4_MPRIVINFO.put("", VAR_TYPE_4_MPRIVINFO_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mPrivInfo = (java.util.Map<String, java.util.ArrayList<PrivInfo>>)istream.readMap(1, false, VAR_TYPE_4_MPRIVINFO);
    }

}

