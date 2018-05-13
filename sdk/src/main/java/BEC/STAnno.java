package BEC;

public final class STAnno extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public java.util.ArrayList<String> vtPositiveDate = null;

    public java.util.ArrayList<String> vtNegativeDate = null;

    public String sAnnoDesc = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public java.util.ArrayList<String> getVtPositiveDate()
    {
        return vtPositiveDate;
    }

    public void  setVtPositiveDate(java.util.ArrayList<String> vtPositiveDate)
    {
        this.vtPositiveDate = vtPositiveDate;
    }

    public java.util.ArrayList<String> getVtNegativeDate()
    {
        return vtNegativeDate;
    }

    public void  setVtNegativeDate(java.util.ArrayList<String> vtNegativeDate)
    {
        this.vtNegativeDate = vtNegativeDate;
    }

    public String getSAnnoDesc()
    {
        return sAnnoDesc;
    }

    public void  setSAnnoDesc(String sAnnoDesc)
    {
        this.sAnnoDesc = sAnnoDesc;
    }

    public STAnno()
    {
    }

    public STAnno(String sDtSecCode, java.util.ArrayList<String> vtPositiveDate, java.util.ArrayList<String> vtNegativeDate, String sAnnoDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.vtPositiveDate = vtPositiveDate;
        this.vtNegativeDate = vtNegativeDate;
        this.sAnnoDesc = sAnnoDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != vtPositiveDate) {
            ostream.writeList(1, vtPositiveDate);
        }
        if (null != vtNegativeDate) {
            ostream.writeList(2, vtNegativeDate);
        }
        if (null != sAnnoDesc) {
            ostream.writeString(3, sAnnoDesc);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTPOSITIVEDATE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTPOSITIVEDATE.add("");
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTNEGATIVEDATE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTNEGATIVEDATE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.vtPositiveDate = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VTPOSITIVEDATE);
        this.vtNegativeDate = (java.util.ArrayList<String>)istream.readList(2, false, VAR_TYPE_4_VTNEGATIVEDATE);
        this.sAnnoDesc = (String)istream.readString(3, false, this.sAnnoDesc);
    }

}

