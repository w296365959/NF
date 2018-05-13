package BEC;

public final class NewsSimple extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sNewsID = "";

    public int eNewsType = 0;

    public int eSecType = 0;

    public String getSNewsID()
    {
        return sNewsID;
    }

    public void  setSNewsID(String sNewsID)
    {
        this.sNewsID = sNewsID;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public int getESecType()
    {
        return eSecType;
    }

    public void  setESecType(int eSecType)
    {
        this.eSecType = eSecType;
    }

    public NewsSimple()
    {
    }

    public NewsSimple(String sNewsID, int eNewsType, int eSecType)
    {
        this.sNewsID = sNewsID;
        this.eNewsType = eNewsType;
        this.eSecType = eSecType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sNewsID) {
            ostream.writeString(0, sNewsID);
        }
        ostream.writeInt32(1, eNewsType);
        ostream.writeInt32(2, eSecType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sNewsID = (String)istream.readString(0, false, this.sNewsID);
        this.eNewsType = (int)istream.readInt32(1, false, this.eNewsType);
        this.eSecType = (int)istream.readInt32(2, false, this.eSecType);
    }

}

