package BEC;

public final class GetHSTradingStatusRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, Integer> mTradingStatus = null;

    public java.util.Map<String, Integer> getMTradingStatus()
    {
        return mTradingStatus;
    }

    public void  setMTradingStatus(java.util.Map<String, Integer> mTradingStatus)
    {
        this.mTradingStatus = mTradingStatus;
    }

    public GetHSTradingStatusRsp()
    {
    }

    public GetHSTradingStatusRsp(java.util.Map<String, Integer> mTradingStatus)
    {
        this.mTradingStatus = mTradingStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mTradingStatus) {
            ostream.writeMap(0, mTradingStatus);
        }
    }

    static java.util.Map<String, Integer> VAR_TYPE_4_MTRADINGSTATUS = new java.util.HashMap<String, Integer>();
    static {
        VAR_TYPE_4_MTRADINGSTATUS.put("", 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mTradingStatus = (java.util.Map<String, Integer>)istream.readMap(0, false, VAR_TYPE_4_MTRADINGSTATUS);
    }

}

