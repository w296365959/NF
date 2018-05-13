package BEC;

public final class GetH5PayUrlReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public String sDtPayOrderId = "";

    public int iThirdPaySource = 1;

    public int iPayType = 0;

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

    public String getSDtPayOrderId()
    {
        return sDtPayOrderId;
    }

    public void  setSDtPayOrderId(String sDtPayOrderId)
    {
        this.sDtPayOrderId = sDtPayOrderId;
    }

    public int getIThirdPaySource()
    {
        return iThirdPaySource;
    }

    public void  setIThirdPaySource(int iThirdPaySource)
    {
        this.iThirdPaySource = iThirdPaySource;
    }

    public int getIPayType()
    {
        return iPayType;
    }

    public void  setIPayType(int iPayType)
    {
        this.iPayType = iPayType;
    }

    public GetH5PayUrlReq()
    {
    }

    public GetH5PayUrlReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, String sDtPayOrderId, int iThirdPaySource, int iPayType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.sDtPayOrderId = sDtPayOrderId;
        this.iThirdPaySource = iThirdPaySource;
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
        ostream.writeString(2, sDtPayOrderId);
        ostream.writeInt32(3, iThirdPaySource);
        ostream.writeInt32(4, iPayType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sDtPayOrderId = (String)istream.readString(2, true, this.sDtPayOrderId);
        this.iThirdPaySource = (int)istream.readInt32(3, false, this.iThirdPaySource);
        this.iPayType = (int)istream.readInt32(4, false, this.iPayType);
    }

}

