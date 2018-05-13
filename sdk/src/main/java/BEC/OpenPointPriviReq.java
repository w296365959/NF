package BEC;

public final class OpenPointPriviReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public int eOpenType = AccuPointOpenType.E_ACCU_POINT_OPEN_BY_POINT;

    public java.util.ArrayList<OpenPointPriviItem> vItem = null;

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

    public int getEOpenType()
    {
        return eOpenType;
    }

    public void  setEOpenType(int eOpenType)
    {
        this.eOpenType = eOpenType;
    }

    public java.util.ArrayList<OpenPointPriviItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<OpenPointPriviItem> vItem)
    {
        this.vItem = vItem;
    }

    public OpenPointPriviReq()
    {
    }

    public OpenPointPriviReq(UserInfo stUserInfo, AccountTicket stAccountTicket, int eOpenType, java.util.ArrayList<OpenPointPriviItem> vItem)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.eOpenType = eOpenType;
        this.vItem = vItem;
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
        ostream.writeInt32(2, eOpenType);
        if (null != vItem) {
            ostream.writeList(3, vItem);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();

    static java.util.ArrayList<OpenPointPriviItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<OpenPointPriviItem>();
    static {
        VAR_TYPE_4_VITEM.add(new OpenPointPriviItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.eOpenType = (int)istream.readInt32(2, false, this.eOpenType);
        this.vItem = (java.util.ArrayList<OpenPointPriviItem>)istream.readList(3, false, VAR_TYPE_4_VITEM);
    }

}

