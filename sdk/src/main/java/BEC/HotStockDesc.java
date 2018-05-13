package BEC;

public final class HotStockDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public String sStrategyName = "";

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

    public String getSStrategyName()
    {
        return sStrategyName;
    }

    public void  setSStrategyName(String sStrategyName)
    {
        this.sStrategyName = sStrategyName;
    }

    public HotStockDesc()
    {
    }

    public HotStockDesc(String sDtSecCode, String sSecName, String sStrategyName)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.sStrategyName = sStrategyName;
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
        if (null != sStrategyName) {
            ostream.writeString(2, sStrategyName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.sStrategyName = (String)istream.readString(2, false, this.sStrategyName);
    }

}

