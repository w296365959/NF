package BEC;

public final class GetOrderPayResultReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public int iPayType = 0;

    public String sInnerOrderId = "";

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

    public int getIPayType()
    {
        return iPayType;
    }

    public void  setIPayType(int iPayType)
    {
        this.iPayType = iPayType;
    }

    public String getSInnerOrderId()
    {
        return sInnerOrderId;
    }

    public void  setSInnerOrderId(String sInnerOrderId)
    {
        this.sInnerOrderId = sInnerOrderId;
    }

    public GetOrderPayResultReq()
    {
    }

    public GetOrderPayResultReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, int iPayType, String sInnerOrderId)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iPayType = iPayType;
        this.sInnerOrderId = sInnerOrderId;
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
        ostream.writeInt32(2, iPayType);
        if (null != sInnerOrderId) {
            ostream.writeString(3, sInnerOrderId);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iPayType = (int)istream.readInt32(2, false, this.iPayType);
        this.sInnerOrderId = (String)istream.readString(3, false, this.sInnerOrderId);
    }

}

