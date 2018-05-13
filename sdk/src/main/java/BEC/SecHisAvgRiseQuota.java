package BEC;

public final class SecHisAvgRiseQuota extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo> vAvgRiseInfo = null;

    public java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo> getVAvgRiseInfo()
    {
        return vAvgRiseInfo;
    }

    public void  setVAvgRiseInfo(java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo> vAvgRiseInfo)
    {
        this.vAvgRiseInfo = vAvgRiseInfo;
    }

    public SecHisAvgRiseQuota()
    {
    }

    public SecHisAvgRiseQuota(java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo> vAvgRiseInfo)
    {
        this.vAvgRiseInfo = vAvgRiseInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vAvgRiseInfo) {
            ostream.writeList(0, vAvgRiseInfo);
        }
    }

    static java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo> VAR_TYPE_4_VAVGRISEINFO = new java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo>();
    static {
        VAR_TYPE_4_VAVGRISEINFO.add(new BEC.SecHisAvgRiseQuotaInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vAvgRiseInfo = (java.util.ArrayList<BEC.SecHisAvgRiseQuotaInfo>)istream.readList(0, false, VAR_TYPE_4_VAVGRISEINFO);
    }

}

