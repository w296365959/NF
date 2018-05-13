package BEC;

public final class SavePortfolioReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public ProSecInfoList stProSecInfoList = null;

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

    public AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public SavePortfolioReq()
    {
    }

    public SavePortfolioReq(UserInfo stUserInfo, ProSecInfoList stProSecInfoList, long iAccountId, AccountTicket stAccountTicket)
    {
        this.stUserInfo = stUserInfo;
        this.stProSecInfoList = stProSecInfoList;
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
        if (null != stProSecInfoList) {
            ostream.writeMessage(1, stProSecInfoList);
        }
        ostream.writeUInt32(2, iAccountId);
        if (null != stAccountTicket) {
            ostream.writeMessage(3, stAccountTicket);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static ProSecInfoList VAR_TYPE_4_STPROSECINFOLIST = new ProSecInfoList();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stProSecInfoList = (ProSecInfoList)istream.readMessage(1, false, VAR_TYPE_4_STPROSECINFOLIST);
        this.iAccountId = (long)istream.readUInt32(2, false, this.iAccountId);
        this.stAccountTicket = (AccountTicket)istream.readMessage(3, false, VAR_TYPE_4_STACCOUNTTICKET);
    }

}

