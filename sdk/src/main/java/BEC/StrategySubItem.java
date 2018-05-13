package BEC;

public final class StrategySubItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sTitle = "";

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public java.util.ArrayList<IntelliStock> vtIntelliStock = null;

    public String sUrl = "";

    public String sDate = "";

    public String sSource = "";

    public float fMaxRetAvgIncrease = 0;

    public String sMaxRetUpBanner = "";

    public int iStatus = 0;

    public float fSuccPercent = 0;

    public int iSubscriptionsCount = 0;

    public String sSubscriptionsCount = "";

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public java.util.ArrayList<TagInfo> getVtTagInfo()
    {
        return vtTagInfo;
    }

    public void  setVtTagInfo(java.util.ArrayList<TagInfo> vtTagInfo)
    {
        this.vtTagInfo = vtTagInfo;
    }

    public java.util.ArrayList<IntelliStock> getVtIntelliStock()
    {
        return vtIntelliStock;
    }

    public void  setVtIntelliStock(java.util.ArrayList<IntelliStock> vtIntelliStock)
    {
        this.vtIntelliStock = vtIntelliStock;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getSSource()
    {
        return sSource;
    }

    public void  setSSource(String sSource)
    {
        this.sSource = sSource;
    }

    public float getFMaxRetAvgIncrease()
    {
        return fMaxRetAvgIncrease;
    }

    public void  setFMaxRetAvgIncrease(float fMaxRetAvgIncrease)
    {
        this.fMaxRetAvgIncrease = fMaxRetAvgIncrease;
    }

    public String getSMaxRetUpBanner()
    {
        return sMaxRetUpBanner;
    }

    public void  setSMaxRetUpBanner(String sMaxRetUpBanner)
    {
        this.sMaxRetUpBanner = sMaxRetUpBanner;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public float getFSuccPercent()
    {
        return fSuccPercent;
    }

    public void  setFSuccPercent(float fSuccPercent)
    {
        this.fSuccPercent = fSuccPercent;
    }

    public int getISubscriptionsCount()
    {
        return iSubscriptionsCount;
    }

    public void  setISubscriptionsCount(int iSubscriptionsCount)
    {
        this.iSubscriptionsCount = iSubscriptionsCount;
    }

    public String getSSubscriptionsCount()
    {
        return sSubscriptionsCount;
    }

    public void  setSSubscriptionsCount(String sSubscriptionsCount)
    {
        this.sSubscriptionsCount = sSubscriptionsCount;
    }

    public StrategySubItem()
    {
    }

    public StrategySubItem(String sId, String sTitle, java.util.ArrayList<TagInfo> vtTagInfo, java.util.ArrayList<IntelliStock> vtIntelliStock, String sUrl, String sDate, String sSource, float fMaxRetAvgIncrease, String sMaxRetUpBanner, int iStatus, float fSuccPercent, int iSubscriptionsCount, String sSubscriptionsCount)
    {
        this.sId = sId;
        this.sTitle = sTitle;
        this.vtTagInfo = vtTagInfo;
        this.vtIntelliStock = vtIntelliStock;
        this.sUrl = sUrl;
        this.sDate = sDate;
        this.sSource = sSource;
        this.fMaxRetAvgIncrease = fMaxRetAvgIncrease;
        this.sMaxRetUpBanner = sMaxRetUpBanner;
        this.iStatus = iStatus;
        this.fSuccPercent = fSuccPercent;
        this.iSubscriptionsCount = iSubscriptionsCount;
        this.sSubscriptionsCount = sSubscriptionsCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
        if (null != vtTagInfo) {
            ostream.writeList(2, vtTagInfo);
        }
        if (null != vtIntelliStock) {
            ostream.writeList(3, vtIntelliStock);
        }
        if (null != sUrl) {
            ostream.writeString(4, sUrl);
        }
        if (null != sDate) {
            ostream.writeString(5, sDate);
        }
        if (null != sSource) {
            ostream.writeString(6, sSource);
        }
        ostream.writeFloat(7, fMaxRetAvgIncrease);
        if (null != sMaxRetUpBanner) {
            ostream.writeString(8, sMaxRetUpBanner);
        }
        ostream.writeInt32(9, iStatus);
        ostream.writeFloat(10, fSuccPercent);
        ostream.writeInt32(11, iSubscriptionsCount);
        if (null != sSubscriptionsCount) {
            ostream.writeString(12, sSubscriptionsCount);
        }
    }

    static java.util.ArrayList<TagInfo> VAR_TYPE_4_VTTAGINFO = new java.util.ArrayList<TagInfo>();
    static {
        VAR_TYPE_4_VTTAGINFO.add(new TagInfo());
    }

    static java.util.ArrayList<IntelliStock> VAR_TYPE_4_VTINTELLISTOCK = new java.util.ArrayList<IntelliStock>();
    static {
        VAR_TYPE_4_VTINTELLISTOCK.add(new IntelliStock());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(2, false, VAR_TYPE_4_VTTAGINFO);
        this.vtIntelliStock = (java.util.ArrayList<IntelliStock>)istream.readList(3, false, VAR_TYPE_4_VTINTELLISTOCK);
        this.sUrl = (String)istream.readString(4, false, this.sUrl);
        this.sDate = (String)istream.readString(5, false, this.sDate);
        this.sSource = (String)istream.readString(6, false, this.sSource);
        this.fMaxRetAvgIncrease = (float)istream.readFloat(7, false, this.fMaxRetAvgIncrease);
        this.sMaxRetUpBanner = (String)istream.readString(8, false, this.sMaxRetUpBanner);
        this.iStatus = (int)istream.readInt32(9, false, this.iStatus);
        this.fSuccPercent = (float)istream.readFloat(10, false, this.fSuccPercent);
        this.iSubscriptionsCount = (int)istream.readInt32(11, false, this.iSubscriptionsCount);
        this.sSubscriptionsCount = (String)istream.readString(12, false, this.sSubscriptionsCount);
    }

}

