package BEC;

public final class AccuPointDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDesc = "";

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public AccuPointDesc()
    {
    }

    public AccuPointDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDesc) {
            ostream.writeString(0, sDesc);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDesc = (String)istream.readString(0, false, this.sDesc);
    }

}

