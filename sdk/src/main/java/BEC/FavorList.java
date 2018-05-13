package BEC;

public final class FavorList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FavorNews> vFavorNews = null;

    public int iVersion = 0;

    public java.util.ArrayList<BEC.FavorNews> getVFavorNews()
    {
        return vFavorNews;
    }

    public void  setVFavorNews(java.util.ArrayList<BEC.FavorNews> vFavorNews)
    {
        this.vFavorNews = vFavorNews;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public FavorList()
    {
    }

    public FavorList(java.util.ArrayList<BEC.FavorNews> vFavorNews, int iVersion)
    {
        this.vFavorNews = vFavorNews;
        this.iVersion = iVersion;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vFavorNews) {
            ostream.writeList(0, vFavorNews);
        }
        ostream.writeInt32(1, iVersion);
    }

    static java.util.ArrayList<BEC.FavorNews> VAR_TYPE_4_VFAVORNEWS = new java.util.ArrayList<BEC.FavorNews>();
    static {
        VAR_TYPE_4_VFAVORNEWS.add(new BEC.FavorNews());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vFavorNews = (java.util.ArrayList<BEC.FavorNews>)istream.readList(0, false, VAR_TYPE_4_VFAVORNEWS);
        this.iVersion = (int)istream.readInt32(1, false, this.iVersion);
    }

}

