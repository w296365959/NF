package BEC;

public final class RelatedSecurity extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String sCode = "";

    public String sDtCode = "";

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSCode()
    {
        return sCode;
    }

    public void  setSCode(String sCode)
    {
        this.sCode = sCode;
    }

    public String getSDtCode()
    {
        return sDtCode;
    }

    public void  setSDtCode(String sDtCode)
    {
        this.sDtCode = sDtCode;
    }

    public RelatedSecurity()
    {
    }

    public RelatedSecurity(String sName, String sCode, String sDtCode)
    {
        this.sName = sName;
        this.sCode = sCode;
        this.sDtCode = sDtCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != sCode) {
            ostream.writeString(1, sCode);
        }
        if (null != sDtCode) {
            ostream.writeString(2, sDtCode);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sCode = (String)istream.readString(1, false, this.sCode);
        this.sDtCode = (String)istream.readString(2, false, this.sDtCode);
    }

}

