package BEC;

public final class FeedItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FeedContent stFeedContent = null;

    public BEC.FeedExtendInfo stFeedExtendInfo = null;

    public BEC.FeedCommentInfoList stFeedCommentInfoList = null;

    public BEC.FeedContent getStFeedContent()
    {
        return stFeedContent;
    }

    public void  setStFeedContent(BEC.FeedContent stFeedContent)
    {
        this.stFeedContent = stFeedContent;
    }

    public BEC.FeedExtendInfo getStFeedExtendInfo()
    {
        return stFeedExtendInfo;
    }

    public void  setStFeedExtendInfo(BEC.FeedExtendInfo stFeedExtendInfo)
    {
        this.stFeedExtendInfo = stFeedExtendInfo;
    }

    public BEC.FeedCommentInfoList getStFeedCommentInfoList()
    {
        return stFeedCommentInfoList;
    }

    public void  setStFeedCommentInfoList(BEC.FeedCommentInfoList stFeedCommentInfoList)
    {
        this.stFeedCommentInfoList = stFeedCommentInfoList;
    }

    public FeedItem()
    {
    }

    public FeedItem(BEC.FeedContent stFeedContent, BEC.FeedExtendInfo stFeedExtendInfo, BEC.FeedCommentInfoList stFeedCommentInfoList)
    {
        this.stFeedContent = stFeedContent;
        this.stFeedExtendInfo = stFeedExtendInfo;
        this.stFeedCommentInfoList = stFeedCommentInfoList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFeedContent) {
            ostream.writeMessage(0, stFeedContent);
        }
        if (null != stFeedExtendInfo) {
            ostream.writeMessage(1, stFeedExtendInfo);
        }
        if (null != stFeedCommentInfoList) {
            ostream.writeMessage(2, stFeedCommentInfoList);
        }
    }

    static BEC.FeedContent VAR_TYPE_4_STFEEDCONTENT = new BEC.FeedContent();

    static BEC.FeedExtendInfo VAR_TYPE_4_STFEEDEXTENDINFO = new BEC.FeedExtendInfo();

    static BEC.FeedCommentInfoList VAR_TYPE_4_STFEEDCOMMENTINFOLIST = new BEC.FeedCommentInfoList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFeedContent = (BEC.FeedContent)istream.readMessage(0, false, VAR_TYPE_4_STFEEDCONTENT);
        this.stFeedExtendInfo = (BEC.FeedExtendInfo)istream.readMessage(1, false, VAR_TYPE_4_STFEEDEXTENDINFO);
        this.stFeedCommentInfoList = (BEC.FeedCommentInfoList)istream.readMessage(2, false, VAR_TYPE_4_STFEEDCOMMENTINFOLIST);
    }

}

