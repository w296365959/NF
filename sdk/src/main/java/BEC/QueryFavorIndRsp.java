package BEC;

public final class QueryFavorIndRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sIndiData = "";

    public String getSIndiData()
    {
        return sIndiData;
    }

    public void  setSIndiData(String sIndiData)
    {
        this.sIndiData = sIndiData;
    }

    public QueryFavorIndRsp()
    {
    }

    public QueryFavorIndRsp(String sIndiData)
    {
        this.sIndiData = sIndiData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeString(0, sIndiData);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sIndiData = (String)istream.readString(0, true, this.sIndiData);
    }

}

