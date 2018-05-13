package BEC;

public final class FinishRegisterAccountRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.AccountTicket stAccountTicket = null;

    public BEC.AccountInfo stAccountInfo = null;

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public BEC.AccountInfo getStAccountInfo()
    {
        return stAccountInfo;
    }

    public void  setStAccountInfo(BEC.AccountInfo stAccountInfo)
    {
        this.stAccountInfo = stAccountInfo;
    }

    public FinishRegisterAccountRsp()
    {
    }

    public FinishRegisterAccountRsp(BEC.AccountTicket stAccountTicket, BEC.AccountInfo stAccountInfo)
    {
        this.stAccountTicket = stAccountTicket;
        this.stAccountInfo = stAccountInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stAccountTicket) {
            ostream.writeMessage(0, stAccountTicket);
        }
        if (null != stAccountInfo) {
            ostream.writeMessage(1, stAccountInfo);
        }
    }

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static BEC.AccountInfo VAR_TYPE_4_STACCOUNTINFO = new BEC.AccountInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(0, false, VAR_TYPE_4_STACCOUNTTICKET);
        this.stAccountInfo = (BEC.AccountInfo)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTINFO);
    }

}

