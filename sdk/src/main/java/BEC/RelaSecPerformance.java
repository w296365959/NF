package BEC;

public final class RelaSecPerformance extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SimpleFinancialAnalysis> vtPerformance = null;

    public String sDesc = "";

    public int iUpdateTime = 0;

    public java.util.ArrayList<SimpleFinancialAnalysis> getVtPerformance()
    {
        return vtPerformance;
    }

    public void  setVtPerformance(java.util.ArrayList<SimpleFinancialAnalysis> vtPerformance)
    {
        this.vtPerformance = vtPerformance;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public RelaSecPerformance()
    {
    }

    public RelaSecPerformance(java.util.ArrayList<SimpleFinancialAnalysis> vtPerformance, String sDesc, int iUpdateTime)
    {
        this.vtPerformance = vtPerformance;
        this.sDesc = sDesc;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtPerformance) {
            ostream.writeList(0, vtPerformance);
        }
        if (null != sDesc) {
            ostream.writeString(1, sDesc);
        }
        ostream.writeInt32(2, iUpdateTime);
    }

    static java.util.ArrayList<SimpleFinancialAnalysis> VAR_TYPE_4_VTPERFORMANCE = new java.util.ArrayList<SimpleFinancialAnalysis>();
    static {
        VAR_TYPE_4_VTPERFORMANCE.add(new SimpleFinancialAnalysis());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtPerformance = (java.util.ArrayList<SimpleFinancialAnalysis>)istream.readList(0, false, VAR_TYPE_4_VTPERFORMANCE);
        this.sDesc = (String)istream.readString(1, false, this.sDesc);
        this.iUpdateTime = (int)istream.readInt32(2, false, this.iUpdateTime);
    }

}

