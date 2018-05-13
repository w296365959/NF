package BEC;

public final class GetUserRelationReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eFeedUserRelationType = BEC.E_FEED_USER_RELATION_TYPE.E_FURT_FOLLOWER;

    public int iDirection = 0;

    public String sStartId = "";

    public BEC.AccountTicket stAccountTicket = null;

    public long iOtherAccountId = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEFeedUserRelationType()
    {
        return eFeedUserRelationType;
    }

    public void  setEFeedUserRelationType(int eFeedUserRelationType)
    {
        this.eFeedUserRelationType = eFeedUserRelationType;
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

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public long getIOtherAccountId()
    {
        return iOtherAccountId;
    }

    public void  setIOtherAccountId(long iOtherAccountId)
    {
        this.iOtherAccountId = iOtherAccountId;
    }

    public GetUserRelationReq()
    {
    }

    public GetUserRelationReq(BEC.UserInfo stUserInfo, int eFeedUserRelationType, int iDirection, String sStartId, BEC.AccountTicket stAccountTicket, long iOtherAccountId)
    {
        this.stUserInfo = stUserInfo;
        this.eFeedUserRelationType = eFeedUserRelationType;
        this.iDirection = iDirection;
        this.sStartId = sStartId;
        this.stAccountTicket = stAccountTicket;
        this.iOtherAccountId = iOtherAccountId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eFeedUserRelationType);
        ostream.writeInt32(2, iDirection);
        if (null != sStartId) {
            ostream.writeString(3, sStartId);
        }
        if (null != stAccountTicket) {
            ostream.writeMessage(4, stAccountTicket);
        }
        ostream.writeUInt32(5, iOtherAccountId);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eFeedUserRelationType = (int)istream.readInt32(1, false, this.eFeedUserRelationType);
        this.iDirection = (int)istream.readInt32(2, false, this.iDirection);
        this.sStartId = (String)istream.readString(3, false, this.sStartId);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(4, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iOtherAccountId = (long)istream.readUInt32(5, false, this.iOtherAccountId);
    }

}

