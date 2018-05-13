package BEC;

public final class UploadAppRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDownloadUrl = "";

    public String getSDownloadUrl()
    {
        return sDownloadUrl;
    }

    public void  setSDownloadUrl(String sDownloadUrl)
    {
        this.sDownloadUrl = sDownloadUrl;
    }

    public UploadAppRsp()
    {
    }

    public UploadAppRsp(String sDownloadUrl)
    {
        this.sDownloadUrl = sDownloadUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDownloadUrl) {
            ostream.writeString(0, sDownloadUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDownloadUrl = (String)istream.readString(0, false, this.sDownloadUrl);
    }

}

