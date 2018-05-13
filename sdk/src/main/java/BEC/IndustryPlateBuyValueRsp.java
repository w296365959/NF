package BEC;

public final class IndustryPlateBuyValueRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.PlateBuyValue> vtIndustryBuyValue = null;

    public java.util.ArrayList<BEC.PlateBuyValue> vtPlateBuyValue = null;

    public java.util.ArrayList<BEC.PlateBuyValue> getVtIndustryBuyValue()
    {
        return vtIndustryBuyValue;
    }

    public void  setVtIndustryBuyValue(java.util.ArrayList<BEC.PlateBuyValue> vtIndustryBuyValue)
    {
        this.vtIndustryBuyValue = vtIndustryBuyValue;
    }

    public java.util.ArrayList<BEC.PlateBuyValue> getVtPlateBuyValue()
    {
        return vtPlateBuyValue;
    }

    public void  setVtPlateBuyValue(java.util.ArrayList<BEC.PlateBuyValue> vtPlateBuyValue)
    {
        this.vtPlateBuyValue = vtPlateBuyValue;
    }

    public IndustryPlateBuyValueRsp()
    {
    }

    public IndustryPlateBuyValueRsp(java.util.ArrayList<BEC.PlateBuyValue> vtIndustryBuyValue, java.util.ArrayList<BEC.PlateBuyValue> vtPlateBuyValue)
    {
        this.vtIndustryBuyValue = vtIndustryBuyValue;
        this.vtPlateBuyValue = vtPlateBuyValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtIndustryBuyValue) {
            ostream.writeList(0, vtIndustryBuyValue);
        }
        if (null != vtPlateBuyValue) {
            ostream.writeList(1, vtPlateBuyValue);
        }
    }

    static java.util.ArrayList<BEC.PlateBuyValue> VAR_TYPE_4_VTINDUSTRYBUYVALUE = new java.util.ArrayList<BEC.PlateBuyValue>();
    static {
        VAR_TYPE_4_VTINDUSTRYBUYVALUE.add(new BEC.PlateBuyValue());
    }

    static java.util.ArrayList<BEC.PlateBuyValue> VAR_TYPE_4_VTPLATEBUYVALUE = new java.util.ArrayList<BEC.PlateBuyValue>();
    static {
        VAR_TYPE_4_VTPLATEBUYVALUE.add(new BEC.PlateBuyValue());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtIndustryBuyValue = (java.util.ArrayList<BEC.PlateBuyValue>)istream.readList(0, false, VAR_TYPE_4_VTINDUSTRYBUYVALUE);
        this.vtPlateBuyValue = (java.util.ArrayList<BEC.PlateBuyValue>)istream.readList(1, false, VAR_TYPE_4_VTPLATEBUYVALUE);
    }

}

