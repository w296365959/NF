package BEC;

public final class AlertRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iSetSize = 0;

    public long getISetSize()
    {
        return iSetSize;
    }

    public void  setISetSize(long iSetSize)
    {
        this.iSetSize = iSetSize;
    }

    public AlertRsp()
    {
    }

    public AlertRsp(long iSetSize)
    {
        this.iSetSize = iSetSize;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iSetSize);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iSetSize = (long)istream.readUInt32(0, false, this.iSetSize);
    }

}

