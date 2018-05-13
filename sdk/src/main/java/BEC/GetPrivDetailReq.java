package BEC;

public final class GetPrivDetailReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public java.util.ArrayList<String> vDtCode = null;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<String> getVDtCode()
    {
        return vDtCode;
    }

    public void  setVDtCode(java.util.ArrayList<String> vDtCode)
    {
        this.vDtCode = vDtCode;
    }

    public GetPrivDetailReq()
    {
    }

    public GetPrivDetailReq(UserInfo stUserInfo, java.util.ArrayList<String> vDtCode)
    {
        this.stUserInfo = stUserInfo;
        this.vDtCode = vDtCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vDtCode) {
            ostream.writeList(1, vDtCode);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();

    static java.util.ArrayList<String> VAR_TYPE_4_VDTCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDTCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vDtCode = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VDTCODE);
    }

}

