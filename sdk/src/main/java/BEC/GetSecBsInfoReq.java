package BEC;

public final class GetSecBsInfoReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public String sDate = "";

    public int iSize = 0;

    public int iFrom = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getISize()
    {
        return iSize;
    }

    public void  setISize(int iSize)
    {
        this.iSize = iSize;
    }

    public int getIFrom()
    {
        return iFrom;
    }

    public void  setIFrom(int iFrom)
    {
        this.iFrom = iFrom;
    }

    public GetSecBsInfoReq()
    {
    }

    public GetSecBsInfoReq(UserInfo stUserInfo, String sDtSecCode, String sDate, int iSize, int iFrom)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.sDate = sDate;
        this.iSize = iSize;
        this.iFrom = iFrom;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sDate) {
            ostream.writeString(2, sDate);
        }
        ostream.writeInt32(3, iSize);
        ostream.writeInt32(4, iFrom);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sDate = (String)istream.readString(2, false, this.sDate);
        this.iSize = (int)istream.readInt32(3, false, this.iSize);
        this.iFrom = (int)istream.readInt32(4, false, this.iFrom);
    }

}

