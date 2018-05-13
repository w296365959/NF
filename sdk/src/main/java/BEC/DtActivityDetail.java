package BEC;

public final class DtActivityDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String sDesc = "";

    public String sPicUrl = "";

    public String sUrl = "";

    public long lOnlineTime = 0;

    public long lOfflineTime = 0;

    public int iStatus = 0;

    public int iPriority = 0;

    public String sPlat = "all";

    public int iType = 0;

    public int iSceneType = 0;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public String getSPicUrl()
    {
        return sPicUrl;
    }

    public void  setSPicUrl(String sPicUrl)
    {
        this.sPicUrl = sPicUrl;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public long getLOnlineTime()
    {
        return lOnlineTime;
    }

    public void  setLOnlineTime(long lOnlineTime)
    {
        this.lOnlineTime = lOnlineTime;
    }

    public long getLOfflineTime()
    {
        return lOfflineTime;
    }

    public void  setLOfflineTime(long lOfflineTime)
    {
        this.lOfflineTime = lOfflineTime;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public int getIPriority()
    {
        return iPriority;
    }

    public void  setIPriority(int iPriority)
    {
        this.iPriority = iPriority;
    }

    public String getSPlat()
    {
        return sPlat;
    }

    public void  setSPlat(String sPlat)
    {
        this.sPlat = sPlat;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public int getISceneType()
    {
        return iSceneType;
    }

    public void  setISceneType(int iSceneType)
    {
        this.iSceneType = iSceneType;
    }

    public DtActivityDetail()
    {
    }

    public DtActivityDetail(String sName, String sDesc, String sPicUrl, String sUrl, long lOnlineTime, long lOfflineTime, int iStatus, int iPriority, String sPlat, int iType, int iSceneType)
    {
        this.sName = sName;
        this.sDesc = sDesc;
        this.sPicUrl = sPicUrl;
        this.sUrl = sUrl;
        this.lOnlineTime = lOnlineTime;
        this.lOfflineTime = lOfflineTime;
        this.iStatus = iStatus;
        this.iPriority = iPriority;
        this.sPlat = sPlat;
        this.iType = iType;
        this.iSceneType = iSceneType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != sDesc) {
            ostream.writeString(1, sDesc);
        }
        if (null != sPicUrl) {
            ostream.writeString(2, sPicUrl);
        }
        if (null != sUrl) {
            ostream.writeString(3, sUrl);
        }
        ostream.writeInt64(4, lOnlineTime);
        ostream.writeInt64(5, lOfflineTime);
        ostream.writeInt32(6, iStatus);
        ostream.writeInt32(7, iPriority);
        if (null != sPlat) {
            ostream.writeString(8, sPlat);
        }
        ostream.writeInt32(9, iType);
        ostream.writeInt32(10, iSceneType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sDesc = (String)istream.readString(1, false, this.sDesc);
        this.sPicUrl = (String)istream.readString(2, false, this.sPicUrl);
        this.sUrl = (String)istream.readString(3, false, this.sUrl);
        this.lOnlineTime = (long)istream.readInt64(4, false, this.lOnlineTime);
        this.lOfflineTime = (long)istream.readInt64(5, false, this.lOfflineTime);
        this.iStatus = (int)istream.readInt32(6, false, this.iStatus);
        this.iPriority = (int)istream.readInt32(7, false, this.iPriority);
        this.sPlat = (String)istream.readString(8, false, this.sPlat);
        this.iType = (int)istream.readInt32(9, false, this.iType);
        this.iSceneType = (int)istream.readInt32(10, false, this.iSceneType);
    }

}

