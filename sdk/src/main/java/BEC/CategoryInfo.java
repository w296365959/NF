package BEC;

public final class CategoryInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sName = "";

    public String sPicUrl = "";

    public String sUrl = "";

    public String sDesc = "";

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSPicUrl()
    {
        return sPicUrl;
    }

    public void  setSPicUrl(String sPicUrl)
    {
        this.sPicUrl = sPicUrl;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public CategoryInfo()
    {
    }

    public CategoryInfo(String sId, String sName, String sPicUrl, String sUrl, String sDesc)
    {
        this.sId = sId;
        this.sName = sName;
        this.sPicUrl = sPicUrl;
        this.sUrl = sUrl;
        this.sDesc = sDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sPicUrl) {
            ostream.writeString(2, sPicUrl);
        }
        if (null != sUrl) {
            ostream.writeString(3, sUrl);
        }
        if (null != sDesc) {
            ostream.writeString(4, sDesc);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sPicUrl = (String)istream.readString(2, false, this.sPicUrl);
        this.sUrl = (String)istream.readString(3, false, this.sUrl);
        this.sDesc = (String)istream.readString(4, false, this.sDesc);
    }

}

