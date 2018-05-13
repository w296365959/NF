package BEC;

public final class ReportAckLoginReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public String sTicket = "";

    public int iTargetType = 0;

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

    public String getSTicket()
    {
        return sTicket;
    }

    public void  setSTicket(String sTicket)
    {
        this.sTicket = sTicket;
    }

    public int getITargetType()
    {
        return iTargetType;
    }

    public void  setITargetType(int iTargetType)
    {
        this.iTargetType = iTargetType;
    }

    public ReportAckLoginReq()
    {
    }

    public ReportAckLoginReq(UserInfo stUserInfo, AccountTicket stAccountTicket, String sTicket, int iTargetType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.sTicket = sTicket;
        this.iTargetType = iTargetType;
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
        if (null != sTicket) {
            ostream.writeString(2, sTicket);
        }
        ostream.writeInt32(3, iTargetType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sTicket = (String)istream.readString(2, false, this.sTicket);
        this.iTargetType = (int)istream.readInt32(3, false, this.iTargetType);
    }

}

