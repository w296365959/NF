package BEC;

public final class CheckUserCouponReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public int iSubjectType = 0;

    public int iNumber = 0;

    public int iNumberUnit = E_DT_PAY_TIME_UNIT.E_DT_PAY_TIME_MONTH;

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

    public int getINumberUnit()
    {
        return iNumberUnit;
    }

    public void  setINumberUnit(int iNumberUnit)
    {
        this.iNumberUnit = iNumberUnit;
    }

    public String getSCommExtraJson()
    {
        return sCommExtraJson;
    }

    public void  setSCommExtraJson(String sCommExtraJson)
    {
        this.sCommExtraJson = sCommExtraJson;
    }

    public CheckUserCouponReq()
    {
    }

    public CheckUserCouponReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, int iSubjectType, int iNumber, int iNumberUnit, String sCommExtraJson)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iSubjectType = iSubjectType;
        this.iNumber = iNumber;
        this.iNumberUnit = iNumberUnit;
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
        ostream.writeInt32(4, iNumberUnit);
        if (null != sCommExtraJson) {
            ostream.writeString(5, sCommExtraJson);
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
        this.iSubjectType = (int)istream.readInt32(2, true, this.iSubjectType);
        this.iNumber = (int)istream.readInt32(3, true, this.iNumber);
        this.iNumberUnit = (int)istream.readInt32(4, false, this.iNumberUnit);
        this.sCommExtraJson = (String)istream.readString(5, false, this.sCommExtraJson);
    }

}

