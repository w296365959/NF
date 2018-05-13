package BEC;

public final class AccountInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public String sUserName = "";

    public byte [] vtPassword = null;

    public byte [] vtDupliPassword = null;

    public byte [] vtOldPassword = null;

    public String sPhoneNum = "";

    public String sFaceUrl = "";

    public int iGender = 0;

    public String sProvince = "";

    public String sCity = "";

    public String sProfile = "";

    public String sVerifyDesc = "";

    public int eUserType = E_FEED_USER_TYPE.E_FEED_USER_NORMAL;

    public DtMemberInfo stMember = null;

    public String sUserRealName = "";

    public String sUserIDNumber = "";

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public String getSUserName()
    {
        return sUserName;
    }

    public void  setSUserName(String sUserName)
    {
        this.sUserName = sUserName;
    }

    public byte [] getVtPassword()
    {
        return vtPassword;
    }

    public void  setVtPassword(byte [] vtPassword)
    {
        this.vtPassword = vtPassword;
    }

    public byte [] getVtDupliPassword()
    {
        return vtDupliPassword;
    }

    public void  setVtDupliPassword(byte [] vtDupliPassword)
    {
        this.vtDupliPassword = vtDupliPassword;
    }

    public byte [] getVtOldPassword()
    {
        return vtOldPassword;
    }

    public void  setVtOldPassword(byte [] vtOldPassword)
    {
        this.vtOldPassword = vtOldPassword;
    }

    public String getSPhoneNum()
    {
        return sPhoneNum;
    }

    public void  setSPhoneNum(String sPhoneNum)
    {
        this.sPhoneNum = sPhoneNum;
    }

    public String getSFaceUrl()
    {
        return sFaceUrl;
    }

    public void  setSFaceUrl(String sFaceUrl)
    {
        this.sFaceUrl = sFaceUrl;
    }

    public int getIGender()
    {
        return iGender;
    }

    public void  setIGender(int iGender)
    {
        this.iGender = iGender;
    }

    public String getSProvince()
    {
        return sProvince;
    }

    public void  setSProvince(String sProvince)
    {
        this.sProvince = sProvince;
    }

    public String getSCity()
    {
        return sCity;
    }

    public void  setSCity(String sCity)
    {
        this.sCity = sCity;
    }

    public String getSProfile()
    {
        return sProfile;
    }

    public void  setSProfile(String sProfile)
    {
        this.sProfile = sProfile;
    }

    public String getSVerifyDesc()
    {
        return sVerifyDesc;
    }

    public void  setSVerifyDesc(String sVerifyDesc)
    {
        this.sVerifyDesc = sVerifyDesc;
    }

    public int getEUserType()
    {
        return eUserType;
    }

    public void  setEUserType(int eUserType)
    {
        this.eUserType = eUserType;
    }

    public BEC.DtMemberInfo getStMember()
    {
        return stMember;
    }

    public void  setStMember(BEC.DtMemberInfo stMember)
    {
        this.stMember = stMember;
    }

    public AccountInfo()
    {
    }

    public AccountInfo(long iAccountId, String sUserName, byte [] vtPassword, byte [] vtDupliPassword, byte [] vtOldPassword, String sPhoneNum, String sFaceUrl, int iGender, String sProvince, String sCity, String sProfile, String sVerifyDesc, int eUserType, BEC.DtMemberInfo stMember)
    {
        this.iAccountId = iAccountId;
        this.sUserName = sUserName;
        this.vtPassword = vtPassword;
        this.vtDupliPassword = vtDupliPassword;
        this.vtOldPassword = vtOldPassword;
        this.sPhoneNum = sPhoneNum;
        this.sFaceUrl = sFaceUrl;
        this.iGender = iGender;
        this.sProvince = sProvince;
        this.sCity = sCity;
        this.sProfile = sProfile;
        this.sVerifyDesc = sVerifyDesc;
        this.eUserType = eUserType;
        this.stMember = stMember;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        if (null != sUserName) {
            ostream.writeString(1, sUserName);
        }
        if (null != vtPassword) {
            ostream.writeBytes(2, vtPassword);
        }
        if (null != vtDupliPassword) {
            ostream.writeBytes(3, vtDupliPassword);
        }
        if (null != vtOldPassword) {
            ostream.writeBytes(4, vtOldPassword);
        }
        if (null != sPhoneNum) {
            ostream.writeString(5, sPhoneNum);
        }
        if (null != sFaceUrl) {
            ostream.writeString(6, sFaceUrl);
        }
        ostream.writeInt32(7, iGender);
        if (null != sProvince) {
            ostream.writeString(8, sProvince);
        }
        if (null != sCity) {
            ostream.writeString(9, sCity);
        }
        if (null != sProfile) {
            ostream.writeString(10, sProfile);
        }
        if (null != sVerifyDesc) {
            ostream.writeString(11, sVerifyDesc);
        }
        ostream.writeInt32(12, eUserType);
        if (null != stMember) {
            ostream.writeMessage(13, stMember);
        }
    }

    static BEC.DtMemberInfo VAR_TYPE_4_STMEMBER = new BEC.DtMemberInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.sUserName = (String)istream.readString(1, false, this.sUserName);
        this.vtPassword = (byte [])istream.readBytes(2, false, this.vtPassword);
        this.vtDupliPassword = (byte [])istream.readBytes(3, false, this.vtDupliPassword);
        this.vtOldPassword = (byte [])istream.readBytes(4, false, this.vtOldPassword);
        this.sPhoneNum = (String)istream.readString(5, false, this.sPhoneNum);
        this.sFaceUrl = (String)istream.readString(6, false, this.sFaceUrl);
        this.iGender = (int)istream.readInt32(7, false, this.iGender);
        this.sProvince = (String)istream.readString(8, false, this.sProvince);
        this.sCity = (String)istream.readString(9, false, this.sCity);
        this.sProfile = (String)istream.readString(10, false, this.sProfile);
        this.sVerifyDesc = (String)istream.readString(11, false, this.sVerifyDesc);
        this.eUserType = (int)istream.readInt32(12, false, this.eUserType);
        this.stMember = (BEC.DtMemberInfo)istream.readMessage(13, false, VAR_TYPE_4_STMEMBER);
    }

}

