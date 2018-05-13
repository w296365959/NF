package BEC;

public final class FeedInvestListHead extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iListType = 0;

    public String sListName = "";

    public int getIListType()
    {
        return iListType;
    }

    public void  setIListType(int iListType)
    {
        this.iListType = iListType;
    }

    public String getSListName()
    {
        return sListName;
    }

    public void  setSListName(String sListName)
    {
        this.sListName = sListName;
    }

    public FeedInvestListHead()
    {
    }

    public FeedInvestListHead(int iListType, String sListName)
    {
        this.iListType = iListType;
        this.sListName = sListName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iListType);
        if (null != sListName) {
            ostream.writeString(1, sListName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iListType = (int)istream.readInt32(0, false, this.iListType);
        this.sListName = (String)istream.readString(1, false, this.sListName);
    }

}

