package BEC;

public final class VerfiyThirdAccountReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.ThirdLoginInfo stThirdLoginInfo = null;

    public int eLoginType = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.ThirdLoginInfo getStThirdLoginInfo()
    {
        return stThirdLoginInfo;
    }

    public void  setStThirdLoginInfo(BEC.ThirdLoginInfo stThirdLoginInfo)
    {
        this.stThirdLoginInfo = stThirdLoginInfo;
    }

    public int getELoginType()
    {
        return eLoginType;
    }

    public void  setELoginType(int eLoginType)
    {
        this.eLoginType = eLoginType;
    }

    public VerfiyThirdAccountReq()
    {
    }

    public VerfiyThirdAccountReq(BEC.UserInfo stUserInfo, BEC.ThirdLoginInfo stThirdLoginInfo, int eLoginType)
    {
        this.stUserInfo = stUserInfo;
        this.stThirdLoginInfo = stThirdLoginInfo;
        this.eLoginType = eLoginType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stThirdLoginInfo) {
            ostream.writeMessage(1, stThirdLoginInfo);
        }
        ostream.writeInt32(2, eLoginType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.ThirdLoginInfo VAR_TYPE_4_STTHIRDLOGININFO = new BEC.ThirdLoginInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stThirdLoginInfo = (BEC.ThirdLoginInfo)istream.readMessage(1, false, VAR_TYPE_4_STTHIRDLOGININFO);
        this.eLoginType = (int)istream.readInt32(2, false, this.eLoginType);
    }

}

