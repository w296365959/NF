package BEC;

public final class IpListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<Integer> vtIPType = null;

    public int eApnType = 0;

    public String sApn = "";

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<Integer> getVtIPType()
    {
        return vtIPType;
    }

    public void  setVtIPType(java.util.ArrayList<Integer> vtIPType)
    {
        this.vtIPType = vtIPType;
    }

    public int getEApnType()
    {
        return eApnType;
    }

    public void  setEApnType(int eApnType)
    {
        this.eApnType = eApnType;
    }

    public String getSApn()
    {
        return sApn;
    }

    public void  setSApn(String sApn)
    {
        this.sApn = sApn;
    }

    public IpListReq()
    {
    }

    public IpListReq(BEC.UserInfo stUserInfo, java.util.ArrayList<Integer> vtIPType, int eApnType, String sApn)
    {
        this.stUserInfo = stUserInfo;
        this.vtIPType = vtIPType;
        this.eApnType = eApnType;
        this.sApn = sApn;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vtIPType) {
            ostream.writeList(1, vtIPType);
        }
        ostream.writeInt32(2, eApnType);
        if (null != sApn) {
            ostream.writeString(3, sApn);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<Integer> VAR_TYPE_4_VTIPTYPE = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VTIPTYPE.add(0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vtIPType = (java.util.ArrayList<Integer>)istream.readList(1, false, VAR_TYPE_4_VTIPTYPE);
        this.eApnType = (int)istream.readInt32(2, false, this.eApnType);
        this.sApn = (String)istream.readString(3, false, this.sApn);
    }

}

