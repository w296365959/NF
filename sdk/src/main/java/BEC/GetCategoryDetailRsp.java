package BEC;

public final class GetCategoryDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<IntelliPickStockEx> vtDaySec = null;

    public java.util.ArrayList<CategoryHotStockDesc> vHotStock = null;

    public java.util.ArrayList<IntelliPickStockEx> getVtDaySec()
    {
        return vtDaySec;
    }

    public void  setVtDaySec(java.util.ArrayList<IntelliPickStockEx> vtDaySec)
    {
        this.vtDaySec = vtDaySec;
    }

    public java.util.ArrayList<CategoryHotStockDesc> getVHotStock()
    {
        return vHotStock;
    }

    public void  setVHotStock(java.util.ArrayList<CategoryHotStockDesc> vHotStock)
    {
        this.vHotStock = vHotStock;
    }

    public GetCategoryDetailRsp()
    {
    }

    public GetCategoryDetailRsp(java.util.ArrayList<IntelliPickStockEx> vtDaySec, java.util.ArrayList<CategoryHotStockDesc> vHotStock)
    {
        this.vtDaySec = vtDaySec;
        this.vHotStock = vHotStock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtDaySec) {
            ostream.writeList(0, vtDaySec);
        }
        if (null != vHotStock) {
            ostream.writeList(1, vHotStock);
        }
    }

    static java.util.ArrayList<IntelliPickStockEx> VAR_TYPE_4_VTDAYSEC = new java.util.ArrayList<IntelliPickStockEx>();
    static {
        VAR_TYPE_4_VTDAYSEC.add(new IntelliPickStockEx());
    }

    static java.util.ArrayList<CategoryHotStockDesc> VAR_TYPE_4_VHOTSTOCK = new java.util.ArrayList<CategoryHotStockDesc>();
    static {
        VAR_TYPE_4_VHOTSTOCK.add(new CategoryHotStockDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtDaySec = (java.util.ArrayList<IntelliPickStockEx>)istream.readList(0, false, VAR_TYPE_4_VTDAYSEC);
        this.vHotStock = (java.util.ArrayList<CategoryHotStockDesc>)istream.readList(1, false, VAR_TYPE_4_VHOTSTOCK);
    }

}

