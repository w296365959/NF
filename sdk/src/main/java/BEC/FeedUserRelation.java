package BEC;

public final class FeedUserRelation extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eFeedUserRelationType = BEC.E_FEED_USER_RELATION_TYPE.E_FURT_FOLLOWER;

    public java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo = null;

    public int getEFeedUserRelationType()
    {
        return eFeedUserRelationType;
    }

    public void  setEFeedUserRelationType(int eFeedUserRelationType)
    {
        this.eFeedUserRelationType = eFeedUserRelationType;
    }

    public java.util.ArrayList<BEC.FeedUserBaseInfo> getVFeedUserBaseInfo()
    {
        return vFeedUserBaseInfo;
    }

    public void  setVFeedUserBaseInfo(java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo)
    {
        this.vFeedUserBaseInfo = vFeedUserBaseInfo;
    }

    public FeedUserRelation()
    {
    }

    public FeedUserRelation(int eFeedUserRelationType, java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo)
    {
        this.eFeedUserRelationType = eFeedUserRelationType;
        this.vFeedUserBaseInfo = vFeedUserBaseInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eFeedUserRelationType);
        if (null != vFeedUserBaseInfo) {
            ostream.writeList(1, vFeedUserBaseInfo);
        }
    }

    static java.util.ArrayList<BEC.FeedUserBaseInfo> VAR_TYPE_4_VFEEDUSERBASEINFO = new java.util.ArrayList<BEC.FeedUserBaseInfo>();
    static {
        VAR_TYPE_4_VFEEDUSERBASEINFO.add(new BEC.FeedUserBaseInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eFeedUserRelationType = (int)istream.readInt32(0, false, this.eFeedUserRelationType);
        this.vFeedUserBaseInfo = (java.util.ArrayList<BEC.FeedUserBaseInfo>)istream.readList(1, false, VAR_TYPE_4_VFEEDUSERBASEINFO);
    }

}

