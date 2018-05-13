package BEC;

public final class PlateStockListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public byte [] vGuid = null;

    public String sDtSecCode = "";

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public PlateStockListReq()
    {
    }

    public PlateStockListReq(byte [] vGuid, String sDtSecCode)
    {
        this.vGuid = vGuid;
        this.sDtSecCode = sDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vGuid) {
            ostream.writeBytes(0, vGuid);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vGuid = (byte [])istream.readBytes(0, false, this.vGuid);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
    }

}

