package BEC;

public final class FeedCommentInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFeedId = "";

    public long iPubAccountId = 0;

    public String sCommentId = "";

    public String sContent = "";

    public int iPubTime = 0;

    public BEC.ReplyCommentInfo stReplyComment = null;

    public String sCommentNickName = "";

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public long getIPubAccountId()
    {
        return iPubAccountId;
    }

    public void  setIPubAccountId(long iPubAccountId)
    {
        this.iPubAccountId = iPubAccountId;
    }

    public String getSCommentId()
    {
        return sCommentId;
    }

    public void  setSCommentId(String sCommentId)
    {
        this.sCommentId = sCommentId;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public int getIPubTime()
    {
        return iPubTime;
    }

    public void  setIPubTime(int iPubTime)
    {
        this.iPubTime = iPubTime;
    }

    public BEC.ReplyCommentInfo getStReplyComment()
    {
        return stReplyComment;
    }

    public void  setStReplyComment(BEC.ReplyCommentInfo stReplyComment)
    {
        this.stReplyComment = stReplyComment;
    }

    public String getSCommentNickName()
    {
        return sCommentNickName;
    }

    public void  setSCommentNickName(String sCommentNickName)
    {
        this.sCommentNickName = sCommentNickName;
    }

    public FeedCommentInfo()
    {
    }

    public FeedCommentInfo(String sFeedId, long iPubAccountId, String sCommentId, String sContent, int iPubTime, BEC.ReplyCommentInfo stReplyComment, String sCommentNickName)
    {
        this.sFeedId = sFeedId;
        this.iPubAccountId = iPubAccountId;
        this.sCommentId = sCommentId;
        this.sContent = sContent;
        this.iPubTime = iPubTime;
        this.stReplyComment = stReplyComment;
        this.sCommentNickName = sCommentNickName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFeedId) {
            ostream.writeString(0, sFeedId);
        }
        ostream.writeUInt32(1, iPubAccountId);
        if (null != sCommentId) {
            ostream.writeString(2, sCommentId);
        }
        if (null != sContent) {
            ostream.writeString(3, sContent);
        }
        ostream.writeInt32(4, iPubTime);
        if (null != stReplyComment) {
            ostream.writeMessage(5, stReplyComment);
        }
        if (null != sCommentNickName) {
            ostream.writeString(6, sCommentNickName);
        }
    }

    static BEC.ReplyCommentInfo VAR_TYPE_4_STREPLYCOMMENT = new BEC.ReplyCommentInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFeedId = (String)istream.readString(0, false, this.sFeedId);
        this.iPubAccountId = (long)istream.readUInt32(1, false, this.iPubAccountId);
        this.sCommentId = (String)istream.readString(2, false, this.sCommentId);
        this.sContent = (String)istream.readString(3, false, this.sContent);
        this.iPubTime = (int)istream.readInt32(4, false, this.iPubTime);
        this.stReplyComment = (BEC.ReplyCommentInfo)istream.readMessage(5, false, VAR_TYPE_4_STREPLYCOMMENT);
        this.sCommentNickName = (String)istream.readString(6, false, this.sCommentNickName);
    }

}

