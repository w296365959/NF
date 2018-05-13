package BEC;

public final class OpenPriviDetailItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iOpenType = 0;

    public int iPriviType = 0;

    public int iOpenDays = 0;

    public String sPriviEndDay = "";

    public long lTimeStamp = 0;

    public int getIOpenType()
    {
        return iOpenType;
    }

    public void  setIOpenType(int iOpenType)
    {
        this.iOpenType = iOpenType;
    }

    public int getIPriviType()
    {
        return iPriviType;
    }

    public void  setIPriviType(int iPriviType)
    {
        this.iPriviType = iPriviType;
    }

    public int getIOpenDays()
    {
        return iOpenDays;
    }

    public void  setIOpenDays(int iOpenDays)
    {
        this.iOpenDays = iOpenDays;
    }

    public String getSPriviEndDay()
    {
        return sPriviEndDay;
    }

    public void  setSPriviEndDay(String sPriviEndDay)
    {
        this.sPriviEndDay = sPriviEndDay;
    }

    public long getLTimeStamp()
    {
        return lTimeStamp;
    }

    public void  setLTimeStamp(long lTimeStamp)
    {
        this.lTimeStamp = lTimeStamp;
    }

    public OpenPriviDetailItem()
    {
    }

    public OpenPriviDetailItem(int iOpenType, int iPriviType, int iOpenDays, String sPriviEndDay, long lTimeStamp)
    {
        this.iOpenType = iOpenType;
        this.iPriviType = iPriviType;
        this.iOpenDays = iOpenDays;
        this.sPriviEndDay = sPriviEndDay;
        this.lTimeStamp = lTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iOpenType);
        ostream.writeInt32(1, iPriviType);
        ostream.writeInt32(2, iOpenDays);
        if (null != sPriviEndDay) {
            ostream.writeString(3, sPriviEndDay);
        }
        ostream.writeInt64(4, lTimeStamp);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iOpenType = (int)istream.readInt32(0, false, this.iOpenType);
        this.iPriviType = (int)istream.readInt32(1, false, this.iPriviType);
        this.iOpenDays = (int)istream.readInt32(2, false, this.iOpenDays);
        this.sPriviEndDay = (String)istream.readString(3, false, this.sPriviEndDay);
        this.lTimeStamp = (long)istream.readInt64(4, false, this.lTimeStamp);
    }

}

