package BEC;

public final class SingalDetailReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sTypeName = "";

    public int iID = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSTypeName()
    {
        return sTypeName;
    }

    public void  setSTypeName(String sTypeName)
    {
        this.sTypeName = sTypeName;
    }

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
    }

    public SingalDetailReq()
    {
    }

    public SingalDetailReq(UserInfo stUserInfo, String sTypeName, int iID)
    {
        this.stUserInfo = stUserInfo;
        this.sTypeName = sTypeName;
        this.iID = iID;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sTypeName) {
            ostream.writeString(1, sTypeName);
        }
        ostream.writeInt32(2, iID);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sTypeName = (String)istream.readString(1, false, this.sTypeName);
        this.iID = (int)istream.readInt32(2, false, this.iID);
    }

}

