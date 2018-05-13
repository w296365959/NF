package BEC;

public final class AdditionLiftedRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.AdditionStockLifted stAdditionStockLifted = null;

    public BEC.AdditionStockLifted getStAdditionStockLifted()
    {
        return stAdditionStockLifted;
    }

    public void  setStAdditionStockLifted(BEC.AdditionStockLifted stAdditionStockLifted)
    {
        this.stAdditionStockLifted = stAdditionStockLifted;
    }

    public AdditionLiftedRsp()
    {
    }

    public AdditionLiftedRsp(BEC.AdditionStockLifted stAdditionStockLifted)
    {
        this.stAdditionStockLifted = stAdditionStockLifted;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stAdditionStockLifted) {
            ostream.writeMessage(0, stAdditionStockLifted);
        }
    }

    static BEC.AdditionStockLifted VAR_TYPE_4_STADDITIONSTOCKLIFTED = new BEC.AdditionStockLifted();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stAdditionStockLifted = (BEC.AdditionStockLifted)istream.readMessage(0, false, VAR_TYPE_4_STADDITIONSTOCKLIFTED);
    }

}

