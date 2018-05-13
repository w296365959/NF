package BEC;

public final class SKLStatisticsReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sAppId = "";

    public int eStatisticsType = 0;

    public int iWantNum = 10;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSAppId()
    {
        return sAppId;
    }

    public void  setSAppId(String sAppId)
    {
        this.sAppId = sAppId;
    }

    public int getEStatisticsType()
    {
        return eStatisticsType;
    }

    public void  setEStatisticsType(int eStatisticsType)
    {
        this.eStatisticsType = eStatisticsType;
    }

    public int getIWantNum()
    {
        return iWantNum;
    }

    public void  setIWantNum(int iWantNum)
    {
        this.iWantNum = iWantNum;
    }

    public SKLStatisticsReq()
    {
    }

    public SKLStatisticsReq(BEC.UserInfo stUserInfo, String sAppId, int eStatisticsType, int iWantNum)
    {
        this.stUserInfo = stUserInfo;
        this.sAppId = sAppId;
        this.eStatisticsType = eStatisticsType;
        this.iWantNum = iWantNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sAppId) {
            ostream.writeString(1, sAppId);
        }
        ostream.writeInt32(2, eStatisticsType);
        ostream.writeInt32(3, iWantNum);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sAppId = (String)istream.readString(1, false, this.sAppId);
        this.eStatisticsType = (int)istream.readInt32(2, false, this.eStatisticsType);
        this.iWantNum = (int)istream.readInt32(3, false, this.iWantNum);
    }

}

