package BEC;

public final class FeedUserBaseInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public int iDynamic = 0;

    public int iFollower = 0;

    public int iFans = 0;

    public String sNickName = "";

    public String sFaceUrl = "";

    public int iGender = 0;

    public String sProvince = "";

    public String sCity = "";

    public String sProfile = "";

    public String sVerifyDesc = "";

    public int eUserType = BEC.E_FEED_USER_TYPE.E_FEED_USER_NORMAL;

    public int iInvestNum = 0;

    public long lInvestTimeStamp = 0;

    public BEC.DtMemberInfo stMember = null;

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public int getIDynamic()
    {
        return iDynamic;
    }

    public void  setIDynamic(int iDynamic)
    {
        this.iDynamic = iDynamic;
    }

    public int getIFollower()
    {
        return iFollower;
    }

    public void  setIFollower(int iFollower)
    {
        this.iFollower = iFollower;
    }

    public int getIFans()
    {
        return iFans;
    }

    public void  setIFans(int iFans)
    {
        this.iFans = iFans;
    }

    public String getSNickName()
    {
        return sNickName;
    }

    public void  setSNickName(String sNickName)
    {
        this.sNickName = sNickName;
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

    public int getIInvestNum()
    {
        return iInvestNum;
    }

    public void  setIInvestNum(int iInvestNum)
    {
        this.iInvestNum = iInvestNum;
    }

    public long getLInvestTimeStamp()
    {
        return lInvestTimeStamp;
    }

    public void  setLInvestTimeStamp(long lInvestTimeStamp)
    {
        this.lInvestTimeStamp = lInvestTimeStamp;
    }

    public BEC.DtMemberInfo getStMember()
    {
        return stMember;
    }

    public void  setStMember(BEC.DtMemberInfo stMember)
    {
        this.stMember = stMember;
    }

    public FeedUserBaseInfo()
    {
    }

    public FeedUserBaseInfo(long iAccountId, int iDynamic, int iFollower, int iFans, String sNickName, String sFaceUrl, int iGender, String sProvince, String sCity, String sProfile, String sVerifyDesc, int eUserType, int iInvestNum, long lInvestTimeStamp, BEC.DtMemberInfo stMember)
    {
        this.iAccountId = iAccountId;
        this.iDynamic = iDynamic;
        this.iFollower = iFollower;
        this.iFans = iFans;
        this.sNickName = sNickName;
        this.sFaceUrl = sFaceUrl;
        this.iGender = iGender;
        this.sProvince = sProvince;
        this.sCity = sCity;
        this.sProfile = sProfile;
        this.sVerifyDesc = sVerifyDesc;
        this.eUserType = eUserType;
        this.iInvestNum = iInvestNum;
        this.lInvestTimeStamp = lInvestTimeStamp;
        this.stMember = stMember;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        ostream.writeInt32(1, iDynamic);
        ostream.writeInt32(2, iFollower);
        ostream.writeInt32(3, iFans);
        if (null != sNickName) {
            ostream.writeString(4, sNickName);
        }
        if (null != sFaceUrl) {
            ostream.writeString(5, sFaceUrl);
        }
        ostream.writeInt32(6, iGender);
        if (null != sProvince) {
            ostream.writeString(7, sProvince);
        }
        if (null != sCity) {
            ostream.writeString(8, sCity);
        }
        if (null != sProfile) {
            ostream.writeString(9, sProfile);
        }
        if (null != sVerifyDesc) {
            ostream.writeString(10, sVerifyDesc);
        }
        ostream.writeInt32(11, eUserType);
        ostream.writeInt32(12, iInvestNum);
        ostream.writeInt64(13, lInvestTimeStamp);
        if (null != stMember) {
            ostream.writeMessage(14, stMember);
        }
    }

    static BEC.DtMemberInfo VAR_TYPE_4_STMEMBER = new BEC.DtMemberInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.iDynamic = (int)istream.readInt32(1, false, this.iDynamic);
        this.iFollower = (int)istream.readInt32(2, false, this.iFollower);
        this.iFans = (int)istream.readInt32(3, false, this.iFans);
        this.sNickName = (String)istream.readString(4, false, this.sNickName);
        this.sFaceUrl = (String)istream.readString(5, false, this.sFaceUrl);
        this.iGender = (int)istream.readInt32(6, false, this.iGender);
        this.sProvince = (String)istream.readString(7, false, this.sProvince);
        this.sCity = (String)istream.readString(8, false, this.sCity);
        this.sProfile = (String)istream.readString(9, false, this.sProfile);
        this.sVerifyDesc = (String)istream.readString(10, false, this.sVerifyDesc);
        this.eUserType = (int)istream.readInt32(11, false, this.eUserType);
        this.iInvestNum = (int)istream.readInt32(12, false, this.iInvestNum);
        this.lInvestTimeStamp = (long)istream.readInt64(13, false, this.lInvestTimeStamp);
        this.stMember = (BEC.DtMemberInfo)istream.readMessage(14, false, VAR_TYPE_4_STMEMBER);
    }

}

