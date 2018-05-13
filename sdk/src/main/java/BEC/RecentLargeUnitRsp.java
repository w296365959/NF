package BEC;

public final class RecentLargeUnitRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, BEC.RecentLargeUnit> mLargeUnitDesc = null;

    public java.util.Map<String, BEC.RecentLargeUnit> getMLargeUnitDesc()
    {
        return mLargeUnitDesc;
    }

    public void  setMLargeUnitDesc(java.util.Map<String, BEC.RecentLargeUnit> mLargeUnitDesc)
    {
        this.mLargeUnitDesc = mLargeUnitDesc;
    }

    public RecentLargeUnitRsp()
    {
    }

    public RecentLargeUnitRsp(java.util.Map<String, BEC.RecentLargeUnit> mLargeUnitDesc)
    {
        this.mLargeUnitDesc = mLargeUnitDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mLargeUnitDesc) {
            ostream.writeMap(0, mLargeUnitDesc);
        }
    }

    static java.util.Map<String, BEC.RecentLargeUnit> VAR_TYPE_4_MLARGEUNITDESC = new java.util.HashMap<String, BEC.RecentLargeUnit>();
    static {
        VAR_TYPE_4_MLARGEUNITDESC.put("", new BEC.RecentLargeUnit());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mLargeUnitDesc = (java.util.Map<String, BEC.RecentLargeUnit>)istream.readMap(0, false, VAR_TYPE_4_MLARGEUNITDESC);
    }

}

