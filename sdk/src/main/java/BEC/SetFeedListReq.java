package BEC;

public final class SetFeedListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sFeedId = "";

    public int eFeedListType = 0;

    public String sKey = "";

    public int eSetFeedListType = 0;

    public int eFeedType = BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

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

    public int getEFeedListType()
    {
        return eFeedListType;
    }

    public void  setEFeedListType(int eFeedListType)
    {
        this.eFeedListType = eFeedListType;
    }

    public String getSKey()
    {
        return sKey;
    }

    public void  setSKey(String sKey)
    {
        this.sKey = sKey;
    }

    public int getESetFeedListType()
    {
        return eSetFeedListType;
    }

    public void  setESetFeedListType(int eSetFeedListType)
    {
        this.eSetFeedListType = eSetFeedListType;
    }

    public int getEFeedType()
    {
        return eFeedType;
    }

    public void  setEFeedType(int eFeedType)
    {
        this.eFeedType = eFeedType;
    }

    public SetFeedListReq()
    {
    }

    public SetFeedListReq(BEC.UserInfo stUserInfo, String sFeedId, int eFeedListType, String sKey, int eSetFeedListType, int eFeedType)
    {
        this.stUserInfo = stUserInfo;
        this.sFeedId = sFeedId;
        this.eFeedListType = eFeedListType;
        this.sKey = sKey;
        this.eSetFeedListType = eSetFeedListType;
        this.eFeedType = eFeedType;
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
        ostream.writeInt32(2, eFeedListType);
        if (null != sKey) {
            ostream.writeString(3, sKey);
        }
        ostream.writeInt32(4, eSetFeedListType);
        ostream.writeInt32(5, eFeedType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sFeedId = (String)istream.readString(1, false, this.sFeedId);
        this.eFeedListType = (int)istream.readInt32(2, false, this.eFeedListType);
        this.sKey = (String)istream.readString(3, false, this.sKey);
        this.eSetFeedListType = (int)istream.readInt32(4, false, this.eSetFeedListType);
        this.eFeedType = (int)istream.readInt32(5, false, this.eFeedType);
    }

}

