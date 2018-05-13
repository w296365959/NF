package BEC;

public final class ConsultExReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int iAppId = 0;

    public java.util.ArrayList<String> vtDtSecCode = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIAppId()
    {
        return iAppId;
    }

    public void  setIAppId(int iAppId)
    {
        this.iAppId = iAppId;
    }

    public java.util.ArrayList<String> getVtDtSecCode()
    {
        return vtDtSecCode;
    }

    public void  setVtDtSecCode(java.util.ArrayList<String> vtDtSecCode)
    {
        this.vtDtSecCode = vtDtSecCode;
    }

    public ConsultExReq()
    {
    }

    public ConsultExReq(BEC.UserInfo stUserInfo, int iAppId, java.util.ArrayList<String> vtDtSecCode)
    {
        this.stUserInfo = stUserInfo;
        this.iAppId = iAppId;
        this.vtDtSecCode = vtDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iAppId);
        if (null != vtDtSecCode) {
            ostream.writeList(2, vtDtSecCode);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<String> VAR_TYPE_4_VTDTSECCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTDTSECCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iAppId = (int)istream.readInt32(1, false, this.iAppId);
        this.vtDtSecCode = (java.util.ArrayList<String>)istream.readList(2, false, VAR_TYPE_4_VTDTSECCODE);
    }

}

