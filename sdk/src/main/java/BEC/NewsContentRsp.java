package BEC;

public final class NewsContentRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.NewsDesc stNewsDesc = null;

    public BEC.NewsDesc getStNewsDesc()
    {
        return stNewsDesc;
    }

    public void  setStNewsDesc(BEC.NewsDesc stNewsDesc)
    {
        this.stNewsDesc = stNewsDesc;
    }

    public NewsContentRsp()
    {
    }

    public NewsContentRsp(BEC.NewsDesc stNewsDesc)
    {
        this.stNewsDesc = stNewsDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeMessage(0, stNewsDesc);
    }

    static BEC.NewsDesc VAR_TYPE_4_STNEWSDESC = new BEC.NewsDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stNewsDesc = (BEC.NewsDesc)istream.readMessage(0, true, VAR_TYPE_4_STNEWSDESC);
    }

}

