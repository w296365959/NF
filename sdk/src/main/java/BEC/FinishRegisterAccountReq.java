package BEC;

public final class FinishRegisterAccountReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountInfo stAccountInfo = null;

    public int iComeFrom = E_PHONE_REGISTER_COME_FROM.E_PHONE_REGISTER_APP;

    public BEC.VerifyCode stVerifyCode = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.AccountInfo getStAccountInfo()
    {
        return stAccountInfo;
    }

    public void  setStAccountInfo(BEC.AccountInfo stAccountInfo)
    {
        this.stAccountInfo = stAccountInfo;
    }

    public int getIComeFrom()
    {
        return iComeFrom;
    }

    public void  setIComeFrom(int iComeFrom)
    {
        this.iComeFrom = iComeFrom;
    }

    public BEC.VerifyCode getStVerifyCode()
    {
        return stVerifyCode;
    }

    public void  setStVerifyCode(BEC.VerifyCode stVerifyCode)
    {
        this.stVerifyCode = stVerifyCode;
    }

    public FinishRegisterAccountReq()
    {
    }

    public FinishRegisterAccountReq(BEC.UserInfo stUserInfo, BEC.AccountInfo stAccountInfo, int iComeFrom, BEC.VerifyCode stVerifyCode)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountInfo = stAccountInfo;
        this.iComeFrom = iComeFrom;
        this.stVerifyCode = stVerifyCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeMessage(1, stAccountInfo);
        ostream.writeInt32(2, iComeFrom);
        if (null != stVerifyCode) {
            ostream.writeMessage(3, stVerifyCode);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountInfo VAR_TYPE_4_STACCOUNTINFO = new BEC.AccountInfo();

    static BEC.VerifyCode VAR_TYPE_4_STVERIFYCODE = new BEC.VerifyCode();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountInfo = (BEC.AccountInfo)istream.readMessage(1, true, VAR_TYPE_4_STACCOUNTINFO);
        this.iComeFrom = (int)istream.readInt32(2, false, this.iComeFrom);
        this.stVerifyCode = (BEC.VerifyCode)istream.readMessage(3, false, VAR_TYPE_4_STVERIFYCODE);
    }

}

