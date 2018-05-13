package BEC;

public final class LongHuRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STLongHu> vtLongHu = null;

    public java.util.ArrayList<BEC.STLongHu> getVtLongHu()
    {
        return vtLongHu;
    }

    public void  setVtLongHu(java.util.ArrayList<BEC.STLongHu> vtLongHu)
    {
        this.vtLongHu = vtLongHu;
    }

    public LongHuRsp()
    {
    }

    public LongHuRsp(java.util.ArrayList<BEC.STLongHu> vtLongHu)
    {
        this.vtLongHu = vtLongHu;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtLongHu) {
            ostream.writeList(0, vtLongHu);
        }
    }

    static java.util.ArrayList<BEC.STLongHu> VAR_TYPE_4_VTLONGHU = new java.util.ArrayList<BEC.STLongHu>();
    static {
        VAR_TYPE_4_VTLONGHU.add(new BEC.STLongHu());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtLongHu = (java.util.ArrayList<BEC.STLongHu>)istream.readList(0, false, VAR_TYPE_4_VTLONGHU);
    }

}

