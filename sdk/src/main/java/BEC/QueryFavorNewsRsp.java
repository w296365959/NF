package BEC;

public final class QueryFavorNewsRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FavorList stFavorList = null;

    public BEC.FavorList getStFavorList()
    {
        return stFavorList;
    }

    public void  setStFavorList(BEC.FavorList stFavorList)
    {
        this.stFavorList = stFavorList;
    }

    public QueryFavorNewsRsp()
    {
    }

    public QueryFavorNewsRsp(BEC.FavorList stFavorList)
    {
        this.stFavorList = stFavorList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFavorList) {
            ostream.writeMessage(0, stFavorList);
        }
    }

    static BEC.FavorList VAR_TYPE_4_STFAVORLIST = new BEC.FavorList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFavorList = (BEC.FavorList)istream.readMessage(0, false, VAR_TYPE_4_STFAVORLIST);
    }

}

