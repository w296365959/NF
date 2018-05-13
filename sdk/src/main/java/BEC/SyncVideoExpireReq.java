package BEC;

public final class SyncVideoExpireReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sVideoKey = "";

    public int iDays = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public int getIDays()
    {
        return iDays;
    }

    public void  setIDays(int iDays)
    {
        this.iDays = iDays;
    }

    public SyncVideoExpireReq()
    {
    }

    public SyncVideoExpireReq(BEC.UserInfo stUserInfo, String sVideoKey, int iDays)
    {
        this.stUserInfo = stUserInfo;
        this.sVideoKey = sVideoKey;
        this.iDays = iDays;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sVideoKey) {
            ostream.writeString(1, sVideoKey);
        }
        ostream.writeInt32(2, iDays);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sVideoKey = (String)istream.readString(1, false, this.sVideoKey);
        this.iDays = (int)istream.readInt32(2, false, this.iDays);
    }

}

