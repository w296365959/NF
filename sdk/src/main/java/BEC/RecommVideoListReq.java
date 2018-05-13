package BEC;

public final class RecommVideoListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sVideoKey = "";

    public int eChannel = 0;

    public BEC.UserInfo stUserInfo = null;

    public int iFlag = 0;

    public java.util.ArrayList<String> vVideoKey = null;

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getIFlag()
    {
        return iFlag;
    }

    public void  setIFlag(int iFlag)
    {
        this.iFlag = iFlag;
    }

    public java.util.ArrayList<String> getVVideoKey()
    {
        return vVideoKey;
    }

    public void  setVVideoKey(java.util.ArrayList<String> vVideoKey)
    {
        this.vVideoKey = vVideoKey;
    }

    public RecommVideoListReq()
    {
    }

    public RecommVideoListReq(String sVideoKey, int eChannel, BEC.UserInfo stUserInfo, int iFlag, java.util.ArrayList<String> vVideoKey)
    {
        this.sVideoKey = sVideoKey;
        this.eChannel = eChannel;
        this.stUserInfo = stUserInfo;
        this.iFlag = iFlag;
        this.vVideoKey = vVideoKey;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sVideoKey) {
            ostream.writeString(0, sVideoKey);
        }
        ostream.writeInt32(1, eChannel);
        if (null != stUserInfo) {
            ostream.writeMessage(2, stUserInfo);
        }
        ostream.writeInt32(3, iFlag);
        if (null != vVideoKey) {
            ostream.writeList(4, vVideoKey);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<String> VAR_TYPE_4_VVIDEOKEY = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VVIDEOKEY.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sVideoKey = (String)istream.readString(0, false, this.sVideoKey);
        this.eChannel = (int)istream.readInt32(1, false, this.eChannel);
        this.stUserInfo = (BEC.UserInfo)istream.readMessage(2, false, VAR_TYPE_4_STUSERINFO);
        this.iFlag = (int)istream.readInt32(3, false, this.iFlag);
        this.vVideoKey = (java.util.ArrayList<String>)istream.readList(4, false, VAR_TYPE_4_VVIDEOKEY);
    }

}

