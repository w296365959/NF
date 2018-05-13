package BEC;

public final class SplashScreenRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sUrl = "";

    public String sUpdateTime = "";

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public SplashScreenRsp()
    {
    }

    public SplashScreenRsp(String sUrl, String sUpdateTime)
    {
        this.sUrl = sUrl;
        this.sUpdateTime = sUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sUrl) {
            ostream.writeString(0, sUrl);
        }
        if (null != sUpdateTime) {
            ostream.writeString(1, sUpdateTime);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sUrl = (String)istream.readString(0, false, this.sUrl);
        this.sUpdateTime = (String)istream.readString(1, false, this.sUpdateTime);
    }

}

