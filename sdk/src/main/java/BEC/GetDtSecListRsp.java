package BEC;

public final class GetDtSecListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vDtSecCode = null;

    public java.util.ArrayList<String> getVDtSecCode()
    {
        return vDtSecCode;
    }

    public void  setVDtSecCode(java.util.ArrayList<String> vDtSecCode)
    {
        this.vDtSecCode = vDtSecCode;
    }

    public GetDtSecListRsp()
    {
    }

    public GetDtSecListRsp(java.util.ArrayList<String> vDtSecCode)
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

    static java.util.ArrayList<String> VAR_TYPE_4_VDTSECCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDTSECCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vDtSecCode = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VDTSECCODE);
    }

}

