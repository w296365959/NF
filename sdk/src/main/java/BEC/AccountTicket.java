package BEC;

public final class AccountTicket extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public byte [] vtTicket = null;

    public String sTicket = "";

    public byte [] getVtTicket()
    {
        return vtTicket;
    }

    public void  setVtTicket(byte [] vtTicket)
    {
        this.vtTicket = vtTicket;
    }

    public String getSTicket()
    {
        return sTicket;
    }

    public void  setSTicket(String sTicket)
    {
        this.sTicket = sTicket;
    }

    public AccountTicket()
    {
    }

    public AccountTicket(byte [] vtTicket, String sTicket)
    {
        this.vtTicket = vtTicket;
        this.sTicket = sTicket;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtTicket) {
            ostream.writeBytes(0, vtTicket);
        }
        if (null != sTicket) {
            ostream.writeString(1, sTicket);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtTicket = (byte [])istream.readBytes(0, false, this.vtTicket);
        this.sTicket = (String)istream.readString(1, false, this.sTicket);
    }

}

