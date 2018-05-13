package BEC;

public final class DtSecStatItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTradingDay = "";

    public String sTitle = "";

    public String sContent = "";

    public String sImgUrl = "";

    public int iUpdateTime = 0;

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public DtSecStatItem()
    {
    }

    public DtSecStatItem(String sTradingDay, String sTitle, String sContent, String sImgUrl, int iUpdateTime)
    {
        this.sTradingDay = sTradingDay;
        this.sTitle = sTitle;
        this.sContent = sContent;
        this.sImgUrl = sImgUrl;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTradingDay) {
            ostream.writeString(0, sTradingDay);
        }
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
        if (null != sContent) {
            ostream.writeString(2, sContent);
        }
        if (null != sImgUrl) {
            ostream.writeString(3, sImgUrl);
        }
        ostream.writeInt32(4, iUpdateTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTradingDay = (String)istream.readString(0, false, this.sTradingDay);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.sContent = (String)istream.readString(2, false, this.sContent);
        this.sImgUrl = (String)istream.readString(3, false, this.sImgUrl);
        this.iUpdateTime = (int)istream.readInt32(4, false, this.iUpdateTime);
    }

}

