package BEC;

public final class SetUserRelationReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eSetType = BEC.E_FEED_USER_RELATION_SET_TYPE.E_FURST_FOLLOW;

    public long iDstAccountId = 0;

    public BEC.AccountTicket stAccountTicket = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getESetType()
    {
        return eSetType;
    }

    public void  setESetType(int eSetType)
    {
        this.eSetType = eSetType;
    }

    public long getIDstAccountId()
    {
        return iDstAccountId;
    }

    public void  setIDstAccountId(long iDstAccountId)
    {
        this.iDstAccountId = iDstAccountId;
    }

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public SetUserRelationReq()
    {
    }

    public SetUserRelationReq(BEC.UserInfo stUserInfo, int eSetType, long iDstAccountId, BEC.AccountTicket stAccountTicket)
    {
        this.stUserInfo = stUserInfo;
        this.eSetType = eSetType;
        this.iDstAccountId = iDstAccountId;
        this.stAccountTicket = stAccountTicket;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eSetType);
        ostream.writeUInt32(2, iDstAccountId);
        if (null != stAccountTicket) {
            ostream.writeMessage(3, stAccountTicket);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eSetType = (int)istream.readInt32(1, false, this.eSetType);
        this.iDstAccountId = (long)istream.readUInt32(2, false, this.iDstAccountId);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(3, false, VAR_TYPE_4_STACCOUNTTICKET);
    }

}

