package BEC;

public final class CompanyProfile extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String sListingDate = "";

    public String sIssuePrice = "";

    public String sIssuanceNumber = "";

    public String sRegion = "";

    public String sIndustry = "";

    public String sMainBusiness = "";

    public String sListedAddr = "";

    public String sCountry = "";

    public String sSecCategory = "";

    public String sTradingUnit = "";

    public String sTransferType = "";

    public String sMarketStartDate = "";

    public String sMarketMakers = "";

    public String sRegistryLocate = "";

    public java.util.ArrayList<String> vController = null;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSListingDate()
    {
        return sListingDate;
    }

    public void  setSListingDate(String sListingDate)
    {
        this.sListingDate = sListingDate;
    }

    public String getSIssuePrice()
    {
        return sIssuePrice;
    }

    public void  setSIssuePrice(String sIssuePrice)
    {
        this.sIssuePrice = sIssuePrice;
    }

    public String getSIssuanceNumber()
    {
        return sIssuanceNumber;
    }

    public void  setSIssuanceNumber(String sIssuanceNumber)
    {
        this.sIssuanceNumber = sIssuanceNumber;
    }

    public String getSRegion()
    {
        return sRegion;
    }

    public void  setSRegion(String sRegion)
    {
        this.sRegion = sRegion;
    }

    public String getSIndustry()
    {
        return sIndustry;
    }

    public void  setSIndustry(String sIndustry)
    {
        this.sIndustry = sIndustry;
    }

    public String getSMainBusiness()
    {
        return sMainBusiness;
    }

    public void  setSMainBusiness(String sMainBusiness)
    {
        this.sMainBusiness = sMainBusiness;
    }

    public String getSListedAddr()
    {
        return sListedAddr;
    }

    public void  setSListedAddr(String sListedAddr)
    {
        this.sListedAddr = sListedAddr;
    }

    public String getSCountry()
    {
        return sCountry;
    }

    public void  setSCountry(String sCountry)
    {
        this.sCountry = sCountry;
    }

    public String getSSecCategory()
    {
        return sSecCategory;
    }

    public void  setSSecCategory(String sSecCategory)
    {
        this.sSecCategory = sSecCategory;
    }

    public String getSTradingUnit()
    {
        return sTradingUnit;
    }

    public void  setSTradingUnit(String sTradingUnit)
    {
        this.sTradingUnit = sTradingUnit;
    }

    public String getSTransferType()
    {
        return sTransferType;
    }

    public void  setSTransferType(String sTransferType)
    {
        this.sTransferType = sTransferType;
    }

    public String getSMarketStartDate()
    {
        return sMarketStartDate;
    }

    public void  setSMarketStartDate(String sMarketStartDate)
    {
        this.sMarketStartDate = sMarketStartDate;
    }

    public String getSMarketMakers()
    {
        return sMarketMakers;
    }

    public void  setSMarketMakers(String sMarketMakers)
    {
        this.sMarketMakers = sMarketMakers;
    }

    public String getSRegistryLocate()
    {
        return sRegistryLocate;
    }

    public void  setSRegistryLocate(String sRegistryLocate)
    {
        this.sRegistryLocate = sRegistryLocate;
    }

    public java.util.ArrayList<String> getVController()
    {
        return vController;
    }

    public void  setVController(java.util.ArrayList<String> vController)
    {
        this.vController = vController;
    }

    public CompanyProfile()
    {
    }

    public CompanyProfile(String sName, String sListingDate, String sIssuePrice, String sIssuanceNumber, String sRegion, String sIndustry, String sMainBusiness, String sListedAddr, String sCountry, String sSecCategory, String sTradingUnit, String sTransferType, String sMarketStartDate, String sMarketMakers, String sRegistryLocate, java.util.ArrayList<String> vController)
    {
        this.sName = sName;
        this.sListingDate = sListingDate;
        this.sIssuePrice = sIssuePrice;
        this.sIssuanceNumber = sIssuanceNumber;
        this.sRegion = sRegion;
        this.sIndustry = sIndustry;
        this.sMainBusiness = sMainBusiness;
        this.sListedAddr = sListedAddr;
        this.sCountry = sCountry;
        this.sSecCategory = sSecCategory;
        this.sTradingUnit = sTradingUnit;
        this.sTransferType = sTransferType;
        this.sMarketStartDate = sMarketStartDate;
        this.sMarketMakers = sMarketMakers;
        this.sRegistryLocate = sRegistryLocate;
        this.vController = vController;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != sListingDate) {
            ostream.writeString(1, sListingDate);
        }
        if (null != sIssuePrice) {
            ostream.writeString(2, sIssuePrice);
        }
        if (null != sIssuanceNumber) {
            ostream.writeString(3, sIssuanceNumber);
        }
        if (null != sRegion) {
            ostream.writeString(4, sRegion);
        }
        if (null != sIndustry) {
            ostream.writeString(5, sIndustry);
        }
        if (null != sMainBusiness) {
            ostream.writeString(6, sMainBusiness);
        }
        if (null != sListedAddr) {
            ostream.writeString(7, sListedAddr);
        }
        if (null != sCountry) {
            ostream.writeString(8, sCountry);
        }
        if (null != sSecCategory) {
            ostream.writeString(9, sSecCategory);
        }
        if (null != sTradingUnit) {
            ostream.writeString(10, sTradingUnit);
        }
        if (null != sTransferType) {
            ostream.writeString(11, sTransferType);
        }
        if (null != sMarketStartDate) {
            ostream.writeString(12, sMarketStartDate);
        }
        if (null != sMarketMakers) {
            ostream.writeString(13, sMarketMakers);
        }
        if (null != sRegistryLocate) {
            ostream.writeString(14, sRegistryLocate);
        }
        if (null != vController) {
            ostream.writeList(15, vController);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VCONTROLLER = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VCONTROLLER.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sListingDate = (String)istream.readString(1, false, this.sListingDate);
        this.sIssuePrice = (String)istream.readString(2, false, this.sIssuePrice);
        this.sIssuanceNumber = (String)istream.readString(3, false, this.sIssuanceNumber);
        this.sRegion = (String)istream.readString(4, false, this.sRegion);
        this.sIndustry = (String)istream.readString(5, false, this.sIndustry);
        this.sMainBusiness = (String)istream.readString(6, false, this.sMainBusiness);
        this.sListedAddr = (String)istream.readString(7, false, this.sListedAddr);
        this.sCountry = (String)istream.readString(8, false, this.sCountry);
        this.sSecCategory = (String)istream.readString(9, false, this.sSecCategory);
        this.sTradingUnit = (String)istream.readString(10, false, this.sTradingUnit);
        this.sTransferType = (String)istream.readString(11, false, this.sTransferType);
        this.sMarketStartDate = (String)istream.readString(12, false, this.sMarketStartDate);
        this.sMarketMakers = (String)istream.readString(13, false, this.sMarketMakers);
        this.sRegistryLocate = (String)istream.readString(14, false, this.sRegistryLocate);
        this.vController = (java.util.ArrayList<String>)istream.readList(15, false, VAR_TYPE_4_VCONTROLLER);
    }

}

