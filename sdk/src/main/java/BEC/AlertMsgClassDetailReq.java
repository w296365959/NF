package BEC;

public final class AlertMsgClassDetailReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public long iAccountId = 0;

    public AccountTicket stAccountTicket = null;

    public int iClassID = 0;

    public int iStart = 0;

    public int iNum = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public int getIClassID()
    {
        return iClassID;
    }

    public void  setIClassID(int iClassID)
    {
        this.iClassID = iClassID;
    }

    public int getIStart()
    {
        return iStart;
    }

    public void  setIStart(int iStart)
    {
        this.iStart = iStart;
    }

    public int getINum()
    {
        return iNum;
    }

    public void  setINum(int iNum)
    {
        this.iNum = iNum;
    }

    public AlertMsgClassDetailReq()
    {
    }

    public AlertMsgClassDetailReq(UserInfo stUserInfo, long iAccountId, AccountTicket stAccountTicket, int iClassID, int iStart, int iNum)
    {
        this.stUserInfo = stUserInfo;
        this.iAccountId = iAccountId;
        this.stAccountTicket = stAccountTicket;
        this.iClassID = iClassID;
        this.iStart = iStart;
        this.iNum = iNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeUInt32(1, iAccountId);
        if (null != stAccountTicket) {
            ostream.writeMessage(2, stAccountTicket);
        }
        ostream.writeInt32(3, iClassID);
        ostream.writeInt32(4, iStart);
        ostream.writeInt32(5, iNum);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iAccountId = (long)istream.readUInt32(1, false, this.iAccountId);
        this.stAccountTicket = (AccountTicket)istream.readMessage(2, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iClassID = (int)istream.readInt32(3, false, this.iClassID);
        this.iStart = (int)istream.readInt32(4, false, this.iStart);
        this.iNum = (int)istream.readInt32(5, false, this.iNum);
    }

}

