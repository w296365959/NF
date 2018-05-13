package BEC;

public final class KLineConditionReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public String sAppId = "";

    public String sDtSecCode = "";

    public int ePeriod = 0;

    public int eUpThreshold = 0;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSAppId()
    {
        return sAppId;
    }

    public void  setSAppId(String sAppId)
    {
        this.sAppId = sAppId;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getEPeriod()
    {
        return ePeriod;
    }

    public void  setEPeriod(int ePeriod)
    {
        this.ePeriod = ePeriod;
    }

    public int getEUpThreshold()
    {
        return eUpThreshold;
    }

    public void  setEUpThreshold(int eUpThreshold)
    {
        this.eUpThreshold = eUpThreshold;
    }

    public KLineConditionReq()
    {
    }

    public KLineConditionReq(BEC.UserInfo stUserInfo, String sAppId, String sDtSecCode, int ePeriod, int eUpThreshold)
    {
        this.stUserInfo = stUserInfo;
        this.sAppId = sAppId;
        this.sDtSecCode = sDtSecCode;
        this.ePeriod = ePeriod;
        this.eUpThreshold = eUpThreshold;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sAppId) {
            ostream.writeString(1, sAppId);
        }
        if (null != sDtSecCode) {
            ostream.writeString(2, sDtSecCode);
        }
        ostream.writeInt32(3, ePeriod);
        ostream.writeInt32(4, eUpThreshold);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sAppId = (String)istream.readString(1, false, this.sAppId);
        this.sDtSecCode = (String)istream.readString(2, false, this.sDtSecCode);
        this.ePeriod = (int)istream.readInt32(3, false, this.ePeriod);
        this.eUpThreshold = (int)istream.readInt32(4, false, this.eUpThreshold);
    }

}

