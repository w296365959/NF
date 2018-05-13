package BEC;

public final class CompanyDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.CompanyProfile stCompanyProfile = null;

    public java.util.ArrayList<BEC.PrimeOperatingRevenue> vPrimeOperatingRevenue = null;

    public java.util.ArrayList<BEC.DividendPayingPlacing> vDividendPayingPlacing = null;

    public BEC.CapitalStructure stCapitalStructure = null;

    public java.util.ArrayList<BEC.TopShareholder> vTopShareholder = null;

    public java.util.ArrayList<BEC.Fundsholder> vFundsholder = null;

    public java.util.ArrayList<BEC.ConcInfo> vConcInfo = null;

    public String sTopShareholderDate = "";

    public String sFundsholderDate = "";

    public java.util.ArrayList<BEC.PlateInfo> vPlateInfo = null;

    public java.util.ArrayList<BEC.SeniorExecutive> vtSExecutive = null;

    public java.util.ArrayList<BEC.IndustryCompare> vtIndustryCompare = null;

    public BEC.MainHolder stMainHolder = null;

    public java.util.ArrayList<BEC.TopShareholder> vTopHolder = null;

    public String sTopHolderDate = "";

    public BEC.CompanyProfile getStCompanyProfile()
    {
        return stCompanyProfile;
    }

    public void  setStCompanyProfile(BEC.CompanyProfile stCompanyProfile)
    {
        this.stCompanyProfile = stCompanyProfile;
    }

    public java.util.ArrayList<BEC.PrimeOperatingRevenue> getVPrimeOperatingRevenue()
    {
        return vPrimeOperatingRevenue;
    }

    public void  setVPrimeOperatingRevenue(java.util.ArrayList<BEC.PrimeOperatingRevenue> vPrimeOperatingRevenue)
    {
        this.vPrimeOperatingRevenue = vPrimeOperatingRevenue;
    }

    public java.util.ArrayList<BEC.DividendPayingPlacing> getVDividendPayingPlacing()
    {
        return vDividendPayingPlacing;
    }

    public void  setVDividendPayingPlacing(java.util.ArrayList<BEC.DividendPayingPlacing> vDividendPayingPlacing)
    {
        this.vDividendPayingPlacing = vDividendPayingPlacing;
    }

    public BEC.CapitalStructure getStCapitalStructure()
    {
        return stCapitalStructure;
    }

    public void  setStCapitalStructure(BEC.CapitalStructure stCapitalStructure)
    {
        this.stCapitalStructure = stCapitalStructure;
    }

    public java.util.ArrayList<BEC.TopShareholder> getVTopShareholder()
    {
        return vTopShareholder;
    }

    public void  setVTopShareholder(java.util.ArrayList<BEC.TopShareholder> vTopShareholder)
    {
        this.vTopShareholder = vTopShareholder;
    }

    public java.util.ArrayList<BEC.Fundsholder> getVFundsholder()
    {
        return vFundsholder;
    }

    public void  setVFundsholder(java.util.ArrayList<BEC.Fundsholder> vFundsholder)
    {
        this.vFundsholder = vFundsholder;
    }

    public java.util.ArrayList<BEC.ConcInfo> getVConcInfo()
    {
        return vConcInfo;
    }

    public void  setVConcInfo(java.util.ArrayList<BEC.ConcInfo> vConcInfo)
    {
        this.vConcInfo = vConcInfo;
    }

    public String getSTopShareholderDate()
    {
        return sTopShareholderDate;
    }

    public void  setSTopShareholderDate(String sTopShareholderDate)
    {
        this.sTopShareholderDate = sTopShareholderDate;
    }

    public String getSFundsholderDate()
    {
        return sFundsholderDate;
    }

    public void  setSFundsholderDate(String sFundsholderDate)
    {
        this.sFundsholderDate = sFundsholderDate;
    }

    public java.util.ArrayList<BEC.PlateInfo> getVPlateInfo()
    {
        return vPlateInfo;
    }

    public void  setVPlateInfo(java.util.ArrayList<BEC.PlateInfo> vPlateInfo)
    {
        this.vPlateInfo = vPlateInfo;
    }

    public java.util.ArrayList<BEC.SeniorExecutive> getVtSExecutive()
    {
        return vtSExecutive;
    }

    public void  setVtSExecutive(java.util.ArrayList<BEC.SeniorExecutive> vtSExecutive)
    {
        this.vtSExecutive = vtSExecutive;
    }

    public java.util.ArrayList<BEC.IndustryCompare> getVtIndustryCompare()
    {
        return vtIndustryCompare;
    }

    public void  setVtIndustryCompare(java.util.ArrayList<BEC.IndustryCompare> vtIndustryCompare)
    {
        this.vtIndustryCompare = vtIndustryCompare;
    }

    public BEC.MainHolder getStMainHolder()
    {
        return stMainHolder;
    }

    public void  setStMainHolder(BEC.MainHolder stMainHolder)
    {
        this.stMainHolder = stMainHolder;
    }

    public java.util.ArrayList<BEC.TopShareholder> getVTopHolder()
    {
        return vTopHolder;
    }

    public void  setVTopHolder(java.util.ArrayList<BEC.TopShareholder> vTopHolder)
    {
        this.vTopHolder = vTopHolder;
    }

    public String getSTopHolderDate()
    {
        return sTopHolderDate;
    }

    public void  setSTopHolderDate(String sTopHolderDate)
    {
        this.sTopHolderDate = sTopHolderDate;
    }

    public CompanyDesc()
    {
    }

    public CompanyDesc(BEC.CompanyProfile stCompanyProfile, java.util.ArrayList<BEC.PrimeOperatingRevenue> vPrimeOperatingRevenue, java.util.ArrayList<BEC.DividendPayingPlacing> vDividendPayingPlacing, BEC.CapitalStructure stCapitalStructure, java.util.ArrayList<BEC.TopShareholder> vTopShareholder, java.util.ArrayList<BEC.Fundsholder> vFundsholder, java.util.ArrayList<BEC.ConcInfo> vConcInfo, String sTopShareholderDate, String sFundsholderDate, java.util.ArrayList<BEC.PlateInfo> vPlateInfo, java.util.ArrayList<BEC.SeniorExecutive> vtSExecutive, java.util.ArrayList<BEC.IndustryCompare> vtIndustryCompare, BEC.MainHolder stMainHolder, java.util.ArrayList<BEC.TopShareholder> vTopHolder, String sTopHolderDate)
    {
        this.stCompanyProfile = stCompanyProfile;
        this.vPrimeOperatingRevenue = vPrimeOperatingRevenue;
        this.vDividendPayingPlacing = vDividendPayingPlacing;
        this.stCapitalStructure = stCapitalStructure;
        this.vTopShareholder = vTopShareholder;
        this.vFundsholder = vFundsholder;
        this.vConcInfo = vConcInfo;
        this.sTopShareholderDate = sTopShareholderDate;
        this.sFundsholderDate = sFundsholderDate;
        this.vPlateInfo = vPlateInfo;
        this.vtSExecutive = vtSExecutive;
        this.vtIndustryCompare = vtIndustryCompare;
        this.stMainHolder = stMainHolder;
        this.vTopHolder = vTopHolder;
        this.sTopHolderDate = sTopHolderDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stCompanyProfile) {
            ostream.writeMessage(0, stCompanyProfile);
        }
        if (null != vPrimeOperatingRevenue) {
            ostream.writeList(1, vPrimeOperatingRevenue);
        }
        if (null != vDividendPayingPlacing) {
            ostream.writeList(2, vDividendPayingPlacing);
        }
        if (null != stCapitalStructure) {
            ostream.writeMessage(3, stCapitalStructure);
        }
        if (null != vTopShareholder) {
            ostream.writeList(4, vTopShareholder);
        }
        if (null != vFundsholder) {
            ostream.writeList(5, vFundsholder);
        }
        if (null != vConcInfo) {
            ostream.writeList(6, vConcInfo);
        }
        if (null != sTopShareholderDate) {
            ostream.writeString(7, sTopShareholderDate);
        }
        if (null != sFundsholderDate) {
            ostream.writeString(8, sFundsholderDate);
        }
        if (null != vPlateInfo) {
            ostream.writeList(9, vPlateInfo);
        }
        if (null != vtSExecutive) {
            ostream.writeList(10, vtSExecutive);
        }
        if (null != vtIndustryCompare) {
            ostream.writeList(11, vtIndustryCompare);
        }
        if (null != stMainHolder) {
            ostream.writeMessage(12, stMainHolder);
        }
        if (null != vTopHolder) {
            ostream.writeList(13, vTopHolder);
        }
        if (null != sTopHolderDate) {
            ostream.writeString(14, sTopHolderDate);
        }
    }

    static BEC.CompanyProfile VAR_TYPE_4_STCOMPANYPROFILE = new BEC.CompanyProfile();

    static java.util.ArrayList<BEC.PrimeOperatingRevenue> VAR_TYPE_4_VPRIMEOPERATINGREVENUE = new java.util.ArrayList<BEC.PrimeOperatingRevenue>();
    static {
        VAR_TYPE_4_VPRIMEOPERATINGREVENUE.add(new BEC.PrimeOperatingRevenue());
    }

    static java.util.ArrayList<BEC.DividendPayingPlacing> VAR_TYPE_4_VDIVIDENDPAYINGPLACING = new java.util.ArrayList<BEC.DividendPayingPlacing>();
    static {
        VAR_TYPE_4_VDIVIDENDPAYINGPLACING.add(new BEC.DividendPayingPlacing());
    }

    static BEC.CapitalStructure VAR_TYPE_4_STCAPITALSTRUCTURE = new BEC.CapitalStructure();

    static java.util.ArrayList<BEC.TopShareholder> VAR_TYPE_4_VTOPSHAREHOLDER = new java.util.ArrayList<BEC.TopShareholder>();
    static {
        VAR_TYPE_4_VTOPSHAREHOLDER.add(new BEC.TopShareholder());
    }

    static java.util.ArrayList<BEC.Fundsholder> VAR_TYPE_4_VFUNDSHOLDER = new java.util.ArrayList<BEC.Fundsholder>();
    static {
        VAR_TYPE_4_VFUNDSHOLDER.add(new BEC.Fundsholder());
    }

    static java.util.ArrayList<BEC.ConcInfo> VAR_TYPE_4_VCONCINFO = new java.util.ArrayList<BEC.ConcInfo>();
    static {
        VAR_TYPE_4_VCONCINFO.add(new BEC.ConcInfo());
    }

    static java.util.ArrayList<BEC.PlateInfo> VAR_TYPE_4_VPLATEINFO = new java.util.ArrayList<BEC.PlateInfo>();
    static {
        VAR_TYPE_4_VPLATEINFO.add(new BEC.PlateInfo());
    }

    static java.util.ArrayList<BEC.SeniorExecutive> VAR_TYPE_4_VTSEXECUTIVE = new java.util.ArrayList<BEC.SeniorExecutive>();
    static {
        VAR_TYPE_4_VTSEXECUTIVE.add(new BEC.SeniorExecutive());
    }

    static java.util.ArrayList<BEC.IndustryCompare> VAR_TYPE_4_VTINDUSTRYCOMPARE = new java.util.ArrayList<BEC.IndustryCompare>();
    static {
        VAR_TYPE_4_VTINDUSTRYCOMPARE.add(new BEC.IndustryCompare());
    }

    static BEC.MainHolder VAR_TYPE_4_STMAINHOLDER = new BEC.MainHolder();

    static java.util.ArrayList<BEC.TopShareholder> VAR_TYPE_4_VTOPHOLDER = new java.util.ArrayList<BEC.TopShareholder>();
    static {
        VAR_TYPE_4_VTOPHOLDER.add(new BEC.TopShareholder());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stCompanyProfile = (BEC.CompanyProfile)istream.readMessage(0, false, VAR_TYPE_4_STCOMPANYPROFILE);
        this.vPrimeOperatingRevenue = (java.util.ArrayList<BEC.PrimeOperatingRevenue>)istream.readList(1, false, VAR_TYPE_4_VPRIMEOPERATINGREVENUE);
        this.vDividendPayingPlacing = (java.util.ArrayList<BEC.DividendPayingPlacing>)istream.readList(2, false, VAR_TYPE_4_VDIVIDENDPAYINGPLACING);
        this.stCapitalStructure = (BEC.CapitalStructure)istream.readMessage(3, false, VAR_TYPE_4_STCAPITALSTRUCTURE);
        this.vTopShareholder = (java.util.ArrayList<BEC.TopShareholder>)istream.readList(4, false, VAR_TYPE_4_VTOPSHAREHOLDER);
        this.vFundsholder = (java.util.ArrayList<BEC.Fundsholder>)istream.readList(5, false, VAR_TYPE_4_VFUNDSHOLDER);
        this.vConcInfo = (java.util.ArrayList<BEC.ConcInfo>)istream.readList(6, false, VAR_TYPE_4_VCONCINFO);
        this.sTopShareholderDate = (String)istream.readString(7, false, this.sTopShareholderDate);
        this.sFundsholderDate = (String)istream.readString(8, false, this.sFundsholderDate);
        this.vPlateInfo = (java.util.ArrayList<BEC.PlateInfo>)istream.readList(9, false, VAR_TYPE_4_VPLATEINFO);
        this.vtSExecutive = (java.util.ArrayList<BEC.SeniorExecutive>)istream.readList(10, false, VAR_TYPE_4_VTSEXECUTIVE);
        this.vtIndustryCompare = (java.util.ArrayList<BEC.IndustryCompare>)istream.readList(11, false, VAR_TYPE_4_VTINDUSTRYCOMPARE);
        this.stMainHolder = (BEC.MainHolder)istream.readMessage(12, false, VAR_TYPE_4_STMAINHOLDER);
        this.vTopHolder = (java.util.ArrayList<BEC.TopShareholder>)istream.readList(13, false, VAR_TYPE_4_VTOPHOLDER);
        this.sTopHolderDate = (String)istream.readString(14, false, this.sTopHolderDate);
    }

}

