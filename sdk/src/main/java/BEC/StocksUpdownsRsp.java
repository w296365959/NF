package BEC;

public final class StocksUpdownsRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtSecUpdowns = null;

    public java.util.ArrayList<String> getVtSecUpdowns()
    {
        return vtSecUpdowns;
    }

    public void  setVtSecUpdowns(java.util.ArrayList<String> vtSecUpdowns)
    {
        this.vtSecUpdowns = vtSecUpdowns;
    }

    public StocksUpdownsRsp()
    {
    }

    public StocksUpdownsRsp(java.util.ArrayList<String> vtSecUpdowns)
    {
        this.vtSecUpdowns = vtSecUpdowns;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSecUpdowns) {
            ostream.writeList(0, vtSecUpdowns);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTSECUPDOWNS = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTSECUPDOWNS.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSecUpdowns = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VTSECUPDOWNS);
    }

}

