package BEC;

public final class RtMinReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iNum = 1;

    public String sDate = "";

    public int iMinute = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getINum()
    {
        return iNum;
    }

    public void  setINum(int iNum)
    {
        this.iNum = iNum;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getIMinute()
    {
        return iMinute;
    }

    public void  setIMinute(int iMinute)
    {
        this.iMinute = iMinute;
    }

    public RtMinReq()
    {
    }

    public RtMinReq(String sDtSecCode, int iNum, String sDate, int iMinute)
    {
        this.sDtSecCode = sDtSecCode;
        this.iNum = iNum;
        this.sDate = sDate;
        this.iMinute = iMinute;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iNum);
        if (null != sDate) {
            ostream.writeString(2, sDate);
        }
        ostream.writeInt32(3, iMinute);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iNum = (int)istream.readInt32(1, false, this.iNum);
        this.sDate = (String)istream.readString(2, false, this.sDate);
        this.iMinute = (int)istream.readInt32(3, false, this.iMinute);
    }

}

