package BEC;

public final class PoCommentReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sFeedId = "";

    public String sContent = "";

    public BEC.ReplyCommentInfo stReplyComment = null;

    public BEC.AccountTicket stAccountTicket = null;

    public String sCommentNickName = "";

    public String sClientCommentId = "";

    public int eFeedType = BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

    public String sCommentId = "";

    public int eFeedSourceType = BEC.E_FEED_SOURCE_TYPE.E_FEED_SOURCE_USER;

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

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public BEC.ReplyCommentInfo getStReplyComment()
    {
        return stReplyComment;
    }

    public void  setStReplyComment(BEC.ReplyCommentInfo stReplyComment)
    {
        this.stReplyComment = stReplyComment;
    }

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public String getSCommentNickName()
    {
        return sCommentNickName;
    }

    public void  setSCommentNickName(String sCommentNickName)
    {
        this.sCommentNickName = sCommentNickName;
    }

    public String getSClientCommentId()
    {
        return sClientCommentId;
    }

    public void  setSClientCommentId(String sClientCommentId)
    {
        this.sClientCommentId = sClientCommentId;
    }

    public int getEFeedType()
    {
        return eFeedType;
    }

    public void  setEFeedType(int eFeedType)
    {
        this.eFeedType = eFeedType;
    }

    public String getSCommentId()
    {
        return sCommentId;
    }

    public void  setSCommentId(String sCommentId)
    {
        this.sCommentId = sCommentId;
    }

    public int getEFeedSourceType()
    {
        return eFeedSourceType;
    }

    public void  setEFeedSourceType(int eFeedSourceType)
    {
        this.eFeedSourceType = eFeedSourceType;
    }

    public PoCommentReq()
    {
    }

    public PoCommentReq(BEC.UserInfo stUserInfo, String sFeedId, String sContent, BEC.ReplyCommentInfo stReplyComment, BEC.AccountTicket stAccountTicket, String sCommentNickName, String sClientCommentId, int eFeedType, String sCommentId, int eFeedSourceType)
    {
        this.stUserInfo = stUserInfo;
        this.sFeedId = sFeedId;
        this.sContent = sContent;
        this.stReplyComment = stReplyComment;
        this.stAccountTicket = stAccountTicket;
        this.sCommentNickName = sCommentNickName;
        this.sClientCommentId = sClientCommentId;
        this.eFeedType = eFeedType;
        this.sCommentId = sCommentId;
        this.eFeedSourceType = eFeedSourceType;
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
        if (null != sContent) {
            ostream.writeString(2, sContent);
        }
        if (null != stReplyComment) {
            ostream.writeMessage(3, stReplyComment);
        }
        if (null != stAccountTicket) {
            ostream.writeMessage(4, stAccountTicket);
        }
        if (null != sCommentNickName) {
            ostream.writeString(5, sCommentNickName);
        }
        if (null != sClientCommentId) {
            ostream.writeString(6, sClientCommentId);
        }
        ostream.writeInt32(7, eFeedType);
        if (null != sCommentId) {
            ostream.writeString(8, sCommentId);
        }
        ostream.writeInt32(9, eFeedSourceType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.ReplyCommentInfo VAR_TYPE_4_STREPLYCOMMENT = new BEC.ReplyCommentInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sFeedId = (String)istream.readString(1, false, this.sFeedId);
        this.sContent = (String)istream.readString(2, false, this.sContent);
        this.stReplyComment = (BEC.ReplyCommentInfo)istream.readMessage(3, false, VAR_TYPE_4_STREPLYCOMMENT);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(4, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sCommentNickName = (String)istream.readString(5, false, this.sCommentNickName);
        this.sClientCommentId = (String)istream.readString(6, false, this.sClientCommentId);
        this.eFeedType = (int)istream.readInt32(7, false, this.eFeedType);
        this.sCommentId = (String)istream.readString(8, false, this.sCommentId);
        this.eFeedSourceType = (int)istream.readInt32(9, false, this.eFeedSourceType);
    }

}

