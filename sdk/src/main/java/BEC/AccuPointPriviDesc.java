package BEC;

public final class AccuPointPriviDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPriviType = 0;

    public String sName = "";

    public String sDesc = "";

    public int iNeedPoints = 0;

    public String sIcon = "";

    public int getIPriviType()
    {
        return iPriviType;
    }

    public void  setIPriviType(int iPriviType)
    {
        this.iPriviType = iPriviType;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public int getINeedPoints()
    {
        return iNeedPoints;
    }

    public void  setINeedPoints(int iNeedPoints)
    {
        this.iNeedPoints = iNeedPoints;
    }

    public String getSIcon()
    {
        return sIcon;
    }

    public void  setSIcon(String sIcon)
    {
        this.sIcon = sIcon;
    }

    public AccuPointPriviDesc()
    {
    }

    public AccuPointPriviDesc(int iPriviType, String sName, String sDesc, int iNeedPoints, String sIcon)
    {
        this.iPriviType = iPriviType;
        this.sName = sName;
        this.sDesc = sDesc;
        this.iNeedPoints = iNeedPoints;
        this.sIcon = sIcon;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPriviType);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sDesc) {
            ostream.writeString(2, sDesc);
        }
        ostream.writeInt32(4, iNeedPoints);
        if (null != sIcon) {
            ostream.writeString(6, sIcon);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPriviType = (int)istream.readInt32(0, false, this.iPriviType);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sDesc = (String)istream.readString(2, false, this.sDesc);
        this.iNeedPoints = (int)istream.readInt32(4, false, this.iNeedPoints);
        this.sIcon = (String)istream.readString(6, false, this.sIcon);
    }

}

