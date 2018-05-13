package BEC;

public final class FavorNews extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sNewsID = "";

    public int eNewsType = 0;

    public BEC.NewsDesc stNewsDesc = null;

    public int eSecType = 0;

    public int iTimeStamp = -1;

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

    public BEC.NewsDesc getStNewsDesc()
    {
        return stNewsDesc;
    }

    public void  setStNewsDesc(BEC.NewsDesc stNewsDesc)
    {
        this.stNewsDesc = stNewsDesc;
    }

    public int getESecType()
    {
        return eSecType;
    }

    public void  setESecType(int eSecType)
    {
        this.eSecType = eSecType;
    }

    public int getITimeStamp()
    {
        return iTimeStamp;
    }

    public void  setITimeStamp(int iTimeStamp)
    {
        this.iTimeStamp = iTimeStamp;
    }

    public FavorNews()
    {
    }

    public FavorNews(String sNewsID, int eNewsType, BEC.NewsDesc stNewsDesc, int eSecType, int iTimeStamp)
    {
        this.sNewsID = sNewsID;
        this.eNewsType = eNewsType;
        this.stNewsDesc = stNewsDesc;
        this.eSecType = eSecType;
        this.iTimeStamp = iTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sNewsID) {
            ostream.writeString(0, sNewsID);
        }
        ostream.writeInt32(1, eNewsType);
        if (null != stNewsDesc) {
            ostream.writeMessage(3, stNewsDesc);
        }
        ostream.writeInt32(4, eSecType);
        ostream.writeInt32(5, iTimeStamp);
    }

    static BEC.NewsDesc VAR_TYPE_4_STNEWSDESC = new BEC.NewsDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sNewsID = (String)istream.readString(0, false, this.sNewsID);
        this.eNewsType = (int)istream.readInt32(1, false, this.eNewsType);
        this.stNewsDesc = (BEC.NewsDesc)istream.readMessage(3, false, VAR_TYPE_4_STNEWSDESC);
        this.eSecType = (int)istream.readInt32(4, false, this.eSecType);
        this.iTimeStamp = (int)istream.readInt32(5, false, this.iTimeStamp);
    }

}

