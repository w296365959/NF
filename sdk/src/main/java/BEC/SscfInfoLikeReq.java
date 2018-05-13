package BEC;

public final class SscfInfoLikeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public AccountTicket stAccountTicket = null;

    public int iSscfInfoID = 0;

    public boolean isAdd = true;

    public int eInfoType = E_INFO_TYPE.E_IT_TEACH;

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

    public int getISscfInfoID()
    {
        return iSscfInfoID;
    }

    public void  setISscfInfoID(int iSscfInfoID)
    {
        this.iSscfInfoID = iSscfInfoID;
    }

    public boolean getIsAdd()
    {
        return isAdd;
    }

    public void  setIsAdd(boolean isAdd)
    {
        this.isAdd = isAdd;
    }

    public int getEInfoType()
    {
        return eInfoType;
    }

    public void  setEInfoType(int eInfoType)
    {
        this.eInfoType = eInfoType;
    }

    public SscfInfoLikeReq()
    {
    }

    public SscfInfoLikeReq(UserInfo stUserInfo, AccountTicket stAccountTicket, int iSscfInfoID, boolean isAdd, int eInfoType)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.iSscfInfoID = iSscfInfoID;
        this.isAdd = isAdd;
        this.eInfoType = eInfoType;
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
        ostream.writeInt32(2, iSscfInfoID);
        ostream.writeBoolean(3, isAdd);
        ostream.writeInt32(4, eInfoType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new AccountTicket();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.iSscfInfoID = (int)istream.readInt32(2, false, this.iSscfInfoID);
        this.isAdd = (boolean)istream.readBoolean(3, false, this.isAdd);
        this.eInfoType = (int)istream.readInt32(4, false, this.eInfoType);
    }

}

