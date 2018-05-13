package BEC;

public final class PopularityRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STPopularity> vtPopularity = null;

    public java.util.ArrayList<BEC.STPopularity> getVtPopularity()
    {
        return vtPopularity;
    }

    public void  setVtPopularity(java.util.ArrayList<BEC.STPopularity> vtPopularity)
    {
        this.vtPopularity = vtPopularity;
    }

    public PopularityRsp()
    {
    }

    public PopularityRsp(java.util.ArrayList<BEC.STPopularity> vtPopularity)
    {
        this.vtPopularity = vtPopularity;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtPopularity) {
            ostream.writeList(0, vtPopularity);
        }
    }

    static java.util.ArrayList<BEC.STPopularity> VAR_TYPE_4_VTPOPULARITY = new java.util.ArrayList<BEC.STPopularity>();
    static {
        VAR_TYPE_4_VTPOPULARITY.add(new BEC.STPopularity());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtPopularity = (java.util.ArrayList<BEC.STPopularity>)istream.readList(0, false, VAR_TYPE_4_VTPOPULARITY);
    }

}

