package BEC;

public final class AccuPointPriviInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPriviType = 0;

    public String sPriviEndDay = "";

    public int getIPriviType()
    {
        return iPriviType;
    }

    public void  setIPriviType(int iPriviType)
    {
        this.iPriviType = iPriviType;
    }

    public String getSPriviEndDay()
    {
        return sPriviEndDay;
    }

    public void  setSPriviEndDay(String sPriviEndDay)
    {
        this.sPriviEndDay = sPriviEndDay;
    }

    public AccuPointPriviInfo()
    {
    }

    public AccuPointPriviInfo(int iPriviType, String sPriviEndDay)
    {
        this.iPriviType = iPriviType;
        this.sPriviEndDay = sPriviEndDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPriviType);
        if (null != sPriviEndDay) {
            ostream.writeString(1, sPriviEndDay);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPriviType = (int)istream.readInt32(0, false, this.iPriviType);
        this.sPriviEndDay = (String)istream.readString(1, false, this.sPriviEndDay);
    }

}

