package BEC;

public final class DBAccInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public String sUserName = "";

    public String sPassword = "";

    public String sPasswordHash = "";

    public String sPasswordSalt = "";

    public String sPhone = "";

    public String sFaceUrl = "";

    public String sWxOpenId = "";

    public String sQQOpenId = "";

    public String sWbOpenId = "";

    public UserInfo stUserInfo = null;

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

    public String getSPassword()
    {
        return sPassword;
    }

    public void  setSPassword(String sPassword)
    {
        this.sPassword = sPassword;
    }

    public String getSPasswordHash()
    {
        return sPasswordHash;
    }

    public void  setSPasswordHash(String sPasswordHash)
    {
        this.sPasswordHash = sPasswordHash;
    }

    public String getSPasswordSalt()
    {
        return sPasswordSalt;
    }

    public void  setSPasswordSalt(String sPasswordSalt)
    {
        this.sPasswordSalt = sPasswordSalt;
    }

    public String getSPhone()
    {
        return sPhone;
    }

    public void  setSPhone(String sPhone)
    {
        this.sPhone = sPhone;
    }

    public String getSFaceUrl()
    {
        return sFaceUrl;
    }

    public void  setSFaceUrl(String sFaceUrl)
    {
        this.sFaceUrl = sFaceUrl;
    }

    public String getSWxOpenId()
    {
        return sWxOpenId;
    }

    public void  setSWxOpenId(String sWxOpenId)
    {
        this.sWxOpenId = sWxOpenId;
    }

    public String getSQQOpenId()
    {
        return sQQOpenId;
    }

    public void  setSQQOpenId(String sQQOpenId)
    {
        this.sQQOpenId = sQQOpenId;
    }

    public String getSWbOpenId()
    {
        return sWbOpenId;
    }

    public void  setSWbOpenId(String sWbOpenId)
    {
        this.sWbOpenId = sWbOpenId;
    }

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public DBAccInfo()
    {
    }

    public DBAccInfo(long iAccountId, String sUserName, String sPassword, String sPasswordHash, String sPasswordSalt, String sPhone, String sFaceUrl, String sWxOpenId, String sQQOpenId, String sWbOpenId, UserInfo stUserInfo)
    {
        this.iAccountId = iAccountId;
        this.sUserName = sUserName;
        this.sPassword = sPassword;
        this.sPasswordHash = sPasswordHash;
        this.sPasswordSalt = sPasswordSalt;
        this.sPhone = sPhone;
        this.sFaceUrl = sFaceUrl;
        this.sWxOpenId = sWxOpenId;
        this.sQQOpenId = sQQOpenId;
        this.sWbOpenId = sWbOpenId;
        this.stUserInfo = stUserInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        if (null != sUserName) {
            ostream.writeString(1, sUserName);
        }
        if (null != sPassword) {
            ostream.writeString(2, sPassword);
        }
        if (null != sPasswordHash) {
            ostream.writeString(3, sPasswordHash);
        }
        if (null != sPasswordSalt) {
            ostream.writeString(4, sPasswordSalt);
        }
        if (null != sPhone) {
            ostream.writeString(5, sPhone);
        }
        if (null != sFaceUrl) {
            ostream.writeString(6, sFaceUrl);
        }
        if (null != sWxOpenId) {
            ostream.writeString(7, sWxOpenId);
        }
        if (null != sQQOpenId) {
            ostream.writeString(8, sQQOpenId);
        }
        if (null != sWbOpenId) {
            ostream.writeString(9, sWbOpenId);
        }
        if (null != stUserInfo) {
            ostream.writeMessage(10, stUserInfo);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.sUserName = (String)istream.readString(1, false, this.sUserName);
        this.sPassword = (String)istream.readString(2, false, this.sPassword);
        this.sPasswordHash = (String)istream.readString(3, false, this.sPasswordHash);
        this.sPasswordSalt = (String)istream.readString(4, false, this.sPasswordSalt);
        this.sPhone = (String)istream.readString(5, false, this.sPhone);
        this.sFaceUrl = (String)istream.readString(6, false, this.sFaceUrl);
        this.sWxOpenId = (String)istream.readString(7, false, this.sWxOpenId);
        this.sQQOpenId = (String)istream.readString(8, false, this.sQQOpenId);
        this.sWbOpenId = (String)istream.readString(9, false, this.sWbOpenId);
        this.stUserInfo = (UserInfo)istream.readMessage(10, false, VAR_TYPE_4_STUSERINFO);
    }

}

