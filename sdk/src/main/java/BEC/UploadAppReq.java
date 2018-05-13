package BEC;

public final class UploadAppReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sFileName = "";

    public String sFileContent = "";

    public String getSFileName()
    {
        return sFileName;
    }

    public void  setSFileName(String sFileName)
    {
        this.sFileName = sFileName;
    }

    public String getSFileContent()
    {
        return sFileContent;
    }

    public void  setSFileContent(String sFileContent)
    {
        this.sFileContent = sFileContent;
    }

    public UploadAppReq()
    {
    }

    public UploadAppReq(String sFileName, String sFileContent)
    {
        this.sFileName = sFileName;
        this.sFileContent = sFileContent;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sFileName) {
            ostream.writeString(0, sFileName);
        }
        if (null != sFileContent) {
            ostream.writeString(1, sFileContent);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sFileName = (String)istream.readString(0, false, this.sFileName);
        this.sFileContent = (String)istream.readString(1, false, this.sFileContent);
    }

}

