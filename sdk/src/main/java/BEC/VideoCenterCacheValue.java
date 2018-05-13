package BEC;

public final class VideoCenterCacheValue extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sValue = "";

    public String getSValue()
    {
        return sValue;
    }

    public void  setSValue(String sValue)
    {
        this.sValue = sValue;
    }

    public VideoCenterCacheValue()
    {
    }

    public VideoCenterCacheValue(String sValue)
    {
        this.sValue = sValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sValue) {
            ostream.writeString(0, sValue);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sValue = (String)istream.readString(0, false, this.sValue);
    }

}

