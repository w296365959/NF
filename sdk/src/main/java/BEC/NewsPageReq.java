package BEC;

public final class NewsPageReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public int eNewsType = 0;

    public String sPageNo = "";

    public int iSupportTop = 0;

    public int iSupportTopic = 0;

    public int eChannelType = 0;

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

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public String getSPageNo()
    {
        return sPageNo;
    }

    public void  setSPageNo(String sPageNo)
    {
        this.sPageNo = sPageNo;
    }

    public int getISupportTop()
    {
        return iSupportTop;
    }

    public void  setISupportTop(int iSupportTop)
    {
        this.iSupportTop = iSupportTop;
    }

    public int getISupportTopic()
    {
        return iSupportTopic;
    }

    public void  setISupportTopic(int iSupportTopic)
    {
        this.iSupportTopic = iSupportTopic;
    }

    public int getEChannelType()
    {
        return eChannelType;
    }

    public void  setEChannelType(int eChannelType)
    {
        this.eChannelType = eChannelType;
    }

    public NewsPageReq()
    {
    }

    public NewsPageReq(BEC.UserInfo stUserInfo, String sDtSecCode, int eNewsType, String sPageNo, int iSupportTop, int iSupportTopic, int eChannelType)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.eNewsType = eNewsType;
        this.sPageNo = sPageNo;
        this.iSupportTop = iSupportTop;
        this.iSupportTopic = iSupportTopic;
        this.eChannelType = eChannelType;
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
        ostream.writeInt32(2, eNewsType);
        if (null != sPageNo) {
            ostream.writeString(3, sPageNo);
        }
        ostream.writeInt32(4, iSupportTop);
        ostream.writeInt32(5, iSupportTopic);
        ostream.writeInt32(6, eChannelType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.eNewsType = (int)istream.readInt32(2, false, this.eNewsType);
        this.sPageNo = (String)istream.readString(3, false, this.sPageNo);
        this.iSupportTop = (int)istream.readInt32(4, false, this.iSupportTop);
        this.iSupportTopic = (int)istream.readInt32(5, false, this.iSupportTopic);
        this.eChannelType = (int)istream.readInt32(6, false, this.eChannelType);
    }

}

