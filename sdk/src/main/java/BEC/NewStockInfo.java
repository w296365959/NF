package BEC;

public final class NewStockInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iNumber = 0;

    public String sDay = "";

    public int iLotResNum = 0;

    public int getINumber()
    {
        return iNumber;
    }

    public void  setINumber(int iNumber)
    {
        this.iNumber = iNumber;
    }

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public int getILotResNum()
    {
        return iLotResNum;
    }

    public void  setILotResNum(int iLotResNum)
    {
        this.iLotResNum = iLotResNum;
    }

    public NewStockInfo()
    {
    }

    public NewStockInfo(int iNumber, String sDay, int iLotResNum)
    {
        this.iNumber = iNumber;
        this.sDay = sDay;
        this.iLotResNum = iLotResNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iNumber);
        if (null != sDay) {
            ostream.writeString(1, sDay);
        }
        ostream.writeInt32(2, iLotResNum);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iNumber = (int)istream.readInt32(0, false, this.iNumber);
        this.sDay = (String)istream.readString(1, false, this.sDay);
        this.iLotResNum = (int)istream.readInt32(2, false, this.iLotResNum);
    }

}

