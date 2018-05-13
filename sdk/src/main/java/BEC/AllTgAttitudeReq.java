package BEC;

public final class AllTgAttitudeReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStartTime = 0;

    public int getIStartTime()
    {
        return iStartTime;
    }

    public void  setIStartTime(int iStartTime)
    {
        this.iStartTime = iStartTime;
    }

    public AllTgAttitudeReq()
    {
    }

    public AllTgAttitudeReq(int iStartTime)
    {
        this.iStartTime = iStartTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStartTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStartTime = (int)istream.readInt32(0, false, this.iStartTime);
    }

}

