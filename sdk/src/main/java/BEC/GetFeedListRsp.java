package BEC;

public final class GetFeedListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FeedItem> vFeedItem = null;

    public java.util.Map<Long, BEC.FeedUserBaseInfo> mFeedUserBaseInfo = null;

    public int iStatus = 0;

    public int iTotal = 0;

    public java.util.ArrayList<BEC.FeedItem> vTopFeedItem = null;

    public java.util.ArrayList<BEC.FeedItem> getVFeedItem()
    {
        return vFeedItem;
    }

    public void  setVFeedItem(java.util.ArrayList<BEC.FeedItem> vFeedItem)
    {
        this.vFeedItem = vFeedItem;
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

    public java.util.ArrayList<BEC.FeedItem> getVTopFeedItem()
    {
        return vTopFeedItem;
    }

    public void  setVTopFeedItem(java.util.ArrayList<BEC.FeedItem> vTopFeedItem)
    {
        this.vTopFeedItem = vTopFeedItem;
    }

    public GetFeedListRsp()
    {
    }

    public GetFeedListRsp(java.util.ArrayList<BEC.FeedItem> vFeedItem, java.util.Map<Long, BEC.FeedUserBaseInfo> mFeedUserBaseInfo, int iStatus, int iTotal, java.util.ArrayList<BEC.FeedItem> vTopFeedItem)
    {
        this.vFeedItem = vFeedItem;
        this.mFeedUserBaseInfo = mFeedUserBaseInfo;
        this.iStatus = iStatus;
        this.iTotal = iTotal;
        this.vTopFeedItem = vTopFeedItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vFeedItem) {
            ostream.writeList(0, vFeedItem);
        }
        if (null != mFeedUserBaseInfo) {
            ostream.writeMap(1, mFeedUserBaseInfo);
        }
        ostream.writeInt32(2, iStatus);
        ostream.writeInt32(3, iTotal);
        if (null != vTopFeedItem) {
            ostream.writeList(4, vTopFeedItem);
        }
    }

    static java.util.ArrayList<BEC.FeedItem> VAR_TYPE_4_VFEEDITEM = new java.util.ArrayList<BEC.FeedItem>();
    static {
        VAR_TYPE_4_VFEEDITEM.add(new BEC.FeedItem());
    }

    static java.util.Map<Long, BEC.FeedUserBaseInfo> VAR_TYPE_4_MFEEDUSERBASEINFO = new java.util.HashMap<Long, BEC.FeedUserBaseInfo>();
    static {
        VAR_TYPE_4_MFEEDUSERBASEINFO.put(0L, new BEC.FeedUserBaseInfo());
    }

    static java.util.ArrayList<BEC.FeedItem> VAR_TYPE_4_VTOPFEEDITEM = new java.util.ArrayList<BEC.FeedItem>();
    static {
        VAR_TYPE_4_VTOPFEEDITEM.add(new BEC.FeedItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vFeedItem = (java.util.ArrayList<BEC.FeedItem>)istream.readList(0, false, VAR_TYPE_4_VFEEDITEM);
        this.mFeedUserBaseInfo = (java.util.Map<Long, BEC.FeedUserBaseInfo>)istream.readMap(1, false, VAR_TYPE_4_MFEEDUSERBASEINFO);
        this.iStatus = (int)istream.readInt32(2, false, this.iStatus);
        this.iTotal = (int)istream.readInt32(3, false, this.iTotal);
        this.vTopFeedItem = (java.util.ArrayList<BEC.FeedItem>)istream.readList(4, false, VAR_TYPE_4_VTOPFEEDITEM);
    }

}

