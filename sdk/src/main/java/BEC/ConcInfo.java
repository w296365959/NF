package BEC;

public final class ConcInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sCHNShortName = "";

    public String sConcDesc = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSCHNShortName()
    {
        return sCHNShortName;
    }

    public void  setSCHNShortName(String sCHNShortName)
    {
        this.sCHNShortName = sCHNShortName;
    }

    public String getSConcDesc()
    {
        return sConcDesc;
    }

    public void  setSConcDesc(String sConcDesc)
    {
        this.sConcDesc = sConcDesc;
    }

    public ConcInfo()
    {
    }

    public ConcInfo(String sDtSecCode, String sCHNShortName, String sConcDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.sCHNShortName = sCHNShortName;
        this.sConcDesc = sConcDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sCHNShortName) {
            ostream.writeString(1, sCHNShortName);
        }
        if (null != sConcDesc) {
            ostream.writeString(2, sConcDesc);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sCHNShortName = (String)istream.readString(1, false, this.sCHNShortName);
        this.sConcDesc = (String)istream.readString(2, false, this.sConcDesc);
    }

}

