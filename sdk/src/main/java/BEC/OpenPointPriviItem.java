package BEC;

public final class OpenPointPriviItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPriviType = 0;

    public int iOpenDays = 0;

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

    public OpenPointPriviItem()
    {
    }

    public OpenPointPriviItem(int iPriviType, int iOpenDays)
    {
        this.iPriviType = iPriviType;
        this.iOpenDays = iOpenDays;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPriviType);
        ostream.writeInt32(1, iOpenDays);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPriviType = (int)istream.readInt32(0, false, this.iPriviType);
        this.iOpenDays = (int)istream.readInt32(1, false, this.iOpenDays);
    }

}

