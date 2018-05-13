package BEC;

public final class VodVideoMonthListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> sMonth = null;

    public java.util.ArrayList<String> getSMonth()
    {
        return sMonth;
    }

    public void  setSMonth(java.util.ArrayList<String> sMonth)
    {
        this.sMonth = sMonth;
    }

    public VodVideoMonthListRsp()
    {
    }

    public VodVideoMonthListRsp(java.util.ArrayList<String> sMonth)
    {
        this.sMonth = sMonth;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sMonth) {
            ostream.writeList(0, sMonth);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_SMONTH = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_SMONTH.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sMonth = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_SMONTH);
    }

}

