package BEC;

public final class ReportPlayReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sVideoKey = "";

    public String sPlayChannel = "";

    public String sClientType = "";

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public String getSPlayChannel()
    {
        return sPlayChannel;
    }

    public void  setSPlayChannel(String sPlayChannel)
    {
        this.sPlayChannel = sPlayChannel;
    }

    public String getSClientType()
    {
        return sClientType;
    }

    public void  setSClientType(String sClientType)
    {
        this.sClientType = sClientType;
    }

    public ReportPlayReq()
    {
    }

    public ReportPlayReq(BEC.UserInfo stUserInfo, String sVideoKey, String sPlayChannel, String sClientType)
    {
        this.stUserInfo = stUserInfo;
        this.sVideoKey = sVideoKey;
        this.sPlayChannel = sPlayChannel;
        this.sClientType = sClientType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sVideoKey) {
            ostream.writeString(1, sVideoKey);
        }
        if (null != sPlayChannel) {
            ostream.writeString(2, sPlayChannel);
        }
        if (null != sClientType) {
            ostream.writeString(3, sClientType);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sVideoKey = (String)istream.readString(1, false, this.sVideoKey);
        this.sPlayChannel = (String)istream.readString(2, false, this.sPlayChannel);
        this.sClientType = (String)istream.readString(3, false, this.sClientType);
    }

}

