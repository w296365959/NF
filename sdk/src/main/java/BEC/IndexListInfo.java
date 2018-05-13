package BEC;

public final class IndexListInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.IndexDetail> vDtSecCode = null;

    public java.util.ArrayList<BEC.IndexDetail> getVDtSecCode()
    {
        return vDtSecCode;
    }

    public void  setVDtSecCode(java.util.ArrayList<BEC.IndexDetail> vDtSecCode)
    {
        this.vDtSecCode = vDtSecCode;
    }

    public IndexListInfo()
    {
    }

    public IndexListInfo(java.util.ArrayList<BEC.IndexDetail> vDtSecCode)
    {
        this.vDtSecCode = vDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vDtSecCode) {
            ostream.writeList(0, vDtSecCode);
        }
    }

    static java.util.ArrayList<BEC.IndexDetail> VAR_TYPE_4_VDTSECCODE = new java.util.ArrayList<BEC.IndexDetail>();
    static {
        VAR_TYPE_4_VDTSECCODE.add(new BEC.IndexDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vDtSecCode = (java.util.ArrayList<BEC.IndexDetail>)istream.readList(0, false, VAR_TYPE_4_VDTSECCODE);
    }

}

