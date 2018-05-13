package BEC;

public final class InitFeedExtendReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFeedId = "";

    public int eType = BEC.E_FEED_EXTEND_TYPE.E_EXTEND_ALL;

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public InitFeedExtendReq()
    {
    }

    public InitFeedExtendReq(String sFeedId, int eType)
    {
        this.sFeedId = sFeedId;
        this.eType = eType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFeedId) {
            ostream.writeString(0, sFeedId);
        }
        ostream.writeInt32(1, eType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFeedId = (String)istream.readString(0, false, this.sFeedId);
        this.eType = (int)istream.readInt32(1, false, this.eType);
    }

}

