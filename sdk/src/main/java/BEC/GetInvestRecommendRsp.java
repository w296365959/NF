package BEC;

public final class GetInvestRecommendRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo = null;

    public java.util.ArrayList<BEC.FeedUserBaseInfo> getVFeedUserBaseInfo()
    {
        return vFeedUserBaseInfo;
    }

    public void  setVFeedUserBaseInfo(java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo)
    {
        this.vFeedUserBaseInfo = vFeedUserBaseInfo;
    }

    public GetInvestRecommendRsp()
    {
    }

    public GetInvestRecommendRsp(java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo)
    {
        this.vFeedUserBaseInfo = vFeedUserBaseInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vFeedUserBaseInfo) {
            ostream.writeList(0, vFeedUserBaseInfo);
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

        this.vFeedUserBaseInfo = (java.util.ArrayList<BEC.FeedUserBaseInfo>)istream.readList(0, false, VAR_TYPE_4_VFEEDUSERBASEINFO);
    }

}

