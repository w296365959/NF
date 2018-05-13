package BEC;

public final class SecLHHisQuota extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sMainTitle = "";

    public java.util.ArrayList<BEC.SecHisSingalQuota> vQuota = null;

    public String sBannerUrl = "";

    public String sBannerDesc = "";

    public int iType = 0;

    public String sBannerSkipUrl = "";

    public String getSMainTitle()
    {
        return sMainTitle;
    }

    public void  setSMainTitle(String sMainTitle)
    {
        this.sMainTitle = sMainTitle;
    }

    public java.util.ArrayList<BEC.SecHisSingalQuota> getVQuota()
    {
        return vQuota;
    }

    public void  setVQuota(java.util.ArrayList<BEC.SecHisSingalQuota> vQuota)
    {
        this.vQuota = vQuota;
    }

    public String getSBannerUrl()
    {
        return sBannerUrl;
    }

    public void  setSBannerUrl(String sBannerUrl)
    {
        this.sBannerUrl = sBannerUrl;
    }

    public String getSBannerDesc()
    {
        return sBannerDesc;
    }

    public void  setSBannerDesc(String sBannerDesc)
    {
        this.sBannerDesc = sBannerDesc;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public String getSBannerSkipUrl()
    {
        return sBannerSkipUrl;
    }

    public void  setSBannerSkipUrl(String sBannerSkipUrl)
    {
        this.sBannerSkipUrl = sBannerSkipUrl;
    }

    public SecLHHisQuota()
    {
    }

    public SecLHHisQuota(String sMainTitle, java.util.ArrayList<BEC.SecHisSingalQuota> vQuota, String sBannerUrl, String sBannerDesc, int iType, String sBannerSkipUrl)
    {
        this.sMainTitle = sMainTitle;
        this.vQuota = vQuota;
        this.sBannerUrl = sBannerUrl;
        this.sBannerDesc = sBannerDesc;
        this.iType = iType;
        this.sBannerSkipUrl = sBannerSkipUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sMainTitle) {
            ostream.writeString(0, sMainTitle);
        }
        if (null != vQuota) {
            ostream.writeList(1, vQuota);
        }
        if (null != sBannerUrl) {
            ostream.writeString(2, sBannerUrl);
        }
        if (null != sBannerDesc) {
            ostream.writeString(3, sBannerDesc);
        }
        ostream.writeInt32(4, iType);
        if (null != sBannerSkipUrl) {
            ostream.writeString(5, sBannerSkipUrl);
        }
    }

    static java.util.ArrayList<BEC.SecHisSingalQuota> VAR_TYPE_4_VQUOTA = new java.util.ArrayList<BEC.SecHisSingalQuota>();
    static {
        VAR_TYPE_4_VQUOTA.add(new BEC.SecHisSingalQuota());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sMainTitle = (String)istream.readString(0, false, this.sMainTitle);
        this.vQuota = (java.util.ArrayList<BEC.SecHisSingalQuota>)istream.readList(1, false, VAR_TYPE_4_VQUOTA);
        this.sBannerUrl = (String)istream.readString(2, false, this.sBannerUrl);
        this.sBannerDesc = (String)istream.readString(3, false, this.sBannerDesc);
        this.iType = (int)istream.readInt32(4, false, this.iType);
        this.sBannerSkipUrl = (String)istream.readString(5, false, this.sBannerSkipUrl);
    }

}

