package BEC;

public final class RelateStocksSimpleFinanceAnalysisRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SimpleFinancialAnalysis> vtFinancialAnalysis = null;

    public int iUpdateTime = 0;

    public String sPlatePb = "";

    public String sPlatePe = "";

    public java.util.ArrayList<BEC.SimpleFinancialAnalysis> getVtFinancialAnalysis()
    {
        return vtFinancialAnalysis;
    }

    public void  setVtFinancialAnalysis(java.util.ArrayList<BEC.SimpleFinancialAnalysis> vtFinancialAnalysis)
    {
        this.vtFinancialAnalysis = vtFinancialAnalysis;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public String getSPlatePb()
    {
        return sPlatePb;
    }

    public void  setSPlatePb(String sPlatePb)
    {
        this.sPlatePb = sPlatePb;
    }

    public String getSPlatePe()
    {
        return sPlatePe;
    }

    public void  setSPlatePe(String sPlatePe)
    {
        this.sPlatePe = sPlatePe;
    }

    public RelateStocksSimpleFinanceAnalysisRsp()
    {
    }

    public RelateStocksSimpleFinanceAnalysisRsp(java.util.ArrayList<BEC.SimpleFinancialAnalysis> vtFinancialAnalysis, int iUpdateTime, String sPlatePb, String sPlatePe)
    {
        this.vtFinancialAnalysis = vtFinancialAnalysis;
        this.iUpdateTime = iUpdateTime;
        this.sPlatePb = sPlatePb;
        this.sPlatePe = sPlatePe;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtFinancialAnalysis) {
            ostream.writeList(0, vtFinancialAnalysis);
        }
        ostream.writeInt32(1, iUpdateTime);
        if (null != sPlatePb) {
            ostream.writeString(2, sPlatePb);
        }
        if (null != sPlatePe) {
            ostream.writeString(3, sPlatePe);
        }
    }

    static java.util.ArrayList<BEC.SimpleFinancialAnalysis> VAR_TYPE_4_VTFINANCIALANALYSIS = new java.util.ArrayList<BEC.SimpleFinancialAnalysis>();
    static {
        VAR_TYPE_4_VTFINANCIALANALYSIS.add(new BEC.SimpleFinancialAnalysis());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtFinancialAnalysis = (java.util.ArrayList<BEC.SimpleFinancialAnalysis>)istream.readList(0, false, VAR_TYPE_4_VTFINANCIALANALYSIS);
        this.iUpdateTime = (int)istream.readInt32(1, false, this.iUpdateTime);
        this.sPlatePb = (String)istream.readString(2, false, this.sPlatePb);
        this.sPlatePe = (String)istream.readString(3, false, this.sPlatePe);
    }

}

