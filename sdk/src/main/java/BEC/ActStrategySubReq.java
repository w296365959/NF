package BEC;

public final class ActStrategySubReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public int iActType = 0;

    public String sStrategyId = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public int getIActType()
    {
        return iActType;
    }

    public void  setIActType(int iActType)
    {
        this.iActType = iActType;
    }

    public String getSStrategyId()
    {
        return sStrategyId;
    }

    public void  setSStrategyId(String sStrategyId)
    {
        this.sStrategyId = sStrategyId;
    }

    public ActStrategySubReq()
    {
    }

    public ActStrategySubReq(UserInfo stUserInfo, AccountTicket stAccountTicket, int iActType, String sStrategyId)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iActType = iActType;
        this.sStrategyId = sStrategyId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stAccountTicket) {
            ostream.writeMessage(1, stAccountTicket);
        }
        ostream.writeInt32(2, iActType);
        if (null != sStrategyId) {
            ostream.writeString(3, sStrategyId);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iActType = (int)istream.readInt32(2, false, this.iActType);
        this.sStrategyId = (String)istream.readString(3, false, this.sStrategyId);
    }

}

