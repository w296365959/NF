package BEC;

public final class GetSecDailyReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int iStart = 0;

    public int iCount = 0;

    public AccountTicket stAccountTicket = null;

    public String sTradingDay = "";

    public String sPreTradingDay = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIStart()
    {
        return iStart;
    }

    public void  setIStart(int iStart)
    {
        this.iStart = iStart;
    }

    public int getICount()
    {
        return iCount;
    }

    public void  setICount(int iCount)
    {
        this.iCount = iCount;
    }

    public AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public String getSPreTradingDay()
    {
        return sPreTradingDay;
    }

    public void  setSPreTradingDay(String sPreTradingDay)
    {
        this.sPreTradingDay = sPreTradingDay;
    }

    public GetSecDailyReq()
    {
    }

    public GetSecDailyReq(UserInfo stUserInfo, int iStart, int iCount, AccountTicket stAccountTicket, String sTradingDay, String sPreTradingDay)
    {
        this.stUserInfo = stUserInfo;
        this.iStart = iStart;
        this.iCount = iCount;
        this.stAccountTicket = stAccountTicket;
        this.sTradingDay = sTradingDay;
        this.sPreTradingDay = sPreTradingDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iStart);
        ostream.writeInt32(2, iCount);
        if (null != stAccountTicket) {
            ostream.writeMessage(3, stAccountTicket);
        }
        if (null != sTradingDay) {
            ostream.writeString(4, sTradingDay);
        }
        if (null != sPreTradingDay) {
            ostream.writeString(5, sPreTradingDay);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iStart = (int)istream.readInt32(1, false, this.iStart);
        this.iCount = (int)istream.readInt32(2, false, this.iCount);
        this.stAccountTicket = (AccountTicket)istream.readMessage(3, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.sTradingDay = (String)istream.readString(4, false, this.sTradingDay);
        this.sPreTradingDay = (String)istream.readString(5, false, this.sPreTradingDay);
    }

}

