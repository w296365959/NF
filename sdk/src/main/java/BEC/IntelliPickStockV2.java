package BEC;

public final class IntelliPickStockV2 extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sTitle = "";

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public String sDescription = "";

    public String sSource = "";

    public java.util.ArrayList<Float> vtAvgIncrease = null;

    public java.util.ArrayList<Float> vtSuccPercent = null;

    public java.util.ArrayList<Float> vtYearEarning = null;

    public String sDate = "";

    public java.util.ArrayList<IntelliStock> vtIntelliStock = null;

    public java.util.ArrayList<IntelliBroker> vBrokerList = null;

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

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public String getSSource()
    {
        return sSource;
    }

    public void  setSSource(String sSource)
    {
        this.sSource = sSource;
    }

    public java.util.ArrayList<Float> getVtAvgIncrease()
    {
        return vtAvgIncrease;
    }

    public void  setVtAvgIncrease(java.util.ArrayList<Float> vtAvgIncrease)
    {
        this.vtAvgIncrease = vtAvgIncrease;
    }

    public java.util.ArrayList<Float> getVtSuccPercent()
    {
        return vtSuccPercent;
    }

    public void  setVtSuccPercent(java.util.ArrayList<Float> vtSuccPercent)
    {
        this.vtSuccPercent = vtSuccPercent;
    }

    public java.util.ArrayList<Float> getVtYearEarning()
    {
        return vtYearEarning;
    }

    public void  setVtYearEarning(java.util.ArrayList<Float> vtYearEarning)
    {
        this.vtYearEarning = vtYearEarning;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public java.util.ArrayList<IntelliStock> getVtIntelliStock()
    {
        return vtIntelliStock;
    }

    public void  setVtIntelliStock(java.util.ArrayList<IntelliStock> vtIntelliStock)
    {
        this.vtIntelliStock = vtIntelliStock;
    }

    public java.util.ArrayList<IntelliBroker> getVBrokerList()
    {
        return vBrokerList;
    }

    public void  setVBrokerList(java.util.ArrayList<IntelliBroker> vBrokerList)
    {
        this.vBrokerList = vBrokerList;
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

    public IntelliPickStockV2()
    {
    }

    public IntelliPickStockV2(String sId, String sTitle, java.util.ArrayList<TagInfo> vtTagInfo, String sDescription, String sSource, java.util.ArrayList<Float> vtAvgIncrease, java.util.ArrayList<Float> vtSuccPercent, java.util.ArrayList<Float> vtYearEarning, String sDate, java.util.ArrayList<IntelliStock> vtIntelliStock, java.util.ArrayList<IntelliBroker> vBrokerList, int iSubscriptionsCount, String sSubscriptionsCount)
    {
        this.sId = sId;
        this.sTitle = sTitle;
        this.vtTagInfo = vtTagInfo;
        this.sDescription = sDescription;
        this.sSource = sSource;
        this.vtAvgIncrease = vtAvgIncrease;
        this.vtSuccPercent = vtSuccPercent;
        this.vtYearEarning = vtYearEarning;
        this.sDate = sDate;
        this.vtIntelliStock = vtIntelliStock;
        this.vBrokerList = vBrokerList;
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
        if (null != sDescription) {
            ostream.writeString(3, sDescription);
        }
        if (null != sSource) {
            ostream.writeString(4, sSource);
        }
        if (null != vtAvgIncrease) {
            ostream.writeList(5, vtAvgIncrease);
        }
        if (null != vtSuccPercent) {
            ostream.writeList(6, vtSuccPercent);
        }
        if (null != vtYearEarning) {
            ostream.writeList(7, vtYearEarning);
        }
        if (null != sDate) {
            ostream.writeString(8, sDate);
        }
        if (null != vtIntelliStock) {
            ostream.writeList(9, vtIntelliStock);
        }
        if (null != vBrokerList) {
            ostream.writeList(10, vBrokerList);
        }
        ostream.writeInt32(11, iSubscriptionsCount);
        if (null != sSubscriptionsCount) {
            ostream.writeString(12, sSubscriptionsCount);
        }
    }

    static java.util.ArrayList<TagInfo> VAR_TYPE_4_VTTAGINFO = new java.util.ArrayList<TagInfo>();
    static {
        VAR_TYPE_4_VTTAGINFO.add(new TagInfo());
    }

    static java.util.ArrayList<Float> VAR_TYPE_4_VTAVGINCREASE = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VTAVGINCREASE.add(0.0f);
    }

    static java.util.ArrayList<Float> VAR_TYPE_4_VTSUCCPERCENT = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VTSUCCPERCENT.add(0.0f);
    }

    static java.util.ArrayList<Float> VAR_TYPE_4_VTYEAREARNING = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VTYEAREARNING.add(0.0f);
    }

    static java.util.ArrayList<IntelliStock> VAR_TYPE_4_VTINTELLISTOCK = new java.util.ArrayList<IntelliStock>();
    static {
        VAR_TYPE_4_VTINTELLISTOCK.add(new IntelliStock());
    }

    static java.util.ArrayList<IntelliBroker> VAR_TYPE_4_VBROKERLIST = new java.util.ArrayList<IntelliBroker>();
    static {
        VAR_TYPE_4_VBROKERLIST.add(new IntelliBroker());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(2, false, VAR_TYPE_4_VTTAGINFO);
        this.sDescription = (String)istream.readString(3, false, this.sDescription);
        this.sSource = (String)istream.readString(4, false, this.sSource);
        this.vtAvgIncrease = (java.util.ArrayList<Float>)istream.readList(5, false, VAR_TYPE_4_VTAVGINCREASE);
        this.vtSuccPercent = (java.util.ArrayList<Float>)istream.readList(6, false, VAR_TYPE_4_VTSUCCPERCENT);
        this.vtYearEarning = (java.util.ArrayList<Float>)istream.readList(7, false, VAR_TYPE_4_VTYEAREARNING);
        this.sDate = (String)istream.readString(8, false, this.sDate);
        this.vtIntelliStock = (java.util.ArrayList<IntelliStock>)istream.readList(9, false, VAR_TYPE_4_VTINTELLISTOCK);
        this.vBrokerList = (java.util.ArrayList<IntelliBroker>)istream.readList(10, false, VAR_TYPE_4_VBROKERLIST);
        this.iSubscriptionsCount = (int)istream.readInt32(11, false, this.iSubscriptionsCount);
        this.sSubscriptionsCount = (String)istream.readString(12, false, this.sSubscriptionsCount);
    }

}

