package BEC;

public final class GetUserEvalResultReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public int iEvalType = 0;

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

    public int getIEvalType()
    {
        return iEvalType;
    }

    public void  setIEvalType(int iEvalType)
    {
        this.iEvalType = iEvalType;
    }

    public GetUserEvalResultReq()
    {
    }

    public GetUserEvalResultReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, int iEvalType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iEvalType = iEvalType;
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
        ostream.writeInt32(2, iEvalType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iEvalType = (int)istream.readInt32(2, false, this.iEvalType);
    }

}

