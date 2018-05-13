package BEC;

public final class CategoryHotStockDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public String sDesc = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public CategoryHotStockDesc()
    {
    }

    public CategoryHotStockDesc(String sDtSecCode, String sSecName, String sDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.sDesc = sDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
        if (null != sDesc) {
            ostream.writeString(2, sDesc);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.sDesc = (String)istream.readString(2, false, this.sDesc);
    }

}

