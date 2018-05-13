package BEC;

public final class PriviExchangeDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPriviType = 0;

    public int iPriviPoints = 0;

    public int iExchangeDays = 0;

    public String sTitleDesc = "";

    public String sPriviDesc = "";

    public String sPriviIcon = "";

    public int getIPriviType()
    {
        return iPriviType;
    }

    public void  setIPriviType(int iPriviType)
    {
        this.iPriviType = iPriviType;
    }

    public int getIPriviPoints()
    {
        return iPriviPoints;
    }

    public void  setIPriviPoints(int iPriviPoints)
    {
        this.iPriviPoints = iPriviPoints;
    }

    public int getIExchangeDays()
    {
        return iExchangeDays;
    }

    public void  setIExchangeDays(int iExchangeDays)
    {
        this.iExchangeDays = iExchangeDays;
    }

    public String getSTitleDesc()
    {
        return sTitleDesc;
    }

    public void  setSTitleDesc(String sTitleDesc)
    {
        this.sTitleDesc = sTitleDesc;
    }

    public String getSPriviDesc()
    {
        return sPriviDesc;
    }

    public void  setSPriviDesc(String sPriviDesc)
    {
        this.sPriviDesc = sPriviDesc;
    }

    public String getSPriviIcon()
    {
        return sPriviIcon;
    }

    public void  setSPriviIcon(String sPriviIcon)
    {
        this.sPriviIcon = sPriviIcon;
    }

    public PriviExchangeDesc()
    {
    }

    public PriviExchangeDesc(int iPriviType, int iPriviPoints, int iExchangeDays, String sTitleDesc, String sPriviDesc, String sPriviIcon)
    {
        this.iPriviType = iPriviType;
        this.iPriviPoints = iPriviPoints;
        this.iExchangeDays = iExchangeDays;
        this.sTitleDesc = sTitleDesc;
        this.sPriviDesc = sPriviDesc;
        this.sPriviIcon = sPriviIcon;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPriviType);
        ostream.writeInt32(1, iPriviPoints);
        ostream.writeInt32(2, iExchangeDays);
        if (null != sTitleDesc) {
            ostream.writeString(3, sTitleDesc);
        }
        if (null != sPriviDesc) {
            ostream.writeString(4, sPriviDesc);
        }
        if (null != sPriviIcon) {
            ostream.writeString(5, sPriviIcon);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPriviType = (int)istream.readInt32(0, false, this.iPriviType);
        this.iPriviPoints = (int)istream.readInt32(1, false, this.iPriviPoints);
        this.iExchangeDays = (int)istream.readInt32(2, false, this.iExchangeDays);
        this.sTitleDesc = (String)istream.readString(3, false, this.sTitleDesc);
        this.sPriviDesc = (String)istream.readString(4, false, this.sPriviDesc);
        this.sPriviIcon = (String)istream.readString(5, false, this.sPriviIcon);
    }

}

