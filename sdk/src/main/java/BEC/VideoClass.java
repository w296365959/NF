package BEC;

public final class VideoClass extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sClassName = "";

    public String sClassImgUrl = "";

    public String getSClassName()
    {
        return sClassName;
    }

    public void  setSClassName(String sClassName)
    {
        this.sClassName = sClassName;
    }

    public String getSClassImgUrl()
    {
        return sClassImgUrl;
    }

    public void  setSClassImgUrl(String sClassImgUrl)
    {
        this.sClassImgUrl = sClassImgUrl;
    }

    public VideoClass()
    {
    }

    public VideoClass(String sClassName, String sClassImgUrl)
    {
        this.sClassName = sClassName;
        this.sClassImgUrl = sClassImgUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sClassName) {
            ostream.writeString(0, sClassName);
        }
        if (null != sClassImgUrl) {
            ostream.writeString(1, sClassImgUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sClassName = (String)istream.readString(0, false, this.sClassName);
        this.sClassImgUrl = (String)istream.readString(1, false, this.sClassImgUrl);
    }

}

