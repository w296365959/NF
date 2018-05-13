package BEC;

public final class GetIPOBasicInfoReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int eStatus = 0;

    public int iVersion = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEStatus()
    {
        return eStatus;
    }

    public void  setEStatus(int eStatus)
    {
        this.eStatus = eStatus;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public GetIPOBasicInfoReq()
    {
    }

    public GetIPOBasicInfoReq(UserInfo stUserInfo, int eStatus, int iVersion)
    {
        this.stUserInfo = stUserInfo;
        this.eStatus = eStatus;
        this.iVersion = iVersion;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eStatus);
        ostream.writeInt32(2, iVersion);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eStatus = (int)istream.readInt32(1, false, this.eStatus);
        this.iVersion = (int)istream.readInt32(2, false, this.iVersion);
    }

}

