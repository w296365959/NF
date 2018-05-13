package BEC;

public final class WxTalkFreeListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iBeginID = 0;

    public int iCount = 0;

    public int getIBeginID()
    {
        return iBeginID;
    }

    public void  setIBeginID(int iBeginID)
    {
        this.iBeginID = iBeginID;
    }

    public int getICount()
    {
        return iCount;
    }

    public void  setICount(int iCount)
    {
        this.iCount = iCount;
    }

    public WxTalkFreeListReq()
    {
    }

    public WxTalkFreeListReq(int iBeginID, int iCount)
    {
        this.iBeginID = iBeginID;
        this.iCount = iCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iBeginID);
        ostream.writeInt32(1, iCount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iBeginID = (int)istream.readInt32(0, false, this.iBeginID);
        this.iCount = (int)istream.readInt32(1, false, this.iCount);
    }

}

