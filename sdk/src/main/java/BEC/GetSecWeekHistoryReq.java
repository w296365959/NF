package BEC;

public final class GetSecWeekHistoryReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iYear = 0;

    public int iWeek = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public int getIWeek()
    {
        return iWeek;
    }

    public void  setIWeek(int iWeek)
    {
        this.iWeek = iWeek;
    }

    public GetSecWeekHistoryReq()
    {
    }

    public GetSecWeekHistoryReq(String sDtSecCode, int iYear, int iWeek)
    {
        this.sDtSecCode = sDtSecCode;
        this.iYear = iYear;
        this.iWeek = iWeek;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iYear);
        ostream.writeInt32(2, iWeek);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iYear = (int)istream.readInt32(1, false, this.iYear);
        this.iWeek = (int)istream.readInt32(2, false, this.iWeek);
    }

}

