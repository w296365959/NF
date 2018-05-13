package BEC;

public final class CheckUserPushReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtGuid = null;

    public PushType stPushType = null;

    public java.util.ArrayList<String> getVtGuid()
    {
        return vtGuid;
    }

    public void  setVtGuid(java.util.ArrayList<String> vtGuid)
    {
        this.vtGuid = vtGuid;
    }

    public PushType getStPushType()
    {
        return stPushType;
    }

    public void  setStPushType(PushType stPushType)
    {
        this.stPushType = stPushType;
    }

    public CheckUserPushReq()
    {
    }

    public CheckUserPushReq(java.util.ArrayList<String> vtGuid, PushType stPushType)
    {
        this.vtGuid = vtGuid;
        this.stPushType = stPushType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtGuid) {
            ostream.writeList(0, vtGuid);
        }
        if (null != stPushType) {
            ostream.writeMessage(1, stPushType);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTGUID = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTGUID.add("");
    }

    static PushType VAR_TYPE_4_STPUSHTYPE = new PushType();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtGuid = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VTGUID);
        this.stPushType = (PushType)istream.readMessage(1, false, VAR_TYPE_4_STPUSHTYPE);
    }

}

