package BEC;

public final class ReportPlayInfoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public BEC.VideoDesc stVideoDesc = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public BEC.VideoDesc getStVideoDesc()
    {
        return stVideoDesc;
    }

    public void  setStVideoDesc(BEC.VideoDesc stVideoDesc)
    {
        this.stVideoDesc = stVideoDesc;
    }

    public ReportPlayInfoReq()
    {
    }

    public ReportPlayInfoReq(BEC.UserInfo stUserInfo, BEC.VideoDesc stVideoDesc)
    {
        this.stUserInfo = stUserInfo;
        this.stVideoDesc = stVideoDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != stVideoDesc) {
            ostream.writeMessage(1, stVideoDesc);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static BEC.VideoDesc VAR_TYPE_4_STVIDEODESC = new BEC.VideoDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.stVideoDesc = (BEC.VideoDesc)istream.readMessage(1, false, VAR_TYPE_4_STVIDEODESC);
    }

}

