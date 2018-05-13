package BEC;

public final class SecCoordsInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sLongitude = "";

    public String sLatitude = "";

    public String getSLongitude()
    {
        return sLongitude;
    }

    public void  setSLongitude(String sLongitude)
    {
        this.sLongitude = sLongitude;
    }

    public String getSLatitude()
    {
        return sLatitude;
    }

    public void  setSLatitude(String sLatitude)
    {
        this.sLatitude = sLatitude;
    }

    public SecCoordsInfo()
    {
    }

    public SecCoordsInfo(String sLongitude, String sLatitude)
    {
        this.sLongitude = sLongitude;
        this.sLatitude = sLatitude;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sLongitude) {
            ostream.writeString(0, sLongitude);
        }
        if (null != sLatitude) {
            ostream.writeString(1, sLatitude);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sLongitude = (String)istream.readString(0, false, this.sLongitude);
        this.sLatitude = (String)istream.readString(1, false, this.sLatitude);
    }

}

