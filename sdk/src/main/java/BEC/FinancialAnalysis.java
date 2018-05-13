package BEC;

public final class FinancialAnalysis extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sEPS = "";

    public String sBVPS = "";

    public String sPB = "";

    public String sROE = "";

    public String sGrossRevenue = "";

    public String sOperatingProfit = "";

    public String sNetProfit = "";

    public String sBusinessRevenueGrowth = "";

    public String sProfitGrowth = "";

    public String sNetGrowth = "";

    public String sTotalAssets = "";

    public String sTotalLiabilities = "";

    public String sTotalEquities = "";

    public String sLEV = "";

    public String sOperationalCashFlow = "";

    public String sInvestmentCashFlows = "";

    public String sFinancingCashFlows = "";

    public String sUpdateTime = "";

    public String sNetCF = "";

    public String getSEPS()
    {
        return sEPS;
    }

    public void  setSEPS(String sEPS)
    {
        this.sEPS = sEPS;
    }

    public String getSBVPS()
    {
        return sBVPS;
    }

    public void  setSBVPS(String sBVPS)
    {
        this.sBVPS = sBVPS;
    }

    public String getSPB()
    {
        return sPB;
    }

    public void  setSPB(String sPB)
    {
        this.sPB = sPB;
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

    public String getSOperatingProfit()
    {
        return sOperatingProfit;
    }

    public void  setSOperatingProfit(String sOperatingProfit)
    {
        this.sOperatingProfit = sOperatingProfit;
    }

    public String getSNetProfit()
    {
        return sNetProfit;
    }

    public void  setSNetProfit(String sNetProfit)
    {
        this.sNetProfit = sNetProfit;
    }

    public String getSBusinessRevenueGrowth()
    {
        return sBusinessRevenueGrowth;
    }

    public void  setSBusinessRevenueGrowth(String sBusinessRevenueGrowth)
    {
        this.sBusinessRevenueGrowth = sBusinessRevenueGrowth;
    }

    public String getSProfitGrowth()
    {
        return sProfitGrowth;
    }

    public void  setSProfitGrowth(String sProfitGrowth)
    {
        this.sProfitGrowth = sProfitGrowth;
    }

    public String getSNetGrowth()
    {
        return sNetGrowth;
    }

    public void  setSNetGrowth(String sNetGrowth)
    {
        this.sNetGrowth = sNetGrowth;
    }

    public String getSTotalAssets()
    {
        return sTotalAssets;
    }

    public void  setSTotalAssets(String sTotalAssets)
    {
        this.sTotalAssets = sTotalAssets;
    }

    public String getSTotalLiabilities()
    {
        return sTotalLiabilities;
    }

    public void  setSTotalLiabilities(String sTotalLiabilities)
    {
        this.sTotalLiabilities = sTotalLiabilities;
    }

    public String getSTotalEquities()
    {
        return sTotalEquities;
    }

    public void  setSTotalEquities(String sTotalEquities)
    {
        this.sTotalEquities = sTotalEquities;
    }

    public String getSLEV()
    {
        return sLEV;
    }

    public void  setSLEV(String sLEV)
    {
        this.sLEV = sLEV;
    }

    public String getSOperationalCashFlow()
    {
        return sOperationalCashFlow;
    }

    public void  setSOperationalCashFlow(String sOperationalCashFlow)
    {
        this.sOperationalCashFlow = sOperationalCashFlow;
    }

    public String getSInvestmentCashFlows()
    {
        return sInvestmentCashFlows;
    }

    public void  setSInvestmentCashFlows(String sInvestmentCashFlows)
    {
        this.sInvestmentCashFlows = sInvestmentCashFlows;
    }

    public String getSFinancingCashFlows()
    {
        return sFinancingCashFlows;
    }

    public void  setSFinancingCashFlows(String sFinancingCashFlows)
    {
        this.sFinancingCashFlows = sFinancingCashFlows;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public String getSNetCF()
    {
        return sNetCF;
    }

    public void  setSNetCF(String sNetCF)
    {
        this.sNetCF = sNetCF;
    }

    public FinancialAnalysis()
    {
    }

    public FinancialAnalysis(String sEPS, String sBVPS, String sPB, String sROE, String sGrossRevenue, String sOperatingProfit, String sNetProfit, String sBusinessRevenueGrowth, String sProfitGrowth, String sNetGrowth, String sTotalAssets, String sTotalLiabilities, String sTotalEquities, String sLEV, String sOperationalCashFlow, String sInvestmentCashFlows, String sFinancingCashFlows, String sUpdateTime, String sNetCF)
    {
        this.sEPS = sEPS;
        this.sBVPS = sBVPS;
        this.sPB = sPB;
        this.sROE = sROE;
        this.sGrossRevenue = sGrossRevenue;
        this.sOperatingProfit = sOperatingProfit;
        this.sNetProfit = sNetProfit;
        this.sBusinessRevenueGrowth = sBusinessRevenueGrowth;
        this.sProfitGrowth = sProfitGrowth;
        this.sNetGrowth = sNetGrowth;
        this.sTotalAssets = sTotalAssets;
        this.sTotalLiabilities = sTotalLiabilities;
        this.sTotalEquities = sTotalEquities;
        this.sLEV = sLEV;
        this.sOperationalCashFlow = sOperationalCashFlow;
        this.sInvestmentCashFlows = sInvestmentCashFlows;
        this.sFinancingCashFlows = sFinancingCashFlows;
        this.sUpdateTime = sUpdateTime;
        this.sNetCF = sNetCF;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sEPS) {
            ostream.writeString(0, sEPS);
        }
        if (null != sBVPS) {
            ostream.writeString(1, sBVPS);
        }
        if (null != sPB) {
            ostream.writeString(2, sPB);
        }
        if (null != sROE) {
            ostream.writeString(3, sROE);
        }
        if (null != sGrossRevenue) {
            ostream.writeString(4, sGrossRevenue);
        }
        if (null != sOperatingProfit) {
            ostream.writeString(5, sOperatingProfit);
        }
        if (null != sNetProfit) {
            ostream.writeString(6, sNetProfit);
        }
        if (null != sBusinessRevenueGrowth) {
            ostream.writeString(7, sBusinessRevenueGrowth);
        }
        if (null != sProfitGrowth) {
            ostream.writeString(8, sProfitGrowth);
        }
        if (null != sNetGrowth) {
            ostream.writeString(9, sNetGrowth);
        }
        if (null != sTotalAssets) {
            ostream.writeString(10, sTotalAssets);
        }
        if (null != sTotalLiabilities) {
            ostream.writeString(11, sTotalLiabilities);
        }
        if (null != sTotalEquities) {
            ostream.writeString(12, sTotalEquities);
        }
        if (null != sLEV) {
            ostream.writeString(13, sLEV);
        }
        if (null != sOperationalCashFlow) {
            ostream.writeString(14, sOperationalCashFlow);
        }
        if (null != sInvestmentCashFlows) {
            ostream.writeString(15, sInvestmentCashFlows);
        }
        if (null != sFinancingCashFlows) {
            ostream.writeString(16, sFinancingCashFlows);
        }
        if (null != sUpdateTime) {
            ostream.writeString(17, sUpdateTime);
        }
        if (null != sNetCF) {
            ostream.writeString(18, sNetCF);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sEPS = (String)istream.readString(0, false, this.sEPS);
        this.sBVPS = (String)istream.readString(1, false, this.sBVPS);
        this.sPB = (String)istream.readString(2, false, this.sPB);
        this.sROE = (String)istream.readString(3, false, this.sROE);
        this.sGrossRevenue = (String)istream.readString(4, false, this.sGrossRevenue);
        this.sOperatingProfit = (String)istream.readString(5, false, this.sOperatingProfit);
        this.sNetProfit = (String)istream.readString(6, false, this.sNetProfit);
        this.sBusinessRevenueGrowth = (String)istream.readString(7, false, this.sBusinessRevenueGrowth);
        this.sProfitGrowth = (String)istream.readString(8, false, this.sProfitGrowth);
        this.sNetGrowth = (String)istream.readString(9, false, this.sNetGrowth);
        this.sTotalAssets = (String)istream.readString(10, false, this.sTotalAssets);
        this.sTotalLiabilities = (String)istream.readString(11, false, this.sTotalLiabilities);
        this.sTotalEquities = (String)istream.readString(12, false, this.sTotalEquities);
        this.sLEV = (String)istream.readString(13, false, this.sLEV);
        this.sOperationalCashFlow = (String)istream.readString(14, false, this.sOperationalCashFlow);
        this.sInvestmentCashFlows = (String)istream.readString(15, false, this.sInvestmentCashFlows);
        this.sFinancingCashFlows = (String)istream.readString(16, false, this.sFinancingCashFlows);
        this.sUpdateTime = (String)istream.readString(17, false, this.sUpdateTime);
        this.sNetCF = (String)istream.readString(18, false, this.sNetCF);
    }

}

