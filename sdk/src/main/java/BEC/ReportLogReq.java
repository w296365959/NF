package BEC;

public final class ReportLogReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sBussName = "default";

    public int iErrCode = 0;

    public String sErrMsg = "";

    public byte [] vData = null;

    public String sFileSuffix = "";

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSBussName()
    {
        return sBussName;
    }

    public void  setSBussName(String sBussName)
    {
        this.sBussName = sBussName;
    }

    public int getIErrCode()
    {
        return iErrCode;
    }

    public void  setIErrCode(int iErrCode)
    {
        this.iErrCode = iErrCode;
    }

    public String getSErrMsg()
    {
        return sErrMsg;
    }

    public void  setSErrMsg(String sErrMsg)
    {
        this.sErrMsg = sErrMsg;
    }

    public byte [] getVData()
    {
        return vData;
    }

    public void  setVData(byte [] vData)
    {
        this.vData = vData;
    }

    public String getSFileSuffix()
    {
        return sFileSuffix;
    }

    public void  setSFileSuffix(String sFileSuffix)
    {
        this.sFileSuffix = sFileSuffix;
    }

    public ReportLogReq()
    {
    }

    public ReportLogReq(UserInfo stUserInfo, String sBussName, int iErrCode, String sErrMsg, byte [] vData, String sFileSuffix)
    {
        this.stUserInfo = stUserInfo;
        this.sBussName = sBussName;
        this.iErrCode = iErrCode;
        this.sErrMsg = sErrMsg;
        this.vData = vData;
        this.sFileSuffix = sFileSuffix;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sBussName) {
            ostream.writeString(1, sBussName);
        }
        ostream.writeInt32(2, iErrCode);
        if (null != sErrMsg) {
            ostream.writeString(3, sErrMsg);
        }
        if (null != vData) {
            ostream.writeBytes(4, vData);
        }
        if (null != sFileSuffix) {
            ostream.writeString(5, sFileSuffix);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sBussName = (String)istream.readString(1, false, this.sBussName);
        this.iErrCode = (int)istream.readInt32(2, false, this.iErrCode);
        this.sErrMsg = (String)istream.readString(3, false, this.sErrMsg);
        this.vData = (byte [])istream.readBytes(4, false, this.vData);
        this.sFileSuffix = (String)istream.readString(5, false, this.sFileSuffix);
    }

}

