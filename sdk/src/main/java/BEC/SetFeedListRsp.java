package BEC;

public final class SetFeedListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTotalSize = 0;

    public int getITotalSize()
    {
        return iTotalSize;
    }

    public void  setITotalSize(int iTotalSize)
    {
        this.iTotalSize = iTotalSize;
    }

    public SetFeedListRsp()
    {
    }

    public SetFeedListRsp(int iTotalSize)
    {
        this.iTotalSize = iTotalSize;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTotalSize);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTotalSize = (int)istream.readInt32(0, false, this.iTotalSize);
    }

}

