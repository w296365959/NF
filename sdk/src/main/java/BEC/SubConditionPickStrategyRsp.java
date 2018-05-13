package BEC;

public final class SubConditionPickStrategyRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public int eSubResult = 0;

    public ConditionPickStrategy stConditionPickStrategy = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public int getESubResult()
    {
        return eSubResult;
    }

    public void  setESubResult(int eSubResult)
    {
        this.eSubResult = eSubResult;
    }

    public ConditionPickStrategy getStConditionPickStrategy()
    {
        return stConditionPickStrategy;
    }

    public void  setStConditionPickStrategy(ConditionPickStrategy stConditionPickStrategy)
    {
        this.stConditionPickStrategy = stConditionPickStrategy;
    }

    public SubConditionPickStrategyRsp()
    {
    }

    public SubConditionPickStrategyRsp(int iRet, int eSubResult, ConditionPickStrategy stConditionPickStrategy)
    {
        this.iRet = iRet;
        this.eSubResult = eSubResult;
        this.stConditionPickStrategy = stConditionPickStrategy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        ostream.writeInt32(1, eSubResult);
        if (null != stConditionPickStrategy) {
            ostream.writeMessage(2, stConditionPickStrategy);
        }
    }

    static ConditionPickStrategy VAR_TYPE_4_STCONDITIONPICKSTRATEGY = new ConditionPickStrategy();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.eSubResult = (int)istream.readInt32(1, false, this.eSubResult);
        this.stConditionPickStrategy = (ConditionPickStrategy)istream.readMessage(2, false, VAR_TYPE_4_STCONDITIONPICKSTRATEGY);
    }

}

