package BEC;

public final class SecValuation extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStockName = "";

    public String sDtSecCode = "";

    public String sMarketValue = "";

    public String sPe = "";

    public String sPb = "";

    public String getSStockName()
    {
        return sStockName;
    }

    public void  setSStockName(String sStockName)
    {
        this.sStockName = sStockName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSMarketValue()
    {
        return sMarketValue;
    }

    public void  setSMarketValue(String sMarketValue)
    {
        this.sMarketValue = sMarketValue;
    }

    public String getSPe()
    {
        return sPe;
    }

    public void  setSPe(String sPe)
    {
        this.sPe = sPe;
    }

    public String getSPb()
    {
        return sPb;
    }

    public void  setSPb(String sPb)
    {
        this.sPb = sPb;
    }

    public SecValuation()
    {
    }

    public SecValuation(String sStockName, String sDtSecCode, String sMarketValue, String sPe, String sPb)
    {
        this.sStockName = sStockName;
        this.sDtSecCode = sDtSecCode;
        this.sMarketValue = sMarketValue;
        this.sPe = sPe;
        this.sPb = sPb;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sStockName) {
            ostream.writeString(0, sStockName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sMarketValue) {
            ostream.writeString(2, sMarketValue);
        }
        if (null != sPe) {
            ostream.writeString(3, sPe);
        }
        if (null != sPb) {
            ostream.writeString(4, sPb);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sStockName = (String)istream.readString(0, false, this.sStockName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sMarketValue = (String)istream.readString(2, false, this.sMarketValue);
        this.sPe = (String)istream.readString(3, false, this.sPe);
        this.sPb = (String)istream.readString(4, false, this.sPb);
    }

}

