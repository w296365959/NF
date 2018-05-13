package BEC;

public final class GetUserRelationListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public int eFeedUserRelationType = BEC.E_FEED_USER_RELATION_TYPE.E_FURT_FOLLOWER;

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public int getEFeedUserRelationType()
    {
        return eFeedUserRelationType;
    }

    public void  setEFeedUserRelationType(int eFeedUserRelationType)
    {
        this.eFeedUserRelationType = eFeedUserRelationType;
    }

    public GetUserRelationListReq()
    {
    }

    public GetUserRelationListReq(long iAccountId, int eFeedUserRelationType)
    {
        this.iAccountId = iAccountId;
        this.eFeedUserRelationType = eFeedUserRelationType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        ostream.writeInt32(1, eFeedUserRelationType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.eFeedUserRelationType = (int)istream.readInt32(1, false, this.eFeedUserRelationType);
    }

}

