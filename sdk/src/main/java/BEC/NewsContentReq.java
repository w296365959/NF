package BEC;

public final class NewsContentReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sNewsID = "";

    public int eNewsType = 0;

    public int eSecType = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSNewsID()
    {
        return sNewsID;
    }

    public void  setSNewsID(String sNewsID)
    {
        this.sNewsID = sNewsID;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public int getESecType()
    {
        return eSecType;
    }

    public void  setESecType(int eSecType)
    {
        this.eSecType = eSecType;
    }

    public NewsContentReq()
    {
    }

    public NewsContentReq(BEC.UserInfo stUserInfo, String sNewsID, int eNewsType, int eSecType)
    {
        this.stUserInfo = stUserInfo;
        this.sNewsID = sNewsID;
        this.eNewsType = eNewsType;
        this.eSecType = eSecType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sNewsID) {
            ostream.writeString(1, sNewsID);
        }
        ostream.writeInt32(2, eNewsType);
        ostream.writeInt32(3, eSecType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sNewsID = (String)istream.readString(1, false, this.sNewsID);
        this.eNewsType = (int)istream.readInt32(2, false, this.eNewsType);
        this.eSecType = (int)istream.readInt32(3, false, this.eSecType);
    }

}

