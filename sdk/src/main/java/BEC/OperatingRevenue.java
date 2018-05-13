package BEC;

public final class OperatingRevenue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sYear = "";

    public float fIncome = 0;

    public float fYearOnYear = 0;

    public java.util.ArrayList<BEC.SeasonOperatingRevenue> vtSeasonOperatingRevenue = null;

    public String getSYear()
    {
        return sYear;
    }

    public void  setSYear(String sYear)
    {
        this.sYear = sYear;
    }

    public float getFIncome()
    {
        return fIncome;
    }

    public void  setFIncome(float fIncome)
    {
        this.fIncome = fIncome;
    }

    public float getFYearOnYear()
    {
        return fYearOnYear;
    }

    public void  setFYearOnYear(float fYearOnYear)
    {
        this.fYearOnYear = fYearOnYear;
    }

    public java.util.ArrayList<BEC.SeasonOperatingRevenue> getVtSeasonOperatingRevenue()
    {
        return vtSeasonOperatingRevenue;
    }

    public void  setVtSeasonOperatingRevenue(java.util.ArrayList<BEC.SeasonOperatingRevenue> vtSeasonOperatingRevenue)
    {
        this.vtSeasonOperatingRevenue = vtSeasonOperatingRevenue;
    }

    public OperatingRevenue()
    {
    }

    public OperatingRevenue(String sYear, float fIncome, float fYearOnYear, java.util.ArrayList<BEC.SeasonOperatingRevenue> vtSeasonOperatingRevenue)
    {
        this.sYear = sYear;
        this.fIncome = fIncome;
        this.fYearOnYear = fYearOnYear;
        this.vtSeasonOperatingRevenue = vtSeasonOperatingRevenue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sYear) {
            ostream.writeString(0, sYear);
        }
        ostream.writeFloat(1, fIncome);
        ostream.writeFloat(2, fYearOnYear);
        if (null != vtSeasonOperatingRevenue) {
            ostream.writeList(3, vtSeasonOperatingRevenue);
        }
    }

    static java.util.ArrayList<BEC.SeasonOperatingRevenue> VAR_TYPE_4_VTSEASONOPERATINGREVENUE = new java.util.ArrayList<BEC.SeasonOperatingRevenue>();
    static {
        VAR_TYPE_4_VTSEASONOPERATINGREVENUE.add(new BEC.SeasonOperatingRevenue());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sYear = (String)istream.readString(0, false, this.sYear);
        this.fIncome = (float)istream.readFloat(1, false, this.fIncome);
        this.fYearOnYear = (float)istream.readFloat(2, false, this.fYearOnYear);
        this.vtSeasonOperatingRevenue = (java.util.ArrayList<BEC.SeasonOperatingRevenue>)istream.readList(3, false, VAR_TYPE_4_VTSEASONOPERATINGREVENUE);
    }

}

