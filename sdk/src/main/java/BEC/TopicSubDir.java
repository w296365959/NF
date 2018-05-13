package BEC;

public final class TopicSubDir extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public java.util.ArrayList<BEC.NewsDesc> vNewsIdList = null;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public java.util.ArrayList<BEC.NewsDesc> getVNewsIdList()
    {
        return vNewsIdList;
    }

    public void  setVNewsIdList(java.util.ArrayList<BEC.NewsDesc> vNewsIdList)
    {
        this.vNewsIdList = vNewsIdList;
    }

    public TopicSubDir()
    {
    }

    public TopicSubDir(String sName, java.util.ArrayList<BEC.NewsDesc> vNewsIdList)
    {
        this.sName = sName;
        this.vNewsIdList = vNewsIdList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != vNewsIdList) {
            ostream.writeList(1, vNewsIdList);
        }
    }

    static java.util.ArrayList<BEC.NewsDesc> VAR_TYPE_4_VNEWSIDLIST = new java.util.ArrayList<BEC.NewsDesc>();
    static {
        VAR_TYPE_4_VNEWSIDLIST.add(new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.vNewsIdList = (java.util.ArrayList<BEC.NewsDesc>)istream.readList(1, false, VAR_TYPE_4_VNEWSIDLIST);
    }

}

