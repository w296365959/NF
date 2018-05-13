package BEC;

public final class QueryStareStrategyRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.StareStrategyClass> vClass = null;

    public java.util.ArrayList<BEC.StareStrategyClass> vBroadcastTime = null;

    public java.util.ArrayList<BEC.StareStrategyClass> getVClass()
    {
        return vClass;
    }

    public void  setVClass(java.util.ArrayList<BEC.StareStrategyClass> vClass)
    {
        this.vClass = vClass;
    }

    public java.util.ArrayList<BEC.StareStrategyClass> getVBroadcastTime()
    {
        return vBroadcastTime;
    }

    public void  setVBroadcastTime(java.util.ArrayList<BEC.StareStrategyClass> vBroadcastTime)
    {
        this.vBroadcastTime = vBroadcastTime;
    }

    public QueryStareStrategyRsp()
    {
    }

    public QueryStareStrategyRsp(java.util.ArrayList<BEC.StareStrategyClass> vClass, java.util.ArrayList<BEC.StareStrategyClass> vBroadcastTime)
    {
        this.vClass = vClass;
        this.vBroadcastTime = vBroadcastTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vClass) {
            ostream.writeList(0, vClass);
        }
        if (null != vBroadcastTime) {
            ostream.writeList(1, vBroadcastTime);
        }
    }

    static java.util.ArrayList<BEC.StareStrategyClass> VAR_TYPE_4_VCLASS = new java.util.ArrayList<BEC.StareStrategyClass>();
    static {
        VAR_TYPE_4_VCLASS.add(new BEC.StareStrategyClass());
    }

    static java.util.ArrayList<BEC.StareStrategyClass> VAR_TYPE_4_VBROADCASTTIME = new java.util.ArrayList<BEC.StareStrategyClass>();
    static {
        VAR_TYPE_4_VBROADCASTTIME.add(new BEC.StareStrategyClass());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vClass = (java.util.ArrayList<BEC.StareStrategyClass>)istream.readList(0, false, VAR_TYPE_4_VCLASS);
        this.vBroadcastTime = (java.util.ArrayList<BEC.StareStrategyClass>)istream.readList(1, false, VAR_TYPE_4_VBROADCASTTIME);
    }

}

