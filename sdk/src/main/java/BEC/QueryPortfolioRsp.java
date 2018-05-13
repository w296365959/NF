package BEC;

public final class QueryPortfolioRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public ProSecInfoList stProSecInfoList = null;

    public long iAccountId = 0;

    public ProSecInfoList getStProSecInfoList()
    {
        return stProSecInfoList;
    }

    public void  setStProSecInfoList(ProSecInfoList stProSecInfoList)
    {
        this.stProSecInfoList = stProSecInfoList;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public QueryPortfolioRsp()
    {
    }

    public QueryPortfolioRsp(ProSecInfoList stProSecInfoList, long iAccountId)
    {
        this.stProSecInfoList = stProSecInfoList;
        this.iAccountId = iAccountId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stProSecInfoList) {
            ostream.writeMessage(0, stProSecInfoList);
        }
        ostream.writeUInt32(1, iAccountId);
    }

    static ProSecInfoList VAR_TYPE_4_STPROSECINFOLIST = new ProSecInfoList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stProSecInfoList = (ProSecInfoList)istream.readMessage(0, false, VAR_TYPE_4_STPROSECINFOLIST);
        this.iAccountId = (long)istream.readUInt32(1, false, this.iAccountId);
    }

}

