package BEC;

public final class TopicListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStartTime = 0;

    public int iEndTime = 0;

    public int getIStartTime()
    {
        return iStartTime;
    }

    public void  setIStartTime(int iStartTime)
    {
        this.iStartTime = iStartTime;
    }

    public int getIEndTime()
    {
        return iEndTime;
    }

    public void  setIEndTime(int iEndTime)
    {
        this.iEndTime = iEndTime;
    }

    public TopicListReq()
    {
    }

    public TopicListReq(int iStartTime, int iEndTime)
    {
        this.iStartTime = iStartTime;
        this.iEndTime = iEndTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStartTime);
        ostream.writeInt32(1, iEndTime);
    }

    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStartTime = (int)istream.readInt32(0, false, this.iStartTime);
        this.iEndTime = (int)istream.readInt32(1, false, this.iEndTime);
    }

}

