package BEC;

public final class PoFeedRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFeedId = "";

    public String sClientFeedId = "";

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public String getSClientFeedId()
    {
        return sClientFeedId;
    }

    public void  setSClientFeedId(String sClientFeedId)
    {
        this.sClientFeedId = sClientFeedId;
    }

    public PoFeedRsp()
    {
    }

    public PoFeedRsp(String sFeedId, String sClientFeedId)
    {
        this.sFeedId = sFeedId;
        this.sClientFeedId = sClientFeedId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFeedId) {
            ostream.writeString(1, sFeedId);
        }
        if (null != sClientFeedId) {
            ostream.writeString(2, sClientFeedId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFeedId = (String)istream.readString(1, false, this.sFeedId);
        this.sClientFeedId = (String)istream.readString(2, false, this.sClientFeedId);
    }

}

