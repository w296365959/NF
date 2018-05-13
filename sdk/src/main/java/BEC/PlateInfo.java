package BEC;

public final class PlateInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sPlateName = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSPlateName()
    {
        return sPlateName;
    }

    public void  setSPlateName(String sPlateName)
    {
        this.sPlateName = sPlateName;
    }

    public PlateInfo()
    {
    }

    public PlateInfo(String sDtSecCode, String sPlateName)
    {
        this.sDtSecCode = sDtSecCode;
        this.sPlateName = sPlateName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sPlateName) {
            ostream.writeString(1, sPlateName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sPlateName = (String)istream.readString(1, false, this.sPlateName);
    }

}

