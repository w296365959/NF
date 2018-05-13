package BEC;

public final class PrimeOperatingRevenue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTypeName = "";

    public String sSalesRevenue = "";

    public String sRatio = "";

    public float fRatio = 0;

    public double dSalesRevenue = 0;

    public String getSTypeName()
    {
        return sTypeName;
    }

    public void  setSTypeName(String sTypeName)
    {
        this.sTypeName = sTypeName;
    }

    public String getSSalesRevenue()
    {
        return sSalesRevenue;
    }

    public void  setSSalesRevenue(String sSalesRevenue)
    {
        this.sSalesRevenue = sSalesRevenue;
    }

    public String getSRatio()
    {
        return sRatio;
    }

    public void  setSRatio(String sRatio)
    {
        this.sRatio = sRatio;
    }

    public float getFRatio()
    {
        return fRatio;
    }

    public void  setFRatio(float fRatio)
    {
        this.fRatio = fRatio;
    }

    public double getDSalesRevenue()
    {
        return dSalesRevenue;
    }

    public void  setDSalesRevenue(double dSalesRevenue)
    {
        this.dSalesRevenue = dSalesRevenue;
    }

    public PrimeOperatingRevenue()
    {
    }

    public PrimeOperatingRevenue(String sTypeName, String sSalesRevenue, String sRatio, float fRatio, double dSalesRevenue)
    {
        this.sTypeName = sTypeName;
        this.sSalesRevenue = sSalesRevenue;
        this.sRatio = sRatio;
        this.fRatio = fRatio;
        this.dSalesRevenue = dSalesRevenue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTypeName) {
            ostream.writeString(0, sTypeName);
        }
        if (null != sSalesRevenue) {
            ostream.writeString(1, sSalesRevenue);
        }
        if (null != sRatio) {
            ostream.writeString(2, sRatio);
        }
        ostream.writeFloat(3, fRatio);
        ostream.writeDouble(4, dSalesRevenue);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTypeName = (String)istream.readString(0, false, this.sTypeName);
        this.sSalesRevenue = (String)istream.readString(1, false, this.sSalesRevenue);
        this.sRatio = (String)istream.readString(2, false, this.sRatio);
        this.fRatio = (float)istream.readFloat(3, false, this.fRatio);
        this.dSalesRevenue = (double)istream.readDouble(4, false, this.dSalesRevenue);
    }

}

