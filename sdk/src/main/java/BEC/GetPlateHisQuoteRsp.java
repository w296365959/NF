package BEC;

public final class GetPlateHisQuoteRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<PlateHisCycleInfo> vHisInfo = null;

    public java.util.ArrayList<PlateHisCycleInfo> getVHisInfo()
    {
        return vHisInfo;
    }

    public void  setVHisInfo(java.util.ArrayList<PlateHisCycleInfo> vHisInfo)
    {
        this.vHisInfo = vHisInfo;
    }

    public GetPlateHisQuoteRsp()
    {
    }

    public GetPlateHisQuoteRsp(java.util.ArrayList<PlateHisCycleInfo> vHisInfo)
    {
        this.vHisInfo = vHisInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vHisInfo) {
            ostream.writeList(0, vHisInfo);
        }
    }

    static java.util.ArrayList<PlateHisCycleInfo> VAR_TYPE_4_VHISINFO = new java.util.ArrayList<PlateHisCycleInfo>();
    static {
        VAR_TYPE_4_VHISINFO.add(new PlateHisCycleInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vHisInfo = (java.util.ArrayList<PlateHisCycleInfo>)istream.readList(0, false, VAR_TYPE_4_VHISINFO);
    }

}

