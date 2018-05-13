package BEC;

public final class UpdateFaverateVideoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public int iAction = 0;

    public java.util.ArrayList<String> vVideoKey = null;

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

    public int getIAction()
    {
        return iAction;
    }

    public void  setIAction(int iAction)
    {
        this.iAction = iAction;
    }

    public java.util.ArrayList<String> getVVideoKey()
    {
        return vVideoKey;
    }

    public void  setVVideoKey(java.util.ArrayList<String> vVideoKey)
    {
        this.vVideoKey = vVideoKey;
    }

    public UpdateFaverateVideoReq()
    {
    }

    public UpdateFaverateVideoReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, int iAction, java.util.ArrayList<String> vVideoKey)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iAction = iAction;
        this.vVideoKey = vVideoKey;
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
        ostream.writeInt32(2, iAction);
        if (null != vVideoKey) {
            ostream.writeList(3, vVideoKey);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static java.util.ArrayList<String> VAR_TYPE_4_VVIDEOKEY = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VVIDEOKEY.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iAction = (int)istream.readInt32(2, false, this.iAction);
        this.vVideoKey = (java.util.ArrayList<String>)istream.readList(3, false, VAR_TYPE_4_VVIDEOKEY);
    }

}

