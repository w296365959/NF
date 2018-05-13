package BEC;

public final class UnlockByConverCodeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sConversionCode = "";

    public String sDtTicket = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSConversionCode()
    {
        return sConversionCode;
    }

    public void  setSConversionCode(String sConversionCode)
    {
        this.sConversionCode = sConversionCode;
    }

    public String getSDtTicket()
    {
        return sDtTicket;
    }

    public void  setSDtTicket(String sDtTicket)
    {
        this.sDtTicket = sDtTicket;
    }

    public UnlockByConverCodeReq()
    {
    }

    public UnlockByConverCodeReq(UserInfo stUserInfo, String sConversionCode, String sDtTicket)
    {
        this.stUserInfo = stUserInfo;
        this.sConversionCode = sConversionCode;
        this.sDtTicket = sDtTicket;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sConversionCode) {
            ostream.writeString(1, sConversionCode);
        }
        if (null != sDtTicket) {
            ostream.writeString(2, sDtTicket);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sConversionCode = (String)istream.readString(1, false, this.sConversionCode);
        this.sDtTicket = (String)istream.readString(2, false, this.sDtTicket);
    }

}

