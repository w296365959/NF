package BEC;

public final class ConfigDetail extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iType = 0;

    public byte [] vData = null;

    public int iVersion = 0;

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public byte [] getVData()
    {
        return vData;
    }

    public void  setVData(byte [] vData)
    {
        this.vData = vData;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public ConfigDetail()
    {
    }

    public ConfigDetail(int iType, byte [] vData, int iVersion)
    {
        this.iType = iType;
        this.vData = vData;
        this.iVersion = iVersion;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iType);
        if (null != vData) {
            ostream.writeBytes(1, vData);
        }
        ostream.writeInt32(2, iVersion);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iType = (int)istream.readInt32(0, false, this.iType);
        this.vData = (byte [])istream.readBytes(1, false, this.vData);
        this.iVersion = (int)istream.readInt32(2, false, this.iVersion);
    }

}

