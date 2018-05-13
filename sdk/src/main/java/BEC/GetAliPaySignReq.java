package BEC;

public final class GetAliPaySignReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public String sDtPayOrderId = "";

    public String sPassback = "";

    public String sAppId = "";

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

    public String getSDtPayOrderId()
    {
        return sDtPayOrderId;
    }

    public void  setSDtPayOrderId(String sDtPayOrderId)
    {
        this.sDtPayOrderId = sDtPayOrderId;
    }

    public String getSPassback()
    {
        return sPassback;
    }

    public void  setSPassback(String sPassback)
    {
        this.sPassback = sPassback;
    }

    public String getSAppId()
    {
        return sAppId;
    }

    public void  setSAppId(String sAppId)
    {
        this.sAppId = sAppId;
    }

    public GetAliPaySignReq()
    {
    }

    public GetAliPaySignReq(UserInfo stUserInfo, AccountTicket stAccountTicket, String sDtPayOrderId, String sPassback, String sAppId)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.sDtPayOrderId = sDtPayOrderId;
        this.sPassback = sPassback;
        this.sAppId = sAppId;
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
        ostream.writeString(2, sDtPayOrderId);
        if (null != sPassback) {
            ostream.writeString(3, sPassback);
        }
        ostream.writeString(4, sAppId);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sDtPayOrderId = (String)istream.readString(2, true, this.sDtPayOrderId);
        this.sPassback = (String)istream.readString(3, false, this.sPassback);
        this.sAppId = (String)istream.readString(4, true, this.sAppId);
    }

}

