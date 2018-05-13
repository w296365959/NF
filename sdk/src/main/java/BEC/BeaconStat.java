package BEC;

public final class BeaconStat extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.UserInfo stUserInfo = null;

    public java.util.ArrayList<BEC.BeaconStatData> vBeaconStatData = null;

    public byte cVersion = 1;

    public BEC.UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(BEC.UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public java.util.ArrayList<BEC.BeaconStatData> getVBeaconStatData()
    {
        return vBeaconStatData;
    }

    public void  setVBeaconStatData(java.util.ArrayList<BEC.BeaconStatData> vBeaconStatData)
    {
        this.vBeaconStatData = vBeaconStatData;
    }

    public byte getCVersion()
    {
        return cVersion;
    }

    public void  setCVersion(byte cVersion)
    {
        this.cVersion = cVersion;
    }

    public BeaconStat()
    {
    }

    public BeaconStat(BEC.UserInfo stUserInfo, java.util.ArrayList<BEC.BeaconStatData> vBeaconStatData, byte cVersion)
    {
        this.stUserInfo = stUserInfo;
        this.vBeaconStatData = vBeaconStatData;
        this.cVersion = cVersion;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != vBeaconStatData) {
            ostream.writeList(1, vBeaconStatData);
        }
        ostream.writeInt8(2, cVersion);
    }

    static BEC.UserInfo VAR_TYPE_4_STUSERINFO = new BEC.UserInfo();

    static java.util.ArrayList<BEC.BeaconStatData> VAR_TYPE_4_VBEACONSTATDATA = new java.util.ArrayList<BEC.BeaconStatData>();
    static {
        VAR_TYPE_4_VBEACONSTATDATA.add(new BEC.BeaconStatData());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (BEC.UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.vBeaconStatData = (java.util.ArrayList<BEC.BeaconStatData>)istream.readList(1, false, VAR_TYPE_4_VBEACONSTATDATA);
        this.cVersion = (byte)istream.readInt8(2, false, this.cVersion);
    }

}

