package BEC;

public final class TopicInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public BEC.TopicInfo stTopicInfo = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public BEC.TopicInfo getStTopicInfo()
    {
        return stTopicInfo;
    }

    public void  setStTopicInfo(BEC.TopicInfo stTopicInfo)
    {
        this.stTopicInfo = stTopicInfo;
    }

    public TopicInfoRsp()
    {
    }

    public TopicInfoRsp(int iRet, BEC.TopicInfo stTopicInfo)
    {
        this.iRet = iRet;
        this.stTopicInfo = stTopicInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != stTopicInfo) {
            ostream.writeMessage(1, stTopicInfo);
        }
    }

    static BEC.TopicInfo VAR_TYPE_4_STTOPICINFO = new BEC.TopicInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.stTopicInfo = (BEC.TopicInfo)istream.readMessage(1, false, VAR_TYPE_4_STTOPICINFO);
    }

}

