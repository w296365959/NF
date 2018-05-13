package BEC;

public final class SecCodeDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecCode = "";

    public String sSecName = "";

    public String getSSecCode()
    {
        return sSecCode;
    }

    public void  setSSecCode(String sSecCode)
    {
        this.sSecCode = sSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public SecCodeDetail()
    {
    }

    public SecCodeDetail(String sSecCode, String sSecName)
    {
        this.sSecCode = sSecCode;
        this.sSecName = sSecName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecCode) {
            ostream.writeString(0, sSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecCode = (String)istream.readString(0, false, this.sSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
    }

}

