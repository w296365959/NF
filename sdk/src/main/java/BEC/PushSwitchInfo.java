package BEC;

public final class PushSwitchInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iSwitchType = 0;

    public int iSwitchState = 0;

    public int getISwitchType()
    {
        return iSwitchType;
    }

    public void  setISwitchType(int iSwitchType)
    {
        this.iSwitchType = iSwitchType;
    }

    public int getISwitchState()
    {
        return iSwitchState;
    }

    public void  setISwitchState(int iSwitchState)
    {
        this.iSwitchState = iSwitchState;
    }

    public PushSwitchInfo()
    {
    }

    public PushSwitchInfo(int iSwitchType, int iSwitchState)
    {
        this.iSwitchType = iSwitchType;
        this.iSwitchState = iSwitchState;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iSwitchType);
        ostream.writeInt32(1, iSwitchState);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iSwitchType = (int)istream.readInt32(0, false, this.iSwitchType);
        this.iSwitchState = (int)istream.readInt32(1, false, this.iSwitchState);
    }

}

