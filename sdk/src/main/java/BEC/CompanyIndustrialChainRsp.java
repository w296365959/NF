package BEC;

public final class CompanyIndustrialChainRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.Product> vUpstreamProducts = null;

    public java.util.ArrayList<BEC.Product> vDownstreamProducts = null;

    public java.util.ArrayList<BEC.Product> vMainProducts = null;

    public java.util.ArrayList<BEC.Industry> vUpstreamIndustries = null;

    public java.util.ArrayList<BEC.Industry> vDownstreamIndustries = null;

    public java.util.ArrayList<BEC.Industry> vBelongedIndustries = null;

    public String sConsumerGroup = "";

    public String sSalesMarket = "";

    public java.util.ArrayList<BEC.Product> getVUpstreamProducts()
    {
        return vUpstreamProducts;
    }

    public void  setVUpstreamProducts(java.util.ArrayList<BEC.Product> vUpstreamProducts)
    {
        this.vUpstreamProducts = vUpstreamProducts;
    }

    public java.util.ArrayList<BEC.Product> getVDownstreamProducts()
    {
        return vDownstreamProducts;
    }

    public void  setVDownstreamProducts(java.util.ArrayList<BEC.Product> vDownstreamProducts)
    {
        this.vDownstreamProducts = vDownstreamProducts;
    }

    public java.util.ArrayList<BEC.Product> getVMainProducts()
    {
        return vMainProducts;
    }

    public void  setVMainProducts(java.util.ArrayList<BEC.Product> vMainProducts)
    {
        this.vMainProducts = vMainProducts;
    }

    public java.util.ArrayList<BEC.Industry> getVUpstreamIndustries()
    {
        return vUpstreamIndustries;
    }

    public void  setVUpstreamIndustries(java.util.ArrayList<BEC.Industry> vUpstreamIndustries)
    {
        this.vUpstreamIndustries = vUpstreamIndustries;
    }

    public java.util.ArrayList<BEC.Industry> getVDownstreamIndustries()
    {
        return vDownstreamIndustries;
    }

    public void  setVDownstreamIndustries(java.util.ArrayList<BEC.Industry> vDownstreamIndustries)
    {
        this.vDownstreamIndustries = vDownstreamIndustries;
    }

    public java.util.ArrayList<BEC.Industry> getVBelongedIndustries()
    {
        return vBelongedIndustries;
    }

    public void  setVBelongedIndustries(java.util.ArrayList<BEC.Industry> vBelongedIndustries)
    {
        this.vBelongedIndustries = vBelongedIndustries;
    }

    public String getSConsumerGroup()
    {
        return sConsumerGroup;
    }

    public void  setSConsumerGroup(String sConsumerGroup)
    {
        this.sConsumerGroup = sConsumerGroup;
    }

    public String getSSalesMarket()
    {
        return sSalesMarket;
    }

    public void  setSSalesMarket(String sSalesMarket)
    {
        this.sSalesMarket = sSalesMarket;
    }

    public CompanyIndustrialChainRsp()
    {
    }

    public CompanyIndustrialChainRsp(java.util.ArrayList<BEC.Product> vUpstreamProducts, java.util.ArrayList<BEC.Product> vDownstreamProducts, java.util.ArrayList<BEC.Product> vMainProducts, java.util.ArrayList<BEC.Industry> vUpstreamIndustries, java.util.ArrayList<BEC.Industry> vDownstreamIndustries, java.util.ArrayList<BEC.Industry> vBelongedIndustries, String sConsumerGroup, String sSalesMarket)
    {
        this.vUpstreamProducts = vUpstreamProducts;
        this.vDownstreamProducts = vDownstreamProducts;
        this.vMainProducts = vMainProducts;
        this.vUpstreamIndustries = vUpstreamIndustries;
        this.vDownstreamIndustries = vDownstreamIndustries;
        this.vBelongedIndustries = vBelongedIndustries;
        this.sConsumerGroup = sConsumerGroup;
        this.sSalesMarket = sSalesMarket;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vUpstreamProducts) {
            ostream.writeList(0, vUpstreamProducts);
        }
        if (null != vDownstreamProducts) {
            ostream.writeList(1, vDownstreamProducts);
        }
        if (null != vMainProducts) {
            ostream.writeList(2, vMainProducts);
        }
        if (null != vUpstreamIndustries) {
            ostream.writeList(3, vUpstreamIndustries);
        }
        if (null != vDownstreamIndustries) {
            ostream.writeList(4, vDownstreamIndustries);
        }
        if (null != vBelongedIndustries) {
            ostream.writeList(5, vBelongedIndustries);
        }
        if (null != sConsumerGroup) {
            ostream.writeString(6, sConsumerGroup);
        }
        if (null != sSalesMarket) {
            ostream.writeString(7, sSalesMarket);
        }
    }

    static java.util.ArrayList<BEC.Product> VAR_TYPE_4_VUPSTREAMPRODUCTS = new java.util.ArrayList<BEC.Product>();
    static {
        VAR_TYPE_4_VUPSTREAMPRODUCTS.add(new BEC.Product());
    }

    static java.util.ArrayList<BEC.Product> VAR_TYPE_4_VDOWNSTREAMPRODUCTS = new java.util.ArrayList<BEC.Product>();
    static {
        VAR_TYPE_4_VDOWNSTREAMPRODUCTS.add(new BEC.Product());
    }

    static java.util.ArrayList<BEC.Product> VAR_TYPE_4_VMAINPRODUCTS = new java.util.ArrayList<BEC.Product>();
    static {
        VAR_TYPE_4_VMAINPRODUCTS.add(new BEC.Product());
    }

    static java.util.ArrayList<BEC.Industry> VAR_TYPE_4_VUPSTREAMINDUSTRIES = new java.util.ArrayList<BEC.Industry>();
    static {
        VAR_TYPE_4_VUPSTREAMINDUSTRIES.add(new BEC.Industry());
    }

    static java.util.ArrayList<BEC.Industry> VAR_TYPE_4_VDOWNSTREAMINDUSTRIES = new java.util.ArrayList<BEC.Industry>();
    static {
        VAR_TYPE_4_VDOWNSTREAMINDUSTRIES.add(new BEC.Industry());
    }

    static java.util.ArrayList<BEC.Industry> VAR_TYPE_4_VBELONGEDINDUSTRIES = new java.util.ArrayList<BEC.Industry>();
    static {
        VAR_TYPE_4_VBELONGEDINDUSTRIES.add(new BEC.Industry());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vUpstreamProducts = (java.util.ArrayList<BEC.Product>)istream.readList(0, false, VAR_TYPE_4_VUPSTREAMPRODUCTS);
        this.vDownstreamProducts = (java.util.ArrayList<BEC.Product>)istream.readList(1, false, VAR_TYPE_4_VDOWNSTREAMPRODUCTS);
        this.vMainProducts = (java.util.ArrayList<BEC.Product>)istream.readList(2, false, VAR_TYPE_4_VMAINPRODUCTS);
        this.vUpstreamIndustries = (java.util.ArrayList<BEC.Industry>)istream.readList(3, false, VAR_TYPE_4_VUPSTREAMINDUSTRIES);
        this.vDownstreamIndustries = (java.util.ArrayList<BEC.Industry>)istream.readList(4, false, VAR_TYPE_4_VDOWNSTREAMINDUSTRIES);
        this.vBelongedIndustries = (java.util.ArrayList<BEC.Industry>)istream.readList(5, false, VAR_TYPE_4_VBELONGEDINDUSTRIES);
        this.sConsumerGroup = (String)istream.readString(6, false, this.sConsumerGroup);
        this.sSalesMarket = (String)istream.readString(7, false, this.sSalesMarket);
    }

}

