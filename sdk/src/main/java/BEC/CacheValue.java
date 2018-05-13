package BEC;

public final class CacheValue extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTimeStamp = 0;

    public int iTimes = 0;

    public int getITimeStamp()
    {
        return iTimeStamp;
    }

    public void  setITimeStamp(int iTimeStamp)
    {
        this.iTimeStamp = iTimeStamp;
    }

    public int getITimes()
    {
        return iTimes;
    }

    public void  setITimes(int iTimes)
    {
        this.iTimes = iTimes;
    }

    public CacheValue()
    {
    }

    public CacheValue(int iTimeStamp, int iTimes)
    {
        this.iTimeStamp = iTimeStamp;
        this.iTimes = iTimes;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTimeStamp);
        ostream.writeInt32(1, iTimes);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTimeStamp = (int)istream.readInt32(0, false, this.iTimeStamp);
        this.iTimes = (int)istream.readInt32(1, false, this.iTimes);
    }

}

