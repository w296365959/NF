package BEC;

public final class GetConsultStockInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.IAConsultStock stIAConsultStock = null;

    public BEC.IAConsultStock getStIAConsultStock()
    {
        return stIAConsultStock;
    }

    public void  setStIAConsultStock(BEC.IAConsultStock stIAConsultStock)
    {
        this.stIAConsultStock = stIAConsultStock;
    }

    public GetConsultStockInfoRsp()
    {
    }

    public GetConsultStockInfoRsp(BEC.IAConsultStock stIAConsultStock)
    {
        this.stIAConsultStock = stIAConsultStock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stIAConsultStock) {
            ostream.writeMessage(0, stIAConsultStock);
        }
    }

    static BEC.IAConsultStock VAR_TYPE_4_STIACONSULTSTOCK = new BEC.IAConsultStock();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stIAConsultStock = (BEC.IAConsultStock)istream.readMessage(0, false, VAR_TYPE_4_STIACONSULTSTOCK);
    }

}

