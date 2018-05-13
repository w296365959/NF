package BEC;

public final class FlashNewsListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStartId = "";

    public String sEndId = "";

    public BEC.UserInfo stUserInfo = null;

    public String getSStartId()
    {
        return sStartId;
    }

    public void  setSStartId(String sStartId)
    {
        this.sStartId = sStartId;
    }

    public String getSEndId()
    {
        return sEndId;
    }

    public void  setSEndId(String sEndId)
    {
        this.sEndId = sEndId;
    }

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public FlashNewsListReq()
    {
    }

    public FlashNewsListReq(String sStartId, String sEndId, BEC.UserInfo stUserInfo)
    {
        this.sStartId = sStartId;
        this.sEndId = sEndId;
        this.stUserInfo = stUserInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sStartId) {
            ostream.writeString(0, sStartId);
        }
        if (null != sEndId) {
            ostream.writeString(1, sEndId);
        }
        if (null != stUserInfo) {
            ostream.writeMessage(2, stUserInfo);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sStartId = (String)istream.readString(0, false, this.sStartId);
        this.sEndId = (String)istream.readString(1, false, this.sEndId);
        this.stUserInfo = (BEC.UserInfo)istream.readMessage(2, false, VAR_TYPE_4_STUSERINFO);
    }

}

