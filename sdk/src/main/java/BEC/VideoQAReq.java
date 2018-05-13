package BEC;

public final class VideoQAReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sTeacherId = "";

    public String sStartQuestionId = "";

    public String sEndQuestionId = "";

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSTeacherId()
    {
        return sTeacherId;
    }

    public void  setSTeacherId(String sTeacherId)
    {
        this.sTeacherId = sTeacherId;
    }

    public String getSStartQuestionId()
    {
        return sStartQuestionId;
    }

    public void  setSStartQuestionId(String sStartQuestionId)
    {
        this.sStartQuestionId = sStartQuestionId;
    }

    public String getSEndQuestionId()
    {
        return sEndQuestionId;
    }

    public void  setSEndQuestionId(String sEndQuestionId)
    {
        this.sEndQuestionId = sEndQuestionId;
    }

    public VideoQAReq()
    {
    }

    public VideoQAReq(BEC.UserInfo stUserInfo, String sTeacherId, String sStartQuestionId, String sEndQuestionId)
    {
        this.stUserInfo = stUserInfo;
        this.sTeacherId = sTeacherId;
        this.sStartQuestionId = sStartQuestionId;
        this.sEndQuestionId = sEndQuestionId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sTeacherId) {
            ostream.writeString(1, sTeacherId);
        }
        if (null != sStartQuestionId) {
            ostream.writeString(2, sStartQuestionId);
        }
        if (null != sEndQuestionId) {
            ostream.writeString(3, sEndQuestionId);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sTeacherId = (String)istream.readString(1, false, this.sTeacherId);
        this.sStartQuestionId = (String)istream.readString(2, false, this.sStartQuestionId);
        this.sEndQuestionId = (String)istream.readString(3, false, this.sEndQuestionId);
    }

}

