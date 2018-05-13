package BEC;

public final class SetLoginDayRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iLeftDays = 0;

    public int getILeftDays()
    {
        return iLeftDays;
    }

    public void  setILeftDays(int iLeftDays)
    {
        this.iLeftDays = iLeftDays;
    }

    public SetLoginDayRsp()
    {
    }

    public SetLoginDayRsp(int iLeftDays)
    {
        this.iLeftDays = iLeftDays;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iLeftDays);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iLeftDays = (int)istream.readInt32(0, false, this.iLeftDays);
    }

}

