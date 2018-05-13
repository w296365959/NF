package BEC;

public final class NewsListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.NewsSimple> vNewsSimple = null;

    public java.util.ArrayList<BEC.NewsSimple> getVNewsSimple()
    {
        return vNewsSimple;
    }

    public void  setVNewsSimple(java.util.ArrayList<BEC.NewsSimple> vNewsSimple)
    {
        this.vNewsSimple = vNewsSimple;
    }

    public NewsListReq()
    {
    }

    public NewsListReq(java.util.ArrayList<BEC.NewsSimple> vNewsSimple)
    {
        this.vNewsSimple = vNewsSimple;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vNewsSimple) {
            ostream.writeList(0, vNewsSimple);
        }
    }

    static java.util.ArrayList<BEC.NewsSimple> VAR_TYPE_4_VNEWSSIMPLE = new java.util.ArrayList<BEC.NewsSimple>();
    static {
        VAR_TYPE_4_VNEWSSIMPLE.add(new BEC.NewsSimple());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vNewsSimple = (java.util.ArrayList<BEC.NewsSimple>)istream.readList(0, false, VAR_TYPE_4_VNEWSSIMPLE);
    }

}

