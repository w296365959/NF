package BEC;

public final class NewsPageRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.NewsList stNewsList = null;

    public int iTotalPageNum = 0;

    public int iTotalDataNum = 0;

    public BEC.NewsList getStNewsList()
    {
        return stNewsList;
    }

    public void  setStNewsList(BEC.NewsList stNewsList)
    {
        this.stNewsList = stNewsList;
    }

    public int getITotalPageNum()
    {
        return iTotalPageNum;
    }

    public void  setITotalPageNum(int iTotalPageNum)
    {
        this.iTotalPageNum = iTotalPageNum;
    }

    public int getITotalDataNum()
    {
        return iTotalDataNum;
    }

    public void  setITotalDataNum(int iTotalDataNum)
    {
        this.iTotalDataNum = iTotalDataNum;
    }

    public NewsPageRsp()
    {
    }

    public NewsPageRsp(BEC.NewsList stNewsList, int iTotalPageNum, int iTotalDataNum)
    {
        this.stNewsList = stNewsList;
        this.iTotalPageNum = iTotalPageNum;
        this.iTotalDataNum = iTotalDataNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stNewsList) {
            ostream.writeMessage(0, stNewsList);
        }
        ostream.writeInt32(1, iTotalPageNum);
        ostream.writeInt32(2, iTotalDataNum);
    }

    static BEC.NewsList VAR_TYPE_4_STNEWSLIST = new BEC.NewsList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stNewsList = (BEC.NewsList)istream.readMessage(0, false, VAR_TYPE_4_STNEWSLIST);
        this.iTotalPageNum = (int)istream.readInt32(1, false, this.iTotalPageNum);
        this.iTotalDataNum = (int)istream.readInt32(2, false, this.iTotalDataNum);
    }

}

