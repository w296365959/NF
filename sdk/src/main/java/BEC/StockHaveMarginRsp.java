package BEC;

public final class StockHaveMarginRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtDtSecCode = null;

    public java.util.ArrayList<String> getVtDtSecCode()
    {
        return vtDtSecCode;
    }

    public void  setVtDtSecCode(java.util.ArrayList<String> vtDtSecCode)
    {
        this.vtDtSecCode = vtDtSecCode;
    }

    public StockHaveMarginRsp()
    {
    }

    public StockHaveMarginRsp(java.util.ArrayList<String> vtDtSecCode)
    {
        this.vtDtSecCode = vtDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtDtSecCode) {
            ostream.writeList(0, vtDtSecCode);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTDTSECCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTDTSECCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtDtSecCode = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VTDTSECCODE);
    }

}

