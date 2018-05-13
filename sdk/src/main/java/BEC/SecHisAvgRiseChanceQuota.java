package BEC;

public final class SecHisAvgRiseChanceQuota extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo> vAvgRiseChanceInfo = null;

    public java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo> getVAvgRiseChanceInfo()
    {
        return vAvgRiseChanceInfo;
    }

    public void  setVAvgRiseChanceInfo(java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo> vAvgRiseChanceInfo)
    {
        this.vAvgRiseChanceInfo = vAvgRiseChanceInfo;
    }

    public SecHisAvgRiseChanceQuota()
    {
    }

    public SecHisAvgRiseChanceQuota(java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo> vAvgRiseChanceInfo)
    {
        this.vAvgRiseChanceInfo = vAvgRiseChanceInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vAvgRiseChanceInfo) {
            ostream.writeList(0, vAvgRiseChanceInfo);
        }
    }

    static java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo> VAR_TYPE_4_VAVGRISECHANCEINFO = new java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo>();
    static {
        VAR_TYPE_4_VAVGRISECHANCEINFO.add(new BEC.SecHisAvgRiseChanceQuotaInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vAvgRiseChanceInfo = (java.util.ArrayList<BEC.SecHisAvgRiseChanceQuotaInfo>)istream.readList(0, false, VAR_TYPE_4_VAVGRISECHANCEINFO);
    }

}

