package BEC;

public final class DtPriviBannerItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPriviType = 0;

    public String sBannerName = "";

    public String sBannerDesc = "";

    public String sBannerUrl = "";

    public String sSkipUrl = "";

    public int iStaySecond = 0;

    public int getIPriviType()
    {
        return iPriviType;
    }

    public void  setIPriviType(int iPriviType)
    {
        this.iPriviType = iPriviType;
    }

    public String getSBannerName()
    {
        return sBannerName;
    }

    public void  setSBannerName(String sBannerName)
    {
        this.sBannerName = sBannerName;
    }

    public String getSBannerDesc()
    {
        return sBannerDesc;
    }

    public void  setSBannerDesc(String sBannerDesc)
    {
        this.sBannerDesc = sBannerDesc;
    }

    public String getSBannerUrl()
    {
        return sBannerUrl;
    }

    public void  setSBannerUrl(String sBannerUrl)
    {
        this.sBannerUrl = sBannerUrl;
    }

    public String getSSkipUrl()
    {
        return sSkipUrl;
    }

    public void  setSSkipUrl(String sSkipUrl)
    {
        this.sSkipUrl = sSkipUrl;
    }

    public int getIStaySecond()
    {
        return iStaySecond;
    }

    public void  setIStaySecond(int iStaySecond)
    {
        this.iStaySecond = iStaySecond;
    }

    public DtPriviBannerItem()
    {
    }

    public DtPriviBannerItem(int iPriviType, String sBannerName, String sBannerDesc, String sBannerUrl, String sSkipUrl, int iStaySecond)
    {
        this.iPriviType = iPriviType;
        this.sBannerName = sBannerName;
        this.sBannerDesc = sBannerDesc;
        this.sBannerUrl = sBannerUrl;
        this.sSkipUrl = sSkipUrl;
        this.iStaySecond = iStaySecond;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPriviType);
        if (null != sBannerName) {
            ostream.writeString(1, sBannerName);
        }
        if (null != sBannerDesc) {
            ostream.writeString(2, sBannerDesc);
        }
        if (null != sBannerUrl) {
            ostream.writeString(3, sBannerUrl);
        }
        if (null != sSkipUrl) {
            ostream.writeString(4, sSkipUrl);
        }
        ostream.writeInt32(5, iStaySecond);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPriviType = (int)istream.readInt32(0, false, this.iPriviType);
        this.sBannerName = (String)istream.readString(1, false, this.sBannerName);
        this.sBannerDesc = (String)istream.readString(2, false, this.sBannerDesc);
        this.sBannerUrl = (String)istream.readString(3, false, this.sBannerUrl);
        this.sSkipUrl = (String)istream.readString(4, false, this.sSkipUrl);
        this.iStaySecond = (int)istream.readInt32(5, false, this.iStaySecond);
    }

}

