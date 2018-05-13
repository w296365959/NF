package BEC;

public final class FavorAction extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FavorNews stFavorNews = null;

    public int eFavorAction = 0;

    public BEC.FavorNews getStFavorNews()
    {
        return stFavorNews;
    }

    public void  setStFavorNews(BEC.FavorNews stFavorNews)
    {
        this.stFavorNews = stFavorNews;
    }

    public int getEFavorAction()
    {
        return eFavorAction;
    }

    public void  setEFavorAction(int eFavorAction)
    {
        this.eFavorAction = eFavorAction;
    }

    public FavorAction()
    {
    }

    public FavorAction(BEC.FavorNews stFavorNews, int eFavorAction)
    {
        this.stFavorNews = stFavorNews;
        this.eFavorAction = eFavorAction;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFavorNews) {
            ostream.writeMessage(0, stFavorNews);
        }
        ostream.writeInt32(1, eFavorAction);
    }

    static BEC.FavorNews VAR_TYPE_4_STFAVORNEWS = new BEC.FavorNews();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFavorNews = (BEC.FavorNews)istream.readMessage(0, false, VAR_TYPE_4_STFAVORNEWS);
        this.eFavorAction = (int)istream.readInt32(1, false, this.eFavorAction);
    }

}

