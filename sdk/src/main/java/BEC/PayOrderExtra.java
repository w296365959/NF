package BEC;

public final class PayOrderExtra extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sClassId = "";

    public String getSClassId()
    {
        return sClassId;
    }

    public void  setSClassId(String sClassId)
    {
        this.sClassId = sClassId;
    }

    public PayOrderExtra()
    {
    }

    public PayOrderExtra(String sClassId)
    {
        this.sClassId = sClassId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sClassId) {
            ostream.writeString(0, sClassId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sClassId = (String)istream.readString(0, false, this.sClassId);
    }

}

