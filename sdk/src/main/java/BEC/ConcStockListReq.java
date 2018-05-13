package BEC;

public final class ConcStockListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.ConcInfo stConcInfo = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.ConcInfo getStConcInfo()
    {
        return stConcInfo;
    }

    public void  setStConcInfo(BEC.ConcInfo stConcInfo)
    {
        this.stConcInfo = stConcInfo;
    }

    public ConcStockListReq()
    {
    }

    public ConcStockListReq(BEC.UserInfo stUserInfo, BEC.ConcInfo stConcInfo)
    {
        this.stUserInfo = stUserInfo;
        this.stConcInfo = stConcInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stConcInfo) {
            ostream.writeMessage(1, stConcInfo);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.ConcInfo VAR_TYPE_4_STCONCINFO = new BEC.ConcInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stConcInfo = (BEC.ConcInfo)istream.readMessage(1, false, VAR_TYPE_4_STCONCINFO);
    }

}

