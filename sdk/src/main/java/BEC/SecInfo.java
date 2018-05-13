package BEC;

public final class SecInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sCHNShortName = "";

    public BEC.PlateInfo stPlateInfo = null;

    public java.util.ArrayList<BEC.ConcInfo> vConcInfo = null;

    public java.util.Map<Integer, String> mSecAttr = null;

    public java.util.ArrayList<BEC.SecBaseInfo> vRelateSecInfo = null;

    public java.util.ArrayList<BEC.SecStatusInfo> vStatusInfo = null;

    public java.util.ArrayList<BEC.PlateInfo> vPlateInfo = null;

    public BEC.SecCoordsInfo stSecCoordsInfo = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSCHNShortName()
    {
        return sCHNShortName;
    }

    public void  setSCHNShortName(String sCHNShortName)
    {
        this.sCHNShortName = sCHNShortName;
    }

    public BEC.PlateInfo getStPlateInfo()
    {
        return stPlateInfo;
    }

    public void  setStPlateInfo(BEC.PlateInfo stPlateInfo)
    {
        this.stPlateInfo = stPlateInfo;
    }

    public java.util.ArrayList<BEC.ConcInfo> getVConcInfo()
    {
        return vConcInfo;
    }

    public void  setVConcInfo(java.util.ArrayList<BEC.ConcInfo> vConcInfo)
    {
        this.vConcInfo = vConcInfo;
    }

    public java.util.Map<Integer, String> getMSecAttr()
    {
        return mSecAttr;
    }

    public void  setMSecAttr(java.util.Map<Integer, String> mSecAttr)
    {
        this.mSecAttr = mSecAttr;
    }

    public java.util.ArrayList<BEC.SecBaseInfo> getVRelateSecInfo()
    {
        return vRelateSecInfo;
    }

    public void  setVRelateSecInfo(java.util.ArrayList<BEC.SecBaseInfo> vRelateSecInfo)
    {
        this.vRelateSecInfo = vRelateSecInfo;
    }

    public java.util.ArrayList<BEC.SecStatusInfo> getVStatusInfo()
    {
        return vStatusInfo;
    }

    public void  setVStatusInfo(java.util.ArrayList<BEC.SecStatusInfo> vStatusInfo)
    {
        this.vStatusInfo = vStatusInfo;
    }

    public java.util.ArrayList<BEC.PlateInfo> getVPlateInfo()
    {
        return vPlateInfo;
    }

    public void  setVPlateInfo(java.util.ArrayList<BEC.PlateInfo> vPlateInfo)
    {
        this.vPlateInfo = vPlateInfo;
    }

    public BEC.SecCoordsInfo getStSecCoordsInfo()
    {
        return stSecCoordsInfo;
    }

    public void  setStSecCoordsInfo(BEC.SecCoordsInfo stSecCoordsInfo)
    {
        this.stSecCoordsInfo = stSecCoordsInfo;
    }

    public SecInfo()
    {
    }

    public SecInfo(String sDtSecCode, String sCHNShortName, BEC.PlateInfo stPlateInfo, java.util.ArrayList<BEC.ConcInfo> vConcInfo, java.util.Map<Integer, String> mSecAttr, java.util.ArrayList<BEC.SecBaseInfo> vRelateSecInfo, java.util.ArrayList<BEC.SecStatusInfo> vStatusInfo, java.util.ArrayList<BEC.PlateInfo> vPlateInfo, BEC.SecCoordsInfo stSecCoordsInfo)
    {
        this.sDtSecCode = sDtSecCode;
        this.sCHNShortName = sCHNShortName;
        this.stPlateInfo = stPlateInfo;
        this.vConcInfo = vConcInfo;
        this.mSecAttr = mSecAttr;
        this.vRelateSecInfo = vRelateSecInfo;
        this.vStatusInfo = vStatusInfo;
        this.vPlateInfo = vPlateInfo;
        this.stSecCoordsInfo = stSecCoordsInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sCHNShortName) {
            ostream.writeString(1, sCHNShortName);
        }
        if (null != stPlateInfo) {
            ostream.writeMessage(2, stPlateInfo);
        }
        if (null != vConcInfo) {
            ostream.writeList(3, vConcInfo);
        }
        if (null != mSecAttr) {
            ostream.writeMap(4, mSecAttr);
        }
        if (null != vRelateSecInfo) {
            ostream.writeList(5, vRelateSecInfo);
        }
        if (null != vStatusInfo) {
            ostream.writeList(6, vStatusInfo);
        }
        if (null != vPlateInfo) {
            ostream.writeList(7, vPlateInfo);
        }
        if (null != stSecCoordsInfo) {
            ostream.writeMessage(8, stSecCoordsInfo);
        }
    }

    static BEC.PlateInfo VAR_TYPE_4_STPLATEINFO = new BEC.PlateInfo();

    static java.util.ArrayList<BEC.ConcInfo> VAR_TYPE_4_VCONCINFO = new java.util.ArrayList<BEC.ConcInfo>();
    static {
        VAR_TYPE_4_VCONCINFO.add(new BEC.ConcInfo());
    }

    static java.util.Map<Integer, String> VAR_TYPE_4_MSECATTR = new java.util.HashMap<Integer, String>();
    static {
        VAR_TYPE_4_MSECATTR.put(0, "");
    }

    static java.util.ArrayList<BEC.SecBaseInfo> VAR_TYPE_4_VRELATESECINFO = new java.util.ArrayList<BEC.SecBaseInfo>();
    static {
        VAR_TYPE_4_VRELATESECINFO.add(new BEC.SecBaseInfo());
    }

    static java.util.ArrayList<BEC.SecStatusInfo> VAR_TYPE_4_VSTATUSINFO = new java.util.ArrayList<BEC.SecStatusInfo>();
    static {
        VAR_TYPE_4_VSTATUSINFO.add(new BEC.SecStatusInfo());
    }

    static java.util.ArrayList<BEC.PlateInfo> VAR_TYPE_4_VPLATEINFO = new java.util.ArrayList<BEC.PlateInfo>();
    static {
        VAR_TYPE_4_VPLATEINFO.add(new BEC.PlateInfo());
    }

    static BEC.SecCoordsInfo VAR_TYPE_4_STSECCOORDSINFO = new BEC.SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sCHNShortName = (String)istream.readString(1, false, this.sCHNShortName);
        this.stPlateInfo = (BEC.PlateInfo)istream.readMessage(2, false, VAR_TYPE_4_STPLATEINFO);
        this.vConcInfo = (java.util.ArrayList<BEC.ConcInfo>)istream.readList(3, false, VAR_TYPE_4_VCONCINFO);
        this.mSecAttr = (java.util.Map<Integer, String>)istream.readMap(4, false, VAR_TYPE_4_MSECATTR);
        this.vRelateSecInfo = (java.util.ArrayList<BEC.SecBaseInfo>)istream.readList(5, false, VAR_TYPE_4_VRELATESECINFO);
        this.vStatusInfo = (java.util.ArrayList<BEC.SecStatusInfo>)istream.readList(6, false, VAR_TYPE_4_VSTATUSINFO);
        this.vPlateInfo = (java.util.ArrayList<BEC.PlateInfo>)istream.readList(7, false, VAR_TYPE_4_VPLATEINFO);
        this.stSecCoordsInfo = (BEC.SecCoordsInfo)istream.readMessage(8, false, VAR_TYPE_4_STSECCOORDSINFO);
    }

}

