package BEC;

public final class PayOrderList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, BEC.PayInfo> mPayInfo = null;

    public java.util.Map<String, BEC.PayInfo> getMPayInfo()
    {
        return mPayInfo;
    }

    public void  setMPayInfo(java.util.Map<String, BEC.PayInfo> mPayInfo)
    {
        this.mPayInfo = mPayInfo;
    }

    public PayOrderList()
    {
    }

    public PayOrderList(java.util.Map<String, BEC.PayInfo> mPayInfo)
    {
        this.mPayInfo = mPayInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mPayInfo) {
            ostream.writeMap(0, mPayInfo);
        }
    }

    static java.util.Map<String, BEC.PayInfo> VAR_TYPE_4_MPAYINFO = new java.util.HashMap<String, BEC.PayInfo>();
    static {
        VAR_TYPE_4_MPAYINFO.put("", new BEC.PayInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mPayInfo = (java.util.Map<String, BEC.PayInfo>)istream.readMap(0, false, VAR_TYPE_4_MPAYINFO);
    }

}

