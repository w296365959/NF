package BEC;

public final class QuickPushReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iId = 0;

    public int getIId()
    {
        return iId;
    }

    public void  setIId(int iId)
    {
        this.iId = iId;
    }

    public QuickPushReq()
    {
    }

    public QuickPushReq(int iId)
    {
        this.iId = iId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iId);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iId = (int)istream.readInt32(0, false, this.iId);
    }

}

