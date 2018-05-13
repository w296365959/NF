package BEC;

public final class GetInvitePassReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sReqPass = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSReqPass()
    {
        return sReqPass;
    }

    public void  setSReqPass(String sReqPass)
    {
        this.sReqPass = sReqPass;
    }

    public GetInvitePassReq()
    {
    }

    public GetInvitePassReq(UserInfo stUserInfo, String sReqPass)
    {
        this.stUserInfo = stUserInfo;
        this.sReqPass = sReqPass;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sReqPass) {
            ostream.writeString(1, sReqPass);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sReqPass = (String)istream.readString(1, false, this.sReqPass);
    }

}

