package BEC;

public final class QuoteSimpleReq extends com.dengtacj.thoth.Message implements Cloneable
{
    public java.util.ArrayList<SecCode> vSecCode = null;

    public byte [] vGuid = null;

    public java.util.ArrayList<SecCode> getVSecCode()
    {
        return vSecCode;
    }

    public void  setVSecCode(java.util.ArrayList<SecCode> vSecCode)
    {
        this.vSecCode = vSecCode;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public QuoteSimpleReq()
    {
    }

    public QuoteSimpleReq(java.util.ArrayList<SecCode> vSecCode, byte [] vGuid)
    {
        this.vSecCode = vSecCode;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecCode)
        {
            ostream.writeList(0, vSecCode);
        }
        if (null != vGuid)
        {
            ostream.writeBytes(1, vGuid);
        }
    }

    static java.util.ArrayList<SecCode> VAR_TYPE_4_VSECCODE = new java.util.ArrayList<SecCode>();
    static {
        VAR_TYPE_4_VSECCODE.add(new SecCode());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecCode = (java.util.ArrayList<SecCode>)istream.readList(0, false, VAR_TYPE_4_VSECCODE);
        this.vGuid = (byte [])istream.readBytes(1, false, this.vGuid);
    }

}

