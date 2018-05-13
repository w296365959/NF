package BEC;

public final class StartPageNotify extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTitle = "";

    public String sUrl = "";

    public int iShowTime = 0;

    public int iHeight = 0;

    public int iWidth = 0;

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

    public int getIShowTime()
    {
        return iShowTime;
    }

    public void  setIShowTime(int iShowTime)
    {
        this.iShowTime = iShowTime;
    }

    public int getIHeight()
    {
        return iHeight;
    }

    public void  setIHeight(int iHeight)
    {
        this.iHeight = iHeight;
    }

    public int getIWidth()
    {
        return iWidth;
    }

    public void  setIWidth(int iWidth)
    {
        this.iWidth = iWidth;
    }

    public StartPageNotify()
    {
    }

    public StartPageNotify(String sTitle, String sUrl, int iShowTime, int iHeight, int iWidth)
    {
        this.sTitle = sTitle;
        this.sUrl = sUrl;
        this.iShowTime = iShowTime;
        this.iHeight = iHeight;
        this.iWidth = iWidth;
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
        ostream.writeInt32(2, iShowTime);
        ostream.writeInt32(3, iHeight);
        ostream.writeInt32(4, iWidth);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTitle = (String)istream.readString(0, false, this.sTitle);
        this.sUrl = (String)istream.readString(1, false, this.sUrl);
        this.iShowTime = (int)istream.readInt32(2, false, this.iShowTime);
        this.iHeight = (int)istream.readInt32(3, false, this.iHeight);
        this.iWidth = (int)istream.readInt32(4, false, this.iWidth);
    }

}

