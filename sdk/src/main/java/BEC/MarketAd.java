package BEC;

public final class MarketAd extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTitle = "";

    public String sImgUrl = "";

    public String sUrl = "";

    public int iPos = 0;

    public int iStyle = 0;

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public int getIPos()
    {
        return iPos;
    }

    public void  setIPos(int iPos)
    {
        this.iPos = iPos;
    }

    public int getIStyle()
    {
        return iStyle;
    }

    public void  setIStyle(int iStyle)
    {
        this.iStyle = iStyle;
    }

    public MarketAd()
    {
    }

    public MarketAd(String sTitle, String sImgUrl, String sUrl, int iPos, int iStyle)
    {
        this.sTitle = sTitle;
        this.sImgUrl = sImgUrl;
        this.sUrl = sUrl;
        this.iPos = iPos;
        this.iStyle = iStyle;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTitle) {
            ostream.writeString(0, sTitle);
        }
        if (null != sImgUrl) {
            ostream.writeString(1, sImgUrl);
        }
        if (null != sUrl) {
            ostream.writeString(2, sUrl);
        }
        ostream.writeInt32(3, iPos);
        ostream.writeInt32(4, iStyle);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTitle = (String)istream.readString(0, false, this.sTitle);
        this.sImgUrl = (String)istream.readString(1, false, this.sImgUrl);
        this.sUrl = (String)istream.readString(2, false, this.sUrl);
        this.iPos = (int)istream.readInt32(3, false, this.iPos);
        this.iStyle = (int)istream.readInt32(4, false, this.iStyle);
    }

}

