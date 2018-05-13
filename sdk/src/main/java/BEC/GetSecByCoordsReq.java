package BEC;

public final class GetSecByCoordsReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int iType = 0;

    public SecCoordsInfo stFCoords = null;

    public SecCoordsInfo stSCoords = null;

    public int iPageNo = 0;

    public int iFullScreen = 0;

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

    public int getIPageNo()
    {
        return iPageNo;
    }

    public void  setIPageNo(int iPageNo)
    {
        this.iPageNo = iPageNo;
    }

    public int getIFullScreen()
    {
        return iFullScreen;
    }

    public void  setIFullScreen(int iFullScreen)
    {
        this.iFullScreen = iFullScreen;
    }

    public GetSecByCoordsReq()
    {
    }

    public GetSecByCoordsReq(UserInfo stUserInfo, int iType, SecCoordsInfo stFCoords, SecCoordsInfo stSCoords, int iPageNo, int iFullScreen)
    {
        this.stUserInfo = stUserInfo;
        this.iType = iType;
        this.stFCoords = stFCoords;
        this.stSCoords = stSCoords;
        this.iPageNo = iPageNo;
        this.iFullScreen = iFullScreen;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iType);
        if (null != stFCoords) {
            ostream.writeMessage(2, stFCoords);
        }
        if (null != stSCoords) {
            ostream.writeMessage(3, stSCoords);
        }
        ostream.writeInt32(4, iPageNo);
        ostream.writeInt32(5, iFullScreen);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static SecCoordsInfo VAR_TYPE_4_STFCOORDS = new SecCoordsInfo();

    static SecCoordsInfo VAR_TYPE_4_STSCOORDS = new SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iType = (int)istream.readInt32(1, false, this.iType);
        this.stFCoords = (SecCoordsInfo)istream.readMessage(2, false, VAR_TYPE_4_STFCOORDS);
        this.stSCoords = (SecCoordsInfo)istream.readMessage(3, false, VAR_TYPE_4_STSCOORDS);
        this.iPageNo = (int)istream.readInt32(4, false, this.iPageNo);
        this.iFullScreen = (int)istream.readInt32(5, false, this.iFullScreen);
    }

}

