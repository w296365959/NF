package BEC;

public final class SaveFavorIndReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sIndiData = "";

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSIndiData()
    {
        return sIndiData;
    }

    public void  setSIndiData(String sIndiData)
    {
        this.sIndiData = sIndiData;
    }

    public SaveFavorIndReq()
    {
    }

    public SaveFavorIndReq(BEC.UserInfo stUserInfo, String sIndiData)
    {
        this.stUserInfo = stUserInfo;
        this.sIndiData = sIndiData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeMessage(0, stUserInfo);
        if (null != sIndiData) {
            ostream.writeString(1, sIndiData);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, true, VAR_TYPE_4_STUSERINFO);
        this.sIndiData = (String)istream.readString(1, false, this.sIndiData);
    }

}

