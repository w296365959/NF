package BEC;

public final class PatchUrlRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sUrl = "";

    public String sMD5 = "";

    public boolean bIsLatest = true;

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSMD5()
    {
        return sMD5;
    }

    public void  setSMD5(String sMD5)
    {
        this.sMD5 = sMD5;
    }

    public boolean getBIsLatest()
    {
        return bIsLatest;
    }

    public void  setBIsLatest(boolean bIsLatest)
    {
        this.bIsLatest = bIsLatest;
    }

    public PatchUrlRsp()
    {
    }

    public PatchUrlRsp(String sUrl, String sMD5, boolean bIsLatest)
    {
        this.sUrl = sUrl;
        this.sMD5 = sMD5;
        this.bIsLatest = bIsLatest;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sUrl) {
            ostream.writeString(0, sUrl);
        }
        if (null != sMD5) {
            ostream.writeString(1, sMD5);
        }
        ostream.writeBoolean(2, bIsLatest);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sUrl = (String)istream.readString(0, false, this.sUrl);
        this.sMD5 = (String)istream.readString(1, false, this.sMD5);
        this.bIsLatest = (boolean)istream.readBoolean(2, false, this.bIsLatest);
    }

}

