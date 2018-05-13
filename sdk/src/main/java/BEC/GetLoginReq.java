package BEC;

public final class GetLoginReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eLoginType = BEC.E_LOGIN_TYPE.E_DENGTA_LOGIN;

    public String sUnionId = "";

    public String sOpenId = "";

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getELoginType()
    {
        return eLoginType;
    }

    public void  setELoginType(int eLoginType)
    {
        this.eLoginType = eLoginType;
    }

    public String getSUnionId()
    {
        return sUnionId;
    }

    public void  setSUnionId(String sUnionId)
    {
        this.sUnionId = sUnionId;
    }

    public String getSOpenId()
    {
        return sOpenId;
    }

    public void  setSOpenId(String sOpenId)
    {
        this.sOpenId = sOpenId;
    }

    public GetLoginReq()
    {
    }

    public GetLoginReq(BEC.UserInfo stUserInfo, int eLoginType, String sUnionId, String sOpenId)
    {
        this.stUserInfo = stUserInfo;
        this.eLoginType = eLoginType;
        this.sUnionId = sUnionId;
        this.sOpenId = sOpenId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eLoginType);
        if (null != sUnionId) {
            ostream.writeString(2, sUnionId);
        }
        if (null != sOpenId) {
            ostream.writeString(3, sOpenId);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eLoginType = (int)istream.readInt32(1, false, this.eLoginType);
        this.sUnionId = (String)istream.readString(2, false, this.sUnionId);
        this.sOpenId = (String)istream.readString(3, false, this.sOpenId);
    }

}

