package BEC;

public final class PushStatusReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public PushData stPushData = null;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public PushData getStPushData()
    {
        return stPushData;
    }

    public void  setStPushData(PushData stPushData)
    {
        this.stPushData = stPushData;
    }

    public PushStatusReq()
    {
    }

    public PushStatusReq(String sId, PushData stPushData)
    {
        this.sId = sId;
        this.stPushData = stPushData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != stPushData) {
            ostream.writeMessage(1, stPushData);
        }
    }

    static PushData VAR_TYPE_4_STPUSHDATA = new PushData();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.stPushData = (PushData)istream.readMessage(1, false, VAR_TYPE_4_STPUSHDATA);
    }

}

