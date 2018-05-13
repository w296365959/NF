package BEC;

public final class ModifyAccountInfoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eModifyType = 0;

    public BEC.AccountInfo stAccountInfo = null;

    public BEC.AccountTicket stAccountTicket = null;

    public BEC.VerifyCode stVerifyCode = null;

    public BEC.ThirdLoginInfo stThirdLoginInfo = null;

    public String sFaceImageFileType = "";

    public byte [] vtFaceData = null;

    public int iComeFrom = E_PHONE_REGISTER_COME_FROM.E_PHONE_REGISTER_APP;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEModifyType()
    {
        return eModifyType;
    }

    public void  setEModifyType(int eModifyType)
    {
        this.eModifyType = eModifyType;
    }

    public BEC.AccountInfo getStAccountInfo()
    {
        return stAccountInfo;
    }

    public void  setStAccountInfo(BEC.AccountInfo stAccountInfo)
    {
        this.stAccountInfo = stAccountInfo;
    }

    public BEC.AccountTicket getStAccountTicket()
    {
        return stAccountTicket;
    }

    public void  setStAccountTicket(BEC.AccountTicket stAccountTicket)
    {
        this.stAccountTicket = stAccountTicket;
    }

    public BEC.VerifyCode getStVerifyCode()
    {
        return stVerifyCode;
    }

    public void  setStVerifyCode(BEC.VerifyCode stVerifyCode)
    {
        this.stVerifyCode = stVerifyCode;
    }

    public BEC.ThirdLoginInfo getStThirdLoginInfo()
    {
        return stThirdLoginInfo;
    }

    public void  setStThirdLoginInfo(BEC.ThirdLoginInfo stThirdLoginInfo)
    {
        this.stThirdLoginInfo = stThirdLoginInfo;
    }

    public String getSFaceImageFileType()
    {
        return sFaceImageFileType;
    }

    public void  setSFaceImageFileType(String sFaceImageFileType)
    {
        this.sFaceImageFileType = sFaceImageFileType;
    }

    public byte [] getVtFaceData()
    {
        return vtFaceData;
    }

    public void  setVtFaceData(byte [] vtFaceData)
    {
        this.vtFaceData = vtFaceData;
    }

    public int getIComeFrom()
    {
        return iComeFrom;
    }

    public void  setIComeFrom(int iComeFrom)
    {
        this.iComeFrom = iComeFrom;
    }

    public ModifyAccountInfoReq()
    {
    }

    public ModifyAccountInfoReq(BEC.UserInfo stUserInfo, int eModifyType, BEC.AccountInfo stAccountInfo, BEC.AccountTicket stAccountTicket, BEC.VerifyCode stVerifyCode, BEC.ThirdLoginInfo stThirdLoginInfo, String sFaceImageFileType, byte [] vtFaceData, int iComeFrom)
    {
        this.stUserInfo = stUserInfo;
        this.eModifyType = eModifyType;
        this.stAccountInfo = stAccountInfo;
        this.stAccountTicket = stAccountTicket;
        this.stVerifyCode = stVerifyCode;
        this.stThirdLoginInfo = stThirdLoginInfo;
        this.sFaceImageFileType = sFaceImageFileType;
        this.vtFaceData = vtFaceData;
        this.iComeFrom = iComeFrom;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eModifyType);
        if (null != stAccountInfo) {
            ostream.writeMessage(2, stAccountInfo);
        }
        ostream.writeMessage(3, stAccountTicket);
        if (null != stVerifyCode) {
            ostream.writeMessage(4, stVerifyCode);
        }
        if (null != stThirdLoginInfo) {
            ostream.writeMessage(5, stThirdLoginInfo);
        }
        if (null != sFaceImageFileType) {
            ostream.writeString(6, sFaceImageFileType);
        }
        if (null != vtFaceData) {
            ostream.writeBytes(7, vtFaceData);
        }
        ostream.writeInt32(8, iComeFrom);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.AccountInfo VAR_TYPE_4_STACCOUNTINFO = new BEC.AccountInfo();

    static BEC.AccountTicket VAR_TYPE_4_STACCOUNTTICKET = new BEC.AccountTicket();

    static BEC.VerifyCode VAR_TYPE_4_STVERIFYCODE = new BEC.VerifyCode();

    static BEC.ThirdLoginInfo VAR_TYPE_4_STTHIRDLOGININFO = new BEC.ThirdLoginInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eModifyType = (int)istream.readInt32(1, false, this.eModifyType);
        this.stAccountInfo = (BEC.AccountInfo)istream.readMessage(2, false, VAR_TYPE_4_STACCOUNTINFO);
        this.stAccountTicket = (BEC.AccountTicket)istream.readMessage(3, true, VAR_TYPE_4_STACCOUNTTICKET);
        this.stVerifyCode = (BEC.VerifyCode)istream.readMessage(4, false, VAR_TYPE_4_STVERIFYCODE);
        this.stThirdLoginInfo = (BEC.ThirdLoginInfo)istream.readMessage(5, false, VAR_TYPE_4_STTHIRDLOGININFO);
        this.sFaceImageFileType = (String)istream.readString(6, false, this.sFaceImageFileType);
        this.vtFaceData = (byte [])istream.readBytes(7, false, this.vtFaceData);
        this.iComeFrom = (int)istream.readInt32(8, false, this.iComeFrom);
    }

}

