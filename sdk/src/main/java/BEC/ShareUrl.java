package BEC;

public final class ShareUrl extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eShareType = 0;

    public String sUrl = "";

    public int getEShareType()
    {
        return eShareType;
    }

    public void  setEShareType(int eShareType)
    {
        this.eShareType = eShareType;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public ShareUrl()
    {
    }

    public ShareUrl(int eShareType, String sUrl)
    {
        this.eShareType = eShareType;
        this.sUrl = sUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eShareType);
        if (null != sUrl) {
            ostream.writeString(1, sUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eShareType = (int)istream.readInt32(0, false, this.eShareType);
        this.sUrl = (String)istream.readString(1, false, this.sUrl);
    }

}

