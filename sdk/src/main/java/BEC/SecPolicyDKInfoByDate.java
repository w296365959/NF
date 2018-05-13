package BEC;

public final class SecPolicyDKInfoByDate extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eDK = 0;

    public int iInDate = 0;

    public float fPrice = 0;

    public float fInPrice = 0;

    public float fOutPrice = 0;

    public float fDKValue = 0;

    public int getEDK()
    {
        return eDK;
    }

    public void  setEDK(int eDK)
    {
        this.eDK = eDK;
    }

    public int getIInDate()
    {
        return iInDate;
    }

    public void  setIInDate(int iInDate)
    {
        this.iInDate = iInDate;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public float getFInPrice()
    {
        return fInPrice;
    }

    public void  setFInPrice(float fInPrice)
    {
        this.fInPrice = fInPrice;
    }

    public float getFOutPrice()
    {
        return fOutPrice;
    }

    public void  setFOutPrice(float fOutPrice)
    {
        this.fOutPrice = fOutPrice;
    }

    public float getFDKValue()
    {
        return fDKValue;
    }

    public void  setFDKValue(float fDKValue)
    {
        this.fDKValue = fDKValue;
    }

    public SecPolicyDKInfoByDate()
    {
    }

    public SecPolicyDKInfoByDate(int eDK, int iInDate, float fPrice, float fInPrice, float fOutPrice, float fDKValue)
    {
        this.eDK = eDK;
        this.iInDate = iInDate;
        this.fPrice = fPrice;
        this.fInPrice = fInPrice;
        this.fOutPrice = fOutPrice;
        this.fDKValue = fDKValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eDK);
        ostream.writeInt32(1, iInDate);
        ostream.writeFloat(2, fPrice);
        ostream.writeFloat(3, fInPrice);
        ostream.writeFloat(4, fOutPrice);
        ostream.writeFloat(5, fDKValue);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eDK = (int)istream.readInt32(0, false, this.eDK);
        this.iInDate = (int)istream.readInt32(1, false, this.iInDate);
        this.fPrice = (float)istream.readFloat(2, false, this.fPrice);
        this.fInPrice = (float)istream.readFloat(3, false, this.fInPrice);
        this.fOutPrice = (float)istream.readFloat(4, false, this.fOutPrice);
        this.fDKValue = (float)istream.readFloat(5, false, this.fDKValue);
    }

}

