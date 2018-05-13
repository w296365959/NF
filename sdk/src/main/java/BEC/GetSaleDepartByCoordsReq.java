package BEC;

public final class GetSaleDepartByCoordsReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public SecCoordsInfo stFCoords = null;

    public SecCoordsInfo stSCoords = null;

    public String sSaleDepName = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public SecCoordsInfo getStFCoords()
    {
        return stFCoords;
    }

    public void  setStFCoords(SecCoordsInfo stFCoords)
    {
        this.stFCoords = stFCoords;
    }

    public SecCoordsInfo getStSCoords()
    {
        return stSCoords;
    }

    public void  setStSCoords(SecCoordsInfo stSCoords)
    {
        this.stSCoords = stSCoords;
    }

    public String getSSaleDepName()
    {
        return sSaleDepName;
    }

    public void  setSSaleDepName(String sSaleDepName)
    {
        this.sSaleDepName = sSaleDepName;
    }

    public GetSaleDepartByCoordsReq()
    {
    }

    public GetSaleDepartByCoordsReq(UserInfo stUserInfo, SecCoordsInfo stFCoords, SecCoordsInfo stSCoords, String sSaleDepName)
    {
        this.stUserInfo = stUserInfo;
        this.stFCoords = stFCoords;
        this.stSCoords = stSCoords;
        this.sSaleDepName = sSaleDepName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stFCoords) {
            ostream.writeMessage(1, stFCoords);
        }
        if (null != stSCoords) {
            ostream.writeMessage(2, stSCoords);
        }
        if (null != sSaleDepName) {
            ostream.writeString(3, sSaleDepName);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static SecCoordsInfo VAR_TYPE_4_STFCOORDS = new SecCoordsInfo();

    static SecCoordsInfo VAR_TYPE_4_STSCOORDS = new SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stFCoords = (SecCoordsInfo)istream.readMessage(1, false, VAR_TYPE_4_STFCOORDS);
        this.stSCoords = (SecCoordsInfo)istream.readMessage(2, false, VAR_TYPE_4_STSCOORDS);
        this.sSaleDepName = (String)istream.readString(3, false, this.sSaleDepName);
    }

}

