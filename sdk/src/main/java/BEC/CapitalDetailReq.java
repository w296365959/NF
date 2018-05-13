package BEC;

public final class CapitalDetailReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eSetType = 0;

    public int iColype = 0;

    public int iStartxh = 0;

    public int iNum = 0;

    public int eSortType = 0;

    public int eDataType = 0;

    public byte [] vGuid = null;

    public int getESetType()
    {
        return eSetType;
    }

    public void  setESetType(int eSetType)
    {
        this.eSetType = eSetType;
    }

    public int getIColype()
    {
        return iColype;
    }

    public void  setIColype(int iColype)
    {
        this.iColype = iColype;
    }

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getINum()
    {
        return iNum;
    }

    public void  setINum(int iNum)
    {
        this.iNum = iNum;
    }

    public int getESortType()
    {
        return eSortType;
    }

    public void  setESortType(int eSortType)
    {
        this.eSortType = eSortType;
    }

    public int getEDataType()
    {
        return eDataType;
    }

    public void  setEDataType(int eDataType)
    {
        this.eDataType = eDataType;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public CapitalDetailReq()
    {
    }

    public CapitalDetailReq(int eSetType, int iColype, int iStartxh, int iNum, int eSortType, int eDataType, byte [] vGuid)
    {
        this.eSetType = eSetType;
        this.iColype = iColype;
        this.iStartxh = iStartxh;
        this.iNum = iNum;
        this.eSortType = eSortType;
        this.eDataType = eDataType;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eSetType);
        ostream.writeInt32(1, iColype);
        ostream.writeInt32(2, iStartxh);
        ostream.writeInt32(3, iNum);
        ostream.writeInt32(4, eSortType);
        ostream.writeInt32(5, eDataType);
        if (null != vGuid) {
            ostream.writeBytes(6, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eSetType = (int)istream.readInt32(0, false, this.eSetType);
        this.iColype = (int)istream.readInt32(1, false, this.iColype);
        this.iStartxh = (int)istream.readInt32(2, false, this.iStartxh);
        this.iNum = (int)istream.readInt32(3, false, this.iNum);
        this.eSortType = (int)istream.readInt32(4, false, this.eSortType);
        this.eDataType = (int)istream.readInt32(5, false, this.eDataType);
        this.vGuid = (byte [])istream.readBytes(6, false, this.vGuid);
    }

}

