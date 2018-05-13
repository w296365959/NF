package BEC;

public final class GetInvestListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo = null;

    public java.util.Map<Long, Integer> mRelation = null;

    public int iStatus = 0;

    public String sNextStartId = "";

    public java.util.ArrayList<BEC.FeedInvestListHead> vHead = null;

    public java.util.ArrayList<BEC.FeedUserBaseInfo> getVFeedUserBaseInfo()
    {
        return vFeedUserBaseInfo;
    }

    public void  setVFeedUserBaseInfo(java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo)
    {
        this.vFeedUserBaseInfo = vFeedUserBaseInfo;
    }

    public java.util.Map<Long, Integer> getMRelation()
    {
        return mRelation;
    }

    public void  setMRelation(java.util.Map<Long, Integer> mRelation)
    {
        this.mRelation = mRelation;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public String getSNextStartId()
    {
        return sNextStartId;
    }

    public void  setSNextStartId(String sNextStartId)
    {
        this.sNextStartId = sNextStartId;
    }

    public java.util.ArrayList<BEC.FeedInvestListHead> getVHead()
    {
        return vHead;
    }

    public void  setVHead(java.util.ArrayList<BEC.FeedInvestListHead> vHead)
    {
        this.vHead = vHead;
    }

    public GetInvestListRsp()
    {
    }

    public GetInvestListRsp(java.util.ArrayList<BEC.FeedUserBaseInfo> vFeedUserBaseInfo, java.util.Map<Long, Integer> mRelation, int iStatus, String sNextStartId, java.util.ArrayList<BEC.FeedInvestListHead> vHead)
    {
        this.vFeedUserBaseInfo = vFeedUserBaseInfo;
        this.mRelation = mRelation;
        this.iStatus = iStatus;
        this.sNextStartId = sNextStartId;
        this.vHead = vHead;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vFeedUserBaseInfo) {
            ostream.writeList(1, vFeedUserBaseInfo);
        }
        if (null != mRelation) {
            ostream.writeMap(2, mRelation);
        }
        ostream.writeInt32(3, iStatus);
        if (null != sNextStartId) {
            ostream.writeString(4, sNextStartId);
        }
        if (null != vHead) {
            ostream.writeList(5, vHead);
        }
    }

    static java.util.ArrayList<BEC.FeedUserBaseInfo> VAR_TYPE_4_VFEEDUSERBASEINFO = new java.util.ArrayList<BEC.FeedUserBaseInfo>();
    static {
        VAR_TYPE_4_VFEEDUSERBASEINFO.add(new BEC.FeedUserBaseInfo());
    }

    static java.util.Map<Long, Integer> VAR_TYPE_4_MRELATION = new java.util.HashMap<Long, Integer>();
    static {
        VAR_TYPE_4_MRELATION.put(0L, 0);
    }

    static java.util.ArrayList<BEC.FeedInvestListHead> VAR_TYPE_4_VHEAD = new java.util.ArrayList<BEC.FeedInvestListHead>();
    static {
        VAR_TYPE_4_VHEAD.add(new BEC.FeedInvestListHead());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vFeedUserBaseInfo = (java.util.ArrayList<BEC.FeedUserBaseInfo>)istream.readList(1, false, VAR_TYPE_4_VFEEDUSERBASEINFO);
        this.mRelation = (java.util.Map<Long, Integer>)istream.readMap(2, false, VAR_TYPE_4_MRELATION);
        this.iStatus = (int)istream.readInt32(3, false, this.iStatus);
        this.sNextStartId = (String)istream.readString(4, false, this.sNextStartId);
        this.vHead = (java.util.ArrayList<BEC.FeedInvestListHead>)istream.readList(5, false, VAR_TYPE_4_VHEAD);
    }

}

