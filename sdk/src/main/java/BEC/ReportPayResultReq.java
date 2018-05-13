package BEC;

public final class ReportPayResultReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public java.util.Map<String, String> mPayResult = null;

    public int iPayType = 0;

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

    public java.util.Map<String, String> getMPayResult()
    {
        return mPayResult;
    }

    public void  setMPayResult(java.util.Map<String, String> mPayResult)
    {
        this.mPayResult = mPayResult;
    }

    public int getIPayType()
    {
        return iPayType;
    }

    public void  setIPayType(int iPayType)
    {
        this.iPayType = iPayType;
    }

    public ReportPayResultReq()
    {
    }

    public ReportPayResultReq(UserInfo stUserInfo, AccountTicket stAccountTicket, java.util.Map<String, String> mPayResult, int iPayType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.mPayResult = mPayResult;
        this.iPayType = iPayType;
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
        if (null != mPayResult) {
            ostream.writeMap(2, mPayResult);
        }
        ostream.writeInt32(3, iPayType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();

    static java.util.Map<String, String> VAR_TYPE_4_MPAYRESULT = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_MPAYRESULT.put("", "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.mPayResult = (java.util.Map<String, String>)istream.readMap(2, false, VAR_TYPE_4_MPAYRESULT);
        this.iPayType = (int)istream.readInt32(3, false, this.iPayType);
    }

}

