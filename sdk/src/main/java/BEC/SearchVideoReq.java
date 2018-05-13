package BEC;

public final class SearchVideoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sStartVideoKey = "";

    public BEC.VideoSearchConditions stVideoSearchConditions = null;

    public int iFlag = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSStartVideoKey()
    {
        return sStartVideoKey;
    }

    public void  setSStartVideoKey(String sStartVideoKey)
    {
        this.sStartVideoKey = sStartVideoKey;
    }

    public BEC.VideoSearchConditions getStVideoSearchConditions()
    {
        return stVideoSearchConditions;
    }

    public void  setStVideoSearchConditions(BEC.VideoSearchConditions stVideoSearchConditions)
    {
        this.stVideoSearchConditions = stVideoSearchConditions;
    }

    public int getIFlag()
    {
        return iFlag;
    }

    public void  setIFlag(int iFlag)
    {
        this.iFlag = iFlag;
    }

    public SearchVideoReq()
    {
    }

    public SearchVideoReq(BEC.UserInfo stUserInfo, String sStartVideoKey, BEC.VideoSearchConditions stVideoSearchConditions, int iFlag)
    {
        this.stUserInfo = stUserInfo;
        this.sStartVideoKey = sStartVideoKey;
        this.stVideoSearchConditions = stVideoSearchConditions;
        this.iFlag = iFlag;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sStartVideoKey) {
            ostream.writeString(1, sStartVideoKey);
        }
        if (null != stVideoSearchConditions) {
            ostream.writeMessage(2, stVideoSearchConditions);
        }
        ostream.writeInt32(3, iFlag);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.VideoSearchConditions VAR_TYPE_4_STVIDEOSEARCHCONDITIONS = new BEC.VideoSearchConditions();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sStartVideoKey = (String)istream.readString(1, false, this.sStartVideoKey);
        this.stVideoSearchConditions = (BEC.VideoSearchConditions)istream.readMessage(2, false, VAR_TYPE_4_STVIDEOSEARCHCONDITIONS);
        this.iFlag = (int)istream.readInt32(3, false, this.iFlag);
    }

}

