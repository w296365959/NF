package BEC;

public final class UserPointDetailItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTaskType = 0;

    public long lTimeStamp = 0;

    public int iGetPoints = 0;

    public int getITaskType()
    {
        return iTaskType;
    }

    public void  setITaskType(int iTaskType)
    {
        this.iTaskType = iTaskType;
    }

    public long getLTimeStamp()
    {
        return lTimeStamp;
    }

    public void  setLTimeStamp(long lTimeStamp)
    {
        this.lTimeStamp = lTimeStamp;
    }

    public int getIGetPoints()
    {
        return iGetPoints;
    }

    public void  setIGetPoints(int iGetPoints)
    {
        this.iGetPoints = iGetPoints;
    }

    public UserPointDetailItem()
    {
    }

    public UserPointDetailItem(int iTaskType, long lTimeStamp, int iGetPoints)
    {
        this.iTaskType = iTaskType;
        this.lTimeStamp = lTimeStamp;
        this.iGetPoints = iGetPoints;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTaskType);
        ostream.writeInt64(1, lTimeStamp);
        ostream.writeInt32(2, iGetPoints);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTaskType = (int)istream.readInt32(0, false, this.iTaskType);
        this.lTimeStamp = (long)istream.readInt64(1, false, this.lTimeStamp);
        this.iGetPoints = (int)istream.readInt32(2, false, this.iGetPoints);
    }

}

