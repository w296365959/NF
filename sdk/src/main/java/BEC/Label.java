package BEC;

public final class Label extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iType = 0;

    public int iID = 0;

    public String sName = "";

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public Label()
    {
    }

    public Label(int iType, int iID, String sName)
    {
        this.iType = iType;
        this.iID = iID;
        this.sName = sName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iType);
        ostream.writeInt32(1, iID);
        if (null != sName) {
            ostream.writeString(2, sName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iType = (int)istream.readInt32(0, false, this.iType);
        this.iID = (int)istream.readInt32(1, false, this.iID);
        this.sName = (String)istream.readString(2, false, this.sName);
    }

}

