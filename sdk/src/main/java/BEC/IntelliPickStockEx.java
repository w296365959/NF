package BEC;

public final class IntelliPickStockEx extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sTitle = "";

    public int eBackgroundType = 0;

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public java.util.ArrayList<IntelliStock> vtIntelliStock = null;

    public String sUrl = "";

    public String sDate = "";

    public String sDescription = "";

    public int iHoldingPeriod = 0;

    public float fAvgIncrease = 0;

    public float fMorrowAvgIncrease = 0;

    public String sSource = "";

    public java.util.ArrayList<IncomeResult> vIncomeResult = null;

    public float fSuccPrecent = 0;

    public float fAnnualizedReturn = 0;

    public String sUpBanner = "";

    public String sDownBanner = "";

    public float fWeekIncrease = 0;

    public float fTodayIncrease = 0;

    public float fMaxSuccPercent = 0;

    public int iMaxSuccHoldingPeriod = 0;

    public float fWeekIncreaseEx = 0;

    public boolean bNew = true;

    public float fMaxRetAvgIncrease = 0;

    public String sMaxRetUpBanner = "";

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

    public int getEBackgroundType()
    {
        return eBackgroundType;
    }

    public void  setEBackgroundType(int eBackgroundType)
    {
        this.eBackgroundType = eBackgroundType;
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

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public int getIHoldingPeriod()
    {
        return iHoldingPeriod;
    }

    public void  setIHoldingPeriod(int iHoldingPeriod)
    {
        this.iHoldingPeriod = iHoldingPeriod;
    }

    public float getFAvgIncrease()
    {
        return fAvgIncrease;
    }

    public void  setFAvgIncrease(float fAvgIncrease)
    {
        this.fAvgIncrease = fAvgIncrease;
    }

    public float getFMorrowAvgIncrease()
    {
        return fMorrowAvgIncrease;
    }

    public void  setFMorrowAvgIncrease(float fMorrowAvgIncrease)
    {
        this.fMorrowAvgIncrease = fMorrowAvgIncrease;
    }

    public String getSSource()
    {
        return sSource;
    }

    public void  setSSource(String sSource)
    {
        this.sSource = sSource;
    }

    public java.util.ArrayList<IncomeResult> getVIncomeResult()
    {
        return vIncomeResult;
    }

    public void  setVIncomeResult(java.util.ArrayList<IncomeResult> vIncomeResult)
    {
        this.vIncomeResult = vIncomeResult;
    }

    public float getFSuccPrecent()
    {
        return fSuccPrecent;
    }

    public void  setFSuccPrecent(float fSuccPrecent)
    {
        this.fSuccPrecent = fSuccPrecent;
    }

    public float getFAnnualizedReturn()
    {
        return fAnnualizedReturn;
    }

    public void  setFAnnualizedReturn(float fAnnualizedReturn)
    {
        this.fAnnualizedReturn = fAnnualizedReturn;
    }

    public String getSUpBanner()
    {
        return sUpBanner;
    }

    public void  setSUpBanner(String sUpBanner)
    {
        this.sUpBanner = sUpBanner;
    }

    public String getSDownBanner()
    {
        return sDownBanner;
    }

    public void  setSDownBanner(String sDownBanner)
    {
        this.sDownBanner = sDownBanner;
    }

    public float getFWeekIncrease()
    {
        return fWeekIncrease;
    }

    public void  setFWeekIncrease(float fWeekIncrease)
    {
        this.fWeekIncrease = fWeekIncrease;
    }

    public float getFTodayIncrease()
    {
        return fTodayIncrease;
    }

    public void  setFTodayIncrease(float fTodayIncrease)
    {
        this.fTodayIncrease = fTodayIncrease;
    }

    public float getFMaxSuccPercent()
    {
        return fMaxSuccPercent;
    }

    public void  setFMaxSuccPercent(float fMaxSuccPercent)
    {
        this.fMaxSuccPercent = fMaxSuccPercent;
    }

    public int getIMaxSuccHoldingPeriod()
    {
        return iMaxSuccHoldingPeriod;
    }

    public void  setIMaxSuccHoldingPeriod(int iMaxSuccHoldingPeriod)
    {
        this.iMaxSuccHoldingPeriod = iMaxSuccHoldingPeriod;
    }

    public float getFWeekIncreaseEx()
    {
        return fWeekIncreaseEx;
    }

    public void  setFWeekIncreaseEx(float fWeekIncreaseEx)
    {
        this.fWeekIncreaseEx = fWeekIncreaseEx;
    }

    public boolean getBNew()
    {
        return bNew;
    }

    public void  setBNew(boolean bNew)
    {
        this.bNew = bNew;
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

    public IntelliPickStockEx()
    {
    }

    public IntelliPickStockEx(String sId, String sTitle, int eBackgroundType, java.util.ArrayList<TagInfo> vtTagInfo, java.util.ArrayList<IntelliStock> vtIntelliStock, String sUrl, String sDate, String sDescription, int iHoldingPeriod, float fAvgIncrease, float fMorrowAvgIncrease, String sSource, java.util.ArrayList<IncomeResult> vIncomeResult, float fSuccPrecent, float fAnnualizedReturn, String sUpBanner, String sDownBanner, float fWeekIncrease, float fTodayIncrease, float fMaxSuccPercent, int iMaxSuccHoldingPeriod, float fWeekIncreaseEx, boolean bNew, float fMaxRetAvgIncrease, String sMaxRetUpBanner, int iSubscriptionsCount, String sSubscriptionsCount)
    {
        this.sId = sId;
        this.sTitle = sTitle;
        this.eBackgroundType = eBackgroundType;
        this.vtTagInfo = vtTagInfo;
        this.vtIntelliStock = vtIntelliStock;
        this.sUrl = sUrl;
        this.sDate = sDate;
        this.sDescription = sDescription;
        this.iHoldingPeriod = iHoldingPeriod;
        this.fAvgIncrease = fAvgIncrease;
        this.fMorrowAvgIncrease = fMorrowAvgIncrease;
        this.sSource = sSource;
        this.vIncomeResult = vIncomeResult;
        this.fSuccPrecent = fSuccPrecent;
        this.fAnnualizedReturn = fAnnualizedReturn;
        this.sUpBanner = sUpBanner;
        this.sDownBanner = sDownBanner;
        this.fWeekIncrease = fWeekIncrease;
        this.fTodayIncrease = fTodayIncrease;
        this.fMaxSuccPercent = fMaxSuccPercent;
        this.iMaxSuccHoldingPeriod = iMaxSuccHoldingPeriod;
        this.fWeekIncreaseEx = fWeekIncreaseEx;
        this.bNew = bNew;
        this.fMaxRetAvgIncrease = fMaxRetAvgIncrease;
        this.sMaxRetUpBanner = sMaxRetUpBanner;
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
        ostream.writeInt32(2, eBackgroundType);
        if (null != vtTagInfo) {
            ostream.writeList(3, vtTagInfo);
        }
        if (null != vtIntelliStock) {
            ostream.writeList(4, vtIntelliStock);
        }
        if (null != sUrl) {
            ostream.writeString(5, sUrl);
        }
        if (null != sDate) {
            ostream.writeString(6, sDate);
        }
        if (null != sDescription) {
            ostream.writeString(7, sDescription);
        }
        ostream.writeInt32(8, iHoldingPeriod);
        ostream.writeFloat(9, fAvgIncrease);
        ostream.writeFloat(10, fMorrowAvgIncrease);
        if (null != sSource) {
            ostream.writeString(11, sSource);
        }
        if (null != vIncomeResult) {
            ostream.writeList(12, vIncomeResult);
        }
        ostream.writeFloat(13, fSuccPrecent);
        ostream.writeFloat(14, fAnnualizedReturn);
        if (null != sUpBanner) {
            ostream.writeString(15, sUpBanner);
        }
        if (null != sDownBanner) {
            ostream.writeString(16, sDownBanner);
        }
        ostream.writeFloat(17, fWeekIncrease);
        ostream.writeFloat(18, fTodayIncrease);
        ostream.writeFloat(19, fMaxSuccPercent);
        ostream.writeInt32(20, iMaxSuccHoldingPeriod);
        ostream.writeFloat(21, fWeekIncreaseEx);
        ostream.writeBoolean(22, bNew);
        ostream.writeFloat(23, fMaxRetAvgIncrease);
        if (null != sMaxRetUpBanner) {
            ostream.writeString(24, sMaxRetUpBanner);
        }
        ostream.writeInt32(25, iSubscriptionsCount);
        if (null != sSubscriptionsCount) {
            ostream.writeString(26, sSubscriptionsCount);
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

    static java.util.ArrayList<IncomeResult> VAR_TYPE_4_VINCOMERESULT = new java.util.ArrayList<IncomeResult>();
    static {
        VAR_TYPE_4_VINCOMERESULT.add(new IncomeResult());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.eBackgroundType = (int)istream.readInt32(2, false, this.eBackgroundType);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(3, false, VAR_TYPE_4_VTTAGINFO);
        this.vtIntelliStock = (java.util.ArrayList<IntelliStock>)istream.readList(4, false, VAR_TYPE_4_VTINTELLISTOCK);
        this.sUrl = (String)istream.readString(5, false, this.sUrl);
        this.sDate = (String)istream.readString(6, false, this.sDate);
        this.sDescription = (String)istream.readString(7, false, this.sDescription);
        this.iHoldingPeriod = (int)istream.readInt32(8, false, this.iHoldingPeriod);
        this.fAvgIncrease = (float)istream.readFloat(9, false, this.fAvgIncrease);
        this.fMorrowAvgIncrease = (float)istream.readFloat(10, false, this.fMorrowAvgIncrease);
        this.sSource = (String)istream.readString(11, false, this.sSource);
        this.vIncomeResult = (java.util.ArrayList<IncomeResult>)istream.readList(12, false, VAR_TYPE_4_VINCOMERESULT);
        this.fSuccPrecent = (float)istream.readFloat(13, false, this.fSuccPrecent);
        this.fAnnualizedReturn = (float)istream.readFloat(14, false, this.fAnnualizedReturn);
        this.sUpBanner = (String)istream.readString(15, false, this.sUpBanner);
        this.sDownBanner = (String)istream.readString(16, false, this.sDownBanner);
        this.fWeekIncrease = (float)istream.readFloat(17, false, this.fWeekIncrease);
        this.fTodayIncrease = (float)istream.readFloat(18, false, this.fTodayIncrease);
        this.fMaxSuccPercent = (float)istream.readFloat(19, false, this.fMaxSuccPercent);
        this.iMaxSuccHoldingPeriod = (int)istream.readInt32(20, false, this.iMaxSuccHoldingPeriod);
        this.fWeekIncreaseEx = (float)istream.readFloat(21, false, this.fWeekIncreaseEx);
        this.bNew = (boolean)istream.readBoolean(22, false, this.bNew);
        this.fMaxRetAvgIncrease = (float)istream.readFloat(23, false, this.fMaxRetAvgIncrease);
        this.sMaxRetUpBanner = (String)istream.readString(24, false, this.sMaxRetUpBanner);
        this.iSubscriptionsCount = (int)istream.readInt32(25, false, this.iSubscriptionsCount);
        this.sSubscriptionsCount = (String)istream.readString(26, false, this.sSubscriptionsCount);
    }

}

