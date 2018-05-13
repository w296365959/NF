package BEC;

public final class AHPlateReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStartxh = 0;

    public int iWantnum = 0;

    public int iColype = 0;

    public int eSortType = 0;

    public byte [] vGuid = null;

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public int getIColype()
    {
        return iColype;
    }

    public void  setIColype(int iColype)
    {
        this.iColype = iColype;
    }

    public int getESortType()
    {
        return eSortType;
    }

    public void  setESortType(int eSortType)
    {
        this.eSortType = eSortType;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public AHPlateReq()
    {
    }

    public AHPlateReq(int iStartxh, int iWantnum, int iColype, int eSortType, byte [] vGuid)
    {
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
        this.iColype = iColype;
        this.eSortType = eSortType;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStartxh);
        ostream.writeInt32(1, iWantnum);
        ostream.writeInt32(2, iColype);
        ostream.writeInt32(3, eSortType);
        if (null != vGuid) {
            ostream.writeBytes(4, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStartxh = (int)istream.readInt32(0, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(1, false, this.iWantnum);
        this.iColype = (int)istream.readInt32(2, false, this.iColype);
        this.eSortType = (int)istream.readInt32(3, false, this.eSortType);
        this.vGuid = (byte [])istream.readBytes(4, false, this.vGuid);
    }

}

