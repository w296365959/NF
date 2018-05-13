package BEC;

public final class DiscoveryNewsReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStartId = "";

    public String sEndId = "";

    public BEC.UserInfo stUserInfo = null;

    public int eNewsFlag = 0;

    public int iSupportTop = 0;

    public int iSupportTopic = 0;

    public int eChannelType = 0;

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

    public int getENewsFlag()
    {
        return eNewsFlag;
    }

    public void  setENewsFlag(int eNewsFlag)
    {
        this.eNewsFlag = eNewsFlag;
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

    public DiscoveryNewsReq()
    {
    }

    public DiscoveryNewsReq(String sStartId, String sEndId, BEC.UserInfo stUserInfo, int eNewsFlag, int iSupportTop, int iSupportTopic, int eChannelType)
    {
        this.sStartId = sStartId;
        this.sEndId = sEndId;
        this.stUserInfo = stUserInfo;
        this.eNewsFlag = eNewsFlag;
        this.iSupportTop = iSupportTop;
        this.iSupportTopic = iSupportTopic;
        this.eChannelType = eChannelType;
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
        ostream.writeInt32(3, eNewsFlag);
        ostream.writeInt32(4, iSupportTop);
        ostream.writeInt32(5, iSupportTopic);
        ostream.writeInt32(6, eChannelType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sStartId = (String)istream.readString(0, false, this.sStartId);
        this.sEndId = (String)istream.readString(1, false, this.sEndId);
        this.stUserInfo = (BEC.UserInfo)istream.readMessage(2, false, VAR_TYPE_4_STUSERINFO);
        this.eNewsFlag = (int)istream.readInt32(3, false, this.eNewsFlag);
        this.iSupportTop = (int)istream.readInt32(4, false, this.iSupportTop);
        this.iSupportTopic = (int)istream.readInt32(5, false, this.iSupportTopic);
        this.eChannelType = (int)istream.readInt32(6, false, this.eChannelType);
    }

}

