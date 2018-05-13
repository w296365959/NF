package BEC;

public final class StockRela extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecBaseInfo stSecBaseInfo = null;

    public float fRela = 0;

    public SecBaseInfo getStSecBaseInfo()
    {
        return stSecBaseInfo;
    }

    public void  setStSecBaseInfo(SecBaseInfo stSecBaseInfo)
    {
        this.stSecBaseInfo = stSecBaseInfo;
    }

    public float getFRela()
    {
        return fRela;
    }

    public void  setFRela(float fRela)
    {
        this.fRela = fRela;
    }

    public StockRela()
    {
    }

    public StockRela(SecBaseInfo stSecBaseInfo, float fRela)
    {
        this.stSecBaseInfo = stSecBaseInfo;
        this.fRela = fRela;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecBaseInfo) {
            ostream.writeMessage(0, stSecBaseInfo);
        }
        ostream.writeFloat(1, fRela);
    }

    static SecBaseInfo VAR_TYPE_4_STSECBASEINFO = new SecBaseInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecBaseInfo = (SecBaseInfo)istream.readMessage(0, false, VAR_TYPE_4_STSECBASEINFO);
        this.fRela = (float)istream.readFloat(1, false, this.fRela);
    }

}

