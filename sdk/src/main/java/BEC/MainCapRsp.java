package BEC;

public final class MainCapRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STMainCap> vtMainCap = null;

    public java.util.ArrayList<BEC.STMainCap> getVtMainCap()
    {
        return vtMainCap;
    }

    public void  setVtMainCap(java.util.ArrayList<BEC.STMainCap> vtMainCap)
    {
        this.vtMainCap = vtMainCap;
    }

    public MainCapRsp()
    {
    }

    public MainCapRsp(java.util.ArrayList<BEC.STMainCap> vtMainCap)
    {
        this.vtMainCap = vtMainCap;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtMainCap) {
            ostream.writeList(0, vtMainCap);
        }
    }

    static java.util.ArrayList<BEC.STMainCap> VAR_TYPE_4_VTMAINCAP = new java.util.ArrayList<BEC.STMainCap>();
    static {
        VAR_TYPE_4_VTMAINCAP.add(new BEC.STMainCap());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtMainCap = (java.util.ArrayList<BEC.STMainCap>)istream.readList(0, false, VAR_TYPE_4_VTMAINCAP);
    }

}

