package BEC;

public final class LikeUserRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iCount = 0;

    public boolean isLike = true;

    public int getICount()
    {
        return iCount;
    }

    public void  setICount(int iCount)
    {
        this.iCount = iCount;
    }

    public boolean getIsLike()
    {
        return isLike;
    }

    public void  setIsLike(boolean isLike)
    {
        this.isLike = isLike;
    }

    public LikeUserRsp()
    {
    }

    public LikeUserRsp(int iCount, boolean isLike)
    {
        this.iCount = iCount;
        this.isLike = isLike;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iCount);
        ostream.writeBoolean(1, isLike);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iCount = (int)istream.readInt32(0, false, this.iCount);
        this.isLike = (boolean)istream.readBoolean(1, false, this.isLike);
    }

}

