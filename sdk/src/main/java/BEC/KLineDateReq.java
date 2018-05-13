package BEC;

public final class KLineDateReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int eKLineType = 0;

    public int iWantnum = 0;

    public String sBeginDate = "";

    public String sEndDate = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getEKLineType()
    {
        return eKLineType;
    }

    public void  setEKLineType(int eKLineType)
    {
        this.eKLineType = eKLineType;
    }

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public String getSBeginDate()
    {
        return sBeginDate;
    }

    public void  setSBeginDate(String sBeginDate)
    {
        this.sBeginDate = sBeginDate;
    }

    public String getSEndDate()
    {
        return sEndDate;
    }

    public void  setSEndDate(String sEndDate)
    {
        this.sEndDate = sEndDate;
    }

    public KLineDateReq()
    {
    }

    public KLineDateReq(String sDtSecCode, int eKLineType, int iWantnum, String sBeginDate, String sEndDate)
    {
        this.sDtSecCode = sDtSecCode;
        this.eKLineType = eKLineType;
        this.iWantnum = iWantnum;
        this.sBeginDate = sBeginDate;
        this.sEndDate = sEndDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, eKLineType);
        ostream.writeInt32(2, iWantnum);
        if (null != sBeginDate) {
            ostream.writeString(3, sBeginDate);
        }
        if (null != sEndDate) {
            ostream.writeString(4, sEndDate);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.eKLineType = (int)istream.readInt32(1, false, this.eKLineType);
        this.iWantnum = (int)istream.readInt32(2, false, this.iWantnum);
        this.sBeginDate = (String)istream.readString(3, false, this.sBeginDate);
        this.sEndDate = (String)istream.readString(4, false, this.sEndDate);
    }

}

