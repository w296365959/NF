package BEC;

public final class UpgradeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sClientMd5 = "";

    public int iCheckFreq = 0;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSClientMd5()
    {
        return sClientMd5;
    }

    public void  setSClientMd5(String sClientMd5)
    {
        this.sClientMd5 = sClientMd5;
    }

    public int getICheckFreq()
    {
        return iCheckFreq;
    }

    public void  setICheckFreq(int iCheckFreq)
    {
        this.iCheckFreq = iCheckFreq;
    }

    public UpgradeReq()
    {
    }

    public UpgradeReq(UserInfo stUserInfo, String sClientMd5, int iCheckFreq)
    {
        this.stUserInfo = stUserInfo;
        this.sClientMd5 = sClientMd5;
        this.iCheckFreq = iCheckFreq;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sClientMd5) {
            ostream.writeString(1, sClientMd5);
        }
        ostream.writeInt32(2, iCheckFreq);
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sClientMd5 = (String)istream.readString(1, false, this.sClientMd5);
        this.iCheckFreq = (int)istream.readInt32(2, false, this.iCheckFreq);
    }

}

