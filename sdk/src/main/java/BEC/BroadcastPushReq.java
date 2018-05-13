package BEC;

public final class BroadcastPushReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public PushData stPushData = null;

    public PushData getStPushData()
    {
        return stPushData;
    }

    public void  setStPushData(PushData stPushData)
    {
        this.stPushData = stPushData;
    }

    public BroadcastPushReq()
    {
    }

    public BroadcastPushReq(PushData stPushData)
    {
        this.stPushData = stPushData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stPushData) {
            ostream.writeMessage(0, stPushData);
        }
    }

    static PushData VAR_TYPE_4_STPUSHDATA = new PushData();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stPushData = (PushData)istream.readMessage(0, false, VAR_TYPE_4_STPUSHDATA);
    }

}

