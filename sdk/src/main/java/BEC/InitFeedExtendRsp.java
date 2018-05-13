package BEC;

public final class InitFeedExtendRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iLikeInitNumber = 0;

    public int iAccessInitNumber = 0;

    public int getILikeInitNumber()
    {
        return iLikeInitNumber;
    }

    public void  setILikeInitNumber(int iLikeInitNumber)
    {
        this.iLikeInitNumber = iLikeInitNumber;
    }

    public int getIAccessInitNumber()
    {
        return iAccessInitNumber;
    }

    public void  setIAccessInitNumber(int iAccessInitNumber)
    {
        this.iAccessInitNumber = iAccessInitNumber;
    }

    public InitFeedExtendRsp()
    {
    }

    public InitFeedExtendRsp(int iLikeInitNumber, int iAccessInitNumber)
    {
        this.iLikeInitNumber = iLikeInitNumber;
        this.iAccessInitNumber = iAccessInitNumber;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iLikeInitNumber);
        ostream.writeInt32(1, iAccessInitNumber);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iLikeInitNumber = (int)istream.readInt32(0, false, this.iLikeInitNumber);
        this.iAccessInitNumber = (int)istream.readInt32(1, false, this.iAccessInitNumber);
    }

}

