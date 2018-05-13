package BEC;

public final class GetPayOrderIdReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public int iSubjectType = 0;

    public int iNumber = 0;

    public BEC.PayOrderExtra stExtra = null;

    public int iNumberUnit = E_DT_PAY_TIME_UNIT.E_DT_PAY_TIME_MONTH;

    public int iPayChannel = E_DT_PAY_CHANNEL.E_DT_PAY_CHANNEL_APP;

    public java.util.ArrayList<String> vCouponCode = null;

    public String sCommExtraJson = "";

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

    public int getISubjectType()
    {
        return iSubjectType;
    }

    public void  setISubjectType(int iSubjectType)
    {
        this.iSubjectType = iSubjectType;
    }

    public int getINumber()
    {
        return iNumber;
    }

    public void  setINumber(int iNumber)
    {
        this.iNumber = iNumber;
    }

    public BEC.PayOrderExtra getStExtra()
    {
        return stExtra;
    }

    public void  setStExtra(BEC.PayOrderExtra stExtra)
    {
        this.stExtra = stExtra;
    }

    public int getINumberUnit()
    {
        return iNumberUnit;
    }

    public void  setINumberUnit(int iNumberUnit)
    {
        this.iNumberUnit = iNumberUnit;
    }

    public int getIPayChannel()
    {
        return iPayChannel;
    }

    public void  setIPayChannel(int iPayChannel)
    {
        this.iPayChannel = iPayChannel;
    }

    public java.util.ArrayList<String> getVCouponCode()
    {
        return vCouponCode;
    }

    public void  setVCouponCode(java.util.ArrayList<String> vCouponCode)
    {
        this.vCouponCode = vCouponCode;
    }

    public String getSCommExtraJson()
    {
        return sCommExtraJson;
    }

    public void  setSCommExtraJson(String sCommExtraJson)
    {
        this.sCommExtraJson = sCommExtraJson;
    }

    public GetPayOrderIdReq()
    {
    }

    public GetPayOrderIdReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, int iSubjectType, int iNumber, BEC.PayOrderExtra stExtra, int iNumberUnit, int iPayChannel, java.util.ArrayList<String> vCouponCode, String sCommExtraJson)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iSubjectType = iSubjectType;
        this.iNumber = iNumber;
        this.stExtra = stExtra;
        this.iNumberUnit = iNumberUnit;
        this.iPayChannel = iPayChannel;
        this.vCouponCode = vCouponCode;
        this.sCommExtraJson = sCommExtraJson;
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
        ostream.writeInt32(2, iSubjectType);
        ostream.writeInt32(3, iNumber);
        if (null != stExtra) {
            ostream.writeMessage(4, stExtra);
        }
        ostream.writeInt32(5, iNumberUnit);
        ostream.writeInt32(6, iPayChannel);
        if (null != vCouponCode) {
            ostream.writeList(7, vCouponCode);
        }
        if (null != sCommExtraJson) {
            ostream.writeString(8, sCommExtraJson);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static BEC.PayOrderExtra VAR_TYPE_4_STEXTRA = new BEC.PayOrderExtra();

    static java.util.ArrayList<String> VAR_TYPE_4_VCOUPONCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VCOUPONCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iSubjectType = (int)istream.readInt32(2, true, this.iSubjectType);
        this.iNumber = (int)istream.readInt32(3, true, this.iNumber);
        this.stExtra = (BEC.PayOrderExtra)istream.readMessage(4, false, VAR_TYPE_4_STEXTRA);
        this.iNumberUnit = (int)istream.readInt32(5, false, this.iNumberUnit);
        this.iPayChannel = (int)istream.readInt32(6, false, this.iPayChannel);
        this.vCouponCode = (java.util.ArrayList<String>)istream.readList(7, false, VAR_TYPE_4_VCOUPONCODE);
        this.sCommExtraJson = (String)istream.readString(8, false, this.sCommExtraJson);
    }

}

