package BEC;

public final class InvestAdviseInfoList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.InvestAdviseInfo> vInvestAdviseInfo = null;

    public java.util.ArrayList<BEC.InvestAdviseInfo> getVInvestAdviseInfo()
    {
        return vInvestAdviseInfo;
    }

    public void  setVInvestAdviseInfo(java.util.ArrayList<BEC.InvestAdviseInfo> vInvestAdviseInfo)
    {
        this.vInvestAdviseInfo = vInvestAdviseInfo;
    }

    public InvestAdviseInfoList()
    {
    }

    public InvestAdviseInfoList(java.util.ArrayList<BEC.InvestAdviseInfo> vInvestAdviseInfo)
    {
        this.vInvestAdviseInfo = vInvestAdviseInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vInvestAdviseInfo) {
            ostream.writeList(0, vInvestAdviseInfo);
        }
    }

    static java.util.ArrayList<BEC.InvestAdviseInfo> VAR_TYPE_4_VINVESTADVISEINFO = new java.util.ArrayList<BEC.InvestAdviseInfo>();
    static {
        VAR_TYPE_4_VINVESTADVISEINFO.add(new BEC.InvestAdviseInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vInvestAdviseInfo = (java.util.ArrayList<BEC.InvestAdviseInfo>)istream.readList(0, false, VAR_TYPE_4_VINVESTADVISEINFO);
    }

}

