package BEC;

public final class GetDtActivityListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eType = BEC.DT_ACTIVITY_TYPE.T_ACTIVITY_LIST;

    public int iAdType = DT_AD_TYPE.E_AD_ACTIVITY;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public int getIAdType()
    {
        return iAdType;
    }

    public void  setIAdType(int iAdType)
    {
        this.iAdType = iAdType;
    }

    public GetDtActivityListReq()
    {
    }

    public GetDtActivityListReq(BEC.UserInfo stUserInfo, int eType, int iAdType)
    {
        this.stUserInfo = stUserInfo;
        this.eType = eType;
        this.iAdType = iAdType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eType);
        ostream.writeInt32(2, iAdType);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eType = (int)istream.readInt32(1, false, this.eType);
        this.iAdType = (int)istream.readInt32(2, false, this.iAdType);
    }

}

