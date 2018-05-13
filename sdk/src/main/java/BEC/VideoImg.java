package BEC;

public final class VideoImg extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sType = "";

    public String sUrl = "";

    public String getSType()
    {
        return sType;
    }

    public void  setSType(String sType)
    {
        this.sType = sType;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public VideoImg()
    {
    }

    public VideoImg(String sType, String sUrl)
    {
        this.sType = sType;
        this.sUrl = sUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sType) {
            ostream.writeString(0, sType);
        }
        if (null != sUrl) {
            ostream.writeString(1, sUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sType = (String)istream.readString(0, false, this.sType);
        this.sUrl = (String)istream.readString(1, false, this.sUrl);
    }

}

