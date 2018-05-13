package BEC;

public final class RecomListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int iCount = 0;

    public java.util.ArrayList<BEC.RecomItem> vKeys = null;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getICount()
    {
        return iCount;
    }

    public void  setICount(int iCount)
    {
        this.iCount = iCount;
    }

    public java.util.ArrayList<BEC.RecomItem> getVKeys()
    {
        return vKeys;
    }

    public void  setVKeys(java.util.ArrayList<BEC.RecomItem> vKeys)
    {
        this.vKeys = vKeys;
    }

    public RecomListReq()
    {
    }

    public RecomListReq(BEC.UserInfo stUserInfo, int iCount, java.util.ArrayList<BEC.RecomItem> vKeys)
    {
        this.stUserInfo = stUserInfo;
        this.iCount = iCount;
        this.vKeys = vKeys;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, iCount);
        if (null != vKeys) {
            ostream.writeList(2, vKeys);
        }
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<BEC.RecomItem> VAR_TYPE_4_VKEYS = new java.util.ArrayList<BEC.RecomItem>();
    static {
        VAR_TYPE_4_VKEYS.add(new BEC.RecomItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.iCount = (int)istream.readInt32(1, false, this.iCount);
        this.vKeys = (java.util.ArrayList<BEC.RecomItem>)istream.readList(2, false, VAR_TYPE_4_VKEYS);
    }

}

