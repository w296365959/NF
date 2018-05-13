package BEC;

public final class CityInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sCityName = "";

    public BEC.SecCoordsInfo stCoords = null;

    public String getSCityName()
    {
        return sCityName;
    }

    public void  setSCityName(String sCityName)
    {
        this.sCityName = sCityName;
    }

    public BEC.SecCoordsInfo getStCoords()
    {
        return stCoords;
    }

    public void  setStCoords(BEC.SecCoordsInfo stCoords)
    {
        this.stCoords = stCoords;
    }

    public CityInfo()
    {
    }

    public CityInfo(String sCityName, BEC.SecCoordsInfo stCoords)
    {
        this.sCityName = sCityName;
        this.stCoords = stCoords;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sCityName) {
            ostream.writeString(0, sCityName);
        }
        if (null != stCoords) {
            ostream.writeMessage(1, stCoords);
        }
    }

    static BEC.SecCoordsInfo VAR_TYPE_4_STCOORDS = new BEC.SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sCityName = (String)istream.readString(0, false, this.sCityName);
        this.stCoords = (BEC.SecCoordsInfo)istream.readMessage(1, false, VAR_TYPE_4_STCOORDS);
    }

}

