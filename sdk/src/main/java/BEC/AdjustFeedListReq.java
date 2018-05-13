package BEC;

public final class AdjustFeedListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public int eSetFeedListType = 0;

    public int eFeedType = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getESetFeedListType()
    {
        return eSetFeedListType;
    }

    public void  setESetFeedListType(int eSetFeedListType)
    {
        this.eSetFeedListType = eSetFeedListType;
    }

    public int getEFeedType()
    {
        return eFeedType;
    }

    public void  setEFeedType(int eFeedType)
    {
        this.eFeedType = eFeedType;
    }

    public AdjustFeedListReq()
    {
    }

    public AdjustFeedListReq(BEC.UserInfo stUserInfo, String sDtSecCode, int eSetFeedListType, int eFeedType)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.eSetFeedListType = eSetFeedListType;
        this.eFeedType = eFeedType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        ostream.writeInt32(2, eSetFeedListType);
        ostream.writeInt32(3, eFeedType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.eSetFeedListType = (int)istream.readInt32(2, false, this.eSetFeedListType);
        this.eFeedType = (int)istream.readInt32(3, false, this.eFeedType);
    }

}

