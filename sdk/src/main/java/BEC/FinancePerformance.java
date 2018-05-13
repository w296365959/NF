package BEC;

public final class FinancePerformance extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.OperatingRevenue> vOperatingRevenue = null;

    public String sOperatingDesc = "";

    public java.util.ArrayList<BEC.Profit> vProfit = null;

    public String sProfitDesc = "";

    public java.util.ArrayList<BEC.Profitability> vProfitability = null;

    public String sProfitabilityDesc = "";

    public int eOPUnit = 0;

    public int eProfitUnit = 0;

    public int eEPSUnit = 0;

    public java.util.ArrayList<BEC.OperatingRevenue> getVOperatingRevenue()
    {
        return vOperatingRevenue;
    }

    public void  setVOperatingRevenue(java.util.ArrayList<BEC.OperatingRevenue> vOperatingRevenue)
    {
        this.vOperatingRevenue = vOperatingRevenue;
    }

    public String getSOperatingDesc()
    {
        return sOperatingDesc;
    }

    public void  setSOperatingDesc(String sOperatingDesc)
    {
        this.sOperatingDesc = sOperatingDesc;
    }

    public java.util.ArrayList<BEC.Profit> getVProfit()
    {
        return vProfit;
    }

    public void  setVProfit(java.util.ArrayList<BEC.Profit> vProfit)
    {
        this.vProfit = vProfit;
    }

    public String getSProfitDesc()
    {
        return sProfitDesc;
    }

    public void  setSProfitDesc(String sProfitDesc)
    {
        this.sProfitDesc = sProfitDesc;
    }

    public java.util.ArrayList<BEC.Profitability> getVProfitability()
    {
        return vProfitability;
    }

    public void  setVProfitability(java.util.ArrayList<BEC.Profitability> vProfitability)
    {
        this.vProfitability = vProfitability;
    }

    public String getSProfitabilityDesc()
    {
        return sProfitabilityDesc;
    }

    public void  setSProfitabilityDesc(String sProfitabilityDesc)
    {
        this.sProfitabilityDesc = sProfitabilityDesc;
    }

    public int getEOPUnit()
    {
        return eOPUnit;
    }

    public void  setEOPUnit(int eOPUnit)
    {
        this.eOPUnit = eOPUnit;
    }

    public int getEProfitUnit()
    {
        return eProfitUnit;
    }

    public void  setEProfitUnit(int eProfitUnit)
    {
        this.eProfitUnit = eProfitUnit;
    }

    public int getEEPSUnit()
    {
        return eEPSUnit;
    }

    public void  setEEPSUnit(int eEPSUnit)
    {
        this.eEPSUnit = eEPSUnit;
    }

    public FinancePerformance()
    {
    }

    public FinancePerformance(java.util.ArrayList<BEC.OperatingRevenue> vOperatingRevenue, String sOperatingDesc, java.util.ArrayList<BEC.Profit> vProfit, String sProfitDesc, java.util.ArrayList<BEC.Profitability> vProfitability, String sProfitabilityDesc, int eOPUnit, int eProfitUnit, int eEPSUnit)
    {
        this.vOperatingRevenue = vOperatingRevenue;
        this.sOperatingDesc = sOperatingDesc;
        this.vProfit = vProfit;
        this.sProfitDesc = sProfitDesc;
        this.vProfitability = vProfitability;
        this.sProfitabilityDesc = sProfitabilityDesc;
        this.eOPUnit = eOPUnit;
        this.eProfitUnit = eProfitUnit;
        this.eEPSUnit = eEPSUnit;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vOperatingRevenue) {
            ostream.writeList(0, vOperatingRevenue);
        }
        if (null != sOperatingDesc) {
            ostream.writeString(1, sOperatingDesc);
        }
        if (null != vProfit) {
            ostream.writeList(2, vProfit);
        }
        if (null != sProfitDesc) {
            ostream.writeString(3, sProfitDesc);
        }
        if (null != vProfitability) {
            ostream.writeList(4, vProfitability);
        }
        if (null != sProfitabilityDesc) {
            ostream.writeString(5, sProfitabilityDesc);
        }
        ostream.writeInt32(6, eOPUnit);
        ostream.writeInt32(7, eProfitUnit);
        ostream.writeInt32(8, eEPSUnit);
    }

    static java.util.ArrayList<BEC.OperatingRevenue> VAR_TYPE_4_VOPERATINGREVENUE = new java.util.ArrayList<BEC.OperatingRevenue>();
    static {
        VAR_TYPE_4_VOPERATINGREVENUE.add(new BEC.OperatingRevenue());
    }

    static java.util.ArrayList<BEC.Profit> VAR_TYPE_4_VPROFIT = new java.util.ArrayList<BEC.Profit>();
    static {
        VAR_TYPE_4_VPROFIT.add(new BEC.Profit());
    }

    static java.util.ArrayList<BEC.Profitability> VAR_TYPE_4_VPROFITABILITY = new java.util.ArrayList<BEC.Profitability>();
    static {
        VAR_TYPE_4_VPROFITABILITY.add(new BEC.Profitability());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vOperatingRevenue = (java.util.ArrayList<BEC.OperatingRevenue>)istream.readList(0, false, VAR_TYPE_4_VOPERATINGREVENUE);
        this.sOperatingDesc = (String)istream.readString(1, false, this.sOperatingDesc);
        this.vProfit = (java.util.ArrayList<BEC.Profit>)istream.readList(2, false, VAR_TYPE_4_VPROFIT);
        this.sProfitDesc = (String)istream.readString(3, false, this.sProfitDesc);
        this.vProfitability = (java.util.ArrayList<BEC.Profitability>)istream.readList(4, false, VAR_TYPE_4_VPROFITABILITY);
        this.sProfitabilityDesc = (String)istream.readString(5, false, this.sProfitabilityDesc);
        this.eOPUnit = (int)istream.readInt32(6, false, this.eOPUnit);
        this.eProfitUnit = (int)istream.readInt32(7, false, this.eProfitUnit);
        this.eEPSUnit = (int)istream.readInt32(8, false, this.eEPSUnit);
    }

}

