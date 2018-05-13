package BEC;

public final class GetCommentInfoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sFeedId = "";

    public int iDirection = 0;

    public String sStartId = "";

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public int getIDirection()
    {
        return iDirection;
    }

    public void  setIDirection(int iDirection)
    {
        this.iDirection = iDirection;
    }

    public String getSStartId()
    {
        return sStartId;
    }

    public void  setSStartId(String sStartId)
    {
        this.sStartId = sStartId;
    }

    public GetCommentInfoReq()
    {
    }

    public GetCommentInfoReq(BEC.UserInfo stUserInfo, String sFeedId, int iDirection, String sStartId)
    {
        this.stUserInfo = stUserInfo;
        this.sFeedId = sFeedId;
        this.iDirection = iDirection;
        this.sStartId = sStartId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sFeedId) {
            ostream.writeString(1, sFeedId);
        }
        ostream.writeInt32(2, iDirection);
        if (null != sStartId) {
            ostream.writeString(3, sStartId);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sFeedId = (String)istream.readString(1, false, this.sFeedId);
        this.iDirection = (int)istream.readInt32(2, false, this.iDirection);
        this.sStartId = (String)istream.readString(3, false, this.sStartId);
    }

}

