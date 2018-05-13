package BEC;

public final class SecHisWeekQuota extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecHisWeekQuotaInfo> vWeekInfo = null;

    public java.util.ArrayList<BEC.SecHisWeekQuotaInfo> getVWeekInfo()
    {
        return vWeekInfo;
    }

    public void  setVWeekInfo(java.util.ArrayList<BEC.SecHisWeekQuotaInfo> vWeekInfo)
    {
        this.vWeekInfo = vWeekInfo;
    }

    public SecHisWeekQuota()
    {
    }

    public SecHisWeekQuota(java.util.ArrayList<BEC.SecHisWeekQuotaInfo> vWeekInfo)
    {
        this.vWeekInfo = vWeekInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vWeekInfo) {
            ostream.writeList(0, vWeekInfo);
        }
    }

    static java.util.ArrayList<BEC.SecHisWeekQuotaInfo> VAR_TYPE_4_VWEEKINFO = new java.util.ArrayList<BEC.SecHisWeekQuotaInfo>();
    static {
        VAR_TYPE_4_VWEEKINFO.add(new BEC.SecHisWeekQuotaInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vWeekInfo = (java.util.ArrayList<BEC.SecHisWeekQuotaInfo>)istream.readList(0, false, VAR_TYPE_4_VWEEKINFO);
    }

}

