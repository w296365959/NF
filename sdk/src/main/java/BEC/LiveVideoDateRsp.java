package BEC;

public final class LiveVideoDateRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vDate = null;

    public java.util.ArrayList<String> getVDate()
    {
        return vDate;
    }

    public void  setVDate(java.util.ArrayList<String> vDate)
    {
        this.vDate = vDate;
    }

    public LiveVideoDateRsp()
    {
    }

    public LiveVideoDateRsp(java.util.ArrayList<String> vDate)
    {
        this.vDate = vDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vDate) {
            ostream.writeList(0, vDate);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VDATE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDATE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vDate = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VDATE);
    }

}

