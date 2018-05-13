package BEC;

public final class TradingTimeRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iNow = 0;

    public java.util.ArrayList<BEC.TradingTimeDesc> vTradingTimeDesc = null;

    public int getINow()
    {
        return iNow;
    }

    public void  setINow(int iNow)
    {
        this.iNow = iNow;
    }

    public java.util.ArrayList<BEC.TradingTimeDesc> getVTradingTimeDesc()
    {
        return vTradingTimeDesc;
    }

    public void  setVTradingTimeDesc(java.util.ArrayList<BEC.TradingTimeDesc> vTradingTimeDesc)
    {
        this.vTradingTimeDesc = vTradingTimeDesc;
    }

    public TradingTimeRsp()
    {
    }

    public TradingTimeRsp(int iNow, java.util.ArrayList<BEC.TradingTimeDesc> vTradingTimeDesc)
    {
        this.iNow = iNow;
        this.vTradingTimeDesc = vTradingTimeDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iNow);
        if (null != vTradingTimeDesc) {
            ostream.writeList(1, vTradingTimeDesc);
        }
    }

    static java.util.ArrayList<BEC.TradingTimeDesc> VAR_TYPE_4_VTRADINGTIMEDESC = new java.util.ArrayList<BEC.TradingTimeDesc>();
    static {
        VAR_TYPE_4_VTRADINGTIMEDESC.add(new BEC.TradingTimeDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iNow = (int)istream.readInt32(0, false, this.iNow);
        this.vTradingTimeDesc = (java.util.ArrayList<BEC.TradingTimeDesc>)istream.readList(1, false, VAR_TYPE_4_VTRADINGTIMEDESC);
    }

}

