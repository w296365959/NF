package BEC;

public final class PushType extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int ePushDataType = 0;

    public String sBusinessId = "";

    public String sMsgId = "";

    public int getEPushDataType()
    {
        return ePushDataType;
    }

    public void  setEPushDataType(int ePushDataType)
    {
        this.ePushDataType = ePushDataType;
    }

    public String getSBusinessId()
    {
        return sBusinessId;
    }

    public void  setSBusinessId(String sBusinessId)
    {
        this.sBusinessId = sBusinessId;
    }

    public String getSMsgId()
    {
        return sMsgId;
    }

    public void  setSMsgId(String sMsgId)
    {
        this.sMsgId = sMsgId;
    }

    public PushType()
    {
    }

    public PushType(int ePushDataType, String sBusinessId, String sMsgId)
    {
        this.ePushDataType = ePushDataType;
        this.sBusinessId = sBusinessId;
        this.sMsgId = sMsgId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, ePushDataType);
        if (null != sBusinessId) {
            ostream.writeString(1, sBusinessId);
        }
        if (null != sMsgId) {
            ostream.writeString(2, sMsgId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.ePushDataType = (int)istream.readInt32(0, false, this.ePushDataType);
        this.sBusinessId = (String)istream.readString(1, false, this.sBusinessId);
        this.sMsgId = (String)istream.readString(2, false, this.sMsgId);
    }

}

