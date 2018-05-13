package BEC;

public final class GetFeedListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eFeedGroupType = BEC.E_FEED_GROUP_TYPE.E_FGT_ALL;

    public int iDirection = 0;

    public String sStartFeedId = "";

    public String sDtSecCode = "";

    public int eSelfType = BEC.E_FEED_TYPE.E_FT_ALL;

    public long iOtherAccountId = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEFeedGroupType()
    {
        return eFeedGroupType;
    }

    public void  setEFeedGroupType(int eFeedGroupType)
    {
        this.eFeedGroupType = eFeedGroupType;
    }

    public int getIDirection()
    {
        return iDirection;
    }

    public void  setIDirection(int iDirection)
    {
        this.iDirection = iDirection;
    }

    public String getSStartFeedId()
    {
        return sStartFeedId;
    }

    public void  setSStartFeedId(String sStartFeedId)
    {
        this.sStartFeedId = sStartFeedId;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getESelfType()
    {
        return eSelfType;
    }

    public void  setESelfType(int eSelfType)
    {
        this.eSelfType = eSelfType;
    }

    public long getIOtherAccountId()
    {
        return iOtherAccountId;
    }

    public void  setIOtherAccountId(long iOtherAccountId)
    {
        this.iOtherAccountId = iOtherAccountId;
    }

    public GetFeedListReq()
    {
    }

    public GetFeedListReq(BEC.UserInfo stUserInfo, int eFeedGroupType, int iDirection, String sStartFeedId, String sDtSecCode, int eSelfType, long iOtherAccountId)
    {
        this.stUserInfo = stUserInfo;
        this.eFeedGroupType = eFeedGroupType;
        this.iDirection = iDirection;
        this.sStartFeedId = sStartFeedId;
        this.sDtSecCode = sDtSecCode;
        this.eSelfType = eSelfType;
        this.iOtherAccountId = iOtherAccountId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eFeedGroupType);
        ostream.writeInt32(2, iDirection);
        if (null != sStartFeedId) {
            ostream.writeString(3, sStartFeedId);
        }
        if (null != sDtSecCode) {
            ostream.writeString(4, sDtSecCode);
        }
        ostream.writeInt32(5, eSelfType);
        ostream.writeUInt32(6, iOtherAccountId);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eFeedGroupType = (int)istream.readInt32(1, false, this.eFeedGroupType);
        this.iDirection = (int)istream.readInt32(2, false, this.iDirection);
        this.sStartFeedId = (String)istream.readString(3, false, this.sStartFeedId);
        this.sDtSecCode = (String)istream.readString(4, false, this.sDtSecCode);
        this.eSelfType = (int)istream.readInt32(5, false, this.eSelfType);
        this.iOtherAccountId = (long)istream.readUInt32(6, false, this.iOtherAccountId);
    }

}

