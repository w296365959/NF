package BEC;

public final class NewsRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.NewsList stNewsList = null;

    public int iStatus = 0;

    public String sNextNewsID = "";

    public BEC.NewsList getStNewsList()
    {
        return stNewsList;
    }

    public void  setStNewsList(BEC.NewsList stNewsList)
    {
        this.stNewsList = stNewsList;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public String getSNextNewsID()
    {
        return sNextNewsID;
    }

    public void  setSNextNewsID(String sNextNewsID)
    {
        this.sNextNewsID = sNextNewsID;
    }

    public NewsRsp()
    {
    }

    public NewsRsp(BEC.NewsList stNewsList, int iStatus, String sNextNewsID)
    {
        this.stNewsList = stNewsList;
        this.iStatus = iStatus;
        this.sNextNewsID = sNextNewsID;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stNewsList) {
            ostream.writeMessage(0, stNewsList);
        }
        ostream.writeInt32(1, iStatus);
        if (null != sNextNewsID) {
            ostream.writeString(2, sNextNewsID);
        }
    }

    static BEC.NewsList VAR_TYPE_4_STNEWSLIST = new BEC.NewsList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stNewsList = (BEC.NewsList)istream.readMessage(0, false, VAR_TYPE_4_STNEWSLIST);
        this.iStatus = (int)istream.readInt32(1, false, this.iStatus);
        this.sNextNewsID = (String)istream.readString(2, false, this.sNextNewsID);
    }

}

