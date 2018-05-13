package BEC;

public final class ReportAliPayResultReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public java.util.Map<String, String> mPayResult = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
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

    public ReportAliPayResultReq()
    {
    }

    public ReportAliPayResultReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, java.util.Map<String, String> mPayResult)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.mPayResult = mPayResult;
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
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static java.util.Map<String, String> VAR_TYPE_4_MPAYRESULT = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_MPAYRESULT.put("", "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.mPayResult = (java.util.Map<String, String>)istream.readMap(2, false, VAR_TYPE_4_MPAYRESULT);
    }

}

