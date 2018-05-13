package BEC;

public final class IntelliPickStockReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sStartId = "";

    public String sEndId = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

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

    public IntelliPickStockReq()
    {
    }

    public IntelliPickStockReq(UserInfo stUserInfo, String sStartId, String sEndId)
    {
        this.stUserInfo = stUserInfo;
        this.sStartId = sStartId;
        this.sEndId = sEndId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sStartId) {
            ostream.writeString(1, sStartId);
        }
        if (null != sEndId) {
            ostream.writeString(2, sEndId);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sStartId = (String)istream.readString(1, false, this.sStartId);
        this.sEndId = (String)istream.readString(2, false, this.sEndId);
    }

}

