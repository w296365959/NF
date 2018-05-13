package BEC;

public final class AllTgAttitudeRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.NewsDesc> vNewsDesc = null;

    public java.util.ArrayList<BEC.NewsDesc> getVNewsDesc()
    {
        return vNewsDesc;
    }

    public void  setVNewsDesc(java.util.ArrayList<BEC.NewsDesc> vNewsDesc)
    {
        this.vNewsDesc = vNewsDesc;
    }

    public AllTgAttitudeRsp()
    {
    }

    public AllTgAttitudeRsp(java.util.ArrayList<BEC.NewsDesc> vNewsDesc)
    {
        this.vNewsDesc = vNewsDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vNewsDesc) {
            ostream.writeList(0, vNewsDesc);
        }
    }

    static java.util.ArrayList<BEC.NewsDesc> VAR_TYPE_4_VNEWSDESC = new java.util.ArrayList<BEC.NewsDesc>();
    static {
        VAR_TYPE_4_VNEWSDESC.add(new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vNewsDesc = (java.util.ArrayList<BEC.NewsDesc>)istream.readList(0, false, VAR_TYPE_4_VNEWSDESC);
    }

}

