package BEC;

public final class NewsId extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTime = "";

    public String sId = "";

    public int eNewsType = 0;

    public int eMarketType = 0;

    public int eSecType = 0;

    public int eDataDbSource = 0;

    public int eNewsTable = 0;

    public String getSTime()
    {
        return sTime;
    }

    public void  setSTime(String sTime)
    {
        this.sTime = sTime;
    }

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
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

    public int getEDataDbSource()
    {
        return eDataDbSource;
    }

    public void  setEDataDbSource(int eDataDbSource)
    {
        this.eDataDbSource = eDataDbSource;
    }

    public int getENewsTable()
    {
        return eNewsTable;
    }

    public void  setENewsTable(int eNewsTable)
    {
        this.eNewsTable = eNewsTable;
    }

    public NewsId()
    {
    }

    public NewsId(String sTime, String sId, int eNewsType, int eMarketType, int eSecType, int eDataDbSource, int eNewsTable)
    {
        this.sTime = sTime;
        this.sId = sId;
        this.eNewsType = eNewsType;
        this.eMarketType = eMarketType;
        this.eSecType = eSecType;
        this.eDataDbSource = eDataDbSource;
        this.eNewsTable = eNewsTable;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTime) {
            ostream.writeString(0, sTime);
        }
        if (null != sId) {
            ostream.writeString(1, sId);
        }
        ostream.writeInt32(2, eNewsType);
        ostream.writeInt32(3, eMarketType);
        ostream.writeInt32(4, eSecType);
        ostream.writeInt32(5, eDataDbSource);
        ostream.writeInt32(6, eNewsTable);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTime = (String)istream.readString(0, false, this.sTime);
        this.sId = (String)istream.readString(1, false, this.sId);
        this.eNewsType = (int)istream.readInt32(2, false, this.eNewsType);
        this.eMarketType = (int)istream.readInt32(3, false, this.eMarketType);
        this.eSecType = (int)istream.readInt32(4, false, this.eSecType);
        this.eDataDbSource = (int)istream.readInt32(5, false, this.eDataDbSource);
        this.eNewsTable = (int)istream.readInt32(6, false, this.eNewsTable);
    }

}

