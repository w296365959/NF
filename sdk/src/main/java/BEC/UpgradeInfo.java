package BEC;

public final class UpgradeInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStatus = 0;

    public String sURL = "";

    public String sText = "";

    public String sHeader = "";

    public String sFileSize = "";

    public String sReleaseTime = "";

    public String sTitle = "";

    public int iMaxVersion = 0;

    public int iMinVersion = 0;

    public String sMd5 = "";

    public int iVersion = 0;

    public String sVersionName = "";

    public int isFreqControl = 0;

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public String getSURL()
    {
        return sURL;
    }

    public void  setSURL(String sURL)
    {
        this.sURL = sURL;
    }

    public String getSText()
    {
        return sText;
    }

    public void  setSText(String sText)
    {
        this.sText = sText;
    }

    public String getSHeader()
    {
        return sHeader;
    }

    public void  setSHeader(String sHeader)
    {
        this.sHeader = sHeader;
    }

    public String getSFileSize()
    {
        return sFileSize;
    }

    public void  setSFileSize(String sFileSize)
    {
        this.sFileSize = sFileSize;
    }

    public String getSReleaseTime()
    {
        return sReleaseTime;
    }

    public void  setSReleaseTime(String sReleaseTime)
    {
        this.sReleaseTime = sReleaseTime;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public int getIMaxVersion()
    {
        return iMaxVersion;
    }

    public void  setIMaxVersion(int iMaxVersion)
    {
        this.iMaxVersion = iMaxVersion;
    }

    public int getIMinVersion()
    {
        return iMinVersion;
    }

    public void  setIMinVersion(int iMinVersion)
    {
        this.iMinVersion = iMinVersion;
    }

    public String getSMd5()
    {
        return sMd5;
    }

    public void  setSMd5(String sMd5)
    {
        this.sMd5 = sMd5;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public String getSVersionName()
    {
        return sVersionName;
    }

    public void  setSVersionName(String sVersionName)
    {
        this.sVersionName = sVersionName;
    }

    public int getIsFreqControl()
    {
        return isFreqControl;
    }

    public void  setIsFreqControl(int isFreqControl)
    {
        this.isFreqControl = isFreqControl;
    }

    public UpgradeInfo()
    {
    }

    public UpgradeInfo(int iStatus, String sURL, String sText, String sHeader, String sFileSize, String sReleaseTime, String sTitle, int iMaxVersion, int iMinVersion, String sMd5, int iVersion, String sVersionName, int isFreqControl)
    {
        this.iStatus = iStatus;
        this.sURL = sURL;
        this.sText = sText;
        this.sHeader = sHeader;
        this.sFileSize = sFileSize;
        this.sReleaseTime = sReleaseTime;
        this.sTitle = sTitle;
        this.iMaxVersion = iMaxVersion;
        this.iMinVersion = iMinVersion;
        this.sMd5 = sMd5;
        this.iVersion = iVersion;
        this.sVersionName = sVersionName;
        this.isFreqControl = isFreqControl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStatus);
        if (null != sURL) {
            ostream.writeString(1, sURL);
        }
        if (null != sText) {
            ostream.writeString(2, sText);
        }
        if (null != sHeader) {
            ostream.writeString(3, sHeader);
        }
        if (null != sFileSize) {
            ostream.writeString(4, sFileSize);
        }
        if (null != sReleaseTime) {
            ostream.writeString(5, sReleaseTime);
        }
        if (null != sTitle) {
            ostream.writeString(6, sTitle);
        }
        ostream.writeInt32(7, iMaxVersion);
        ostream.writeInt32(8, iMinVersion);
        if (null != sMd5) {
            ostream.writeString(9, sMd5);
        }
        ostream.writeInt32(10, iVersion);
        if (null != sVersionName) {
            ostream.writeString(11, sVersionName);
        }
        ostream.writeInt32(12, isFreqControl);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStatus = (int)istream.readInt32(0, false, this.iStatus);
        this.sURL = (String)istream.readString(1, false, this.sURL);
        this.sText = (String)istream.readString(2, false, this.sText);
        this.sHeader = (String)istream.readString(3, false, this.sHeader);
        this.sFileSize = (String)istream.readString(4, false, this.sFileSize);
        this.sReleaseTime = (String)istream.readString(5, false, this.sReleaseTime);
        this.sTitle = (String)istream.readString(6, false, this.sTitle);
        this.iMaxVersion = (int)istream.readInt32(7, false, this.iMaxVersion);
        this.iMinVersion = (int)istream.readInt32(8, false, this.iMinVersion);
        this.sMd5 = (String)istream.readString(9, false, this.sMd5);
        this.iVersion = (int)istream.readInt32(10, false, this.iVersion);
        this.sVersionName = (String)istream.readString(11, false, this.sVersionName);
        this.isFreqControl = (int)istream.readInt32(12, false, this.isFreqControl);
    }

}

