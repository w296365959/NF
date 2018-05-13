package BEC;

public final class DongmiQaListReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sStartId = "";

    public String sEndId = "";

    public String sDtSecCode = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
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

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public DongmiQaListReq()
    {
    }

    public DongmiQaListReq(UserInfo stUserInfo, String sStartId, String sEndId, String sDtSecCode)
    {
        this.stUserInfo = stUserInfo;
        this.sStartId = sStartId;
        this.sEndId = sEndId;
        this.sDtSecCode = sDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sStartId) {
            ostream.writeString(1, sStartId);
        }
        if (null != sEndId) {
            ostream.writeString(2, sEndId);
        }
        if (null != sDtSecCode) {
            ostream.writeString(3, sDtSecCode);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sStartId = (String)istream.readString(1, false, this.sStartId);
        this.sEndId = (String)istream.readString(2, false, this.sEndId);
        this.sDtSecCode = (String)istream.readString(3, false, this.sDtSecCode);
    }

}

