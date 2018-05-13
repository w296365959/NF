package BEC;

public final class CtrlKey extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sKey = "";

    public String sUUID = "";

    public String getSKey()
    {
        return sKey;
    }

    public void  setSKey(String sKey)
    {
        this.sKey = sKey;
    }

    public String getSUUID()
    {
        return sUUID;
    }

    public void  setSUUID(String sUUID)
    {
        this.sUUID = sUUID;
    }

    public CtrlKey()
    {
    }

    public CtrlKey(String sKey, String sUUID)
    {
        this.sKey = sKey;
        this.sUUID = sUUID;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sKey) {
            ostream.writeString(0, sKey);
        }
        if (null != sUUID) {
            ostream.writeString(1, sUUID);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sKey = (String)istream.readString(0, false, this.sKey);
        this.sUUID = (String)istream.readString(1, false, this.sUUID);
    }

}

