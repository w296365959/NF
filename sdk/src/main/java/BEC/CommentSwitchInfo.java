package BEC;

public final class CommentSwitchInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iSwitchOn = 0;

    public int iFirstStartInterval = 0;

    public int iLaterStartInterval = 0;

    public int getISwitchOn()
    {
        return iSwitchOn;
    }

    public void  setISwitchOn(int iSwitchOn)
    {
        this.iSwitchOn = iSwitchOn;
    }

    public int getIFirstStartInterval()
    {
        return iFirstStartInterval;
    }

    public void  setIFirstStartInterval(int iFirstStartInterval)
    {
        this.iFirstStartInterval = iFirstStartInterval;
    }

    public int getILaterStartInterval()
    {
        return iLaterStartInterval;
    }

    public void  setILaterStartInterval(int iLaterStartInterval)
    {
        this.iLaterStartInterval = iLaterStartInterval;
    }

    public CommentSwitchInfo()
    {
    }

    public CommentSwitchInfo(int iSwitchOn, int iFirstStartInterval, int iLaterStartInterval)
    {
        this.iSwitchOn = iSwitchOn;
        this.iFirstStartInterval = iFirstStartInterval;
        this.iLaterStartInterval = iLaterStartInterval;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iSwitchOn);
        ostream.writeInt32(1, iFirstStartInterval);
        ostream.writeInt32(2, iLaterStartInterval);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iSwitchOn = (int)istream.readInt32(0, false, this.iSwitchOn);
        this.iFirstStartInterval = (int)istream.readInt32(1, false, this.iFirstStartInterval);
        this.iLaterStartInterval = (int)istream.readInt32(2, false, this.iLaterStartInterval);
    }

}

