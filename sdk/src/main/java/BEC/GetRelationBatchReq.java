package BEC;

public final class GetRelationBatchReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public java.util.ArrayList<Long> vAccountId = null;

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

    public java.util.ArrayList<Long> getVAccountId()
    {
        return vAccountId;
    }

    public void  setVAccountId(java.util.ArrayList<Long> vAccountId)
    {
        this.vAccountId = vAccountId;
    }

    public GetRelationBatchReq()
    {
    }

    public GetRelationBatchReq(BEC.UserInfo stUserInfo, BEC.AccountTicket stAccountTicket, java.util.ArrayList<Long> vAccountId)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountTicket = stAccountTicket;
        this.vAccountId = vAccountId;
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
        if (null != vAccountId) {
            ostream.writeList(2, vAccountId);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static java.util.ArrayList<Long> VAR_TYPE_4_VACCOUNTID = new java.util.ArrayList<Long>();
    static {
        VAR_TYPE_4_VACCOUNTID.add(0L);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.vAccountId = (java.util.ArrayList<Long>)istream.readList(2, false, VAR_TYPE_4_VACCOUNTID);
    }

}

