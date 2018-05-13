package BEC;

public final class InformationSpiderNewsListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<InformationSpiderNews> vtInformationSpiderNews = null;

    public int iTotalCount = 0;

    public java.util.ArrayList<InformationSpiderNews> getVtInformationSpiderNews()
    {
        return vtInformationSpiderNews;
    }

    public void  setVtInformationSpiderNews(java.util.ArrayList<InformationSpiderNews> vtInformationSpiderNews)
    {
        this.vtInformationSpiderNews = vtInformationSpiderNews;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public InformationSpiderNewsListRsp()
    {
    }

    public InformationSpiderNewsListRsp(java.util.ArrayList<InformationSpiderNews> vtInformationSpiderNews, int iTotalCount)
    {
        this.vtInformationSpiderNews = vtInformationSpiderNews;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtInformationSpiderNews) {
            ostream.writeList(0, vtInformationSpiderNews);
        }
        ostream.writeInt32(1, iTotalCount);
    }

    static java.util.ArrayList<InformationSpiderNews> VAR_TYPE_4_VTINFORMATIONSPIDERNEWS = new java.util.ArrayList<InformationSpiderNews>();
    static {
        VAR_TYPE_4_VTINFORMATIONSPIDERNEWS.add(new InformationSpiderNews());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtInformationSpiderNews = (java.util.ArrayList<InformationSpiderNews>)istream.readList(0, false, VAR_TYPE_4_VTINFORMATIONSPIDERNEWS);
        this.iTotalCount = (int)istream.readInt32(1, false, this.iTotalCount);
    }

}

