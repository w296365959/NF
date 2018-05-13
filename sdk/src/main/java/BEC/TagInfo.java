package BEC;

public final class TagInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTagName = "";

    public int eTagType = 0;

    public int eAttiType = 0;

    public String getSTagName()
    {
        return sTagName;
    }

    public void  setSTagName(String sTagName)
    {
        this.sTagName = sTagName;
    }

    public int getETagType()
    {
        return eTagType;
    }

    public void  setETagType(int eTagType)
    {
        this.eTagType = eTagType;
    }

    public int getEAttiType()
    {
        return eAttiType;
    }

    public void  setEAttiType(int eAttiType)
    {
        this.eAttiType = eAttiType;
    }

    public TagInfo()
    {
    }

    public TagInfo(String sTagName, int eTagType, int eAttiType)
    {
        this.sTagName = sTagName;
        this.eTagType = eTagType;
        this.eAttiType = eAttiType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTagName) {
            ostream.writeString(0, sTagName);
        }
        ostream.writeInt32(1, eTagType);
        ostream.writeInt32(2, eAttiType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTagName = (String)istream.readString(0, false, this.sTagName);
        this.eTagType = (int)istream.readInt32(1, false, this.eTagType);
        this.eAttiType = (int)istream.readInt32(2, false, this.eAttiType);
    }

}

