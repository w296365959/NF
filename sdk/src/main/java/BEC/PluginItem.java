package BEC;

public final class PluginItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFileName = "";

    public String sPackName = "";

    public int iVersionCode = 0;

    public String sShowName = "";

    public String sDesc = "";

    public long lFileSize = 0;

    public String sDownloadUrl = "";

    public String sMd5 = "";

    public int iType = 0;

    public int iMatchMinVer = 0;

    public int iMatchMaxVer = 0;

    public String getSFileName()
    {
        return sFileName;
    }

    public void  setSFileName(String sFileName)
    {
        this.sFileName = sFileName;
    }

    public String getSPackName()
    {
        return sPackName;
    }

    public void  setSPackName(String sPackName)
    {
        this.sPackName = sPackName;
    }

    public int getIVersionCode()
    {
        return iVersionCode;
    }

    public void  setIVersionCode(int iVersionCode)
    {
        this.iVersionCode = iVersionCode;
    }

    public String getSShowName()
    {
        return sShowName;
    }

    public void  setSShowName(String sShowName)
    {
        this.sShowName = sShowName;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public long getLFileSize()
    {
        return lFileSize;
    }

    public void  setLFileSize(long lFileSize)
    {
        this.lFileSize = lFileSize;
    }

    public String getSDownloadUrl()
    {
        return sDownloadUrl;
    }

    public void  setSDownloadUrl(String sDownloadUrl)
    {
        this.sDownloadUrl = sDownloadUrl;
    }

    public String getSMd5()
    {
        return sMd5;
    }

    public void  setSMd5(String sMd5)
    {
        this.sMd5 = sMd5;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public int getIMatchMinVer()
    {
        return iMatchMinVer;
    }

    public void  setIMatchMinVer(int iMatchMinVer)
    {
        this.iMatchMinVer = iMatchMinVer;
    }

    public int getIMatchMaxVer()
    {
        return iMatchMaxVer;
    }

    public void  setIMatchMaxVer(int iMatchMaxVer)
    {
        this.iMatchMaxVer = iMatchMaxVer;
    }

    public PluginItem()
    {
    }

    public PluginItem(String sFileName, String sPackName, int iVersionCode, String sShowName, String sDesc, long lFileSize, String sDownloadUrl, String sMd5, int iType, int iMatchMinVer, int iMatchMaxVer)
    {
        this.sFileName = sFileName;
        this.sPackName = sPackName;
        this.iVersionCode = iVersionCode;
        this.sShowName = sShowName;
        this.sDesc = sDesc;
        this.lFileSize = lFileSize;
        this.sDownloadUrl = sDownloadUrl;
        this.sMd5 = sMd5;
        this.iType = iType;
        this.iMatchMinVer = iMatchMinVer;
        this.iMatchMaxVer = iMatchMaxVer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFileName) {
            ostream.writeString(0, sFileName);
        }
        if (null != sPackName) {
            ostream.writeString(1, sPackName);
        }
        ostream.writeInt32(2, iVersionCode);
        if (null != sShowName) {
            ostream.writeString(3, sShowName);
        }
        if (null != sDesc) {
            ostream.writeString(4, sDesc);
        }
        ostream.writeInt64(5, lFileSize);
        if (null != sDownloadUrl) {
            ostream.writeString(6, sDownloadUrl);
        }
        if (null != sMd5) {
            ostream.writeString(7, sMd5);
        }
        ostream.writeInt32(8, iType);
        ostream.writeInt32(9, iMatchMinVer);
        ostream.writeInt32(10, iMatchMaxVer);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFileName = (String)istream.readString(0, false, this.sFileName);
        this.sPackName = (String)istream.readString(1, false, this.sPackName);
        this.iVersionCode = (int)istream.readInt32(2, false, this.iVersionCode);
        this.sShowName = (String)istream.readString(3, false, this.sShowName);
        this.sDesc = (String)istream.readString(4, false, this.sDesc);
        this.lFileSize = (long)istream.readInt64(5, false, this.lFileSize);
        this.sDownloadUrl = (String)istream.readString(6, false, this.sDownloadUrl);
        this.sMd5 = (String)istream.readString(7, false, this.sMd5);
        this.iType = (int)istream.readInt32(8, false, this.iType);
        this.iMatchMinVer = (int)istream.readInt32(9, false, this.iMatchMinVer);
        this.iMatchMaxVer = (int)istream.readInt32(10, false, this.iMatchMaxVer);
    }

}

