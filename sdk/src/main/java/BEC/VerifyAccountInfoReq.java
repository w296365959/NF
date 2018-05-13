package BEC;

public final class VerifyAccountInfoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountInfo stAccountInfo = null;

    public BEC.VerifyCode stVerifyCode = null;

    public int eVerifyWay = 0;

    public int eVerifyType = 0;

    public String sMachineVerfiyCode = "";

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

    public BEC.VerifyCode getStVerifyCode()
    {
        return stVerifyCode;
    }

    public void  setStVerifyCode(BEC.VerifyCode stVerifyCode)
    {
        this.stVerifyCode = stVerifyCode;
    }

    public int getEVerifyWay()
    {
        return eVerifyWay;
    }

    public void  setEVerifyWay(int eVerifyWay)
    {
        this.eVerifyWay = eVerifyWay;
    }

    public int getEVerifyType()
    {
        return eVerifyType;
    }

    public void  setEVerifyType(int eVerifyType)
    {
        this.eVerifyType = eVerifyType;
    }

    public String getSMachineVerfiyCode()
    {
        return sMachineVerfiyCode;
    }

    public void  setSMachineVerfiyCode(String sMachineVerfiyCode)
    {
        this.sMachineVerfiyCode = sMachineVerfiyCode;
    }

    public VerifyAccountInfoReq()
    {
    }

    public VerifyAccountInfoReq(BEC.UserInfo stUserInfo, BEC.AccountInfo stAccountInfo, BEC.VerifyCode stVerifyCode, int eVerifyWay, int eVerifyType, String sMachineVerfiyCode)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountInfo = stAccountInfo;
        this.stVerifyCode = stVerifyCode;
        this.eVerifyWay = eVerifyWay;
        this.eVerifyType = eVerifyType;
        this.sMachineVerfiyCode = sMachineVerfiyCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeMessage(1, stAccountInfo);
        if (null != stVerifyCode) {
            ostream.writeMessage(2, stVerifyCode);
        }
        ostream.writeInt32(3, eVerifyWay);
        ostream.writeInt32(4, eVerifyType);
        if (null != sMachineVerfiyCode) {
            ostream.writeString(5, sMachineVerfiyCode);
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
        this.stVerifyCode = (BEC.VerifyCode)istream.readMessage(2, false, VAR_TYPE_4_STVERIFYCODE);
        this.eVerifyWay = (int)istream.readInt32(3, false, this.eVerifyWay);
        this.eVerifyType = (int)istream.readInt32(4, false, this.eVerifyType);
        this.sMachineVerfiyCode = (String)istream.readString(5, false, this.sMachineVerfiyCode);
    }

}

