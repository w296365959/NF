package BEC;

public final class SecHisSingalQuota extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDesc = "";

    public java.util.ArrayList<BEC.SecHisMonthQuotaInfo> vInfo = null;

    public String sSelectName = "";

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public java.util.ArrayList<BEC.SecHisMonthQuotaInfo> getVInfo()
    {
        return vInfo;
    }

    public void  setVInfo(java.util.ArrayList<BEC.SecHisMonthQuotaInfo> vInfo)
    {
        this.vInfo = vInfo;
    }

    public String getSSelectName()
    {
        return sSelectName;
    }

    public void  setSSelectName(String sSelectName)
    {
        this.sSelectName = sSelectName;
    }

    public SecHisSingalQuota()
    {
    }

    public SecHisSingalQuota(String sDesc, java.util.ArrayList<BEC.SecHisMonthQuotaInfo> vInfo, String sSelectName)
    {
        this.sDesc = sDesc;
        this.vInfo = vInfo;
        this.sSelectName = sSelectName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDesc) {
            ostream.writeString(0, sDesc);
        }
        if (null != vInfo) {
            ostream.writeList(1, vInfo);
        }
        if (null != sSelectName) {
            ostream.writeString(2, sSelectName);
        }
    }

    static java.util.ArrayList<BEC.SecHisMonthQuotaInfo> VAR_TYPE_4_VINFO = new java.util.ArrayList<BEC.SecHisMonthQuotaInfo>();
    static {
        VAR_TYPE_4_VINFO.add(new BEC.SecHisMonthQuotaInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDesc = (String)istream.readString(0, false, this.sDesc);
        this.vInfo = (java.util.ArrayList<BEC.SecHisMonthQuotaInfo>)istream.readList(1, false, VAR_TYPE_4_VINFO);
        this.sSelectName = (String)istream.readString(2, false, this.sSelectName);
    }

}

