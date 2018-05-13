package BEC;

public final class SecUpdowns extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public String sWeekUd = "";

    public String sMonthUd = "";

    public String sThreeMonthUd = "";

    public String sHalfYearUd = "";

    public String sYearUd = "";

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSWeekUd()
    {
        return sWeekUd;
    }

    public void  setSWeekUd(String sWeekUd)
    {
        this.sWeekUd = sWeekUd;
    }

    public String getSMonthUd()
    {
        return sMonthUd;
    }

    public void  setSMonthUd(String sMonthUd)
    {
        this.sMonthUd = sMonthUd;
    }

    public String getSThreeMonthUd()
    {
        return sThreeMonthUd;
    }

    public void  setSThreeMonthUd(String sThreeMonthUd)
    {
        this.sThreeMonthUd = sThreeMonthUd;
    }

    public String getSHalfYearUd()
    {
        return sHalfYearUd;
    }

    public void  setSHalfYearUd(String sHalfYearUd)
    {
        this.sHalfYearUd = sHalfYearUd;
    }

    public String getSYearUd()
    {
        return sYearUd;
    }

    public void  setSYearUd(String sYearUd)
    {
        this.sYearUd = sYearUd;
    }

    public SecUpdowns()
    {
    }

    public SecUpdowns(String sSecName, String sDtSecCode, String sWeekUd, String sMonthUd, String sThreeMonthUd, String sHalfYearUd, String sYearUd)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.sWeekUd = sWeekUd;
        this.sMonthUd = sMonthUd;
        this.sThreeMonthUd = sThreeMonthUd;
        this.sHalfYearUd = sHalfYearUd;
        this.sYearUd = sYearUd;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecName) {
            ostream.writeString(0, sSecName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sWeekUd) {
            ostream.writeString(2, sWeekUd);
        }
        if (null != sMonthUd) {
            ostream.writeString(3, sMonthUd);
        }
        if (null != sThreeMonthUd) {
            ostream.writeString(4, sThreeMonthUd);
        }
        if (null != sHalfYearUd) {
            ostream.writeString(5, sHalfYearUd);
        }
        if (null != sYearUd) {
            ostream.writeString(6, sYearUd);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sWeekUd = (String)istream.readString(2, false, this.sWeekUd);
        this.sMonthUd = (String)istream.readString(3, false, this.sMonthUd);
        this.sThreeMonthUd = (String)istream.readString(4, false, this.sThreeMonthUd);
        this.sHalfYearUd = (String)istream.readString(5, false, this.sHalfYearUd);
        this.sYearUd = (String)istream.readString(6, false, this.sYearUd);
    }

}

