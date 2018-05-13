package BEC;

public final class ConcBaseInfoReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<String> vDtSecCode = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<String> getVDtSecCode()
    {
        return vDtSecCode;
    }

    public void  setVDtSecCode(java.util.ArrayList<String> vDtSecCode)
    {
        this.vDtSecCode = vDtSecCode;
    }

    public ConcBaseInfoReq()
    {
    }

    public ConcBaseInfoReq(BEC.UserInfo stUserInfo, java.util.ArrayList<String> vDtSecCode)
    {
        this.stUserInfo = stUserInfo;
        this.vDtSecCode = vDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vDtSecCode) {
            ostream.writeList(1, vDtSecCode);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<String> VAR_TYPE_4_VDTSECCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDTSECCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vDtSecCode = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VDTSECCODE);
    }

}

