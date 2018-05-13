package BEC;

public final class ZipDiffRes extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFileName = "";

    public String sMd5 = "";

    public String sUrl = "";

    public String getSFileName()
    {
        return sFileName;
    }

    public void  setSFileName(String sFileName)
    {
        this.sFileName = sFileName;
    }

    public String getSMd5()
    {
        return sMd5;
    }

    public void  setSMd5(String sMd5)
    {
        this.sMd5 = sMd5;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public ZipDiffRes()
    {
    }

    public ZipDiffRes(String sFileName, String sMd5, String sUrl)
    {
        this.sFileName = sFileName;
        this.sMd5 = sMd5;
        this.sUrl = sUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFileName) {
            ostream.writeString(0, sFileName);
        }
        if (null != sMd5) {
            ostream.writeString(1, sMd5);
        }
        if (null != sUrl) {
            ostream.writeString(2, sUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFileName = (String)istream.readString(0, false, this.sFileName);
        this.sMd5 = (String)istream.readString(1, false, this.sMd5);
        this.sUrl = (String)istream.readString(2, false, this.sUrl);
    }

}

