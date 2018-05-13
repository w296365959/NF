package BEC;

public final class FeedContent extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFeedId = "";

    public long iPubAccountId = 0;

    public String sTitle = "";

    public String sContent = "";

    public int iPubTime = 0;

    public int eType = BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

    public java.util.ArrayList<BEC.SecCodeName> vRelateSec = null;

    public int eAttiType = BEC.E_FEED_INVEST_ATTI_TYPE.E_FIAT_NEUTRAL;

    public String sUrl = "";

    public String sDescription = "";

    public String getSFeedId()
    {
        return sFeedId;
    }

    public void  setSFeedId(String sFeedId)
    {
        this.sFeedId = sFeedId;
    }

    public long getIPubAccountId()
    {
        return iPubAccountId;
    }

    public void  setIPubAccountId(long iPubAccountId)
    {
        this.iPubAccountId = iPubAccountId;
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

    public int getIPubTime()
    {
        return iPubTime;
    }

    public void  setIPubTime(int iPubTime)
    {
        this.iPubTime = iPubTime;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public java.util.ArrayList<BEC.SecCodeName> getVRelateSec()
    {
        return vRelateSec;
    }

    public void  setVRelateSec(java.util.ArrayList<BEC.SecCodeName> vRelateSec)
    {
        this.vRelateSec = vRelateSec;
    }

    public int getEAttiType()
    {
        return eAttiType;
    }

    public void  setEAttiType(int eAttiType)
    {
        this.eAttiType = eAttiType;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public FeedContent()
    {
    }

    public FeedContent(String sFeedId, long iPubAccountId, String sTitle, String sContent, int iPubTime, int eType, java.util.ArrayList<BEC.SecCodeName> vRelateSec, int eAttiType, String sUrl, String sDescription)
    {
        this.sFeedId = sFeedId;
        this.iPubAccountId = iPubAccountId;
        this.sTitle = sTitle;
        this.sContent = sContent;
        this.iPubTime = iPubTime;
        this.eType = eType;
        this.vRelateSec = vRelateSec;
        this.eAttiType = eAttiType;
        this.sUrl = sUrl;
        this.sDescription = sDescription;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFeedId) {
            ostream.writeString(0, sFeedId);
        }
        ostream.writeUInt32(1, iPubAccountId);
        if (null != sTitle) {
            ostream.writeString(2, sTitle);
        }
        if (null != sContent) {
            ostream.writeString(3, sContent);
        }
        ostream.writeInt32(4, iPubTime);
        ostream.writeInt32(5, eType);
        if (null != vRelateSec) {
            ostream.writeList(6, vRelateSec);
        }
        ostream.writeInt32(7, eAttiType);
        if (null != sUrl) {
            ostream.writeString(8, sUrl);
        }
        if (null != sDescription) {
            ostream.writeString(9, sDescription);
        }
    }

    static java.util.ArrayList<BEC.SecCodeName> VAR_TYPE_4_VRELATESEC = new java.util.ArrayList<BEC.SecCodeName>();
    static {
        VAR_TYPE_4_VRELATESEC.add(new BEC.SecCodeName());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFeedId = (String)istream.readString(0, false, this.sFeedId);
        this.iPubAccountId = (long)istream.readUInt32(1, false, this.iPubAccountId);
        this.sTitle = (String)istream.readString(2, false, this.sTitle);
        this.sContent = (String)istream.readString(3, false, this.sContent);
        this.iPubTime = (int)istream.readInt32(4, false, this.iPubTime);
        this.eType = (int)istream.readInt32(5, false, this.eType);
        this.vRelateSec = (java.util.ArrayList<BEC.SecCodeName>)istream.readList(6, false, VAR_TYPE_4_VRELATESEC);
        this.eAttiType = (int)istream.readInt32(7, false, this.eAttiType);
        this.sUrl = (String)istream.readString(8, false, this.sUrl);
        this.sDescription = (String)istream.readString(9, false, this.sDescription);
    }

}

