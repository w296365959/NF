package BEC;

public final class GetDiscBannerRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BannerInfo> vBannerInfo = null;

    public java.util.ArrayList<BannerInfo> getVBannerInfo()
    {
        return vBannerInfo;
    }

    public void  setVBannerInfo(java.util.ArrayList<BannerInfo> vBannerInfo)
    {
        this.vBannerInfo = vBannerInfo;
    }

    public GetDiscBannerRsp()
    {
    }

    public GetDiscBannerRsp(java.util.ArrayList<BannerInfo> vBannerInfo)
    {
        this.vBannerInfo = vBannerInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vBannerInfo) {
            ostream.writeList(0, vBannerInfo);
        }
    }

    static java.util.ArrayList<BannerInfo> VAR_TYPE_4_VBANNERINFO = new java.util.ArrayList<BannerInfo>();
    static {
        VAR_TYPE_4_VBANNERINFO.add(new BannerInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vBannerInfo = (java.util.ArrayList<BannerInfo>)istream.readList(0, false, VAR_TYPE_4_VBANNERINFO);
    }

}

