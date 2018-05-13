package BEC;

public final class DoAccessCountReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sFeedId = "";

    public int eType = BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

    public String sFeedTitle = "";

    public long iPubAccountId = 0;

    public String sPubNickName = "";

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

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public String getSFeedTitle()
    {
        return sFeedTitle;
    }

    public void  setSFeedTitle(String sFeedTitle)
    {
        this.sFeedTitle = sFeedTitle;
    }

    public long getIPubAccountId()
    {
        return iPubAccountId;
    }

    public void  setIPubAccountId(long iPubAccountId)
    {
        this.iPubAccountId = iPubAccountId;
    }

    public String getSPubNickName()
    {
        return sPubNickName;
    }

    public void  setSPubNickName(String sPubNickName)
    {
        this.sPubNickName = sPubNickName;
    }

    public DoAccessCountReq()
    {
    }

    public DoAccessCountReq(BEC.UserInfo stUserInfo, String sFeedId, int eType, String sFeedTitle, long iPubAccountId, String sPubNickName)
    {
        this.stUserInfo = stUserInfo;
        this.sFeedId = sFeedId;
        this.eType = eType;
        this.sFeedTitle = sFeedTitle;
        this.iPubAccountId = iPubAccountId;
        this.sPubNickName = sPubNickName;
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
        ostream.writeInt32(2, eType);
        if (null != sFeedTitle) {
            ostream.writeString(3, sFeedTitle);
        }
        ostream.writeUInt32(4, iPubAccountId);
        if (null != sPubNickName) {
            ostream.writeString(5, sPubNickName);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sFeedId = (String)istream.readString(1, false, this.sFeedId);
        this.eType = (int)istream.readInt32(2, false, this.eType);
        this.sFeedTitle = (String)istream.readString(3, false, this.sFeedTitle);
        this.iPubAccountId = (long)istream.readUInt32(4, false, this.iPubAccountId);
        this.sPubNickName = (String)istream.readString(5, false, this.sPubNickName);
    }

}

