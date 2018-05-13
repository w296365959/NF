package BEC;

public final class GetGoldenStockSwitchRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public boolean bOpen = true;

    public boolean getBOpen()
    {
        return bOpen;
    }

    public void  setBOpen(boolean bOpen)
    {
        this.bOpen = bOpen;
    }

    public GetGoldenStockSwitchRsp()
    {
    }

    public GetGoldenStockSwitchRsp(boolean bOpen)
    {
        this.bOpen = bOpen;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeBoolean(0, bOpen);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.bOpen = (boolean)istream.readBoolean(0, false, this.bOpen);
    }

}

