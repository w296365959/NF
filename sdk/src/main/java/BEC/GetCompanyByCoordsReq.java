package BEC;

public final class GetCompanyByCoordsReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public SecCoordsInfo stFCoords = null;

    public SecCoordsInfo stSCoords = null;

    public int iRegionLevel = E_REGION_LEVEL_TYPE.E_REGION_LEVEL_PROVINCE;

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

    public int getIRegionLevel()
    {
        return iRegionLevel;
    }

    public void  setIRegionLevel(int iRegionLevel)
    {
        this.iRegionLevel = iRegionLevel;
    }

    public GetCompanyByCoordsReq()
    {
    }

    public GetCompanyByCoordsReq(UserInfo stUserInfo, SecCoordsInfo stFCoords, SecCoordsInfo stSCoords, int iRegionLevel)
    {
        this.stUserInfo = stUserInfo;
        this.stFCoords = stFCoords;
        this.stSCoords = stSCoords;
        this.iRegionLevel = iRegionLevel;
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
        ostream.writeInt32(3, iRegionLevel);
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
        this.iRegionLevel = (int)istream.readInt32(3, false, this.iRegionLevel);
    }

}

