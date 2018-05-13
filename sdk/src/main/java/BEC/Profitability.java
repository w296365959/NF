package BEC;

public final class Profitability extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sYear = "";

    public float fEPS = 0;

    public float fROE = 0;

    public String getSYear()
    {
        return sYear;
    }

    public void  setSYear(String sYear)
    {
        this.sYear = sYear;
    }

    public float getFEPS()
    {
        return fEPS;
    }

    public void  setFEPS(float fEPS)
    {
        this.fEPS = fEPS;
    }

    public float getFROE()
    {
        return fROE;
    }

    public void  setFROE(float fROE)
    {
        this.fROE = fROE;
    }

    public Profitability()
    {
    }

    public Profitability(String sYear, float fEPS, float fROE)
    {
        this.sYear = sYear;
        this.fEPS = fEPS;
        this.fROE = fROE;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sYear) {
            ostream.writeString(0, sYear);
        }
        ostream.writeFloat(1, fEPS);
        ostream.writeFloat(2, fROE);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sYear = (String)istream.readString(0, false, this.sYear);
        this.fEPS = (float)istream.readFloat(1, false, this.fEPS);
        this.fROE = (float)istream.readFloat(2, false, this.fROE);
    }

}

