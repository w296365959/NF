package BEC;

public final class LoginReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.AccountInfo stAccountInfo = null;

    public BEC.VerifyCode stVerifyCode = null;

    public int eLoginType = 0;

    public BEC.ThirdLoginInfo stThirdLoginInfo = null;

    public byte [] vtFaceData = null;

    public String sFaceImageFileType = "";

    public int iComeFrom = E_PHONE_REGISTER_COME_FROM.E_PHONE_REGISTER_APP;

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

    public int getELoginType()
    {
        return eLoginType;
    }

    public void  setELoginType(int eLoginType)
    {
        this.eLoginType = eLoginType;
    }

    public BEC.ThirdLoginInfo getStThirdLoginInfo()
    {
        return stThirdLoginInfo;
    }

    public void  setStThirdLoginInfo(BEC.ThirdLoginInfo stThirdLoginInfo)
    {
        this.stThirdLoginInfo = stThirdLoginInfo;
    }

    public byte [] getVtFaceData()
    {
        return vtFaceData;
    }

    public void  setVtFaceData(byte [] vtFaceData)
    {
        this.vtFaceData = vtFaceData;
    }

    public String getSFaceImageFileType()
    {
        return sFaceImageFileType;
    }

    public void  setSFaceImageFileType(String sFaceImageFileType)
    {
        this.sFaceImageFileType = sFaceImageFileType;
    }

    public int getIComeFrom()
    {
        return iComeFrom;
    }

    public void  setIComeFrom(int iComeFrom)
    {
        this.iComeFrom = iComeFrom;
    }

    public LoginReq()
    {
    }

    public LoginReq(BEC.UserInfo stUserInfo, BEC.AccountInfo stAccountInfo, BEC.VerifyCode stVerifyCode, int eLoginType, BEC.ThirdLoginInfo stThirdLoginInfo, byte [] vtFaceData, String sFaceImageFileType, int iComeFrom)
    {
        this.stUserInfo = stUserInfo;
        this.stAccountInfo = stAccountInfo;
        this.stVerifyCode = stVerifyCode;
        this.eLoginType = eLoginType;
        this.stThirdLoginInfo = stThirdLoginInfo;
        this.vtFaceData = vtFaceData;
        this.sFaceImageFileType = sFaceImageFileType;
        this.iComeFrom = iComeFrom;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stAccountInfo) {
            ostream.writeMessage(1, stAccountInfo);
        }
        if (null != stVerifyCode) {
            ostream.writeMessage(2, stVerifyCode);
        }
        ostream.writeInt32(3, eLoginType);
        if (null != stThirdLoginInfo) {
            ostream.writeMessage(4, stThirdLoginInfo);
        }
        if (null != vtFaceData) {
            ostream.writeBytes(5, vtFaceData);
        }
        if (null != sFaceImageFileType) {
            ostream.writeString(6, sFaceImageFileType);
        }
        ostream.writeInt32(7, iComeFrom);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountInfo VAR_TYPE_4_STACCOUNTINFO = new BEC.AccountInfo();

    static BEC.VerifyCode VAR_TYPE_4_STVERIFYCODE = new BEC.VerifyCode();

    static BEC.ThirdLoginInfo VAR_TYPE_4_STTHIRDLOGININFO = new BEC.ThirdLoginInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stAccountInfo = (BEC.AccountInfo)istream.readMessage(1, false, VAR_TYPE_4_STACCOUNTINFO);
        this.stVerifyCode = (BEC.VerifyCode)istream.readMessage(2, false, VAR_TYPE_4_STVERIFYCODE);
        this.eLoginType = (int)istream.readInt32(3, false, this.eLoginType);
        this.stThirdLoginInfo = (BEC.ThirdLoginInfo)istream.readMessage(4, false, VAR_TYPE_4_STTHIRDLOGININFO);
        this.vtFaceData = (byte [])istream.readBytes(5, false, this.vtFaceData);
        this.sFaceImageFileType = (String)istream.readString(6, false, this.sFaceImageFileType);
        this.iComeFrom = (int)istream.readInt32(7, false, this.iComeFrom);
    }

}

