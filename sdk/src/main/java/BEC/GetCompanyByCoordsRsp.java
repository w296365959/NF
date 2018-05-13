package BEC;

public final class GetCompanyByCoordsRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.RegionItem> vRegion = null;

    public java.util.ArrayList<BEC.RegionItem> getVRegion()
    {
        return vRegion;
    }

    public void  setVRegion(java.util.ArrayList<BEC.RegionItem> vRegion)
    {
        this.vRegion = vRegion;
    }

    public GetCompanyByCoordsRsp()
    {
    }

    public GetCompanyByCoordsRsp(java.util.ArrayList<BEC.RegionItem> vRegion)
    {
        this.vRegion = vRegion;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vRegion) {
            ostream.writeList(0, vRegion);
        }
    }

    static java.util.ArrayList<BEC.RegionItem> VAR_TYPE_4_VREGION = new java.util.ArrayList<BEC.RegionItem>();
    static {
        VAR_TYPE_4_VREGION.add(new BEC.RegionItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vRegion = (java.util.ArrayList<BEC.RegionItem>)istream.readList(0, false, VAR_TYPE_4_VREGION);
    }

}

