package BEC;

public final class SimpleFinancialAnalysis extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStockName = "";

    public String sDtSecCode = "";

    public String sROE = "";

    public String sGrossRevenue = "";

    public String sNetProfit = "";

    public float fNAPS = 0;

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

    public String getSROE()
    {
        return sROE;
    }

    public void  setSROE(String sROE)
    {
        this.sROE = sROE;
    }

    public String getSGrossRevenue()
    {
        return sGrossRevenue;
    }

    public void  setSGrossRevenue(String sGrossRevenue)
    {
        this.sGrossRevenue = sGrossRevenue;
    }

    public String getSNetProfit()
    {
        return sNetProfit;
    }

    public void  setSNetProfit(String sNetProfit)
    {
        this.sNetProfit = sNetProfit;
    }

    public float getFNAPS()
    {
        return fNAPS;
    }

    public void  setFNAPS(float fNAPS)
    {
        this.fNAPS = fNAPS;
    }

    public SimpleFinancialAnalysis()
    {
    }

    public SimpleFinancialAnalysis(String sStockName, String sDtSecCode, String sROE, String sGrossRevenue, String sNetProfit, float fNAPS)
    {
        this.sStockName = sStockName;
        this.sDtSecCode = sDtSecCode;
        this.sROE = sROE;
        this.sGrossRevenue = sGrossRevenue;
        this.sNetProfit = sNetProfit;
        this.fNAPS = fNAPS;
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
        if (null != sROE) {
            ostream.writeString(2, sROE);
        }
        if (null != sGrossRevenue) {
            ostream.writeString(3, sGrossRevenue);
        }
        if (null != sNetProfit) {
            ostream.writeString(4, sNetProfit);
        }
        ostream.writeFloat(5, fNAPS);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sStockName = (String)istream.readString(0, false, this.sStockName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sROE = (String)istream.readString(2, false, this.sROE);
        this.sGrossRevenue = (String)istream.readString(3, false, this.sGrossRevenue);
        this.sNetProfit = (String)istream.readString(4, false, this.sNetProfit);
        this.fNAPS = (float)istream.readFloat(5, false, this.fNAPS);
    }

}

