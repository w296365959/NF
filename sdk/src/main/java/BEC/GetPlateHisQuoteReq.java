package BEC;

public final class GetPlateHisQuoteReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public int iPlateType = 0;

    public int iCycleType = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIPlateType()
    {
        return iPlateType;
    }

    public void  setIPlateType(int iPlateType)
    {
        this.iPlateType = iPlateType;
    }

    public int getICycleType()
    {
        return iCycleType;
    }

    public void  setICycleType(int iCycleType)
    {
        this.iCycleType = iCycleType;
    }

    public GetPlateHisQuoteReq()
    {
    }

    public GetPlateHisQuoteReq(UserInfo stUserInfo, int iPlateType, int iCycleType)
    {
        this.stUserInfo = stUserInfo;
        this.iPlateType = iPlateType;
        this.iCycleType = iCycleType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iPlateType);
        ostream.writeInt32(2, iCycleType);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iPlateType = (int)istream.readInt32(1, false, this.iPlateType);
        this.iCycleType = (int)istream.readInt32(2, false, this.iCycleType);
    }

}

