package BEC;

public final class GetFeedUserInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FeedUserBaseInfo stFeedUserBaseInfo = null;

    public BEC.FeedUserBaseInfo getStFeedUserBaseInfo()
    {
        return stFeedUserBaseInfo;
    }

    public void  setStFeedUserBaseInfo(BEC.FeedUserBaseInfo stFeedUserBaseInfo)
    {
        this.stFeedUserBaseInfo = stFeedUserBaseInfo;
    }

    public GetFeedUserInfoRsp()
    {
    }

    public GetFeedUserInfoRsp(BEC.FeedUserBaseInfo stFeedUserBaseInfo)
    {
        this.stFeedUserBaseInfo = stFeedUserBaseInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFeedUserBaseInfo) {
            ostream.writeMessage(0, stFeedUserBaseInfo);
        }
    }

    static BEC.FeedUserBaseInfo VAR_TYPE_4_STFEEDUSERBASEINFO = new BEC.FeedUserBaseInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFeedUserBaseInfo = (BEC.FeedUserBaseInfo)istream.readMessage(0, false, VAR_TYPE_4_STFEEDUSERBASEINFO);
    }

}

