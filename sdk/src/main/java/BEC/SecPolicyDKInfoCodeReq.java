package BEC;

public final class SecPolicyDKInfoCodeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sSecCode = "";

    public int iStart = 0;

    public int iSize = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSSecCode()
    {
        return sSecCode;
    }

    public void  setSSecCode(String sSecCode)
    {
        this.sSecCode = sSecCode;
    }

    public int getIStart()
    {
        return iStart;
    }

    public void  setIStart(int iStart)
    {
        this.iStart = iStart;
    }

    public int getISize()
    {
        return iSize;
    }

    public void  setISize(int iSize)
    {
        this.iSize = iSize;
    }

    public SecPolicyDKInfoCodeReq()
    {
    }

    public SecPolicyDKInfoCodeReq(UserInfo stUserInfo, String sSecCode, int iStart, int iSize)
    {
        this.stUserInfo = stUserInfo;
        this.sSecCode = sSecCode;
        this.iStart = iStart;
        this.iSize = iSize;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sSecCode) {
            ostream.writeString(1, sSecCode);
        }
        ostream.writeInt32(2, iStart);
        ostream.writeInt32(3, iSize);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sSecCode = (String)istream.readString(1, false, this.sSecCode);
        this.iStart = (int)istream.readInt32(2, false, this.iStart);
        this.iSize = (int)istream.readInt32(3, false, this.iSize);
    }

}

