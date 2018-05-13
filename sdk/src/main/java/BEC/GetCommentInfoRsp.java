package BEC;

public final class GetCommentInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FeedCommentInfo> vCommentList = null;

    public java.util.Map<Long, BEC.FeedUserBaseInfo> mFeedUserBaseInfo = null;

    public int iStatus = 0;

    public int iTotal = 0;

    public java.util.ArrayList<BEC.FeedCommentInfo> getVCommentList()
    {
        return vCommentList;
    }

    public void  setVCommentList(java.util.ArrayList<BEC.FeedCommentInfo> vCommentList)
    {
        this.vCommentList = vCommentList;
    }

    public java.util.Map<Long, BEC.FeedUserBaseInfo> getMFeedUserBaseInfo()
    {
        return mFeedUserBaseInfo;
    }

    public void  setMFeedUserBaseInfo(java.util.Map<Long, BEC.FeedUserBaseInfo> mFeedUserBaseInfo)
    {
        this.mFeedUserBaseInfo = mFeedUserBaseInfo;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public int getITotal()
    {
        return iTotal;
    }

    public void  setITotal(int iTotal)
    {
        this.iTotal = iTotal;
    }

    public GetCommentInfoRsp()
    {
    }

    public GetCommentInfoRsp(java.util.ArrayList<BEC.FeedCommentInfo> vCommentList, java.util.Map<Long, BEC.FeedUserBaseInfo> mFeedUserBaseInfo, int iStatus, int iTotal)
    {
        this.vCommentList = vCommentList;
        this.mFeedUserBaseInfo = mFeedUserBaseInfo;
        this.iStatus = iStatus;
        this.iTotal = iTotal;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCommentList) {
            ostream.writeList(0, vCommentList);
        }
        if (null != mFeedUserBaseInfo) {
            ostream.writeMap(1, mFeedUserBaseInfo);
        }
        ostream.writeInt32(2, iStatus);
        ostream.writeInt32(3, iTotal);
    }

    static java.util.ArrayList<BEC.FeedCommentInfo> VAR_TYPE_4_VCOMMENTLIST = new java.util.ArrayList<BEC.FeedCommentInfo>();
    static {
        VAR_TYPE_4_VCOMMENTLIST.add(new BEC.FeedCommentInfo());
    }

    static java.util.Map<Long, BEC.FeedUserBaseInfo> VAR_TYPE_4_MFEEDUSERBASEINFO = new java.util.HashMap<Long, BEC.FeedUserBaseInfo>();
    static {
        VAR_TYPE_4_MFEEDUSERBASEINFO.put(0L, new BEC.FeedUserBaseInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCommentList = (java.util.ArrayList<BEC.FeedCommentInfo>)istream.readList(0, false, VAR_TYPE_4_VCOMMENTLIST);
        this.mFeedUserBaseInfo = (java.util.Map<Long, BEC.FeedUserBaseInfo>)istream.readMap(1, false, VAR_TYPE_4_MFEEDUSERBASEINFO);
        this.iStatus = (int)istream.readInt32(2, false, this.iStatus);
        this.iTotal = (int)istream.readInt32(3, false, this.iTotal);
    }

}

