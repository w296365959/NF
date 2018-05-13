package BEC;

public final class GetFeedRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FeedItem stFeedItem = null;

    public BEC.FeedUserBaseInfo stFeedUserBaseInfo = null;

    public BEC.FeedItem getStFeedItem()
    {
        return stFeedItem;
    }

    public void  setStFeedItem(BEC.FeedItem stFeedItem)
    {
        this.stFeedItem = stFeedItem;
    }

    public BEC.FeedUserBaseInfo getStFeedUserBaseInfo()
    {
        return stFeedUserBaseInfo;
    }

    public void  setStFeedUserBaseInfo(BEC.FeedUserBaseInfo stFeedUserBaseInfo)
    {
        this.stFeedUserBaseInfo = stFeedUserBaseInfo;
    }

    public GetFeedRsp()
    {
    }

    public GetFeedRsp(BEC.FeedItem stFeedItem, BEC.FeedUserBaseInfo stFeedUserBaseInfo)
    {
        this.stFeedItem = stFeedItem;
        this.stFeedUserBaseInfo = stFeedUserBaseInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFeedItem) {
            ostream.writeMessage(0, stFeedItem);
        }
        if (null != stFeedUserBaseInfo) {
            ostream.writeMessage(1, stFeedUserBaseInfo);
        }
    }

    static BEC.FeedItem VAR_TYPE_4_STFEEDITEM = new BEC.FeedItem();

    static BEC.FeedUserBaseInfo VAR_TYPE_4_STFEEDUSERBASEINFO = new BEC.FeedUserBaseInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFeedItem = (BEC.FeedItem)istream.readMessage(0, false, VAR_TYPE_4_STFEEDITEM);
        this.stFeedUserBaseInfo = (BEC.FeedUserBaseInfo)istream.readMessage(1, false, VAR_TYPE_4_STFEEDUSERBASEINFO);
    }

}

