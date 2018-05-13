package BEC;

public final class RtMinDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public java.util.ArrayList<BEC.TrendDesc> vTrendDesc = null;

    public float fPreClose = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public java.util.ArrayList<BEC.TrendDesc> getVTrendDesc()
    {
        return vTrendDesc;
    }

    public void  setVTrendDesc(java.util.ArrayList<BEC.TrendDesc> vTrendDesc)
    {
        this.vTrendDesc = vTrendDesc;
    }

    public float getFPreClose()
    {
        return fPreClose;
    }

    public void  setFPreClose(float fPreClose)
    {
        this.fPreClose = fPreClose;
    }

    public RtMinDesc()
    {
    }

    public RtMinDesc(String sDate, java.util.ArrayList<BEC.TrendDesc> vTrendDesc, float fPreClose)
    {
        this.sDate = sDate;
        this.vTrendDesc = vTrendDesc;
        this.fPreClose = fPreClose;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        if (null != vTrendDesc) {
            ostream.writeList(1, vTrendDesc);
        }
        ostream.writeFloat(2, fPreClose);
    }

    static java.util.ArrayList<BEC.TrendDesc> VAR_TYPE_4_VTRENDDESC = new java.util.ArrayList<BEC.TrendDesc>();
    static {
        VAR_TYPE_4_VTRENDDESC.add(new BEC.TrendDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.vTrendDesc = (java.util.ArrayList<BEC.TrendDesc>)istream.readList(1, false, VAR_TYPE_4_VTRENDDESC);
        this.fPreClose = (float)istream.readFloat(2, false, this.fPreClose);
    }

}

