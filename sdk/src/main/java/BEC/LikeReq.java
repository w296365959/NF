package BEC;

public final class LikeReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public String sFeedId = "";

    public boolean isAdd = true;

    public int eFeedType = BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public boolean getIsAdd()
    {
        return isAdd;
    }

    public void  setIsAdd(boolean isAdd)
    {
        this.isAdd = isAdd;
    }

    public int getEFeedType()
    {
        return eFeedType;
    }

    public void  setEFeedType(int eFeedType)
    {
        this.eFeedType = eFeedType;
    }

    public LikeReq()
    {
    }

    public LikeReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, String sFeedId, boolean isAdd, int eFeedType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.sFeedId = sFeedId;
        this.isAdd = isAdd;
        this.eFeedType = eFeedType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stAccountTicket) {
            ostream.writeMessage(1, stAccountTicket);
        }
        if (null != sFeedId) {
            ostream.writeString(2, sFeedId);
        }
        ostream.writeBoolean(3, isAdd);
        ostream.writeInt32(4, eFeedType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sFeedId = (String)istream.readString(2, false, this.sFeedId);
        this.isAdd = (boolean)istream.readBoolean(3, false, this.isAdd);
        this.eFeedType = (int)istream.readInt32(4, false, this.eFeedType);
    }

}

