package BEC;

public final class TrendCacheValue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.TrendDesc> vTrendDesc = null;

    public int iFlushTime = 0;

    public java.util.ArrayList<BEC.TrendDesc> getVTrendDesc()
    {
        return vTrendDesc;
    }

    public void  setVTrendDesc(java.util.ArrayList<BEC.TrendDesc> vTrendDesc)
    {
        this.vTrendDesc = vTrendDesc;
    }

    public int getIFlushTime()
    {
        return iFlushTime;
    }

    public void  setIFlushTime(int iFlushTime)
    {
        this.iFlushTime = iFlushTime;
    }

    public TrendCacheValue()
    {
    }

    public TrendCacheValue(java.util.ArrayList<BEC.TrendDesc> vTrendDesc, int iFlushTime)
    {
        this.vTrendDesc = vTrendDesc;
        this.iFlushTime = iFlushTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vTrendDesc) {
            ostream.writeList(0, vTrendDesc);
        }
        ostream.writeInt32(1, iFlushTime);
    }

    static java.util.ArrayList<BEC.TrendDesc> VAR_TYPE_4_VTRENDDESC = new java.util.ArrayList<BEC.TrendDesc>();
    static {
        VAR_TYPE_4_VTRENDDESC.add(new BEC.TrendDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vTrendDesc = (java.util.ArrayList<BEC.TrendDesc>)istream.readList(0, false, VAR_TYPE_4_VTRENDDESC);
        this.iFlushTime = (int)istream.readInt32(1, false, this.iFlushTime);
    }

}

