package BEC;

public final class DividendPayingPlacing extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sYear = "";

    public String sDetail = "";

    public String sDate = "";

    public String getSYear()
    {
        return sYear;
    }

    public void  setSYear(String sYear)
    {
        this.sYear = sYear;
    }

    public String getSDetail()
    {
        return sDetail;
    }

    public void  setSDetail(String sDetail)
    {
        this.sDetail = sDetail;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public DividendPayingPlacing()
    {
    }

    public DividendPayingPlacing(String sYear, String sDetail, String sDate)
    {
        this.sYear = sYear;
        this.sDetail = sDetail;
        this.sDate = sDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sYear) {
            ostream.writeString(0, sYear);
        }
        if (null != sDetail) {
            ostream.writeString(1, sDetail);
        }
        if (null != sDate) {
            ostream.writeString(2, sDate);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sYear = (String)istream.readString(0, false, this.sYear);
        this.sDetail = (String)istream.readString(1, false, this.sDetail);
        this.sDate = (String)istream.readString(2, false, this.sDate);
    }

}

