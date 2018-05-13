package BEC;

public final class LotNumResult extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDescriptio = "";

    public String sNumber = "";

    public String getSDescriptio()
    {
        return sDescriptio;
    }

    public void  setSDescriptio(String sDescriptio)
    {
        this.sDescriptio = sDescriptio;
    }

    public String getSNumber()
    {
        return sNumber;
    }

    public void  setSNumber(String sNumber)
    {
        this.sNumber = sNumber;
    }

    public LotNumResult()
    {
    }

    public LotNumResult(String sDescriptio, String sNumber)
    {
        this.sDescriptio = sDescriptio;
        this.sNumber = sNumber;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDescriptio) {
            ostream.writeString(0, sDescriptio);
        }
        if (null != sNumber) {
            ostream.writeString(1, sNumber);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDescriptio = (String)istream.readString(0, false, this.sDescriptio);
        this.sNumber = (String)istream.readString(1, false, this.sNumber);
    }

}

