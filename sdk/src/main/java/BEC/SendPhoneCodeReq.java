package BEC;

public final class SendPhoneCodeReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sPhoneNum = "";

    public short iStatus = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSPhoneNum()
    {
        return sPhoneNum;
    }

    public void  setSPhoneNum(String sPhoneNum)
    {
        this.sPhoneNum = sPhoneNum;
    }

    public short getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(short iStatus)
    {
        this.iStatus = iStatus;
    }

    public SendPhoneCodeReq()
    {
    }

    public SendPhoneCodeReq(BEC.UserInfo stUserInfo, String sPhoneNum, short iStatus)
    {
        this.stUserInfo = stUserInfo;
        this.sPhoneNum = sPhoneNum;
        this.iStatus = iStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sPhoneNum) {
            ostream.writeString(1, sPhoneNum);
        }
        ostream.writeInt16(2, iStatus);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sPhoneNum = (String)istream.readString(1, false, this.sPhoneNum);
        this.iStatus = (short)istream.readInt16(2, false, this.iStatus);
    }

}

