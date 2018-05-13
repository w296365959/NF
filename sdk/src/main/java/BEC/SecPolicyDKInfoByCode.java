package BEC;

public final class SecPolicyDKInfoByCode extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecCodeDetail stSecCodeDetail = null;

    public float fPrice = 0;

    public float fDKValue = 0;

    public float fInPrice = 0;

    public float fOutPrice = 0;

    public SecCodeDetail getStSecCodeDetail()
    {
        return stSecCodeDetail;
    }

    public void  setStSecCodeDetail(SecCodeDetail stSecCodeDetail)
    {
        this.stSecCodeDetail = stSecCodeDetail;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public float getFDKValue()
    {
        return fDKValue;
    }

    public void  setFDKValue(float fDKValue)
    {
        this.fDKValue = fDKValue;
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

    public SecPolicyDKInfoByCode()
    {
    }

    public SecPolicyDKInfoByCode(SecCodeDetail stSecCodeDetail, float fPrice, float fDKValue, float fInPrice, float fOutPrice)
    {
        this.stSecCodeDetail = stSecCodeDetail;
        this.fPrice = fPrice;
        this.fDKValue = fDKValue;
        this.fInPrice = fInPrice;
        this.fOutPrice = fOutPrice;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecCodeDetail) {
            ostream.writeMessage(0, stSecCodeDetail);
        }
        ostream.writeFloat(1, fPrice);
        ostream.writeFloat(2, fDKValue);
        ostream.writeFloat(3, fInPrice);
        ostream.writeFloat(4, fOutPrice);
    }

    static SecCodeDetail VAR_TYPE_4_STSECCODEDETAIL = new SecCodeDetail();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecCodeDetail = (SecCodeDetail)istream.readMessage(0, false, VAR_TYPE_4_STSECCODEDETAIL);
        this.fPrice = (float)istream.readFloat(1, false, this.fPrice);
        this.fDKValue = (float)istream.readFloat(2, false, this.fDKValue);
        this.fInPrice = (float)istream.readFloat(3, false, this.fInPrice);
        this.fOutPrice = (float)istream.readFloat(4, false, this.fOutPrice);
    }

}

