package BEC;

public final class DtMorNewsItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTitle = "";

    public String sAbstract = "";

    public String sNewsId = "";

    public String sPublish = "";

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSAbstract()
    {
        return sAbstract;
    }

    public void  setSAbstract(String sAbstract)
    {
        this.sAbstract = sAbstract;
    }

    public String getSNewsId()
    {
        return sNewsId;
    }

    public void  setSNewsId(String sNewsId)
    {
        this.sNewsId = sNewsId;
    }

    public String getSPublish()
    {
        return sPublish;
    }

    public void  setSPublish(String sPublish)
    {
        this.sPublish = sPublish;
    }

    public DtMorNewsItem()
    {
    }

    public DtMorNewsItem(String sTitle, String sAbstract, String sNewsId, String sPublish)
    {
        this.sTitle = sTitle;
        this.sAbstract = sAbstract;
        this.sNewsId = sNewsId;
        this.sPublish = sPublish;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTitle) {
            ostream.writeString(0, sTitle);
        }
        if (null != sAbstract) {
            ostream.writeString(1, sAbstract);
        }
        if (null != sNewsId) {
            ostream.writeString(2, sNewsId);
        }
        if (null != sPublish) {
            ostream.writeString(3, sPublish);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTitle = (String)istream.readString(0, false, this.sTitle);
        this.sAbstract = (String)istream.readString(1, false, this.sAbstract);
        this.sNewsId = (String)istream.readString(2, false, this.sNewsId);
        this.sPublish = (String)istream.readString(3, false, this.sPublish);
    }

}

