package BEC;

public final class SecPerformance extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<OperatingRevenue> vOperatingRevenue = null;

    public String sOperatingDesc = "";

    public java.util.ArrayList<Profit> vProfit = null;

    public String sProfitDesc = "";

    public java.util.ArrayList<OperatingRevenue> getVOperatingRevenue()
    {
        return vOperatingRevenue;
    }

    public void  setVOperatingRevenue(java.util.ArrayList<OperatingRevenue> vOperatingRevenue)
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

    public java.util.ArrayList<Profit> getVProfit()
    {
        return vProfit;
    }

    public void  setVProfit(java.util.ArrayList<Profit> vProfit)
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

    public SecPerformance()
    {
    }

    public SecPerformance(java.util.ArrayList<OperatingRevenue> vOperatingRevenue, String sOperatingDesc, java.util.ArrayList<Profit> vProfit, String sProfitDesc)
    {
        this.vOperatingRevenue = vOperatingRevenue;
        this.sOperatingDesc = sOperatingDesc;
        this.vProfit = vProfit;
        this.sProfitDesc = sProfitDesc;
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
    }

    static java.util.ArrayList<OperatingRevenue> VAR_TYPE_4_VOPERATINGREVENUE = new java.util.ArrayList<OperatingRevenue>();
    static {
        VAR_TYPE_4_VOPERATINGREVENUE.add(new OperatingRevenue());
    }

    static java.util.ArrayList<Profit> VAR_TYPE_4_VPROFIT = new java.util.ArrayList<Profit>();
    static {
        VAR_TYPE_4_VPROFIT.add(new Profit());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vOperatingRevenue = (java.util.ArrayList<OperatingRevenue>)istream.readList(0, false, VAR_TYPE_4_VOPERATINGREVENUE);
        this.sOperatingDesc = (String)istream.readString(1, false, this.sOperatingDesc);
        this.vProfit = (java.util.ArrayList<Profit>)istream.readList(2, false, VAR_TYPE_4_VPROFIT);
        this.sProfitDesc = (String)istream.readString(3, false, this.sProfitDesc);
    }

}

