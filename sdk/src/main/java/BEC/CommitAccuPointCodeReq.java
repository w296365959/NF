package BEC;

public final class CommitAccuPointCodeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public String sCode = "";

    public int iCodeType = 0;

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

    public String getSCode()
    {
        return sCode;
    }

    public void  setSCode(String sCode)
    {
        this.sCode = sCode;
    }

    public int getICodeType()
    {
        return iCodeType;
    }

    public void  setICodeType(int iCodeType)
    {
        this.iCodeType = iCodeType;
    }

    public CommitAccuPointCodeReq()
    {
    }

    public CommitAccuPointCodeReq(UserInfo stUserInfo, AccountTicket stAccountTicket, String sCode, int iCodeType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.sCode = sCode;
        this.iCodeType = iCodeType;
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
        if (null != sCode) {
            ostream.writeString(2, sCode);
        }
        ostream.writeInt32(3, iCodeType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sCode = (String)istream.readString(2, false, this.sCode);
        this.iCodeType = (int)istream.readInt32(3, false, this.iCodeType);
    }

}

