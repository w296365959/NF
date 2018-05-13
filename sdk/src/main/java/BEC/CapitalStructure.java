package BEC;

public final class CapitalStructure extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sEquity = "";

    public String sCirculationStock = "";

    public String sShareholderNumber = "";

    public String sPerCapitaHoldings = "";

    public java.util.ArrayList<BEC.CapitalStructureDetail> vCapitalStructureDetail = null;

    public String sDate = "";

    public String sFloatShare = "";

    public String getSEquity()
    {
        return sEquity;
    }

    public void  setSEquity(String sEquity)
    {
        this.sEquity = sEquity;
    }

    public String getSCirculationStock()
    {
        return sCirculationStock;
    }

    public void  setSCirculationStock(String sCirculationStock)
    {
        this.sCirculationStock = sCirculationStock;
    }

    public String getSShareholderNumber()
    {
        return sShareholderNumber;
    }

    public void  setSShareholderNumber(String sShareholderNumber)
    {
        this.sShareholderNumber = sShareholderNumber;
    }

    public String getSPerCapitaHoldings()
    {
        return sPerCapitaHoldings;
    }

    public void  setSPerCapitaHoldings(String sPerCapitaHoldings)
    {
        this.sPerCapitaHoldings = sPerCapitaHoldings;
    }

    public java.util.ArrayList<BEC.CapitalStructureDetail> getVCapitalStructureDetail()
    {
        return vCapitalStructureDetail;
    }

    public void  setVCapitalStructureDetail(java.util.ArrayList<BEC.CapitalStructureDetail> vCapitalStructureDetail)
    {
        this.vCapitalStructureDetail = vCapitalStructureDetail;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getSFloatShare()
    {
        return sFloatShare;
    }

    public void  setSFloatShare(String sFloatShare)
    {
        this.sFloatShare = sFloatShare;
    }

    public CapitalStructure()
    {
    }

    public CapitalStructure(String sEquity, String sCirculationStock, String sShareholderNumber, String sPerCapitaHoldings, java.util.ArrayList<BEC.CapitalStructureDetail> vCapitalStructureDetail, String sDate, String sFloatShare)
    {
        this.sEquity = sEquity;
        this.sCirculationStock = sCirculationStock;
        this.sShareholderNumber = sShareholderNumber;
        this.sPerCapitaHoldings = sPerCapitaHoldings;
        this.vCapitalStructureDetail = vCapitalStructureDetail;
        this.sDate = sDate;
        this.sFloatShare = sFloatShare;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sEquity) {
            ostream.writeString(0, sEquity);
        }
        if (null != sCirculationStock) {
            ostream.writeString(1, sCirculationStock);
        }
        if (null != sShareholderNumber) {
            ostream.writeString(2, sShareholderNumber);
        }
        if (null != sPerCapitaHoldings) {
            ostream.writeString(3, sPerCapitaHoldings);
        }
        if (null != vCapitalStructureDetail) {
            ostream.writeList(4, vCapitalStructureDetail);
        }
        if (null != sDate) {
            ostream.writeString(5, sDate);
        }
        if (null != sFloatShare) {
            ostream.writeString(6, sFloatShare);
        }
    }

    static java.util.ArrayList<BEC.CapitalStructureDetail> VAR_TYPE_4_VCAPITALSTRUCTUREDETAIL = new java.util.ArrayList<BEC.CapitalStructureDetail>();
    static {
        VAR_TYPE_4_VCAPITALSTRUCTUREDETAIL.add(new BEC.CapitalStructureDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sEquity = (String)istream.readString(0, false, this.sEquity);
        this.sCirculationStock = (String)istream.readString(1, false, this.sCirculationStock);
        this.sShareholderNumber = (String)istream.readString(2, false, this.sShareholderNumber);
        this.sPerCapitaHoldings = (String)istream.readString(3, false, this.sPerCapitaHoldings);
        this.vCapitalStructureDetail = (java.util.ArrayList<BEC.CapitalStructureDetail>)istream.readList(4, false, VAR_TYPE_4_VCAPITALSTRUCTUREDETAIL);
        this.sDate = (String)istream.readString(5, false, this.sDate);
        this.sFloatShare = (String)istream.readString(6, false, this.sFloatShare);
    }

}

