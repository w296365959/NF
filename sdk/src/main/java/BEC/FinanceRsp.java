package BEC;

public final class FinanceRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FinancePerformance stFinancePerformance = null;

    public BEC.FinancialAnalysis stFinancialAnalysis = null;

    public BEC.FinancePerformance getStFinancePerformance()
    {
        return stFinancePerformance;
    }

    public void  setStFinancePerformance(BEC.FinancePerformance stFinancePerformance)
    {
        this.stFinancePerformance = stFinancePerformance;
    }

    public BEC.FinancialAnalysis getStFinancialAnalysis()
    {
        return stFinancialAnalysis;
    }

    public void  setStFinancialAnalysis(BEC.FinancialAnalysis stFinancialAnalysis)
    {
        this.stFinancialAnalysis = stFinancialAnalysis;
    }

    public FinanceRsp()
    {
    }

    public FinanceRsp(BEC.FinancePerformance stFinancePerformance, BEC.FinancialAnalysis stFinancialAnalysis)
    {
        this.stFinancePerformance = stFinancePerformance;
        this.stFinancialAnalysis = stFinancialAnalysis;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFinancePerformance) {
            ostream.writeMessage(0, stFinancePerformance);
        }
        if (null != stFinancialAnalysis) {
            ostream.writeMessage(1, stFinancialAnalysis);
        }
    }

    static BEC.FinancePerformance VAR_TYPE_4_STFINANCEPERFORMANCE = new BEC.FinancePerformance();

    static BEC.FinancialAnalysis VAR_TYPE_4_STFINANCIALANALYSIS = new BEC.FinancialAnalysis();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFinancePerformance = (BEC.FinancePerformance)istream.readMessage(0, false, VAR_TYPE_4_STFINANCEPERFORMANCE);
        this.stFinancialAnalysis = (BEC.FinancialAnalysis)istream.readMessage(1, false, VAR_TYPE_4_STFINANCIALANALYSIS);
    }

}

