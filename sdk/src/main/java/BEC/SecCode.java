package BEC;

public final class SecCode extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecCode = "";

    public int eMarketType = 0;

    public int eSecType = 0;

    public String getSSecCode()
    {
        return sSecCode;
    }

    public void  setSSecCode(String sSecCode)
    {
        this.sSecCode = sSecCode;
    }

    public int getEMarketType()
    {
        return eMarketType;
    }

    public void  setEMarketType(int eMarketType)
    {
        this.eMarketType = eMarketType;
    }

    public int getESecType()
    {
        return eSecType;
    }

    public void  setESecType(int eSecType)
    {
        this.eSecType = eSecType;
    }

    public SecCode()
    {
    }

    public SecCode(String sSecCode, int eMarketType, int eSecType)
    {
        this.sSecCode = sSecCode;
        this.eMarketType = eMarketType;
        this.eSecType = eSecType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecCode) {
            ostream.writeString(0, sSecCode);
        }
        ostream.writeInt32(1, eMarketType);
        ostream.writeInt32(2, eSecType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecCode = (String)istream.readString(0, false, this.sSecCode);
        this.eMarketType = (int)istream.readInt32(1, false, this.eMarketType);
        this.eSecType = (int)istream.readInt32(2, false, this.eSecType);
    }

}

