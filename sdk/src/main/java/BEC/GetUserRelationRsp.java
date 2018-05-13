package BEC;

public final class GetUserRelationRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FeedUserRelation stFeedUserRelation = null;

    public java.util.Map<Long, Integer> mRelation = null;

    public int iStatus = 0;

    public String sNextStartId = "";

    public BEC.FeedUserRelation getStFeedUserRelation()
    {
        return stFeedUserRelation;
    }

    public void  setStFeedUserRelation(BEC.FeedUserRelation stFeedUserRelation)
    {
        this.stFeedUserRelation = stFeedUserRelation;
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

    public GetUserRelationRsp()
    {
    }

    public GetUserRelationRsp(BEC.FeedUserRelation stFeedUserRelation, java.util.Map<Long, Integer> mRelation, int iStatus, String sNextStartId)
    {
        this.stFeedUserRelation = stFeedUserRelation;
        this.mRelation = mRelation;
        this.iStatus = iStatus;
        this.sNextStartId = sNextStartId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFeedUserRelation) {
            ostream.writeMessage(0, stFeedUserRelation);
        }
        if (null != mRelation) {
            ostream.writeMap(1, mRelation);
        }
        ostream.writeInt32(2, iStatus);
        if (null != sNextStartId) {
            ostream.writeString(3, sNextStartId);
        }
    }

    static BEC.FeedUserRelation VAR_TYPE_4_STFEEDUSERRELATION = new BEC.FeedUserRelation();

    static java.util.Map<Long, Integer> VAR_TYPE_4_MRELATION = new java.util.HashMap<Long, Integer>();
    static {
        VAR_TYPE_4_MRELATION.put(0L, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFeedUserRelation = (BEC.FeedUserRelation)istream.readMessage(0, false, VAR_TYPE_4_STFEEDUSERRELATION);
        this.mRelation = (java.util.Map<Long, Integer>)istream.readMap(1, false, VAR_TYPE_4_MRELATION);
        this.iStatus = (int)istream.readInt32(2, false, this.iStatus);
        this.sNextStartId = (String)istream.readString(3, false, this.sNextStartId);
    }

}

