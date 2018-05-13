package BEC;

public final class MsgValue4Cache extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStatus = 0;

    public int iCreateTime = 0;

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public MsgValue4Cache()
    {
    }

    public MsgValue4Cache(int iStatus, int iCreateTime)
    {
        this.iStatus = iStatus;
        this.iCreateTime = iCreateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStatus);
        ostream.writeInt32(1, iCreateTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStatus = (int)istream.readInt32(0, false, this.iStatus);
        this.iCreateTime = (int)istream.readInt32(1, false, this.iCreateTime);
    }

}

