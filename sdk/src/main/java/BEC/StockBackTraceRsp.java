package BEC;

public final class StockBackTraceRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.StockBackTraceRst stMaxIncRst = null;

    public BEC.StockBackTraceRst stMaxWinRst = null;

    public BEC.StockBackTraceRst getStMaxIncRst()
    {
        return stMaxIncRst;
    }

    public void  setStMaxIncRst(BEC.StockBackTraceRst stMaxIncRst)
    {
        this.stMaxIncRst = stMaxIncRst;
    }

    public BEC.StockBackTraceRst getStMaxWinRst()
    {
        return stMaxWinRst;
    }

    public void  setStMaxWinRst(BEC.StockBackTraceRst stMaxWinRst)
    {
        this.stMaxWinRst = stMaxWinRst;
    }

    public StockBackTraceRsp()
    {
    }

    public StockBackTraceRsp(BEC.StockBackTraceRst stMaxIncRst, BEC.StockBackTraceRst stMaxWinRst)
    {
        this.stMaxIncRst = stMaxIncRst;
        this.stMaxWinRst = stMaxWinRst;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stMaxIncRst) {
            ostream.writeMessage(0, stMaxIncRst);
        }
        if (null != stMaxWinRst) {
            ostream.writeMessage(1, stMaxWinRst);
        }
    }

    static BEC.StockBackTraceRst VAR_TYPE_4_STMAXINCRST = new BEC.StockBackTraceRst();

    static BEC.StockBackTraceRst VAR_TYPE_4_STMAXWINRST = new BEC.StockBackTraceRst();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stMaxIncRst = (BEC.StockBackTraceRst)istream.readMessage(0, false, VAR_TYPE_4_STMAXINCRST);
        this.stMaxWinRst = (BEC.StockBackTraceRst)istream.readMessage(1, false, VAR_TYPE_4_STMAXWINRST);
    }

}

