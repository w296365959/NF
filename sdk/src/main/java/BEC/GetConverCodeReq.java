package BEC;

public final class GetConverCodeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int iType = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public GetConverCodeReq()
    {
    }

    public GetConverCodeReq(UserInfo stUserInfo, int iType)
    {
        this.stUserInfo = stUserInfo;
        this.iType = iType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iType = (int)istream.readInt32(1, false, this.iType);
    }

}

