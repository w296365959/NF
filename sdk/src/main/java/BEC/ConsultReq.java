package BEC;

public final class ConsultReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtDtSecCode = null;

    public byte [] vGuid = null;

    public String sAppId = "";

    public java.util.ArrayList<String> getVtDtSecCode()
    {
        return vtDtSecCode;
    }

    public void  setVtDtSecCode(java.util.ArrayList<String> vtDtSecCode)
    {
        this.vtDtSecCode = vtDtSecCode;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public String getSAppId()
    {
        return sAppId;
    }

    public void  setSAppId(String sAppId)
    {
        this.sAppId = sAppId;
    }

    public ConsultReq()
    {
    }

    public ConsultReq(java.util.ArrayList<String> vtDtSecCode, byte [] vGuid, String sAppId)
    {
        this.vtDtSecCode = vtDtSecCode;
        this.vGuid = vGuid;
        this.sAppId = sAppId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtDtSecCode) {
            ostream.writeList(0, vtDtSecCode);
        }
        if (null != vGuid) {
            ostream.writeBytes(1, vGuid);
        }
        if (null != sAppId) {
            ostream.writeString(2, sAppId);
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
        this.vGuid = (byte [])istream.readBytes(1, false, this.vGuid);
        this.sAppId = (String)istream.readString(2, false, this.sAppId);
    }

}

