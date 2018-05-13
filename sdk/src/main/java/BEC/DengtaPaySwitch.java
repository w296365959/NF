package BEC;

public final class DengtaPaySwitch extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iSwitch = 1;

    public int getISwitch()
    {
        return iSwitch;
    }

    public void  setISwitch(int iSwitch)
    {
        this.iSwitch = iSwitch;
    }

    public DengtaPaySwitch()
    {
    }

    public DengtaPaySwitch(int iSwitch)
    {
        this.iSwitch = iSwitch;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iSwitch);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iSwitch = (int)istream.readInt32(0, false, this.iSwitch);
    }

}

