package BEC;

public final class ConcDetailRelaStock extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sStockName = "";

    public float fRelaDegree = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSStockName()
    {
        return sStockName;
    }

    public void  setSStockName(String sStockName)
    {
        this.sStockName = sStockName;
    }

    public float getFRelaDegree()
    {
        return fRelaDegree;
    }

    public void  setFRelaDegree(float fRelaDegree)
    {
        this.fRelaDegree = fRelaDegree;
    }

    public ConcDetailRelaStock()
    {
    }

    public ConcDetailRelaStock(String sDtSecCode, String sStockName, float fRelaDegree)
    {
        this.sDtSecCode = sDtSecCode;
        this.sStockName = sStockName;
        this.fRelaDegree = fRelaDegree;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sStockName) {
            ostream.writeString(1, sStockName);
        }
        ostream.writeFloat(2, fRelaDegree);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sStockName = (String)istream.readString(1, false, this.sStockName);
        this.fRelaDegree = (float)istream.readFloat(2, false, this.fRelaDegree);
    }

}

