package BEC;

public final class RecomItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eRecomType = 0;

    public String sKey = "";

    public byte [] vData = null;

    public int getERecomType()
    {
        return eRecomType;
    }

    public void  setERecomType(int eRecomType)
    {
        this.eRecomType = eRecomType;
    }

    public String getSKey()
    {
        return sKey;
    }

    public void  setSKey(String sKey)
    {
        this.sKey = sKey;
    }

    public byte [] getVData()
    {
        return vData;
    }

    public void  setVData(byte [] vData)
    {
        this.vData = vData;
    }

    public RecomItem()
    {
    }

    public RecomItem(int eRecomType, String sKey, byte [] vData)
    {
        this.eRecomType = eRecomType;
        this.sKey = sKey;
        this.vData = vData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eRecomType);
        if (null != sKey) {
            ostream.writeString(1, sKey);
        }
        if (null != vData) {
            ostream.writeBytes(2, vData);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eRecomType = (int)istream.readInt32(0, false, this.eRecomType);
        this.sKey = (String)istream.readString(1, false, this.sKey);
        this.vData = (byte [])istream.readBytes(2, false, this.vData);
    }

}

