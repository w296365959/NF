package BEC;

public final class DelCommentReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sCommentId = "";

    public String sFeedId = "";

    public BEC.AccountTicket stAccountTicket = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSCommentId()
    {
        return sCommentId;
    }

    public void  setSCommentId(String sCommentId)
    {
        this.sCommentId = sCommentId;
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

    public DelCommentReq()
    {
    }

    public DelCommentReq(BEC.UserInfo stUserInfo, String sCommentId, String sFeedId, BEC.AccountTicket stAccountTicket)
    {
        this.stUserInfo = stUserInfo;
        this.sCommentId = sCommentId;
        this.sFeedId = sFeedId;
        this.stAccountTicket = stAccountTicket;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sCommentId) {
            ostream.writeString(1, sCommentId);
        }
        if (null != sFeedId) {
            ostream.writeString(2, sFeedId);
        }
        if (null != stAccountTicket) {
            ostream.writeMessage(3, stAccountTicket);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sCommentId = (String)istream.readString(1, false, this.sCommentId);
        this.sFeedId = (String)istream.readString(2, false, this.sFeedId);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(3, false, VAR_TYPE_4_STACCOUNTTICKET);
    }

}

