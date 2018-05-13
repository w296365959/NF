package BEC;

public final class IntelliPickStockDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public IntelliPickStockEx stIntelliPickStockEx = null;

    public IntelliPickStockEx getStIntelliPickStockEx()
    {
        return stIntelliPickStockEx;
    }

    public void  setStIntelliPickStockEx(IntelliPickStockEx stIntelliPickStockEx)
    {
        this.stIntelliPickStockEx = stIntelliPickStockEx;
    }

    public IntelliPickStockDetailRsp()
    {
    }

    public IntelliPickStockDetailRsp(IntelliPickStockEx stIntelliPickStockEx)
    {
        this.stIntelliPickStockEx = stIntelliPickStockEx;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stIntelliPickStockEx) {
            ostream.writeMessage(0, stIntelliPickStockEx);
        }
    }

    static IntelliPickStockEx VAR_TYPE_4_STINTELLIPICKSTOCKEX = new IntelliPickStockEx();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stIntelliPickStockEx = (IntelliPickStockEx)istream.readMessage(0, false, VAR_TYPE_4_STINTELLIPICKSTOCKEX);
    }

}

