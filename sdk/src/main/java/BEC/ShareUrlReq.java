package BEC;

public final class ShareUrlReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sDtSecCode = "";

    public java.util.ArrayList<Integer> vtShareType = null;

    public String sSecName = "";

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

    public java.util.ArrayList<Integer> getVtShareType()
    {
        return vtShareType;
    }

    public void  setVtShareType(java.util.ArrayList<Integer> vtShareType)
    {
        this.vtShareType = vtShareType;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public ShareUrlReq()
    {
    }

    public ShareUrlReq(BEC.UserInfo stUserInfo, String sDtSecCode, java.util.ArrayList<Integer> vtShareType, String sSecName)
    {
        this.stUserInfo = stUserInfo;
        this.sDtSecCode = sDtSecCode;
        this.vtShareType = vtShareType;
        this.sSecName = sSecName;
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
        if (null != vtShareType) {
            ostream.writeList(2, vtShareType);
        }
        if (null != sSecName) {
            ostream.writeString(3, sSecName);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<Integer> VAR_TYPE_4_VTSHARETYPE = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VTSHARETYPE.add(0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.vtShareType = (java.util.ArrayList<Integer>)istream.readList(2, false, VAR_TYPE_4_VTSHARETYPE);
        this.sSecName = (String)istream.readString(3, false, this.sSecName);
    }

}

