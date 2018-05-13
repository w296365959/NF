package BEC;

public final class SaveFavorNewsReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public long iAccountId = 0;

    public BEC.AccountTicket stAccountTicket = null;

    public java.util.ArrayList<BEC.FavorAction> vFavorAction = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
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

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public java.util.ArrayList<BEC.FavorAction> getVFavorAction()
    {
        return vFavorAction;
    }

    public void  setVFavorAction(java.util.ArrayList<BEC.FavorAction> vFavorAction)
    {
        this.vFavorAction = vFavorAction;
    }

    public SaveFavorNewsReq()
    {
    }

    public SaveFavorNewsReq(BEC.UserInfo stUserInfo, long iAccountId, BEC.AccountTicket stAccountTicket, java.util.ArrayList<BEC.FavorAction> vFavorAction)
    {
        this.stUserInfo = stUserInfo;
        this.iAccountId = iAccountId;
        this.stAccountTicket = stAccountTicket;
        this.vFavorAction = vFavorAction;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeUInt32(2, iAccountId);
        if (null != stAccountTicket) {
            ostream.writeMessage(3, stAccountTicket);
        }
        if (null != vFavorAction) {
            ostream.writeList(4, vFavorAction);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static java.util.ArrayList<BEC.FavorAction> VAR_TYPE_4_VFAVORACTION = new java.util.ArrayList<BEC.FavorAction>();
    static {
        VAR_TYPE_4_VFAVORACTION.add(new BEC.FavorAction());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iAccountId = (long)istream.readUInt32(2, false, this.iAccountId);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(3, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.vFavorAction = (java.util.ArrayList<BEC.FavorAction>)istream.readList(4, false, VAR_TYPE_4_VFAVORACTION);
    }

}

