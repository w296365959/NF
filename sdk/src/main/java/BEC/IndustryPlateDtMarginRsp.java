package BEC;

public final class IndustryPlateDtMarginRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.MarginTradeDt> vtIndustryBuyValue = null;

    public java.util.ArrayList<BEC.MarginTradeDt> getVtIndustryBuyValue()
    {
        return vtIndustryBuyValue;
    }

    public void  setVtIndustryBuyValue(java.util.ArrayList<BEC.MarginTradeDt> vtIndustryBuyValue)
    {
        this.vtIndustryBuyValue = vtIndustryBuyValue;
    }

    public IndustryPlateDtMarginRsp()
    {
    }

    public IndustryPlateDtMarginRsp(java.util.ArrayList<BEC.MarginTradeDt> vtIndustryBuyValue)
    {
        this.vtIndustryBuyValue = vtIndustryBuyValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtIndustryBuyValue) {
            ostream.writeList(0, vtIndustryBuyValue);
        }
    }

    static java.util.ArrayList<BEC.MarginTradeDt> VAR_TYPE_4_VTINDUSTRYBUYVALUE = new java.util.ArrayList<BEC.MarginTradeDt>();
    static {
        VAR_TYPE_4_VTINDUSTRYBUYVALUE.add(new BEC.MarginTradeDt());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtIndustryBuyValue = (java.util.ArrayList<BEC.MarginTradeDt>)istream.readList(0, false, VAR_TYPE_4_VTINDUSTRYBUYVALUE);
    }

}

