package BEC;

public final class ZipResponse extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public boolean bLast = true;

    public int iType = 0;

    public int iVersion = 0;

    public String sUrl = "";

    public int iSize = 0;

    public String sMd5 = "";

    public String sUploadTime = "";

    public String sAppVersion = "";

    public java.util.ArrayList<ZipDiffRes> vZipDiffRes = null;

    public boolean getBLast()
    {
        return bLast;
    }

    public void  setBLast(boolean bLast)
    {
        this.bLast = bLast;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public int getISize()
    {
        return iSize;
    }

    public void  setISize(int iSize)
    {
        this.iSize = iSize;
    }

    public String getSMd5()
    {
        return sMd5;
    }

    public void  setSMd5(String sMd5)
    {
        this.sMd5 = sMd5;
    }

    public String getSUploadTime()
    {
        return sUploadTime;
    }

    public void  setSUploadTime(String sUploadTime)
    {
        this.sUploadTime = sUploadTime;
    }

    public String getSAppVersion()
    {
        return sAppVersion;
    }

    public void  setSAppVersion(String sAppVersion)
    {
        this.sAppVersion = sAppVersion;
    }

    public java.util.ArrayList<ZipDiffRes> getVZipDiffRes()
    {
        return vZipDiffRes;
    }

    public void  setVZipDiffRes(java.util.ArrayList<ZipDiffRes> vZipDiffRes)
    {
        this.vZipDiffRes = vZipDiffRes;
    }

    public ZipResponse()
    {
    }

    public ZipResponse(boolean bLast, int iType, int iVersion, String sUrl, int iSize, String sMd5, String sUploadTime, String sAppVersion, java.util.ArrayList<ZipDiffRes> vZipDiffRes)
    {
        this.bLast = bLast;
        this.iType = iType;
        this.iVersion = iVersion;
        this.sUrl = sUrl;
        this.iSize = iSize;
        this.sMd5 = sMd5;
        this.sUploadTime = sUploadTime;
        this.sAppVersion = sAppVersion;
        this.vZipDiffRes = vZipDiffRes;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeBoolean(0, bLast);
        ostream.writeInt32(1, iType);
        ostream.writeInt32(2, iVersion);
        if (null != sUrl) {
            ostream.writeString(3, sUrl);
        }
        ostream.writeInt32(4, iSize);
        if (null != sMd5) {
            ostream.writeString(5, sMd5);
        }
        if (null != sUploadTime) {
            ostream.writeString(6, sUploadTime);
        }
        if (null != sAppVersion) {
            ostream.writeString(7, sAppVersion);
        }
        if (null != vZipDiffRes) {
            ostream.writeList(8, vZipDiffRes);
        }
    }

    static java.util.ArrayList<ZipDiffRes> VAR_TYPE_4_VZIPDIFFRES = new java.util.ArrayList<ZipDiffRes>();
    static {
        VAR_TYPE_4_VZIPDIFFRES.add(new ZipDiffRes());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.bLast = (boolean)istream.readBoolean(0, false, this.bLast);
        this.iType = (int)istream.readInt32(1, false, this.iType);
        this.iVersion = (int)istream.readInt32(2, false, this.iVersion);
        this.sUrl = (String)istream.readString(3, false, this.sUrl);
        this.iSize = (int)istream.readInt32(4, false, this.iSize);
        this.sMd5 = (String)istream.readString(5, false, this.sMd5);
        this.sUploadTime = (String)istream.readString(6, false, this.sUploadTime);
        this.sAppVersion = (String)istream.readString(7, false, this.sAppVersion);
        this.vZipDiffRes = (java.util.ArrayList<ZipDiffRes>)istream.readList(8, false, VAR_TYPE_4_VZIPDIFFRES);
    }

}

