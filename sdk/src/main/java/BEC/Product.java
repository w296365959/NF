package BEC;

public final class Product extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public Product()
    {
    }

    public Product(String sName)
    {
        this.sName = sName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
    }

}

