package BEC;

public final class DelFeedReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sFeedId = "";

    public BEC.AccountTicket stAccountTicket = null;

    public String sDtSecCode = "";

    public int eSetFeedListType = 0;

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

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getESetFeedListType()
    {
        return eSetFeedListType;
    }

    public void  setESetFeedListType(int eSetFeedListType)
    {
        this.eSetFeedListType = eSetFeedListType;
    }

    public DelFeedReq()
    {
    }

    public DelFeedReq(BEC.UserInfo stUserInfo, String sFeedId, BEC.AccountTicket stAccountTicket, String sDtSecCode, int eSetFeedListType)
    {
        this.stUserInfo = stUserInfo;
        this.sFeedId = sFeedId;
        this.stAccountTicket = stAccountTicket;
        this.sDtSecCode = sDtSecCode;
        this.eSetFeedListType = eSetFeedListType;
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
        if (null != stAccountTicket) {
            ostream.writeMessage(2, stAccountTicket);
        }
        if (null != sDtSecCode) {
            ostream.writeString(3, sDtSecCode);
        }
        ostream.writeInt32(4, eSetFeedListType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sFeedId = (String)istream.readString(1, false, this.sFeedId);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(2, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sDtSecCode = (String)istream.readString(3, false, this.sDtSecCode);
        this.eSetFeedListType = (int)istream.readInt32(4, false, this.eSetFeedListType);
    }

}

