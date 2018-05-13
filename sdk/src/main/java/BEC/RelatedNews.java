package BEC;

public final class RelatedNews extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sNewsId = "";

    public String sTitle = "";

    public String getSNewsId()
    {
        return sNewsId;
    }

    public void  setSNewsId(String sNewsId)
    {
        this.sNewsId = sNewsId;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public RelatedNews()
    {
    }

    public RelatedNews(String sNewsId, String sTitle)
    {
        this.sNewsId = sNewsId;
        this.sTitle = sTitle;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sNewsId) {
            ostream.writeString(0, sNewsId);
        }
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sNewsId = (String)istream.readString(0, false, this.sNewsId);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
    }

}

