package BEC;

public final class AlertMsgClassListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public long iAccountId = 0;

    public AccountTicket stAccountTicket = null;

    public java.util.Map<Integer, Integer> mEndPushTime = null;

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

    public java.util.Map<Integer, Integer> getMEndPushTime()
    {
        return mEndPushTime;
    }

    public void  setMEndPushTime(java.util.Map<Integer, Integer> mEndPushTime)
    {
        this.mEndPushTime = mEndPushTime;
    }

    public AlertMsgClassListReq()
    {
    }

    public AlertMsgClassListReq(UserInfo stUserInfo, long iAccountId, AccountTicket stAccountTicket, java.util.Map<Integer, Integer> mEndPushTime)
    {
        this.stUserInfo = stUserInfo;
        this.iAccountId = iAccountId;
        this.stAccountTicket = stAccountTicket;
        this.mEndPushTime = mEndPushTime;
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
        if (null != mEndPushTime) {
            ostream.writeMap(4, mEndPushTime);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();

    static java.util.Map<Integer, Integer> VAR_TYPE_4_MENDPUSHTIME = new java.util.HashMap<Integer, Integer>();
    static {
        VAR_TYPE_4_MENDPUSHTIME.put(0, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iAccountId = (long)istream.readUInt32(1, false, this.iAccountId);
        this.stAccountTicket = (AccountTicket)istream.readMessage(2, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.mEndPushTime = (java.util.Map<Integer, Integer>)istream.readMap(4, false, VAR_TYPE_4_MENDPUSHTIME);
    }

}

