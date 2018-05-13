package BEC;

public final class ConcStockListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.RelateSecInfo> vRelateSecInfo = null;

    public java.util.ArrayList<BEC.RelateSecInfo> getVRelateSecInfo()
    {
        return vRelateSecInfo;
    }

    public void  setVRelateSecInfo(java.util.ArrayList<BEC.RelateSecInfo> vRelateSecInfo)
    {
        this.vRelateSecInfo = vRelateSecInfo;
    }

    public ConcStockListRsp()
    {
    }

    public ConcStockListRsp(java.util.ArrayList<BEC.RelateSecInfo> vRelateSecInfo)
    {
        this.vRelateSecInfo = vRelateSecInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vRelateSecInfo) {
            ostream.writeList(0, vRelateSecInfo);
        }
    }

    static java.util.ArrayList<BEC.RelateSecInfo> VAR_TYPE_4_VRELATESECINFO = new java.util.ArrayList<BEC.RelateSecInfo>();
    static {
        VAR_TYPE_4_VRELATESECINFO.add(new BEC.RelateSecInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vRelateSecInfo = (java.util.ArrayList<BEC.RelateSecInfo>)istream.readList(0, false, VAR_TYPE_4_VRELATESECINFO);
    }

}

