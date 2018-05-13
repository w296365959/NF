package BEC;

public final class ConditionPickStrategyListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.ArrayList<ConditionPickStrategy> vtConditionPickStrategy = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.ArrayList<ConditionPickStrategy> getVtConditionPickStrategy()
    {
        return vtConditionPickStrategy;
    }

    public void  setVtConditionPickStrategy(java.util.ArrayList<ConditionPickStrategy> vtConditionPickStrategy)
    {
        this.vtConditionPickStrategy = vtConditionPickStrategy;
    }

    public ConditionPickStrategyListRsp()
    {
    }

    public ConditionPickStrategyListRsp(int iRet, java.util.ArrayList<ConditionPickStrategy> vtConditionPickStrategy)
    {
        this.iRet = iRet;
        this.vtConditionPickStrategy = vtConditionPickStrategy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != vtConditionPickStrategy) {
            ostream.writeList(1, vtConditionPickStrategy);
        }
    }

    static java.util.ArrayList<ConditionPickStrategy> VAR_TYPE_4_VTCONDITIONPICKSTRATEGY = new java.util.ArrayList<ConditionPickStrategy>();
    static {
        VAR_TYPE_4_VTCONDITIONPICKSTRATEGY.add(new ConditionPickStrategy());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.vtConditionPickStrategy = (java.util.ArrayList<ConditionPickStrategy>)istream.readList(1, false, VAR_TYPE_4_VTCONDITIONPICKSTRATEGY);
    }

}

