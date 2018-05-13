package BEC;

public final class ClearAlertMessageListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public long iAccountId = 0;

    public AccountTicket stAccountTicket = null;

    public int iClassID = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
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

    public int getIClassID()
    {
        return iClassID;
    }

    public void  setIClassID(int iClassID)
    {
        this.iClassID = iClassID;
    }

    public ClearAlertMessageListReq()
    {
    }

    public ClearAlertMessageListReq(UserInfo stUserInfo, long iAccountId, AccountTicket stAccountTicket, int iClassID)
    {
        this.stUserInfo = stUserInfo;
        this.iAccountId = iAccountId;
        this.stAccountTicket = stAccountTicket;
        this.iClassID = iClassID;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeUInt32(1, iAccountId);
        if (null != stAccountTicket) {
            ostream.writeMessage(2, stAccountTicket);
        }
        ostream.writeInt32(3, iClassID);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iAccountId = (long)istream.readUInt32(1, false, this.iAccountId);
        this.stAccountTicket = (AccountTicket)istream.readMessage(2, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iClassID = (int)istream.readInt32(3, false, this.iClassID);
    }

}

