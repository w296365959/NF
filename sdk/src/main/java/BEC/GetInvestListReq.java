package BEC;

public final class GetInvestListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public int eListType = BEC.E_FEED_INVEST_LIST_TYPE.E_FILT_HOT;

    public int iDirection = 0;

    public String sStartId = "";

    public boolean bGetListConf = false;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public int getEListType()
    {
        return eListType;
    }

    public void  setEListType(int eListType)
    {
        this.eListType = eListType;
    }

    public int getIDirection()
    {
        return iDirection;
    }

    public void  setIDirection(int iDirection)
    {
        this.iDirection = iDirection;
    }

    public String getSStartId()
    {
        return sStartId;
    }

    public void  setSStartId(String sStartId)
    {
        this.sStartId = sStartId;
    }

    public boolean getBGetListConf()
    {
        return bGetListConf;
    }

    public void  setBGetListConf(boolean bGetListConf)
    {
        this.bGetListConf = bGetListConf;
    }

    public GetInvestListReq()
    {
    }

    public GetInvestListReq(BEC.UserInfo stUserInfo, int eListType, int iDirection, String sStartId, boolean bGetListConf)
    {
        this.stUserInfo = stUserInfo;
        this.eListType = eListType;
        this.iDirection = iDirection;
        this.sStartId = sStartId;
        this.bGetListConf = bGetListConf;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        ostream.writeInt32(1, eListType);
        ostream.writeInt32(2, iDirection);
        if (null != sStartId) {
            ostream.writeString(3, sStartId);
        }
        ostream.writeBoolean(4, bGetListConf);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.eListType = (int)istream.readInt32(1, false, this.eListType);
        this.iDirection = (int)istream.readInt32(2, false, this.iDirection);
        this.sStartId = (String)istream.readString(3, false, this.sStartId);
        this.bGetListConf = (boolean)istream.readBoolean(4, false, this.bGetListConf);
    }

}

