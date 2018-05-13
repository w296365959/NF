package BEC;

public final class SecNewsContentRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.NewsDesc> vtNewsDesc = null;

    public java.util.ArrayList<BEC.NewsDesc> getVtNewsDesc()
    {
        return vtNewsDesc;
    }

    public void  setVtNewsDesc(java.util.ArrayList<BEC.NewsDesc> vtNewsDesc)
    {
        this.vtNewsDesc = vtNewsDesc;
    }

    public SecNewsContentRsp()
    {
    }

    public SecNewsContentRsp(java.util.ArrayList<BEC.NewsDesc> vtNewsDesc)
    {
        this.vtNewsDesc = vtNewsDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtNewsDesc) {
            ostream.writeList(0, vtNewsDesc);
        }
    }

    static java.util.ArrayList<BEC.NewsDesc> VAR_TYPE_4_VTNEWSDESC = new java.util.ArrayList<BEC.NewsDesc>();
    static {
        VAR_TYPE_4_VTNEWSDESC.add(new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtNewsDesc = (java.util.ArrayList<BEC.NewsDesc>)istream.readList(0, false, VAR_TYPE_4_VTNEWSDESC);
    }

}

