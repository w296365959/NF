package BEC;

public final class NewsListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, BEC.NewsDesc> mNewsDesc = null;

    public java.util.Map<String, BEC.NewsDesc> getMNewsDesc()
    {
        return mNewsDesc;
    }

    public void  setMNewsDesc(java.util.Map<String, BEC.NewsDesc> mNewsDesc)
    {
        this.mNewsDesc = mNewsDesc;
    }

    public NewsListRsp()
    {
    }

    public NewsListRsp(java.util.Map<String, BEC.NewsDesc> mNewsDesc)
    {
        this.mNewsDesc = mNewsDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mNewsDesc) {
            ostream.writeMap(0, mNewsDesc);
        }
    }

    static java.util.Map<String, BEC.NewsDesc> VAR_TYPE_4_MNEWSDESC = new java.util.HashMap<String, BEC.NewsDesc>();
    static {
        VAR_TYPE_4_MNEWSDESC.put("", new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mNewsDesc = (java.util.Map<String, BEC.NewsDesc>)istream.readMap(0, false, VAR_TYPE_4_MNEWSDESC);
    }

}

