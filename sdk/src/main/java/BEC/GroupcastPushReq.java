package BEC;

public final class GroupcastPushReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public PushData stPushData = null;

    public java.util.ArrayList<String> vTags = null;

    public PushData getStPushData()
    {
        return stPushData;
    }

    public void  setStPushData(PushData stPushData)
    {
        this.stPushData = stPushData;
    }

    public java.util.ArrayList<String> getVTags()
    {
        return vTags;
    }

    public void  setVTags(java.util.ArrayList<String> vTags)
    {
        this.vTags = vTags;
    }

    public GroupcastPushReq()
    {
    }

    public GroupcastPushReq(PushData stPushData, java.util.ArrayList<String> vTags)
    {
        this.stPushData = stPushData;
        this.vTags = vTags;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stPushData) {
            ostream.writeMessage(0, stPushData);
        }
        if (null != vTags) {
            ostream.writeList(1, vTags);
        }
    }

    static PushData VAR_TYPE_4_STPUSHDATA = new PushData();

    static java.util.ArrayList<String> VAR_TYPE_4_VTAGS = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTAGS.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stPushData = (PushData)istream.readMessage(0, false, VAR_TYPE_4_STPUSHDATA);
        this.vTags = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VTAGS);
    }

}

