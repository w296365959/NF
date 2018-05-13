package BEC;

public final class GetSecMonthTopUpReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iYear = 0;

    public int iMonth = 0;

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public int getIMonth()
    {
        return iMonth;
    }

    public void  setIMonth(int iMonth)
    {
        this.iMonth = iMonth;
    }

    public GetSecMonthTopUpReq()
    {
    }

    public GetSecMonthTopUpReq(int iYear, int iMonth)
    {
        this.iYear = iYear;
        this.iMonth = iMonth;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iYear);
        ostream.writeInt32(1, iMonth);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iYear = (int)istream.readInt32(0, false, this.iYear);
        this.iMonth = (int)istream.readInt32(1, false, this.iMonth);
    }

}

