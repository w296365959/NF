package BEC;

public final class HisChipDistReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public int iStartxh = 0;

    public int iWantnum = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
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

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public HisChipDistReq()
    {
    }

    public HisChipDistReq(BEC.UserInfo stUserInfo, String sDtSecCode, int iStartxh, int iWantnum)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
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
        ostream.writeInt32(2, iStartxh);
        ostream.writeInt32(3, iWantnum);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.iStartxh = (int)istream.readInt32(2, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(3, false, this.iWantnum);
    }

}

