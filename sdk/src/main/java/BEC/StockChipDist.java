package BEC;

public final class StockChipDist extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fPrice = 0;

    public float fPer = 0;

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public float getFPer()
    {
        return fPer;
    }

    public void  setFPer(float fPer)
    {
        this.fPer = fPer;
    }

    public StockChipDist()
    {
    }

    public StockChipDist(float fPrice, float fPer)
    {
        this.fPrice = fPrice;
        this.fPer = fPer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fPrice);
        ostream.writeFloat(1, fPer);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fPrice = (float)istream.readFloat(0, false, this.fPrice);
        this.fPer = (float)istream.readFloat(1, false, this.fPer);
    }

}

