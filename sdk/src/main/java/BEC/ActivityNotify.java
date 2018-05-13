package BEC;

public final class ActivityNotify extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTitle = "";

    public String sUrl = "";

    public String sMsg = "";

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public ActivityNotify()
    {
    }

    public ActivityNotify(String sTitle, String sUrl, String sMsg)
    {
        this.sTitle = sTitle;
        this.sUrl = sUrl;
        this.sMsg = sMsg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTitle) {
            ostream.writeString(0, sTitle);
        }
        if (null != sUrl) {
            ostream.writeString(1, sUrl);
        }
        if (null != sMsg) {
            ostream.writeString(2, sMsg);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTitle = (String)istream.readString(0, false, this.sTitle);
        this.sUrl = (String)istream.readString(1, false, this.sUrl);
        this.sMsg = (String)istream.readString(2, false, this.sMsg);
    }

}

