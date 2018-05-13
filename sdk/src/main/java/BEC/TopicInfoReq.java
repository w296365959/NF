package BEC;

public final class TopicInfoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTopicId = 0;

    public BEC.UserInfo stUserInfo = null;

    public int eChannelType = 0;

    public int getITopicId()
    {
        return iTopicId;
    }

    public void  setITopicId(int iTopicId)
    {
        this.iTopicId = iTopicId;
    }

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEChannelType()
    {
        return eChannelType;
    }

    public void  setEChannelType(int eChannelType)
    {
        this.eChannelType = eChannelType;
    }

    public TopicInfoReq()
    {
    }

    public TopicInfoReq(int iTopicId, BEC.UserInfo stUserInfo, int eChannelType)
    {
        this.iTopicId = iTopicId;
        this.stUserInfo = stUserInfo;
        this.eChannelType = eChannelType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTopicId);
        if (null != stUserInfo) {
            ostream.writeMessage(1, stUserInfo);
        }
        ostream.writeInt32(2, eChannelType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTopicId = (int)istream.readInt32(0, false, this.iTopicId);
        this.stUserInfo = (BEC.UserInfo)istream.readMessage(1, false, VAR_TYPE_4_STUSERINFO);
        this.eChannelType = (int)istream.readInt32(2, false, this.eChannelType);
    }

}

