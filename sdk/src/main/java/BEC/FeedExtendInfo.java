package BEC;

public final class FeedExtendInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTotalCommentSize = 0;

    public int iLikeSize = 0;

    public int iAccessCount = 0;

    public int getITotalCommentSize()
    {
        return iTotalCommentSize;
    }

    public void  setITotalCommentSize(int iTotalCommentSize)
    {
        this.iTotalCommentSize = iTotalCommentSize;
    }

    public int getILikeSize()
    {
        return iLikeSize;
    }

    public void  setILikeSize(int iLikeSize)
    {
        this.iLikeSize = iLikeSize;
    }

    public int getIAccessCount()
    {
        return iAccessCount;
    }

    public void  setIAccessCount(int iAccessCount)
    {
        this.iAccessCount = iAccessCount;
    }

    public FeedExtendInfo()
    {
    }

    public FeedExtendInfo(int iTotalCommentSize, int iLikeSize, int iAccessCount)
    {
        this.iTotalCommentSize = iTotalCommentSize;
        this.iLikeSize = iLikeSize;
        this.iAccessCount = iAccessCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTotalCommentSize);
        ostream.writeInt32(1, iLikeSize);
        ostream.writeInt32(2, iAccessCount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTotalCommentSize = (int)istream.readInt32(0, false, this.iTotalCommentSize);
        this.iLikeSize = (int)istream.readInt32(1, false, this.iLikeSize);
        this.iAccessCount = (int)istream.readInt32(2, false, this.iAccessCount);
    }

}

