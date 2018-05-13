package BEC;

public final class NewsReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public String sStartId = "";

    public String sEndId = "";

    public int eGetSource = 0;

    public int eNewsType = 0;

    public boolean bGetFromDb = true;

    public String sAnnounceType = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSStartId()
    {
        return sStartId;
    }

    public void  setSStartId(String sStartId)
    {
        this.sStartId = sStartId;
    }

    public String getSEndId()
    {
        return sEndId;
    }

    public void  setSEndId(String sEndId)
    {
        this.sEndId = sEndId;
    }

    public int getEGetSource()
    {
        return eGetSource;
    }

    public void  setEGetSource(int eGetSource)
    {
        this.eGetSource = eGetSource;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public boolean getBGetFromDb()
    {
        return bGetFromDb;
    }

    public void  setBGetFromDb(boolean bGetFromDb)
    {
        this.bGetFromDb = bGetFromDb;
    }

    public String getSAnnounceType()
    {
        return sAnnounceType;
    }

    public void  setSAnnounceType(String sAnnounceType)
    {
        this.sAnnounceType = sAnnounceType;
    }

    public NewsReq()
    {
    }

    public NewsReq(UserInfo stUserInfo, String sDtSecCode, String sStartId, String sEndId, int eGetSource, int eNewsType, boolean bGetFromDb, String sAnnounceType)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.sStartId = sStartId;
        this.sEndId = sEndId;
        this.eGetSource = eGetSource;
        this.eNewsType = eNewsType;
        this.bGetFromDb = bGetFromDb;
        this.sAnnounceType = sAnnounceType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sStartId) {
            ostream.writeString(4, sStartId);
        }
        if (null != sEndId) {
            ostream.writeString(5, sEndId);
        }
        ostream.writeInt32(6, eGetSource);
        ostream.writeInt32(7, eNewsType);
        ostream.writeBoolean(8, bGetFromDb);
        if (null != sAnnounceType) {
            ostream.writeString(9, sAnnounceType);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sStartId = (String)istream.readString(4, false, this.sStartId);
        this.sEndId = (String)istream.readString(5, false, this.sEndId);
        this.eGetSource = (int)istream.readInt32(6, false, this.eGetSource);
        this.eNewsType = (int)istream.readInt32(7, false, this.eNewsType);
        this.bGetFromDb = (boolean)istream.readBoolean(8, false, this.bGetFromDb);
        this.sAnnounceType = (String)istream.readString(9, false, this.sAnnounceType);
    }

}

