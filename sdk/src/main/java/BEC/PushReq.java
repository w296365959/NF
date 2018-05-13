package BEC;

public final class PushReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int ePushIdType = 0;

    public java.util.ArrayList<String> vtId = null;

    public PushData stPushData = null;

    public int eFreqControlType = 0;

    public java.util.ArrayList<String> vTags = null;

    public int getEPushIdType()
    {
        return ePushIdType;
    }

    public void  setEPushIdType(int ePushIdType)
    {
        this.ePushIdType = ePushIdType;
    }

    public java.util.ArrayList<String> getVtId()
    {
        return vtId;
    }

    public void  setVtId(java.util.ArrayList<String> vtId)
    {
        this.vtId = vtId;
    }

    public PushData getStPushData()
    {
        return stPushData;
    }

    public void  setStPushData(PushData stPushData)
    {
        this.stPushData = stPushData;
    }

    public int getEFreqControlType()
    {
        return eFreqControlType;
    }

    public void  setEFreqControlType(int eFreqControlType)
    {
        this.eFreqControlType = eFreqControlType;
    }

    public java.util.ArrayList<String> getVTags()
    {
        return vTags;
    }

    public void  setVTags(java.util.ArrayList<String> vTags)
    {
        this.vTags = vTags;
    }

    public PushReq()
    {
    }

    public PushReq(int ePushIdType, java.util.ArrayList<String> vtId, PushData stPushData, int eFreqControlType, java.util.ArrayList<String> vTags)
    {
        this.ePushIdType = ePushIdType;
        this.vtId = vtId;
        this.stPushData = stPushData;
        this.eFreqControlType = eFreqControlType;
        this.vTags = vTags;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, ePushIdType);
        if (null != vtId) {
            ostream.writeList(1, vtId);
        }
        if (null != stPushData) {
            ostream.writeMessage(2, stPushData);
        }
        ostream.writeInt32(3, eFreqControlType);
        if (null != vTags) {
            ostream.writeList(4, vTags);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTID = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTID.add("");
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

        this.ePushIdType = (int)istream.readInt32(0, false, this.ePushIdType);
        this.vtId = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VTID);
        this.stPushData = (PushData)istream.readMessage(2, false, VAR_TYPE_4_STPUSHDATA);
        this.eFreqControlType = (int)istream.readInt32(3, false, this.eFreqControlType);
        this.vTags = (java.util.ArrayList<String>)istream.readList(4, false, VAR_TYPE_4_VTAGS);
    }

}

