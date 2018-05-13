package BEC;

public final class PlateQuoteReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int ePlateQuoteReqType = 0;

    public int iColype = 0;

    public int iStartxh = 0;

    public int iWantnum = 0;

    public int ePlateQuoteSortType = 0;

    public int eMarketType = 0;

    public byte [] vGuid = null;

    public int getEPlateQuoteReqType()
    {
        return ePlateQuoteReqType;
    }

    public void  setEPlateQuoteReqType(int ePlateQuoteReqType)
    {
        this.ePlateQuoteReqType = ePlateQuoteReqType;
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

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public int getEPlateQuoteSortType()
    {
        return ePlateQuoteSortType;
    }

    public void  setEPlateQuoteSortType(int ePlateQuoteSortType)
    {
        this.ePlateQuoteSortType = ePlateQuoteSortType;
    }

    public int getEMarketType()
    {
        return eMarketType;
    }

    public void  setEMarketType(int eMarketType)
    {
        this.eMarketType = eMarketType;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public PlateQuoteReq()
    {
    }

    public PlateQuoteReq(int ePlateQuoteReqType, int iColype, int iStartxh, int iWantnum, int ePlateQuoteSortType, int eMarketType, byte [] vGuid)
    {
        this.ePlateQuoteReqType = ePlateQuoteReqType;
        this.iColype = iColype;
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
        this.ePlateQuoteSortType = ePlateQuoteSortType;
        this.eMarketType = eMarketType;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, ePlateQuoteReqType);
        ostream.writeInt32(1, iColype);
        ostream.writeInt32(2, iStartxh);
        ostream.writeInt32(3, iWantnum);
        ostream.writeInt32(4, ePlateQuoteSortType);
        ostream.writeInt32(5, eMarketType);
        if (null != vGuid) {
            ostream.writeBytes(6, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.ePlateQuoteReqType = (int)istream.readInt32(0, false, this.ePlateQuoteReqType);
        this.iColype = (int)istream.readInt32(1, false, this.iColype);
        this.iStartxh = (int)istream.readInt32(2, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(3, false, this.iWantnum);
        this.ePlateQuoteSortType = (int)istream.readInt32(4, false, this.ePlateQuoteSortType);
        this.eMarketType = (int)istream.readInt32(5, false, this.eMarketType);
        this.vGuid = (byte [])istream.readBytes(6, false, this.vGuid);
    }

}

