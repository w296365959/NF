package BEC;

public final class VodVideoListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sStartVideoId = "";

    public String sEndVideoId = "";

    public java.util.ArrayList<BEC.VideoFilter> vtFilterCondition = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSStartVideoId()
    {
        return sStartVideoId;
    }

    public void  setSStartVideoId(String sStartVideoId)
    {
        this.sStartVideoId = sStartVideoId;
    }

    public String getSEndVideoId()
    {
        return sEndVideoId;
    }

    public void  setSEndVideoId(String sEndVideoId)
    {
        this.sEndVideoId = sEndVideoId;
    }

    public java.util.ArrayList<BEC.VideoFilter> getVtFilterCondition()
    {
        return vtFilterCondition;
    }

    public void  setVtFilterCondition(java.util.ArrayList<BEC.VideoFilter> vtFilterCondition)
    {
        this.vtFilterCondition = vtFilterCondition;
    }

    public VodVideoListReq()
    {
    }

    public VodVideoListReq(BEC.UserInfo stUserInfo, String sStartVideoId, String sEndVideoId, java.util.ArrayList<BEC.VideoFilter> vtFilterCondition)
    {
        this.stUserInfo = stUserInfo;
        this.sStartVideoId = sStartVideoId;
        this.sEndVideoId = sEndVideoId;
        this.vtFilterCondition = vtFilterCondition;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sStartVideoId) {
            ostream.writeString(1, sStartVideoId);
        }
        if (null != sEndVideoId) {
            ostream.writeString(2, sEndVideoId);
        }
        if (null != vtFilterCondition) {
            ostream.writeList(3, vtFilterCondition);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<BEC.VideoFilter> VAR_TYPE_4_VTFILTERCONDITION = new java.util.ArrayList<BEC.VideoFilter>();
    static {
        VAR_TYPE_4_VTFILTERCONDITION.add(new BEC.VideoFilter());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sStartVideoId = (String)istream.readString(1, false, this.sStartVideoId);
        this.sEndVideoId = (String)istream.readString(2, false, this.sEndVideoId);
        this.vtFilterCondition = (java.util.ArrayList<BEC.VideoFilter>)istream.readList(3, false, VAR_TYPE_4_VTFILTERCONDITION);
    }

}

