package BEC;

public final class GetSaleDepartByCoordsRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SaleDepartItem> vSaleDepart = null;

    public java.util.ArrayList<BEC.SaleDepartItem> getVSaleDepart()
    {
        return vSaleDepart;
    }

    public void  setVSaleDepart(java.util.ArrayList<BEC.SaleDepartItem> vSaleDepart)
    {
        this.vSaleDepart = vSaleDepart;
    }

    public GetSaleDepartByCoordsRsp()
    {
    }

    public GetSaleDepartByCoordsRsp(java.util.ArrayList<BEC.SaleDepartItem> vSaleDepart)
    {
        this.vSaleDepart = vSaleDepart;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSaleDepart) {
            ostream.writeList(0, vSaleDepart);
        }
    }

    static java.util.ArrayList<BEC.SaleDepartItem> VAR_TYPE_4_VSALEDEPART = new java.util.ArrayList<BEC.SaleDepartItem>();
    static {
        VAR_TYPE_4_VSALEDEPART.add(new BEC.SaleDepartItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSaleDepart = (java.util.ArrayList<BEC.SaleDepartItem>)istream.readList(0, false, VAR_TYPE_4_VSALEDEPART);
    }

}

