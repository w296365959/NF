package BEC;

public final class IntelliPickStockDetailV2Rsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public IntelliPickStockV2 stIntelliPickStockV2 = null;

    public IntelliPickStockV2 getStIntelliPickStockV2()
    {
        return stIntelliPickStockV2;
    }

    public void  setStIntelliPickStockV2(IntelliPickStockV2 stIntelliPickStockV2)
    {
        this.stIntelliPickStockV2 = stIntelliPickStockV2;
    }

    public IntelliPickStockDetailV2Rsp()
    {
    }

    public IntelliPickStockDetailV2Rsp(IntelliPickStockV2 stIntelliPickStockV2)
    {
        this.stIntelliPickStockV2 = stIntelliPickStockV2;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stIntelliPickStockV2) {
            ostream.writeMessage(0, stIntelliPickStockV2);
        }
    }

    static IntelliPickStockV2 VAR_TYPE_4_STINTELLIPICKSTOCKV2 = new IntelliPickStockV2();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stIntelliPickStockV2 = (IntelliPickStockV2)istream.readMessage(0, false, VAR_TYPE_4_STINTELLIPICKSTOCKV2);
    }

}

