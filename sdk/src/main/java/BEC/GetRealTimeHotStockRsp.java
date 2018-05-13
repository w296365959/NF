package BEC;

public final class GetRealTimeHotStockRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<RealTimeStockItem> vItem = null;

    public java.util.ArrayList<RealTimeStockItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<RealTimeStockItem> vItem)
    {
        this.vItem = vItem;
    }

    public GetRealTimeHotStockRsp()
    {
    }

    public GetRealTimeHotStockRsp(java.util.ArrayList<RealTimeStockItem> vItem)
    {
        this.vItem = vItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vItem) {
            ostream.writeList(0, vItem);
        }
    }

    static java.util.ArrayList<RealTimeStockItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<RealTimeStockItem>();
    static {
        VAR_TYPE_4_VITEM.add(new RealTimeStockItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vItem = (java.util.ArrayList<RealTimeStockItem>)istream.readList(0, false, VAR_TYPE_4_VITEM);
    }

}

