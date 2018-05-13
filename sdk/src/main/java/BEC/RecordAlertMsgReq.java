package BEC;

public final class RecordAlertMsgReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<PushData> vPushData = null;

    public java.util.ArrayList<PushData> getVPushData()
    {
        return vPushData;
    }

    public void  setVPushData(java.util.ArrayList<PushData> vPushData)
    {
        this.vPushData = vPushData;
    }

    public RecordAlertMsgReq()
    {
    }

    public RecordAlertMsgReq(java.util.ArrayList<PushData> vPushData)
    {
        this.vPushData = vPushData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vPushData) {
            ostream.writeList(2, vPushData);
        }
    }

    static java.util.ArrayList<PushData> VAR_TYPE_4_VPUSHDATA = new java.util.ArrayList<PushData>();
    static {
        VAR_TYPE_4_VPUSHDATA.add(new PushData());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vPushData = (java.util.ArrayList<PushData>)istream.readList(2, false, VAR_TYPE_4_VPUSHDATA);
    }

}

