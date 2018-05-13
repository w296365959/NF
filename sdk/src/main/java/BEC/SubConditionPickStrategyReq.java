package BEC;

public final class SubConditionPickStrategyReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public int eSubAction = 0;

    public ConditionPickStrategy stConditionPickStrategy = null;

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

    public int getESubAction()
    {
        return eSubAction;
    }

    public void  setESubAction(int eSubAction)
    {
        this.eSubAction = eSubAction;
    }

    public ConditionPickStrategy getStConditionPickStrategy()
    {
        return stConditionPickStrategy;
    }

    public void  setStConditionPickStrategy(ConditionPickStrategy stConditionPickStrategy)
    {
        this.stConditionPickStrategy = stConditionPickStrategy;
    }

    public SubConditionPickStrategyReq()
    {
    }

    public SubConditionPickStrategyReq(UserInfo stUserInfo, AccountTicket stAccountTicket, int eSubAction, ConditionPickStrategy stConditionPickStrategy)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.eSubAction = eSubAction;
        this.stConditionPickStrategy = stConditionPickStrategy;
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
        ostream.writeInt32(2, eSubAction);
        if (null != stConditionPickStrategy) {
            ostream.writeMessage(3, stConditionPickStrategy);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();

    static ConditionPickStrategy VAR_TYPE_4_STCONDITIONPICKSTRATEGY = new ConditionPickStrategy();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.eSubAction = (int)istream.readInt32(2, false, this.eSubAction);
        this.stConditionPickStrategy = (ConditionPickStrategy)istream.readMessage(3, false, VAR_TYPE_4_STCONDITIONPICKSTRATEGY);
    }

}

