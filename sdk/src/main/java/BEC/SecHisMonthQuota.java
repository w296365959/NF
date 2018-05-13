package BEC;

public final class SecHisMonthQuota extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecHisMonthQuotaInfo> vMonthInfo = null;

    public java.util.ArrayList<BEC.SecHisMonthQuotaInfo> getVMonthInfo()
    {
        return vMonthInfo;
    }

    public void  setVMonthInfo(java.util.ArrayList<BEC.SecHisMonthQuotaInfo> vMonthInfo)
    {
        this.vMonthInfo = vMonthInfo;
    }

    public SecHisMonthQuota()
    {
    }

    public SecHisMonthQuota(java.util.ArrayList<BEC.SecHisMonthQuotaInfo> vMonthInfo)
    {
        this.vMonthInfo = vMonthInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vMonthInfo) {
            ostream.writeList(0, vMonthInfo);
        }
    }

    static java.util.ArrayList<BEC.SecHisMonthQuotaInfo> VAR_TYPE_4_VMONTHINFO = new java.util.ArrayList<BEC.SecHisMonthQuotaInfo>();
    static {
        VAR_TYPE_4_VMONTHINFO.add(new BEC.SecHisMonthQuotaInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vMonthInfo = (java.util.ArrayList<BEC.SecHisMonthQuotaInfo>)istream.readList(0, false, VAR_TYPE_4_VMONTHINFO);
    }

}

