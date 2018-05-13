package BEC;

public final class QuoteReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vDtSecCode = null;

    public byte [] vGuid = null;

    public java.util.ArrayList<String> getVDtSecCode()
    {
        return vDtSecCode;
    }

    public void  setVDtSecCode(java.util.ArrayList<String> vDtSecCode)
    {
        this.vDtSecCode = vDtSecCode;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public QuoteReq()
    {
    }

    public QuoteReq(java.util.ArrayList<String> vDtSecCode, byte [] vGuid)
    {
        this.vDtSecCode = vDtSecCode;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vDtSecCode) {
            ostream.writeList(0, vDtSecCode);
        }
        if (null != vGuid) {
            ostream.writeBytes(1, vGuid);
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
        this.vGuid = (byte [])istream.readBytes(1, false, this.vGuid);
    }

}

