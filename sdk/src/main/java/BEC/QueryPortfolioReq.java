package BEC;

public final class QueryPortfolioReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int iVersion = 0;

    public long iAccountId = 0;

    public AccountTicket stAccountTicket = null;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public QueryPortfolioReq()
    {
    }

    public QueryPortfolioReq(UserInfo stUserInfo, int iVersion, long iAccountId, AccountTicket stAccountTicket)
    {
        this.stUserInfo = stUserInfo;
        this.iVersion = iVersion;
        this.iAccountId = iAccountId;
        this.stAccountTicket = stAccountTicket;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iVersion);
        ostream.writeUInt32(3, iAccountId);
        if (null != stAccountTicket) {
            ostream.writeMessage(4, stAccountTicket);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iVersion = (int)istream.readInt32(1, false, this.iVersion);
        this.iAccountId = (long)istream.readUInt32(3, false, this.iAccountId);
        this.stAccountTicket = (AccountTicket)istream.readMessage(4, false, VAR_TYPE_4_STACCOUNTTICKET);
    }

}

