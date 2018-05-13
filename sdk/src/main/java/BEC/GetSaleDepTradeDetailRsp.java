package BEC;

public final class GetSaleDepTradeDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SaleDepSecDetail> vDetail = null;

    public java.util.ArrayList<BEC.SaleDepSecDetail> getVDetail()
    {
        return vDetail;
    }

    public void  setVDetail(java.util.ArrayList<BEC.SaleDepSecDetail> vDetail)
    {
        this.vDetail = vDetail;
    }

    public GetSaleDepTradeDetailRsp()
    {
    }

    public GetSaleDepTradeDetailRsp(java.util.ArrayList<BEC.SaleDepSecDetail> vDetail)
    {
        this.vDetail = vDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vDetail) {
            ostream.writeList(0, vDetail);
        }
    }

    static java.util.ArrayList<BEC.SaleDepSecDetail> VAR_TYPE_4_VDETAIL = new java.util.ArrayList<BEC.SaleDepSecDetail>();
    static {
        VAR_TYPE_4_VDETAIL.add(new BEC.SaleDepSecDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vDetail = (java.util.ArrayList<BEC.SaleDepSecDetail>)istream.readList(0, false, VAR_TYPE_4_VDETAIL);
    }

}

