package BEC;

public final class SKLStatisticsRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SKLStatisticsInfo> vtSKLStatisticsInfo = null;

    public java.util.ArrayList<BEC.SKLStatisticsInfo> getVtSKLStatisticsInfo()
    {
        return vtSKLStatisticsInfo;
    }

    public void  setVtSKLStatisticsInfo(java.util.ArrayList<BEC.SKLStatisticsInfo> vtSKLStatisticsInfo)
    {
        this.vtSKLStatisticsInfo = vtSKLStatisticsInfo;
    }

    public SKLStatisticsRsp()
    {
    }

    public SKLStatisticsRsp(java.util.ArrayList<BEC.SKLStatisticsInfo> vtSKLStatisticsInfo)
    {
        this.vtSKLStatisticsInfo = vtSKLStatisticsInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSKLStatisticsInfo) {
            ostream.writeList(0, vtSKLStatisticsInfo);
        }
    }

    static java.util.ArrayList<BEC.SKLStatisticsInfo> VAR_TYPE_4_VTSKLSTATISTICSINFO = new java.util.ArrayList<BEC.SKLStatisticsInfo>();
    static {
        VAR_TYPE_4_VTSKLSTATISTICSINFO.add(new BEC.SKLStatisticsInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSKLStatisticsInfo = (java.util.ArrayList<BEC.SKLStatisticsInfo>)istream.readList(0, false, VAR_TYPE_4_VTSKLSTATISTICSINFO);
    }

}

