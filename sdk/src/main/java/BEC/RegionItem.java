package BEC;

public final class RegionItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sRegionName = "";

    public SecCoordsInfo stCoords = null;

    public int iCompanyNum = 0;

    public String getSRegionName()
    {
        return sRegionName;
    }

    public void  setSRegionName(String sRegionName)
    {
        this.sRegionName = sRegionName;
    }

    public SecCoordsInfo getStCoords()
    {
        return stCoords;
    }

    public void  setStCoords(SecCoordsInfo stCoords)
    {
        this.stCoords = stCoords;
    }

    public int getICompanyNum()
    {
        return iCompanyNum;
    }

    public void  setICompanyNum(int iCompanyNum)
    {
        this.iCompanyNum = iCompanyNum;
    }

    public RegionItem()
    {
    }

    public RegionItem(String sRegionName, SecCoordsInfo stCoords, int iCompanyNum)
    {
        this.sRegionName = sRegionName;
        this.stCoords = stCoords;
        this.iCompanyNum = iCompanyNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sRegionName) {
            ostream.writeString(0, sRegionName);
        }
        if (null != stCoords) {
            ostream.writeMessage(1, stCoords);
        }
        ostream.writeInt32(2, iCompanyNum);
    }

    static SecCoordsInfo VAR_TYPE_4_STCOORDS = new SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sRegionName = (String)istream.readString(0, false, this.sRegionName);
        this.stCoords = (SecCoordsInfo)istream.readMessage(1, false, VAR_TYPE_4_STCOORDS);
        this.iCompanyNum = (int)istream.readInt32(2, false, this.iCompanyNum);
    }

}

