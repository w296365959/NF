package BEC;

public final class VideoCenterCacheKey extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sKey = "";

    public String getSKey()
    {
        return sKey;
    }

    public void  setSKey(String sKey)
    {
        this.sKey = sKey;
    }

    public VideoCenterCacheKey()
    {
    }

    public VideoCenterCacheKey(String sKey)
    {
        this.sKey = sKey;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sKey) {
            ostream.writeString(0, sKey);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sKey = (String)istream.readString(0, false, this.sKey);
    }

}

