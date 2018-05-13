package BEC;

public final class IntelliPickStockRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<IntelliPickStock> vtIntelliPickStock = null;

    public java.util.ArrayList<IntelliPickStock> getVtIntelliPickStock()
    {
        return vtIntelliPickStock;
    }

    public void  setVtIntelliPickStock(java.util.ArrayList<IntelliPickStock> vtIntelliPickStock)
    {
        this.vtIntelliPickStock = vtIntelliPickStock;
    }

    public IntelliPickStockRsp()
    {
    }

    public IntelliPickStockRsp(java.util.ArrayList<IntelliPickStock> vtIntelliPickStock)
    {
        this.vtIntelliPickStock = vtIntelliPickStock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtIntelliPickStock) {
            ostream.writeList(0, vtIntelliPickStock);
        }
    }

    static java.util.ArrayList<IntelliPickStock> VAR_TYPE_4_VTINTELLIPICKSTOCK = new java.util.ArrayList<IntelliPickStock>();
    static {
        VAR_TYPE_4_VTINTELLIPICKSTOCK.add(new IntelliPickStock());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtIntelliPickStock = (java.util.ArrayList<IntelliPickStock>)istream.readList(0, false, VAR_TYPE_4_VTINTELLIPICKSTOCK);
    }

}

