package BEC;

public final class HotStockListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<HotStockDesc> vtHotStock = null;

    public String sHotDesc = "";

    public java.util.ArrayList<HotStockDesc> getVtHotStock()
    {
        return vtHotStock;
    }

    public void  setVtHotStock(java.util.ArrayList<HotStockDesc> vtHotStock)
    {
        this.vtHotStock = vtHotStock;
    }

    public String getSHotDesc()
    {
        return sHotDesc;
    }

    public void  setSHotDesc(String sHotDesc)
    {
        this.sHotDesc = sHotDesc;
    }

    public HotStockListRsp()
    {
    }

    public HotStockListRsp(java.util.ArrayList<HotStockDesc> vtHotStock, String sHotDesc)
    {
        this.vtHotStock = vtHotStock;
        this.sHotDesc = sHotDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtHotStock) {
            ostream.writeList(0, vtHotStock);
        }
        if (null != sHotDesc) {
            ostream.writeString(1, sHotDesc);
        }
    }

    static java.util.ArrayList<HotStockDesc> VAR_TYPE_4_VTHOTSTOCK = new java.util.ArrayList<HotStockDesc>();
    static {
        VAR_TYPE_4_VTHOTSTOCK.add(new HotStockDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtHotStock = (java.util.ArrayList<HotStockDesc>)istream.readList(0, false, VAR_TYPE_4_VTHOTSTOCK);
        this.sHotDesc = (String)istream.readString(1, false, this.sHotDesc);
    }

}

