package BEC;

public final class LiveVideoStateReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<String> vVideoId = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<String> getVVideoId()
    {
        return vVideoId;
    }

    public void  setVVideoId(java.util.ArrayList<String> vVideoId)
    {
        this.vVideoId = vVideoId;
    }

    public LiveVideoStateReq()
    {
    }

    public LiveVideoStateReq(BEC.UserInfo stUserInfo, java.util.ArrayList<String> vVideoId)
    {
        this.stUserInfo = stUserInfo;
        this.vVideoId = vVideoId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vVideoId) {
            ostream.writeList(1, vVideoId);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<String> VAR_TYPE_4_VVIDEOID = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VVIDEOID.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vVideoId = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VVIDEOID);
    }

}

